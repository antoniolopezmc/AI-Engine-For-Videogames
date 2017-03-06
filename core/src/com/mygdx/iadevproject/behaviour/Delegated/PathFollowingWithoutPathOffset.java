package com.mygdx.iadevproject.behaviour.Delegated;

import java.util.*;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.Behaviour;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov.Seek_NoAccelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.steering.Steering;

//TODO IMPORTANTE -> PROBAR.

// TODO Esto funciona. Los he probado con el Seek NO ACELERADO y funciona. Con el Seek acelerado el personaje se va a tomar por culo (porque el cambio de velocidad no es inmediato).
// TODO Preguntárselo a Luis Daniel.
public class PathFollowingWithoutPathOffset extends Seek_Accelerated implements Behaviour {

	public static int MODO_PARAR_AL_FINAL = 0; // Cuando el personaje llega al último punto, terminamos.
	public static int MODO_IDA_Y_VUELTA = 1; // El personaje va y viene infinitamente.
	
	private List<Vector3> pointsList; // Lista de puntos aún no visitados.
	// -> Esta segunda lista es útil para cuando haya que recordar los puntos ya visitados. (Por ej. en MODO_IDA_Y_VUELTA)
	private List<Vector3> removedPointsList; // Lista de puntos ya visitados.
	private float radius;
	private int modo;
	
	public float getRadius() {
		return radius;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getModo() {
		return modo;
	}

	public void setModo(int modo) {
		this.modo = modo;
	}

	public PathFollowingWithoutPathOffset(Character source, float maxAcceleration, List<Vector3> pointsList, float radius, int modo) {
		super(source, null, maxAcceleration);
		this.pointsList = new LinkedList<Vector3>(pointsList); // IMPORTANTE -> Como la lista se va a modificar, almacenamos una copia.
		this.radius = radius;
		this.modo = modo;
		this.removedPointsList = new LinkedList<Vector3>();
	}

	@Override
	public Steering getSteering() {
		if (!this.pointsList.isEmpty()) {
			Vector3 nextTarget = this.pointsList.get(0); // Consultamos el primer elemento de la lista (el punto donde tenemos que ir).
			
			// Calculamos la dirección (el vector) y distancia entre la fuente y el siguiente objetivo (el punto donde tenemos que ir).
			Vector3 direction = new Vector3(nextTarget);
			direction = direction.sub(super.getSource().getPosition());
			float distance = direction.len(); // Módulo del vector 'direction'.
			
			// Si la distancia hasta el siguiente objetivo es menor que el radio, eso quiere decir que ya hemos llegado al punto deseado.
			// 		(o al menos nos hemos acercado la distancia necesaria).
			if (distance < this.radius) {
				// IMPORTANTE -> En esta segunda lista, los puntos se van añadiendo en orden INVERSO a como estaban en la lista original.
				//		Esto nos permite tener al final justo el camino inverso.
				this.removedPointsList.add(0, nextTarget); // Añadimos el punto alcanzado a la lista de puntos eliminados.
				this.pointsList.remove(0); // Eliminamos ese mismo punto de la lista de puntos activos.
				
				// Si hemos llegado al final del camino y estamos en el modo ido y vuelta, hacemos el camino inverso.
				if ((modo == MODO_IDA_Y_VUELTA) && (this.pointsList.isEmpty())) {
					this.pointsList = new LinkedList<Vector3>(this.removedPointsList);
					this.removedPointsList.clear(); // Limpiamos la lista de puntos eliminados.
				}
			}
			
			// Si ya hemos llegado al final, al eliminar el primer elemento, la lista puede quedarse vacía.
			//	Por tanto, debemos volver a comprobarlo.
			if (!this.pointsList.isEmpty()) {
				// ---> Cuando ya sabemos exactamente cual debe ser el siguiente objetivo, preparamos y devolvemos el Steergin adecuado.
				// Siguiente objetivo -> Creamos un personaje ficticio. De este personaje solo se usará la posición (en el Seek acelerado solo se usa el target para consultar su posición).
				// 		-> Por tanto, es lo único que hay que introducir.
				nextTarget = this.pointsList.get(0);
				WorldObject fakeCharacter = new Character();
				float finalPositionX = nextTarget.x;
				float finalPositionY = nextTarget.y;
				float finalPositionZ = nextTarget.z;
				fakeCharacter.setPosition(new Vector3(finalPositionX, finalPositionY, finalPositionZ));
				
				// Establecemos como objectivo el 'fakeCharacter'
				super.setTarget(fakeCharacter);
				return super.getSteering();
			}
		}
		
		// Si la lista está vacía, es que ya no quedan más puntos a donde ir. Por tanto, no hacemos nada.
		//		Esto es equivalente a obtener el Steering entre el pensonaje y él mismo.
		super.setTarget(super.getSource());
		return super.getSteering();
		
	}

}
