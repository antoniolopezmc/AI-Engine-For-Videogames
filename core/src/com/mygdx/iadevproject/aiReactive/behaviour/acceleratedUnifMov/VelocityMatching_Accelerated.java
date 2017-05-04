package com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class VelocityMatching_Accelerated implements Behaviour {

	private Character source;
	private WorldObject target;
	// Máxima aceleración del personaje
	private float maxAcceleration;
	// Tiempo en alcanzar la velocidad del objetivo
	private float timeToTarget;
	
	/**
	 * Constructor.
	 * @param maxAcceleration Máxima aceleración del personaje.
	 * @param timeToTarget Tiempo en alcanzar la velocidad del objetivo.
	 */
	public VelocityMatching_Accelerated (Character source, WorldObject target, float maxAcceleration, float timeToTarget) {
		this.source = source;
		this.target = target;
		this.maxAcceleration = maxAcceleration;
		this.timeToTarget = timeToTarget;
	}

	public Character getSource() {
		return source;
	}

	public void setSource(Character source) {
		this.source = source;
	}

	public WorldObject getTarget() {
		return target;
	}

	public void setTarget(WorldObject target) {
		this.target = target;
	}

	/**
	 * Método 'get' para la aceleración máxima.
	 * @return - Aceleración máxima del personaje.
	 */
	public float getMaxAcceleration() {
		return maxAcceleration;
	}
	
	/**
	 * Método 'set' para la aceleración máxima.
	 * @param maxAcceleration - Aceleración máxima del personaje.
	 */
	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	/**
	 * Método 'get' para el tiempo de alcance de la velocidad del objetivo.
	 * @return - Tiempo de alcance de la velocidad del objetivo.
	 */
	public float getTimeToTarget() {
		return timeToTarget;
	}

	/**
	 * Método 'set' para el tiempo de alcance de la velocidad del objetivo.
	 * @param timeToTarget - Tiempo de alcance de la velocidad del objetivo.
	 */
	public void setTimeToTarget(float timeToTarget) {
		this.timeToTarget = timeToTarget;
	}

	
	/**
	 * Método para pintar las líneas de debug del Behaviour
	 */
	private void debug(Vector3 origin, Vector3 velocity) {
		if (IADeVProject.PRINT_PATH_BEHAVIOUR) {
			IADeVProject.renderer.begin(ShapeType.Line);
			IADeVProject.renderer.line(origin.x, origin.y, origin.x+velocity.x, origin.y+velocity.y);
			IADeVProject.renderer.end();
		}
	}
	
	@Override
	public Steering getSteering() {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
						
		// Calculamos el atributo 'lineal' como diferencia de las velocidades entre el personajey el objetivo.
		Vector3 copy = new Vector3(this.target.getVelocity());;
		Vector3 finalLineal = copy.sub(this.source.getVelocity());
				
		finalLineal.x = finalLineal.x / this.timeToTarget;
		finalLineal.y = finalLineal.y / this.timeToTarget;
		finalLineal.z = finalLineal.z / this.timeToTarget;
		
		// Si la aceleración es superior a la aceleración máxima, entonces normalizamos y establecemos la máxima aceleración.
		if (finalLineal.len() > this.maxAcceleration) {
			finalLineal = finalLineal.nor();
			
			finalLineal.x = finalLineal.x * this.maxAcceleration;
			finalLineal.y = finalLineal.y * this.maxAcceleration;
			finalLineal.z = finalLineal.z * this.maxAcceleration; 			
		}
		
		debug(this.source.getPosition(), this.target.getVelocity());
		
		output.setLineal(finalLineal);	
		output.setAngular(0);
		
		return output;
	}
}
