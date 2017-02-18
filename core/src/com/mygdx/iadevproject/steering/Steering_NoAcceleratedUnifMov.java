package com.mygdx.iadevproject.steering;

import com.badlogic.gdx.math.Vector3;

/**
 * 
 * Steering para movimiento uniforme no acelerado.
 * 
 */
public class Steering_NoAcceleratedUnifMov extends Steering {

	// Representa el vector velocidad.
	private Vector3 velocity;
	// Es un escalar que representa la velocidad angular. 
	private float rotation;
	
	/**
	 * Constructor vacío de la clase.
	 */
	public Steering_NoAcceleratedUnifMov () {
		
	}
	
	/**
	 * Constructor de 2 parámetros de la clase.
	 * @param velocity Vector velocidad.
	 * @param rotation Velocidad angular.
	 */
	public Steering_NoAcceleratedUnifMov(Vector3 velocity, float rotation) {
		this.velocity = velocity;
		this.rotation = rotation;
	}
	
	/**
	 * Constructor de 4 parámetros de la clase.
	 * @param x Coordenada 'x' del vector velocidad.
	 * @param y Coordenada 'y' del vector velocidad.
	 * @param z Coordenada 'z' del vector velocidad.
	 * @param rotation Velocidad angular.
	 */
	public Steering_NoAcceleratedUnifMov (float x, float y, float z, float rotation) {
		this(new Vector3(x,y,z), rotation);
	}
	
	/**
	 * Método 'get' para el atributo 'velocity'.
	 * @return El vector velocidad.
	 */
	public Vector3 getVelocity() {
		return velocity;
	}
	
	/**
	 * Método 'set' para el atributo 'velocity'.
	 * @param velocity El vector velocidad.
	 */
	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Método 'get' para el atributo 'rotation'.
	 * @return Velocidad angular.
	 */
	public float getRotation() {
		return rotation;
	}
	
	/**
	 * Método 'set' para el atributo 'rotation'.
	 * @param rotation Velocidad angular.
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Calcula el módulo del vector velocidad.
	 * @return Módulo del vector velocidad.
	 */
	public float getSpeed() {
		return this.velocity.len();
	}	
}
