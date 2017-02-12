package com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.*;
import com.mygdx.iadevproject.modelo.Character;

public class Wander_NoAccelerated implements Behaviour {

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el 'Steering' que será devuelto.
		Steering_NoAcceleratedUnifMov output = new Steering_NoAcceleratedUnifMov();

		// Calculamos el atributo 'velocity' del Steering.
		// Primero, hay que pasar la orientación (ángulo con respecto a la vertical) a un vector. El vector obtenido será unitario (el módulo realmente nos da igual).
					// Si algo revienta, investigar por aquí.
		Vector3 orientationAsVector = new Vector3((float) Math.toRadians(Math.sin(source.getOrientation())), (float) Math.toRadians(Math.cos(source.getOrientation())), (float) 0);
		// Ahora, calculamos 'velocity'.
		output.setVelocity(new Vector3(orientationAsVector.x * source.getMaxSpeed(), orientationAsVector.y * source.getMaxSpeed(), orientationAsVector.z * source.getMaxSpeed()));
		
		// Calculamos el atributo 'rotation' del Steering.
		output.setRotation((Character.aletorio.nextFloat() - Character.aletorio.nextFloat())*source.getMaxRotation());
		
		return output;
	}

}
