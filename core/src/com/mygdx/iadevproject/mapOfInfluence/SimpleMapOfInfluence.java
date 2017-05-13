package com.mygdx.iadevproject.mapOfInfluence;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.model.formation.Formation;
import com.mygdx.iadevproject.model.Character;

/**
 * Muy importante.
 * Para usar el mapa de influencia en el pathfinding táctico, habría que pasar el mapa DEL EQUIPO CONTRARÍA.
 * El pathfinding debe tener en cuenta la influencia de equipo rival (coste añadido) para NO IR por esas zonas.
 * No tiene sentido usar el pathfinding de mi propio equipo, ya que estaría penalizando las zonas controladas por mí. 
 *
 */
public class SimpleMapOfInfluence {
	
	// Valor por defecto de influencia que ejerce una unidad cualquiera. Valor estándar para todas las unidades.
	private static int default_influenceValue = 10;
	// Valor por defecto de la distancia (¡¡¡¡HORIZONTAL/VERTICAL!!!!) a la que llega la influencia de una unidad.
	// IMPORTANTE -> DISNTACIA 0 QUIERE DECIR QUE EL PERSONAJE SOLO TIENE INFLUENCIA SOBRE LA CASILLA EN LA QUE SE ENCUENTRA.
	private static int default_influenceDistance = 4;
	// MUY MUY IMPORTANTE.
	// El personaje provoca una influencia de X sobre la casilla donde se encuentra. Conforme nos vamos alejando, a la influencia se le va restando 1.
	// ===> Es muy importante tener en cuenta que un personaje dejará de influir sobre una casilla si la influencia o la distancia llegan a 0.
	
	// Longitud del lado de las celdas del grid (map_of_costs).
	private static int grid_cell_size;
	// Anchura y altura de las matrices de influencia. (anchura y altura del mapa / grid_cell_size)
	private static int grid_width, grid_height;
	// También vamos a necesotar el mapa de costes del terreno (para, a la hora de dibujar, no dibujar sobre terrenos infranqueables).
	public static int[][] map_of_costs;
	// Personajes del mundo a tener en cuenta.
	public static List<WorldObject> worldObjects;
	
	// IMPORTANTE.
	// Cada equipo tendrá su matriz de influencia numérica. Cuando vayamos a dibujar, tendremos en cuenta el contenido de ambas.
	private static int[][] simpleMapOfInfluence_LDANIEL;
	private static int[][] simpleMapOfInfluence_FJAVIER;
	// Al colorear una casilla (x, y), restamos en valores máximo de esa casilla en ambas matrices menos el valor mínimo de esa casilla en ambas matrices (recordando a qué equipo pertenece el valor máximo).
	// 	Tras realizar esta operación, el color final se elije en función del equipo que domina la casilla (valor máximo recordado) y en función del valor de la resta obtenido (tono final de color en función del valor de la resta).
	
	// Método que devuelve la matriz de influencia concreta de un personaje de entrada.
	private static int[][] getMySimpleMapOfInfluence(Character source) {
		if (source.getTeam() == Team.FJAVIER) {
			return simpleMapOfInfluence_FJAVIER;
		} else if (source.getTeam() == Team.LDANIEL) {
			return simpleMapOfInfluence_LDANIEL;
		} else {
			return null;
		}
	}
	
	/**
	 * Método que calcula las posiones adyacente de una posición determinada CUYO VALOR SEA 0.
	 * @param position Posición a la que se calcularán sus adyacentes.
	 * @param simpleMapOfInfluence Mapa de influencia.
	 * @return Lista de posiciones adyacentes.
	 */
	private static List<Vector3> generateSuccessors (Vector3 position, int[][] simpleMapOfInfluence) {
		// Creamos la lista que será devuelta.
		List<Vector3> result = new LinkedList<Vector3>();
		
		// Extraemos las coordenadas del vector.
		float x = position.x;
		float y = position.y;
		
		if (x > 0) {
			if (simpleMapOfInfluence[(int)x-1][(int)y] == 0) {
				result.add(new Vector3(x-1, y, 0.0f));
			}
			
		}
		if (x < (grid_width-1)) {
			if (simpleMapOfInfluence[(int)x+1][(int)y] == 0) {
				result.add(new Vector3(x+1, y, 0.0f));
			}
		}
		if (y > 0) {
			if (simpleMapOfInfluence[(int)x][(int)y-1] == 0) {
				result.add(new Vector3(x, y-1, 0.0f));
			}
		}
		if (y < (grid_height-1)) {
			if (simpleMapOfInfluence[(int)x][(int)y+1] == 0) {
				result.add(new Vector3(x, y+1, 0.0f));
			}
		}

		if ((x > 0) && (y > 0)) {
			if (simpleMapOfInfluence[(int)x-1][(int)y-1] == 0) {
				result.add(new Vector3(x-1, y-1, 0.0f));
			}
		}
		if ((x > 0) && (y < (grid_height-1))) {
			if (simpleMapOfInfluence[(int)x-1][(int)y+1] == 0) {
				result.add(new Vector3(x-1, y+1, 0.0f));
			}
		}
		if ((x < (grid_width-1)) && (y > 0)) {
			if (simpleMapOfInfluence[(int)x+1][(int)y-1] == 0) {
				result.add(new Vector3(x+1, y-1, 0.0f));
			}
		}
		if ((x < (grid_width-1)) && (y < (grid_height-1))) {
			if (simpleMapOfInfluence[(int)x+1][(int)y+1] == 0) {
				result.add(new Vector3(x+1, y+1, 0.0f));
			}
		}
		// IMPORTANTE -> En esta lista hay siempre como mínimo 3 elementos.
		return result;
	}
	
