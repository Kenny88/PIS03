package com.example.pisv1;

import java.util.ArrayList;


public class Doors extends ArrayList<Door> {
	public Door get(String nombre){
	for(Door m:this){
		if (m.getName().equals(nombre)){
			return m;
		}
	}
	return null;
}

}
