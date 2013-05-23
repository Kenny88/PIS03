package com.example.pisv1;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.opengl.GLES20;
import android.util.Log;

public class Inventory extends InGameMenu {
	
	private int pos=0;
	private int select=-1;
	private int selectBox=-1;
	
	private final int BACKGROUND = 8;
	private final int EQUIPMENT=5;
	private final int UP=2;
	private final int DOWN=3;
	
	private ArrayList<Item> items = new ArrayList<Item>();
	private Item weapon=null, accessory=null, defense=null;
	private String equipChar[]={"W","A","D"};
	//private 
	
	private SimpleBaseGameActivity app;
	
	private BitmapTextureAtlas mMenuTexture;
	private BitmapTextureAtlas arrowTexture;
	private BitmapTextureAtlas attackTexture;
	private BitmapTextureAtlas deffenceTexture;
	private BitmapTextureAtlas highlightTexture;
	private BitmapTextureAtlas boxTexture;
	private BitmapTextureAtlas highboxTexture;
	
	protected ITextureRegion mMenuResetTextureRegion;
	protected ITextureRegion arrowTextureRegion;
	protected ITextureRegion attackTextureRegion;
	protected ITextureRegion deffenceTextureRegion;
	protected ITextureRegion highlightTextureRegion;
	protected ITextureRegion boxTextureRegion;
	protected ITextureRegion highboxTextureRegion;
	
	private MenuScene invMenuScene;
	
	public Inventory(SimpleBaseGameActivity application) {
		super(application);
		app=application;
		
	}

