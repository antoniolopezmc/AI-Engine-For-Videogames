package com.mygdx.iadevproject.aiTactical.roles.states;

import java.util.Map;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiTactical.roles.TacticalRole;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;

public class CureMe implements State<Character> {

	public CureMe() { /* empty constructor */}

	@Override
	public void update(Character entity) {
		/** IMPORTANTE: SOLAMENTE SE INCLUYE EL NO CHOCAR. */
		
		// Obtenemos los comportamientos para no chocar
		Map<Float, Behaviour> behaviours = Actions.notCollide(200.0f, entity);
		// Obtenemos los comporamientos para curarse
		Map<Float, Behaviour> cure = Actions.cure(entity, TacticalRole.health_cure);
		// Obtenemos los comportamientos para ir a la fuente.
		Map<Float, Behaviour> goToManantial = Actions.goToCure(30.0f, entity, 20.0f);
		// Juntamos todos los comportamientos.
		behaviours.putAll(cure);
		behaviours.putAll(goToManantial);
		
		// Establecemos los nuevos comportamientos al personaje.
		entity.setListBehaviour(behaviours);
	}

	@Override
	public void enter(Character entity) { /* empty method */ }

	@Override
	public void exit(Character entity) {
		// Cuando salimos de este estado, dejamos de curarnos
		Actions.leaveCure(entity);
	}

	@Override
	public boolean onMessage(Character entity, Telegram telegram) { return false; }

}
