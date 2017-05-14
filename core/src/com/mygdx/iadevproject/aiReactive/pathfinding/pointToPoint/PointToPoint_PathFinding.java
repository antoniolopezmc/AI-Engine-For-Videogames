package com.mygdx.iadevproject.aiReactive.pathfinding.pointToPoint;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.pathfinding.ChebyshevDistance;
import com.mygdx.iadevproject.aiReactive.pathfinding.Distance;
import com.mygdx.iadevproject.aiReactive.pathfinding.EuclideanDistance;
import com.mygdx.iadevproject.aiReactive.pathfinding.ManhattanDistance;
import com.mygdx.iadevproject.model.Character;

public class PointToPoint_PathFinding {
	
	public static final int MANHATTAN_DISTANCE = 0;
	public static final int EUCLIDEAN_DISTANCE = 1;
	public static final int CHEBYSHEV_DISTANCE = 2;
	
	// Personaje que ejecuta el pathfinding.
	private Character source;
	// Objetico actual al que debe ir el personaje. Atributo interno.
	// =====> EXTREMADAMENTE IMPORTANTE -> Coordenadas del grid.
	private Vector3 objetivoActual;
	// No hace falta que el personaje llegue extactamente al objetivo actual.
	// 	Es suficiente con acercarse lo suficiente. Eso es lo que indica este atributo.	
	private float radious;
	
	// Distancia/Heurística para calcular la matriz de distancias.
	private Distance distance;
	// Longitud del lado de las celdas del grid (map_of_costs).
	private int grid_cell_size;
	// Posición inicial real del mapa del personaje.
	private float xSource_real, ySource_real;
	// Posición real del mapa del destino.
	private float xGoal_real, yGoal_real;
	// Posición inicial DEL GRID del personaje.
	private int xSource, ySource;
	// Posición DEL GRID del destino.
	private int xGoal, yGoal;
	// Mapa de costes del terreno.
	private int[][] map_of_costs;
	// Anchura y altura DEL GRID (del mapa de costes).
	private int matrix_width, matrix_height;
	
	// El pathfinding también debe almacenar el objeto de tipo 'PointToPoint_LRTA_star', para no crearlo cada vez que se llame.
	private PointToPoint_LRTA_star lrta_star;
	
	// Flag que indica si el pathfinding debe usar información táctica (la matriz de influencia, por ejemplo). Por defecto, no se usa.
	// IMPORTANTE -> En el caso concreto de la matriz de influencia, NO SE DEBE ALMACENAR COMO ATRIBUTO, ya que está matriz estará cambiando continuamente.
	// ESTE FLAG SE PUEDE IR ACTIVANDO O DESACTIVANDO SOBRE LA MARCHA.
	private boolean tacticalInformation;
	
	/**
	 * Constructor de la clase pathfinding.
	 * @param source Personaje que ejecuta el pathfinding.
	 * @param radious Radio de satisfacción de los puntos a los que ir. No hace falta que el personaje llegue extactamente al siguiente objetivo actual. Es suficiente con acercarse lo suficiente.
	 * @param distance
	 * @param grid_cell_size
	 * @param xSource_real
	 * @param ySource_real
	 * @param xGoal_real
	 * @param yGoal_real
	 * @param map_of_costs
	 * @param matrix_width
	 * @param matrix_height
	 */
	public PointToPoint_PathFinding(Character source, float radious, int distance, int grid_cell_size, float xSource_real, float ySource_real, float xGoal_real, float yGoal_real,
			int[][] map_of_costs, int matrix_width, int matrix_height) {
		this.source = source;
		this.radious = radious;
		
		// Creamos el objeto correspondiente a la distancia/heurística que vamos a aplicar.
		if (distance == MANHATTAN_DISTANCE) {
			this.distance = new ManhattanDistance();
		} else if (distance == EUCLIDEAN_DISTANCE) {
			this.distance = new EuclideanDistance();
		} else {
			this.distance = new ChebyshevDistance();
		}
		
		// Almacenamos grid_cell_size
		this.grid_cell_size = grid_cell_size;
		
		// Almacenamos las coordenadas reales del mapa y convertimos de coordenadas reales del plano a coordenadas del GRID.
		this.xSource_real = xSource_real;
		this.ySource_real = ySource_real;
		this.xGoal_real = xGoal_real;
		this.yGoal_real = yGoal_real;
		Vector3 source_gridPosition = IADeVProject.mapPositionTOgridPosition(grid_cell_size, new Vector3(xSource_real, ySource_real, 0.0f));
		Vector3 goal_gridPosition = IADeVProject.mapPositionTOgridPosition(grid_cell_size, new Vector3(xGoal_real, yGoal_real, 0.0f));
		this.xSource = (int) source_gridPosition.x;
		this.ySource = (int) source_gridPosition.y;
		this.xGoal = (int) goal_gridPosition.x;
		this.yGoal = (int) goal_gridPosition.y;
		
		// Almacenamos el mapa de costes
		this.map_of_costs = map_of_costs;
		// Almacenamos las dimesiones de ese mapa.
		this.matrix_width = matrix_width;
		this.matrix_height = matrix_height;
		
		// Transformamos la posición del mapa al grid, y volvemos a transformar al mapa. ESTE SERÁ EL PRIMER OBJETIVO ACTUAL.
		// Con esta transformación estamos consiguiendo que el personaje se situe en el centro del tile.
		this.objetivoActual = new Vector3(IADeVProject.mapPositionTOgridPosition(grid_cell_size, this.source.getPosition()));
		
		lrta_star = new PointToPoint_LRTA_star(map_of_costs, this.distance, matrix_width, matrix_height, xGoal, yGoal);
		
		this.tacticalInformation = false;
	}

