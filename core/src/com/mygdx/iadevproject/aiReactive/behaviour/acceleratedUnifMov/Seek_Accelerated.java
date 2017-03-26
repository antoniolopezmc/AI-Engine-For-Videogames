package com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class Seek_Accelerated implements Behaviour {

	// Versión a utilizar.
	public static int SEEK_ACCELERATED_MILLINGTON = 0;
	public static int SEEK_ACCELERATED_REYNOLDS = 1;
	
	private Character source;
	private WorldObject target;
	private float maxAcceleration;
	private int mode;
	
	public Seek_Accelerated (Character source, WorldObject target, float maxAcceleration) {
		this.source = source;
		this.target = target;
		this.maxAcceleration = maxAcceleration;
		this.mode = Seek_Accelerated.SEEK_ACCELERATED_MILLINGTON; // Por defecto, se usa MILLINGTON.
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

	public float getMaxAcceleration() {
		return maxAcceleration;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}
	
	public int getMode() {
		return this.mode;
	}
	
	public void setMode(int mode) {
		// Si introducimos un valor no permitido, se establece el modo por defecto.
		if ((mode != Seek_Accelerated.SEEK_ACCELERATED_MILLINGTON) 
				&& (mode != Seek_Accelerated.SEEK_ACCELERATED_REYNOLDS)) {
			this.mode = Seek_Accelerated.SEEK_ACCELERATED_MILLINGTON;
		} else {
			this.mode = mode;
		}
	}

	@Override
	public Steering getSteering() {
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
				
		// Calculamos el atributo 'lineal'.
		Vector3 copy = new Vector3(this.target.getPosition());
		Vector3 finalLineal = copy.sub(this.source.getPosition());
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//TODO IMPORTANTE!!!!!!!! CONSULTAR CON ANTONIO Y COMENTARLO!!!!!
		if (finalLineal.len() < 1) { 
			output.setLineal(new Vector3(0,0,0));
			output.setAngular(0.0f);
			return output;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		finalLineal.nor();
		
		// Consideramos las dos versiones del Seek acelerado. Por defecto, se utiliza la de Millington.
		// --> Versión de Millington: el personaje no se para nunca
		// --> Versión de Reynolds: el personaje se para
		if (mode == Seek_Accelerated.SEEK_ACCELERATED_REYNOLDS) { 
			// Versión de Craig W. Reynolds
			finalLineal.x = finalLineal.x * this.maxAcceleration;
			finalLineal.y = finalLineal.y * this.maxAcceleration;
			finalLineal.z = finalLineal.z * this.maxAcceleration;
			output.setLineal(finalLineal.sub(source.getVelocity()));
			
		} else if (mode == Seek_Accelerated.SEEK_ACCELERATED_MILLINGTON) {
			// Versión de Ian Millington
			finalLineal.x = finalLineal.x * this.maxAcceleration;
			finalLineal.y = finalLineal.y * this.maxAcceleration;
			finalLineal.z = finalLineal.z * this.maxAcceleration; 
			output.setLineal(finalLineal);		
		}
		
		// La aceleración angular del steering se pone a 0.
		output.setAngular(0);
				
		return output;
	}

}
