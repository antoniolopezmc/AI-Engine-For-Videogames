package com.mygdx.iadevproject.behaviour.AcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;

public class VelocityMatching_Accelerated implements Behaviour {

	// Máxima aceleración del personaje
	private float maxAcceleration;
	// Tiempo en alcanzar la velocidad del objetivo
	private float timeToTarget;
	
	/**
	 * Constructor.
	 * @param maxAcceleration - Máxima aceleración del personaje.
	 * @param timeToTarget - Tiempo en alcanzar la velocidad del objetivo.
	 */
	public VelocityMatching_Accelerated (float maxAcceleration, float timeToTarget) {
		this.maxAcceleration = maxAcceleration;
		this.timeToTarget = timeToTarget;
	}
	
	/**
	 * Método 'get' para la aceleración máxima.
	 * @return - Aceleración máxima del personaje.
	 */
	public float getMaxAcceleration() {
		return maxAcceleration;
	}
	
	/**
	 * Método 'set' para la aceleración máxima.
	 * @param maxAcceleration - Aceleración máxima del personaje.
	 */
	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	/**
	 * Método 'get' para el tiempo de alcance de la velocidad del objetivo.
	 * @return - Tiempo de alcance de la velocidad del objetivo.
	 */
	public float getTimeToTarget() {
		return timeToTarget;
	}

	/**
	 * Método 'set' para el tiempo de alcance de la velocidad del objetivo.
	 * @param timeToTarget - Tiempo de alcance de la velocidad del objetivo.
	 */
	public void setTimeToTarget(float timeToTarget) {
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
						
		// Calculamos el atributo 'lineal' como diferencia de las velocidades entre el personajey el objetivo.
		Vector3 copy = new Vector3(target.getVelocity());;
		Vector3 finalLineal = copy.sub(source.getVelocity());
				
		finalLineal.x = finalLineal.x / this.timeToTarget;
		finalLineal.y = finalLineal.y / this.timeToTarget;
		finalLineal.z = finalLineal.z / this.timeToTarget;
		
		// Si la aceleración es superior a la aceleración máxima, entonces normalizamos y establecemos la máxima aceleración.
		if (finalLineal.len() > maxAcceleration) {
			finalLineal = finalLineal.nor();
			
			finalLineal.x = finalLineal.x * this.maxAcceleration;
			finalLineal.y = finalLineal.y * this.maxAcceleration;
			finalLineal.z = finalLineal.z * this.maxAcceleration; 			
		}
		
		output.setLineal(finalLineal);	
		
		output.setAngular(0);
		
		return output;
	}
}
