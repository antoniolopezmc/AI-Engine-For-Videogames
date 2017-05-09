package com.mygdx.iadevproject.checksAndActions;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;

public class Checks {
	
	// Distancia por defecto a la que nosotros consideramos que objeto está "cerca" de otro. 
	private static final float NEAR = 10;
	// Cantidad por defecto de salud que nosotros consideramos como "poca" salud.
	private static final float LITTLE_HEALTH = 20;
	
	/**
	 * Método que cumprueba si me están atacando.
	 * @param source Personaje que quiere saber si le están atacando.
	 * @return true si me están atacando, false en caso contrario.
	 */
	public static boolean doTheyAttackMe (Character source) {
		// Consideramos que a un personaje lo están atacando cuando su vida se reduce con respecto al frame anterior
		// 		(por eso hemos creado el atributo 'previousHealth').
		return source.getCurrentHealth() != source.getPreviousHealth();
	}
	
	/**
	 * Método que comprueba si hay enemigos a menos de una distancia pasada como parámetro.
	 * @param source Personaje que cumprueba si hay enemigos cerca.
	 * @param distance Distancia máxima a la que se considera que un objeto está cerca de otro.
	 * @return true si hay enemigos cerca, false en caso contrario.
	 */
	public static boolean areThereEnemiesNear (Character source, float distance) {
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj instanceof Character) {
				Character target = (Character)obj;
				
				if (isItFromEnemyTeam(source, target)) {
					// Calculamos la distancia entre ambos personajes.
					Vector3 targetPosition = new Vector3(target.getPosition());
					Vector3 sourcePosition = new Vector3(source.getPosition());
					Vector3 vDistancia = targetPosition.sub(sourcePosition);
					float distancia = vDistancia.len();
					// Si la distancia entre source y target es menor a la que pasamos como parámetro
					// 	sí hay enemigos cerca.
					if (distancia < distance) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Método que comprueba si hay enemigos a menos de una distancia por defecto.
	 * @param source Personaje que cumprueba si hay enemigos cerca.
	 * @return true si hay enemigos cerca, false en caso contrario.
	 */
	public static boolean areThereEnemiesNear (Character source) {
		return areThereEnemiesNear(source, NEAR);
	}
	
	/**
	 * Método que comprueba si hay alidos a menos de una distancia pasada como parámetro.
	 * @param source Personaje que cumprueba si hay alidos cerca.
	 * @param distance Distancia máxima a la que se considera que un objeto está cerca de otro.
	 * @return true si hay alidos cerca, false en caso contrario.
	 */
	public static boolean areThereCompanionsNear (Character source, float distance) {
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj instanceof Character) {
				Character target = (Character)obj;
				
				// --> Es muy importante comprobar si el elemento actual no soy yo mismo.
				if ((!source.equals(target)) && isItFromMyTeam(source, target)) {
					// Calculamos la distancia entre ambos personajes.
					Vector3 targetPosition = new Vector3(target.getPosition());
					Vector3 sourcePosition = new Vector3(source.getPosition());
					Vector3 vDistancia = targetPosition.sub(sourcePosition);
					float distancia = vDistancia.len();
					// Si la distancia entre source y target es menor a la que pasamos como parámetro
					// 	sí hay enemigos cerca.
					if (distancia < distance) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Método que comprueba si hay aliados a menos de una distancia por defecto.
	 * @param source Personaje que cumprueba si hay aliados cerca.
	 * @return true si hay aliados cerca, false en caso contrario.
	 */
	public static boolean areThereCompanionsNear (Character source) {
		return areThereCompanionsNear(source, NEAR);
	}
	
	/**
	 * Método que comprueba y devuelve cuántos enemigos hay a menos de una distancia especificada.
	 * @param source Personaje que cumprueba cuántos enemigos hay cerca.
	 * @param distance Distancia máxima a la que se considera que un objeto está cerca de otro.
	 * @return la cantidad de enemigos que hay cerca.
	 */
	public static int howManyEnemiesAreThereNear (Character source, float distance) {
		int resultado = 0;
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj instanceof Character) {
				Character target = (Character)obj;
				
				if (isItFromEnemyTeam(source, target)) {
					// Calculamos la distancia entre ambos personajes.
					Vector3 targetPosition = new Vector3(target.getPosition());
					Vector3 sourcePosition = new Vector3(source.getPosition());
					Vector3 vDistancia = targetPosition.sub(sourcePosition);
					float distancia = vDistancia.len();
					// Si la distancia entre source y target es menor a la que pasamos como parámetro
					// 	sí hay enemigos cerca.
					if (distancia < distance) {
						resultado += 1;
					}
				}
			}
		}
		return resultado;
	}
	
	/**
	 * Método que comprueba y devuelve cuántos enemigos hay a menos de una distancia por defecto.
	 * @param source Personaje que cumprueba cuántos enemigos hay cerca.
	 * @return la cantidad de enemigos que hay cerca.
	 */
	public static int howManyEnemiesAreThereNear (Character source) {
		return howManyEnemiesAreThereNear(source, NEAR);
	}
	
	/**
	 * Método que comprueba y devuelve cuántos enemigos hay a menos de una distancia especificada.
	 * @param source Personaje que cumprueba cuántos enemigos hay cerca.
	 * @param distance Distancia máxima a la que se considera que un objeto está cerca de otro.
	 * @return la cantidad de enemigos que hay cerca.
	 */
	public static int howManyCompanionsAreThereNear (Character source, float distance) {
		int resultado = 0;
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj instanceof Character) {
				Character target = (Character)obj;
				
				if ((!source.equals(target)) && isItFromMyTeam(source, target)) {
					// Calculamos la distancia entre ambos personajes.
					Vector3 targetPosition = new Vector3(target.getPosition());
					Vector3 sourcePosition = new Vector3(source.getPosition());
					Vector3 vDistancia = targetPosition.sub(sourcePosition);
					float distancia = vDistancia.len();
					// Si la distancia entre source y target es menor a la que pasamos como parámetro
					// 	sí hay enemigos cerca.
					if (distancia < distance) {
						resultado += 1;
					}
				}
			}
		}
		return resultado;
	}
	
