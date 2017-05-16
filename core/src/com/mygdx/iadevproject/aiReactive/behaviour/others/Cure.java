package com.mygdx.iadevproject.aiReactive.behaviour.others;

import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.model.Character;

public class Cure implements Behaviour {

	/**
	 * Método para pintar las líneas de debug del Behaviour
	 */
	private void debug() {
		if (IADeVProject.PRINT_PATH_BEHAVIOUR) {	
			IADeVProject.batch.begin();
			// Imprimimos la palabra Attack para representar que un personaje está atacando.
			IADeVProject.font.draw(IADeVProject.batch, "Cure", source.getPosition().x + 20, source.getPosition().y + 20);
			IADeVProject.batch.end();
		}
	}
	
	// Personaje que se está curando.
	private Character source;
	// Vida que se incrementa al source.
	private float health;
	
	public Cure(Character source, float health) {
		super();
		this.source = source;
		this.health = health;
	}

	public Character getSource() {
		return source;
	}

	public void setSource(Character source) {
		this.source = source;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	/**
	 * Hemos decidido implementar la curación como un Behavior para poder tratarlo de manera homogenea con los otros Behaviours y poder
	 * meterlo en el Map.
	 * Aunque este Behaviour devuelva null y un árbitro nunca lo tenga en cuenta, al menos el código sí se ha ejecutado y la vida del
	 * source sí se ha incrementado (QUE ES REALMENTE LO QUE ANDAMOS BUSCANDO).
	 */
	@Override
	public Steering getSteering() {
		// Realizamos la curación, añadiendo vida al source.
		source.addHealth(this.health);
		
		this.debug(); // Mostramos información de depuración, si procede.
		
		// Devolvemos null, puesto que lo importante de este Behaviour no es el propio Behavior sino la reducción de vida del target.
		return null;
	}

}
