package com.mygdx.iadevproject.model;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.arbitrator.Arbitrator;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.*;


/**
 * 
 * Clase que representa a un personaje del videojuego.
 */
public class Character extends WorldObject {
	
	// Constante que indica la importancia por defecto que tienen los comportamientos que se añaden
	// al personaje.
	private static float DEFAULT_IMPORTANCE = 1;
	
	// Mapa de posibles comportamientos del personaje con los valores de importancia de los mismos para el personaje.
	// Está <Float, Behaviour> porque si se quiere tener ordenado por valor de importancia (lo que nos ahorraría muchas comprobaciones
	// cuando se haga el árbitro de prioridades). El TreeMap se ordena por la clave no por el valor, por lo que es necesario que sea así.
	protected Map<Float, Behaviour> listBehaviour;
	// Atributo que indica si el personaje forma parte de una formación. Por defecto está a false.
	private boolean inFormation;
	
	// Árbitro que maneja el comportamiento del personaje
	protected Arbitrator arbitrator;
	
	
	// CONSTRUCTORES.
	public Character(Arbitrator arbitrator) {
		super();
		createListBehaviour();
		this.arbitrator = arbitrator;
	}
	
	public Character(Arbitrator arbitrator, float maxSpeed) {
		super(maxSpeed);
		createListBehaviour();
		this.arbitrator = arbitrator;
	}
	
	public Character(Arbitrator arbitrator, float maxSpeed, Texture texture) {
		super(maxSpeed, texture);
		createListBehaviour();
		this.arbitrator = arbitrator;
	}
	
	public Character(Arbitrator arbitrator, Texture texture) {
		super(texture);
		createListBehaviour();
		this.arbitrator = arbitrator;
	}
	
	
	/**
	 * CONSTRUCTORES PARA LA SUBCLASE FORMATION!
	 */
	protected Character() {
		super();
		createListBehaviour();
	}
	
	protected Character(float maxSpeed) {
		super(maxSpeed);
		createListBehaviour();
	}
	
	protected Character(float maxSpeed, Texture texture) {
		super(maxSpeed, texture);
		createListBehaviour();
	}
	
	protected Character(Texture texture) {
		super(texture);
		createListBehaviour();
	}
	
	/**
	 * Método que crea la lista de comportamientos. Se ha creado este método para evitar
	 * repetir el código en los constructores
	 */
	private void createListBehaviour() {
		// Añadimos el comparador porque si no, el TreeMap por defecto, si se encuentra
		// dos claves que tengan el mismo valor (aunque sean objetos distintos) se queda con 
		// el último que introduces
		this.listBehaviour = new TreeMap<Float, Behaviour>(new Comparator<Float>() {
			@Override
			public int compare(Float o1, Float o2) {
				// Para que los ordene de mayor a menor
				if (o1 > o2) return -1;
				if (o1 == o2) return 0;
				return 1;
			}
		});
	}
	
	// GETs y SETs.
	/**
	 * Método 'get' para el atributo 'listBehaviour'.
	 * @return La lista de los posibles comportamientos del personaje.
	 */
	public Map<Float, Behaviour> getListBehaviour() {
		return listBehaviour;
	}
	
	/**
	 * Método 'set' para el atributo 'listBehaviour'.
	 * @param listBehaviour La lista de los posibles comportamientos del personaje.
	 */
	public void setListBehaviour(Map<Float, Behaviour> listBehaviour) {
		this.listBehaviour = listBehaviour;
	}

	/**
	 * Método para añadir un nuevo comportamiento a la lista del objeto.
	 * @param behaviour Comportamiento a añadir.
	 */
	public void addToListBehaviour(Behaviour behaviour) {
		this.listBehaviour.put(new Float(DEFAULT_IMPORTANCE), behaviour);
	}
	
	/**
	 * Método para añadir un nuevo comportamiento a la lista del objeto cuyo valor
	 * de importancia es 'importance'
	 * @param behaviour Comportamiento a añadir.
	 * @param importance Valor de importancia que tiene ese comportamiento.
	 */
	public void addToListBehaviour(Behaviour behaviour, float importance) {
		this.listBehaviour.put(new Float(importance), behaviour);
	}

	public boolean isInFormation() {
		return inFormation;
	}

	public void setInFormation(boolean inFormation) {
		this.inFormation = inFormation;
	}

	public Arbitrator getArbitrator() {
		return arbitrator;
	}

	public void setArbitrator(Arbitrator arbitrator) {
		this.arbitrator = arbitrator;
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
	public void applyBehaviour () { 
		if (!this.inFormation){
			this.update(this.arbitrator.getSteering(listBehaviour), Gdx.graphics.getDeltaTime());
		}
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