package com.mygdx.iadevproject.behaviour.AcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.*;

public class Arrive_Accelerated implements Behaviour {
	
	// Máxima aceleración lineal. (Módulo del vector aceleración).
	private float maxAcceleration;
	private float maxSpeed;
	// Radio interior.
	private float targetRadious;
	// Radio exterior.
	private float slowRadious;
	private float timeToTarget;

	public Arrive_Accelerated(float maxAcceleration, float maxSpeed, float targetRadious, float slowRadious, float timeToTarget) {
		this.maxAcceleration = maxAcceleration;
		this.maxSpeed = maxSpeed;
		this.targetRadious = targetRadious;
		this.slowRadious = slowRadious;
		this.timeToTarget = timeToTarget;
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

	public float getTargetRadious() {
		return targetRadious;
	}

	public void setTargetRadious(float targetRadious) {
		this.targetRadious = targetRadious;
	}

	public float getSlowRadious() {
		return slowRadious;
	}

	public void setSlowRadious(float slowRadious) {
		this.slowRadious = slowRadious;
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
		
		// Calculamos el vector 'dirección' entre la fuente y el destino y la distancia entre ambos (el módulo del vector).
		Vector3 direction = new Vector3(target.getPosition());
		direction = direction.sub(source.getPosition());
		float distance = direction.len();
		
		// Si la fuente está dentro del radio interior del destino, no hay que hacer nada más. 
		if (distance < targetRadious) {
			output.setLineal(new Vector3(0,0,0));
			output.setAngular(0);
			return output;
		}
		
		// Si la fuente está fuera del radio exterior, debe ir a máxima velocidad. 
		// 		Si está entre el exterior y el interior, la velocidad debe adaptarse a la distancia que separa a los 2 personajes.
		float targetSpeed; // Velocidad a la que debe ir la fuente (hay que escalar la velocidad).
		if (distance > slowRadious) {
			targetSpeed = this.maxSpeed;
		} else {
			targetSpeed = maxSpeed * distance / slowRadious;
		}
		
		// Tras haber calculado la velocidad (Speed) a la que debemos ir y el vector 'dirección', ahora hay que calcular el vector velocidad (velocity).
		Vector3 targetVelocity = new Vector3(direction);
		targetVelocity = targetVelocity.nor();
		targetVelocity.x = targetVelocity.x * targetSpeed;
		targetVelocity.y = targetVelocity.y * targetSpeed;
		targetVelocity.z = targetVelocity.z * targetSpeed;
		
		// Calculamos el vector aceleración del Steering.
		output.setLineal(targetVelocity.sub(source.getVelocity()));
		targetVelocity.x = targetVelocity.x / timeToTarget;
		targetVelocity.y = targetVelocity.y / timeToTarget;
		targetVelocity.z = targetVelocity.z / timeToTarget;
		
		// Si el módulo de la aceleración es mayor que el máximo permitido, establecemos la aceleración al máximo.
		if (output.getLineal().len() > maxAcceleration) {
			targetVelocity = targetVelocity.nor();
			targetVelocity.x = targetVelocity.x * maxAcceleration;
			targetVelocity.y = targetVelocity.y * maxAcceleration;
			targetVelocity.z = targetVelocity.z * maxAcceleration;
		}
		
		// Establecemos la aceleración angular a 0.
		output.setAngular(0);
		
		return output;
	}

}
