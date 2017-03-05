package com.mygdx.iadevproject.behaviour.Delegated;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;

public class LookingWhereYouGoing extends Align_Accelerated implements Behaviour {

	public LookingWhereYouGoing(float maxAngularAcceleration, float maxRotation, float targetRadius, float slowRadius, float timeToTarget) {
		super(maxAngularAcceleration, maxRotation, targetRadius, slowRadius, timeToTarget);
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// 1.- Calcular el objetivo al que alinearse
		
		// Si la velocidad es cero, no cambianos nada. Estamos mirando al objetivo
		if (source.getVelocity().len() == 0.0f) {
			Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
			output.setAngular(0);
			output.setLineal(new Vector3(0,0,0));
			return output;
		}
		
		Character explicitTarget = new Character();
		explicitTarget.setOrientation((float) Math.toDegrees(MathUtils.atan2(-source.getVelocity().x, source.getVelocity().y)));
		
		return super.getSteering(source, explicitTarget);
	}

}
