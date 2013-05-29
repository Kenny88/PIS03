package com.example.pisv1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;

import android.graphics.Point;
import android.util.Log;

public class Enemy extends Character {
	
	private int distV=200;
	private int distA=40;
	private int dirX;
	private int dirY;
	protected float maxRange=200;
	ArrayList<ArrayList<String>> visited;
	private PathModifier cPath;
	private IUpdateHandler update;
	private Stack<Node> stack;
	private Game app;
	//private Game app1;
	Path path;
	Random ran=new Random();
	boolean isWalking=false;
	Node node1;
	private int tileNumX;
	private int tileNumY;
	private int tileSizeX;
	private int tileSizeY;

	
	public Enemy(float x, float y, String image,final Game app1, Map map, boolean range) {
		super(x, y, image, app1);
		// TODO Auto-generated constructor stub
		app=app1;
		tileNumX=map.getTileNumX();
		tileNumY=map.getTileNumY();
		tileSizeX=map.getTileX();
		tileSizeY=map.getTileY();
		ranged=range;
		
	}

	
	public Node bfs(boolean[][] board, int rootX, int rootY, int endX, int endY, boolean player){
		Node child, root= new Node(rootX, rootY);
		Queue<Node> queue = new LinkedList<Node>();
		boolean[][] visited=new boolean[tileNumY][tileNumX];
		for(int i=0;i<tileNumY;i++){
			for(int j=0;j<tileNumX;j++){
				visited[i][j]=false;
			}
		}
		queue.add(root);
		visited[root.y][root.x]=true;
		while(!queue.isEmpty()){
			root=(Node)queue.remove();
			child=null;
			while((child=getNeighbors(root, visited, board, endX, endY))!=null){
				child.setOld(root);
				queue.add(child);
				if(child.x==endX&&child.y==endY){
					if(board[child.y][child.x]&&!player)
						return null;
					else
						return child;
				}
				visited[child.y][child.x]=true;
			}
		}
		return null;
	}
	
	public Node getNeighbors(Node node, boolean[][] visited, boolean[][] board, int endX, int endY){
		Node node2=null;
		if(node.x-1>-1&&!visited[node.y][node.x-1]&&!board[node.y][node.x-1]||(node.x-1==endX&&node.y==endY))
			node2=new Node(node.x-1,node.y);
		else if(node.x+1<tileNumX&&!visited[node.y][node.x+1]&&!board[node.y][node.x+1]||(node.x+1==endX&&node.y==endY))
			node2=new Node(node.x+1,node.y);
		else if(node.y-1>-1&&!visited[node.y-1][node.x]&&!board[node.y-1][node.x]||(node.x==endX&&node.y-1==endY))
			node2=new Node(node.x,node.y-1);
		else if(node.y+1<tileNumY&&!visited[node.y+1][node.x]&&!board[node.y+1][node.x]||(node.x==endX&&node.y+1==endY))
			node2=new Node(node.x,node.y+1);
		return node2;
		
	}
	
	public void startUpdate(){
		update= new IUpdateHandler(){

			@Override
			public void onUpdate(float arg0) {
				// TODO Auto-generated method stub
				//double angle=Math.atan2(mAnimatedSprite.getY()-app.mMap.getPlayerY(), mAnimatedSprite.getX()-app.mMap.getPlayerX())* 180 / Math.PI;
				float dist1=MathUtils.distance(mAnimatedSprite.getX(), mAnimatedSprite.getY(), app.mMap.getPlayerX(), app.mMap.getPlayerY());
				int dir=-4;
				Log.d("Dirrection",Boolean.toString(ranged&&(dir=getDirection())!=-1));
				Log.d("Dirrection",Integer.toString(dir));
				if(dist1<distA){
					move(0,0);
					attack(0,1);
				}else if(ranged&&(dir=getDirection())!=-1){
					Log.d("Dirrection",Integer.toString(dir));
					direction=dir;
					attack(0,-1);

				}else{
					
					
					if(dist1<distV){
						node1=bfs(app.mMap.getTileMap(), 
								((int)mAnimatedSprite.getX()/tileSizeX),
								((int)mAnimatedSprite.getY()/tileSizeY),
								app.mMap.getPlayerTile().x,app.mMap.getPlayerTile().y,true);
						if(node1!=null)
							getPath(node1);
						else{
							stack=null;
						}
					
						
					}
					if(stack==null||stack.size()==0){
						int i=-1, j=-1;
						i=ran.nextInt(8)-4+((int)mAnimatedSprite.getX()/tileSizeX);
						j=ran.nextInt(8)-4+((int)mAnimatedSprite.getY()/tileSizeY);
						if(i<0&&j<0){
							i+=ran.nextInt(8)+4;
							j+=ran.nextInt(8)+4;
						}
						node1=bfs(app.mMap.getTileMap(),((int)mAnimatedSprite.getX()/tileSizeX),
								((int)mAnimatedSprite.getY()/tileSizeY),i,j,false);
						if(node1!=null)
							getPath(node1);
						else{
							stack=null;
						}
						
					}
					if(node1!=null){

						int error=10;
						if(node1.y*tileSizeY+error<mAnimatedSprite.getY()){
							dirY=-1;
						}else if(node1.y*tileSizeY-3>mAnimatedSprite.getY()){
							dirY=1;
						}else{
							dirY=0;
						}
						if(node1.x*tileSizeX+error<mAnimatedSprite.getX()){
							dirX=-1;
						}else if(node1.x*tileSizeX-3>mAnimatedSprite.getX()){
							dirX=1;
						}else{
							dirX=0;
						}
						move(dirX,dirY);
						if(dirX==0&&dirY==0){
							if(stack!=null&&stack.size()>0){
								node1=stack.pop();
							}
							
						}
					}
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
		};
		app.mMainScene.registerUpdateHandler(update);
	}
	


	private void getPath(Node node){
		stack= new Stack<Node>();
		while(node.getOld()!=null){
			stack.push(node);
			node=node.getOld();
		}
		if(stack.size()>0)
			node1=stack.pop();
		else node1=node;
	}
	public Node getPropers(Node node){
		
		return node;
	}
	
	public int getDirection(){
		float epX, epY;
		epX=mAnimatedSprite.getX()-app.mMap.getPlayerX();
		epY=mAnimatedSprite.getY()-app.mMap.getPlayerY();
		if((Math.abs(epX)<maxRange&&Math.abs(epY)<distA*0.66)||Math.abs(epY)<maxRange&&Math.abs(epX)<distA*0.66){
			if(epX<maxRange&&epX>distA*0.66)
				return 1;
			else if(epX>-maxRange&&epX<-distA*0.66)
				return 3;
			else if(epY<maxRange&&epY>distA*0.66)
				return 0;
			else if(epY>-maxRange&&epY<-distA*0.66)
				return 2;
			
		}
		return -1;
	}

}

class Node{
	int x;
	int y;
	
	Node old=null;
	public Node(int x1, int y1){
		x=x1;
		y=y1;
	}
	
	public void setOld(Node node){
		old=node;
	}
	public Node getOld(){
		return old;
	}
	
}
