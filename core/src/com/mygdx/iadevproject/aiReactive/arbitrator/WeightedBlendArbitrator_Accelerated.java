package com.mygdx.iadevproject.aiReactive.arbitrator;

import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;

/**
 * Clase que representa al árbitro de Mezcla Ponderada. Este árbitro
 * trabaja solamente con comportamientos Acelerados. Si alguno de los 
 * comportamientos es no acelerado, no se tiene en cuenta.
 */
public class WeightedBlendArbitrator_Accelerated implements Arbitrator {

	private float maxAcceleration;	// Máxima aceleración lineal
	private float maxAngular;		// Máxima aceleración angular
	
	/**
	 * Constructor.
	 * @param maxAcceleration Máxima aceleración lineal que se puede aplicar.
	 * @param maxAngular Máxima aceleración angular que se puede aplicar.
	 */
	public WeightedBlendArbitrator_Accelerated(float maxAcceleration, float maxAngular) {
		this.maxAcceleration = maxAcceleration;
		this.maxAngular = maxAngular;
	}
	
	public float getMaxAcceleration() {
		return maxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	public float getMaxAngular() {
		return maxAngular;
	}

	public void setMaxAngular(float maxAngular) {
		this.maxAngular = maxAngular;
	}

	@Override
	public Steering getSteering(Map<Float, Behaviour> behaviours) {
		// Steering de salida
		Steering_AcceleratedUnifMov steering = new Steering_AcceleratedUnifMov();
		
		// Valores que se asignarán al Steering anterior, que se van
		// modificando conforme se obtienen los steerings de los comportamientos
		Vector3 lineal = new Vector3(0,0,0);
		float angular = 0.0f;
		
		// Para cada uno de los comportamientos
		for (Entry<Float, Behaviour> behaviour : behaviours.entrySet()) {
			// Obtenemos el steering del comportamiento
			Steering steer = behaviour.getValue().getSteering();
			
			// Si el steering es acelerado, lo tenemos en cuenta.
			if (steer instanceof Steering_AcceleratedUnifMov) {
				Steering_AcceleratedUnifMov steerAcc = (Steering_AcceleratedUnifMov) steer;
				
				// Si es distinto de null actualizamos los valores.
				// No comprobamos si el vector lineal o el valor angular es 0 porque
				// al ser una mezcla ponderada, no va a afectar al resultado.
				if (steerAcc != null) {	 
					lineal.add(steerAcc.getLineal().scl(behaviour.getKey()));
					angular += steerAcc.getAngular()*behaviour.getKey();
				}
			}
		}
		
		// Comprobamos que no superen el máximo
		if (lineal.len() > this.maxAcceleration) {
			lineal.nor();
			lineal.scl(this.maxAcceleration);
		}
		
		angular = (angular > this.maxAngular) ? this.maxAngular : angular;
		
		// Devolvemos el steering
		steering.setLineal(lineal);
		steering.setAngular(angular);
		return steering;
	}

}
