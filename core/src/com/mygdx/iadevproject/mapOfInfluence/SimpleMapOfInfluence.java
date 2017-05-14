package com.mygdx.iadevproject.mapOfInfluence;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.map.Ground;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.model.formation.Formation;
import com.mygdx.iadevproject.model.Character;

/**
 * Muy importante.
 * Para usar el mapa de influencia en el pathfinding táctico, habría que pasar el mapa DEL EQUIPO CONTRARIO.
 * El pathfinding debe tener en cuenta la influencia de equipo rival (coste añadido) para NO IR por esas zonas.
 * No tiene sentido usar el pathfinding de mi propio equipo, ya que estaría penalizando las zonas controladas por mí. 
 *
 */
public class SimpleMapOfInfluence {
	
	// Valor máximo de influencia que ejerce una unidad cualquiera. Valor estándar para todas las unidades.
	// -->  MÁXIMA INFLUENCIA QUE SE PUEDE EJERCER SOBRE UNA CASILLA.
	private static int max_influenceValue = 10;
	// Valor máximo de la distancia (¡¡¡¡HORIZONTAL/VERTICAL <=> CHEBYSHEV!!!!) a la que llega la influencia de una unidad.
	// IMPORTANTE -> DISNTACIA 0 QUIERE DECIR QUE EL PERSONAJE SOLO TIENE INFLUENCIA SOBRE LA CASILLA EN LA QUE SE ENCUENTRA.
	private static int max_influenceDistance = 4;
	// MUY MUY IMPORTANTE.
	// El personaje provoca una influencia de X sobre la casilla donde se encuentra. Conforme nos vamos alejando, a la influencia se le va restando 1.
	// ===> Es muy importante tener en cuenta que un personaje dejará de influir sobre una casilla si la influencia o la distancia llegan a 0.
	
	// Longitud del lado de las celdas del grid (map_of_costs).
	private static int grid_cell_size;
	// Anchura y altura de las matrices de influencia. (anchura y altura del mapa / grid_cell_size)
	private static int grid_width, grid_height;
	// También vamos a necesotar el mapa de costes del terreno (para, a la hora de dibujar, no dibujar sobre terrenos infranqueables).
	private static int[][] map_of_costs;
	// Personajes del mundo a tener en cuenta.
	private static List<WorldObject> worldObjects;
	
	// IMPORTANTE.
	// Cada equipo tendrá su matriz de influencia numérica. Cuando vayamos a dibujar, tendremos en cuenta el contenido de ambas.
	private static int[][] simpleMapOfInfluence_LDANIEL;
	private static int[][] simpleMapOfInfluence_FJAVIER;
	// Al colorear una casilla (x, y), restamos en valores máximo de esa casilla en ambas matrices menos el valor mínimo de esa casilla en ambas matrices (recordando a qué equipo pertenece el valor máximo).
	// 	Tras realizar esta operación, el color final se elije en función del equipo que domina la casilla (valor máximo recordado) y en función del valor de la resta obtenido (tono final de color en función del valor de la resta).
	
	/**
	 * Método que devuelve la matriz de influencia concreta de un personaje de entrada (según su equipo). ¡¡SIN TENER EN CUENTA AL EQUIPO RIVAL!!.
	 * @param source Personaje de entrada.
	 * @return matriz de influencia correspondiente al equipo de ese personaje.
	 */
	public static int[][] getMySimpleMapOfInfluence(Character source) {
		if (source.getTeam() == Team.FJAVIER) {
			return simpleMapOfInfluence_FJAVIER;
		} else if (source.getTeam() == Team.LDANIEL) {
			return simpleMapOfInfluence_LDANIEL;
		} else {
			return null;
		}
	}
		
