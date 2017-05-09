package com.mygdx.iadevproject.aiReactive.behaviour.delegated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.aiReactive.steering.Steering;
import com.mygdx.iadevproject.aiReactive.steering.Steering_AcceleratedUnifMov;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

public class WallAvoidance extends Seek_Accelerated {

	/**
	 * Enumerado para las posiciones de los rayos del Behaviour
	 */
	public enum RayPosition { LEFT, CENTER, RIGHT }
	
	private static float INFINITY = Float.MAX_VALUE; 	// Valor de infinito
	private static float LOOKAHEAD_PERCENTAGE = 0.75f;	// Porcentaje de la longitud del rayo central
	
	private float avoidDistance;						// Distancia mínima de separación de la pared
	private float centerLookahead;						// Longitud del rayo central
	private float leftLookahead;						// Longitud del rayo lateral izquierdo
	private float rightLookahead;						// Longitud del rayo lateral derecho
	private float separationAngle;						// Ángulo de separación entre el rayo central y los laterales
	
	private List<WorldObject> targets; 					// Lista de objetivos a evitar
	private Map<RayPosition, Ray> rays;					// Rayos de colisión
	private Map<RayPosition, Float> raysLength;			// Longitud de los rayos de colisión
	
	private Vector3 intersection;						// Vector intersección
	
	/**
	 * Los dos primeros parámetros son los mismos que para el Seek_Accelerated. Este constructor establece por defecto
	 * que la longitud de los rayos de colisión laterales es igual al 75% de la longitud del rayo central.
	 * @param source
	 * @param maxAcceleration
	 * @param targets - Lista de objetivos a evitar
	 * @param avoidDistance - Distancia mínima de separación de la pared. Debe ser mayor que el radio del personaje
	 * @param separationAngle - Ángulo de separación entre el rayo central y los lateraless
	 * @param centerLookahead - Longitud del rayo de colisión central.
	 * @throws IllegalArgumentException - Si la distancia mínima de separación es menor o igual que el radio del personaje
	 */
	public WallAvoidance(Character source, float maxAcceleration, List<WorldObject> targets, float avoidDistance, float separationAngle, float centerLookahead) {
		super(source, null, maxAcceleration);
		
		if (avoidDistance <= source.getBoundingRadius()) throw new IllegalArgumentException("Avoid distance should be greater than the radius of the character.");
		
		this.intersection = new Vector3();
		this.targets = targets;
		this.avoidDistance = avoidDistance;
		this.separationAngle = separationAngle;

		this.rays = new HashMap<RayPosition, Ray>();
		this.raysLength = new HashMap<RayPosition, Float>();
		
		this.centerLookahead = centerLookahead;
		this.leftLookahead = LOOKAHEAD_PERCENTAGE*centerLookahead;
		this.rightLookahead = this.leftLookahead;
		
		inicializeRaysLength();
	}
	
	/**
	 * Constructor para establecer la longitud de los rayos laterales.
	 * @param source
	 * @param maxAcceleration
	 * @param targets
	 * @param avoidDistance
	 * @param centerLookahead
	 * @param leftLookahead - Longitud del rayo lateral izquierdo
 	 * @param rightLookahead - Longitud del rayo lateral derecho
	 */
	public WallAvoidance(Character source, float maxAcceleration, List<WorldObject> targets, float avoidDistance, float separationAngle, float centerLookahead, float leftLookahead, float rightLookahead) {
		this(source, maxAcceleration, targets, avoidDistance, separationAngle, centerLookahead);
		
		this.leftLookahead = leftLookahead;
		this.rightLookahead = rightLookahead;
		
		inicializeRaysLength();
	}
	
	/**
	 * Método para inicializar el objeto 'raysLength'
	 */
	private void inicializeRaysLength() {
		this.raysLength.put(RayPosition.LEFT, this.leftLookahead);
		this.raysLength.put(RayPosition.RIGHT, this.rightLookahead);
		this.raysLength.put(RayPosition.CENTER, this.centerLookahead);
	}
	
	public List<WorldObject> getTargets() {
		return targets;
	}

	public void setTargets(List<WorldObject> targets) {
		this.targets = targets;
	}

	public float getAvoidDistance() {
		return avoidDistance;
	}

	public void setAvoidDistance(float avoidDistance) {
		this.avoidDistance = avoidDistance;
	}

