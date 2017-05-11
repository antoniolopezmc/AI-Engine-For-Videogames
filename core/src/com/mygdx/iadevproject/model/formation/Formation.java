package com.mygdx.iadevproject.model.formation;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.arbitrator.Arbitrator;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

// ---> PATRÓN COMPOSITE.
public abstract class Formation extends Character {

	// Los integrantes de una formación, podrán orientarse de distinta manera trás haber creado la formación.
	public static int FREE_ORIENTATION = 0; // POR DEFECTO.
	public static int LOOK_INSIDE = 1;
	public static int LOOK_OUTSIDE = 2;
	public static int SAME_ORIENTATION = 3; // Todos los integrantes mirarán hacia el mismo sitio (orientación de la formación).
	
	// Lista de personajes que integran la formación.
	private List<Character> charactersList;
	// Orientación final de los ¡¡¡componentes del la formación!!!
	private int componentFormationOrientationMode;

	// CONSTRUCTORES.
	// IMPORTANTE -> Al construir la formación NO se le pasa la lista de integrantes como parámetro. Hay 2 métodos especiales para añadir o eliminar un componente de la formación.
	// 		Estos métodos nos permitirá realizar un tratamiento/procesamiento especial a los personajes cuando sean añadidos y eliminados.
	public Formation(Arbitrator arbitrator) {
		super(arbitrator);
		this.charactersList = new LinkedList<Character>();
		this.componentFormationOrientationMode = 0;
	}
	
	// CUIDADO -> No confundir la velocidad máxima de la formación con la velocidad máxima de cada uno de sus integrantes.
	public Formation(Arbitrator arbitrator, float maxSpeed) {
		super(arbitrator, maxSpeed);
		this.charactersList = new LinkedList<Character>();
		this.componentFormationOrientationMode = 0;
	}
	
	public Formation(Arbitrator arbitrator, float maxSpeed, Texture texture) {
		super(arbitrator, maxSpeed, texture);
		this.charactersList = new LinkedList<Character>();
		this.componentFormationOrientationMode = 0;
	}
	
	// GETs y SETs.
	public List<Character> getCharactersList() {
		return charactersList;
	}
	
	public int getNumberOfCharacters() {
		// Un integrante de una formación puede ser un personaje u otra formación. Eso hay que tenerlo en cuenta.
		int resultado = 0;
		for (Character character : this.charactersList) {
			if (character instanceof Formation) {
				Formation charForm = (Formation) character;
				resultado = resultado + charForm.getNumberOfCharacters();
			} else { 
				resultado = resultado + 1;
			}
		}
		return resultado;
	}
	
	// No hay método set para el atributo 'charactersList'.
	
	// MÉTODOS.
	// Devuelve una lista con las posición de cada uno de los integrantes
	// 		de la formación, en base a la forma de la propia formación.
	// MUY IMPORTANTE -> ESTAS POSICIONES SON RELATIVAS AL CENTRO/POSICIÓN DE LA FORMACIÓN.
	//		Para obtener las posiciones de nuestro mundo habrá que sumarlas a la posición de la formación dentro del mundo.
	// ------> OBVIAMENTE, LA LONGITUD DE ESTA LISTA DEBE SER IGUAL A LA LONGITUD DE LA LISTA 'charactersList'.
	/**
	 * Método que calcula todas las posiciones a las que deben ir los componentes de la formación.
	 * @return Lista de posiciones en las que los componentes de la formación se situarán.
	 */
	protected abstract List<Vector3> getCharactersPosition(); // ---> Patrón método plantilla.
	
	// MUY IMPORTANTE -> Las orientaciones de los personajes empiezan en la vertical (orientación 0º está en la parte superior de la vertical).
	// 			Los ángulos de una formación empiezan en la horizontal (el ángulo de 0º está a la derecha). -> Circunferencia goniométrica.

	
	// Los comportamiento ¡¡¡¡DE LOS INTEGRANTES!!!! de cada tipo de formación, dependerán de la formación concreta. (Esta funcionalidad se delega a los hijos).
	// --> Los hijos deberán implementar este método, que se encargará de aplicar el árbitro sobre la lista de comportamieentos en cada uno de los hijos y devolver el Steergin final.
	// Como parámetro se le pasa el personaje actual de la formación y el punto final al que debe ir (en forma de WorldObject).
	// --> Hemos tomado esta decisión, precisamente, para que cada hijo pueda tener su propio comportamiento para los integrantes de la formación.
	/**
	 * Método plantilla que devuelve el steering que se aplicará sobre cada uno de los integrantes de la formación.
	 * El target solamente puede ser un punto y no nada más complejo, puesto que al fin y al cabo, los integrantes de la formación deben limitarse a ir al punto que ha sido calculado para ellos.
	 * @param source Personaje origen.
	 * @param fakeTarget Punto destino.
	 * @return El steering que se aplicará sobre el componente de la formación.
	 */
	protected abstract Steering getComponentFormationSteerginToApply(Character source, WorldObject fakeTarget);// ---> Patrón método plantilla.
	
