package com.mygdx.iadevproject.model;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.steering.Steering;

// PATRÓN COMPOSITE.
public abstract class Formation extends Character {

	// Lista de personajes que integran la formación.
	private List<Character> charactersList;

	// CONSTRUCTORES.
	// IMPORTANTE -> Al construir la formación NO se le pasa la lista de integrantes como parámetro. Hay 2 métodos especiales para añadir o eliminar un componente de la formación.
	// 		Estos métodos nos permitirá realizar un tratamiento/procesamiento especial a los personajes cuando sean añadidos y eliminados.
	public Formation() {
		super();
		this.charactersList = new LinkedList<Character>();
	}
	
	// CUIDADO -> No confundir la velocidad máxima de la formación con la velocidad máxima de cada uno de sus integrantes.
	public Formation(float maxSpeed) {
		super(maxSpeed);
		this.charactersList = new LinkedList<Character>();
	}
	
	public Formation(float maxSpeed, Texture texture) {
		super(maxSpeed, texture);
		this.charactersList = new LinkedList<Character>();
	}
	
	public Formation(Texture texture) {
		super(texture);
	}

	// GETs y SETs.
	public List<Character> getCharactersList() {
		return charactersList;
	}
	
	// No hay método set para el atributo 'charactersList'.

	// MÉTODOS.
	// Devuelve una lista con las posición de cada uno de los integrantes
	// 		de la formación, en base a la forma de la propia formación.
	// MUY IMPORTANTE -> ESTAS POSICIONES SON RELATIVAS AL CENTRO/POSICIÓN DE LA FORMACIÓN.
	//		Para obtener las posiciones de nuestro mundo habrá que sumarlas a la posición de la formación dentro del mundo.
	protected abstract List<Vector3> getCharactersPosition();
	
	public void addCharacterToCharactersList(Character character) {
		this.charactersList.add(character);
	}
	
	public void deleteCharacterFromCharactersList(Character character) {
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
		
		
		// TODO --> En general la idea podría funcionar. Sin embargo, hay que hacer demasiados cambios en lo que ya hay hecho.
		// 	¿Merece la pena?
		
		
		/*
		
		// Finalmente, se aplica EL BEHAVIOUR DE LA FORMACIÓN a cada uno de los integrantes.
		// 	--> IMPORTANTE -> En este paso hay que tener en cuenta que la posición del target de 'behaviour' ya no será la misma, sino que se 
		// 				verá modificada por la posición que ocupa cada personaje en la formación (LA QUE ACABAMOS DE CALCULAR).
		for (int index = 0; index < this.charactersList.size(); index++) {
			// Primero, guardamos la posición REAL del target de 'behaviour'
			Vector3 realPosition = new Vector3();
			
			
			// Primero, obtenemos el taget (o targets) del behaviour de la formación.
		}
		
		*/
		
		
		
		
	}
}
