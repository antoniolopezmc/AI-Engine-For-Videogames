package com.mygdx.iadevproject.model;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.arbitrator.Arbitrator;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.*;
import com.mygdx.iadevproject.aiTactical.roles.TacticalRole;
import com.mygdx.iadevproject.map.Ground;
import com.mygdx.iadevproject.model.formation.Formation;


/**
 * Clase que representa a un personaje del videojuego.
 */
public class Character extends WorldObject {
	
	// Constante que indica la importancia por defecto que tienen los comportamientos que se añaden
	// al personaje.
	private static float DEFAULT_IMPORTANCE = 1;
	public static float DEFAULT_HEALTH = 20000.0f; // La vida baja rápido. Hay que poner un valor adecuado apra que se vea el descenso.
	
	// Mapa de posibles comportamientos del personaje con los valores de importancia de los mismos para el personaje.
	// Está <Float, Behaviour> porque si se quiere tener ordenado por valor de importancia (lo que nos ahorraría muchas comprobaciones
	// cuando se haga el árbitro de prioridades). El TreeMap se ordena por la clave no por el valor, por lo que es necesario que sea así.
	private Map<Float, Behaviour> listBehaviour;
	// Formación a la que pertece el personaje. Si no pertecene a ninguna está a null. HAY QUE TENER CUIDADO AL MANEJAR LA DOBLE REFERENCIA.
	private Formation formation;
	
	// Árbitro que maneja el comportamiento del personaje
	private Arbitrator arbitrator;
	// Rol del personaje
	private TacticalRole role;
	
	// MUY IMPORTANTE -> A las formaciones también hay que ponerles el equipo al que pertenecen.
	private Team team;
	private float currentHealth;
	private float maxHealth;
	private float previousHealth; // Este atributo es para la comprobación de si están pegando a un personaje
	

	// CONSTRUCTORES.
	public Character(Arbitrator arbitrator) {
		super();
		initializeAttributes(arbitrator);
	}
	
	public Character(Arbitrator arbitrator, float maxSpeed) {
		super(maxSpeed);
		initializeAttributes(arbitrator);
	}
	
	public Character(Arbitrator arbitrator, float maxSpeed, Texture texture) {
		super(maxSpeed, texture);
		initializeAttributes(arbitrator);
	}
	
	public Character(Arbitrator arbitrator, Texture texture) {
		super(texture);
		initializeAttributes(arbitrator);
	}
	
	private void initializeAttributes(Arbitrator arbitrator) {
		createListBehaviour();
		this.arbitrator = arbitrator;
		this.currentHealth = DEFAULT_HEALTH;
		this.maxHealth = DEFAULT_HEALTH;
		this.previousHealth = DEFAULT_HEALTH;
		this.team = Team.NEUTRAL;
	}
	
	
	/**
	 * Método que crea la lista de comportamientos. Se ha creado este método para evitar
	 * repetir el código en los constructores. IMPORTANTE: LA CREACIÓN DEL MAP PERMITE
	 * QUE SE LE PASE CUALQUIER VALOR COMO CLAVE, YA SEA UN float O UN new Float() Y
	 * NO SE SUSTITUYE.
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
		// Un personaje está en una formación si el atributo formación es distinto de null.
		// Dicho atributo corresponderá con la formación en la que está el personaje.
		return this.formation != null;
	}

	public Formation getFormation() {
		return formation;
	}

	public void setFormation(Formation formation) {
		this.formation = formation;
	}

	public Arbitrator getArbitrator() {
		return arbitrator;
	}

	public void setArbitrator(Arbitrator arbitrator) {
		this.arbitrator = arbitrator;
	}
	
	public TacticalRole getRole() {
		return role;
	}
	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public float getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(float currentHealth) {
		this.currentHealth = currentHealth;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getPreviousHealth() {
		return previousHealth;
	}

	public void setPreviousHealth(float previousHealth) {
		this.previousHealth = previousHealth;
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
	
	

	
	
	
	
	
	
	
	
	public void initializeTacticalRole(TacticalRole role) {
		this.role = role;
		// Devolver lista de comportamientos inicial llamando 
		this.role.initialize(this);
	}	
	
	public void updateTacticalRole() {
		this.role.update(this);
		this.applyBehaviour();
	}
	
	
	
	
	
	
	
	
	
	
	
	// **********************************************************************************************
	/**
	 * Aplicar un determinado comportamiento hacia un objetivo (otro personaje). La aplicación de ese comportamiento provocará la actualización del personaje actual (this).
	 */
	public void applyBehaviour () {
		if (!this.isInFormation()) {
			Steering steer = this.arbitrator.getSteering(listBehaviour);
			this.applySteering(steer);
		}
	}
	
