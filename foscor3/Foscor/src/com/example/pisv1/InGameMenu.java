package com.example.pisv1;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.opengl.GLES20;
import android.view.KeyEvent;

public class InGameMenu implements IOnMenuItemClickListener {

	protected MenuScene mMenuScene;
	private SimpleBaseGameActivity app;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;
	protected Font mFont;
	
	protected static final int QUIT=0;
	protected static final int INVENTORY=1;
	
	public InGameMenu(SimpleBaseGameActivity application){
		app=application;
	}
	
	public void startMenu(){
		this.mFont = FontFactory.create(((Game) app).getFontManager(), ((Game) app).getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
		
		this.mMenuScene = new MenuScene(((Game) app).mBoundChaseCamera);

		final IMenuItem inventoryMenuItem = new ColorMenuItemDecorator(new TextMenuItem(INVENTORY, this.mFont, "Inventory", ((Game) app).getVertexBufferObjectManager()), new Color(1,0,0), new Color(0,0,0));
		inventoryMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(inventoryMenuItem);
		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(QUIT, this.mFont, "Quit", ((Game) app).getVertexBufferObjectManager()), new Color(1,0,0), new Color(0,0,0));
		quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(quitMenuItem);


		this.mMenuScene.buildAnimations();

		this.mMenuScene.setBackgroundEnabled(false);

		this.mMenuScene.setOnMenuItemClickListener(this);
		System.out.print("Menu creat");
	}
	public MenuScene getScene(){
		return mMenuScene;
	}


	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
		case QUIT:
			mMenuScene.back();
			((Game) app).bMenu=false;
			((Game) app).finish();

			return true;
		default:
			return false;
	}
	}

}
