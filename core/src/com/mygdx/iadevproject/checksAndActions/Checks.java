package com.mygdx.iadevproject.checksAndActions;

import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;

public class Checks {
	
	/**
	 * Método que cumprueba si me están atacando.
	 * @param source Personaje que quiere saber si le están atacando.
	 * @return true si me están atacando, false en caso contrario.
	 */
	public static boolean doTheyAttackMe (Character source) {
		return source.getCurrentHealth() != source.getPreviousHealth();
	}
	
	
	
	/**
	 * Método que comprueba si hay enemigos en la base del personaje 'source'
	 * @param source Personaje que quiere saber si hay enemigos en su base
	 * @return true si hay personajes en su base, false en caso contrario
	 */
	public static boolean areThereEnemyInMyBase(Character source) {
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj instanceof Character) {
				Character target = (Character)obj;
				
				if (isItFromEnemyTeam(source, target)) {
					
				}
			}
		}
		return false;
	}
	
	/**
	 * Método que comprueba si el personaje 'source' se encuentra en su base.
	 * @param source Personaje que quiere saber si está en su base.
	 * @return true si está en su base, false en caso contrario.
	 */
	public static boolean amIInMyBase(Character source) {
		return false;
	}
	
	/**
	 * Método que comprueba si el personaje 'source' está en la base del enemigo.
	 * @param source Personaje que quiere saber si está en la base del enemigo.
	 * @return true si está en la base enemiga, false en caso contrario.
	 */
	public static boolean amIInEnemyBase(Character source) {
		return false;
	}
	
	/**
	 * Método que comprueba si el personaje 'source' ha recuperado su vida totalmente.
	 * @param source Personaje que quiere saber si ha recuperado su vida totalmente.
	 * @return true si hay recuperado toda su vida, false en caso contrario.
	 */
	public static boolean haveIFullyRecoveredMyHealth(Character source) {
		return source.getCurrentHealth() == source.getMaxHealth();
	}
	
	/**
	 * Método que comprueba si el personaje 'source' ha muerto.
	 * @param source Personaje que quiere saber si ha muerto.
	 * @return true si ha muerto, false en caso contrario.
	 */
	public static boolean haveIDead(Character source) {
		return source.getCurrentHealth() <= 0;
	}
	
	/**
	 * Método que comprueba si el personaje 'target' es del equipo enemigo al personaje 'source'.
	 * @param source Personaje que quiere saber si 'target' es del equipo contrario.
	 * @param target Personaje del que se quiere saber si es del equipo contrario.
	 * @return true si es del equipo contrario, false en caso contrario.
	 */
	public static boolean isItFromEnemyTeam(Character source, Character target) {
		return source.getTeam() == target.getTeam() && target.getTeam() != Team.NEUTRAL;
	}
	
	/**
	 * Método que comprueba si los dos personajes son del mismo equipo.
	 * @param source Personaje que realiza la pregunta.
	 * @param target Personaje del que se quiere saber si es del equipo de 'source'.
	 * @return true si son del mismo equipo, false en caso contrario.
	 */
	public static boolean isItFromMyTeam(Character source, Character target) {
		return source.getTeam() == target.getTeam();
	}

}
