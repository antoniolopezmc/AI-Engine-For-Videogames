package com.mygdx.iadevproject.behaviour.AcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.modelo.Character;

public class Seek_Accelerated implements Behaviour {

	private float maxAcceleration;
	private float maxSpeed;
	
	public Seek_Accelerated (float maxAcceleration, float maxSpeed) {
		this.maxAcceleration = maxAcceleration;
		this.maxSpeed = maxSpeed;
	}

	public float getMaxAcceleration() {
		return maxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
				
		// Calculamos el atributo 'lineal'.
		Vector3 copy = new Vector3(target.getPosition());
		Vector3 finalLineal = copy.sub(source.getPosition()).nor();
		
		// Consideramos las dos versiones del Seek acelerado. Por defecto, se utiliza la de Millington.
		// --> Versión de Millington: el personaje no se para nunca
		// --> Versión de Reynolds: el personaje se para
		boolean reynolds = false;
		if (reynolds) { 
			// Versión de Craig W. Reynolds
			finalLineal.x = finalLineal.x * this.maxSpeed;
			finalLineal.y = finalLineal.y * this.maxSpeed;
			finalLineal.z = finalLineal.z * this.maxSpeed;
			output.setLineal(finalLineal.sub(source.getVelocity()));
			
		} else {
			// Versión de Ian Millington
			finalLineal.x = finalLineal.x * this.maxAcceleration;
			finalLineal.y = finalLineal.y * this.maxAcceleration;
			finalLineal.z = finalLineal.z * this.maxAcceleration; 
			output.setLineal(finalLineal);		
		}
		
		
		// La aceleración angular del steering se pone a 0.
		output.setAngular(0);
				
		return output;
	}

}