	/**
	 * Añadimos la influencia mediante inundación.
	 */
	// Añade la influencia de un personaje en una casilla de su matriz correspondiente, solo si la casilla está a la distancia suficiente para que el personaje pueda ejercer influencia sobre ella.
	// ==> En la primera llamada, el parámetro cell será la posición del personaje source (¡¡¡TRANSFORMADA A POSICIÓN DE LA MATRIZ!!!).
	// EXTREMADAMENTE IMPORTANTE -> Cell es una posición DE LA MATRIZ DE INFLUENCIA.
	private static void addInfluenceOfCharacter (Character character, Vector3 cell, int distance, int value) {
		if ((distance >= 0) && (value > 0) && (character.getTeam() != Team.NEUTRAL)) {
			// Obtenemos la matriz de influencia según el equipo del personaje.
			int[][] simpleMapOfInfluence = getMySimpleMapOfInfluence(character);
			// MUY IMPORTANTE -> La celda de la matriz de influencia solo se modifica si vale 0 (es decir, si no se ha modificado previamente).
			if (simpleMapOfInfluence[(int)cell.x][(int)cell.y] != 0) {
				// Añadimos el valor de la influencia a la matriz de influencia.
				simpleMapOfInfluence[(int)cell.x][(int)cell.y] = value;
				// Obtenemos las celdas adyacentes CUYO VALOR SEA 0 (es decir, aquellas vecinas a la que realmente debemos ir).
				List<Vector3> vecinas = generateSuccessors(cell, simpleMapOfInfluence);
				// IMPORTANTE -> Antes de llamar recursivamente a las vecinas, añadimos su influencia.
				// 	Esto se hace para que una vecina no "retroceda" y no vaya a otras vecinas que no le corresponden.
				for (Vector3 vector3 : vecinas) {
					if (simpleMapOfInfluence[(int)vector3.x][(int)vector3.y] == 0) {
						simpleMapOfInfluence[(int)vector3.x][(int)vector3.y] = value-1;
					}	
				}
				// Recorremos las vecinas.
				for (Vector3 vector3 : vecinas) {
					// Conforme nos alejamos del origen, el valor se decrementa en 1.
					// Además, para representar el movimiento hacia afuera y para controlar la condición de parada de este método, a la distancia también se le resta 1.
					addInfluenceOfCharacter(character, vector3, distance-1, value-1);
				}
			}	
		}
	}

	/**
	 * Inicializa todas las variables necesarias.
	 */
	public static void initializeSimpleMapOfInfluence() {
		grid_cell_size = IADeVProject.GRID_CELL_SIZE;
		grid_width = IADeVProject.GRID_WIDTH;
		grid_height = IADeVProject.GRID_HEIGHT;
		map_of_costs = IADeVProject.MAP_OF_COSTS;
		worldObjects = IADeVProject.worldObjects;
		
		// Los mapas de influencia de cada jugador no se crean ni se inicializan. Eso se hará en cada update.
	}
	
	/**
	 * Actualiza las matrices de influencia.
	 */
	public static void updateSimpleMapOfInfluence() {
		// IMPORTANTE -> Cada vez que actualizo, las matrices deben limpiarse.
		// Equipo de arriba.
		simpleMapOfInfluence_LDANIEL = new int[grid_width][grid_height];
		// Equipo de abajo.
		simpleMapOfInfluence_FJAVIER = new int[grid_width][grid_height];
		
		for (WorldObject wo : worldObjects) {
			// Solo se tendrán en cuenta los personajes como tal a la hora de crear el mapa de influencia.
			if ((wo instanceof Character) && (!(wo instanceof Formation))) {
				Character character = (Character) wo;
				addInfluenceOfCharacter (character, mapPositionTOgridPosition(grid_cell_size, wo.getPosition()), default_influenceDistance, default_influenceValue);
			}
		}
	}
	
	/**
	 * Método que transforma una posición del plano o mapa real en una posición del grid.
	 * @param grid_cell_size Longitud del lado de las celdas del grid.
	 * @param mapPosition Posición real del mapa.
	 * @return Posición del grid.
	 */
	private static Vector3 mapPositionTOgridPosition (int grid_cell_size, Vector3 mapPosition) {
		// Eliminamos los decimales haciendo el casting.
		int gridPosition_x = (int) mapPosition.x/grid_cell_size;
		int gridPosition_y = (int) mapPosition.y/grid_cell_size;
		int gridPosition_z = 0;
		return new Vector3(gridPosition_x, gridPosition_y, gridPosition_z);
	}
	
	/**
	 * Método para dibujar el mapa de influencia final.
	 * @param renderer Renderer sobre el que dibujar.
	 */
	// Se dibujarán CUADRADOS de distinto color.
	public static void drawInfluenceMap (ShapeRenderer renderer) {
		
	}
}