	/**
	 * Método que comprueba y devuelve cuántos enemigos hay a menos de una distancia por defecto.
	 * @param source Personaje que cumprueba cuántos enemigos hay cerca.
	 * @return la cantidad de enemigos que hay cerca.
	 */
	public static int howManyCompanionsAreThereNear (Character source) {
		return howManyCompanionsAreThereNear(source, NEAR);
	}
	
	/**
	 * Método que comprueba si un personaje tiene menos que una salud por defecto.
	 * @param source Personaje que comprueba si tiene poca salud.
	 * @return true si el personaje tiene poca salud, false en caso contrario.
	 */
	public static boolean haveILittleHealth (Character source) {
		return haveILessThanThisHealth(source, LITTLE_HEALTH);
	}
	
	/**
	 * Método que comprueba si un personaje tiene menos que una salud pasada como parámetro.
	 * @param source Personaje que comprueba si tiene poca salud.
	 * @return true si el personaje tiene menos que la salud especificada, false en caso contrario.
	 */
	public static boolean haveILessThanThisHealth (Character source, float health) {
		return source.getCurrentHealth() < health;
	}
	
	/**
	 * Método que comprueba si hemos dado una orden manual a un pesonaje.
	 * @param source Personaje a comprobar.
	 * @return true si hemos dado una orden manual al personaje, false en caso contrario.
	 */
	public static boolean manualMovement (Character source) {
		// return source.getManualPosition != null
		return false;
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
					// TODO
				}
			}
		}
		return false;
	}
	
	// TODO Pensar -> ¿Hay aliados en mi base? ¿Hay aliados en la base enemiga? ¿Hay enemigos en la base enemiga (en su propia base)?
	
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
	
	// TODO -> Comprobar si estoy en fuentes de curación. CADA EQUIPO TIENE LA SUYA.
	
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
		return !amIFromNeutralTeam(source) && !amIFromNeutralTeam(target) && !isItFromMyTeam(source, target);
	}
	
	/**
	 * Método que comprueba si los dos personajes son del mismo equipo.
	 * @param source Personaje que realiza la pregunta.
	 * @param target Personaje del que se quiere saber si es del equipo de 'source'.
	 * @return true si son del mismo equipo, false en caso contrario.
	 */
	public static boolean isItFromMyTeam(Character source, Character target) {
		return !amIFromNeutralTeam(source) && !amIFromNeutralTeam(target) && source.getTeam() == target.getTeam();
	}

	/**
	 * Método que comprueba si el personaje 'source' es del equipo neutral.
	 * @param source Personaque que realiza la pregunta.
	 * @return true si es del equipo neutral, false en caso contrario.
	 */
	public static boolean amIFromNeutralTeam(Character source) {
		return source.getTeam() == Team.NEUTRAL;
	}
}
