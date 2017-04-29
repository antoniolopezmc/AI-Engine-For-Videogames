package com.mygdx.iadevproject.aiReactive.pathfinding;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;

public class PathFinding {
	
	public static final int MANHATTAN_DISTANCE = 0;
	public static final int EUCLIDEAN_DISTANCE = 1;
	public static final int CHEBYSHEV_DISTANCE = 2;
	
	/**
	 * Aplica un pathfinding desde un punto origen a un punto destino, según un mapa de coste del terreno determinado y según una matriz de distancias determinada.
	 * @param map_of_costs Mapa de costes del terreno.
	 * @param grid_cell_size Longitud del lado de las celdas del grid (map_of_costs).
	 * @param distance Tipo distancia para calcular la matriz de distancias.
	 * @param matrix_width Ancho de ambas matrices anteriores.
	 * @param matrix_height Alto de ambas matrices anteriores.
	 * @param xSource_real Coordenada X real del punto origen (punto del plano).
	 * @param ySource_real Coordenada Y real del punto origen (punto del plano).
	 * @param xGoal_real Coordenada X real del punto destino (punto del plano).
	 * @param yGoal_real Coordenada Y real del punto destino (punto del plano).
	 * @return Lista de puntos desde el origen hasta el destino.
	 */
	public List<Vector3> applyPathFinding (int[][] map_of_costs, int grid_cell_size, int distance, int matrix_width, int matrix_height, float xSource_real, float ySource_real, float xGoal_real, float yGoal_real) {
		// Primero, convertimos de coordenadas reales del plano a coordenadas del GRID.
		int xSource = (int) xSource_real/grid_cell_size;
		int ySource = (int) ySource_real/grid_cell_size;
		int xGoal = (int) xGoal_real/grid_cell_size;
		int yGoal = (int) yGoal_real/grid_cell_size;
		
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
		LRTA_star lrta_star = new LRTA_star(map_of_costs, d, matrix_width, matrix_height, xSource, ySource, xGoal, yGoal);
		List<Vector3> list = lrta_star.applyLRTA_start();
		
		// Finalmente, pasamos las coordenadas obtenidas (coordenadas del grid) a coordenadas reales del plano.
		for (Vector3 vector3 : list) {
			vector3.x = vector3.x * ((float) grid_cell_size);
			vector3.y = vector3.y * ((float) grid_cell_size);
			vector3.z = vector3.z * ((float) grid_cell_size);
		}
		
		list.get(0).x = xSource_real;
		list.get(0).y = ySource_real;
		
		list.get(list.size()-1).x = xGoal_real;
		list.get(list.size()-1).y = yGoal_real;
		
		return list;
		
	}
}
