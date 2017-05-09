package com.mygdx.iadevproject.model;

public enum Team {
	FJAVIER, LDANIEL, NEUTRAL;
	
	/**
	 * Método que devuelve el equipo enemigo del objeto 'this'.
	 * @return Equipo enemigo del objeto 'this'
	 */
	public Team getEnemyTeam() {
		switch(this) {
		case FJAVIER: return LDANIEL;
		case LDANIEL: return FJAVIER;
		default: return NEUTRAL;
		}
	}
}
