package com.mygdx.iadevproject.modelo;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.*;

/**
 * 
 * Clase que representa a un personaje del videojuego.
 */
public class Character {

	// ----> Ya veremos donde se pone esto.
	// La clase personaje va a disponer de una semilla aleatoria para los casos en los que haga falta generar valores aleatorios.
	public static Random aletorio = new Random();
	
	// Ancho y alto.
	private float width,height;
	// Velocidad máxima a la que puede ir el personaje.
	private float maxSpeed;
	// Velocidad angular máxima a la que puede ir el personaje.
	private float maxRotation;
	//TODO ¿Esto se pone aquí o se pone como un atributo del comportamiento tipo 'Arrive'? (porque realmente solo se va a usar en ese caso).
	// Radio de satisfacción del personaje.
	private float satisfactionRadius;
	
	// Vector posición de 3 componentes.
	private Vector3 position;
	// Ángulo de orientación del personaje CON RESPECTO AL EJE VERTICAL. ------> MUY MUY MUY MUY IMPORTANTE -> ESTE ÁNGULO ESTÁ EN GRADOS.
	private float orientation;
	// Vector velocidad de 3 componenetes.
	private Vector3 velocity;
	// Escalar que representa la velocidad angular.
	private float rotation;
	// Lista de posibles comportamientos del personaje.
	private List<Behaviour> listBehaviour;
	
	/**
	 * Constructor por defecto.
	 */
	public Character() {
		listBehaviour = new LinkedList<Behaviour>();
	}
	
	/**
	 * Método 'get' para el atributo 'width'.
	 * @return la anchura del personaje.
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Método 'set' para el atributo 'width'.
	 * @param width La anchura del personaje.
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Método 'get' para el atributo 'height'.
	 * @return La altura del personaje.
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Método 'set' para el atributo 'height'.
	 * @param height La altura del personaje.
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	
	/**
	 * Método 'get' para el atributo 'maxSpeed'.
	 * @return La velocidad máxima a la que puede ir el personaje.
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * Método 'set' para el atributo 'maxSpeed'.
	 * @param maxSpeed La velocidad máxima a la que puede ir el personaje.
	 */
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Método 'get' para el atributo 'maxRotation'.
	 * @return La velocidad angular máxima a la que puede ir el personaje.
	 */
	public float getMaxRotation() {
		return maxRotation;
	}

	/**
	 * Método 'set' para el atributo 'maxRotation'.
	 * @param maxRotation La velocidad angular máxima a la que puede ir el personaje.
	 */
	public void setMaxRotation(float maxRotation) {
		this.maxRotation = maxRotation;
	}
	
	/**
	 * Método 'get' para el atributo 'satisfactionRadius'.
	 * @return El radio de satisfacción del personaje.
	 */
	public float getSatisfactionRadius() {
		return satisfactionRadius;
	}

	/**
	 * Método 'set' para el atributo 'satisfactionRadius'.
	 * @param satisfactionRadius El radio de satisfacción del personaje.
	 */
	public void setSatisfactionRadius(float satisfactionRadius) {
		this.satisfactionRadius = satisfactionRadius;
	}

	/**
	 * Método 'get' para el atributo 'position'.
	 * @return Vector posición del personaje.
	 */
	public Vector3 getPosition() {
		return position;
	}
	
	/**
	 * Método 'set' para el atributo 'position'.
	 * @param position Vector posición del personaje.
	 */
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
	/**
	 * Método 'get' para el atributo 'orientation'.
	 * @return Ángulo de orientación del personaje con respecto al eje vertical.
	 */
	public float getOrientation() {
		return orientation;
	}
	
	/**
	 * Método 'set' para el atributo 'orientation'.
	 * @param orientation Ángulo de orientación del personaje con respecto al eje vertical.
	 */
	public void setOrientation(float orientation) {
		this.orientation = orientation;
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
	public float getRotation() {
		return rotation;
	}
	
	/**
	 * Método 'set' para el atributo 'rotation'.
	 * @param rotation La velocidad angular del personaje.
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
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
				// --------> Si algo no va bien, pensar en esto. (En las coordenadas).
				return (float) Math.toDegrees(MathUtils.atan2(-newSteering.getVelocity().x, newSteering.getVelocity().z));
			} else {
				return this.orientation;
			}
		}
		return this.orientation;
	}
	
	// **********************************************************************************************
	/**
	 * Aplicar un determinado comportamiento hacia un objetivo (otro personaje). La aplicación de ese comportamiento provocará la actualización del personaje actual (this).
	 * @param target Personaje objetico sobre el que se aplicará el comportamiento.
	 */
	// Este método me lo he inventado. DISCUTIR.
	public void applyBehaviour (Character target) {
		// Como ejemplo se va a coger el primer elemento de la lista de comportamientos.
		this.update(this.listBehaviour.get(0).getSteering(this, target), (float) 1/10);
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
				this.setPosition(this.position.add(velPRODtime));
				
				//Modificamos la orientación del personaje.
				float rotPRODtime = newSteering.getRotation() * time;
				this.setOrientation(this.orientation + rotPRODtime);
				
			} else if (steering instanceof Steering_AcceleratedUnifMov) {
				Steering_AcceleratedUnifMov newSteering = (Steering_AcceleratedUnifMov) steering;
				// Si el Steering es de tipo uniforme acelerado, se modifica la posición y orientación del personaje en función de la velocidad y rotación del personaje
				//		y la velocidad y rotación del personaje en función de la aceleración lineal y angular del Steering.
				
				// Modificamos la posición del personaje.
				Vector3 velPRODtime = new Vector3(this.getVelocity().x * time, this.getVelocity().y * time, this.getVelocity().z * time);
				this.setPosition(this.position.add(velPRODtime));
				
				// Modificamos la orientación del personaje.
				float rotPRODtime = this.getRotation() * time;
				this.setOrientation(this.orientation + rotPRODtime);
				
				// Modificamos la velocidad del personaje.
				Vector3 linPRODtime = new Vector3(newSteering.getLineal().x * time, newSteering.getLineal().y * time, newSteering.getLineal().z * time);
				this.setVelocity(this.velocity.add(linPRODtime));
				
				// Modificamos la rotación (velocidad angular) del personaje.
				float angPRODtime = newSteering.getAngular() * time;
				this.setRotation(this.rotation + angPRODtime);
			}
		}
	}

}
