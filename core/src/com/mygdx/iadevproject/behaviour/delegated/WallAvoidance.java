package com.mygdx.iadevproject.behaviour.delegated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.iadevproject.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.steering.Steering;
import com.mygdx.iadevproject.steering.Steering_AcceleratedUnifMov;

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
	
	private List<WorldObject> targets; 					// Lista de objetivos a evitar
	private Map<RayPosition, Ray> rays;					// Rayos de colisión
	private Map<RayPosition, Float> raysLength;			// Longitud de los rayos de colisión
	
	private Vector3 intersection;	// Vector intersección
	
	/**
	 * Los dos primeros parámetros son los mismos que para el Seek_Accelerated. Este constructor establece por defecto
	 * que la longitud de los rayos de colisión laterales es igual al 75% de la longitud del rayo central.
	 * @param source
	 * @param maxAcceleration
	 * @param targets - Lista de objetivos a evitar
	 * @param avoidDistance - Distancia mínima de separación de la pared. Debe ser mayor que el radio del personaje
	 * @param centerLookahead - Longitud del rayo de colisión central.
	 * @throws IllegalArgumentException - Si la distancia mínima de separación es menor o igual que el radio del personaje
	 */
	public WallAvoidance(Character source, float maxAcceleration, List<WorldObject> targets, float avoidDistance, float centerLookahead) {
		super(source, null, maxAcceleration);
		
		if (avoidDistance <= source.getBoundingRadius()) throw new IllegalArgumentException("Avoid distance should be greater than the radius of the character.");
		
		this.intersection = new Vector3();
		this.targets = targets;
		this.avoidDistance = avoidDistance;

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
	public WallAvoidance(Character source, float maxAcceleration, List<WorldObject> targets, float avoidDistance, float centerLookahead, float leftLookahead, float rightLookahead) {
		this(source, maxAcceleration, targets, avoidDistance, centerLookahead);
		
		this.leftLookahead = leftLookahead;
		this.rightLookahead = rightLookahead;
		
		inicializeRaysLength();
	}
	
	/**
	 * Método para inicializar el objeto 'raysLength'
	 */
	private void inicializeRaysLength() {
		this.raysLength.put(RayPosition.CENTER, this.centerLookahead);
		this.raysLength.put(RayPosition.LEFT, this.leftLookahead);
		this.raysLength.put(RayPosition.RIGHT, this.rightLookahead);
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
		float leftOrientation = centerOrientation + 15;
		float rightOrientation = centerOrientation - 15;
		
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
		
		// 2.- Encontrar la colisión con alguno de los targets
		BoundingBox boundingBox; 						// BoundingBox para la comprobacion de la intersección
		Vector3 minimum, maximum; 						// Mínimo y máximo para el boundingBox
		Rectangle boundingRectangle;					// BoundingRectangle de cada target para obtener el boundingBox
		
		WorldObject firstTarget = null;
		float distance, firstDistance = INFINITY, firstRayLength = INFINITY;
		Ray firstRay = null, rayVector = null;
		
		for (WorldObject target : targets) {
			
			for (RayPosition position : this.rays.keySet()) {
				
				// Crear el BoundingBox para el cálculo de la intersección.
				boundingRectangle = target.getBoundingRectangle();
				minimum = new Vector3(boundingRectangle.x - boundingRectangle.height/2, boundingRectangle.y - boundingRectangle.width/2, 0.0f);
				maximum = new Vector3(minimum.x + boundingRectangle.height, minimum.y + boundingRectangle.width, 0.0f);
				boundingBox = new BoundingBox(minimum, maximum);
				
				// Obtenemos el rayo
				rayVector = this.rays.get(position);
				
				// Comprobamos si hay intersección
				if (Intersector.intersectRayBounds(rayVector, boundingBox, intersection)) {
				
					// Si hay intersección, obtenemos la distancia entre el centro del rayo 
					// y el punto de intersección.
					distance = rayVector.origin.dst(intersection);
					
					// Comprobamos si la distancia es menor que la longitud del rayo y si
					// es menor que la menor distancia obtenida. Para quedarnos con la intersección
					// más cercana (si hay varios rayos que intersecan)
					if (distance < this.raysLength.get(position) && distance < firstDistance) {
						firstDistance 		= distance;
						firstTarget 		= target;
						firstRay 			= rayVector;
						firstRayLength 		= this.raysLength.get(position);
					}
				}
			}
		}
		
		// Si hemos obtenido una intersección y la distancia de intersección es mejor
		// que la longitud del rayo, hacemos un evade
		if (firstRay != null && firstDistance <= firstRayLength) {
			return (new Evade(this.getSource(), firstTarget, this.getMaxAcceleration(), 1.0f)).getSteering();			
		
		}
		
		// Si no chocamos con nada, entonces no hacemos nada
		output.setLineal(new Vector3(0,0,0));
		output.setAngular(0.0f);
		return output;
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
		// IMPORTANTE -> Para establecer la orientación, se llama al método del padre. El de 'this' está sobreescrito para evitar confusiones con los nombres.
		float realOrientation = orientation % 360;
		
		if (realOrientation < 0) { realOrientation += 360; }
		
		return realOrientation;
	}
}
