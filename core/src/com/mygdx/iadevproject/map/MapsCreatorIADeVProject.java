package com.mygdx.iadevproject.map;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.mygdx.iadevproject.IADeVProject;

/**
 * Clase que se encarga de crear los mapas del juego
 */
public class MapsCreatorIADeVProject {
	
	/**
	 * Método que crea los mapas
	 */
	public static void createMaps() {
			
		inicializeMaps();
		
		// Ancho y alto de los 'Tile' del mapa
		int tileWidth, tileHeight;
		
		// Para cada terreno
		for (Ground ground : Ground.values()) {
			// Obtenemos su capa
			TiledMapTileLayer layer =  (TiledMapTileLayer)IADeVProject.tiledMap.getLayers().get(ground.toString().toLowerCase());
		    			
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
		      			int x = i*tileHeight  / IADeVProject.GRID_CELL_SIZE;
		      			int y = j*tileWidth   / IADeVProject.GRID_CELL_SIZE;
		      			
		      			int width = cell.getTile().getTextureRegion().getRegionWidth();
		      			int height = cell.getTile().getTextureRegion().getRegionHeight();
   			
		      			for (int ic=x; ic < x+height / IADeVProject.GRID_CELL_SIZE ; ic++) {
		      				for (int jc=y; jc < y+width / IADeVProject.GRID_CELL_SIZE; jc++) {
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
	 * Método que inicializa los mapas a los valores por defecto.
	 */
	private static void inicializeMaps() {
		for (int i=0; i < IADeVProject.GRID_WIDTH; i++) {
			for (int j=0; j < IADeVProject.GRID_HEIGHT; j++) {
				IADeVProject.MAP_OF_COSTS[i][j] = IADeVProject.DEFAULT_COST;
				IADeVProject.MAP_OF_GROUNDS[i][j] = IADeVProject.DEFAULT_GROUND;
			}
		}
	}
}
