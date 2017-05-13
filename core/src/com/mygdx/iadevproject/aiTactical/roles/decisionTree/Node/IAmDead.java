package com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;

public class IAmDead implements Node {

	@Override
	public void update(Character entity) {
		// Obtenemos la posición del manantial del personaje.
		Vector3 manantialPosition = IADeVProject.getPositionOfTeamManantial(entity.getTeam());
		// Movemos al personaje al manantial A PELO: este método funciona para formaciones también.
		Actions.moveToPosition(entity, manantialPosition);
	}

	@Override
	public void enter(Character entity) {
		
		
	}

	@Override
	public void exit(Character entity) {
		
		
	}

}
