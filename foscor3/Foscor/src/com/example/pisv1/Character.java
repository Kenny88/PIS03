package com.example.pisv1;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Character {
	protected AnimatedSprite mAnimatedSprite;
	protected Body mBody;
	private Game app;
	protected int directionX = 0;
	protected int directionY = 0;
	public Character(float x, float y, String image,Game app1){
		this.app=app1;
		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(app.getTextureManager(), 72, 128, TextureOptions.DEFAULT);
		TiledTextureRegion mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, app,image, 0, 0, 3, 4);
		mBitmapTextureAtlas.load();
		mAnimatedSprite=new AnimatedSprite(x,y,mPlayerTextureRegion,app.getVertexBufferObjectManager());
		
	}
	public void addToPhysicsWorld(PhysicsWorld mPhysicsWorld,FixtureDef fixtureDef){
   		mBody=PhysicsFactory.createBoxBody(mPhysicsWorld, mAnimatedSprite, BodyType.DynamicBody, fixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mAnimatedSprite, mBody, true, false));
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
		// TODO Auto-generated method stub
		
		mBody.setTransform(x,y, 0);
		}
}
