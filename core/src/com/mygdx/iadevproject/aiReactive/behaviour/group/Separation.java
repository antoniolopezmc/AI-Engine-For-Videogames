package com.mygdx.iadevproject.aiReactive.behaviour.group;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.*;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class Separation implements Behaviour {
	
	private Character source;
	private List<WorldObject> targets; // Lista de objetivos.
	private float threshold; // Distancia máxima para tener en cuenta un objetivo.
	private float decayCoefficient;
	private float maxAcceleration; // Aceleración máxima del personaje.

	/**
	 * Constructor de la clase.
	 * @param source
	 * @param maxAcceleration Máxima aceleración a aplicar en el comportamiento.
	 * @param targets Lista de WorldObject (objetivos).
	 * @param threshold Radio en el que se tienen en cuenta los objetivos (todos los elementos fuera de este radio no son tenidos en cuenta al aplicar el comportamiento).
	 * @param decayCoefficient Coeficiente de repulsión.
	 */
	public Separation(Character source, float maxAcceleration, List<WorldObject> targets, float threshold, float decayCoefficient) {
		this.source = source;
		this.targets = targets;
		this.threshold = threshold;
		this.decayCoefficient = decayCoefficient;
		this.maxAcceleration = maxAcceleration;
	}
	
	public List<WorldObject> getTargets() {
		return targets;
	}

	public void setTargets(List<WorldObject> targets) {
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

	@Override
	public Steering getSteering() {
		// Creamos el steering que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
		output.setLineal(new Vector3(0,0,0));
		output.setAngular(0);
		
		// Recorremos la lista de objetivos.
		for (WorldObject character : this.targets) {
			// Comprobamos si el objetivo está a la distancia adecuada para ser tenido en cuenta.
			Vector3 direction = new Vector3(character.getPosition());
			direction = direction.sub(this.source.getPosition());
			float distance = direction.len();
			if (distance < this.threshold) {
				// Calculamos la fuerza de repulsión.
				float strenght = Math.min(this.decayCoefficient / (distance*distance), this.maxAcceleration);
				
				// Añadimos la aceleración.
				direction = direction.nor();
				Vector3 incremento = output.getLineal();
				// En los apuntes está al revés. Pero así es como funciona.
				incremento = incremento.add(new Vector3(-direction.x * strenght, -direction.y * strenght, -direction.z * strenght));
				output.setLineal(incremento);
			}
		}
		return output;
	}

}
