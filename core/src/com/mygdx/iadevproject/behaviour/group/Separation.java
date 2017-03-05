package com.mygdx.iadevproject.behaviour.group;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.steering.*;

public class Separation implements Behaviour {
	
	private List<Character> targets; // Lista de objetivos.
	private float threshold; // Distancia máxima para tener en cuenta un objetivo.
	private float decayCoefficient;
	private float maxAcceleration; // Aceleración máxima del personaje.

	public List<Character> getTargets() {
		return targets;
	}

	public void setTargets(List<Character> targets) {
		this.targets = targets;
	}

	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	public float getDecayCoefficient() {
		return decayCoefficient;
	}

	public void setDecayCoefficient(float decayCoefficient) {
		this.decayCoefficient = decayCoefficient;
	}

	public float getMaxAcceleration() {
		return maxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	public Separation(float maxAcceleration, List<Character> targets, float threshold, float decayCoefficient) {
		this.targets = targets;
		this.threshold = threshold;
		this.decayCoefficient = decayCoefficient;
		this.maxAcceleration = maxAcceleration;
	}

	@Override
	public Steering getSteering(Character source, Character target) {
		// Creamos el steering que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
		output.setLineal(new Vector3(0,0,0));
		output.setAngular(0);
		
		// Recorremos la lista de objetivos.
		for (Character character : this.targets) {
			// Comprobamos si el objetivo está a la distancia adecuada para ser tenido en cuenta.
			Vector3 direction = new Vector3(character.getPosition());
			direction = direction.sub(source.getPosition());
			float distance = direction.len();
			if (distance < this.threshold) {
				// Calculamos la fuerza de repulsión.
				float strenght = Math.min(this.decayCoefficient / (distance*distance), this.maxAcceleration);
				
				// Añadimos la aceleración.
				direction = direction.nor();
				output.setLineal(output.getLineal().add(new Vector3(direction.x * strenght, direction.y * strenght, direction.z * strenght)));
			}
		}
		return output;
	}

}
