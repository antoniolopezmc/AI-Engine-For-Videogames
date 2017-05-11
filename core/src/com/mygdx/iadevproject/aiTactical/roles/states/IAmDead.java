package com.mygdx.iadevproject.aiTactical.roles.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.waypoints.Waypoints;

public class IAmDead implements State<Character> {

	public IAmDead()  { /* empty constructor */ }

	@Override
	public void update(Character entity) {
		// Obtenemos la posición del manantial del personaje.
		Vector3 manantialPosition = IADeVProject.getPositionOfTeamManantial(entity.getTeam());
		// Movemos al personaje al manantial A PELO: este método funciona para formaciones también.
		Actions.moveToPosition(entity, manantialPosition);
	}

	@Override
	public void enter(Character entity)  { 
		// Cuando entramos a este estado, liberamos el waypoint que esté asociado el personaje.
		// Aunque sea el personaje no tenga asociado ningún waypoint, no pasaría nada.
		Waypoints.freeBridgeWaypoint(entity);
	}
	
	@Override
	public void exit(Character entity) { /* empty method */ }

	@Override
	public boolean onMessage(Character entity, Telegram telegram) { return false; }

}
