package com.mygdx.iadevproject.behaviour.group;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;

public class Cohesion extends Seek_Accelerated {

	private List<WorldObject> targets; // Lista de objetivos.
	private float threshold;
	// Los atributos 'source' y 'maxAcceleration' están en el padre.
	
	public Cohesion(Character source, List<WorldObject> targets, float maxAcceleration, float threshold) {
		super(source, null, maxAcceleration);
		this.targets = targets;
		this.threshold = threshold;
	}
	
	@Override
	public Steering getSteering() {
		int count = 0;
		Vector3 centerOfMass = new Vector3(0.0f, 0.0f, 0.0f);
		// Recorremos la lista de objetivos.
		for (WorldObject worldObject : targets) {
			// Calculamos la dirección y distancia hacia el objetivo.
			Vector3 direction = new Vector3(worldObject.getPosition());
			direction = direction.sub(super.getSource().getPosition());
			float distance = direction.len();
			// Si está dentro del radio especificado, se añade a 'centerOfMass'
			if (distance <= this.threshold) {
				centerOfMass = centerOfMass.add(new Vector3(worldObject.getPosition()));
				count++;
			}
		}
		// Si ningún objetivo estaba dentro del rango a tener en cuenta, devolvemos el steering nulo.
		if (count == 0) {
			// Creamos el 'Steering' que será devuelto. El steering nulo en este caso.
			Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
			output.setLineal(new Vector3(0.0f, 0.0f, 0.0f));
			output.setAngular(0.0f);
			return output;
		}
		// Sino, creamos un personaje ficticio y hacemos un Seek hacie él.
		// La posición del personaje ficticio será el centro de masas calculado.
		centerOfMass = new Vector3(centerOfMass.x/((float)count), centerOfMass.y/((float)count), centerOfMass.z/((float)count));
		WorldObject fakeTarget = new Obstacle();
		fakeTarget.setPosition(centerOfMass);
		super.setTarget(fakeTarget); // Establecemos el objetivo real.
		// Llamamos al padre.
		return super.getSteering();
	}
	
	public Vector3 getCenterOfMass() {
		Vector3 centerOfMass = new Vector3(0.0f, 0.0f, 0.0f);
		int count = 0;
		
		for (WorldObject worldObject : targets) {
			// Calculamos la dirección y distancia hacia el objetivo.
			Vector3 direction = new Vector3(worldObject.getPosition());
			direction = direction.sub(super.getSource().getPosition());
			float distance = direction.len();
			// Si está dentro del radio especificado, se añade a 'centerOfMass'
			if (distance <= this.threshold) {
				centerOfMass = centerOfMass.add(new Vector3(worldObject.getPosition()));
				count++;
			}
		}
		
		if (count > 0) {
			centerOfMass = new Vector3(centerOfMass.x/((float)count), centerOfMass.y/((float)count), centerOfMass.z/((float)count));
		}
		
		return centerOfMass;
	}
}
