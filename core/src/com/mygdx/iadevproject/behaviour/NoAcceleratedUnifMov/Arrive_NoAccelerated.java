package com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_NoAcceleratedUnifMov;
import com.mygdx.iadevproject.modelo.Character;

public class Arrive_NoAccelerated implements Behaviour {
	
	private float timeToTarget;
	
	/**
	 * Constructor por defecto.
	 */
	public Arrive_NoAccelerated() {
		this((float) 0.25);
	}
	
	/**
	 * Constructor de 1 parámetro.
	 * @param timeToTarget Tiempo hasta el objetivo.
	 */
	public Arrive_NoAccelerated (float timeToTarget) {
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el 'Steering' que será devuelto.
		Steering_NoAcceleratedUnifMov output = new Steering_NoAcceleratedUnifMov();
		
		// Calculamos el atributo 'velocity'.
		Vector3 finalVelocity = target.getPosition().sub(source.getPosition());
		output.setVelocity(finalVelocity);
		
		//TODO ¿Por qué se compara con la velocidad? ¿Qué sentido tiene eso?
		// Comprobamos si estamos fuera o dentro del radio de satisfacción del objetivo.
		if (output.getSpeed() < target.getSatisfactionRadius()) {
			// Si esta dentro, entonces ya no tenemos que hacer ningún movimiento.
			return null;
		}
		
		// Si está fuera, nos debemos mover hacía nuestro objetivo, pero nos gustaría que ese desplazamiento fuera en el tiempo indicado.
		finalVelocity.x = finalVelocity.x / this.timeToTarget;
		finalVelocity.y = finalVelocity.y / this.timeToTarget;
		finalVelocity.z = finalVelocity.z / this.timeToTarget; 
		output.setVelocity(finalVelocity);
		
		// Si vamos demasiado rápido, reducimos a la máxima velocidad.
		finalVelocity = finalVelocity.nor();
		finalVelocity.x = finalVelocity.x * source.getMaxSpeed();
		finalVelocity.y = finalVelocity.y * source.getMaxSpeed();
		finalVelocity.z = finalVelocity.z * source.getMaxSpeed();
		output.setVelocity(finalVelocity);
		
		//TODO ¿Por qué se modifica el personaje dentro de este método? ¿Eso no lo hace ya el 'update' de la clase 'Character'?
		// Modificamos la orientación del personaje (source) para que mire hacia el objetivo (en función del vector velocidad que acabamos de calcular).
		source.setOrientation(source.getNewOrientation(output));
		
		// La rotación (velocidad angular) del steering se pone a 0.
		output.setRotation(0);
		
		return output;
	}

}
