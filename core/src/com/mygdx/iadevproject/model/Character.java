package com.mygdx.iadevproject.model;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.*;

/**
 * 
 * Clase que representa a un personaje del videojuego.
 */
public class Character extends WorldObject {
	
	// Lista de posibles comportamientos del personaje.
	private List<Behaviour> listBehaviour;
	// Atributo que indica si el personaje forma parte de una formación. Por defecto está a false.
	private boolean inFormation;
	
	// CONSTRUCTORES.
	public Character() {
		super();
		listBehaviour = new LinkedList<Behaviour>();
	}
	
	public Character(float maxSpeed) {
		super(maxSpeed);
		listBehaviour = new LinkedList<Behaviour>();
	}
	
	public Character(float maxSpeed, Texture texture) {
		super(maxSpeed, texture);
		listBehaviour = new LinkedList<Behaviour>();
	}
	
	public Character(Texture texture) {
		super(texture);
		listBehaviour = new LinkedList<Behaviour>(); 
	}
	
	// GETs y SETs.
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
	 * Método para añadir un nuevo comportamiento a la lista del objeto.
	 * @param behaviour Comportamiento a añadir.
	 */
	public void addToListBehaviour(Behaviour behaviour) {
		this.listBehaviour.add(behaviour);
	}

	public boolean isInFormation() {
		return inFormation;
	}

	public void setInFormation(boolean inFormation) {
		this.inFormation = inFormation;
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
			}
		}
		
		return this.getOrientation();
	}
	
	// **********************************************************************************************
	/**
	 * Aplicar un determinado comportamiento hacia un objetivo (otro personaje). La aplicación de ese comportamiento provocará la actualización del personaje actual (this).
	 * @param target Personaje objetico sobre el que se aplicará el comportamiento.
	 */
	// Este método me lo he inventado. DISCUTIR.
	public void applyBehaviour () { // TODO En este método es donde se implementará la parte del árbitro. Aunque sea otro behavior más, mejor poner como otro atributo, porque será único.
		// Como ejemplo se va a coger el primer elemento de la lista de comportamientos.
		this.applyBehaviour(this.listBehaviour.get(0));
	}
	
	// Aplicar un determinado comportamiento a un personaje. Este comportamiento se le pasa como parámetro.
	public void applyBehaviour (Behaviour behaviour) {
		// Si el personaje forma parte de una formación, sus comportamientos propios no serán tenidos en cuenta.
		// 		No podrá comportarse como él quiera.
		if (!this.inFormation) {
			this.update(behaviour.getSteering(), Gdx.graphics.getDeltaTime());
		}		
	}
	// **********************************************************************************************
	
	/**
	 * Método que actualiza la información adecuada del personaje actual (this) en función del tipo de Steering pasado como parámetro y de otro parámetro que indica el tiempo transcurrido entre un frame y en siguiente.
	 * @param steering Steering considerado.
	 * @param time Parámetro tiempo. Indica el tiempo transcurrido entre un frame y en siguiente.
	 */
	public void update(Steering steering, float time) {
		// --> Si en algún momento 'steering' vale null, da igual porque tampoco entraría a ninguno de los ifs.
		if (steering instanceof Steering_NoAcceleratedUnifMov) {
			Steering_NoAcceleratedUnifMov newSteering = (Steering_NoAcceleratedUnifMov) steering;
			// Si el Steering es de tipo uniforme no acelerado, se modifica la posición y orientación del personaje en función de la velocidad y rotación del Steering.
			
			// Modificamos la posición del personaje.
			Vector3 velPRODtime = new Vector3(newSteering.getVelocity().x * time, newSteering.getVelocity().y * time, newSteering.getVelocity().z * time);
			this.setPosition(this.getPosition().add(velPRODtime));
			
			//Modificamos la orientación del personaje.
			float rotPRODtime = newSteering.getRotation() * time;
			this.setOrientation(this.getOrientation() + rotPRODtime);
			
		} else if (steering instanceof Steering_AcceleratedUnifMov) {
			Steering_AcceleratedUnifMov newSteering = (Steering_AcceleratedUnifMov) steering;
			// Si el Steering es de tipo uniforme acelerado, se modifica la posición y orientación del personaje en función de la velocidad y rotación del personaje
			//		y la velocidad y rotación del personaje en función de la aceleración lineal y angular del Steering.
			
			// Modificamos la velocidad del personaje.
			Vector3 linPRODtime = new Vector3(newSteering.getLineal().x * time, newSteering.getLineal().y * time, newSteering.getLineal().z * time);
			
			Vector3 velocity = new Vector3(this.getVelocity());
			velocity.add(linPRODtime);
			
			this.setVelocity(velocity); 
			
			// Modificamos la rotación (velocidad angular) del personaje.
			float angPRODtime = newSteering.getAngular() * time;
			this.setRotation_angularSpeed(this.getRotation_angularSpeed() + angPRODtime);
			
			// Modificamos la posición del personaje.
			Vector3 velPRODtime = new Vector3(this.getVelocity().x * time, this.getVelocity().y * time, this.getVelocity().z * time);
			this.setPosition(this.getPosition().add(velPRODtime));
			
			// Modificamos la orientación del personaje.
			float rotPRODtime = this.getRotation_angularSpeed() * time;
			this.setOrientation(this.getOrientation() + rotPRODtime);
			
		}
	}
}