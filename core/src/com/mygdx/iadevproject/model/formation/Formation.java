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
import com.mygdx.iadevproject.aiReactive.arbitrator.PriorityArbitrator;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.noAcceleratedUnifMov.Arrive_NoAccelerated;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

// ---> PATRÓN COMPOSITE.
public abstract class Formation extends Character {

	// Lista de personajes que integran la formación.
	private List<Character> charactersList;

	// CONSTRUCTORES.
	// IMPORTANTE -> Al construir la formación NO se le pasa la lista de integrantes como parámetro. Hay 2 métodos especiales para añadir o eliminar un componente de la formación.
	// 		Estos métodos nos permitirá realizar un tratamiento/procesamiento especial a los personajes cuando sean añadidos y eliminados.
	public Formation(Arbitrator arbitrator) {
		super(arbitrator);
		this.charactersList = new LinkedList<Character>();
	}
	
	// CUIDADO -> No confundir la velocidad máxima de la formación con la velocidad máxima de cada uno de sus integrantes.
	public Formation(Arbitrator arbitrator, float maxSpeed) {
		super(arbitrator, maxSpeed);
		this.charactersList = new LinkedList<Character>();
	}
	
	public Formation(Arbitrator arbitrator, float maxSpeed, Texture texture) {
		super(arbitrator, maxSpeed, texture);
		this.charactersList = new LinkedList<Character>();
	}
	
	// GETs y SETs.
	public List<Character> getCharactersList() {
		return charactersList;
	}
	
	public int getNumberOfCharacters() {
		return this.getCharactersList().size();
	}
	
	// No hay método set para el atributo 'charactersList'.
	
	// MÉTODOS.
	// Devuelve una lista con las posición de cada uno de los integrantes
	// 		de la formación, en base a la forma de la propia formación.
	// MUY IMPORTANTE -> ESTAS POSICIONES SON RELATIVAS AL CENTRO/POSICIÓN DE LA FORMACIÓN.
	//		Para obtener las posiciones de nuestro mundo habrá que sumarlas a la posición de la formación dentro del mundo.
	// ------> OBVIAMENTE, LA LONGITUD DE ESTA LISTA DEBE SER IGUAL A LA LONGITUD DE LA LISTA 'charactersList'.
	protected abstract List<Vector3> getCharactersPosition(); // ---> Patrón método plantilla.
	
	// MUY IMPORTANTE -> Las orientaciones de los personajes empiezan en la vertical (orientación 0º está en la parte superior de la vertical).
	// 			Los ángulos de una formación empiezan en la horizontal (el ángulo de 0º está a la derecha). -> Circunferencia goniométrica.

	public void addCharacterToCharactersList(Character character) {
		// Solo podemos añadir un personaje a una formación si no pertenece a ninguna otra.
		if (!character.isInFormation()) {
			// Al añadir un personaje a la formación, activamos el flag correspondiente.
			character.setInFormation(true);
			this.charactersList.add(character);
		}
	}
	
	public void deleteCharacterFromCharactersList(Character character) {
		// Al eliminar un personaje de la formación, desactivamos el flag correspondiente.
		character.setInFormation(false);
		this.charactersList.remove(character);
	} 
	
