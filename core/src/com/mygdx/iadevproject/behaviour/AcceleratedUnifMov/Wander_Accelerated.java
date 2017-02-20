package com.mygdx.iadevproject.behaviour.AcceleratedUnifMov;

import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.modelo.Character;

public class Wander_Accelerated implements Behaviour {

	private static Random aletorio = new Random();
	
	// Distancia desde el personaje hasta el Facing
	private float wanderOffset;
	// Radio del círculo del Facing
	private float wanderRadius;
	// Máximo grado que puede girar
	private float wanderRate;
	// Orientación actual del personaje
	private float wanderOrientation;
	// Máxima aceleración
	private float maxAcceleration;
	
	/**
	 * Constructor.
	 * @param wanderOffset - Distancia desde el personaje hasta el Facing.
	 * @param wanderRadius - Radio del círculo del Facing.
	 * @param wanderRate - Máximo grado que puede girar.
	 * @param wanderOrientation - Orientación actual del personaje.
	 * @param maxAcceleration - Máxima aceleración.
	 */
	public Wander_Accelerated(float wanderOffset, float wanderRadius, float wanderRate, float wanderOrientation, float maxAcceleration) {
		this.wanderOffset = wanderOffset;
		this.wanderRadius = wanderRadius;
		this.wanderRate = wanderRate;
		this.wanderOrientation = wanderOrientation;
		this.maxAcceleration = maxAcceleration;
	}
	
	public float getWanderOffset() {
		return wanderOffset;
	}

	public void setWanderOffset(float wanderOffset) {
		this.wanderOffset = wanderOffset;
	}

	public float getWanderRadius() {
		return wanderRadius;
	}

	public void setWanderRadius(float wanderRadius) {
		this.wanderRadius = wanderRadius;
	}

	public float getWanderRate() {
		return wanderRate;
	}

	public void setWanderRate(float wanderRate) {
		this.wanderRate = wanderRate;
	}

	public float getWanderOrientation() {
		return wanderOrientation;
	}

	public void setWanderOrientation(float wanderOrientation) {
		this.wanderOrientation = wanderOrientation;
	}

	public float getMaxAcceleration() {
		return maxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// 1.- Calculamos el objetivo hacia donde mirar
		
		// Actualizamos la orientación del wander
		this.wanderOrientation += (aletorio.nextFloat() - aletorio.nextFloat()) * this.wanderRate;
		
		// Calculamos la orientación del objetivo
		float targetOrientation = this.wanderOrientation + source.getOrientation();
		
		// Calculamos el centro del círculo Wander
		Vector3 sourceOrientationVector = new Vector3((float) -Math.sin(Math.toRadians(source.getOrientation())), (float) Math.cos(Math.toRadians(source.getOrientation())), 0.0f);
		sourceOrientationVector.x *= this.wanderOffset;
		sourceOrientationVector.y *= this.wanderOffset;
		sourceOrientationVector.z *= this.wanderOffset;
		
		Vector3 targetVector = new Vector3();
		targetVector.x = source.getPosition().x + sourceOrientationVector.x;
		targetVector.y = source.getPosition().y + sourceOrientationVector.y;
		targetVector.z = source.getPosition().z + sourceOrientationVector.z;
		
		// Calculamos la locacización del objetivo
		Vector3 targetOrientationVector = new Vector3((float) -Math.sin(Math.toRadians(targetOrientation)), (float) Math.cos(Math.toRadians(targetOrientation)), 0.0f);
		
		targetVector.x += this.wanderRadius * targetOrientationVector.x;
		targetVector.y += this.wanderRadius * targetOrientationVector.y;
		targetVector.z += this.wanderRadius * targetOrientationVector.z;
		
		// 2.- Delegamos en el Behaviour Face:
		// Face face = new Face();
		// Steering output = face.getSteering(source, target);
		
		// Como no tenemos implementado el Face, creo un steering nuevo
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
		
		// Creamos el vector lineal como el vector de la orientación del personaje multiplicado por la máxima aceleración
		Vector3 lineal = new Vector3((float) -Math.sin(Math.toRadians(source.getOrientation())), (float) Math.cos(Math.toRadians(source.getOrientation())), 0.0f);
		lineal.x *= this.maxAcceleration;
		lineal.y *= this.maxAcceleration;
		lineal.z *= this.maxAcceleration;
		
		output.setLineal(lineal);
		output.setAngular(0);
		
		return output;
	}

}
