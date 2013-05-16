
package com.example.pisv1;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.debug.Debug;

import android.widget.Toast;

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

	public Map(String name,final Game activity, Scene scene,BoundCamera camera, final AnimatedSprite player) {

		try {
			final TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), activity.getEngine().getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, activity.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					/* We are going to count the tiles that have the property "cactus=true" set. */
					if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
						
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
	        scene.attachChild(this.mTMXTiledMap.getTMXLayers().get(layerID));
	}

		/* Make the camera not exceed the bounds of the TMXEntity. */
		camera.setBounds(0, 0, mTMXTiledMap.getTileRows()*mTMXTiledMap.getTileHeight(), mTMXTiledMap.getTileColumns()*mTMXTiledMap.getTileWidth());
		camera.setBoundsEnabled(true);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}







/*

package com.example.pisv1;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

public class Map extends Scene {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================


	private TMXTiledMap mTMXTiledMap;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public Map(String name,SimpleBaseGameActivity activity, Scene scene,BoundCamera camera) {

		try {
			final TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), activity.getEngine().getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, activity.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
						
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
	        scene.attachChild(this.mTMXTiledMap.getTMXLayers().get(layerID));
	}

		camera.setBounds(0, 0, mTMXTiledMap.getTileRows()*mTMXTiledMap.getTileHeight(), mTMXTiledMap.getTileColumns()*mTMXTiledMap.getTileWidth());
		camera.setBoundsEnabled(true);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
*/