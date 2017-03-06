package com.mygdx.iadevproject.behaviour;

import com.mygdx.iadevproject.steering.Steering;

/**
 * 
 * Interfaz que deben implementar todos los tipos de 'Behaviour' concretos.
 */
public interface Behaviour {

	/**
	 * Método que devuelve el Steering concreto según el tipo de comportamiento.
	 * @return El Steering concreto creado según el tipo de comportamiento.
	 */
	Steering getSteering();
}
