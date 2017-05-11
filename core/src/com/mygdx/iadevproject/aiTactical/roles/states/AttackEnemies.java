package com.mygdx.iadevproject.aiTactical.roles.states;

import java.util.Map;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;

public class AttackEnemies implements State<Character> {

	public AttackEnemies() { /* empty constructor */ }

	@Override
	public void update(Character entity) {
		/** IMPORTANTE: SOLAMENTE SE INCLUYE EL NO CHOCAR. */
		
		// Obtenemos los comportamientos para no colisionar
		Map<Float, Behaviour> behaviours = Actions.notCollide(200.0f, entity);
		// Obtenemos los comportamientos para atacar al enemigo más cercano
		Map<Float, Behaviour> patrol = Actions.attackTheNearestEnemy(entity, entity.getRole().getDamageToDone(), entity.getRole().getMaxDistanceOfAttack());
		// Juntamos ambos comportamientos
		behaviours.putAll(patrol);
				
		// Establecemos los nuevos comportamientos al personaje.
		entity.setListBehaviour(behaviours);
	}

	@Override
	public void exit(Character entity) {
		// Cuando salimos de este estado, dejamos de atacar
		Actions.leaveAttack(entity);
	}

	@Override
	public void enter(Character entity) { /* empty method */ }

	@Override
	public boolean onMessage(Character entity, Telegram telegram) { return false; }

}
