package com.mygdx.iadevproject.model.formation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.arbitrator.Arbitrator;
import com.mygdx.iadevproject.aiReactive.arbitrator.PriorityArbitrator;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Arrive_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.others.Attack;
import com.mygdx.iadevproject.aiReactive.behaviour.others.Cure;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

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
			while (character <= this.getNumberOfCharacters()/2) { // Primero, situamos la mitad de componentes a un lado de la posición de la formación.
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

	@Override
	protected Steering getComponentFormationSteerginToApply(Character source, WorldObject fakeTarget) {
		// Tanto el árbitro como el map se crean "a pelo", porque dependerán de la formación concreta y no serán configurables desde el exterior.
		Arbitrator arbitrator = new PriorityArbitrator(1e-3f);
		Map<Float, Behaviour> map = new TreeMap<Float, Behaviour>(new Comparator<Float>() {
			@Override
			public int compare(Float o1, Float o2) {
				// Para que los ordene de mayor a menor
				if (o1 > o2) return -1;
				if (o1 == o2) return 0;
				return 1;
			}
		});
		
		// TODO MUY IMPORTANTE -> ¿Los componentes de una formación deben tener en cuenta las colisiones (sería lo primero en el map)? PENSAR. PENSAR EN EL PATHFINDING.
		
		// Primer comportamiento del map. -> Desplazamiento hacia 'fakeTarget'.
//		map.put(30.0f, new Arrive_NoAccelerated(source, fakeTarget, source.getMaxSpeed(), 5.0f, 1.0f));
		map.put(30.0f, new Arrive_Accelerated(source, fakeTarget, source.getMaxSpeed(), source.getMaxSpeed(), 5.0f, 10.0f, 1.0f));
		
		
		// Segundo comportamiento del map (con menor prioridad). -> Rotación/Cambio de orientación.
		// 	--> ESTE CAMBIO DE ORIENTACIÓN SÍ PODRÁ SER CONFIGURADO DESDE EL EXTERIOR (MEDIANTE UNA CONSTANTE).
		
		// Para algunos cambios de orientación necesitaremos las posiones de la formación y del integrante actual. 
		//		Con esas posiciones calcularemos un vector y después la orientación asociada a ese vector.
		WorldObject invented = new Obstacle();
		Vector3 center = new Vector3(this.getPosition()); // Posición de la formación.
		Vector3 extreme = new Vector3(fakeTarget.getPosition()); // Posición del integrante actual.
		
		// En función del tipo de orientación elegida...
		if (this.getComponentFormationOrientationMode() == Formation.LOOK_OUTSIDE) {
			invented.setOrientation(this.calculateOrientation(extreme.sub(center)));
			map.put(20.0f, new Align_Accelerated(source, invented, 30.0f, 20.0f, 1.0f, 10.0f, 1.0f));
		} else if (this.getComponentFormationOrientationMode() == Formation.LOOK_INSIDE) {
			invented.setOrientation(this.calculateOrientation(center.sub(extreme)));
			map.put(20.0f, new Align_Accelerated(source, invented, 30.0f, 20.0f, 1.0f, 10.0f, 1.0f));
		} else if (this.getComponentFormationOrientationMode() == Formation.SAME_ORIENTATION) {
			// Nos alineamos con la formación. Es decir, todos los integrantes tendrán la misma orientación que la formación.
			map.put(20.0f, new Align_Accelerated(source, this, 30.0f, 20.0f, 1.0f, 10.0f, 1.0f));
		}
		
		if (this.flag_attack) {
			// Este comportamiento debe estar en la segunda posición de la lista para que siempre se ejecute, excepto cuando nos
			// 	vayamos a chocar.
			map.put(40.0f, new Attack(source, target_attack, health_attack, max_distance_attack));
		}
		if (this.flag_cure) {
			// Este comportamiento debe estar en la segunda posición de la lista para que siempre se ejecute, excepto cuando nos
			// 	vayamos a chocar.
			map.put(42.0f, new Cure(source, health_cure));
		}
		
		// Devolvemos el comportamiento que nos diga el árbitro.
		return arbitrator.getSteering(map);
	}

}
