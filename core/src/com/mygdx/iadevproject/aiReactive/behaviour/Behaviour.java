package com.mygdx.iadevproject.aiReactive.behaviour;

import com.mygdx.iadevproject.aiReactive.steering.Steering;

/**
 * 
 * Interfaz que deben implementar todos los tipos de 'Behaviour' concretos.
 */
public interface Behaviour {

	/**
	 * Método que devuelve el Steering concreto según el tipo de comportamiento.
	 * @return El Steering concreto creado según el tipo de comportamiento.
	 */
	public Steering getSteering();
	
}
