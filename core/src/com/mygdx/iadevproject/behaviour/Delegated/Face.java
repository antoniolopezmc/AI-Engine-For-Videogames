package com.mygdx.iadevproject.behaviour.Delegated;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.modelo.Character;
import com.mygdx.iadevproject.steering.Steering;

public class Face extends Align_Accelerated implements Behaviour {

	public Face(float maxAngularAcceleration, float maxRotation, float targetRadius, float slowRadius, float timeToTarget) {
		super(maxAngularAcceleration, maxRotation, targetRadius, slowRadius, timeToTarget);
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// 1.- Calcular el objetivo al que alinearse
		
		// Calcular la dirección hacia el objetivo
		Vector3 direction = new Vector3(target.getPosition());
		direction = direction.sub(source.getPosition());
		
		// Si la dirección es cero, no cambianos nada. Estamos mirando al objetivo
		if (direction.len() == 0.0f) {
			return null;
		}
		
		Character explicitTarget = new Character();
		explicitTarget.setOrientation((float) Math.toDegrees(MathUtils.atan2(-direction.x, direction.y)));
		
		return super.getSteering(source, explicitTarget);
	}

}
