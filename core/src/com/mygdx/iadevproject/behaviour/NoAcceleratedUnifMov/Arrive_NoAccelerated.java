package com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_NoAcceleratedUnifMov;
import com.mygdx.iadevproject.modelo.Character;

public class Arrive_NoAccelerated implements Behaviour {
	
	private float maxSpeed;
	private float satisfactionTargetRadius;
	private float timeToTarget;
	
	public Arrive_NoAccelerated (float maxSpeed, float satisfactionTargetRadius, float timeToTarget) {
		this.maxSpeed = maxSpeed;
		this.satisfactionTargetRadius = satisfactionTargetRadius;
		this.timeToTarget = timeToTarget;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getSatisfactionTargetRadius() {
		return satisfactionTargetRadius;
	}

	public void setSatisfactionTargetRadius(float satisfactionTargetRadius) {
		this.satisfactionTargetRadius = satisfactionTargetRadius;
	}

	public float getTimeToTarget() {
		return timeToTarget;
	}

	public void setTimeToTarget(float timeToTarget) {
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el 'Steering' que será devuelto.
		Steering_NoAcceleratedUnifMov output = new Steering_NoAcceleratedUnifMov();
		
		// Calculamos el atributo 'velocity'.
		Vector3 finalVelocity = new Vector3(target.getPosition()); 
		finalVelocity = finalVelocity.sub(source.getPosition());
		output.setVelocity(finalVelocity);
		
		// Comprobamos si EL MÓDULO DEL VECTOR (DE MOMENTO, LA DIFERENCIA ENTRE AMBAS POSICIONES) está fuera o dentro del radio de satisfacción del objetivo.
		if (output.getSpeed() < this.satisfactionTargetRadius) {
			// Si esta dentro, entonces ya no tenemos que hacer ningún movimiento.
			// Vector de velocidad 0, por tanto el objeto se para.
			output.setVelocity(new Vector3(0.0f, 0.0f, 0.0f));
			output.setRotation(0);
			return output;
		}
		
		// Si está fuera, nos debemos mover hacía nuestro objetivo, pero nos gustaría que ese desplazamiento fuera en el tiempo indicado.
		finalVelocity.x = finalVelocity.x / this.timeToTarget;
		finalVelocity.y = finalVelocity.y / this.timeToTarget;
		finalVelocity.z = finalVelocity.z / this.timeToTarget; 
		
		// Si vamos demasiado rápido, reducimos a la máxima velocidad.
		if (output.getSpeed() > this.maxSpeed) {
			finalVelocity = finalVelocity.nor();
			finalVelocity.x = finalVelocity.x * this.maxSpeed;
			finalVelocity.y = finalVelocity.y * this.maxSpeed;
			finalVelocity.z = finalVelocity.z * this.maxSpeed;
		}
		
		// Modificamos la orientación del personaje (source) para que mire hacia el objetivo (en función del vector velocidad que acabamos de calcular).
		source.setOrientation(source.getNewOrientation(output));
		
		// La rotación (velocidad angular) del steering se pone a 0.
		output.setRotation(0);
		
		return output;
	}

}
