package com.mygdx.iadevproject.model.formation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.arbitrator.Arbitrator;
import com.mygdx.iadevproject.aiReactive.arbitrator.PriorityArbitrator;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Arrive_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.CollisionAvoidance;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.LookingWhereYouGoing;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.WallAvoidance;
import com.mygdx.iadevproject.aiReactive.behaviour.others.Attack;
import com.mygdx.iadevproject.aiReactive.behaviour.others.Cure;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

public class CircularFormation extends Formation {
	
	// Arco de separación entre los componentes de la formación.
	private float separationDistance;
	
	// CONSTRUCTORES.
	public CircularFormation(Arbitrator arbitrator, float maxSpeed) {
		super(arbitrator, maxSpeed);
	}
	
	public CircularFormation(Arbitrator arbitrator, float maxSpeed, Texture texture) {
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
	@Override
	protected List<Vector3> getCharactersPosition() {
		List<Vector3> salida = new LinkedList<Vector3>();
		
		// Si hay personajes en la formación.
		if (this.getNumberOfComponents() > 0) {
			// Calculamos el ángulo de separación entre cada uno de los personajes.
			float numberOfCharactersAsFloat = (float) this.getNumberOfComponents();
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
		
		// Comportamiento del map. -> Desplazamiento hacia 'fakeTarget'.
		map.put(30.0f, new Arrive_Accelerated(source, fakeTarget, source.getMaxSpeed(), source.getMaxSpeed(), 5.0f, 10.0f, 1.0f));
		
		// Comportamiento del map (con menor prioridad que el de arriba). -> Cuando los personajes se paren, se orientan en función del tipo de orientación elegida.
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
		
		// IMPORTANTE -> Además de todo lo anterior, los componentes de una formación no deben chocarse.
		map.put(60.0f, new WallAvoidance(source, 300.0f, IADeVProject.worldObjects, 100.0f, 20.0f, 100.0f));
		map.put(61.0f, new CollisionAvoidance(source, IADeVProject.worldObjects, 200.0f));
		
		// Devolvemos el comportamiento que nos diga el árbitro.
		return arbitrator.getSteering(map);
	}
}
