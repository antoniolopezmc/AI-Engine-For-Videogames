package com.mygdx.iadevproject.checksAndActions;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Flee_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.PathFollowingWithoutPathOffset;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Wander_Delegated;
import com.mygdx.iadevproject.aiReactive.pathfinding.PathFinding;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class Actions {
	
	/**
	 * Método que refleja la acción de huir de un objetivo.
	 * @param source Personaje que quiere aplicar la acción.
	 * @param target Objetivo del que se quiere huir
	 * @param maxAcceleration Máxima aceleración a la que se quiere huir.
	 * @return El comportamiento correspondiente a la acción de huir.
	 */
	public Behaviour flee(Character source, WorldObject target, float maxAcceleration) {
		return new Flee_Accelerated(source, target, maxAcceleration);
	}
	
	/**
	 * Método que refleja la acción de ir a curarse a una posición 'position' dada como parámetro
	 * @param source Personaje que quiere aplicar la acción.
	 * @param position Posición donde se va a curar.
	 * @param maxAcceleration Máxima acceleración a la que se quiere ir
	 * @return El comportamiento correspondiente a la acción de ir a curarse.
	 */
	public Behaviour goToCure(Character source, Vector3 position, float maxAcceleration) {
		PathFinding pf = new PathFinding();
		List<Vector3> pointsList = pf.applyPathFinding(IADeVProject.MAP_OF_COSTS, IADeVProject.GRID_CELL_SIZE, PathFinding.CHEBYSHEV_DISTANCE, IADeVProject.GRID_WIDTH, IADeVProject.GRID_HEIGHT, 
				source.getPosition().x, source.getPosition().y, position.x, position.y);
		
		return new PathFollowingWithoutPathOffset(source, maxAcceleration, pointsList, 1.0f, PathFollowingWithoutPathOffset.MODO_PARAR_AL_FINAL);
	}
	
	/**
	 * Método que refleja la acción de apoyar a un aliado
	 * @param source
	 * @param target
	 * @return
	 */
	public Behaviour supportAnAlly(Character source, Character target) {
		return null;
	}
	
	/**
	 * Método que refleja la acción de patrullar la zona del personaje
	 * @param source
	 * @return
	 */
	public Behaviour patrolYourArea(Character source) {
		return null;
	}
	
	/**
	 * Método que refleja la acción de hacer cosas aleatorias. Básicamente se basa en que el 
	 * personaje se mueva de manera aleatoria.
	 * @param source
	 * @return
	 */
	public Behaviour doRandomThings(Character source) {
		return new Wander_Delegated(source, 10.0f, 10.0f, 10.0f, 30.0f, 1.0f, 100.0f, 30.0f, 30.0f, 45.0f, 10.0f);
	}
}
