package com.mygdx.iadevproject.behaviour.Delegated;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.steering.Steering;

//TODO IMPORTANTE -> PROBAR.

public class Persue extends Seek_Accelerated implements Behaviour {
	
	// Tiempo máximo de predicción. En segundos.
	private float maxPrediction;
	
	public Persue(Character source, WorldObject target, float maxAcceleration, float maxPrediction) {
		super(source, target, maxAcceleration);
		this.maxPrediction = maxPrediction;
	}
	
	public float getMaxPrediction() {
		return maxPrediction;
	}

	public void setMaxPrediction(float maxPrediction) {
		this.maxPrediction = maxPrediction;
	}

	@Override
	public Steering getSteering() {
		// Calculamos los datos necesarios para poder llamar al 'getSteering' del Seek. (Llamada a super).
		// IMPORTANTE -> Como tenemos que hacer una predicción, se creará un personaje ficticio. Ese personaje será lo que se pase realmente al 'getSteering' del Seek.
		
		// Calculamos la dirección (el vector) y distancia entre la fuente y el objetivo REAL.
		Vector3 direction = new Vector3(this.getTarget().getPosition());
		direction = direction.sub(this.getSource().getPosition());
		float distance = direction.len(); // Módulo del vector 'direction'.
		
		// Calculamos el escalar correspondiente a la velocidad de la fuente. (Módulo de velocity.)
		float speed = this.getSource().getVelocity().len();
		
		// Comprobamos si la velocidad es menor o igual que la distancia entre el máximo tiempo para predicción.
		// 	-> En ese caso, el tiempo de predicción en el máximo posible.
		float prediction; // Tiempo sobre el que se hace la predicción.
		if (speed <= (distance / maxPrediction)) {
			prediction = maxPrediction;
		// Sino, calculamos el tiempo de predicción.
		} else {
			prediction = distance / speed;
		}
		
		// Personaje predicho. De este personaje solo se usará la posición (en el Seek acelerado solo se usa el target para consultar su posición).
		// 		-> Por tanto, es lo único que hay que introducir. 
		WorldObject characterPrediction = new Character();
		float finalPositionX = this.getTarget().getPosition().x + this.getTarget().getVelocity().x * prediction;
		float finalPositionY = this.getTarget().getPosition().y + this.getTarget().getVelocity().y * prediction;
		float finalPositionZ = this.getTarget().getPosition().z + this.getTarget().getVelocity().z * prediction;
		characterPrediction.setPosition(new Vector3(finalPositionX, finalPositionY, finalPositionZ));
		
		// Almacenamos el objetivo principal para poder llamar al método del padre con el 'explicitTarget'
		// y no perder el objetivo principal.
		WorldObject aux = this.getTarget();
		this.setTarget(characterPrediction);
		// Llamamos al 'getSteering' del padre
		Steering output = super.getSteering();
		// Recuperamos el objetivo principal
		this.setTarget(aux);
		
		// Devolvemos el steering calculado
		return output;
	}
}
