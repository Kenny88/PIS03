package com.example.pisv1;

import java.util.Vector;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
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
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.opengl.GLES20;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Toast;

public class Game extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private static final int DIALOG_ALLOWDIAGONAL_ID = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	protected BoundCamera mBoundChaseCamera;
	protected Scene mMainScene;
	
	private HUD mHud;
	
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	protected volatile Map mMap;
	String mapaName ="tmx/desert.tmx";
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private boolean write =true;
	
	private BitmapTextureAtlas mInventoryMenuButtonTexture;
	private ITextureRegion mInventoryMenuButtonTextureRegion;
	private ButtonSprite mInventoryMenuButton;
	
	protected Inventory menu;
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

		Point point= new Point();
		Display display= this.getWindowManager().getDefaultDisplay();
		display.getSize(point);
		CAMERA_WIDTH = (point.x*3) / 5;
		CAMERA_HEIGHT = (point.y*3) / 5;
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
		this.mOnScreenControlTexture.load();
		
		mInventoryMenuButtonTexture = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.NEAREST);
		mInventoryMenuButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mInventoryMenuButtonTexture, this, "button-backpack-up.png", 0, 0);
		mInventoryMenuButtonTexture.load();
		
		itemTest();
	}

	@Override
	public Scene onCreateScene() {

		this.mEngine.registerUpdateHandler(new FPSLogger());

		mMainScene = new Scene();

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (CAMERA_WIDTH) / 2;
		final float centerY = (CAMERA_HEIGHT) / 2;

		/* Create the sprite and add it to the scene. */
		Character anci=new Character(centerX-40,centerY,"anciano.png",this);
		final AnimatedSprite anciano = anci.getAnimatedSprite();
		Player player =new Player(centerX,centerY,"prota.png",this);
		this.mBoundChaseCamera.setChaseEntity(player.getAnimatedSprite());
		this.mMap=new Map(mapaName,this,player);
		
		mMainScene.attachChild(mMap.getMapScene());
		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), 
				this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, 
				this.getVertexBufferObjectManager(), player.getIOnScreenControlListener());
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);
		this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl.getControlBase().setScale(1.0f);
		this.mDigitalOnScreenControl.getControlKnob().setScale(1.0f);
		this.mDigitalOnScreenControl.refreshControlKnobPosition();
		//this.mDigitalOnScreenControl.setAllowDiagonal(true);
		mMainScene.setChildScene(this.mDigitalOnScreenControl);
		
		mMap.getMapScene().attachChild(anciano);
		
		menu.startMenu();
		
		/* Inventory Menu Button */
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
						mMainScene.setChildScene(Game.this.mDigitalOnScreenControl);
					}
					else
					{
						bMenu=true;
						
						Game.this.mMainScene.setChildScene(Game.this.menu.getScene(), false, true, true);
					}
				}
				
				return super.onAreaTouched(event, x, y);
			}
		};
		mHud.registerTouchArea(mInventoryMenuButton);
		mHud.attachChild(mInventoryMenuButton);
		
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

