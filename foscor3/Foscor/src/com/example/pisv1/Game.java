package com.example.pisv1;

import java.io.Serializable;
import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;

public class Game extends SimpleBaseGameActivity implements Serializable {
	// ===========================================================
	// Constants
	// ===========================================================
	
	
	/* The categories. */
	public static final short CATEGORYBIT_WALL = 1;
	public static final short CATEGORYBIT_CHARACTER = 2;
	public static final short CATEGORYBIT_ZERO = 4;

	/* And what should collide with what. */
	public static final short MASKBITS_WALL = CATEGORYBIT_WALL + CATEGORYBIT_CHARACTER+CATEGORYBIT_ZERO;
	public static final short MASKBITS_CHARACTER = CATEGORYBIT_WALL + CATEGORYBIT_CHARACTER+CATEGORYBIT_ZERO; // Missing: CATEGORYBIT_CIRCLE
	public static final short MASKBITS_ZERO= CATEGORYBIT_WALL + CATEGORYBIT_CHARACTER; // Missing: CATEGORYBIT_BOX

	public final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(10.0f, 0.5f, 1.0f, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
	public final FixtureDef CHARACTER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1.0f, 1.0f, 1.0f, false, CATEGORYBIT_CHARACTER, MASKBITS_CHARACTER, (short)0);
	public final FixtureDef ZERO_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f, true, CATEGORYBIT_ZERO, MASKBITS_ZERO, (short)0);
	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private static final int DIALOG_ALLOWDIAGONAL_ID = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	protected BoundCamera mBoundChaseCamera;
	protected Scene mMainScene;
	protected Scene mVisualScene;

	private HUD mHud;
	
	// MENU BUTTON
	private BitmapTextureAtlas mInventoryMenuButtonTexture;
	private ITextureRegion mInventoryMenuButtonTextureRegion;
	private ButtonSprite mInventoryMenuButton;
	private ArrayList<Ataque> atacs= new ArrayList<Ataque>();
	
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	protected volatile Map mMap;
	protected volatile Player mPlayer;
	String mapaName ="tmx/bosque1.tmx";
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private DigitalOnScreenControl mDigitalOnScreenControl2;
	Maps mapas=new Maps();	String message1="";
	int message2=0;
	private final Handler handler = new Handler() {
        @Override
		public void handleMessage(Message msg) {
              if(msg.arg1 == 1){
              Toast.makeText(getApplicationContext(),message1, Toast.LENGTH_LONG).show();
              }
              if(msg.arg1 == 2){
              Toast.makeText(getApplicationContext(),message2, Toast.LENGTH_LONG).show();
              }
        }
    };
	protected Inventory menu;
	protected boolean bMenu=false;
	// ===========================================================
	// Constructors
	// ===========================================================
	private SpriteBackground cargando;
	private BitmapTextureAtlas mOnScreenControlTexture2;
	private TextureRegion mOnScreenControlBaseTextureRegion2;
	private TextureRegion mOnScreenControlKnobTextureRegion2;
	public ContactListener listener=new ContactListener(){

		@Override
		public void beginContact(Contact arg0) {
			mMainScene.setIgnoreUpdate(true);
//			Log.d("fixtureA", arg0.getFixtureA().getBody().getUserData().toString());
//			Log.d("fixtureB", arg0.getFixtureB().getBody().getUserData().toString());
			java.lang.Object fixtureA=arg0.getFixtureA().getBody().getUserData();
			java.lang.Object fixtureB=arg0.getFixtureB().getBody().getUserData(); 
			String A=fixtureA.toString();
			String B=fixtureB.toString();
			if(A.equals("player")){
					if(B.equals("object")){
						MapItem object=(MapItem) fixtureB;
						menu.addItem(object.getItem());
						object.detach(mMap.getmPhysicsWorld());
					}else if(B.equals("key")){
						Key key=(Key) fixtureB;
		  		    	  Door puerta=mapas.get(key.map).getDoors().get(key.door);
	         		    	if(key.abrir()&&!puerta.isAbierta()){
		           				puerta.abrir();
		           		    	key.detach(mMap.getmPhysicsWorld());
	         		    	}
	         		    	else if(key.cerrar()&&puerta.isAbierta()){
		           				puerta.cerrar();
		           		    	key.detach(mMap.getmPhysicsWorld());
	         		    	}
					}else if(B.equals("door")){
						Door door=(Door) fixtureB; 
	       				door.pasar((Player)fixtureA);
					}else if(B.equals("attack")){
						Ataque ataque= (Ataque)fixtureB;
						if (!ataque.getCreator().toString().equals("player")){
							Character character=(Character)fixtureA;
							character.restVida(ataque.getDaño()/character.cDefense);
							if(ataque.getTypeAttack().equals("magicRanged")){
								ataque.detach(mMap.getmPhysicsWorld());
							}
						}
		       		}
			}else if(A.equals("object")){
						if(B.equals("player")){
								MapItem object=(MapItem) fixtureA;
								menu.addItem(object.getItem());
								object.detach(mMap.getmPhysicsWorld());										
						}
			}else if(A.equals("key")){
				if(B.equals("player")){
					Key key=(Key) fixtureA;
	  		    	  Door puerta=mapas.get(key.map).getDoors().get(key.door);
         		    	if(key.abrir()&&!puerta.isAbierta()){
	           				puerta.abrir();
	           		    	key.detach(mMap.getmPhysicsWorld());
         		    	}
         		    	else if(key.cerrar()&&puerta.isAbierta()){
	           				puerta.cerrar();
	           		    	key.detach(mMap.getmPhysicsWorld());
         		    	}
				}
			}else if(A.equals("door")){
				if(B.equals("player")){
					Door door=(Door) fixtureA; 
       				door.pasar((Player)fixtureB);				}
			}else if(A.equals("attack")){
				if(B.equals("enemy")){
					Ataque ataque= (Ataque)fixtureA;
					if (!ataque.getCreator().toString().equals("enemy")){
						Character character=(Character)fixtureB;
						character.restVida(ataque.getDaño()/character.cDefense);
						if(ataque.getTypeAttack().equals("magicRanged")){
							ataque.detach(mMap.getmPhysicsWorld());
						}
					}
				}else if(B.equals("player")){
					Ataque ataque= (Ataque)fixtureA;
					if (!ataque.getCreator().toString().equals("player")){
						Player character=(Player)fixtureB;
						character.restVida(ataque.getDaño()/character.cDefense);
					}
				}else if(B.equals("wall")){
					Ataque ataque= (Ataque)fixtureA;
					ataque.detach(mMap.getmPhysicsWorld());
				}
			}else if(A.equals("enemy")){
				if(B.equals("attack")){
					Ataque ataque= (Ataque)fixtureB;
					if (!ataque.getCreator().toString().equals("enemy")){
						Character character=(Character)fixtureA;
						character.restVida(ataque.getDaño()/character.cDefense);
						if(ataque.getTypeAttack().equals("magicRanged")){
							ataque.detach(mMap.getmPhysicsWorld());
						}
					}
				}
			}else if(A.equals("wall")){
				if(B.equals("attack")){
					Ataque ataque= (Ataque)fixtureB;
					if(ataque.getTypeAttack().equals("magicRanged")){
						ataque.detach(mMap.getmPhysicsWorld());
					}
				}
			}
			mMainScene.setIgnoreUpdate(false);
			
			
		}

		@Override
		public void endContact(Contact arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void postSolve(Contact arg0, ContactImpulse arg1) {
		}

		@Override
		public void preSolve(Contact arg0, Manifold arg1) {
			// TODO Auto-generated method stub
			
		}
		
	};
	private Rectangle mVida;
	private float vRed=0;
	private float vGreen=1;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {

		DisplayMetrics metrics= new DisplayMetrics();
		Display display= this.getWindowManager().getDefaultDisplay();
		display.getMetrics(metrics);
		CAMERA_WIDTH=(int) ((metrics.widthPixels)/metrics.xdpi*120);
		CAMERA_HEIGHT=(int) ((metrics.heightPixels)/metrics.ydpi*120);
		Toast( CAMERA_WIDTH+"   "+CAMERA_HEIGHT);
		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		EngineOptions engineOptions= new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera);

		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		mHud = new HUD();
		mBoundChaseCamera.setHUD(mHud);
		return engineOptions;
}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menu= new Inventory(this);
		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture2 = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture2, this, "botones_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture2, this, "botones_control_knob.png", 128, 0);

		mInventoryMenuButtonTexture = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.NEAREST);
		mInventoryMenuButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mInventoryMenuButtonTexture, this, "button-backpack-up.png", 0, 0);
		mInventoryMenuButtonTexture.load();
		
   		this.mOnScreenControlTexture.load();
   		this.mOnScreenControlTexture2.load();
		itemTest();
		
		
	}

	@Override
	public Scene onCreateScene() {

		this.mEngine.registerUpdateHandler(new FPSLogger());

		mMainScene = new Scene();
		mVisualScene=new Scene();
		BitmapTextureAtlas texture = new BitmapTextureAtlas(getTextureManager(), 1024, 768, TextureOptions.BILINEAR);
		ITextureRegion region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, this, "cargando.png", 0, 0);
		texture.load();
		mMainScene.setBackground(new SpriteBackground(new Sprite(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, region,getVertexBufferObjectManager() )));
		
		return mMainScene;
	}
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(bMenu) {
				/* Remove the menu and reset it. */
				menu.getScene().back();
				bMenu=false;
				mMainScene.setChildScene(mVisualScene);
			} else {
				/* Attach the menu. */
				bMenu=true;
				
				this.mMainScene.setChildScene(this.menu.getScene(), false, true, true);
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}
	
	 @Override
	public void onBackPressed()
	    {
	        if(bMenu){
	            bMenu=false;
		    	mMainScene.setChildScene(this.mDigitalOnScreenControl);
	        }
	        else{
	        	new AlertDialog.Builder(this)
	        .setIcon(R.drawable.ic_launcher)
	        .setTitle(R.string.Exit)
	        .setMessage(R.string.ExitQuestion)
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();    
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
	        }
	    }

		public void Toast(String string) {
			if(!message1.equals(string)){
				this.message1=string;
				Message msg = handler.obtainMessage();
				msg.arg1 = 1;
				handler.sendMessage(msg);
			}
			
		}
		public void Toast(int message) {
			if(message2!=message){
				this.message2=message;
				Message msg = handler.obtainMessage();
				msg.arg1 = 2;
				handler.sendMessage(msg);
			}
			
		}
	
	public void itemTest(){
		menu.addItem("greataxe8hv.png", "axe1", 3, 0, "Weapon");
		menu.addItem("emeraldring4om.png", "ring1", 0, 2, "Accessory");
		menu.addItem("greataxe8hv.png", "axe2", 3, 2, "Weapon");
		menu.addItem("emeraldring4om.png", "ring2", 2, 2, "Accessory");
		menu.addItem("greataxe8hv.png", "axe3", 0, 0, "Weapon");
		menu.addItem("emeraldring4om.png", "ring3", 0, 0, "Accessory");
	}
	/*@Override
	public void onGameCreated() {
		this.showDialog(DIALOG_ALLOWDIAGONAL_ID);
	}*/

	public int getCameraHeight() {
		return CAMERA_HEIGHT;
	}

	public int getCameraWidth() {
		return CAMERA_WIDTH;
	}
	@Override
	public Engine getEngine(){
		return mEngine;
	}

	public void changeMap(String name,final float x,final float y) {
		mapaName=name;
		mVisualScene.detachChild(mMap.getMapScene());
		mHud.setVisible(false);
		mVisualScene.setVisible(false);
		mPlayer.move(0, 0);
			mMainScene.registerUpdateHandler(new IUpdateHandler(){

				@Override
				public void onUpdate(float arg0) {
					mMap.deletePlayer(mPlayer);
					mMap=mapas.get(mapaName);
					mMap.addPlayer(mPlayer,x, y);
					mVisualScene.attachChild(mMap.getMapScene());
					mHud.setVisible(true);
					mVisualScene.setVisible(true);
					message1="";
					message2=0;
					mMainScene.unregisterUpdateHandler(this);
				}

				@Override
				public void reset() {
					// TODO Auto-generated method stub
					
				}
				
			});
		
	}
	
	@Override
	public void onGameCreated() {
		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH) / 2;
		final float centerY = (CAMERA_HEIGHT) / 2;

		/* Create the sprite and add it to the scene. */
		mPlayer =new Player(centerX,centerY,"prota.png",this);
		this.mBoundChaseCamera.setChaseEntity(mPlayer.getAnimatedSprite());
		this.mapas.setGame(this);
		this.mMap=mapas.get(mapaName);
		mMap.addPlayer(mPlayer);
		mVisualScene.attachChild(mMap.getMapScene());
		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), 
				this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, 
				this.getVertexBufferObjectManager(), mPlayer.getIOnScreenControlListener());
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.8f);
		this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl.getControlBase().setScale(1.1f);
		this.mDigitalOnScreenControl.getControlKnob().setScale(1.1f);
		this.mDigitalOnScreenControl.refreshControlKnobPosition();
		//this.mDigitalOnScreenControl.setAllowDiagonal(true);
		this.mDigitalOnScreenControl2 = new DigitalOnScreenControl(CAMERA_WIDTH - this.mOnScreenControlBaseTextureRegion2.getWidth()-24000/CAMERA_WIDTH, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion2.getHeight(), this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion2, this.mOnScreenControlKnobTextureRegion2, 0.1f, this.getVertexBufferObjectManager(),mPlayer.getBotonsControlListener());
		this.mDigitalOnScreenControl2.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl2.getControlBase().setAlpha(0.8f);
		this.mDigitalOnScreenControl2.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl2.getControlBase().setScale(1.3f);
		this.mDigitalOnScreenControl2.getControlKnob().setScale(1.3f);
		this.mDigitalOnScreenControl2.refreshControlKnobPosition();

		mVisualScene.setChildScene(mDigitalOnScreenControl);
		mDigitalOnScreenControl.setChildScene(mDigitalOnScreenControl2);
		mMainScene.setChildScene(mVisualScene);
		menu.startMenu();
		Rectangle vida=new Rectangle(0, 0, 104, 16, mEngine.getVertexBufferObjectManager());
		vida.setVisible(true);
		vida.setColor(new Color(0, 0, 0));
		mHud.attachChild(vida);
		mVida=new Rectangle(4, 4, 100, 8, mEngine.getVertexBufferObjectManager());
		mVida.setVisible(true);
		mVida.setColor(new Color(vRed, vGreen, 0));
		mHud.attachChild(mVida);
		
		mInventoryMenuButton = new ButtonSprite(16, 16, mInventoryMenuButtonTextureRegion, mEngine.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent event, float x, float y)
			{
				if ( event.isActionDown() )
				{
					if ( bMenu )
					{
						menu.getScene().back();
						bMenu=false;
						mMainScene.setChildScene(mVisualScene);
					}
					else
					{
						bMenu=true;
						
						Game.this.mMainScene.setChildScene(menu.getScene(), false, true, true);
					}
				}
				
				return super.onAreaTouched(event, x, y);
			}
		};
		mHud.registerTouchArea(mInventoryMenuButton);
		mHud.attachChild(mInventoryMenuButton);
		mMainScene.attachChild(mHud);


	}

	public void gameOver() {
		// TODO Auto-generated method stub
		
	}

	public void setVida(int cVida) {
		vGreen=cVida/100f;
		vRed=1-vGreen;
		Log.d("vida",Integer.toString(cVida));
		mVida.setColor(new Color(vRed, vGreen, 0));
		mVida.setWidth(cVida);
	}
	public void clearAttack(Ataque atac){
		atacs.add(atac);
		runOnUpdateThread(new Runnable() {

		    @Override
		    // to safely detach and re-attach the sprites
		    public void run() {
		    	Ataque atac;
				while(atacs.size()>0){
					atac=atacs.get(atacs.size()-1);
					atac.getAnimatedSprite().setVisible(false);
					atac.getAnimatedSprite().detachSelf();
					atac.getAnimatedSprite().clearUpdateHandlers();
					atac.getAnimatedSprite().clearEntityModifiers();
					mMap.getmPhysicsWorld().unregisterPhysicsConnector(mMap.getmPhysicsWorld().getPhysicsConnectorManager().findPhysicsConnectorByShape(atac.getAnimatedSprite()));
					mMap.getmPhysicsWorld().destroyBody(atac.getBody());
					atacs.remove(atac);
				}
			}


		});
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

