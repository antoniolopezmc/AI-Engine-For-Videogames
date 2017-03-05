package com.mygdx.iadevproject.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.steering.Steering;

/**
 * Clase que representa los obstáculos del mundo. Es una subclase de Character donde los métodos que van modificando
 * la posición, orientación, velocidad, etc, están vacíos o modificados para que no se realice nada.
 */
public class Obstacle extends Character {
	
	public Obstacle() {
		super();
	}
	
	public Obstacle(Texture texture) {
		super(texture);
	}

	/**
	 * Método que devuelve un Vector3(0,0,0)
	 */
	@Override
	public Vector3 getVelocity() {
		return new Vector3(0,0,0);
	}
	
	@Override
	public float getNewOrientation(Steering steering) { return 0.0f; }
	
	@Override
	public void applyBehaviour(Character target) { 	}
	
	@Override
	public void update(Steering steering, float time) {  }
}
