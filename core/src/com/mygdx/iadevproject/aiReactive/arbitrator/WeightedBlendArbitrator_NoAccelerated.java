package com.mygdx.iadevproject.aiReactive.arbitrator;

import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_NoAcceleratedUnifMov;

/**
 * Clase que representa al árbitro de Mezcla Ponderada. Este árbitro
 * trabaja solamente con comportamientos No Acelerados. Si alguno de los 
 * comportamientos es acelerado, no se tiene en cuenta.
 */
public class WeightedBlendArbitrator_NoAccelerated implements Arbitrator {

	private float maxSpeed;		// Máxima velocidad lineal
	private float maxRotation;	// Máxima velocidad angular
	
	/**
	 * Constructor.
	 * @param maxSpeed Máxima velocidad lineal que se puede aplicar.
	 * @param maxRotation Máxima velocidad lineal que se puede aplicar.
	 */
	public WeightedBlendArbitrator_NoAccelerated(float maxSpeed, float maxRotation) {
		this.maxSpeed = maxSpeed;
		this.maxRotation = maxRotation;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxRotation() {
		return maxRotation;
	}

	public void setMaxRotation(float maxRotation) {
		this.maxRotation = maxRotation;
	}

	@Override
	public Steering getSteering(Map<Float, Behaviour> behaviours) {
		// Steering de salida
		Steering_NoAcceleratedUnifMov steering = new Steering_NoAcceleratedUnifMov();
		
		// Valores que se asignarán al Steering anterior, que se van
		// modificando conforme se obtienen los steerings de los comportamientos
		Vector3 velocity = new Vector3(0,0,0);
		float rotation = 0.0f;

		// Para cada uno de los comportamientos
		for (Entry<Float, Behaviour> behaviour : behaviours.entrySet()) {
			// Obtenemos el steering del comportamiento
			Steering steer = behaviour.getValue().getSteering();
			
			// Si el steering es acelerado, lo tenemos en cuenta.
			if (steer instanceof Steering_NoAcceleratedUnifMov) {
				Steering_NoAcceleratedUnifMov steerAcc = (Steering_NoAcceleratedUnifMov) steer;
				
				// Si es distinto de null actualizamos los valores.
				// No comprobamos si el vector lineal o el valor angular es 0 porque
				// al ser una mezcla ponderada, no va a afectar al resultado.
				if (steerAcc != null) {	 
					velocity.add(steerAcc.getVelocity().scl(behaviour.getKey()));
					rotation += steerAcc.getRotation()*behaviour.getKey();
				}
			}
		}
		
		// Comprobamos que no superen el máximo
		if (velocity.len() > this.maxSpeed) {
			velocity.nor();
			velocity.scl(this.maxSpeed);
		}
		
		rotation = (rotation > this.maxRotation) ? this.maxRotation : rotation;
		
		// Devolvemos el steering
		steering.setVelocity(velocity);
		steering.setRotation(rotation);
		return steering;
	}

}