	// ===> EXTREMADAMENTE IMPORTANTE  <===
	// Cuando una formación ataca, el behaviour de atacar no se añadirá a la lista de comportamientos de la propia formación, sino a la lista ¡¡¡¡DE LOS INTEGRANTES!!!! (la que se mete a pelo en los hijos).
	// 	Por tanto, pata saber cuando estoy atacando voy a neceistar 2 nuevos atributos: un flag de ataque y la distancia máxima de ataque. Si ese flag está a true, el behaviour de attack se añadirá a los hijos.
	protected boolean flag_attack = false;
	protected float max_distance_attack = 0.0f;
	protected Character target_attack;
	protected float health_attack = 0.0f;

	/**
	 * Indica a los componentes de la formación que deben atacar a un enemigo.
	 * @param max_distance_attack Máxima distancia para realizar el ataque.
	 * @param target_attack Objetivo del ataque.
	 * @param health_attack Salud que se resta al objetivo en el ataque.
	 */
	public void enableAttackMode (float max_distance_attack, Character target_attack, float health_attack) {
		this.flag_attack = true;
		this.max_distance_attack = max_distance_attack;
		this.target_attack = target_attack;
		this.health_attack = health_attack;
	}
	
	/**
	 * Indica a los componentes de la formación que no deben atacar a nadie.
	 */
	public void disableAttackMode() {
		this.flag_attack = false;
	}
	
	/**
	 * Añade un personaje a la formación, solo sí no está ya en una formación.
	 * @param character Personaje a añadir a la formación,
	 */
	public void addCharacterToCharactersList(Character character) {
		// Solo podemos añadir un personaje a una formación si no pertenece a ninguna otra.
		if (!character.isInFormation()) {
			// Al añadir un personaje a la formación, le indicamos la formación a la que pertenece (this).
			character.setFormation(this);
			this.charactersList.add(character);
		}
	}
	
	/**
	 * Elimina un personaje de la formación.
	 * @param character Personaje a eliminar de la formación.
	 */
	public void deleteCharacterFromCharactersList(Character character) {
		// Para eliminar un personaje de una formación, éste debe pertenecer a esta formación.
		if (character.getFormation() == this) {
			// Al eliminar un personaje de la formación, el atributo correspondiente del personaje vuelve a ser null.
			character.setFormation(null);
			this.charactersList.remove(character);
		}
	}

	// No va a haber 'setComponentFormationArbitrator'. En esta ocasión el árbitro será siempre por prioridad.
	
	/**
	 * Obtenemos el tipo de orientación que adoptarán los componentes de la formación.
	 * @return Tipo de orientación que adoptarán los componentes de la formación.
	 */
	public int getComponentFormationOrientationMode() {
		return componentFormationOrientationMode;
	}

	/**
	 * Establecemos el tipo de orientación que adoptarán los componentes de la formación.
	 * @param mode Tipo de orientación a aplicar.
	 */
	public void setComponentFormationOrientationMode(int mode) {
		this.componentFormationOrientationMode = mode;
	}
	
