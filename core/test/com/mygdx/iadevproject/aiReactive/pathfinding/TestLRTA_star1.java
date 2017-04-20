package com.mygdx.iadevproject.aiReactive.pathfinding;

import java.util.List;

import com.badlogic.gdx.math.Vector3;

public class TestLRTA_star1 {

	// Programa de prueba
	public static void main(String[] args) {
		int[][] costesDelTerreno = new int[10][10];
		for (int x = 0; x <10; x++) {
			for (int y = 0; y <5; y++) {
				costesDelTerreno[x][y] = 1;
			}
		}
		for (int x = 0; x <10; x++) {
			for (int y = 5; y <10; y++) {
				costesDelTerreno[x][y] = 2;
			}
		}
		for (int x = 0; x <5; x++) {
			for (int y = 4; y <6; y++) {
				costesDelTerreno[x][y] = Integer.MAX_VALUE;
			}
		}
		
		for (int y = 0; y <10; y++) {
			for (int x = 0; x <10; x++) {
				if (costesDelTerreno[x][y] == Integer.MAX_VALUE) {
					System.out.print("9 ");
				} else {
					System.out.print(costesDelTerreno[x][y] + " ");
				}
			}
			System.out.println();
		}
		
		System.out.println("********************************************");
		
		LRTA_star lrta_star = new LRTA_star(costesDelTerreno, new EuclideanDistance(), 10, 10, 0, 0, 0, 9);
		List<Vector3> lista = lrta_star.applyLRTA_start();
		
		int contador = 0;
		for (Vector3 vector3 : lista) {
			System.out.println("Paso " + contador);
			int valorAntiguo = costesDelTerreno[(int) vector3.x][(int) vector3.y];
			costesDelTerreno[(int) vector3.x][(int) vector3.y] = 0;
			for (int y = 0; y <10; y++) {
				for (int x = 0; x <10; x++) {
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
