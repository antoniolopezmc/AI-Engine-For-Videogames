package com.mygdx.iadevproject.behaviour.acceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;

public class Flee_Accelerated implements Behaviour {
	
	private Character source;
	private WorldObject target;
	private float maxAcceleration;

	public Flee_Accelerated(Character source, WorldObject target, float maxAcceleration) {
		this.source = source;
		this.target = target;
		this.maxAcceleration = maxAcceleration;
	}

	public Character getSource() {
		return source;
	}

	public void setSource(Character source) {
		this.source = source;
	}

	public WorldObject getTarget() {
		return target;
	}

	public void setTarget(WorldObject target) {
		this.target = target;
	}

	public float getMaxAcceleration() {
		return maxAcceleration;
	}
	
	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}
	
	@Override
	public Steering getSteering() {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
				
		// Calculamos el atributo 'lineal'.
		Vector3 copy = new Vector3(this.source.getPosition());
		Vector3 finalLineal = copy.sub(this.target.getPosition()).nor();
		
		// El personaje se aleja a la máxima aceleración fijada (la velocidad cada vez es mayor).
		finalLineal.x = finalLineal.x * this.maxAcceleration;
		finalLineal.y = finalLineal.y * this.maxAcceleration;
		finalLineal.z = finalLineal.z * this.maxAcceleration; 
		output.setLineal(finalLineal);			
		
		// La aceleración angular del steering se pone a 0.
		output.setAngular(0);
				
		return output;
	}

}
