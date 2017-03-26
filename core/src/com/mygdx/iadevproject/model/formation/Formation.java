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

	
	// Los comportamiento ¡¡¡¡DE LOS INTEGRANTES!!!! de cada tipo de formación, dependerán de la formación concreta. (Esta funcionalidad se delega a los hijos).
	// --> Los hijos deberán implementar este método, que se encargará de aplicar el árbitro sobre la lista de comportamieentos en cada uno de los hijos y devolver el Steergin final.
	// Como parámetro se le pasa el personaje actual de la formación y el punto final al que debe ir (en forma de WorldObject).
	// --> Hemos tomado esta decisión, precisamente, para que cada hijo pueda tener su propio comportamiento para los integrantes de la formación.
	protected abstract Steering getComponentFormationSteerginToApply(Character source, WorldObject fakeTarget);// ---> Patrón método plantilla.
	
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

	// No va a haber 'setComponentFormationArbitrator'. En esta ocasión el árbitro será siempre por prioridad.
	
	public int getComponentFormationOrientationMode() {
		return componentFormationOrientationMode;
	}

	public void setComponentFormationOrientationMode(int mode) {
		this.componentFormationOrientationMode = mode;
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
			//		Es mejor el arrive con radio pequeño porque así cuando el personaje llegue a la región interior se parará,
			//			en vez de estar oscilando continuamente.
			for (int index = 0; index < this.charactersList.size(); index++) {
				// Recuperamos el personaje de la lista.
				Character thisCharacter = this.charactersList.get(index);
				// Primero, desactivamos el flag del personaje. Si no lo hacemos, no podemos aplicarle ningún comportamiento.
				thisCharacter.setInFormation(false);
				
				// Creamos un personaje ficticio para poder pasarlo al comportamiento.
				// 	---> La posición del personaje ficticio será la correspondiente posición calculada anteriormente.
				WorldObject fakeTarget = new Obstacle();
				Vector3 targetPosition =  charactersPositionList.get(index);
				fakeTarget.setPosition(new Vector3(targetPosition.x, targetPosition.y, targetPosition.z));
				
				// Aplicamos un steering al integrante actual de la formación. Para obtener dicho steering delegamos en el hijo (en la formación concreta).
				thisCharacter.applySteering(this.getComponentFormationSteerginToApply(thisCharacter, fakeTarget));
				
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
	// Se pone como 'protected' para que los hijos puedan acceder a ella.
	protected float calculateOrientation (Vector3 vector) {
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
