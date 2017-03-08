package com.mygdx.iadevproject.model;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.steering.Steering;

// ---> PATRÓN COMPOSITE.
public abstract class Formation extends Character {

	// Lista de personajes que integran la formación.
	private List<Character> charactersList;
	// Máxima aceleración de la formación. Necesaria para llamar al Seek.
	private float maxAcceleration;

	// CONSTRUCTORES.
	// IMPORTANTE -> Al construir la formación NO se le pasa la lista de integrantes como parámetro. Hay 2 métodos especiales para añadir o eliminar un componente de la formación.
	// 		Estos métodos nos permitirá realizar un tratamiento/procesamiento especial a los personajes cuando sean añadidos y eliminados.
	public Formation(float maxAcceleration) {
		super();
		this.charactersList = new LinkedList<Character>();
	}
	
	// CUIDADO -> No confundir la velocidad máxima de la formación con la velocidad máxima de cada uno de sus integrantes.
	public Formation(float maxAcceleration, float maxSpeed) {
		super(maxSpeed);
		this.charactersList = new LinkedList<Character>();
	}
	
	public Formation(float maxAcceleration, float maxSpeed, Texture texture) {
		super(maxSpeed, texture);
		this.charactersList = new LinkedList<Character>();
	}
	
	public Formation(float maxAcceleration, Texture texture) {
		super(texture);
	}

	// GETs y SETs.
	public List<Character> getCharactersList() {
		return charactersList;
	}
	
	// No hay método set para el atributo 'charactersList'.

	public float getMaxAcceleration() {
		return maxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}
	
	// MÉTODOS.
	// Devuelve una lista con las posición de cada uno de los integrantes
	// 		de la formación, en base a la forma de la propia formación.
	// MUY IMPORTANTE -> ESTAS POSICIONES SON RELATIVAS AL CENTRO/POSICIÓN DE LA FORMACIÓN.
	//		Para obtener las posiciones de nuestro mundo habrá que sumarlas a la posición de la formación dentro del mundo.
	// ------> OBVIAMENTE, LA LONGITUS DE ESTA LISTA DEBE SER IGUAL A LA LONGITUD DE LA LISTA 'charactersList'.
	protected abstract List<Vector3> getCharactersPosition(); // Patrón método plantilla.

	public void addCharacterToCharactersList(Character character) {
		// Al añadir un personaje a la formación, activamos el flag correspondiente.
		character.setInFormation(true);
		this.charactersList.add(character);
	}
	
	public void deleteCharacterFromCharactersList(Character character) {
		// Al eliminar un personaje de la formación, desactivamos el flag correspondiente.
		character.setInFormation(false);
		this.charactersList.remove(character);
	} 
	
	// CUIDADO -> NO CONFUNDIR EL BEHAVIOUR DE LA FORMACIÓN CON LOS BEHAVIOURs DE CADA UNO DE LOS PERSONAJES QUE LA INTEGRAN.
	//		Los behaviours de cada uno de los personajes aquí no valen para nada.
	public void applyBehaviour(Behaviour behaviour) {
		// En primer lugar, aplicamos el behaviour a la propia formación.
		this.update(behaviour.getSteering(), Gdx.graphics.getDeltaTime());
		
		// Ahora, calculamos la lista de posiciones de los personajes de la formación, con respecto a la propia formación.
		// NOS QUEDAMOS CON UNA COPIA PARA CONSERVAR LOS ELEMENTOS DE LA LISTA ORIGINAL.
		List<Vector3> charactersPositionList = new LinkedList<Vector3>(getCharactersPosition());
		
		// Tras el update de la formación, obtenemos su posición, ya que con respecto a ella se moverán los integrantes.
		Vector3 formationPosition = new Vector3(this.getPosition());
		
		// Ahora, calculamos la nueva posición hacia la que deben ir los integrantes de la formación.
		for (Vector3 p : charactersPositionList) {
			p.add(formationPosition);
		}
		
		// YA TENEMOS LAS POSICIONES FINALES DEL MUNDO HACIA LAS QUE DEBEN MOVERSE CADA UNO DE LOS INTEGRANTES DE LA FORMACIÓN.
		
		// Ahora, los personajes de la formación deben ir/encontrarse a/con la formación. Para ello, deben moverse lo más rápido posible. ==> SEEK.
		for (int index = 0; index < this.charactersList.size(); index++) {
			Character thisCharacter = this.charactersList.get(index);
			// Primero, desactivamos el flag del personaje. Si no lo hacemos, no podemos aplicarle ningún comportamiento.
			thisCharacter.setInFormation(false);
			
			// Creamos un personaje ficticio para poder pasarlo al Seek. De este personaje solo nos interesa la posición, ya que es lo único que se usa en el Seek.
			// La posición del personaje ficticio será la correspondiente posición calculada anteriormente.
			Character fakeCharacter = new Character();
			Vector3 targetPosition =  charactersPositionList.get(index);
			float finalPositionX = targetPosition.x;
			float finalPositionY = targetPosition.y;
			float finalPositionZ = targetPosition.z;
			fakeCharacter.setPosition(new Vector3(finalPositionX, finalPositionY, finalPositionZ));
			
			// Ahora, aplicamos el comportamiento al personaje.
			thisCharacter.applyBehaviour(new Seek_Accelerated(thisCharacter, fakeCharacter, this.maxAcceleration));
			
			// Finalmente, volvemos a activar el flag para que no se pueda mover al personaje desde otro sitio.
			thisCharacter.setInFormation(true);
			
		}
		
	}
}
