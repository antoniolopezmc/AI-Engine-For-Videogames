package com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node;

import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiTactical.roles.TacticalRole;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;

public class CureMe implements Node {

	@Override
	public void update(Character entity) {
		/** IMPORTANTE: SOLAMENTE SE INCLUYE EL NO CHOCAR. */
		
		// Obtenemos los comportamientos para no chocar
		Map<Float, Behaviour> behaviours = Actions.notCollide(200.0f, entity);
		// Obtenemos los comporamientos para curarse
		Map<Float, Behaviour> cure = Actions.cure(entity, TacticalRole.health_cure);
		
		
		/*
		 * Importante: en este caso, como estamos cerca del manantial, simplemente hacemos un arrive
		 * a la fuente, sin cálculo de pathfinding
		 */
		
		// Obtenemos la posición del manantial
		Vector3 pos = IADeVProject.getPositionOfTeamManantial(entity.getTeam());
		// Creamos un objeto al que aplicar el arrive con la posición del manantial
		Obstacle manantial = new Obstacle();
		manantial.setPosition(pos);
		// Obtenemos los comportamientos para ir a la fuente:
		Map<Float, Behaviour> goToManantial = Actions.arrive(30.0f, entity, manantial, 20.0f);
		
		// Juntamos todos los comportamientos.
		behaviours.putAll(cure);
		behaviours.putAll(goToManantial);
		
		// Establecemos los nuevos comportamientos al personaje.
		entity.setListBehaviour(behaviours);
		
	}

	@Override
	public void enter(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(Character entity) {
		// Cuando salimos de este estado, dejamos de curarnos
		Actions.leaveCure(entity);	
	}

}
