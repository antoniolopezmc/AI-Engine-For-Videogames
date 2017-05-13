package com.mygdx.iadevproject.aiReactive.pathfinding;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.pathfinding.continuous.Continuous_LRTA_star;

public class TestLRTA_star3 {

	// Programa de prueba
	public static void main(String[] args) {
		int[][] costesDelTerreno = new int[4][3];
		for (int x = 0; x <4; x++) {
			for (int y = 0; y <3; y++) {
				costesDelTerreno[x][y] = 1;
			}
		}
		costesDelTerreno[2][0] = Integer.MAX_VALUE;
		costesDelTerreno[2][1] = Integer.MAX_VALUE;
		costesDelTerreno[1][1] = Integer.MAX_VALUE;
		
		for (int y = 0; y <3; y++) {
			for (int x = 0; x <4; x++) {
				if (costesDelTerreno[x][y] == Integer.MAX_VALUE) {
					System.out.print("9 ");
				} else {
					System.out.print(costesDelTerreno[x][y] + " ");
				}
			}
			System.out.println();
		}
		
		System.out.println("********************************************");
		
		Continuous_LRTA_star lrta_star = new Continuous_LRTA_star(costesDelTerreno, new EuclideanDistance(), 4, 3, 1, 0, 3, 0);
		List<Vector3> lista = lrta_star.applyLRTA_start();
		
		int contador = 0;
		for (Vector3 vector3 : lista) {
			System.out.println("Paso " + contador);
			int valorAntiguo = costesDelTerreno[(int) vector3.x][(int) vector3.y];
			costesDelTerreno[(int) vector3.x][(int) vector3.y] = 0;
			for (int y = 0; y <3; y++) {
				for (int x = 0; x <4; x++) {
					if (costesDelTerreno[x][y] == Integer.MAX_VALUE) {
						System.out.print("9 ");
					} else {
						System.out.print(costesDelTerreno[x][y] + " ");
					}
				}
				System.out.println();
			}
			contador++;
			costesDelTerreno[(int) vector3.x][(int) vector3.y] = valorAntiguo;
			System.out.println("********************************************");
		}	
	}
}
