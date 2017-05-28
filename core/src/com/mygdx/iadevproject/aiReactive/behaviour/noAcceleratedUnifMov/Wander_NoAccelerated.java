package com.mygdx.iadevproject.aiReactive.behaviour.noAcceleratedUnifMov;

import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.*;
import com.mygdx.iadevproject.model.Character;

public class Wander_NoAccelerated implements Behaviour {
	
	private Character source;
	private static Random aletorio = new Random();
	// Máxima velocidad lineal (módulo de velocity).
	private float maxSpeed;
	private float maxRotation;
	
	/**
	 * 
	 * @param source
	 * @param maxSpeed Máxima velocidad lineal que se aplica en este comportamiento.
	 * @param maxRotation Máxima velocidad angular que se aplica en este comportamiento.
	 */
	public Wander_NoAccelerated (Character source, float maxSpeed, float maxRotation) {
		this.source = source;
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
	public Steering getSteering() {
		// Creamos el 'Steering' que será devuelto.
		Steering_NoAcceleratedUnifMov output = new Steering_NoAcceleratedUnifMov();

		float randomDif = (aletorio.nextFloat() - aletorio.nextFloat()) * this.maxRotation;
		float finalOrientation = this.source.getOrientation() + randomDif;
		
		// IMPORTANTE -> Al girar a la derecha, el ángulo es negativo (según libgdx). Sin embargo, en el plano, al ir hacia la derecha, ambas coordenadas del vector velocidad deben ser positivas.
		//				Por eso, se pone un signo menos en el seno.
		Vector3 newVectorVelocity = new Vector3((float) -Math.sin(Math.toRadians(finalOrientation)), (float) Math.cos(Math.toRadians(finalOrientation)), 0.0f);
		
		newVectorVelocity = newVectorVelocity.nor();
		newVectorVelocity.x = newVectorVelocity.x * this.maxSpeed;
		newVectorVelocity.y = newVectorVelocity.y * this.maxSpeed;
		newVectorVelocity.z = newVectorVelocity.z * this.maxSpeed;
		output.setVelocity(newVectorVelocity);
		
		// Modificamos la orientación del personaje (source) para que mire hacia el objetivo (en función del vector velocidad que acabamos de calcular).
		this.source.setOrientation(this.source.getNewOrientation(output));
						
		// La rotación (velocidad angular) del steering se pone a 0.
		output.setRotation(0);
						
		return output;
	}

}
