package com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.formation.Formation;

public class IAmDead implements Node {

	@Override
	public void update(Character entity) {
		// Obtenemos la posición del manantial del personaje.
		Vector3 manantialPosition = IADeVProject.getPositionOfTeamManantial(entity.getTeam());
		// Movemos al personaje al manantial A PELO: este método funciona para formaciones también.
		Actions.moveToPosition(entity, manantialPosition);
		
		// MUY IMPORTANTE.
		// Como en un árbol de comportamiento se COMPRUEBA TODO DESDE EL PRINCIPIO (vamos recorriendo todas las ramas de izquierda a derecha hasta llegar a una que se cumple), si un
		// 	PERSONAJE COMO TAL (no formaciones) se muere (si vida llega a 0), el árbol se quedará pillado para siempre en la primera comprobación.
		// --> Para solucionar este problema, cuando un personaje muere, lo movemos a la fuente y establecemos su salud a 1.
		if ((entity instanceof Character) && (!(entity instanceof Formation))) {
			entity.setCurrentHealth(1);
		}
	}

	@Override
	public void enter(Character entity) {
		
		
	}

	@Override
	public void exit(Character entity) {
		
		
	}

}
