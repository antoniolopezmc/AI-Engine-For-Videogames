package com.mygdx.iadevproject.steering;

import com.badlogic.gdx.math.Vector3;

/**
 * 
 * Steering para movimiento uniforme acelerado.
 * 
 */
public class Steering_AcceleratedUnifMov extends Steering {

	// Representa el vector aceleración.
	private Vector3 lineal;
	// Es un escalar que representa la aceleración angular.
	private float angular;
	
	/**
	 * Constructor vacío de la clase.
	 */
	public Steering_AcceleratedUnifMov () {
		
	}
	
	/**
	 * Constructor de 2 parámetros de la clase.
	 * @param lineal Vector aceleración.
	 * @param angular Aceleración angular.
	 */
	public Steering_AcceleratedUnifMov(Vector3 lineal, float angular) {
		this.lineal = lineal;
		this.angular = angular;
	}
	
	/**
	 * Constructor de 4 parámetros de la clase.
	 * @param x Coordenada 'x' del vector aceleración.
	 * @param y Coordenada 'y' del vector aceleración.
	 * @param z Coordenada 'z' del vector aceleración.
	 * @param angular Aceleración angular.
	 */
	public Steering_AcceleratedUnifMov (float x, float y, float z, float angular) {
		this(new Vector3(x,y,z), angular);
	}

	/**
	 * Método 'get' para el atributo 'linal'.
	 * @return El vector aceleración.
	 */
	public Vector3 getLineal() {
		return lineal;
	}

	/**
	 * Método 'set' para el atributo 'lineal'.
	 * @param lineal El vector aceleración.
	 */
	public void setLineal(Vector3 lineal) {
		this.lineal = lineal;
	}

	/**
	 * Método 'get' para el atributo 'angular'.
	 * @return Aceleración angular.
	 */
	public float getAngular() {
		return angular;
	}

	/**
	 * Método 'set' para el atributo 'angular'.
	 * @param angular Aceleración angular.
	 */
	public void setAngular(float angular) {
		this.angular = angular;
	}
	
}
