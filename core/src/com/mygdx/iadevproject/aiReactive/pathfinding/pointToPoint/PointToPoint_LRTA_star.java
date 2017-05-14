package com.mygdx.iadevproject.aiReactive.pathfinding.pointToPoint;

import java.util.*;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.pathfinding.Distance;


// IMPORTANTE -> LRTA* con espacio de búsqueda minimal, es decir, el espacio de búsqueda es solo el estado actual.
public class PointToPoint_LRTA_star {

	// --> Coste de pasar de una celda a otra adyacente (tanto en diagonal, como en horizontal, como en vertical).
	private static final float default_action_cost = 1.0f;
	
	// Mapa de costes del terreno.
	private int[][] map_of_costs;
	// Anchura y altura de las matrices con las que se está trabajando.
	private int matrix_width, matrix_height;

	// IMPORTANTE -> En este caso, la posición origen del grid no es necesaria, puesto que cada vez que se llame al LRTA se hará con una posición de origen distinta 
	// 		(el 'objetivoActual' del pathfinding -> Coordenada actual en el grid).
	
	// Coordenadas del punto destino ===> Será el punto objetivo de la matriz de distancias/costes.
	// COORDENADAS DEL GRID, NO SON LAS COORDENADAS REALES.
	private int xGoal, yGoal;
	
	// Matriz de distancias hacia el objetivo.
	private float[][] distancesMatrix;
	
	// Flag que indica si el algoritmo LRTA* debe usar información táctica (la matriz de influencia, por ejemplo). Por defecto, no se usa.
	// ESTE FLAG SE PUEDE IR ACTIVANDO O DESACTIVANDO SOBRE LA MARCHA.
	private boolean tacticalInformation;
	
	// Constructor.
	public PointToPoint_LRTA_star(int[][] map_of_costs, Distance distance, int matrix_width, int matrix_height, int xGoal, int yGoal) {
		this.map_of_costs = map_of_costs;
		this.matrix_width = matrix_width;
		this.matrix_height = matrix_height;
		this.xGoal = xGoal;
		this.yGoal = yGoal;
		// Creamos la matriz de distancias al objetivo.
		this.distancesMatrix = distance.getMatrixOfDistances(this.matrix_width, this.matrix_height, this.xGoal, this.yGoal);
	}
	
	/**
	 * Devuelve el siguiente punto a partir del punto actual.
	 * @param xActual Coordenada X del punto actual.
	 * @param yActual Coordenada Y del punto actual.
	 * @return siguiente punto al que ir.
	 */
	public Vector3 applyLRTA_start(int xActual, int yActual) {
		// Nada más empezar comprobamos si la posición actual es la final. Es ese caso, la devolvemos directamente.
		if ((xActual == xGoal) && (yActual == yGoal)) {
			return new Vector3((float) xActual, (float) yActual, 0.0f);
		}
		
		// Creamos un vector para almacenar la posición actual. Al principio, corresponderá con la posición inicial.
		Vector3 position = new Vector3((float) xActual, (float) yActual, 0.0f);
	
		// Calculamos la siguiente posición a la que debemos ir.
		Vector3 nextCell = getSuccessorWithTheSmallestHeuristic(distancesMatrix, this.generateSuccessors(position)); // Seleccionamos el vecino al que ir.
		distancesMatrix[(int) position.x][(int) position.y] = Math.max(distancesMatrix[(int) position.x][(int) position.y], this.getFx(distancesMatrix, nextCell)); // Actualizo matriz de costes / matriz de h.
		
		// Devuelvo la siguiente posición a la que ir.
		return nextCell;
	}
	
	// f(x) = coste de acción + coste heurístico en ese punto + coste del terreno en ese punto.
	/**
	 * Calcula f(p) en un punto.
	 * @param distancesMatrix Matriz de distancias a un punto.
	 * @param position Punto p.
	 * @return f(p) = coste de acción + coste heurístico en ese punto + coste del terreno en ese punto
	 */
	private float getFx (float[][] distancesMatrix, Vector3 position) {
		return PointToPoint_LRTA_star.default_action_cost + distancesMatrix[(int) position.x][(int) position.y] + (float) this.map_of_costs[(int) position.x][(int) position.y];
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
		if (x < (this.matrix_width-1)) {
			result.add(new Vector3(x+1, y, 0.0f));
		}
		if (y > 0) {
			result.add(new Vector3(x, y-1, 0.0f));
		}
		if (y < (this.matrix_height-1)) {
			result.add(new Vector3(x, y+1, 0.0f));
		}

		if ((x > 0) && (y > 0)) {
			result.add(new Vector3(x-1, y-1, 0.0f));
		}
		if ((x > 0) && (y < (this.matrix_height-1))) {
			result.add(new Vector3(x-1, y+1, 0.0f));
		}
		if ((x < (this.matrix_width-1)) && (y > 0)) {
			result.add(new Vector3(x+1, y-1, 0.0f));
		}
		if ((x < (this.matrix_width-1)) && (y < (this.matrix_height-1))) {
			result.add(new Vector3(x+1, y+1, 0.0f));
		}
		// IMPORTANTE -> En esta lista hay siempre como mínimo 3 elementos.
		return result;
	}
	
	/**
	 * Método para habilitar el uso de información táctica en el algoritmo LRTA*.
	 */
	public void useTactialInformation() {
		this.tacticalInformation = true;
	}
	
	/**
	 * Método para deshabilitar el uso de información táctica en el algoritmo LRTA*.
	 */
	public void notUseTacticalInformation() {
		this.tacticalInformation = false;
	}
}
