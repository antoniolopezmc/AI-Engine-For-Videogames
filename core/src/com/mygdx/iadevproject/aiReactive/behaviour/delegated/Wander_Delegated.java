package com.mygdx.iadevproject.aiReactive.behaviour.delegated;

import java.util.Random;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

//TODO IMPORTANTE -> PROBAR.

public class Wander_Delegated extends Face implements Behaviour {

	private ShapeRenderer renderer = null;
    
	public ShapeRenderer getRenderer() {
		return this.renderer;
	}
	
	public void setRenderer(ShapeRenderer renderer) {
		this.renderer = renderer;
	}
	
	private void debug() {
		if (IADeVProject.PRINT_PATH_BEHAVIOUR && this.renderer != null) {
			
		}
	}
	
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
	 * Constructor. Primeros 5 parámetros significan lo mismo que los del Align
	 * 
	 * @param maxAngularAcceleration
	 * @param maxRotation
	 * @param targetRadius
	 * @param slowRadius
	 * @param timeToTarget
	 * @param wanderOffset Distancia desde el personaje hasta el Facing.
	 * @param wanderRadius Radio del círculo del Facing.
	 * @param wanderRate Máximo grado que puede girar.
	 * @param wanderOrientation Orientación actual del personaje.
	 * @param maxAcceleration Máxima aceleración.
	 */
	public Wander_Delegated(Character source, float maxAngularAcceleration, float maxRotation, float targetRadius, float slowRadius, float timeToTarget,
			float wanderOffset, float wanderRadius, float wanderRate, float wanderOrientation, float maxAcceleration) {
		super(source, null, maxAngularAcceleration, maxRotation, targetRadius, slowRadius, timeToTarget);
		
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
	public Steering getSteering() {
		// 1.- Calculamos el objetivo hacia donde mirar
		
		// Actualizamos la orientación del wander
		this.wanderOrientation += (aletorio.nextFloat() - aletorio.nextFloat()) * this.wanderRate;
		
		// Calculamos la orientación del objetivo
		float targetOrientation = this.wanderOrientation + this.getSource().getOrientation();
		
		// Calculamos el centro del círculo Wander
		Vector3 sourceOrientationVector = new Vector3((float) -Math.sin(Math.toRadians(super.getSource().getOrientation())), (float) Math.cos(Math.toRadians(super.getSource().getOrientation())), 0.0f);
		sourceOrientationVector.x *= this.wanderOffset;
		sourceOrientationVector.y *= this.wanderOffset;
		sourceOrientationVector.z *= this.wanderOffset;
		
		Vector3 targetPosition = new Vector3();
		targetPosition.x = this.getSource().getPosition().x + sourceOrientationVector.x;
		targetPosition.y = this.getSource().getPosition().y + sourceOrientationVector.y;
		targetPosition.z = this.getSource().getPosition().z + sourceOrientationVector.z;
		
		// Calculamos la locacización del objetivo
		Vector3 targetOrientationVector = new Vector3((float) -Math.sin(Math.toRadians(targetOrientation)), (float) Math.cos(Math.toRadians(targetOrientation)), 0.0f);
		
		targetPosition.x += this.wanderRadius * targetOrientationVector.x;
		targetPosition.y += this.wanderRadius * targetOrientationVector.y;
		targetPosition.z += this.wanderRadius * targetOrientationVector.z;
		
		// 2.- Delegamos en el Behaviour Face:
		WorldObject targetExplicit = new Obstacle();
		targetExplicit.setPosition(targetPosition);
		// Establecemos como objetivo el objetivo calculado.
		this.setTarget(targetExplicit);
		Steering steering = super.getSteering();

		// Comprobamos que el steering que produce el Face sea de tipo
		// acelerado. Si no lo es, no hacemos nada.
		if (steering instanceof Steering_AcceleratedUnifMov) {
			Steering_AcceleratedUnifMov output = (Steering_AcceleratedUnifMov) steering;
			
			// Creamos el vector lineal como el vector de la orientación del personaje multiplicado por la máxima aceleración
			Vector3 lineal = new Vector3((float) -Math.sin(Math.toRadians(super.getSource().getOrientation())), (float) Math.cos(Math.toRadians(super.getSource().getOrientation())), 0.0f);
			lineal.x *= this.maxAcceleration;
			lineal.y *= this.maxAcceleration;
			lineal.z *= this.maxAcceleration;
			
			output.setLineal(lineal);
			
			return steering;	
		} else {
			return null;
		}
	}

}
