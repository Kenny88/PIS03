package com.example.pisv1;

import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.util.Constants;

public class Player extends Character {
	private IOnScreenControlListener mIOnScreenControlListener;
	public Player(float x, float y, String image,final Game app) {
		super(x, y, image, app);
		// TODO Auto-generated constructor stub
		mIOnScreenControlListener=new IOnScreenControlListener() {
	
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl,  float pValueX,  float pValueY) {
		float[] playerFootCordinates = Player.this.convertLocalToSceneCoordinates(12, 31);

		if ( pValueX > 0){
            // X = 1 => Right
			if (directionX!= 1){
            	mAnimatedSprite.animate(new long[]{200, 200, 200}, 6, 8, true);
            	directionX = 1;
			}
    	}
        else if ( pValueX < 0){
            // X = -1 => Left
			if (directionX!= -1){
				mAnimatedSprite.animate(new long[]{200, 200, 200}, 3, 5, true);
            	directionX = -1;
			}
    	}
        else directionX = 0;
        
        if ( pValueY > 0){
            // Y = 1 => Down
        	if (directionY!= 1){
        		mAnimatedSprite.animate(new long[]{200, 200, 200}, 0, 2, true);
            	directionY = 1;
			}
    	}
        else if ( pValueY < 0){
            // Y = -1 => Up
        	if (directionY!= -1){
				mAnimatedSprite.animate(new long[]{200, 200, 200}, 9, 11, true);
            	directionY = -1;
			}
    	}
        else directionY = 0;
		
		if( pValueX == 0 && pValueY == 0){
			mAnimatedSprite.stopAnimation();
    		directionX = 0;
    		directionY = 0;	
			
    	}
//		if(mAnimatedSprite.collidesWith(anciano)){
//			if(write){
//				mMainScene.clearChildScene();
//			
//			Toast("Swil, me alegro que estés bien, tengo algo para ti, ");
//			Toast("yo ya soy muy viejo y mis heridas...");
//			Toast("No soy lo bastante fuerte... Creo que ya poco me queda...");
//			Toast("¡Mirame! Escucha atentamente, es importante que ");
//			Toast("llegues a Balmont y le entregues esto a Reese, ");
//			Toast("tranquilo él sabrá qué hacer, siempre sabe qué hacer...");
//			
//			write=false;
//			mMainScene.setChildScene(Game.this.mDigitalOnScreenControl);
//			}
//		}
		/*
		for (int layerID = 0; layerID < mMap.getmTMXTiledMap().getTMXLayers().size(); layerID++) {
			final TMXProperties <TMXTileProperty> pTMXTileProperties = mMap.getmTMXTiledMap().getTMXLayers().get(layerID).getTMXTileAt(
					playerFootCordinates[Constants.VERTEX_INDEX_X], playerFootCordinates[Constants.VERTEX_INDEX_Y]).getTMXTileProperties(mMap.getmTMXTiledMap());
			if(pTMXTileProperties != null) {
				if(pTMXTileProperties.containsTMXProperty("cactus", "true")) {
					
					Toast("Te has pinchado!");
				
				}
			}
		
			// tmxTile.setTextureRegion(null); <-- Rubber-style removing of tiles =D
		}*/
		TMXTile tile=app.mMap.getmTMXTiledMap().getTMXLayers().get(1).getTMXTileAt(playerFootCordinates[Constants.VERTEX_INDEX_X]+10*directionX, playerFootCordinates[Constants.VERTEX_INDEX_Y]+10*directionY);
		TMXProperties <TMXTileProperty> pTMXTileProperties =null;
		if (tile.getGlobalTileID()!=0){
    		pTMXTileProperties =tile.getTMXTileProperties(app.mMap.getmTMXTiledMap());
    	}
//		if(pTMXTileProperties != null) {
//			if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
//				pValueX=0;	
//				pValueY=0;
//			}
//		}
		if(pTMXTileProperties != null) {
			if(pTMXTileProperties.get(0).getName().equals("pass")) {
				app.mapaName=pTMXTileProperties.get(0).getValue();
				
				app.mMainScene.detachChild(app.mMap.getMapScene());
				app.mMap.delete();
				app.mMap=new Map(app.mapaName,app,Player.this,Integer.valueOf(pTMXTileProperties.get(1).getValue()),Integer.valueOf(pTMXTileProperties.get(2).getValue()));
				app.mMainScene.attachChild(app.mMap.getMapScene());
				pValueX=0;
				pValueY=0;
					
//					Toast("Debería mirar si alguien más ha escapado antes de continuar.");
					
			}
				}
		mBody.setLinearVelocity(pValueX*2, pValueY*2); 
		
}
	
};
	}

	protected float[] convertLocalToSceneCoordinates(int i, int j) {
		// TODO Auto-generated method stub
		return mAnimatedSprite.convertLocalToSceneCoordinates(i,j);
	}

	public IOnScreenControlListener getIOnScreenControlListener() {
		// TODO Auto-generated method stub
		return mIOnScreenControlListener;
	}


	
}
