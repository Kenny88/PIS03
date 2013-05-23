
package com.example.pisv1;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.Constants;
import org.andengine.util.debug.Debug;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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
	
	
	
	/* The categories. */
	public static final short CATEGORYBIT_WALL = 1;
	public static final short CATEGORYBIT_CHARACTER = 2;
	public static final short CATEGORYBIT_CIRCLE = 4;

	/* And what should collide with what. */
	public static final short MASKBITS_WALL = CATEGORYBIT_WALL + CATEGORYBIT_CHARACTER + CATEGORYBIT_CIRCLE;
	public static final short MASKBITS_CHARACTER = CATEGORYBIT_WALL + CATEGORYBIT_CHARACTER; // Missing: CATEGORYBIT_CIRCLE
	public static final short MASKBITS_CIRCLE = CATEGORYBIT_WALL + CATEGORYBIT_CIRCLE; // Missing: CATEGORYBIT_BOX

	public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
	public static final FixtureDef CHARACTER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_CHARACTER, MASKBITS_CHARACTER, (short)0);
	public static final FixtureDef CIRCLE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_CIRCLE, MASKBITS_CIRCLE, (short)0);


	
	
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
		mPhysicsWorld=new PhysicsWorld(new Vector2(0,0),false);
		this.mMapScene.registerUpdateHandler(this.mPhysicsWorld);
		player.registerUpdateHandler(mPhysicsWorld);
		try {
			final TMXLoader tmxLoader = new TMXLoader(app.getAssets(), app.getEngine().getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, app.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					/* We are going to count the tiles that have the property "cactus=true" set. */
					if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
		                final Rectangle rect = new Rectangle(0.0f+pTMXTile.getTileX(), 0.0f+pTMXTile.getTileY(),0.0f+pTMXTile.getTileWidth(), 0.0f+ pTMXTile.getTileHeight(), app.getVertexBufferObjectManager());
//		           		rect.setColor(0,1, 1, 0.5f);
	                    PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, WALL_FIXTURE_DEF);
//	                    rect.setVisible(false);
//		           		mMapScene.attachChild(rect);
						
					}
					if(pTMXTileProperties.containsTMXProperty("monster", "true")){
						
					}
				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset(name);

			
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		
		for (int layerID = 0; layerID < this.mTMXTiledMap.getTMXLayers().size(); layerID++) {
			mMapScene.attachChild(this.mTMXTiledMap.getTMXLayers().get(layerID));
	}
        // Loop through the object groups
        for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) 
        {
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("wall", "true"))
	        {
	            // This is our "wall" layer. Create the boxes from it
	            for(TMXObject object : group.getTMXObjects()) 
	            {
	                final Rectangle rect = new Rectangle(0.0f+object.getX(), 0.0f+object.getY(),0.0f+object.getWidth(), 0.0f+ object.getHeight(), app.getVertexBufferObjectManager());
//	           		rect.setColor(1, 0, 0, 0.5f);
                    PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, WALL_FIXTURE_DEF);
//                    rect.setVisible(false);
//	           		mMapScene.attachChild(rect);
	            }
            }
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("object", "true"))
	        {
	            // This is our "wall" layer. Create the boxes from it
	            for(final TMXObject object : group.getTMXObjects()) 
	            {
	           		int w=40,h=40;
	    			BitmapTextureAtlas texture = new BitmapTextureAtlas(((Game) app).getTextureManager(), w, h, TextureOptions.BILINEAR);
	    			ITextureRegion region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, ((Game) app), object.getTMXObjectProperties().get(2).getValue(), 0, 0);
	    			texture.load();
	    			final Sprite sprite= new Sprite(object.getX(), object.getY(),w,h,region,app.getVertexBufferObjectManager());
	           		mMapScene.attachChild(sprite);
	           		sprite.registerUpdateHandler(new IUpdateHandler() {
		           		   @Override
		           		   public void reset() { }
		           		       
		           		   @Override
		           		   public void onUpdate(final float pSecondsElapsed) {
		           		      if (sprite.collidesWith(player.getAnimatedSprite())&&sprite.isVisible()){
		           		    	  Log.d("getObject", object.getType());
		           		    	  app.menu.addItem(object.getTMXObjectProperties().get(2).getValue(), object.getName(), Integer.valueOf(object.getTMXObjectProperties().get(0).getValue()), Integer.valueOf(object.getTMXObjectProperties().get(1).getValue()), object.getType());
		           		    	  sprite.setVisible(false);
		           		      }
		           		   }
		           		});
		        }	
            }
        	if(group.getTMXObjectGroupProperties().containsTMXProperty("pj", "true"))
	        {
	            for(TMXObject object : group.getTMXObjects()) 
	            {
	        		
	        		final Character anciano = new Character(object.getX(), object.getY(),object.getTMXObjectProperties().get(0).getValue(),app);
	           		mMapScene.attachChild(anciano.getAnimatedSprite());
	           		final Path path = new Path(5).to(0, 160).to(0, 500).to(1200, 500).to(1200, 160).to(0, 160);
	           		anciano.getAnimatedSprite().registerUpdateHandler(new IUpdateHandler() {
		           		   @Override
		           		   public void reset() { }
		           		       
		           		   @Override
		           		   public void onUpdate(final float pSecondsElapsed) {
//		           			  anciano.setPosition(anciano.getAnimatedSprite().getX(), anciano.getAnimatedSprite().getY());
		           		   }
		           		});
	        		anciano.getAnimatedSprite().registerEntityModifier(new LoopEntityModifier(new PathModifier(30, path, null, new IPathModifierListener() {
	        			@Override
	        			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {

	        			}

	        			@Override
	        			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
	        				switch(pWaypointIndex) {
	        					case 0:
	        						anciano.getAnimatedSprite().animate(new long[]{200, 200, 200}, 0, 2, true);
	        						break;
	        					case 1:
	        						anciano.getAnimatedSprite().animate(new long[]{200, 200, 200}, 6, 8, true);
	        						break;
	        					case 2:
	        						anciano.getAnimatedSprite().animate(new long[]{200, 200, 200}, 9, 11, true);
	        						break;
	        					case 3:
	        						anciano.getAnimatedSprite().animate(new long[]{200, 200, 200}, 3, 5, true);
	        						break;
	        				}
	        			}

	        			@Override
	        			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {

	        			}

	        			@Override
	        			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {

	        			}
	        		})));
		        }	
            }
        }

		/* Make the camera not exceed the bounds of the TMXEntity. */
		app.mBoundChaseCamera.setBounds(0, 0, mTMXTiledMap.getTileRows()*mTMXTiledMap.getTileHeight(), mTMXTiledMap.getTileColumns()*mTMXTiledMap.getTileWidth());
		app.mBoundChaseCamera.setBoundsEnabled(true);
		player.addToPhysicsWorld(mPhysicsWorld, CHARACTER_FIXTURE_DEF);
		mMapScene.attachChild(player.getAnimatedSprite());
	}
	
	
	public Map(String name,Game app1,Player player,int positionX,int positionY){
		this(name,app1, player);
		float [] playerFootCordinates = mMapScene.convertLocalToSceneCoordinates(positionX,positionY);
		player.setPosition(playerFootCordinates[Constants.VERTEX_INDEX_X], playerFootCordinates[Constants.VERTEX_INDEX_Y]);

	}

	public Scene getMapScene(){
		return mMapScene;
	}

	public void delete() {
		mMapScene.detachChildren();
		mPhysicsWorld.clearPhysicsConnectors();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
