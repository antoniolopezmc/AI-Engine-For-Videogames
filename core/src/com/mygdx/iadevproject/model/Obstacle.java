package com.mygdx.iadevproject.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Clase que representa los obstáculos del mundo. Es una subclase de WorldObject.
 */
public class Obstacle extends WorldObject {
	
	// Los obstaculos son objetos estáticos.
	
	// CONSTRUCTORES.
	public Obstacle(Texture texture) {
		super(0.0f, texture);
	}
	
	public Obstacle() {
		super(0.0f);
	}

	// GETs y SETs SOBREESCRITOS.
	/**
	 * Método que devuelve el vector velocidad del obstáculo. Siempre será el vector nulo.
	 */
	@Override
	public Vector3 getVelocity() {
		return new Vector3(0,0,0);
	}
	
	/**
	 * Método que establece el vector velocidad del obstáculo. Siempre será el vector nulo independientemente del parámetro introducido.
	 */
	public void setVelocity(Vector3 velocity) {
		super.setVelocity(new Vector3(0,0,0));
	}
	
	/**
	 * Método que devuelve la velocidad del obstáculo (módulo del vector velocidad). Siempre será 0.
	 */
	public float getSpeed() {
		return 0.0f;
	}
	
	/**
	 * Método 'get' para el atributo 'rotation_angularSpeed'. Siempre devolverá 0.
	 * @return La velocidad angular del obstáculo.
	 */
	public float getRotation_angularSpeed() {
		return 0.0f;
	}
	
	/**
	 * Método 'set' para el atributo 'rotation_angularSpeed'. Siempre será 0 independientemente del parámetro introducido.
	 * @param rotation La velocidad angular del obstáculo.
	 */
	public void setRotation_angularSpeed(float rotation_angularSpeed) {
		super.setRotation_angularSpeed(0.0f);
	}
	
	/**
	 * Método que devuelve la máxima velocidad a la que puede ir el objeto. Siempre devolverá 0.
	 * @return Máxima velocidad del personaje.
	 */
	public float getMaxSpeed() {
		return 0.0f;
	}

	/** 
	 * Método que establece la velocidad máxima a un objeto. Siempre será 0 independientemente del parámetro introducido.
	 * @param maxSpeed Nueva velocidad máxima.
	 */
	public void setMaxSpeed(float maxSpeed) {
		super.setMaxSpeed(0.0f);
	}
}
