package com.mygdx.iadevproject.model.formation;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

// La formación en estrella es un tipo de formación en círculo.
public class StarFormation extends CircularFormation {
	// Una estrella solo se podrá formar si se dan ciertas condiciones.
	// 		- Si la cantidad de integrantes de la formación es mayor estricto que 3.
	// 		- Si la cantidad de integrantes de la formación es par.
	// Si no se dan estas condiciones, la formación será un círculo normal.

	// Desplazamiento de algunos componentes de la formación con respecto al sitio que deberían ocupar en una formación normal en círculo.
	private float armSize;
	
	// CONSTRUCTORES.
	public StarFormation(float maxSpeed) {
		super(maxSpeed);
	}
	
	public StarFormation(float maxSpeed, Texture texture) {
		super(maxSpeed, texture);
	}

	// GETs y SETs.
	public float getArmSize() {
		return armSize;
	}

	public void setArmSize(float armSize) {
		this.armSize = armSize;
	}
	
	// MÉTODOS.
	@Override
	protected List<Vector3> getCharactersPosition() {
		int numCharacters = this.getNumberOfCharacters();
		// Si se cumplen las condiciones necesarias, calculamos la formación en estrella.
		if ((numCharacters > 3) && ((numCharacters % 2) == 0)) {
			List<Vector3> salida = new LinkedList<Vector3>();
			
			// Calculamos el ángulo de separación entre cada uno de los personajes.
			float numberOfCharactersAsFloat = (float) this.getNumberOfCharacters();
			float theta = 360.0f / numberOfCharactersAsFloat;
			
			
			// Calculamos también el radio que debe tener la circunferencia de la formación deseada.
			float separationDistanceAsFloat = (float) this.getSeparationDistance();
			float radious = (numberOfCharactersAsFloat * separationDistanceAsFloat) / (2.0f*(float)Math.PI); // FÓRMULA DE LA LONGITUD DE LA CIRCUNFERENCIA. AQUÍ SÍ SE PONE 2 * PI.
			
			// Ahora, teniendo en cuenta el ángulo y la distancia, calculamos la posición de cada personaje CON RESPECTO A LA FORMACIÓN.
			float angle = 0; // El primer personaje siempre formará un ángulo de 0 grados.
			Vector3 finalPosition = null;
			int index = 0; // Este índice servirá para controlar si la posición del personaje va a ocupar una posición par o impar en la lista.
			while (angle < 360.0f) {
				if ((index % 2) != 0) { // Si es impar, incrementamos el radio con respecto al centro de la foramción.
					finalPosition = new Vector3((radious+this.armSize) * ((float) Math.cos((double) Math.toRadians(angle))), (radious+this.armSize) * ((float) Math.sin((double) Math.toRadians(angle))), 0.0f);
				} else { // Si es par, no.
					finalPosition = new Vector3(radious * ((float) Math.cos((double) Math.toRadians(angle))), radious * ((float) Math.sin((double) Math.toRadians(angle))), 0.0f);
				}
				salida.add(new Vector3(finalPosition)); // IMPORTANTE -> AÑADIMOS UNA COPIA PARA QUE NO HAYA ALIASING.
				angle += theta;
				index++; // Incrementamos el índice.
			}
			
			return salida;
		}
		// Sino, se calcula una formación normal en círculo.
		return super.getCharactersPosition();
	}
}
