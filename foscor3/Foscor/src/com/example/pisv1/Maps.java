package com.example.pisv1;

import java.util.ArrayList;

public class Maps extends ArrayList<Map> {
	private Game app;
	public void setGame(Game app){
		this.app=app;
	}
	public Map get(String nombre){
		Map map=null;
		for(Map m :this){
			if (m.getName().equals(nombre)){
				map=m;
				return m;
			}
		}
		if (map==null){
			for (String []grupo :mapas){
				for (String nombreMap:grupo){
					if (nombreMap.equals(nombre)){
						for(String name:grupo){							
							if (name.equals(nombre)){
								map=new Map(name,app,app.mPlayer);
								add(map);
							}else{
								add(new Map(name,app,app.mPlayer));
							}
						}
						return map;


					}
				}
			}
		}
		return null;
	}
	String [] inicial={"tmx/bosque1.tmx"};
	String [] balmont={"tmx/balmont1.tmx","tmx/casabalmont1.tmx","tmx/balmont2.tmx","tmx/balmont3.tmx","tmx/balmont4.tmx"};
	String [] bosque={"tmx/bosque2.tmx","tmx/bosque3.tmx","tmx/bosque4.tmx","tmx/bosque5.tmx"};
	String [] alasar={"tmx/desierto.tmx"};
	String [][]mapas={inicial,bosque,balmont,alasar};

}
