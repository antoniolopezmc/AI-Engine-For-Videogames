package com.mygdx.iadevproject.aiReactive.behaviour.delegated;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

public class LookingWhereYouGoing extends Align_Accelerated implements Behaviour {

	/**
	 * Constructor de la clase.
	 * @param source Personaje que realiza el comportamiento (personaje fuente).
	 * @param maxAngularAcceleration Máxima aceleración angular.
	 * @param maxRotation Máxima velocidad angular.
	 * @param targetRadius Ángulo interior (igual o más pequeño que slowRadius).
	 * @param slowRadius Ángulo exterior (igual o más grande que targetRadius).
	 * @param timeToTarget Tiempo en el que se realizará este comportamiento.
	 */
	public LookingWhereYouGoing(Character source, float maxAngularAcceleration, float maxRotation, float targetRadius, float slowRadius, float timeToTarget) {
		super(source, null, maxAngularAcceleration, maxRotation, targetRadius, slowRadius, timeToTarget);
	}

	@Override
	public Steering getSteering() {
		// 1.- Calcular el objetivo al que alinearse
		
		// Si la velocidad es cero, no cambianos nada. Estamos mirando al objetivo
		if (this.getSource().getVelocity().len() == 0.0f) {
			Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
			output.setAngular(0);
			output.setLineal(new Vector3(0,0,0));
			return output;
		}
		
		WorldObject explicitTarget = new Obstacle();
		explicitTarget.setOrientation((float) Math.toDegrees(MathUtils.atan2(-this.getSource().getVelocity().x, this.getSource().getVelocity().y)));
		
		// Establecemos como objetivo, el objetivo calculado
		this.setTarget(explicitTarget);
		return super.getSteering();
	}

}
