package com.example.pisv1;

public class Item {
	private String path;
	private String name;
	private int atk;
	private int def;
	private int id;
	private String type;
	
	public Item(String p, String n,int at, int d, int idn, String tp){
		path=p;
		name=n;
		atk=at;
		def=d;
		id=idn;
		type=tp;
	}
	public Item(String p, String n,int at, int d, String tp){
		path=p;
		name=n;
		atk=at;
		def=d;
		type=tp;
	}
	
	public void setId(int id){
		this.id=id;
	}
	public String getPath(){
		return path;
	}
	public int getAtk(){
		return atk;
	}
	public int getDef(){
		return def;
	}
	public int getId(){
		return id;
	}
	public String getType(){
		return type;
	}
}
