package com.mygdx.iadevproject.behaviour.AcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.modelo.Character;

public class VelocityMatching_Accelerated implements Behaviour {

	private float maxAcceleration;
	private float timeToTarget;
	
	public VelocityMatching_Accelerated (float maxAcceleration, float timeToTarget) {
		this.maxAcceleration = maxAcceleration;
		this.timeToTarget = timeToTarget;
	}
	
	public float getMaxAcceleration() {
		return maxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	public float getTimeToTarget() {
		return timeToTarget;
	}

	public void setTimeToTarget(float timeToTarget) {
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
						
		// Calculamos el atributo 'lineal'.
		Vector3 copy = new Vector3(target.getVelocity());;
		Vector3 finalLineal = copy.sub(source.getVelocity());
				
		finalLineal.x = finalLineal.x / this.timeToTarget;
		finalLineal.y = finalLineal.y / this.timeToTarget;
		finalLineal.z = finalLineal.z / this.timeToTarget;
		
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
