package com.mygdx.iadevproject.model.formation;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class CircularFormation extends Formation {
	
	// Arco de separación entre los componentes de la formación.
	private float separationDistance;

	// CONSTRUCTORES.
	public CircularFormation(float maxSpeed) {
		super(maxSpeed);
	}
	
	public CircularFormation(float maxSpeed, Texture texture) {
		super(maxSpeed, texture);
	}
	
	// GETs y SETs.
	public float getSeparationDistance() {
		return separationDistance;
	}

	public void setSeparationDistance(float separationDistance) {
		this.separationDistance = separationDistance;
	}
	
	// MÉTODOS.
	@Override
	protected List<Vector3> getCharactersPosition() {
		List<Vector3> salida = new LinkedList<Vector3>();
		
		// Si hay personajes en la formación.
		if (this.getNumberOfCharacters() > 0) {
			// Calculamos el ángulo de separación entre cada uno de los personajes.
			float numberOfCharactersAsFloat = (float) this.getNumberOfCharacters();
			float theta = 360.0f / numberOfCharactersAsFloat;
			
			
			// Calculamos también el radio que debe tener la circunferencia de la formación deseada.
			float separationDistanceAsFloat = (float) this.separationDistance;
			float radious = (numberOfCharactersAsFloat * separationDistanceAsFloat) / (2.0f*(float)Math.PI); // FÓRMULA DE LA LONGITUD DE LA CIRCUNFERENCIA. AQUÍ SÍ SE PONE 2 * PI.
			
			// Ahora, teniendo en cuenta el ángulo y la distancia, calculamos la posición de cada personaje CON RESPECTO A LA FORMACIÓN.
			float angle = 0; // El primer personaje siempre formará un ángulo de 0 grados.
			Vector3 finalPosition = null;
			while (angle < 360.0f) {
				finalPosition = new Vector3(radious * ((float) Math.cos((double) Math.toRadians(angle))), radious * ((float) Math.sin((double) Math.toRadians(angle))), 0.0f);
				salida.add(new Vector3(finalPosition)); // IMPORTANTE -> AÑADIMOS UNA COPIA PARA QUE NO HAYA ALIASING.
				angle += theta;
			}
		
		}
		return salida;
	}
}
