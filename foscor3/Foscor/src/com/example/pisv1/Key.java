package com.example.pisv1;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXObject;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Key {
private Body mBody;
public String map;
public String door;
private boolean abrir;
private boolean cerrar;
private Rectangle rect;

public Key(TMXObject object,Game app, Map mMap){
	this.rect = new Rectangle(object.getX(), object.getY(),object.getWidth(),object.getHeight(),app.getVertexBufferObjectManager());
	mMap.getMapScene().attachChild(rect);
   	rect.setColor(0,1, 0, 0.5f);
    rect.setVisible(true);
    this.mBody=PhysicsFactory.createBoxBody(mMap.getmPhysicsWorld(), rect, BodyType.KinematicBody, app.ZERO_FIXTURE_DEF);
    this.map=object.getName();
	mMap.getmPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(rect, mBody, false, false));
    mBody.setUserData(this);
    this.door=object.getType();
    this.abrir=object.getTMXObjectProperties().containsTMXProperty("abrir", "true");
    this.cerrar=object.getTMXObjectProperties().containsTMXProperty("cerrar", "true");
}
public String toString(){
	return "key";	
}
public boolean abrir() {
	// TODO Auto-generated method stub
	return abrir;
}
public boolean cerrar() {
	// TODO Auto-generated method stub
	return cerrar;
}
public void detach(final PhysicsWorld mPhysicsWorld) {
	rect.registerUpdateHandler(new IUpdateHandler(){

		@Override
		public void onUpdate(float arg0) {
			// TODO Auto-generated method stub
			mPhysicsWorld.destroyBody(mBody);
			rect.detachSelf();

		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		
	});	}


}