	/**
	 * Aplica el algoritmo LRTA* para obtener el siguiente punto al que ir.
	 * @return Una lista que contiene el siguiente punto al que ir.
	 */
	public List<Vector3> applyPathFinding () {
		// IMPORTANTE.
		// Nada más entrar a este método se realizar algunas comprobaciones:
		// * Si estamos en la posición FINAL DEL MAPA, devolvemos dicha posición.
		// * Si hemos llegado a la siguiente posición objetivo y esa posición es la posición final DEL GRID, entonces vamos a la posición FINAL DEL MAPA.
		// * Si NO hemos llegado a la siguiente posición, devolvemos UNA LISTA CON ESE ÚNICO PUNTO.
		// * Si hemos llegado a la siguiente posición objetivo y esa posición NO es la posición final DEL GRID, seguimos con la ejecución del pathfinding.
		
		// Calculamos la dirección (el vector) y distancia entre la fuente y el PUNTO FINAL DEL MAPA.
		Vector3 direction = new Vector3(xGoal_real, yGoal_real, 0.0f);
		direction = direction.sub(this.source.getPosition());
		float distanceBetweenSourceAndGoal = direction.len(); // Módulo del vector 'direction'.
		// Si estamos en la posicíon objetivo DEL MAPA, devolvemos dicha posición.
		if (distanceBetweenSourceAndGoal < this.radious) {
			List<Vector3> salida = new LinkedList<Vector3>();
			salida.add(new Vector3(xGoal_real, yGoal_real, 0.0f));
			return salida; 
		}
		
		// Calculamos la dirección (el vector) y distancia entre la fuente y el OBJETIVO ACTUAL (el punto donde tenemos que ir).
		Vector3 objetivoActual_mapa = IADeVProject.gridPositionTOmapPosition(grid_cell_size, objetivoActual);
		direction = new Vector3(objetivoActual_mapa);
		direction = direction.sub(this.source.getPosition());
		float distanceBetweenSourceAndTarger = direction.len(); // Módulo del vector 'direction'.
		
		// Si hemos llegado a la siguiente posición objetivo y esa posición es la posición final DEL GRID, entonces vamos a la posición FINAL DEL MAPA.
		if ((distanceBetweenSourceAndTarger < this.radious) && (objetivoActual_mapa.x == xGoal) && (objetivoActual_mapa.x == xGoal)) {
			List<Vector3> salida = new LinkedList<Vector3>();
			salida.add(new Vector3(xGoal_real, yGoal_real, 0.0f));
			return salida;
		}
		
		// Si no hemos llegado al objetivo actual (distancia mayor que es radio), lo devolvemos como siguiente punto al que ir.
		if (distanceBetweenSourceAndTarger > this.radious) {
			List<Vector3> salida = new LinkedList<Vector3>();
			salida.add(new Vector3(objetivoActual_mapa));
			return salida;
		}
		// Si ya hemos llegado y el punto no es el final, seguimos con la ejecución del pathfinding.
	
		// Comprobamos si debe usarse información táctica.
		if (tacticalInformation) {
			lrta_star.useTactialInformation();
		} else {
			lrta_star.notUseTacticalInformation();
		}
		
		// Ahora, aplicamos el algoritmo LRTA*.
		Vector3 vector3 = lrta_star.applyLRTA_start((int)objetivoActual.x, (int)objetivoActual.y);
		
		// IMPORTANTE -> Actualizamos el objetivo actual.
		this.objetivoActual = new Vector3(vector3);
		
		// Finalmente, pasamos las coordenadas obtenidas (coordenadas del grid) a coordenadas reales del plano.
		Vector3 modificado = IADeVProject.gridPositionTOmapPosition(grid_cell_size, vector3);
		
		// Creamos una lista y devolvemos ese elemento.
		List<Vector3> list = new LinkedList<Vector3>();
		list.add(modificado);
		
		return list;
		
	}
	
	/**
	 * Método para habilitar el uso de información táctica en el pathfinding.
	 */
	public void useTactialInformation() {
		this.tacticalInformation = true;
	}
	
	/**
	 * Método para deshabilitar el uso de información táctica en el pathfinding.
	 */
	public void notUseTacticalInformation() {
		this.tacticalInformation = false;
	}
}
