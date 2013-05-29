package com.example.pisv1;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Character {
	protected int cAttack=2;
	protected int cDefense=1;
	protected int cMagic=1;
	protected int cVida=100;
	
	protected AnimatedSprite mAnimatedSprite;
	protected Body mBody;
	protected Game app;
	protected int direction;	// 0 - Up, 1 - Right, 2 - Down, 3 - left (Sentit horari)
	protected AtaqueMelee ataqueMelee;
	protected boolean attack=true;
	protected boolean move=true;
	protected boolean ranged=false;
	protected AtaqueMagicoRanged ataqueMagicoRanged;
	public Character(float x, float y, String image,Game app1){
		this.app=app1;
		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(app.getTextureManager(), 72, 128, TextureOptions.DEFAULT);
		TiledTextureRegion mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, app,image, 0, 0, 3, 4);
		mBitmapTextureAtlas.load();
		mAnimatedSprite=new AnimatedSprite(x,y,mPlayerTextureRegion,app.getVertexBufferObjectManager());
		direction = 2;	//El personatge comença mirant cap abaix
	}
	public void addToPhysicsWorld(PhysicsWorld mPhysicsWorld,FixtureDef fixtureDef){
   		mBody=PhysicsFactory.createBoxBody(mPhysicsWorld, mAnimatedSprite, BodyType.DynamicBody, fixtureDef);
   		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mAnimatedSprite, mBody, true, false));
   		mBody.setUserData(this);
	}
	public AnimatedSprite getAnimatedSprite() {
		return mAnimatedSprite;
	}
	public void setAnimatedSprite(AnimatedSprite mAnimatedSprite) {
		this.mAnimatedSprite = mAnimatedSprite;
	}

	public void registerUpdateHandler(PhysicsWorld mPhysicsWorld) {
		mAnimatedSprite.registerUpdateHandler(mPhysicsWorld);
		
	}

	public void setPosition(float x, float y) {
		mBody.setTransform(x,y, 0);
		}
	public void setVelocity(float pValueX,float pValueY) {
		mBody.setLinearVelocity(pValueX*2, pValueY*2); 
		
	}
	protected float[] convertLocalToSceneCoordinates(int i, int j) {
		return mAnimatedSprite.convertLocalToSceneCoordinates(i,j);
	}

	public void detach(final PhysicsWorld mPhysicsWorld) {
		getAnimatedSprite().registerUpdateHandler(new IUpdateHandler(){

			@Override
			public void onUpdate(float arg0) {
				// TODO Auto-generated method stub
				mPhysicsWorld.destroyBody(mBody);
				mAnimatedSprite.clearUpdateHandlers();
				mAnimatedSprite.detachSelf();
	
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}


	public void unregisterUpdateHandler(PhysicsWorld mPhysicsWorld) {
		mAnimatedSprite.unregisterUpdateHandler(mPhysicsWorld);
	}

	protected void move(float pValueX, float pValueY) {
		if (move){
			if ( pValueX > 0){
		        // X = 1 => Right
				if (direction!= 1){
		        	mAnimatedSprite.animate(new long[]{200, 200, 200}, 6, 8, true);
		        	direction = 1;
				}
			}
		    else if ( pValueX < 0){
		        // X = -1 => Left
				if (direction!= 3){
					mAnimatedSprite.animate(new long[]{200, 200, 200}, 3, 5, true);
		        	direction = 3;
				}
			}
		    
		    if ( pValueY > 0){
		        // Y = 1 => Down
		    	if (direction!=2){
		    		mAnimatedSprite.animate(new long[]{200, 200, 200}, 0, 2, true);
		        	direction = 2;
				}
			}
		    else if ( pValueY < 0){
		        // Y = -1 => Up
		    	if (direction!= 0){
					mAnimatedSprite.animate(new long[]{200, 200, 200}, 9, 11, true);
		        	direction = 0;
				}
			}
			if( pValueX == 0 && pValueY == 0){
				mAnimatedSprite.stopAnimation();			
			}
			setVelocity(pValueX, pValueY); 
		}
	}
	protected void attack(float pValueX, float pValueY) { 
			if(attack){
				if(pValueX > 0){
		        // X = 1 => Right
					attackRanged();
				}
			    else if ( pValueX < 0){
			        // X = -1 => Left
			    	attackMagicMelee();
				}else if ( pValueY > 0){
			        // Y = 1 => Down
					attackMelee();
				}
			    else if ( pValueY < 0){
			        // Y = -1 => Up
			    	attackMagicRanged();
				}
			}
			
		}
	protected void attackMagicRanged() {
		move(0,0);
		attack=false;
		final Map map=app.mMap;
		IAnimationListener listener=new IAnimationListener(){

			private int espera=0;

			@Override
			public void onAnimationFinished(AnimatedSprite arg0) {

				attack=true;
			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub
				/*espera++;
				if (espera>8){
					attack=true;
				}*/
			}

			@Override
			public void onAnimationStarted(AnimatedSprite arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		};
		ataqueMagicoRanged = new AtaqueMagicoRanged(mAnimatedSprite.getX(),mAnimatedSprite.getY(), direction,3,(cAttack*cMagic)/2,listener, this, app);
	}
	protected void attackMelee() {
		move(0,0);
		attack=false;
		final Map map=app.mMap;
		IAnimationListener listener=new IAnimationListener(){

			@Override
			public void onAnimationFinished(AnimatedSprite arg0) {
								
			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub
				attack=true;
				move=true;
				ataqueMelee.detach(map.getmPhysicsWorld());
			}

			@Override
			public void onAnimationStarted(AnimatedSprite arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		};
		ataqueMelee = new AtaqueMelee(mAnimatedSprite.getX(),mAnimatedSprite.getY(), direction, cAttack,listener, this, app);
		
	}
	protected void attackMagicMelee() {
		// TODO Auto-generated method stub
		
	}
	protected void attackRanged() {
		// TODO Auto-generated method stub
		
	}
	public String toString(){
		return "enemy";
	}
	public void restVida(float f) {
		cVida-=f;
		if (cVida<=0){
			detach(app.mMap.getmPhysicsWorld());
		}
	}
}