	/**
	 * Al personaje se le aplica el steering pasado como parámetro. 
	 * @param steer Steering a aplicar.
	 */
	// Aplicar un determinado Steering a un personaje. Este Steering se le pasa como parámetro.
	public void applySteering (Steering steer) {
		// Si el personaje forma parte de una formación, sus comportamientos propios no serán tenidos en cuenta.
		// 		No podrá comportarse como él quiera.
		if (!this.isInFormation()) {
			this.update(steer, Gdx.graphics.getDeltaTime());
		}		
	}
	// **********************************************************************************************
	
	/**
	 * Método que actualiza la información adecuada del personaje actual (this) en función del tipo de Steering pasado como parámetro y de otro parámetro que indica el tiempo transcurrido entre un frame y en siguiente.
	 * @param steering Steering considerado.
	 * @param time Parámetro tiempo. Indica el tiempo transcurrido entre un frame y en siguiente.
	 */
	public void update(Steering steering, float time) {
		// ESTO ES DE LA PARTE TÁCTICA
		// Calculamos el factor de velocidad a aplicar al personaje.
		float velocityFactor = this.getVelocityFactorOfThisCharacter();
		
		
		// --> Si en algún momento 'steering' vale null, da igual porque tampoco entraría a ninguno de los ifs.
		if (steering instanceof Steering_NoAcceleratedUnifMov) {
			Steering_NoAcceleratedUnifMov newSteering = (Steering_NoAcceleratedUnifMov) steering;
			// Si el Steering es de tipo uniforme no acelerado, se modifica la posición y orientación del personaje en función de la velocidad y rotación del Steering.
			
			// Modificamos la posición del personaje.
			Vector3 velPRODtime = new Vector3(newSteering.getVelocity().x * time * velocityFactor,
					newSteering.getVelocity().y * time * velocityFactor,
					newSteering.getVelocity().z * time * velocityFactor);
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
			Vector3 velPRODtime = new Vector3(this.getVelocity().x * time * velocityFactor,
					this.getVelocity().y * time * velocityFactor,
					this.getVelocity().z * time * velocityFactor);
			this.setPosition(this.getPosition().add(velPRODtime));
			
			// Modificamos la orientación del personaje.
			float rotPRODtime = this.getRotation_angularSpeed() * time;
			this.setOrientation(this.getOrientation() + rotPRODtime);
			
		}
	}
	
	// Método privado que calcula el factor de velocidad del personaje. Este factor de velocidad se usará en 'update'.
	// 	Es método pertenece a la parte táctica.
	private float getVelocityFactorOfThisCharacter() {
		float velocityFactor;
		// A la hora de comprobar si debe aplicarse un factor de velocidad o ralentización de velocidad, hay varias alternativas.
		if ((this.role == null) && (this.formation == null)) {
			// Si el personaje no tiene rol y no pertenece a ninguna formación, se asigna 1 (es decir, no hay ralentización).
			velocityFactor = 1.0f;
		} else if ((this.role == null) && (this.formation != null)) {
			// Obtenemos el terreno sobre el que se encuentra el personaje.
			Ground g = IADeVProject.getGroundOfPosition(this.getPosition());
			// Si el personaje forma parte de una formación, se accede al rol de la formación.
			velocityFactor = this.formation.getRole().getVelocityFactor(g);
		} else if (this.role != null) {
			// Obtenemos el terreno sobre el que se encuentra el personaje.
			Ground g = IADeVProject.getGroundOfPosition(this.getPosition());
			// Si el personaje tiene un rol, consultamos a dicho rol.
			velocityFactor = this.role.getVelocityFactor(g);
		} else {
			// Por defecto, 1
			velocityFactor = 1.0f;
		}
		return velocityFactor;
	}
	
	// **********************************************************************************************
	
	/**
	 * Método para reducir la vida del personaje.
	 * @param health Vida que queremos restar.
	 */
	public void reduceHealth (float health) {
		this.currentHealth = Math.max(0.0f, this.currentHealth - health);
	}
	
	/**
	 * Método para aumentar la vida del personaje.
	 * @param health Vida que queremos sumar.
	 */
	public void addHealth (float health) {
		this.currentHealth = Math.min(this.maxHealth, this.currentHealth + health);
		this.previousHealth = this.currentHealth; // TODO Pensar.
	}
	
	/**
	 * Método para dibujar la cantidad de vida que tiene el personaje.
	 * @param batch
	 * @param font
	 */
	public void drawHealth (SpriteBatch batch, BitmapFont font) {
		batch.begin();
		font.draw(batch, "Health: " + this.getCurrentHealth(), this.getPosition().x + 20, this.getPosition().y); // Dibujamos la vida a la derecha del personaje.
        batch.end();
	}
}