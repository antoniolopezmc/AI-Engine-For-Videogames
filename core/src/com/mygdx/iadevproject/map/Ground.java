package com.mygdx.iadevproject.map;

import com.mygdx.iadevproject.IADeVProject;

/**
 * Enumerado que tiene los tipos de terreno del juego. También proporciona 
 * los métodos necesarios para obtener el coste de un terreno o el terreno
 * asociado a un coste determinado.
 */
public enum Ground {
	// Importante: No tocar el orden del enumerado. Están así porque es el orden en el que se
	// rellena el mapa y el correspondiente coste del terreno (pues hay terrenos que están encima
	// de otros)
	TRAIL, WATER, DESERT, MEADOW, FOREST, WAY, MOUNTAINS;
	
	/** Costes de los terrenos **/
	private static final int WATER_COST 		= IADeVProject.INFINITY;
	private static final int MOUNTAINS_COST 	= IADeVProject.INFINITY-1; // Se le resta uno, porque en el método 'getGround' no pueden haber dos con el mismo valor
	private static final int FOREST_COST 		= 50;
	private static final int DESERT_COST 		= 45;
	private static final int MEADOW_COST 		= 10;
	private static final int WAY_COST 			= 2;
	private static final int TRAIL_COST 		= IADeVProject.DEFAULT_COST;
	
	/**
	 * Método que devuelve el coste que tiene el terreno (this)
	 * @return Coste del terreno (this)
	 */
	public int getCost() {
		switch (this) {
			case MOUNTAINS: return MOUNTAINS_COST;
			case WATER:		return WATER_COST;
			case WAY: 		return WAY_COST;
			case FOREST:	return FOREST_COST;
			case MEADOW: 	return MEADOW_COST;
			case DESERT:	return DESERT_COST;
			case TRAIL:		return TRAIL_COST;
			default:		return IADeVProject.DEFAULT_COST;		
		}
	}
	
	/**
	 * Método que dado un coste 'cost', devuelve el terreno asociado a ese coste. Si no 
	 * hay ningún terreno asociado a ese coste, devuelve 'null'
	 * @param cost Coste al que se quiere saber su terreno asociado.
	 * @return Terreno asociado al coste 'cost' o 'null' en el caso de no haya ningún terreno asociado
	 */
	public static Ground getGround(int cost) {
		switch (cost) {
			case WATER_COST: 		return WATER;
			case MOUNTAINS_COST: 	return MOUNTAINS;
			case FOREST_COST: 		return FOREST;
			case DESERT_COST: 		return DESERT;
			case MEADOW_COST: 		return MEADOW;
			case WAY_COST:			return WAY;
			case TRAIL_COST: 		return TRAIL;
			default: 				return null;
		}
	}
}
