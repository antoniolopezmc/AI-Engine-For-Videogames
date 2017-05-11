package com.mygdx.iadevproject.waypoints;

import com.badlogic.gdx.math.Vector3;

/**
 * Como se ha explicado antes, en la estructura para manejar los waypoints de los puentes tendremos:
 *  - Una clave -> Waypoint concreto del puente.
 *  - Un valor -> Está formado por un booleano que indica si ese waypoint está ocupado o no y el waypoint vecino
 *  					(del mismo lado del mismo puente). 
 *
 */
public class ValueOfBridgeWaypoint {

	private Boolean ocupacion;
	private Vector3 waypointVecino;
	
	// Constructor.
	public ValueOfBridgeWaypoint(Boolean ocupacion, Vector3 waypointVecino) {
		super();
		this.ocupacion = ocupacion;
		this.waypointVecino = waypointVecino;
	}

	// GETs y SETs.
	public Boolean getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(Boolean ocupacion) {
		this.ocupacion = ocupacion;
	}

	public Vector3 getWaypointVecino() {
		return waypointVecino;
	}

	public void setWaypointVecino(Vector3 waypointVecino) {
		this.waypointVecino = waypointVecino;
	}
	
	
}
