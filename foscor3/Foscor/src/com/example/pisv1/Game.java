package com.example.pisv1;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.debug.Debug;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.view.KeyEvent;
import android.widget.Toast;

public class Game extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 533;
	private static final int CAMERA_HEIGHT = 320;
	private static final int DIALOG_ALLOWDIAGONAL_ID = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	protected BoundCamera mBoundChaseCamera;
	protected Scene mMainScene;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;
	
	private BitmapTextureAtlas mBitmapTextureAtlas2;
	private TiledTextureRegion mPlayerTextureRegion2;
	
	
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;

	private volatile Map mMap;
	protected int directionX = 0;
	protected int directionY = 0;
	private String mapa ="tmx/desert.tmx";
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private boolean write =true;
	
	private Inventory menu;
	protected boolean bMenu=false;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {

		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menu= new Inventory(this);

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 72, 128, TextureOptions.DEFAULT);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "prota.png", 0, 0, 3, 4);
		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mBitmapTextureAtlas.load();
		
		this.mBitmapTextureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 72, 128, TextureOptions.DEFAULT);
		this.mPlayerTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "anciano.png", 0, 0, 3, 4);
		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mBitmapTextureAtlas2.load();
		
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();
		itemTest();
	}

	@Override
	public Scene onCreateScene() {

		this.mEngine.registerUpdateHandler(new FPSLogger());

		mMainScene = new Scene();

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH - this.mPlayerTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mPlayerTextureRegion.getHeight()) / 2;

		/* Create the sprite and add it to the scene. */
		final AnimatedSprite anciano = new AnimatedSprite(centerX-50, centerY,  this.mPlayerTextureRegion2 , this.getVertexBufferObjectManager());
		final AnimatedSprite player = new AnimatedSprite(centerX, centerY,  this.mPlayerTextureRegion, this.getVertexBufferObjectManager());
		final PhysicsHandler physicsHandler = new PhysicsHandler(player);
		player.registerUpdateHandler(physicsHandler);
		this.mBoundChaseCamera.setChaseEntity(player);

	
		this.mMap=new Map(mapa,this,mMainScene, this.mBoundChaseCamera,player);

		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), 
				this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, 
				this.getVertexBufferObjectManager(), new IOnScreenControlListener() {
			
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl,  float pValueX,  float pValueY) {
				float[] playerFootCordinates = player.convertLocalToSceneCoordinates(12, 31);

				if ( pValueX > 0){
                    // X = 1 => Right
					if (directionX!= 1){
	                	player.animate(new long[]{200, 200, 200}, 6, 8, true);
	                	directionX = 1;
					}
            	}
                else if ( pValueX < 0){
                    // X = -1 => Left
					if (directionX!= -1){
	                	player.animate(new long[]{200, 200, 200}, 3, 5, true);
	                	directionX = -1;
					}
            	}
                else directionX = 0;
                
                if ( pValueY > 0){
                    // Y = 1 => Down
                	if (directionY!= 1){
	            		player.animate(new long[]{200, 200, 200}, 0, 2, true);
	                	directionY = 1;
					}
            	}
                else if ( pValueY < 0){
                    // Y = -1 => Up
                	if (directionY!= -1){
						player.animate(new long[]{200, 200, 200}, 9, 11, true);
	                	directionY = -1;
					}
            	}
                else directionY = 0;
				
				if( pValueX == 0 && pValueY == 0){
            		player.stopAnimation();
            		directionX = 0;
            		directionY = 0;	
    				
            	}
				/* Get the scene-coordinates of the players feet. */
				for (int layerID = 0; layerID < mMap.getmTMXTiledMap().getTMXLayers().size(); layerID++) {/* Get the tile the feet of the player are currently waking on. */
					final TMXProperties <TMXTileProperty> pTMXTileProperties = mMap.getmTMXTiledMap().getTMXLayers().get(layerID).getTMXTileAt(
							playerFootCordinates[Constants.VERTEX_INDEX_X], playerFootCordinates[Constants.VERTEX_INDEX_Y]).getTMXTileProperties(mMap.getmTMXTiledMap());
					if(pTMXTileProperties != null) {
						if(pTMXTileProperties.containsTMXProperty("cactus", "true")) {
							
							Toast("Te has pinchado!");
						
						}
					}
				
					// tmxTile.setTextureRegion(null); <-- Rubber-style removing of tiles =D
				}
				
            	TMXProperties <TMXTileProperty> pTMXTileProperties = mMap.getmTMXTiledMap().getTMXLayers().get(1).getTMXTileAt(
            	playerFootCordinates[Constants.VERTEX_INDEX_X]+10*directionX, playerFootCordinates[Constants.VERTEX_INDEX_Y]+10*directionY).getTMXTileProperties(mMap.getmTMXTiledMap());
				if(pTMXTileProperties != null) {
					if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
						pValueX=0;	
						pValueY=0;
					}
				}
				
				if(player.collidesWith(anciano)){
					if(write){
						mMainScene.clearChildScene();
					
					Toast("Swil, me alegro que estés bien, tengo algo para ti, ");
					Toast("yo ya soy muy viejo y mis heridas...");
					Toast("No soy lo bastante fuerte... Creo que ya poco me queda...");
					Toast("¡Mirame! Escucha atentamente, es importante que ");
					Toast("llegues a Balmont y le entregues esto a Reese, ");
					Toast("tranquilo él sabrá qué hacer, siempre sabe qué hacer...");
					
					write=false;
					mMainScene.setChildScene(Game.this.mDigitalOnScreenControl);
					}
					
				}
				pTMXTileProperties = mMap.getmTMXTiledMap().getTMXLayers().get(1).getTMXTileAt(
            	playerFootCordinates[Constants.VERTEX_INDEX_X]+10*directionX, playerFootCordinates[Constants.VERTEX_INDEX_Y]+10*directionY).getTMXTileProperties(mMap.getmTMXTiledMap());
				if(pTMXTileProperties != null) {
					if(pTMXTileProperties.get(0).getName().equals("pass")) {
						if(!(write)){
						mapa=pTMXTileProperties.get(0).getValue();
						mMainScene.detachChild(player);
						Game.this.mMap=new Map(mapa,Game.this,mMainScene, Game.this.mBoundChaseCamera,player);
						mMainScene.attachChild(player);
						playerFootCordinates = mMainScene.convertLocalToSceneCoordinates(Integer.valueOf(pTMXTileProperties.get(1).getValue())*32,Integer.valueOf(pTMXTileProperties.get(2).getValue())*32);
						player.setPosition(playerFootCordinates[Constants.VERTEX_INDEX_X], playerFootCordinates[Constants.VERTEX_INDEX_Y]);
						}else{
							
							Toast("Debería mirar si alguien más ha escapado antes de continuar.");
							
						}
					}
				}
				physicsHandler.setVelocity(pValueX * 100, pValueY * 100); 
				
		}
			
		});
		
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);
		this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl.getControlBase().setScale(0.9f);
		this.mDigitalOnScreenControl.getControlKnob().setScale(0.9f);
		this.mDigitalOnScreenControl.refreshControlKnobPosition();
		//this.mDigitalOnScreenControl.setAllowDiagonal(true);
		mMainScene.setChildScene(this.mDigitalOnScreenControl);
		mMainScene.attachChild(player);
		mMainScene.attachChild(anciano);
		menu.startMenu();
		return mMainScene;
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(bMenu) {
				/* Remove the menu and reset it. */
				menu.getScene().back();
				bMenu=false;
				mMainScene.setChildScene(this.mDigitalOnScreenControl);

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
	            this.finish();
	        }
	    }
	
	public void Toast(String string) {
		toastOnUIThread(string,1);
		
	}
	
	public void itemTest(){
		menu.addItem("greataxe8hv.png", "axe1", 3, 0, "Weapon");
		menu.addItem("emeraldring4om.png", "ring1", 0, 2, "Accessory");
		menu.addItem("greataxe8hv.png", "axe2", 3, 2, "Weapon");
		menu.addItem("emeraldring4om.png", "ring2", 2, 2, "Accessory");
	}
	/*@Override
	public void onGameCreated() {
		this.showDialog(DIALOG_ALLOWDIAGONAL_ID);
	}*/

	public static int getCameraHeight() {
		return CAMERA_HEIGHT;
	}

	public static int getCameraWidth() {
		return CAMERA_WIDTH;
	}
	
	/*@Override
	public void onGameCreated() {
		this.showDialog(DIALOG_ALLOWDIAGONAL_ID);
	}*/

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

