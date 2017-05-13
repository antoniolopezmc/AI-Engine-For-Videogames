package com.mygdx.iadevproject.aiTactical.roles.states;

import java.util.Map;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
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
		/** IMPORTANTE: SOLAMENTE SE INCLUYE EL NO CHOCAR. 
		 * CUANDO ATACAMOS, MIRAMOS AL OBJETIVO */
		
		System.out.println("Entra Attack");
		// Obtenemos los comportamientos para no colisionar
		Map<Float, Behaviour> behaviours = Actions.notCollide(200.0f, entity);
		
		// Obtenemos el enemigo más cercano
		Character target = Actions.getTheNearestEnemy(entity);
		if (target == null) return; // Si es null, no hay personaje al que atacar.
		
		// Obtenemos la posición del target. La obtenemos de la siguiente manera:
		Vector3 targetPos = new Vector3(target.getPosition());
		Vector3 sourcePos = new Vector3(entity.getPosition());
		// Primero calculamos la dirección entre ambos personajes haciendo source-target
		Vector3 direction = sourcePos.sub(targetPos);
		// Calculamos la distancia a la que se va a situar el personaje: distancia máxima de ataque - 10 
		// (para que se vaya a una distancia lejos del objetivo pero que no sea límite a su ataque.
		// De esta manera, los arqueros podrán atacar desde lejos.
		float distance = entity.getRole().getMaxDistanceOfAttack()-10;
		// Obtenemos la posicion como el vector director anterior normalizado, escalado a la distancia y movido a la posición del target.
		targetPos = direction.nor().scl(distance).add(targetPos);
		
		IADeVProject.renderer.begin(ShapeType.Filled);
		IADeVProject.renderer.setColor(Color.BLACK);
		IADeVProject.renderer.line(target.getPosition(), targetPos);
		IADeVProject.renderer.circle(targetPos.x, targetPos.y, 2.0f);
		IADeVProject.renderer.end();
		
		// Obtenemos los comportamientos para ir hacia el target
		System.out.println("Entra al goto");
		Map<Float, Behaviour> goToTarget = Actions.goTo(150.0f, entity, targetPos, 30.0f);
		System.out.println("Sale del goto");
		// Obtenemos los comportamientos para atacar al enemigo más cercano
		Map<Float, Behaviour> attack = Actions.attack(entity, target, entity.getRole().getDamageToDone(), entity.getRole().getMaxDistanceOfAttack());
		// Obtenemos los comportamientos para mirar al objetivo 
		Map<Float, Behaviour> faceToTarget = Actions.faceToTarget(200.0f, entity, target);
		
		// Juntamos todos los comportamientos
		behaviours.putAll(goToTarget);
		behaviours.putAll(attack);
		behaviours.putAll(faceToTarget);
		
		// Establecemos los nuevos comportamientos al personaje.
		entity.setListBehaviour(behaviours);
		System.out.println("Sale del Attack");
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
