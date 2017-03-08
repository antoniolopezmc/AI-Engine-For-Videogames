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
	
	/**
	 * Método que devuelve el objetivo u objetivos del comportamiento.
	 * @return Aquello que el comportamiento considera como 'objetivo'.
	 */
	Object getTarget();
}
