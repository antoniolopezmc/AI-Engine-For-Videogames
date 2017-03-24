package com.mygdx.iadevproject.model.formation;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.arbitrator.Arbitrator;
import com.mygdx.iadevproject.model.Character;

public class LineFormation extends Formation {
	
	// Separación lineal entre los componentes de la formación.
	private float separationDistance;

	// CONSTRUCTORES.
	public LineFormation(Arbitrator arbitrator) {
		super(arbitrator);
	}
	
	public LineFormation(Arbitrator arbitrator, float maxSpeed) {
		super(arbitrator, maxSpeed);
	}
	
	public LineFormation(Arbitrator arbitrator, float maxSpeed, Texture texture) {
		super(arbitrator, maxSpeed, texture);
	}
	
	// GETs y SETs.
	public float getSeparationDistance() {
		return separationDistance;
	}

	public void setSeparationDistance(float separationDistance) {
		this.separationDistance = separationDistance;
	}
	
	// MÉTODOS.
	// Importante -> Hay que tener en cuenta que el ángulo de orientación del personaje no coincide con el ángulo en las formaciones.
	private float getStandarFormationAngle(float formationAngle) {
		
		// TODO IMPORTANTE
		// Si la formación cambia bruscamente de orientación (giro de 180º), esto revienta. El ángulo de la formación seguirá siendo el mismo, pero los personajes
		// 		ocuparán posiciones opuestas. SERÍA MEJOR REDUCIR LOS ÁNGULOS HASTA 180. VER MÁS ADELANTE.
		// Para observar esto, hay que poner un Seek no acelerado como comportamiento de la formación.
		if ((formationAngle >= 247.5f) && (formationAngle < 292.5f)) {
			return 0.0f;
		} else if ((formationAngle >= 292.5f) && (formationAngle < 337.5f)) {
			return 45.0f;
		} else if (((formationAngle >= 337.5f) && (formationAngle < 360.0f)) || ((formationAngle >= 0.0f) && (formationAngle < 22.5f))) {
			return 90.0f;
		} else if ((formationAngle >= 22.5f) && (formationAngle < 67.5f)) {
			return 135.0f;
		} else if ((formationAngle >= 67.5f) && (formationAngle < 112.5f)) {
			return 180.0f;
		} else if ((formationAngle >= 112.5f) && (formationAngle < 157.5f)) {
			return 225.0f;
		} else if ((formationAngle >= 157.5f) && (formationAngle < 202.5f)) {
			return 270;
		} else {
			return 315.0f;
		}
	}
	
	@Override
	protected List<Vector3> getCharactersPosition() {
		List<Vector3> salida = new LinkedList<Vector3>();
		
		// Si hay personajes en la formación.
		if (this.getNumberOfCharacters() > 0) {
			// Almacenamos la cantidad de personajes de la formación en formato 'float'.
			float numberOfCharactersAsFloat = (float) this.getNumberOfCharacters();
			
			// IMPORTANTE -> Puesto que la posición de los componentes de la formación dependen de la orientación de la propia formación
			//		y esa orientación puede variar muy rápida y bruscamente, vamos a trabajar con orientaciones estándar.
			// Con esto conseguimos que para todo un rango de posibles orientaciones, la posición de los componentes de la formación no cambie.
			// A CADA RANGO DE ORIENTACIONES DE LA FORMACIÓN, SE LE ASOCIA UN ÁNGLO DE ORIENTACIÓN ESTÁNDAR.
			float standarFormationOrientation = this.getStandarFormationAngle(this.getOrientation());
			
			// Según la orientación anterior, ahora vamos a calcular los ángulos que deben formar los "brazos" de la formación en línea.
			float rightLineOrientation = (standarFormationOrientation - 90) % 360;
			float leftLineOrientation = (standarFormationOrientation + 90) % 360;
			
			// Ahora, calculamos las posiciones:
			salida.add(new Vector3(0.0f, 0.0f, 0.0f)); // El primer personaje siempre se situará en la posición de la formación.
			
			// Los siguientes:
			int character = 1; // Variable para contar los personajes en la formación. Nos permite saber cuántos hemos situado ya.
			Vector3 finalPosition = null;
			int desplazamiento = 1; // IMPORTANTE -> Con esta variable indicamos el desplazamiento o separación lateral con respecto al centro. (Se multiplica por 'this.separationDistance')
			while (character < this.getNumberOfCharacters()/2) { // Primero, situamos la mitad de componentes a un lado de la posición de la formación.
				finalPosition = new Vector3((this.separationDistance * desplazamiento) * ((float) Math.cos((double) Math.toRadians(rightLineOrientation))), (this.separationDistance * desplazamiento) * ((float) Math.sin((double) Math.toRadians(rightLineOrientation))), 0.0f);
				salida.add(new Vector3(finalPosition)); // IMPORTANTE -> AÑADIMOS UNA COPIA PARA QUE NO HAYA ALIASING.
				character++;
				desplazamiento++;
			}
			desplazamiento = 1; // Al pasar al otro lado, comenzamos desde 1.
			while(character < this.getNumberOfCharacters()) { // Despés, situamos el resto de componentes al otro lado de la posición de la formación.
				finalPosition = new Vector3((this.separationDistance * desplazamiento) * ((float) Math.cos((double) Math.toRadians(leftLineOrientation))), (this.separationDistance * desplazamiento) * ((float) Math.sin((double) Math.toRadians(leftLineOrientation))), 0.0f);
				salida.add(new Vector3(finalPosition)); // IMPORTANTE -> AÑADIMOS UNA COPIA PARA QUE NO HAYA ALIASING.
				character++;
				desplazamiento++;
			}
		}
		return salida;		
	}

	

}
