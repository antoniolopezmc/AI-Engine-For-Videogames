package com.mygdx.iadevproject.behaviour.acceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;

/**
 * Este comportamiento es una mezcla entre el Seek y el Arrive.
 * El personaje va a máxima velocidad hacía un objetivo, pero al acercarse a una determinada distancia, empieza a frenar. 
 */
public class Arrive_Accelerated_WithOneRadious extends Seek_Accelerated {

	private float targetRadious;
	
	public Arrive_Accelerated_WithOneRadious(Character source, WorldObject target, float maxAcceleration) {
		super(source, target, maxAcceleration);
		// A menos que se introduzca un radio, este behaviour se comporta como un Seek Acelerado normal.
		this.targetRadious = 0;
	}
	
	public Arrive_Accelerated_WithOneRadious(Character source, WorldObject target, float maxAcceleration, float targetRadious) {
		this(source, target, maxAcceleration);
		this.targetRadious = targetRadious;
	}
	
	public float getTargetRadious() {
		return this.targetRadious;
	}
	
	public void setTargetRadious (float targetRadious) {
		this.targetRadious = targetRadious;
	}
	
	@Override
	public Steering getSteering() {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
		
		// Calculamos el vector 'dirección' entre la fuente y el destino y la distancia entre ambos (el módulo del vector).
		Vector3 direction = new Vector3(super.getTarget().getPosition());
		direction = direction.sub(super.getSource().getPosition());
		float distance = direction.len();
		
		// Si la fuente está dentro del radio del destino, DECELERAMOS. 
		if (distance < this.targetRadious) {
			Vector3 sourceVelocity = new Vector3(super.getSource().getVelocity());
			output.setLineal(new Vector3(-sourceVelocity.x,-sourceVelocity.y,-sourceVelocity.z));
			output.setAngular(0);
			return output;
		}
		// Sino, aplicamos un Seek normal y corriente.
		return super.getSteering();
	}
	
}