	/*
	 * MUY IMPORTANTE. DE JM -> HE CAMBIADO EL MÉTODO A applyBehaviour() POR QUE YA EL MÉTODO DEL PADRE LLAMA AL ÁRBITRO,
	 * NO DEVUELVE DIRECTAMENTE EL PRIMERO DE LA LISTA. DE ESTA MANERA, SOBREESCRIBIMOS ESTE MÉTODO PARA QUE AHORA EN VEZ DE 
	 * OBTENER EL STEERING DEL ARGUMENTO QUE NOS PASABAN, LO OBTENEMOS DEL ÁRBITRO QUE TIENE LA FORMACIÓN. 
	 * 
	 * LO HE PROBADO Y FUNCIONA!!
	 */
	// CUIDADO -> NO CONFUNDIR EL BEHAVIOUR DE LA FORMACIÓN CON LOS BEHAVIOURs DE CADA UNO DE LOS PERSONAJES QUE LA INTEGRAN.
	//		Los behaviours de cada uno de los personajes aquí no valen para nada.
	/**
	 * Método para aplicar un steering a la formación.
	 * @param steer Steering a aplicar.
	 */
	public void applySteering(Steering steer) {
		if (!this.isInFormation()) {
			// En primer lugar, aplicamos el behaviour a la propia formación.
			this.update(steer, Gdx.graphics.getDeltaTime());
			
			// Ahora, calculamos la lista de posiciones de los personajes de la formación, con respecto a la propia formación.
			List<Vector3> charactersPositionList = getCharactersPosition();
			
			// Tras el update de la formación, obtenemos su nueva posición, ya que con respecto a ella se moverán los integrantes.
			Vector3 formationPosition = new Vector3(this.getPosition());
			
			// Ahora, calculamos la nueva posición hacia la que deben ir los integrantes de la formación.
			for (Vector3 p : charactersPositionList) {
				p.add(formationPosition);
			}
			
			// YA TENEMOS LAS POSICIONES FINALES DEL MUNDO HACIA LAS QUE DEBEN MOVERSE CADA UNO DE LOS INTEGRANTES DE LA FORMACIÓN.
			
			// Ahora, los personajes de la formación deben ir/encontrarse a/con la formación.
			// 		Para ello, deben moverse lo más rápido posible. ==> SEEK o ARRIVE con radio muy pequeño.
			//		Es mejor el arrive con radio pequeño porque así cuando el personaje llegue a la región interior se parará,
			//			en vez de estar oscilando continuamente.
			for (int index = 0; index < this.charactersList.size(); index++) {
				// Recuperamos el personaje de la lista.
				Character thisCharacter = this.charactersList.get(index);
				// Primero, indicamos momentaneamente que el personaje no está en ninguna formación. Si no lo hacemos, no podemos aplicarle ningún comportamiento.
				thisCharacter.setFormation(null);
				
				// Creamos un personaje ficticio para poder pasarlo al comportamiento.
				// 	---> La posición del personaje ficticio será la correspondiente posición calculada anteriormente.
				WorldObject fakeTarget = new Obstacle();
				Vector3 targetPosition =  charactersPositionList.get(index);
				fakeTarget.setPosition(new Vector3(targetPosition.x, targetPosition.y, targetPosition.z));
				
				// Aplicamos un steering al integrante actual de la formación. Para obtener dicho steering delegamos en el hijo (en la formación concreta).
				thisCharacter.applySteering(this.getComponentFormationSteerginToApply(thisCharacter, fakeTarget));
				
				// Finalmente, volvemos a indicar la formación a la que pertenece el personaje para que no se le pueda mover desde otro sitio.
				thisCharacter.setFormation(this);
				
			}
		}
	}

	/**
	 * Método que obtiene la orientación de un vector.
	 * @param vector - Vector del que obtenemos la orientación
	 * @return - Orientación del vector 
	 */
	// Se pone como 'protected' para que los hijos puedan acceder a ella.
	protected float calculateOrientation (Vector3 vector) {
		return (float) Math.toDegrees(MathUtils.atan2(-vector.x, vector.y));
	}
	
	public void reduceHealth (float health) {
		// MUY MUY MUY MUY MUY IMPORTANTE.
		// --> Las formaciones no pierden vida.
		// --> A un objeto formación como tal no le puede atacar ni puede perder vida.
		
		// Por tanto, este método no hace nada.
	}
	
	/**
	 * Método que dibuja las posiciones donde se deben colocar los componentes de la formación.
	 * @param renderer Renderer sobre el que dibujar.
	 */
	public void drawFormationPoints(ShapeRenderer renderer) {
		// Calculamos la lista de posiciones de los personajes de la formación, con respecto a la propia formación.
		List<Vector3> charactersPositionList = getCharactersPosition();
		
		// Obtenemos la posición de la formación.
		Vector3 formationPosition = new Vector3(this.getPosition());
		
		// Ahora, calculamos la nueva posición hacia la que deben ir los integrantes de la formación.
		for (Vector3 p : charactersPositionList) {
			p.add(formationPosition);
		}
		
		// Dibujamos las posiciones resultantes.
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.RED);
		for (Vector3 vector : charactersPositionList) {
			renderer.circle(vector.x, vector.y, 2);
		}
		renderer.end();
	}
}
