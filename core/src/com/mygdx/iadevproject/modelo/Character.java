package com.mygdx.iadevproject.modelo;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.*;

/**
 * 
 * Clase que representa a un personaje del videojuego.
 */
public class Character extends Sprite {
	
	// Vector velocidad de 3 componenetes.
	private Vector3 velocity;
	// Escalar que representa la velocidad angular.
	// EXTREMADAMENTE IMPORTANTE -> Lo que en la clase Sprite se llama 'rotation', es realmente lo que nosotros llamamos 'orientación'.
	private float angularRotation;
	// Lista de posibles comportamientos del personaje.
	private List<Behaviour> listBehaviour;
	
	/**
	 * Constructor por defecto.
	 */
	public Character(Texture texture) {
		super(texture);
		listBehaviour = new LinkedList<Behaviour>();
	}
	
	public Vector3 getPosition() {
		return new Vector3(this.getX(), this.getY(), 0.0f);
	}
	
	public void setPosition(Vector3 position) {
		this.setX(position.x);
		this.setY(position.y);
	}
	
	/**
	 * Método 'get' para el atributo 'velocity'.
	 * @return Vector velocidad del personaje.
	 */
	public Vector3 getVelocity() {
		return velocity;
	}
	
	/**
	 * Método 'set' para el atributo 'velocity'.
	 * @param velocity Vector velocidad del personaje.
	 */
	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Método 'get' para el atributo 'rotation'.
	 * @return La velocidad angular del personaje.
	 */
	public float getAngularRotation() {
		return angularRotation;
	}
	
	/**
	 * Método 'set' para el atributo 'rotation'.
	 * @param rotation La velocidad angular del personaje.
	 */
	public void setAngularRotation(float angularRotation) {
		this.angularRotation = angularRotation;
	}
	
	/**
	 * Método 'get' para el atributo 'listBehaviour'.
	 * @return La lista de los posibles comportamientos del personaje.
	 */
	public List<Behaviour> getListBehaviour() {
		return listBehaviour;
	}
	
	/**
	 * Método 'set' para el atributo 'listBehaviour'.
	 * @param listBehaviour La lista de los posibles comportamientos del personaje.
	 */
	public void setListBehaviour(List<Behaviour> listBehaviour) {
		this.listBehaviour = listBehaviour;
	}

	/**
	 * Método para añadir un nuevo comportamiento a la lista del personaje.
	 * @param behaviour Comportamiento a añadir.
	 */
	public void addToListBehaviour(Behaviour behaviour) {
		this.listBehaviour.add(behaviour);
	}
	
	
	// MÉTODOS.
	/**
	 * Método que devuleve la nueva orientación del personaje, a partir de la orientación actual y del steering elegido.
	 * @param steering Steering elegido.
	 * @return
	 */
	public float getNewOrientation (Steering steering) {
		if (steering instanceof Steering_NoAcceleratedUnifMov) {
			Steering_NoAcceleratedUnifMov newSteering = (Steering_NoAcceleratedUnifMov) steering;
			if (newSteering.getSpeed() > 0) {
				// LA CLAVE DEL EXITO. ESTO YA ESTA PROBADO Y FUNCIONA -----> -x, y
				return (float) Math.toDegrees(MathUtils.atan2(-newSteering.getVelocity().x, newSteering.getVelocity().y));
			} else {
				// IMPORTANTE -> En la librería se llama 'Rotation', pero es realmente lo que nosotros llamamos 'orientación'.
				return this.getRotation();
			}
		}
		return this.getRotation();
	}
	
	// **********************************************************************************************
	/**
	 * Aplicar un determinado comportamiento hacia un objetivo (otro personaje). La aplicación de ese comportamiento provocará la actualización del personaje actual (this).
	 * @param target Personaje objetico sobre el que se aplicará el comportamiento.
	 */
	// Este método me lo he inventado. DISCUTIR.
	public void applyBehaviour (Character target) {
		// Como ejemplo se va a coger el primer elemento de la lista de comportamientos.
		this.update(this.listBehaviour.get(0).getSteering(this, target), Gdx.graphics.getDeltaTime());
	}
	// **********************************************************************************************
	
	/**
	 * Método que actualiza la información adecuada del personaje actual (this) en función del tipo de Steering pasado como parámetro y de otro parámetro que indica el tiempo transcurrido entre un frame y en siguiente.
	 * @param steering Steering considerado.
	 * @param time Parámetro tiempo. Indica el tiempo transcurrido entre un frame y en siguiente.
	 */
	public void update(Steering steering, float time) {
		if (steering != null) {
			if (steering instanceof Steering_NoAcceleratedUnifMov) {
				Steering_NoAcceleratedUnifMov newSteering = (Steering_NoAcceleratedUnifMov) steering;
				// Si el Steering es de tipo uniforme no acelerado, se modifica la posición y orientación del personaje en función de la velocidad y rotación del Steering.
				
				// Modificamos la posición del personaje.
				Vector3 velPRODtime = new Vector3(newSteering.getVelocity().x * time, newSteering.getVelocity().y * time, newSteering.getVelocity().z * time);
				this.setPosition(this.getPosition().add(velPRODtime));
				
				//Modificamos la orientación del personaje.
				float rotPRODtime = newSteering.getRotation() * time;
				this.setRotation(this.getRotation() + rotPRODtime);
				
			} else if (steering instanceof Steering_AcceleratedUnifMov) {
				Steering_AcceleratedUnifMov newSteering = (Steering_AcceleratedUnifMov) steering;
				// Si el Steering es de tipo uniforme acelerado, se modifica la posición y orientación del personaje en función de la velocidad y rotación del personaje
				//		y la velocidad y rotación del personaje en función de la aceleración lineal y angular del Steering.
				
				// Modificamos la posición del personaje.
				Vector3 velPRODtime = new Vector3(this.getVelocity().x * time, this.getVelocity().y * time, this.getVelocity().z * time);
				this.setPosition(this.getPosition().add(velPRODtime));
				
				// Modificamos la orientación del personaje.
				float rotPRODtime = this.getRotation() * time;
				this.setRotation(this.getRotation() + rotPRODtime);
				
				// Modificamos la velocidad del personaje.
				Vector3 linPRODtime = new Vector3(newSteering.getLineal().x * time, newSteering.getLineal().y * time, newSteering.getLineal().z * time);
				this.setVelocity(this.velocity.add(linPRODtime));
				
				// Modificamos la rotación (velocidad angular) del personaje.
				float angPRODtime = newSteering.getAngular() * time;
				this.setAngularRotation(this.angularRotation + angPRODtime);
			}
		}
	}
}
