package com.mygdx.iadevproject.arbitrator;

import java.util.Map;

import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.Steering;

/**
 * Interfaz que deben implementar todos los tipos de 'Arbitrator' concretos.
 */
public interface Arbitrator {
	
	/**
	 * Método que devuelve el Steering concreto según el tipo de árbitro.
	 * @return El Steering concreto creado según el tipo de árbitro.
	 */
	public Steering getSteering(Map<Float, Behaviour> behaviours);
}
