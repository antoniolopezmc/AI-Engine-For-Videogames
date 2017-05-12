package com.mygdx.iadevproject.aiTactical.roles.states;

import java.util.Map;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
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
		
		// Obtenemos el enemigo más cercano
		Character target = Actions.getTheNearestEnemy(entity);
		
		if (target == null) return; // Si es null, no hay personaje al que atacar.
		
		// Obtenemos la posición del target
		Vector3 targetPos = new Vector3(target.getPosition());
		// Escalamos para ir hacia donde está el target + distancia de ataque - 5 para que no esté en el límite
		targetPos.add(entity.getRole().getMaxDistanceOfAttack()-50);
		
		IADeVProject.renderer.begin(ShapeType.Filled);
		IADeVProject.renderer.circle(targetPos.x, targetPos.y, 5.0f);
		IADeVProject.renderer.end();
		
		// Obtenemos los comportamientos para ir hacia el target
		Map<Float, Behaviour> goToTarget = Actions.goTo(150.0f, entity, targetPos, 10.0f);
		
		// Obtenemos los comportamientos para atacar al enemigo más cercano
		Map<Float, Behaviour> attack = Actions.attack(entity, target, entity.getRole().getDamageToDone(), entity.getRole().getMaxDistanceOfAttack());
		
		// Juntamos todos los comportamientos
		behaviours.putAll(goToTarget);
		behaviours.putAll(attack);
				
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
