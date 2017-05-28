package com.mygdx.iadevproject.aiReactive.behaviour.noAcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_NoAcceleratedUnifMov;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class Seek_NoAccelerated implements Behaviour {
	
	private Character source;
	private WorldObject target;
	private float maxSpeed; // IMPORTANTE -> No confundir con maxSpeed de WorldObject.
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param maxSpeed Máxima velocidad lineal que se aplica en este comportamiento.
	 */
	public Seek_NoAccelerated (Character source, WorldObject target, float maxSpeed) {
		this.source = source;
		this.target = target;
		this.maxSpeed = maxSpeed;
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

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	public Steering getSteering() {
		// Creamos el 'Steering' que será devuelto.
		Steering_NoAcceleratedUnifMov output = new Steering_NoAcceleratedUnifMov();
				
		// Calculamos el atributo 'velocity'.
		Vector3 copy = new Vector3(this.target.getPosition());
		Vector3 finalVelocity = copy.sub(this.source.getPosition()).nor();
		
		finalVelocity.x = finalVelocity.x * this.maxSpeed;
		finalVelocity.y = finalVelocity.y * this.maxSpeed;
		finalVelocity.z = finalVelocity.z * this.maxSpeed; 
		output.setVelocity(finalVelocity);
		
		// Modificamos la orientación del personaje (source) para que mire hacia el objetivo (en función del vector velocidad que acabamos de calcular).
		this.source.setOrientation(this.source.getNewOrientation(output));
				
		// La rotación (velocidad angular) del steering se pone a 0.
		output.setRotation(0);
				
		return output;
	}

}
