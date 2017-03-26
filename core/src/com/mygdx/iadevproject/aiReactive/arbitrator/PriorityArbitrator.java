package com.mygdx.iadevproject.aiReactive.arbitrator;

import java.util.Map;
import java.util.Map.Entry;

import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.aiReactive.steering.Steering_NoAcceleratedUnifMov;

/**
 * Clase que representa el árbitro de prioridades por Behaviours independientes.
 */
public class PriorityArbitrator implements Arbitrator {

	// Mantiene el valor mínimo que debe de tener un Steering para que se tenga en consideración.
	private float epsilon;
	
	/**
	 * Constructor.
	 * @param epsilon Valor mínimo que debe de tener un Steering para que se tenga en consideración.
	 */
	public PriorityArbitrator(float epsilon) {
		this.epsilon = epsilon;
	}
	
	public float getEpsilon() {
		return epsilon;
	}
	
	public void setEpsilon(float epsilon) {
		this.epsilon = epsilon;
	}

	@Override
	public Steering getSteering(Map<Float, Behaviour> behaviours) {
		// Steering de salida
		Steering steer = null;
			
		// Para cada uno de los comportamientos
		for (Entry<Float, Behaviour> behaviour : behaviours.entrySet()) {
			// Obtenemos el steering del comportamiento
			steer = behaviour.getValue().getSteering();
			
			// Si el steering es acelerado, lo tenemos en cuenta.
			if (steer instanceof Steering_AcceleratedUnifMov) {
				Steering_AcceleratedUnifMov steering = (Steering_AcceleratedUnifMov) steer;
				
				// Si es distinto de null y el valor de la aceleración o el de la rotación es mayor que epsilon
				// entonces devolvemos el steering
				if (steering != null && (steering.getLineal().len() > epsilon || Math.abs(steering.getAngular()) > epsilon)) {	 
					return steering;
				}
			}
			
			// Si el steering es no acelerado, lo tenemos en cuenta.
			if (steer instanceof Steering_NoAcceleratedUnifMov) {
				Steering_NoAcceleratedUnifMov steering = (Steering_NoAcceleratedUnifMov) steer;
				
				// Si es distinto de null y el valor de la velocidad o el de la rotación es mayor que epsilon
				// entonces devolvemos el steering
				if (steering != null && (steering.getVelocity().len() > epsilon || Math.abs(steering.getRotation()) > epsilon)) {	 
					return steering;
				}
			}
		}

		// Devolvemos steering del último comportamiento (por muy pequeño que sea, para que el personaje no se quede parado).
		return steer;
	}
}
