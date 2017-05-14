package com.mygdx.iadevproject.aiTactical.roles;

import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.map.Ground;

public abstract class Soldier implements TacticalRole {
	
	/** Coste táctico de los terrenos **/
	// UN SOLDADO NO TIENE PROBLEMAS (TÁCTICAMENTE HABLANDO) EN IR POR CUALQUIER TIPO DE TERRENO.
	private static final int WATER_COST 		= IADeVProject.INFINITY;
	private static final int MOUNTAINS_COST 	= IADeVProject.INFINITY-1; // Se le resta uno, porque en el método 'getGround' no pueden haber dos con el mismo valor
	private static final int FOREST_COST 		= 5;
	private static final int DESERT_COST 		= 4;
	private static final int MEADOW_COST 		= 3;
	private static final int WAY_COST 			= 2;
	private static final int TRAIL_COST 		= IADeVProject.DEFAULT_COST;
	
	// Distancia máxima de ataque.
	private static float SOLDIER_ATTACK_MAX_DISTANCE = 100.0f;
	// Vida que quita este personaje al atacar.
	private static float SOLDIER_ATTACK_DAMAGE_TO_DONE = 50.0f;
	// Máxima velocidad del soldado.
	private static float SOLDIER_MAX_SPEED = 50.0f;

	@Override
	public float getVelocityFactor(Ground ground) {
		switch (ground) {
			case MOUNTAINS: return 0.1f; // Terreno infranqueable.
			case WATER:		return 0.1f; // Terreno infranqueable.
			case WAY: 		return 1.0f;
			case FOREST:	return 0.4f;
			case MEADOW: 	return 0.6f;
			case DESERT:	return 0.4f;
			case TRAIL:		return 1.0f;
			default:		return 0.1f;		
		}
	}

	@Override
	public int getTacticalCost(Ground ground) {
		switch (ground) {
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
	
	public float getMaxDistanceOfAttack() {
		return SOLDIER_ATTACK_MAX_DISTANCE;
	}
	
	public float getDamageToDone() {
		return SOLDIER_ATTACK_DAMAGE_TO_DONE;
	}

	public float getMaxSpeed() {
		return SOLDIER_MAX_SPEED;
	}
}
