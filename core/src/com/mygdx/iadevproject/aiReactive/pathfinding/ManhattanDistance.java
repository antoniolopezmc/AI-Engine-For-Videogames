package com.mygdx.iadevproject.aiReactive.pathfinding;

public class ManhattanDistance implements Distance {

	// Métodos.
	@Override
	public float[][] getMatrixOfDistances(int width, int height, int xGoal, int yGoal) {
		// Creamos la matriz que será devuelta.
		float[][] result = new float[width][height];
		// La posición del objeto origen se inicializa a 0.
		result[xGoal][yGoal] = 0.0f;
		// Recorremos la matriz para calcular el valor del resto de posiciones.
		for (int x = 0; x<width; x++) {
			for (int y = 0; y<height; y++) {
				result[x][y] = Math.abs(xGoal - x) + Math.abs(yGoal - y);
			}
		}	
		// Devolvemos el resultado.
		return result;
	}
	
	// Programa de prueba.
	public static void main(String[] args) {
		Distance d = new ManhattanDistance();
		float[][] matriz = d.getMatrixOfDistances(20, 14, 5, 12);
		for (int y = 0; y<14; y++) {
			for (int x = 0; x<20; x++) {
				if (matriz[x][y] < 10) {
					System.out.print(matriz[x][y] + "     ");
				} else {
					System.out.print(matriz[x][y] + "    ");
				}
			}
			System.out.println();
		}
	}

}
