package com.example.pisv1;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.opengl.GLES20;

public class InGameMenu implements IOnMenuItemClickListener {

	protected MenuScene mMenuScene;
	private Game app;
	protected Font mFont;
	
	protected static final int QUIT=0;
	protected static final int INVENTORY=1;
	
	public InGameMenu(Game application){
		app=application;
	}
	
	public void startMenu(){
		this.mFont = FontFactory.create(( app).getFontManager(), ( app).getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
		
		this.mMenuScene = new MenuScene(( app).mBoundChaseCamera);

		final IMenuItem inventoryMenuItem = new ColorMenuItemDecorator(new TextMenuItem(INVENTORY, this.mFont,app.getResources().getString(R.string.inventory),( app).getVertexBufferObjectManager()), new Color(1,0,0), new Color(0,0,0));
		inventoryMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(inventoryMenuItem);
		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(QUIT, this.mFont,app.getResources().getString(R.string.quit), ( app).getVertexBufferObjectManager()), new Color(1,0,0), new Color(0,0,0));
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
        	new AlertDialog.Builder(app)
        .setIcon(R.drawable.ic_launcher)
        .setTitle(R.string.Exit)
        .setMessage(R.string.ExitQuestion)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
			mMenuScene.back();
			app.bMenu=false;
			app.finish();

        }

    })
    .setNegativeButton("No", null)
    .show();
			return true;
		default:
			return false;
	}
	}

}
