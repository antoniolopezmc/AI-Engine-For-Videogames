package com.mygdx.iadevproject.aiReactive.pathfinding;

import java.util.List;

import com.badlogic.gdx.math.Vector3;

public class PathFinding {
	
	public static final int MANHATTAN_DISTANCE = 0;
	public static final int EUCLIDEAN_DISTANCE = 1;
	public static final int CHEBYSHEV_DISTANCE = 2;
	
	/**
	 * Aplica un pathfinding desde un punto origen a un punto destino, según un mapa de coste del terreno determinado y según una matriz de distancias determinada.
	 * @param map_of_costs Mapa de costes del terreno.
	 * @param distance Tipo distancia para calcular la matriz de distancias.
	 * @param width Ancho de ambas matrices anteriores.
	 * @param height Alto de ambas matrices anteriores.
	 * @param xSource_real Coordenada X real del punto origen (punto del plano).
	 * @param ySource_real Coordenada Y real del punto origen (punto del plano).
	 * @param xGoal_real Coordenada X real del punto destino (punto del plano).
	 * @param yGoal_real Coordenada Y real del punto destino (punto del plano).
	 * @return
	 */
	public List<Vector3> applyPathFinding (int[][] map_of_costs, int distance, int width, int height, float xSource_real, float ySource_real, float xGoal_real, float yGoal_real) {
		// Primero, convertimos de coordenadas reales del plano a coordenadas del GRID ==> Truncamos el número.
		int xSource = (int) xSource_real;
		int ySource = (int) ySource_real;
		int xGoal = (int) xGoal_real;
		int yGoal = (int) yGoal_real;
		
		// Creamos el objeto correspondiente a la distancia/heurística que vamos a aplicar.
		Distance d = null;
		if (distance == MANHATTAN_DISTANCE) {
			d = new ManhattanDistance();
		} else if (distance == EUCLIDEAN_DISTANCE) {
			d = new EuclideanDistance();
		} else {
			d = new ChebyshevDistance();
		}
		
		// Ahora, aplicamos el algoritmo LRTA*.
		LRTA_star lrta_star = new LRTA_star(map_of_costs, d, width, height, xSource, ySource, xGoal, yGoal);
		List<Vector3> list = lrta_star.applyLRTA_start();
		
		// Finalmente, volvemos a convertir las posiciones inicial y final del grid al plano.
		list.get(0).x = xSource_real;
		list.get(0).y = ySource_real;
		
		list.get(list.size()-1).x = xGoal_real;
		list.get(list.size()-1).y = yGoal_real;
		
		return list;
		
	}
}
