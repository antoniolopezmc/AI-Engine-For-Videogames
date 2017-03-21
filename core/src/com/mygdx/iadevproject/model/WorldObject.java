package com.mygdx.iadevproject.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

/**
 * 
 * Clase que representa a un objeto o entidad del juego.
 */
public abstract class WorldObject extends Sprite {

	// PARA OBJETOS ESTÁTICOS, TODO LO QUE TENGA QUE VER CON LA VELOCIDAD (DE CUALQUIER TIPO) SERÁ SIEMPRE 0.
	// EN OBJETOS EN MOVIMIENTO (PERSONAJES) LA VELOCIDAD SÍ TOMARÁ VALORES DISTINTOS DE 0.
	
	// Vector velocidad de 3 componenetes.
	private Vector3 velocity;
	// Escalar que representa la velocidad angular.
	// EXTREMADAMENTE IMPORTANTE -> Lo que en la clase Sprite se llama 'rotation', es realmente lo que nosotros llamamos 'orientación'.
	private float rotation_angularSpeed;
	
	private float minBoxLength; // Longitud mínima de la caja de detección
	// IMPORTANTE -> No confundir con maxSpeed de Behaviour (de algunos).
	private float maxSpeed; 	// Máxima velocidad a la que puede ir el personaje, independientemente de su comportamiento.

	// CONSTRUCTORES.
	public WorldObject() {
		super();
		this.maxSpeed = Float.MAX_VALUE; // Si no establecemos una velocidad máxima, no hay valocidad máxima.
		this.setOrigin(this.getWidth()/2, this.getHeight()/2);
	}
	
	public WorldObject(float maxSpeed) {
		super();
		this.maxSpeed = maxSpeed;
		this.setOrigin(this.getWidth()/2, this.getHeight()/2);
	}
	
	public WorldObject(float maxSpeed, Texture texture) {
		super(texture);
		this.maxSpeed = maxSpeed;
		this.setOrigin(this.getWidth()/2, this.getHeight()/2);
	}
	
	public WorldObject(Texture texture) {
		super(texture);
		this.maxSpeed = Float.MAX_VALUE; // Si no establecemos una velocidad máxima, no hay valocidad máxima.
		this.setOrigin(this.getWidth()/2, this.getHeight()/2);
	}
	
	// GETs y SETs.
	public Vector3 getPosition() {
		return new Vector3(this.getX(), this.getY(), 0.0f);
	}
	
	public void setPosition(Vector3 position) {
		this.setX(position.x);
		this.setY(position.y);
	}
	
	public float getOrientation() {
		// EXTREMADAMENTE IMPORTANTE -> Lo que en la clase Sprite se llama 'rotation', es realmente lo que nosotros llamamos 'orientación'.
		// IMPORTANTE -> Para obtener la orientación, se llama al método del padre.
		// 		El de 'this' está sobreescrito para evitar confusiones con los nombres.
		return super.getRotation();
	}
	
	public void setOrientation(float orientation) {
		// IMPORTANTE -> Para establecer la orientación, se llama al método del padre. El de 'this' está sobreescrito para evitar confusiones con los nombres.
		float realOrientation = orientation % 360;
		
		if (realOrientation < 0) { realOrientation += 360; }
		
		super.setRotation(realOrientation);
	}
	
	public float getRotation() {
		throw new IllegalAccessError("Este no es el método al que tienes que llamar. ¡Te estás liando!");
	}
	
	public void setRotation(float rotation) {
		throw new IllegalAccessError("Este no es el método al que tienes que llamar. ¡Te estás liando!");
	}
	
	/**
	 * Método 'get' para el atributo 'velocity'.
	 * @return Vector velocidad del objeto.
	 */
	public Vector3 getVelocity() {
		return velocity;
	}
	
	/**
	 * Método 'set' para el atributo 'velocity'.
	 * @param velocity Vector velocidad del objeto.
	 */
	public void setVelocity(Vector3 velocity) {
		Vector3 vel = new Vector3(velocity);
		if (vel.len() > this.maxSpeed) { 
			// Si la velocidad que nos pasan como parámetro tiene un módulo mayor que la máxima velocidad
			// a la que puede ir el personaje, la establecemos a la máxima velocidad
			vel = vel.nor();
	
			vel.x *= this.maxSpeed;
			vel.y *= this.maxSpeed;
			vel.z *= this.maxSpeed;
		}
		
		this.velocity = vel;
	}
	
	/**
	 * Método que devuelve la velocidad a la que va el objeto.
	 * @return Velocidad a la que va el objeto.
	 */
	public float getSpeed() {
		if (this.velocity == null) {return 0.0f;}
		return this.velocity.len();
	}
	
	/**
	 * Método 'get' para el atributo 'rotation_angularSpeed'.
	 * @return La velocidad angular del objeto.
	 */
	public float getRotation_angularSpeed() {
		return rotation_angularSpeed;
	}
	
	/**
	 * Método 'set' para el atributo 'rotation_angularSpeed'.
	 * @param rotation La velocidad angular del objeto.
	 */
	public void setRotation_angularSpeed(float rotation_angularSpeed) {
		this.rotation_angularSpeed = rotation_angularSpeed;
	}
	
	/**
	 * Método que obtiene la longitud mínima de la caja de detección.
	 * @return Longitud mínima de la caja de detección.
	 */
	public float getMinBoxLength() {
		return minBoxLength;
	}
	
	/**
	 * Método que establece la longitud mínima de la caja de detección.
	 * @param minBoxLength Nueva longitud mínima.
	 */
	public void setMinBoxLength(float minBoxLength) {
		this.minBoxLength = minBoxLength;
	}
	
	/**
	 * Método que devuelve la máxima velocidad a la que puede ir el objeto.
	 * @return Máxima velocidad del personaje.
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}

	/** 
	 * Método que establece la velocidad máxima a un objeto.
	 * @param maxSpeed Nueva velocidad máxima.
	 */
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	/**
	 * Método que obtiene el radio que recubre al objeto.
	 * @return Radio que recubre al objeto.
	 */
	public float getBoundingRadius() {
		//TODO Lo he calculado como el 80% del minimo de los lados del rectángulo que lo recubre.
		//TODO CONSULTAR CON ANTONIO A VER QUÉ LE PARECE
		
		float maxEdge = Math.min(this.getBoundingRectangle().height, this.getBoundingRectangle().width);
		return (float) maxEdge * 0.8f;
	}

	/** 
	 * Método que calcula el centro de masa del objeto
	 * @return Centro de masa del objeto
	 */
	public Vector3 getCenterOfMass() {
		return new Vector3(this.getX(), this.getY(), 0.0f);
	}
	
	
	// MÉTODOS PARA DIBUJAR EL Sprite SOBREESCRITOS.
	public void draw(Batch batch) {
		if (this.getTexture() != null) {
			// Cuando libgdx dibuja un Sprite, lo hace de tal manera que la posición de dicho Sprite es el vértice DE ABAJO A LA IZQUIERDA de la textura.
			// Sin embargo, los que nosotros queremos es que en CENTRO de la textura sea la posición del Sprite.
			// --> Por tanto, a la hora de dibujar se cambiará momentaneamente la posición real del objeto.
			Vector3 realPosition = new Vector3(this.getPosition());
			this.setPosition(this.getPosition().x - this.getWidth()/2, this.getPosition().y - this.getHeight()/2);
			super.draw(batch);
			this.setPosition(realPosition);
		}
	}
	
	public void draw(Batch batch, float alphaModulation) {
		this.draw(batch);
	}
}
