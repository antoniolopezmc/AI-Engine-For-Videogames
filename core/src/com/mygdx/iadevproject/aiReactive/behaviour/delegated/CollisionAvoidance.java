package com.mygdx.iadevproject.aiReactive.behaviour.delegated;

import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class CollisionAvoidance implements Behaviour {
	
	private ShapeRenderer renderer = null;
    
	public ShapeRenderer getRenderer() {
		return this.renderer;
	}
	
	public void setRenderer(ShapeRenderer renderer) {
		this.renderer = renderer;
	}
	
	private void debug() {
		if (IADeVProject.PRINT_PATH_BEHAVIOUR && this.renderer != null) {
			
		}
	}
	
	private final float INFINITY = Float.MAX_VALUE; // Constante que representa el número infinito
	
	private Character source;
	private List<WorldObject> targets; // Lista de objetivos a evitar
	private float maxAcceleration;	 // Máxima aceleración
	
	public CollisionAvoidance(Character source, List<WorldObject> targets, float maxAcceleration) {
		this.source = source;
		this.targets = targets;
		this.maxAcceleration = maxAcceleration;
	}

	@Override
	public Steering getSteering() {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
		
		// 1.- Encontrar el objetivo más cercano para la colisión
		
		// Inicializamos las variables
		float shortestTime = INFINITY; // Almacena el tiempo de colisión más pequeño
		
		// Almacenan el objetivo con el que evitar chocar y algunos datos que necesitaremos
		// para poder evitar el choque.
		WorldObject firstTarget = null;
		float firstMinSeparation = 0.0f;
		float firstDistance = 0.0f;
		float firstSumRadius = 0.0f;
//		Vector3 firstRelativePos = null;
//		Vector3 firstRelativeVel = null;
		
		// Recorremos los posibles objetivos 
		for (WorldObject tar : targets) {
			
			// Calculamos el tiempo de colisión
			Vector3 relativePos = new Vector3(tar.getCenterOfMass());
			relativePos.sub(source.getCenterOfMass());
			
			Vector3 relativeVel = new Vector3(tar.getVelocity());
			relativeVel.sub(source.getVelocity());
			
			float relativeSpeed = relativeVel.len();
			float timeToCollision = - ((relativePos.dot(relativeVel)) / (relativeSpeed * relativeSpeed));
			
			// Comprobamos si va a haber una colisión
			float distance = relativePos.len();
			// Calculamos la distancia más pequeña
			float minSeparation = distance - relativeSpeed * timeToCollision;
			
			// Comprobamos si hay colisión entre el personaje y el objetivo. Hay colisión si la separación
			// mínima es menor que la suma de los radios de los personajes.
			float sumRadius = (source.getBoundingRadius() + tar.getBoundingRadius());
			if (minSeparation <= sumRadius) {
				
				// Si hay colisión, tenemos que comprobar si la colisión se produce antes que las anteriores
				if (timeToCollision > 0 && timeToCollision < shortestTime) {
					
					// Si se produce antes que los ya vistos, entonces actualizamos los valores para utilizarlos después en el cálculo
					// del steering:
					shortestTime 		= timeToCollision;
					firstTarget 		= tar;
					firstMinSeparation 	= minSeparation;
					firstDistance 		= distance;
//					firstRelativePos 	= relativePos;
//					firstRelativeVel 	= relativeVel;
					firstSumRadius 		= sumRadius;
				}
			}
		}
		
		if (firstTarget != null) {
			// Comprobamos si chocaremos exactamente o estamos ya chocando, para calcular el steering 
			// sobre la posición actual o sobre la posición futura:
			if (firstMinSeparation <= 0 || firstDistance <= firstSumRadius) {
				return (new Evade(source, firstTarget, this.maxAcceleration, shortestTime)).getSteering();
			}
		}
		
		// Si no chocamos con nada, entonces no hacemos nada
		output.setLineal(new Vector3(0,0,0));
		output.setAngular(0.0f);
		return output;
	}
}
