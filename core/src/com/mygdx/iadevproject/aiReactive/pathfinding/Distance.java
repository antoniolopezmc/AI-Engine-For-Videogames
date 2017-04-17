package com.mygdx.iadevproject.aiReactive.pathfinding;

public interface Distance {
	// Los distintos tipos de distancias son singleton, porque realmente no tiene sentido crear varias instancias.
	//	Lo único interesante de estas clases en el método 'getMatrixOfDistances' que nos devuelve la matriz de distancias deseada.
	
	/**
	 * Método que calcula y devuelve la matriz de distancias desde un punto determinado y usando un tipo de distancia determinado.
	 * La posición del objeto origen debe estar dentro de las dimesiones de la matriz.
	 * @param width Anchura de la matriz.
	 * @param height Altura de la matriz
	 * @param xSource Posición X del objeto origen.
	 * @param ySource Posición Y del objeto origen.
	 * @return La matriz de distancia según la distancia concreta elegida.
	 */
	public float[][] getMatrixOfDistances(int width, int height, int xSource, int ySource);
}
