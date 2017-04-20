package com.mygdx.iadevproject.aiReactive.pathfinding;

import java.util.*;

import com.badlogic.gdx.math.Vector3;

public class LRTA_star {

	// --> Coste de pasar de una celda a otra adyacente (tanto en diagonal, como en horizontal, como en vertical).
	private static final float default_action_cost = 1.0f;
	
	// Mapa de costes del terreno.
	private int[][] map_of_costs;
	// Distancia concreta elegida para aplicar el algoritmo.
	private Distance distance;
	// Anchura y altura de las matrices con las que se está trabajando.
	private int width, height;
	// Coordenadas del punto origen. COORDENADAS DEL GRID, NO SON LAS COORDENADAS REALES.
	private int xSource, ySource;
	// Coordenadas del punto destino ===> Será el punto objetivo de la matriz de distancias/costes.
	// COORDENADAS DEL GRID, NO SON LAS COORDENADAS REALES.
	private int xGoal, yGoal;
	
	// Constructor.
	public LRTA_star(int[][] map_of_costs, Distance distance, int width, int height, int xSource, int ySource, int xGoal, int yGoal) {
		this.map_of_costs = map_of_costs;
		this.distance = distance;
		this.width = width;
		this.height = height;
		this.xSource = xSource;
		this.ySource = ySource;
		this.xGoal = xGoal;
		this.yGoal = yGoal;
	}
	
	/**
	 * Calcula el camino (lista de puntos) desde el origen hasta el destino (ambos incluidos).
	 * @return Lista de puntos desde el origen hasta el destino.
	 */
	public List<Vector3> applyLRTA_start() {
		// Creamos la lista que será devuelta.
		List<Vector3> result = new LinkedList<Vector3>();
		// Creamos la matriz de distancias al objetivo.
		float[][] distancesMatrix = distance.getMatrixOfDistances(this.width, this.height, this.xGoal, this.yGoal);
		// Creamos un vector para almacenar la posición actual. Al principio, corresponderá con la posición inicial.
		Vector3 position = new Vector3((float) xSource, (float) ySource, 0.0f);
		// Añadimos la posición inicial a la lista.
		result.add(new Vector3(position)); // Añadimos una copia para evitar aliasing.
		// Creamos un vector para almacenar la posición final/objetivo.
		Vector3 finalPosition = new Vector3((float) xGoal, (float) yGoal, 0.0f);
		
		Vector3 nextCell = null;
		
		while (!position.equals(finalPosition)) { // Mientras la posición actual sea distinta a la posición final.
			nextCell = getSuccessorWithTheSmallestHeuristic(distancesMatrix, this.generateSuccessors(position)); // Seleccionamos el vecino al que ir.
			distancesMatrix[(int) position.x][(int) position.y] = Math.max(distancesMatrix[(int) position.x][(int) position.y], this.getFx(distancesMatrix, nextCell)); // Actualizo matriz de costes / matriz de h.
			position = nextCell; // Actualizo la posición actual.
			result.add(new Vector3(position)); // Añado el elementos a la lista resultante.
		}
		
		// La posición final ya se ha añadido a la lista en la última iteración.
		
		return result;
	}
	
	// f(x) = coste de acción + coste heurístico en ese punto + coste del terreno en ese punto.
	/**
	 * Calcula f(p) en un punto.
	 * @param distancesMatrix Matriz de distancias a un punto.
	 * @param position Punto p.
	 * @return f(p)
	 */
	private float getFx (float[][] distancesMatrix, Vector3 position) {
		return LRTA_star.default_action_cost + distancesMatrix[(int) position.x][(int) position.y] + (float) this.map_of_costs[(int) position.x][(int) position.y];
	}
	
	/**
	 * Dada una matriz de distancias y una lista de posiciones adyacentes, devuelve aquella tal que f(x) sea menor.
	 * f(x) = coste de acción + coste heurístico en un punto + coste del terreno en ese punto.
	 * @param distancesMatrix Matriz de distancias a un punto.
	 * @param successors Lista de sucesores a analizar.
	 * @return
	 */
	private Vector3 getSuccessorWithTheSmallestHeuristic (float[][] distancesMatrix, List<Vector3> successors) {
		// En la lista de sucesores de entrada siempre debe haber como mínimo 3 elementos.
		List<Vector3> listElems = new LinkedList<Vector3>(successors);
		// Eliminamos y obtenemos el primero de ellos.
		Vector3 result = listElems.remove(0);
		// Recorremos la lista resultante buscando el sucesor (celda adyacente) con menor valor heurístico.
		for (Vector3 vector3 : listElems) {
			if (this.getFx(distancesMatrix, vector3) < this.getFx(distancesMatrix, result)) {
				result = new Vector3(vector3);
			}
		}
		return result;
	}
	
	/**
	 * Método de calcula todos los sucesores o posiones adyacente de una posición determinada.
	 * @param position Posición a la que se calcularán sus adyacentes.
	 * @return Lista de posiciones adyacentes.
	 */
	private List<Vector3> generateSuccessors (Vector3 position) {
		// Creamos la lista que será devuelta.
		List<Vector3> result = new LinkedList<Vector3>();
		
		// Extraemos las coordenadas del vector.
		float x = position.x;
		float y = position.y;
		
		if (x > 0) {
			result.add(new Vector3(x-1, y, 0.0f));
		}
		if (x < (this.width-1)) {
			result.add(new Vector3(x+1, y, 0.0f));
		}
		if (y > 0) {
			result.add(new Vector3(x, y-1, 0.0f));
		}
		if (y < (this.height-1)) {
			result.add(new Vector3(x, y+1, 0.0f));
		}

		if ((x > 0) && (y > 0)) {
			result.add(new Vector3(x-1, y-1, 0.0f));
		}
		if ((x > 0) && (y < (this.height-1))) {
			result.add(new Vector3(x-1, y+1, 0.0f));
		}
		if ((x < (this.width-1)) && (y > 0)) {
			result.add(new Vector3(x+1, y-1, 0.0f));
		}
		if ((x < (this.width-1)) && (y < (this.height-1))) {
			result.add(new Vector3(x+1, y+1, 0.0f));
		}
		// IMPORTANTE -> En esta lista hay siempre como mínimo 3 elementos.
		return result;
	}
	
}
