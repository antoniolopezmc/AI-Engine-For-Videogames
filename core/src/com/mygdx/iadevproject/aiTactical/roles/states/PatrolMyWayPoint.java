package com.mygdx.iadevproject.aiTactical.roles.states;

import java.util.Map;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;

public class PatrolMyWayPoint implements State<Character> {

	public PatrolMyWayPoint() { /* Empty constructor */ }
	
	@Override
	public void update(Character entity) {
		
		/** IMPORTANTE: SOLAMENTE SE INCLUYE EL NO CHOCAR. PODRÍA INCLUIRSE EL HACER LO QUE QUIERA, PERO
		 * COMO EL PATRULLAR SIEMPRE VA A ESTAR HACIENDO ALGO, NO ES NECESARIO (EN PRINCIPIO, HAY QUE PROBARLO)*/
		
		// Obtenemos los comportamientos para no colisionar
		Map<Float, Behaviour> behaviours = Actions.notCollide(200.0f, entity);
		// Obtenemos los comportamientos para patrullar el waypoint
		Map<Float, Behaviour> patrol = Actions.patrolYourWaypoint(20.0f, entity, 20.0f);
		// Juntamos ambos comportamientos
		behaviours.putAll(patrol);
		
		// Establecemos los nuevos comportamientos al personaje.
		entity.setListBehaviour(behaviours);
	}

	@Override
	public void enter(Character entity) { /* empty method */ }

	@Override
	public void exit(Character entity) { /* empty method */ }

	@Override
	public boolean onMessage(Character entity, Telegram telegram) { return false; }

}
