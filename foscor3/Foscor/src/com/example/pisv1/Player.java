package com.example.pisv1;

import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Player extends Character {
	private IOnScreenControlListener mIOnScreenControlListener;
	private IOnScreenControlListener mBotonsControlListener;
	public Player(float x, float y, String image,final Game app) {
		super(x, y, image, app);
		setStatistics(75,75, 75);
		mIOnScreenControlListener=new IOnScreenControlListener() {
	
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl,  float pValueX,  float pValueY) {
				move(pValueX,pValueY);
			};
		};
		mBotonsControlListener=new IOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl,  float pValueX,  float pValueY) {

				attack(pValueX,pValueY);
			}
		
		};
	}
	public void detach(final PhysicsWorld mPhysicsWorld) {
		mPhysicsWorld.destroyBody(mBody);
		mAnimatedSprite.detachSelf();
	}
	public void restVida(float f) {
		cVida-=f;
		if (cVida<0){
			app.gameOver();
		}else{
			app.setVida(cVida);
		}
	}
	public void sumVida(float f) {
		cVida+=f;
		if (cVida>100){
			cVida=100;
		}
		app.setVida(cVida);
	}
	public IOnScreenControlListener getIOnScreenControlListener() {
		return mIOnScreenControlListener;
	}
	public IOnScreenControlListener getBotonsControlListener() { 
		return mBotonsControlListener;
	}


	public String toString(){
		return "player";
	}




	
}
