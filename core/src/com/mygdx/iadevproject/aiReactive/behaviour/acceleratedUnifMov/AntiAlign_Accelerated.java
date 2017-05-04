package com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class AntiAlign_Accelerated implements Behaviour {

	/**
	 * Método para pintar las líneas de debug del Behaviour
	 */
	private void debug() {
		if (IADeVProject.PRINT_PATH_BEHAVIOUR) {
			
		}
	}
	
	private Character source;
	private WorldObject target;
	// Máxima aceleración angular.
	private float maxAngularAcceleration;
	// Máxima rotación -> Velocidad angular.
	private float maxRotation;
	// Ángulo interior. --> MUY IMPORTANTE: En este comportamiento, targetRadius es un ángulo.
	private float targetRadius;
	// Ángulo exterior. --> MUY IMPORTANTE: En este comportamiento, slowRadius es un ángulo.
	private float slowRadius;
	private float timeToTarget;	

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

	public AntiAlign_Accelerated(Character source, WorldObject target, float maxAngularAcceleration, float maxRotation, float targetRadius, float slowRadius, float timeToTarget) {
		this.source = source;
		this.target = target;
		this.maxAngularAcceleration = maxAngularAcceleration;
		this.maxRotation = maxRotation;
		this.targetRadius = targetRadius;
		this.slowRadius = slowRadius;
		this.timeToTarget = timeToTarget;
	}

	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	public float getMaxRotation() {
		return maxRotation;
	}

	public void setMaxRotation(float maxRotation) {
		this.maxRotation = maxRotation;
	}

	public float getTargetRadius() {
		return targetRadius;
	}

	public void setTargetRadius(float targetRadius) {
		this.targetRadius = targetRadius;
	}

	public float getSlowRadius() {
		return slowRadius;
	}

	public void setSlowRadius(float slowRadius) {
		this.slowRadius = slowRadius;
	}

	public float getTimeToTarget() {
		return timeToTarget;
	}

	public void setTimeToTarget(float timeToTarget) {
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering() {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
		
		// Obtenemos la diferencia de las orientaciones entre el objetivo y la fuente. EN GRADOS.
		float rotation = ((target.getOrientation() + 180) % 360) - source.getOrientation();

		// Map to Range. (0, 360)
		if (Math.abs(rotation) >= 180) {
			if (rotation > 0) {
				rotation = rotation - 360;
			} else {
				rotation = rotation + 360;
			}			
		} 
		
		float rotationSize = Math.abs(rotation);
		
		// Comprobamos si estamos dentro del radio interior.
		if (rotationSize < targetRadius) {
			output.setAngular(-source.getRotation_angularSpeed());
			output.setLineal(new Vector3(0,0,0));
			return output;
		}
		
		// Si estamos fuera del radio exterior, entonces usamos la máxima rotacion.
		float targetRotation;
		if (rotationSize > slowRadius) {
			targetRotation = maxRotation;
		// Sino, escalamos.
		} else {
			targetRotation = maxRotation * rotationSize / slowRadius;
		}
		
		targetRotation = targetRotation * rotation / rotationSize;
		
		output.setAngular((targetRotation - source.getRotation_angularSpeed()) / timeToTarget);
		
		float angularAcceleration = Math.abs(output.getAngular());
		if (angularAcceleration > maxAngularAcceleration) {
			output.setAngular(output.getAngular() / angularAcceleration * maxAngularAcceleration);
		}
		
		output.setLineal(new Vector3(0,0,0));
		
		return output;
	}

}
