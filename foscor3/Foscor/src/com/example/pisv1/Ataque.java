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

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ataque {
	protected AnimatedSprite mAnimatedSprite;
	protected Body mBody;
	private Game app;
	protected int directionX = 0;
	protected int directionY = 0;
	private float daño;
	private Character creador;
	private float velocidad;
	protected String type;
	public Ataque(float x, float y,int direction,float velocidad,float daño,String image,IAnimationListener listener,Character creador,Game app1){
		this.app=app1;
		this.daño=daño;
		this.creador=creador;
		this.velocidad=velocidad;
		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(app.getTextureManager(), 96, 128, TextureOptions.DEFAULT);
		TiledTextureRegion mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, app,image, 0, 0, 3, 4);
		mBitmapTextureAtlas.load();
		int i=0,f=0;
		float vY=0,vX=0;
		if (direction == 1){	//(directionX== 1){
			x+=24;
			i=6;
			f=8;
			vX=+velocidad;
		}
		else if (direction == 3){	//(directionX== -1){
			x-=24;
			i=3;
			f=5;
			vX=-velocidad;
		}
		else if (direction == 2){	//(directionY== 1){
			y+=32;
			i=0;
			f=2;
			vY=+velocidad;
		}
	    else if (direction == 0){	//(directionY!= -1){
	    	y-=32;
			i=9;
			f=11;
			vY=-velocidad;
		}
		mAnimatedSprite=new AnimatedSprite(x,y,mPlayerTextureRegion,app.getVertexBufferObjectManager());
		app.mMap.getMapScene().attachChild(mAnimatedSprite);
		addToPhysicsWorld(app.mMap.getmPhysicsWorld(),app.ZERO_FIXTURE_DEF);

    	mAnimatedSprite.animate(new long[]{60, 60, 60}, i, f, true,listener);
    	setVelocity(vX,vY);
		
    	
	}
	public void addToPhysicsWorld(PhysicsWorld mPhysicsWorld,FixtureDef fixtureDef){
   		mBody=PhysicsFactory.createBoxBody(mPhysicsWorld, mAnimatedSprite, BodyType.DynamicBody, fixtureDef);
   		mBody.setUserData(this);
   		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mAnimatedSprite, mBody, true, false));
	}
	public AnimatedSprite getAnimatedSprite() {
		return mAnimatedSprite;
	}
	public Body getBody(){
		return mBody;
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
		app.clearAttack(this);
		creador.move=true;
	}
	
	public String getTypeAttack(){
		return type;
	}

	public void unregisterUpdateHandler(PhysicsWorld mPhysicsWorld) {
		mAnimatedSprite.unregisterUpdateHandler(mPhysicsWorld);
	}
	public String toString(){
		return "attack";
	}
	public float getDaño() {
		// TODO Auto-generated method stub
		return daño;
	}
	public Character getCreator() {
		// TODO Auto-generated method stub
		return creador;
	}
	

}
