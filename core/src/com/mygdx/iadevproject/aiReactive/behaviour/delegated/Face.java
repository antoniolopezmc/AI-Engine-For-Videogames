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

public class Face extends Align_Accelerated implements Behaviour {

	public Face(Character source, WorldObject target, float maxAngularAcceleration, float maxRotation, float targetRadius, float slowRadius, float timeToTarget) {
		super(source, target, maxAngularAcceleration, maxRotation, targetRadius, slowRadius, timeToTarget);
	}

	@Override
	public Steering getSteering() {
		// 1.- Calcular el objetivo al que alinearse
		
		// Calcular la dirección hacia el objetivo
		Vector3 direction = new Vector3(this.getTarget().getPosition());
		direction = direction.sub(this.getSource().getPosition());
		
		// Si la dirección es cero, no cambianos nada. Estamos sobre el objetivo
		if (direction.len() == 0.0f) {
			Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
			output.setAngular(0);
			output.setLineal(new Vector3(0,0,0));
			return output;
		}
		
		WorldObject explicitTarget = new Obstacle();
		explicitTarget.setOrientation((float) Math.toDegrees(MathUtils.atan2(-direction.x, direction.y)));
		
		// Almacenamos el objetivo principal para poder llamar al método del padre con el 'explicitTarget'
		// y no perder el objetivo principal.
		WorldObject aux = this.getTarget();
		this.setTarget(explicitTarget);
		// Llamamos al 'getSteering' del padre
		Steering output = super.getSteering();
		// Recuperamos el objetivo principal
		this.setTarget(aux);
		
		// Devolvemos el steering calculado
		return output;
	}

}