	/**
	 * Añadimos la influencia mediante inundación.
	 */
	// Añade la influencia de un personaje en una casilla de su matriz correspondiente, solo si la casilla está a la distancia suficiente para que el personaje pueda ejercer influencia sobre ella.
	private static void addInfluenceOfCharacter (Character character, int distance, int value) {
		if (character.getTeam() != Team.NEUTRAL) { // El equipo neutral no tiene mapa de influencia.
			// En primer lugar, obtenemos la posición DEL GRID a partir de la posición del mapa del personaje.
			Vector3 character_grid_position = mapPositionTOgridPosition(grid_cell_size, character.getPosition());
			// Obtenemos la matriz de influencia según el equipo del personaje.
			int[][] simpleMapOfInfluence = getMySimpleMapOfInfluence(character);
			// Añadimos el valor de la influencia a la celda inicial de la matriz de influencia (en esta celda la influencia es máxima).
			simpleMapOfInfluence[(int)character_grid_position.x][(int)character_grid_position.y] = value;
			
			// Recorremos la zona más próxima al personaje en la matriz de influencia (según la distancia), para asignar el valor que corresponda.
			for (int x = (int)Math.max(0, character_grid_position.x-distance); x <= (int)Math.min(grid_width-1, character_grid_position.x + distance); x++) {
				for (int y = (int)Math.max(0, character_grid_position.y-distance); y <= (int)Math.min(grid_height-1, character_grid_position.y + distance); y++) {
					// Calculamos el valor que se AÑADIRÁ A LA CELDA ACTUAL.
					int valueToAdd = value - (int) Math.max(Math.abs(character_grid_position.x - x), Math.abs(character_grid_position.y - y));
					// Si al añadir ese valor a la celda que corresponde, se supera el valor máximo, entonces a la celda se le asigna el valor máximo.
					if ((valueToAdd + simpleMapOfInfluence[x][y]) > value) {
						simpleMapOfInfluence[x][y] = value;
					} else { // Sino, se calcula y asigna la suma.
						simpleMapOfInfluence[x][y] += valueToAdd;
					}
	
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
	 * Inicializa todas las variables necesarios con los valores pasados como parámetro.
	 * @param grid_cell_size
	 * @param grid_width
	 * @param grid_height
	 * @param map_of_costs
	 * @param worldObjects
	 */
	public static void initializeSimpleMapOfInfluence(int grid_cell_size, int grid_width, int grid_height, int[][] map_of_costs, List<WorldObject> worldObjects) {
		SimpleMapOfInfluence.grid_cell_size = grid_cell_size;
		SimpleMapOfInfluence.grid_width = grid_width;
		SimpleMapOfInfluence.grid_height = grid_height;
		SimpleMapOfInfluence.map_of_costs = map_of_costs;
		SimpleMapOfInfluence.worldObjects = worldObjects;
		
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
				addInfluenceOfCharacter (character, max_influenceDistance, max_influenceValue);
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
	 * Método para dibujar el mapa de influencia final. SE TENDRÁN EN CUENTA LOS MAPAS DE AMBOS BANDOS.
	 * @param renderer Renderer.
	 * @param output_grid_cell_size Lado de cada uno de los cuadraditos que se dibujará.
	 * @param positionX Posición X de la esquina inferior izquierda del mapa de influencia.
	 * @param positionY Posición Y de la esquina inferior izquierda del mapa de influencia.
	 * @param filled true para que los cuadrados se rellenen, false para que no.
	 */
	// Se dibujarán CUADRADOS de distinto color.
	public static void drawInfluenceMap (ShapeRenderer renderer, int output_grid_cell_size, float positionX, float positionY, boolean filled) {
		// IMPORTANTE. Por cada equipo habrá 5 colores. 5 tonos de azul (equipo LDANIEL) y 5 tonos de rojo (equipo FJAVIER). A parte, un color neutral que será en blanco.
		// Inicializamos los colores. LOS VALORES ESTÁN ENTRE 0 y 1.
		Color neutral = new Color(1, 1, 1, 0);
		Color[] ldaniel = new Color[5];
		ldaniel[4] = new Color(0, 0, 139/255.0f, 0);
		ldaniel[3] = new Color(16.0f/255.0f, 78.0f/255.0f, 139.0f/255.0f, 0);
		ldaniel[2] = new Color(24.0f/255.0f, 116.0f/255.0f, 205.0f/255.0f, 0);
		ldaniel[1] = new Color(122.0f/255.0f, 197.0f/255.0f, 205.0f/255.0f, 0);
		ldaniel[0] = new Color(142.0f/255.0f, 229.0f/255.0f, 238.0f/255.0f, 0);
		Color[] fjavier = new Color[5];
		fjavier[4] = new Color(139.0f/255.0f, 35.0f/255.0f, 35.0f/255.0f, 0);
		fjavier[3] = new Color(205.0f/255.0f, 51.0f/255.0f, 51.0f/255.0f, 0);
		fjavier[2] = new Color(238.0f/255.0f, 59.0f/255.0f, 59.0f/255.0f, 0);
		fjavier[1] = new Color(255.0f/255.0f, 64.0f/255.0f, 64.0f/255.0f, 0);
		fjavier[0] = new Color(238.0f/255.0f, 180.0f/255.0f, 180.0f/255.0f, 0);
		
		// Dibujamos el mapa de influencia.
		if (filled) {
			renderer.begin(ShapeType.Filled);
		} else {
			renderer.begin(ShapeType.Line);
		}
		for (int y = 0; y < grid_height; y++) {
			for (int x = 0; x < grid_width; x++) {
				if (filled && Ground.isImpassable(map_of_costs[x][y])) {
					// Si el terreno es infranqueable, se dibuja de color neutral.
					renderer.setColor(neutral);
					renderer.rect(positionX + x * output_grid_cell_size, positionY + y * output_grid_cell_size, 
							output_grid_cell_size, output_grid_cell_size, neutral, neutral, neutral, neutral);
					
				} else if (simpleMapOfInfluence_FJAVIER[x][y] > simpleMapOfInfluence_LDANIEL[x][y]) {
					// EXTREMADANTE IMPORTANTE -> El equipo con mayor influencia en una casilla, controla la casilla (es decir, se usa su color).
					// De todas formas, el color final dibujado sí tendra en cuenta la influencia de los 2 bando.
					int finalValue = (simpleMapOfInfluence_FJAVIER[x][y] - simpleMapOfInfluence_LDANIEL[x][y]) / 5; // Se divide entre 5 porque hay 5 colores posibles
					renderer.setColor(fjavier[finalValue]);
					renderer.rect(positionX + x * output_grid_cell_size, positionY + y * output_grid_cell_size, 
							output_grid_cell_size, output_grid_cell_size, fjavier[finalValue], fjavier[finalValue], fjavier[finalValue], fjavier[finalValue]);
				} else if (simpleMapOfInfluence_LDANIEL[x][y] > simpleMapOfInfluence_FJAVIER[x][y]) {
					// EXTREMADANTE IMPORTANTE -> El equipo con mayor influencia en una casilla, controla la casilla (es decir, se usa su color).
					// De todas formas, el color final dibujado sí tendra en cuenta la influencia de los 2 bando.
					int finalValue = (simpleMapOfInfluence_LDANIEL[x][y] - simpleMapOfInfluence_FJAVIER[x][y]) / 5; // Se divide entre 5 porque hay 5 colores posibles
					renderer.setColor(ldaniel[finalValue]);
					renderer.rect(positionX + x * output_grid_cell_size, positionY + y * output_grid_cell_size, 
							output_grid_cell_size, output_grid_cell_size, ldaniel[finalValue], ldaniel[finalValue], ldaniel[finalValue], ldaniel[finalValue]);
				} else {
					if (filled) {
						// Si la influencia es 0, se dibuja blanco
						renderer.setColor(neutral);
						renderer.rect(positionX + x * output_grid_cell_size, positionY + y * output_grid_cell_size, 
								output_grid_cell_size, output_grid_cell_size, neutral, neutral, neutral, neutral);
					}
				}
			}
		}
		renderer.end();
	}
}
