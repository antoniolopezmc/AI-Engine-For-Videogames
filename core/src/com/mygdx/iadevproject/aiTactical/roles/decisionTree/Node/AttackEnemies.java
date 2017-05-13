package com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node;

import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;

public class AttackEnemies implements Node {

	@Override
	public void update(Character entity) {
		/** IMPORTANTE: SOLAMENTE SE INCLUYE EL NO CHOCAR. 
		 * CUANDO ATACAMOS, MIRAMOS AL OBJETIVO */
		
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
		
		/**
		 * Importante: en este caso, como estamos cerca del personaje, simplemente hacemos un arrive
		 * a la posición calculada, sin cálculo de pathfinding
		 */
		// Creamos un objeto al que aplicar el arrive con la posición calculada
		Obstacle enemy = new Obstacle();
		enemy.setPosition(targetPos);
		
		// Obtenemos los comportamientos para ir hacia el target
		Map<Float, Behaviour> goToTarget = Actions.arrive(150.0f, entity, enemy, 30.0f);
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
		
	}

	@Override
	public void enter(Character entity) {
		
		
	}

	@Override
	public void exit(Character entity) {
		// Cuando salimos de este estado, dejamos de atacar
		Actions.leaveAttack(entity);
	}

}
