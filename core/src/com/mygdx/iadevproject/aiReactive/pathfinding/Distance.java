package com.mygdx.iadevproject.aiReactive.pathfinding;

public interface Distance {
	/**
	 * Método que calcula y devuelve la matriz de distancias (desde cada uno de los puntos a un punto objetivo).
	 * La posición del objetivo debe estar dentro de las dimesiones de la matriz.
	 * @param width Anchura de la matriz.
	 * @param height Altura de la matriz
	 * @param xGoal Posición X del objetico
	 * @param yGoal Posición Y del objetivo
	 * @return La matriz de distancia según la distancia concreta elegida.
	 */
	public float[][] getMatrixOfDistances(int matrix_width, int matrix_height, int xGoal, int yGoal);
}
