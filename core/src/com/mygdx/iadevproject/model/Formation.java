package com.mygdx.iadevproject.model;

import java.util.*;

import com.mygdx.iadevproject.steering.Steering;

// ---> PATRÓN COMPOSITE.
public abstract class Formation extends Character {

	// Lista de personajes que integran la formación.
	private List<Character> charactersList;

	// CONSTRUCTORES.
	// En una formación no hay textura.
	// En una formación no hay velocidad máxima. No tiene sentido, ya que 
	// 		el movimiento de la formación se delegará al movimiento de cada uno
	//		de los integrantes.
	public Formation(List<Character> charactersList) {
		super();
		if (charactersList != null) {
			this.charactersList = charactersList;
		} else {
			this.charactersList = new LinkedList<Character>();
		}
	}

	// GETs y SETs.
	public List<Character> getCharactersList() {
		return charactersList;
	}

	public void setCharactersList(List<Character> charactersList) {
		if (charactersList != null) {
			this.charactersList = charactersList;
		} else {
			this.charactersList = new LinkedList<Character>();
		}
	}
	
	public void addToCharactersList(Character character) {
		this.charactersList.add(character);
	}

	// MÉTODOS.
	public void update(Steering steering, float time) {
		for (Character character : charactersList) {
			character.update(steering, time);
		}
	}
}
