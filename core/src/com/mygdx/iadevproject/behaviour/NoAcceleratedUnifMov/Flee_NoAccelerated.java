package com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_NoAcceleratedUnifMov;
import com.mygdx.iadevproject.modelo.Character;

public class Flee_NoAccelerated implements Behaviour {

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el 'Steering' que será devuelto.
		Steering_NoAcceleratedUnifMov output = new Steering_NoAcceleratedUnifMov();
				
		// Calculamos el atributo 'velocity'.
		Vector3 copy = new Vector3(source.getPosition());
		Vector3 finalVelocity = copy.sub(target.getPosition()).nor();
		
		finalVelocity.x = finalVelocity.x * source.getMaxSpeed();
		finalVelocity.y = finalVelocity.y * source.getMaxSpeed();
		finalVelocity.z = finalVelocity.z * source.getMaxSpeed(); 
		output.setVelocity(finalVelocity);
				
		// Modificamos la orientación del personaje (source) para que mire hacia el objetivo (en función del vector velocidad que acabamos de calcular).
		source.setOrientation(source.getNewOrientation(output));
				
		// La rotación (velocidad angular) del steering se pone a 0.
		output.setRotation(0);
				
		return output;
	}

}
