package com.mygdx.iadevproject.map;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;

/**
 * Clase que se encarga de crear los mapas del juego
 */
public class MapsCreatorIADeVProject {
	
	/**
	 * Método que crea los mapas a partir del parámetro 'tiledMap' con un tamaño de celda 'grid_cell_size'. 
	 * Este método hace uso de los mapas de la clase IADeVProject, pues este método es dependiente de los
	 * mapas que se vayan a crear en dicha clase.
	 * @param tiledMap Mapa 
	 * @param grid_cell_size Tamaño de la celda
	 */
	public static void createMaps(TiledMap tiledMap, int grid_cell_size) {
		
		// Ancho y alto de los 'Tile' del mapa
		int tileWidth, tileHeight;
		
		// Para cada terreno
		for (Ground ground : Ground.values()) {
			// Obtenemos su capa
			TiledMapTileLayer layer =  (TiledMapTileLayer)tiledMap.getLayers().get(ground.toString().toLowerCase());
		    			
			// Obtenemos el alto y el ancho de del Tile
			tileWidth 	= (int)layer.getTileWidth();
			tileHeight 	= (int)layer.getTileHeight();
			
			// Recorremos la capa
			for (int i=0; i < layer.getHeight(); i++) {
		      	for (int j=0; j < layer.getWidth(); j++) {
		      		Cell cell = layer.getCell(i,j); 
		      		if (cell != null) {
		      			
		      			// Para cada celda obtenemos su posición en el mapa y su tamaño 
		      			// y rellenamos los mapas correspondientes
		      			int x = i*tileHeight  / grid_cell_size;
		      			int y = j*tileWidth   / grid_cell_size;
		      			
		      			int width = cell.getTile().getTextureRegion().getRegionWidth();
		      			int height = cell.getTile().getTextureRegion().getRegionHeight();
   			
		      			for (int ic=x; ic < x+height / grid_cell_size ; ic++) {
		      				for (int jc=y; jc < y+width / grid_cell_size; jc++) {
		      					IADeVProject.MAP_OF_COSTS[ic][jc] = ground.getCost();
		      					IADeVProject.MAP_OF_GROUNDS[ic][jc] = ground;
		      				}
		      			}
		      		}
		      	}
			}
		}
	}
	
	/**
	 * Método que obtiene los obstáculo del mapa 'tiledMap' y lo devuelve en forma de Lista
	 * @return Lista de los obstáculos del mundo
	 */
	public static List<WorldObject> getObstaclesOfMap(TiledMap tiledMap) {
		List<WorldObject> obstacles = new LinkedList<WorldObject>();
        
		// Obtenemos la capa del mapa de los obstáculos
        MapLayer obstacleLayer = tiledMap.getLayers().get("obstacles");
        for (MapObject obj : obstacleLayer.getObjects()) {
        	
        	// Para cada objeto, obtenemos sus propiedades
        	MapProperties properties = obj.getProperties();
        	
        	// Calculamos la coordenada X como la coordenada x + la mitad del ancho del obstáculo (ya que la coordenada X se encuentra abajo izquierda del objeto)
        	float x = (Float) properties.get("x");
        	x += ((Float) properties.get("width"))/2;
        	
        	// Calculamos la coordenada Y como la coordenada y + la mitad del alto del obstáculo (ya que la coordenada Y se encuentra abajo izquierda del objeto)
        	float y = (Float) properties.get("y");
        	y += ((Float) properties.get("height"))/2;
        	
        	// Creamos el obstáculo con las coordenadas calculadas y las propiedades de alto y ancho del obstáculo
			Obstacle obs = new Obstacle();
			obs.setBounds(x, y, (Float) properties.get("width"), (Float) properties.get("height"));
			
			// Lo añadimos a la lista de obstáculos
			obstacles.add(obs);
        }
        
        // Devolvemos la lista de obstáculos
        return obstacles;
	}
	
	/**
	 * Método que dado el mapa 'tiledMap' y la capa 'strlayer' devuelve el rectángulo
	 * que contiene a esa capa. Supone que la capa es del tipo 'TiledMapImageLayer'.
	 * 
	 * Hace uso de la variable 'map_height' porque el cálculo de la posición 'y' del
	 * rectángulo se hace teniendo en cuenta que el mapa 'tiledMap' tiene como sistema de coordenadas
	 * ARRIBA-IZQUIERDA, mientras que LibGDX y nuestro proyecto lo toma como ABAJO-IZQUIERDA,
	 * por lo tanto, hay que transformar las posiciones que nos ofrecen desde el mapa.
	 * @param tiledMap Mapa.
	 * @param strlayer Capa.
	 * @return Rectángulo que contiene a esa capa.
	 */
	private static Rectangle getRectangleOfLayer(TiledMap tiledMap, String strlayer, int map_height) {
		TiledMapImageLayer layer =  (TiledMapImageLayer)tiledMap.getLayers().get(strlayer);
		
		float x = (Float)layer.getProperties().get("offsetX");
		float width = layer.getTextureRegion().getRegionWidth();
		float height = layer.getTextureRegion().getRegionHeight();
		float y = (map_height - (Float)layer.getProperties().get("offsetY") - height);
		
		return new Rectangle(x, y, width, height);
	}
	
	/**
	 * Método que obtiene las bases del mapa 'tiledMap' usando el alto del mapa 'map_height'.
	 * @param tiledMap Mapa.
	 * @param map_height Alto del mapa.
	 * @return Un objeto de la clase 'Map' con las bases de cada equipo.
	 */
	public static Map<Team, Rectangle> getBasesOfMap(TiledMap tiledMap, int map_height) {
		
		Rectangle basefjavier = getRectangleOfLayer(tiledMap, "basefjavier", map_height);
		Rectangle baseldaniel = getRectangleOfLayer(tiledMap, "baseldaniel", map_height);
		
		Map<Team, Rectangle> bases = new HashMap<Team, Rectangle>();
		bases.put(Team.FJAVIER, basefjavier);
		bases.put(Team.LDANIEL, baseldaniel);
		
		return bases;
	}
	
	/**
	 * Método que obtiene los manantiales del mapa 'tiledMap' usando el alto del mapa 'map_height'.
	 * @param tiledMap Mapa.
	 * @param map_height Alto del mapa.
	 * @return Un objeto de la clase 'Map' con los manantiales de cada equipo.
	 */
	public static Map<Team, Rectangle> getManantialsOfMap(TiledMap tiledMap, int map_height) {
		Rectangle manantialfjavier = getRectangleOfLayer(tiledMap, "manantialfjavier", map_height);
		Rectangle manantialldaniel = getRectangleOfLayer(tiledMap, "manantialldaniel", map_height);
		
		Map<Team, Rectangle> manantials = new HashMap<Team, Rectangle>();
		manantials.put(Team.FJAVIER, manantialfjavier);
		manantials.put(Team.LDANIEL, manantialldaniel);
		
		return manantials;
	}
}
