package com.mygdx.iadevproject.aiTactical.roles.states;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.pathfinding.pointToPoint.PointToPoint_PathFinding;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.waypoints.Waypoints;

public class GoToMyWayPoint implements State<Character> {

	private PointToPoint_PathFinding pf; 	// Objeto pathfinding para ir hacia mi base

	public GoToMyWayPoint() { /* empty constructor */ }

	@Override
	public void update(Character entity) {
		
		/** IMPORTANTE: SOLAMENTE SE INCLUYE EL NO CHOCAR. */
		
		// Obtenemos los comportamientos para no colisionar
		Map<Float, Behaviour> behaviours = Actions.notCollide(200.0f, entity);
		// Obtenemos los comportamientos para ir a mi waypoint
		Map<Float, Behaviour> goToMyWaypoint = Actions.goTo(100.0f, entity, pf, 20.0f);
		// Juntamos ambos comportamientos
		behaviours.putAll(goToMyWaypoint);
		
		// Establecemos los nuevos comportamientos al personaje.
		entity.setListBehaviour(behaviours);
	}

	@Override
	public void enter(Character entity) { 
		// Cuando entramos a este estado, calculamos el pathfinding hacia el waypoint:
		// Obtenemos el waypoint asociado 
		//(IMPORTANTE: NO CONSULTAMOS SI LA LISTA ESTÁ VACÍA PORQUE CUANDO SE ENTRA A ESTE ESTADO YA SE HA COMPROBADO QUE TIENE WAYPOINT)
		List<Vector3> waypoints = Waypoints.getAssociatedWaypointAndNeighboring(entity);
		// Creamos el pathfinding
		pf = Actions.createPathFinding(entity, waypoints.get(0));
	}

	@Override
	public void exit(Character entity) { /* empty method */ }
	
	@Override
	public boolean onMessage(Character entity, Telegram telegram) { return false; }

}
