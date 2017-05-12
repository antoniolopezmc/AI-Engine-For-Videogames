package com.mygdx.iadevproject.aiTactical.roles.states;

import java.util.Map;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;

public class GoToMyWayPoint implements State<Character> {

	public GoToMyWayPoint() { /* empty constructor */ }

	@Override
	public void update(Character entity) {
		
		/** IMPORTANTE: SOLAMENTE SE INCLUYE EL NO CHOCAR. */
		
		// Obtenemos los comportamientos para no colisionar
		Map<Float, Behaviour> behaviours = Actions.notCollide(200.0f, entity);
		// Obtenemos los comportamientos para ir a mi waypoint
		Map<Float, Behaviour> goToMyWaypoint = Actions.goToMyWayPoint(20.0f, entity, 20.0f);
		// Juntamos ambos comportamientos
		behaviours.putAll(goToMyWaypoint);
		
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