	public void addItem(String path, String name, int attack, int deffence, String type){
		items.add(new Item(path,name, attack, deffence, items.size(), type));
	}
	@Override
	public void startMenu() {
		super.startMenu();
		
		invMenuScene= new MenuScene((((Camera)((Game) app).mBoundChaseCamera)));
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mMenuTexture = new BitmapTextureAtlas(((Game) app).getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		arrowTexture = new BitmapTextureAtlas(((Game) app).getTextureManager(), 50, 50, TextureOptions.BILINEAR);
		attackTexture = new BitmapTextureAtlas(((Game) app).getTextureManager(), 40, 40, TextureOptions.BILINEAR);
		deffenceTexture = new BitmapTextureAtlas(((Game) app).getTextureManager(), 40, 40, TextureOptions.BILINEAR);
		highlightTexture = new BitmapTextureAtlas(((Game) app).getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		boxTexture = new BitmapTextureAtlas(((Game) app).getTextureManager(), 40, 40, TextureOptions.BILINEAR);
		highboxTexture = new BitmapTextureAtlas(((Game) app).getTextureManager(), 40, 40, TextureOptions.BILINEAR);
		
		this.mMenuResetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, ((Game) app), "menu_background.png", 0, 0);
		arrowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.arrowTexture, ((Game) app), "Up_Arrow_Icon.png", 0, 0);
		attackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.attackTexture, ((Game) app), "Attack.png", 0, 0);
		deffenceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.deffenceTexture, ((Game) app), "deffense.png", 0, 0);
		highlightTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.highlightTexture, ((Game) app), "highlight.png", 0, 0);
		boxTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.boxTexture, ((Game) app), "box.png", 0, 0);
		highboxTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.highboxTexture, ((Game) app), "highbox.png", 0, 0);
		
		this.mMenuTexture.load();
		this.arrowTexture.load();
		this.attackTexture.load();
		this.deffenceTexture.load();
		highlightTexture.load();
		boxTexture.load();
		highboxTexture.load();
		
		invMenuScene.setBackgroundEnabled(false);
		invMenuScene.setOnMenuItemClickListener(this);

	}
	
	public void drawEquipment(Item item, int cont, ITextureRegion box){
		
		if(item==null){
			SpriteMenuItem def=new SpriteMenuItem(EQUIPMENT+cont, box, ((Game) app).getVertexBufferObjectManager());
			def.setPosition(((Game) app).getCameraWidth()/3+20+40*cont, ((Game) app).getCameraHeight()/3-100);
			invMenuScene.addMenuItem(def);
			
			final Text statsA= new Text(((Game) app).getCameraWidth()/3+24+40*cont,((Game) app).getCameraHeight()/3-100, this.mFont, equipChar[cont], new TextOptions(HorizontalAlign.LEFT), ((Game) app).getVertexBufferObjectManager());
			invMenuScene.attachChild(statsA);
		}
		else{
			SpriteMenuItem def=new SpriteMenuItem(EQUIPMENT+cont, box, ((Game) app).getVertexBufferObjectManager());
			def.setPosition(((Game) app).getCameraWidth()/3+20+40*cont, ((Game) app).getCameraHeight()/3-100);
			invMenuScene.addMenuItem(def);
			
			BitmapTextureAtlas sprite1 = new BitmapTextureAtlas(((Game) app).getTextureManager(), 40, 40, TextureOptions.BILINEAR);
			ITextureRegion region1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprite1, ((Game) app), item.getPath(), 0, 0);
			sprite1.load();
			
			SpriteMenuItem item1=new SpriteMenuItem(EQUIPMENT+cont, region1, ((Game) app).getVertexBufferObjectManager());
			item1.setPosition(((Game) app).getCameraWidth()/3+20+40*cont, ((Game) app).getCameraHeight()/3-100);
			invMenuScene.attachChild(item1);
						
		}
	}
	
	
	
	public void drawInventory(){
		int i=0;
		ArrayList<ITextureRegion> cell=null;
		ArrayList<SpriteMenuItem> sprite=null;
		ArrayList<BitmapTextureAtlas> texture=null;
		
		if(pos<items.size()){
			texture = new ArrayList<BitmapTextureAtlas>();
			cell=new ArrayList<ITextureRegion>();
			sprite=new ArrayList<SpriteMenuItem>();
		}
		if(3<items.size()){
			SpriteMenuItem arrowUp=new SpriteMenuItem(UP, arrowTextureRegion, ((Game) app).getVertexBufferObjectManager());
			arrowUp.setPosition(((Game) app).getCameraWidth()/3+200, ((Game) app).getCameraHeight()/3-50+50*i);
			invMenuScene.addMenuItem(arrowUp);
			
			SpriteMenuItem arrowDown=new SpriteMenuItem(DOWN, arrowTextureRegion, ((Game) app).getVertexBufferObjectManager());
			arrowDown.setFlippedVertical(true);
			arrowDown.setPosition(((Game) app).getCameraWidth()/3+200, ((Game) app).getCameraHeight()/3-90+50*3);
			invMenuScene.addMenuItem(arrowDown);
		}
		for(i=0;i<items.size()&&i<3;i++){
			texture.add(new BitmapTextureAtlas(((Game) app).getTextureManager(), 40, 40, TextureOptions.BILINEAR));
			cell.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture.get(i), ((Game) app), items.get(i+pos).getPath(), 0, 0));
			texture.get(i).load();
			
			Log.d("path", items.get(i).getPath()+" "+i);
			
			SpriteMenuItem item1=new SpriteMenuItem(BACKGROUND+i, mMenuResetTextureRegion, ((Game) app).getVertexBufferObjectManager());
			item1.setPosition(((Game) app).getCameraWidth()/3, ((Game) app).getCameraHeight()/3-50+50*i);
			invMenuScene.addMenuItem(item1);

			sprite.add(new SpriteMenuItem(BACKGROUND+i, cell.get(i), ((Game) app).getVertexBufferObjectManager()));
			sprite.get(i).setPosition(((Game) app).getCameraWidth()/3+10, ((Game) app).getCameraHeight()/3-40+50*i);
			invMenuScene.attachChild(sprite.get(i));
			
			SpriteMenuItem att=new SpriteMenuItem(BACKGROUND+i, attackTextureRegion, ((Game) app).getVertexBufferObjectManager());
			att.setPosition(((Game) app).getCameraWidth()/3+60, ((Game) app).getCameraHeight()/3-37+50*i);
			invMenuScene.attachChild(att);
			
			final Text statsA= new Text(((Game) app).getCameraWidth()/3+90,((Game) app).getCameraHeight()/3-40+50*i, this.mFont, Integer.toString(items.get(i+pos).getAtk()), new TextOptions(HorizontalAlign.LEFT), ((Game) app).getVertexBufferObjectManager());
			invMenuScene.attachChild(statsA);
			
			SpriteMenuItem def=new SpriteMenuItem(BACKGROUND+i, deffenceTextureRegion, ((Game) app).getVertexBufferObjectManager());
			def.setPosition(((Game) app).getCameraWidth()/3+117, ((Game) app).getCameraHeight()/3-38+50*i);
			invMenuScene.attachChild(def);
			
			final Text statsD= new Text(((Game) app).getCameraWidth()/3+150,((Game) app).getCameraHeight()/3-40+50*i, this.mFont, Integer.toString(items.get(i+pos).getDef()), new TextOptions(HorizontalAlign.LEFT), ((Game) app).getVertexBufferObjectManager());
			invMenuScene.attachChild(statsD);
			
		}
		
	}
	public void highlight(int id){
		//invMenuScene.getChildByMatcher(pEntityMatcher)
		int i=id-BACKGROUND;
		
		BitmapTextureAtlas sprite1 = new BitmapTextureAtlas(((Game) app).getTextureManager(), 40, 40, TextureOptions.BILINEAR);
		ITextureRegion region1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprite1, ((Game) app), items.get(i+pos).getPath(), 0, 0);
		sprite1.load();
		
		if(select!=id){
			SpriteMenuItem sprite = new SpriteMenuItem(id, highlightTextureRegion, ((Game) app).getVertexBufferObjectManager());
			sprite.setPosition(((Game) app).getCameraWidth()/3, ((Game) app).getCameraHeight()/3-50+50*i);
			invMenuScene.addMenuItem(sprite);
			select=id;
		}else{
			SpriteMenuItem sprite = new SpriteMenuItem(id, mMenuResetTextureRegion, ((Game) app).getVertexBufferObjectManager());
			sprite.setPosition(((Game) app).getCameraWidth()/3, ((Game) app).getCameraHeight()/3-50+50*i);
			invMenuScene.addMenuItem(sprite);
			select=-1;
		}
		SpriteMenuItem item1=new SpriteMenuItem(id, region1, ((Game) app).getVertexBufferObjectManager());
		item1.setPosition(((Game) app).getCameraWidth()/3+10, ((Game) app).getCameraHeight()/3-40+50*i);
		invMenuScene.attachChild(item1);
		
		SpriteMenuItem att=new SpriteMenuItem(id, attackTextureRegion, ((Game) app).getVertexBufferObjectManager());
		att.setPosition(((Game) app).getCameraWidth()/3+60, ((Game) app).getCameraHeight()/3-37+50*i);
		invMenuScene.attachChild(att);
		
		final Text statsA= new Text(((Game) app).getCameraWidth()/3+90,((Game) app).getCameraHeight()/3-40+50*i, this.mFont, Integer.toString(items.get(i+pos).getAtk()), new TextOptions(HorizontalAlign.LEFT), ((Game) app).getVertexBufferObjectManager());
		invMenuScene.attachChild(statsA);
		
		SpriteMenuItem def=new SpriteMenuItem(id, deffenceTextureRegion, ((Game) app).getVertexBufferObjectManager());
		def.setPosition(((Game) app).getCameraWidth()/3+117, ((Game) app).getCameraHeight()/3-38+50*i);
		invMenuScene.attachChild(def);
		
		final Text statsD= new Text(((Game) app).getCameraWidth()/3+150,((Game) app).getCameraHeight()/3-40+50*i, this.mFont, Integer.toString(items.get(i+pos).getDef()), new TextOptions(HorizontalAlign.LEFT), ((Game) app).getVertexBufferObjectManager());
		invMenuScene.attachChild(statsD);
		
		
	}
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		ITextureRegion region1;
		Log.d("id", Integer.toString(pMenuItem.getID()));
		if(BACKGROUND<=pMenuItem.getID()&&pMenuItem.getID()<BACKGROUND+items.size()){
			drawInventory();
			highlight(pMenuItem.getID());
		}
		else if(EQUIPMENT<=pMenuItem.getID()&&pMenuItem.getID()<BACKGROUND){
			if(pMenuItem.getID()!=selectBox){
				region1=highboxTextureRegion;
				selectBox=pMenuItem.getID();
			}else{
				region1=boxTextureRegion;
				selectBox=-1;
			}
			Log.d("id", Integer.toString(pMenuItem.getID()-EQUIPMENT));
			Log.d("selectbox", Integer.toString(selectBox));
			switch(pMenuItem.getID()-EQUIPMENT){
				case 0:
					drawEquipment(weapon, 0, region1);
					drawEquipment(accessory, 1, boxTextureRegion);
					drawEquipment(defense, 2, boxTextureRegion);
					break;
				case 1:
					drawEquipment(weapon, 0, boxTextureRegion);
					drawEquipment(accessory, 1, region1);
					drawEquipment(defense, 2, boxTextureRegion);
					break;
				case 2:
					drawEquipment(weapon, 0, boxTextureRegion);
					drawEquipment(accessory, 1, boxTextureRegion);
					drawEquipment(defense, 2, region1);
					break;
			}
		}
		else{
			switch(pMenuItem.getID()) {
			case QUIT:
				((Game) app).bMenu=false;
				((Game) app).finish();
	
				return true;
			case INVENTORY:
				pMenuScene.back();
				invReset();
				((Game) app).mMainScene.setChildScene(invMenuScene, false, true, true);
				return true;
			case UP:
				Log.d("case", "Enter case up");
				if(pos>0){
					pos--;
					Log.d("pos", Integer.toString(pos));
					drawInventory();
					select=-1;
				}
				return true;
			case DOWN:
				Log.d("case", "Enter case down");
				if(pos<items.size()-3){
					pos++;
					Log.d("pos", Integer.toString(pos));
					drawInventory();
					select=-1;
				}
				return true;
			default:
				return false;
			}
		}
		if(select!=-1&&selectBox!=-1){
			Item item=null,item2=items.get(select-BACKGROUND+pos);
			if(item2.getType().equals("Weapon")&&selectBox-EQUIPMENT==0){
				item=weapon;
				weapon=item2;
			}
			else if(item2.getType().equals("Accessory")&&selectBox-EQUIPMENT==1){
				item=accessory;
				accessory=item2;
			}
			else if(item2.getType().equals("Defense")&&selectBox-EQUIPMENT==2){
				item=defense;
				defense=item2;
			}else{
				invReset();
				return true;
			}
			if(item!=null){
				
				items.add(item);
			}
			items.remove(item2);
			invReset();
			return true;
			
		}
		return true;
	}
	
	public void invReset(){
		invMenuScene.detachChildren();
		drawInventory();
		drawEquipment(weapon, 0, boxTextureRegion);
		drawEquipment(accessory, 1, boxTextureRegion);
		drawEquipment(defense, 2, boxTextureRegion);
		pos=0;
		selectBox=-1;
		select=-1;
	}


	
}
