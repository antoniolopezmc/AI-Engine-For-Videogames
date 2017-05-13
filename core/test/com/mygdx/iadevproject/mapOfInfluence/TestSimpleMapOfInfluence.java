package com.mygdx.iadevproject.mapOfInfluence;

import java.util.*;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;

public class TestSimpleMapOfInfluence {

	public static void main(String[] args) {
		// --> Inicializamos el mapa de influencias.
		// Creamos los personajes
		Character c1 = new Character(null);
		c1.setPosition(new Vector3(0.0f, 0.0f, 0.0f));
		c1.setTeam(Team.LDANIEL);
		Character c2 = new Character(null);
		c2.setPosition(new Vector3(5.0f, 3.0f, 0.0f));
		c2.setTeam(Team.FJAVIER);
		Character c3 = new Character(null);
		c3.setPosition(new Vector3(15.0f, 17.0f, 0.0f));
		c3.setTeam(Team.LDANIEL);
		Character c4 = new Character(null);
		c4.setPosition(new Vector3(10.0f, 11.0f, 0.0f));
		c4.setTeam(Team.FJAVIER);
		// Creamos la lista de objetos del mundo.
		List<WorldObject> worldObject = new LinkedList<WorldObject>();
		worldObject.add(c1);
		worldObject.add(c2);
		worldObject.add(c3);
		worldObject.add(c4);
		
		SimpleMapOfInfluence.initializeSimpleMapOfInfluence(1, 20, 20, new int[20][20], worldObject);
		
		// Actualizar.
		SimpleMapOfInfluence.updateSimpleMapOfInfluence();
		
		// Algunas comprobaciones.
		int[][] c1_map_of_influence = SimpleMapOfInfluence.getMySimpleMapOfInfluence(c1);
		int[][] c3_map_of_influence = SimpleMapOfInfluence.getMySimpleMapOfInfluence(c3);
		System.out.println("c1_map_of_influence == c3_map_of_influence -> " + (c1_map_of_influence == c3_map_of_influence));
		int[][] c2_map_of_influence = SimpleMapOfInfluence.getMySimpleMapOfInfluence(c2);
		int[][] c4_map_of_influence = SimpleMapOfInfluence.getMySimpleMapOfInfluence(c4);
		System.out.println("c2_map_of_influence == c4_map_of_influence -> " + (c2_map_of_influence == c4_map_of_influence));
		System.out.println("c2_map_of_influence == c3_map_of_influence -> " + (c2_map_of_influence == c3_map_of_influence));
		
		System.out.println("***************************************************");
		System.out.println("***************************************************");
		// Imprimimos los mapas.
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 20; x++) {
				if (c1_map_of_influence[x][y] >= 10) {
					System.out.print(c1_map_of_influence[x][y] + " ");
				} else {
					System.out.print(c1_map_of_influence[x][y] + "  ");
				}	
			}
			System.out.println();
		}
		System.out.println("***************************************************");
		System.out.println("***************************************************");
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 20; x++) {
				if (c2_map_of_influence[x][y] >= 10) {
					System.out.print(c2_map_of_influence[x][y] + " ");
				} else {
					System.out.print(c2_map_of_influence[x][y] + "  ");
				}	
			}
			System.out.println();
		}

	}
}
