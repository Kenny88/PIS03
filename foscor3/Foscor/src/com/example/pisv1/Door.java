package com.example.pisv1;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectProperty;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Door{
	public String name;
	private Game app;
	private boolean abierta;
	private Integer delayX;
	private Integer delayY;
	private String map;
	private Scene mMapScene;
	private Sprite sprite;
	private Body mBody;
	public Door(TMXObject object, final Game app,Map mMap){
		TMXProperties<TMXObjectProperty> properties=object.getTMXObjectProperties();
		String image=properties.get(1).getValue();
		this.name=object.getName();
		this.map=object.getType();
		this.abierta=properties.get(0).getValue().equals("true");
		this.delayX=Integer.valueOf(properties.get(2).getValue());
		this.delayY=Integer.valueOf(properties.get(3).getValue());
		this.app=app;
		this.mMapScene=mMap.getMapScene();
		float w=object.getWidth(),h=object.getHeight(),x=object.getX(),y=object.getY();
		
		if (!image.equals("null")){
			BitmapTextureAtlas texture = new BitmapTextureAtlas(app.getTextureManager(),(int) w,(int)h, TextureOptions.BILINEAR);
			ITextureRegion region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, app, image, 0, 0);
			texture.load();
			sprite= new Sprite(x, x,w,h,region,app.getVertexBufferObjectManager());
			if (!abierta){
				mMapScene.attachChild(sprite);
			}
		}
		final Rectangle rect = new Rectangle(x, y,w,h,app.getVertexBufferObjectManager());
   		mMapScene.attachChild(rect);
       	rect.setColor(1,1, 0, 0.5f);
        rect.setVisible(true);
        this.mBody=PhysicsFactory.createBoxBody(mMap.getmPhysicsWorld(), rect, BodyType.StaticBody, app.WALL_FIXTURE_DEF);
   		mMap.getmPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(rect, mBody, true, false));
        mBody.setUserData(this);
	}

	public void cerrar(){
		abierta=false;
		if(sprite!=null){
			mMapScene.attachChild(sprite);
		}
	}
	public void abrir(){
		abierta=true;
		if(sprite!=null){
			sprite.detachSelf();
		}
	}
	public String getName() {
		return name;
	}
	
	public boolean isAbierta() {
		return abierta;
	}
	public String toString(){
		return "door";	
	}
	public void pasar(Player fixtureB) {
		if(abierta){
			float x,y;
			x=app.mPlayer.mBody.getPosition().x;
			x+=delayX;
			y=app.mPlayer.mBody.getPosition().y;
			y+=delayY;
			app.changeMap(map,x,y);
		}else{
			app.Toast(R.string.cantCross);
		}
		
	}
}
