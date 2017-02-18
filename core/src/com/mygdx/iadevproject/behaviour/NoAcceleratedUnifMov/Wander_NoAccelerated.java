package com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov;

import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.*;
import com.mygdx.iadevproject.modelo.Character;

public class Wander_NoAccelerated implements Behaviour {
	
	private static Random aletorio = new Random();
	// TODO Ver si esto es cierto.
	// Máxima velocidad angular.
	private float maxSpeed;
	private float maxRotation;
	
	public Wander_NoAccelerated (float maxSpeed, float maxRotation) {
		this.maxSpeed = maxSpeed;
		this.maxRotation = maxRotation;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxRotation() {
		return maxRotation;
	}

	public void setMaxRotation(float maxRotation) {
		this.maxRotation = maxRotation;
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el 'Steering' que será devuelto.
		Steering_NoAcceleratedUnifMov output = new Steering_NoAcceleratedUnifMov();

		float randomDif = (aletorio.nextFloat() - aletorio.nextFloat()) * this.maxRotation;
		float finalOrientation = source.getOrientation() + randomDif;
		
		// IMPORTANTE -> Al girar a la derecha, el ángulo es negativo (según libgdx). Sin embargo, en el plano, al ir hacia la derecha, ambas coordenadas del vector velocidad deben ser positivas.
		//				Por eso, se pone un signo menos en el seno.
		Vector3 newVectorVelocity = new Vector3((float) -Math.sin(Math.toRadians(finalOrientation)), (float) Math.cos(Math.toRadians(finalOrientation)), 0.0f);
		
		newVectorVelocity = newVectorVelocity.nor();
		newVectorVelocity.x = newVectorVelocity.x * this.maxSpeed;
		newVectorVelocity.y = newVectorVelocity.y * this.maxSpeed;
		newVectorVelocity.z = newVectorVelocity.z * this.maxSpeed;
		output.setVelocity(newVectorVelocity);
		//System.out.println(newVectorVelocity.x + " -- " + newVectorVelocity.y);
		
		// Modificamos la orientación del personaje (source) para que mire hacia el objetivo (en función del vector velocidad que acabamos de calcular).
		source.setOrientation(source.getNewOrientation(output));
						
		// La rotación (velocidad angular) del steering se pone a 0.
		output.setRotation(0);
						
		return output;
	}

}
