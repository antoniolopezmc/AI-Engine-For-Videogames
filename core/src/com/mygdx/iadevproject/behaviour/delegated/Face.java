package com.mygdx.iadevproject.behaviour.delegated;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.behaviour.acceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.steering.Steering;

//TODO IMPORTANTE -> PROBAR.

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
		
		// Si la dirección es cero, no cambianos nada. Estamos mirando al objetivo
		if (direction.len() == 0.0f) {
			return null;
		}
		
		WorldObject explicitTarget = new Character();
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