	/**
	 * MUY IMPORTANTE. DE JM -> HE CAMBIADO EL MÉTODO A applyBehaviour() POR QUE YA EL MÉTODO DEL PADRE LLAMA AL ÁRBITRO,
	 * NO DEVUELVE DIRECTAMENTE EL PRIMERO DE LA LISTA. DE ESTA MANERA, SOBREESCRIBIMOS ESTE MÉTODO PARA QUE AHORA EN VEZ DE 
	 * OBTENER EL STEERING DEL ARGUMENTO QUE NOS PASABAN, LO OBTENEMOS DEL ÁRBITRO QUE TIENE LA FORMACIÓN. 
	 * 
	 * LO HE PROBADO Y FUNCIONA!!
	 */
	// CUIDADO -> NO CONFUNDIR EL BEHAVIOUR DE LA FORMACIÓN CON LOS BEHAVIOURs DE CADA UNO DE LOS PERSONAJES QUE LA INTEGRAN.
	//		Los behaviours de cada uno de los personajes aquí no valen para nada.
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
			//		Es mejor el arrive con radio pequeño porque así cuando el personaje llegue a la región interior se parará.
			for (int index = 0; index < this.charactersList.size(); index++) {
				Character thisCharacter = this.charactersList.get(index);
				// Primero, desactivamos el flag del personaje. Si no lo hacemos, no podemos aplicarle ningún comportamiento.
				thisCharacter.setInFormation(false);
				
				// Creamos un personaje ficticio para poder pasarlo al Seek/Arrive. De este personaje solo nos interesa la posición,
				//		ya que es lo único que se usa en el Seek/Arrive.
				// La posición del personaje ficticio será la correspondiente posición calculada anteriormente.
				WorldObject fakeCharacter = new Obstacle();
				Vector3 targetPosition =  charactersPositionList.get(index);
				fakeCharacter.setPosition(new Vector3(targetPosition.x, targetPosition.y, targetPosition.z));
				
				/**
				 * 										EXTREMADAMENTE IMPORTANTE.
				 * 
				 * Cuando nosotros hacemos una formación, lo esperado es que los personajes 'formen' los más rápido posible,
				 * es decir, que vayan a la posición lo más rápido posible y mantengan la formación. Y AHÍ SE QUEDEN SIN MOVERSE.
				 * 
				 *  Esto es extremadamente complejo hacerlo con un movimiento ACELERADO, ya que en este tipo de movimientos el steering
				 *  solamente devuelve la acelaración y, por tanto, controlar la velocidad de un personaje y donde queremos que se pare
				 *  exactamente es más complicado.
				 *  
				 *  Si queremos una total precisión en el punto de parada de un personaje (como es el caso de las formaciones), tenemos
				 *  que hacer uso de comportamientos NO ACELERADOS. En los steerings de este tipo de comportamientos sí podemos acceder
				 *  y establecer la velocidad que finalmente será aplicada al personaje.
				 *  
				 *  ---> Sería interesante hacer varias demos de prueba con movimientos ACELERADOS para mostrar 
				 *  que es lo que pasa y por qué no funciona en esto casos.
				 */
	//			ESTO ES LO QUE HA PUESTO JM y FUNCIONA AL PELO!!
				Arbitrator arbitrator = new PriorityArbitrator(1e-3f);
				Map<Float, Behaviour> map = new TreeMap<Float, Behaviour>(new Comparator<Float>() {
					@Override
					public int compare(Float o1, Float o2) {
						// Para que los ordene de mayor a menor
						if (o1 > o2) return -1;
						if (o1 == o2) return 0;
						return 1;
					}
				});
				map.put(30.0f, new Arrive_NoAccelerated(thisCharacter, fakeCharacter, thisCharacter.getMaxSpeed(), 5.0f, 1.0f));
				WorldObject invented = new Obstacle();
				Vector3 center = new Vector3(formationPosition);
				Vector3 extreme = new Vector3(fakeCharacter.getPosition());
				invented.setOrientation(getOrientation(extreme.sub(center)));
				map.put(1.0f, new Align_Accelerated(thisCharacter, invented, 30.0f, 20.0f, 1.0f, 10.0f, 1.0f));
				
				thisCharacter.applySteering(arbitrator.getSteering(map));
				
	
	//			ESTO ES LO QUE TENÍA ANTONIO
	//			// Ahora, aplicamos el comportamiento al personaje.
	//			thisCharacter.applyBehaviour(new Arrive_NoAccelerated(thisCharacter, fakeCharacter, thisCharacter.getMaxSpeed(), 5.0f, 1.0f));
				
				// Finalmente, volvemos a activar el flag para que no se pueda mover al personaje desde otro sitio.
				thisCharacter.setInFormation(true);
				
			}
		}
	}
	
	/**
	 * Método que obtiene la orientación de un vector.
	 * @param vector - Vector del que obtenemos la orientación
	 * @return - Orientación del vector 
	 */
	private float getOrientation (Vector3 vector) {
		return (float) Math.toDegrees(MathUtils.atan2(-vector.x, vector.y));
	}
	
	public void drawFormationPoints(ShapeRenderer renderer) {
		// Ahora, calculamos la lista de posiciones de los personajes de la formación, con respecto a la propia formación.
		List<Vector3> charactersPositionList = getCharactersPosition();
		
		// Tras el update de la formación, obtenemos su nueva posición, ya que con respecto a ella se moverán los integrantes.
		Vector3 formationPosition = new Vector3(this.getPosition());
		
		// Ahora, calculamos la nueva posición hacia la que deben ir los integrantes de la formación.
		for (Vector3 p : charactersPositionList) {
			p.add(formationPosition);
		}
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.RED);
		for (Vector3 vector : charactersPositionList) {
			renderer.circle(vector.x, vector.y, 2);
		}
		renderer.end();
	}
}
