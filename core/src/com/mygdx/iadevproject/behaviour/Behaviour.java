package com.mygdx.iadevproject.behaviour;

import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.steering.Steering;

/**
 * 
 * Interfaz que deben implementar todos los tipos de 'Behaviour' concretos.
 */
public interface Behaviour {

	/**
	 * Método que recibe 2 personajes y devuelve el Steering concreto según el tipo de comportamiento.
	 * @param source Objeto que representa al origen.
	 * @param target Objeto que representa al objetivo.
	 * @return El Steering concreto creado según el tipo de comportamiento.
	 */
	Steering getSteering(Character source, Character target);
}