	public Map<RayPosition, Ray> getRays() {
		return rays;
	}

	public Map<RayPosition, Float> getRaysLength() {
		return raysLength;
	}

	public Vector3 getIntersection() {
		return intersection;
	}
	
	public static float getLOOKAHEAD_PERCENTAGE() {
		return LOOKAHEAD_PERCENTAGE;
	}

	public float getCenterLookahead() {
		return centerLookahead;
	}

	public float getLeftLookahead() {
		return leftLookahead;
	}

	public float getRightLookahead() {
		return rightLookahead;
	}
	
	/**
	 * Método para pintar las líneas de debug del Behaviour
	 */
	private void debug(boolean thereIsIntersection, WorldObject target, Vector3 normal) {
		if (IADeVProject.PRINT_PATH_BEHAVIOUR) {
			IADeVProject.renderer.begin(ShapeType.Line);
			IADeVProject.renderer.setColor(Color.RED);
			
			Ray ray = this.rays.get(RayPosition.CENTER);
			Vector3 endPoint = ray.getEndPoint(new Vector3(0,0,0), this.raysLength.get(RayPosition.CENTER));
			IADeVProject.renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
			
			ray = this.rays.get(RayPosition.LEFT);
			endPoint = ray.getEndPoint(new Vector3(0,0,0), this.raysLength.get(RayPosition.LEFT));
			IADeVProject.renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);

			ray = this.rays.get(RayPosition.RIGHT);
			endPoint = ray.getEndPoint(new Vector3(0,0,0), this.raysLength.get(RayPosition.RIGHT));
			IADeVProject.renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
			
			if (thereIsIntersection) {
				IADeVProject.renderer.line(intersection.x, intersection.y, normal.x, normal.y);
				IADeVProject.renderer.circle(target.getPosition().x, target.getPosition().y, target.getBoundingRadius());
			}
						
			IADeVProject.renderer.end();
		}
	}

	@Override
	public Steering getSteering() {		
		// Creamos el 'Steering' que será devuelto.
		Steering_AcceleratedUnifMov output = new Steering_AcceleratedUnifMov();
		
		// 1.- Calcular el objetivo para delegar en el Seek
		Vector3 origin = new Vector3(this.getSource().getPosition());
		Vector3 centerDirection = new Vector3(this.getSource().getVelocity());
		
		// Obtengo la orientación de la velocidad del personaje para establecer la orientación
		// del rayo central y el derecho e izquierdo
		float centerOrientation = convertToRange0_360(getOrientation(centerDirection));
		float leftOrientation = centerOrientation + this.separationAngle;
		float rightOrientation = centerOrientation - this.separationAngle;
		
		// Normalizo el vector y lo escalo a su medida
		centerDirection.nor();
		// Esto hace lo mismo que multiplicar por cada una de las componentes por separado
		centerDirection.scl(this.raysLength.get(RayPosition.CENTER));
		
		// Obtenemos el vector del lado IZQUIERDO
		Vector3 leftDirection = getVector(leftOrientation);
		// Normalizo el vector y lo escalo a su medida
		leftDirection.nor();
		leftDirection.scl(this.raysLength.get(RayPosition.LEFT));

		// Obtenemos el vector del lado DERECHO
		Vector3 rightDirection = getVector(rightOrientation);
		// Normalizo el vector y lo escalo a su medida
		rightDirection.nor();
		rightDirection.scl(this.raysLength.get(RayPosition.RIGHT));
		
		// Añadimos los rayos con sus respectivas posiciones y direcciones.
		this.rays.put(RayPosition.LEFT, new Ray(origin, leftDirection));
		this.rays.put(RayPosition.CENTER, new Ray(origin, centerDirection));		
		this.rays.put(RayPosition.RIGHT, new Ray(origin, rightDirection));	
		
		
		// Obtenemos el objetivo más cercano
		WorldObject firstTarget = getCloserTarget(origin);
		
		// Si es null, significa que no chocamos con ninguno. Por lo que no hacemos nada.
		if (firstTarget == null) {
			// Si no chocamos con nada, entonces no hacemos nada
			output.setLineal(new Vector3(0,0,0));
			output.setAngular(0.0f);
			return output;
		}
		
		// 2.- Encontrar la colisión con alguno de los rayos
		float distance, firstDistance = INFINITY, firstRayLength = INFINITY;
		Ray firstRay = null, rayVector = null;

		for (RayPosition position : this.rays.keySet()) {
			
			// Obtenemos el rayo
			rayVector = this.rays.get(position);
			
			// Comprobamos si hay intersección
			// IMPORTANTE: Consideramos que todos los personajes están rodeados por un círculo y comprobamos
			// si el rayo colisiona con ese círculo
			if (Intersector.intersectRaySphere(rayVector, firstTarget.getCenterOfMass(), firstTarget.getBoundingRadius(), intersection)) {
			
				// Si hay intersección, obtenemos la distancia entre el centro del rayo 
				// y el punto de intersección.
				distance = rayVector.origin.dst(intersection);
				
				// Comprobamos si la distancia es menor que la longitud del rayo y si
				// es menor que la menor distancia obtenida. Para quedarnos con la intersección
				// más cercana (si hay varios rayos que intersecan)
				if (distance < this.raysLength.get(position) && distance < firstDistance) {
					firstDistance 		= distance;
					firstRay 			= rayVector;
					firstRayLength 		= this.raysLength.get(position);
				}
			}
		}
		
		// Si hemos obtenido una intersección y la distancia de intersección es mejor
		// que la longitud del rayo, hacemos un Seek hacia el punto dado por el vector
		// normal en el punto de intersección escalado a la distancia de evasión 'avoidDistance'
		if (firstRay != null && firstDistance <= firstRayLength) {
			// Obtenemos la normal en el punto de intersección.
			Vector3 center = new Vector3(intersection);
			Vector3 normal = center.sub(firstTarget.getCenterOfMass());
			// Normalizamos y escalamos
			normal.nor();
			normal.scl(this.avoidDistance);
			
			// Obtenemos la posición del objetivo al que hacer el Seek
			Vector3 targetPosition = new Vector3(intersection);
			targetPosition.add(normal);
			
			// Creamos el objetivo al que vamos a hacer el Seek y le asignamos la posición anteriormente calculada
			WorldObject targetSeek = new Obstacle();
			targetSeek.setPosition(targetPosition);
			
			debug(true, firstTarget, targetPosition);
			
			// Devolvemos el resultado del 'getSteering' del padre
			this.setTarget(targetSeek);
			return super.getSteering();
		}
		
		debug(false, null, null);
		
		// Si no chocamos con nada, entonces no hacemos nada
		output.setLineal(new Vector3(0,0,0));
		output.setAngular(0.0f);
		return output;
	}
	
	/**
	 * Obtiene el objetivo más cercano a la posición 'position' de la lista de objetivos 'targets'
	 * @param position - Posición de la que queremos obtener el objetivo más cercano.
	 * @return - Objetivo más cercano.
	 */
	private WorldObject getCloserTarget(Vector3 position) {
		WorldObject obj = null;
		float minDistance = INFINITY;
		float distance;
		
		for (WorldObject target : this.targets) {
			distance = position.dst(target.getPosition());
			// Comprobamos que la distancia sea distinta de 0 para que, si en la lista
			// de 'targets' el está el propio personaje, no se tenga en cuenta como
			// objetivo más cercano.
			if (distance != 0 && distance < minDistance) {
				obj = target;
				minDistance = distance;
			}
		}
		
		return obj;
	}
	
	/**
	 * Método que apartir de la orientación 'orientation' devuelve el vector
	 * que representa esa dirección.
	 * @param orientation -  Orientación.
	 * @return - Vector que representa a esa dirección.
	 */
	private Vector3 getVector (float orientation) {
		return new Vector3((float)-Math.sin(Math.toRadians(orientation)), (float)Math.cos(Math.toRadians(orientation)), 0);
	}
	
	/**
	 * Método que obtiene la orientación de un vector.
	 * @param vector - Vector del que obtenemos la orientación
	 * @return - Orientación del vector 
	 */
	private float getOrientation (Vector3 vector) {
		return (float) Math.toDegrees(MathUtils.atan2(-vector.x, vector.y));
	}
	
	/**
	 * Método que transforma la orientación 'orientation' al rango 0-360
	 * @param orientation - orientación que queremos transformar
	 * @return - 'orientation' en el rango 0-360
	 */
	private float convertToRange0_360(float orientation) {
		float realOrientation = orientation % 360;
		
		if (realOrientation < 0) { realOrientation += 360; }
		
		return realOrientation;
	}
}
