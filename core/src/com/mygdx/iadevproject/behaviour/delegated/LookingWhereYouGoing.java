package com.mygdx.iadevproject.behaviour.delegated;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.behaviour.acceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;

//TODO IMPORTANTE -> PROBAR.

public class LookingWhereYouGoing extends Align_Accelerated implements Behaviour {

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
