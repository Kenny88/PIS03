
package com.example.pisv1;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXObjectProperty;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.debug.Debug;

import android.graphics.Point;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 13:58:48 - 19.07.2010
 */
public class Map {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================


	private TMXTiledMap mTMXTiledMap;
	private Game app;
	private Scene mMapScene;
	private PhysicsWorld mPhysicsWorld;
	
	private int tileHeight;
	private int tileWight;
	private int tileNumX;
	private int tileNumY;
	
	private boolean[][] tileMatrix;
	
	public PhysicsWorld getmPhysicsWorld() {
		return mPhysicsWorld;
	}

	private String mName;
	private Doors doors=new Doors();
	


	
	
	public TMXTiledMap getmTMXTiledMap() {
		return mTMXTiledMap;
	}
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public Map(String name,Game app1,final Player player) {
		this.mMapScene=new Scene();
		this.app=app1;
		this.mName=name;
		mPhysicsWorld=new PhysicsWorld(new Vector2(0,0),false);
		mPhysicsWorld.setContactListener(app.listener);
		this.mMapScene.registerUpdateHandler(this.mPhysicsWorld);
		try {
			final TMXLoader tmxLoader = new TMXLoader(app.getAssets(), app.getEngine().getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, app.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {

				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset(name);
			tileNumX=mTMXTiledMap.getTileColumns();
			tileNumY=mTMXTiledMap.getTileRows();
			tileHeight=mTMXTiledMap.getTileHeight();
			tileWight=mTMXTiledMap.getTileColumns();

			
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		
		for (int layerID = 0; layerID < this.mTMXTiledMap.getTMXLayers().size(); layerID++) {
			mMapScene.attachChild(this.mTMXTiledMap.getTMXLayers().get(layerID));
	}
        // Loop through the object groups
		
		tileMatrix=new boolean[tileNumY][tileNumX];
		
		for(int i=0;i<tileNumY;i++){
			for(int j=0;j<tileNumX;j++){
				tileMatrix[i][j]=false;
			}
		}
        for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) 
        {
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("wall", "true"))
	        {
	            // This is our "wall" layer. Create the boxes from it
	            for(TMXObject object : group.getTMXObjects()) 
	            {
	            	setWalls(object);
	                final Rectangle rect = new Rectangle(0.0f+object.getX(), 0.0f+object.getY(),0.0f+object.getWidth(), 0.0f+ object.getHeight(), app.getVertexBufferObjectManager());
	           		//rect.setColor(1, 0, 0, 0.5f);
                    PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, app.WALL_FIXTURE_DEF).setUserData("wall");
                    //rect.setVisible(true);
	           		//mMapScene.attachChild(rect);
	            }
            }
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("object", "true"))
	        {
	            // This is our "object" layer. Create the boxes from it
	            for(final TMXObject object : group.getTMXObjects()) 
	            {
	            	new MapItem(object,app,this);
	         
		        }	
            }
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("keys", "true"))
	        {
	            // This is our "object" layer. Create the boxes from it
	            for(final TMXObject object : group.getTMXObjects()) 
	            {
	            	new Key(object,app,this);
		        }	
            }
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("pass", "true"))
	        {
	            // This is our "door" layer. Create the boxes from it
	            for(final TMXObject object : group.getTMXObjects()) 
	            {
	            	Door door=new Door(object,app,this);
	                getDoors().add(door);
		        }	
            }
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("pj", "true"))
	        {
	            for(TMXObject object : group.getTMXObjects()) 
	            {
	        		
	        		final Character anciano = new Character(object.getX(), object.getY(),object.getTMXObjectProperties().get(0).getValue(),app);
	           		mMapScene.attachChild(anciano.getAnimatedSprite());
	           		anciano.addToPhysicsWorld(mPhysicsWorld, app.CHARACTER_FIXTURE_DEF);
	           		
		        }	
            }
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("enemy", "true"))
	        {
	            for(TMXObject object : group.getTMXObjects()) 
	            {
	        		TMXProperties<TMXObjectProperty> prop=object.getTMXObjectProperties();
	        		final Enemy anciano = new Enemy(object.getX(), object.getY(),prop.get(3).getValue(),app,this,prop.containsTMXProperty("ranged", "true"));
	        		anciano.setStatistics(Integer.parseInt(prop.get(0).getValue()),Integer.parseInt(prop.get(1).getValue()),Integer.parseInt(prop.get(2).getValue()));
	           		mMapScene.attachChild(anciano.getAnimatedSprite());
	           		anciano.addToPhysicsWorld(mPhysicsWorld, app.CHARACTER_FIXTURE_DEF);
	           		anciano.startUpdate();
	           		
		        }	
            }
        }

		/* Make the camera not exceed the bounds of the TMXEntity. */
		app.mBoundChaseCamera.setBounds(0, 0, mTMXTiledMap.getTileRows()*mTMXTiledMap.getTileHeight(), mTMXTiledMap.getTileColumns()*mTMXTiledMap.getTileWidth());
		app.mBoundChaseCamera.setBoundsEnabled(true);
	}
	
	
	public Map(String name,Game app1,Player player,float positionX,float positionY){
		this(name,app1, player);
		player.setVelocity(0,0);
		player.setPosition(positionX, positionY);
		
	}

	public void addPlayer(Player player, float x, float y) {
		addPlayer(player);
		player.setPosition(x, y);
	}
	public void deletePlayer(final Player player){
		player.unregisterUpdateHandler(mPhysicsWorld);
		player.detach(mPhysicsWorld);
	}

	public Scene getMapScene(){
		return mMapScene;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return mName;
	}

	public void addPlayer(Player player) {
		player.registerUpdateHandler(mPhysicsWorld);
		mMapScene.attachChild(player.getAnimatedSprite());
		player.addToPhysicsWorld(mPhysicsWorld, app.CHARACTER_FIXTURE_DEF);
		
	}

	public Doors getDoors() {
		return doors;
	}


	public void setWalls(TMXObject object){
		int startx, starty, finalx, finaly, i, j;
		startx=object.getX()/tileWight;
		starty=object.getY()/tileHeight;
		finalx=(object.getX()+object.getWidth())/tileWight;
		finaly=(object.getY()+object.getHeight())/tileHeight;
		if (finalx==tileNumX){
			finalx--;
		}if (finaly==tileNumY){
			finaly--;
		}
		for(i=starty;i<=finaly;i++){
			for(j=startx;j<=finalx;j++){
				tileMatrix[i][j]=true;
			}
		}
	}
	
	public boolean[][] getTileMap(){
		return tileMatrix;
	}

	public float getPlayerX(){
		return app.mPlayer.mAnimatedSprite.getX();
	}
	
	public float getPlayerY(){
		return app.mPlayer.mAnimatedSprite.getY();
	}
	
	public int getTileX(){
		return tileWight;
	}
	public int getTileY(){
		return tileHeight;
	}
	public int getTileNumX(){
		return tileNumX;
	}
	public int getTileNumY(){
		return tileNumY;
	}
	public Point getPlayerTile(){
		return new Point((int)(app.mPlayer.mAnimatedSprite.getX())/tileWight, (int)(app.mPlayer.mAnimatedSprite.getY())/tileHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
