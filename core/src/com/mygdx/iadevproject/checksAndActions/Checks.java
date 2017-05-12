package com.mygdx.iadevproject.checksAndActions;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.model.formation.Formation;

public class Checks {
	
	// Distancia por defecto a la que nosotros consideramos que objeto está "cerca" de otro. 
	private static final float NEAR = 300;
	// Cantidad por defecto de salud que nosotros consideramos como "poca" salud.
	// 		Depende de la salud por defecto del personaje.
	private static final float LITTLE_HEALTH = Character.DEFAULT_HEALTH * 0.2f;
	
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
	public static boolean areThereEnemiesLessThanDistance (Character source, float distance) {
		for (WorldObject obj : IADeVProject.worldObjects) {
			// Consideramos los personajes que no sean formaciones
			if (obj instanceof Character && !(obj instanceof Formation)) {
				Character target = (Character)obj;
				
				// Comprobamos también que el personaje no esté muerto.
				if (isItFromEnemyTeam(source, target) && !Checks.haveIDead(target)) {
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
		return areThereEnemiesLessThanDistance(source, NEAR);
	}
	
	/**
	 * Método que comprueba si hay alidos a menos de una distancia pasada como parámetro.
	 * @param source Personaje que cumprueba si hay alidos cerca.
	 * @param distance Distancia máxima a la que se considera que un objeto está cerca de otro.
	 * @return true si hay alidos cerca, false en caso contrario.
	 */
	public static boolean areThereCompanionsLessThanDistance (Character source, float distance) {
		for (WorldObject obj : IADeVProject.worldObjects) {
			// Consideramos los personajes que no sean formaciones
			if (obj instanceof Character && !(obj instanceof Formation)) {
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
		return areThereCompanionsLessThanDistance(source, NEAR);
	}
	
	/**
	 * Método que comprueba y devuelve cuántos enemigos hay a menos de una distancia especificada.
	 * @param source Personaje que cumprueba cuántos enemigos hay cerca.
	 * @param distance Distancia máxima a la que se considera que un objeto está cerca de otro.
	 * @return la cantidad de enemigos que hay cerca.
	 */
	public static int howManyEnemiesAreThereLessThanDistance (Character source, float distance) {
		int resultado = 0;
		for (WorldObject obj : IADeVProject.worldObjects) {
			// Consideramos los personajes que no sean formaciones
			if (obj instanceof Character && !(obj instanceof Formation)) {
				Character target = (Character)obj;
				
				// Comprobamos también que el personaje no esté muerto.
				if (isItFromEnemyTeam(source, target) && !Checks.haveIDead(target)) {
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
		return howManyEnemiesAreThereLessThanDistance(source, NEAR);
	}
	
	/**
	 * Método que comprueba y devuelve cuántos enemigos hay a menos de una distancia especificada.
	 * @param source Personaje que cumprueba cuántos enemigos hay cerca.
	 * @param distance Distancia máxima a la que se considera que un objeto está cerca de otro.
	 * @return la cantidad de enemigos que hay cerca.
	 */
	public static int howManyCompanionsAreThereLessThanDistance (Character source, float distance) {
		int resultado = 0;
		for (WorldObject obj : IADeVProject.worldObjects) {
			// Consideramos los personajes que no sean formaciones
			if (obj instanceof Character && !(obj instanceof Formation)) {
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
		return howManyCompanionsAreThereLessThanDistance(source, NEAR);
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
			// Consideramos los personajes que no sean formaciones
			if (obj instanceof Character && !(obj instanceof Formation)) {
				Character target = (Character)obj;
				
				// Si el target es del equipo contrario y está en la base enemiga (está en mi base), entonces
				// devolvemos true
				if (isItFromEnemyTeam(source, target) && amIInEnemyBase(target)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Método que comprueba si el personaje 'source' se encuentra en su
	 * manantial de curación. 
	 * @param source Personajque que quiere saber si está en su manantial de curación.
	 * @return true si está en su manantial de curación, false en caso contrario.
	 */
	public static boolean amIInMyManantial(Character source) {
		// Obtenemos el manantial del personaje
		Rectangle myManantial = IADeVProject.getManantialOfTeam(source.getTeam());
		// Obtenemos la posición del personaje
		Vector2 position = new Vector2(source.getPosition().x, source.getPosition().y);
		// Devolvemos si el manantial contiene a la posición del personaje
		return myManantial.contains(position);
	}
	
	// TODO Pensar -> ¿Hay aliados en mi base? ¿Hay aliados en la base enemiga? ¿Hay enemigos en la base enemiga (en su propia base)?
	
	/**
	 * Método que comprueba si el personaje 'source' se encuentra en su base.
	 * @param source Personaje que quiere saber si está en su base.
	 * @return true si está en su base, false en caso contrario.
	 */
	public static boolean amIInMyBase(Character source) {
		// Obtenemos la base del personaje
		Rectangle myBase = IADeVProject.getBaseOfTeam(source.getTeam());
		// Obtenemos la posición del personaje
		Vector2 position = new Vector2(source.getPosition().x, source.getPosition().y);
		// Devolvemos si la base contiene a la posición del personaje
		return myBase.contains(position);
	}
	
	/**
	 * Método que comprueba si el personaje 'source' está en la base del enemigo.
	 * @param source Personaje que quiere saber si está en la base del enemigo.
	 * @return true si está en la base enemiga, false en caso contrario.
	 */
	public static boolean amIInEnemyBase(Character source) {
		// Obtenemos la base enemiga del personaje
		Rectangle enemyBase = IADeVProject.getBaseOfTeam(source.getTeam().getEnemyTeam());
		// Obtenemos la posición del personaje
		Vector2 position = new Vector2(source.getPosition().x, source.getPosition().y);
		// Devolvemos si la base contiene a la posición del personaje
		return enemyBase.contains(position);
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
	
	/**
	 * Método que comprueba si el personaje 'source' está a menos de 'distance' distancia
	 * de su base.
	 * @param source Personaje que realiza la pregunta.
	 * @param distance Distancia que se quiere consultar.
	 * @return true si está a menos de esa distancia, false en caso contrario.
	 */
	public static boolean amILessThanDistanceFromMyBase(Character source, float distance) {
		// Obtenemos la posición de la base
		Vector3 basePosition = IADeVProject.getPositionOfTeamBase(source.getTeam());
		// Obtenemos la distancia entre la base y el personaje.
		float dst = basePosition.dst(source.getPosition());
		// Comprobamos la distancia.
		return dst < distance;
	}
	
	/**
	 * Método que comprueba si el personaje 'source' está cerca de su base.
	 * @param source Personaje que realiza la pregunta.
	 * @return true si está cerca de su base, false en caso contrario.
	 */
	public static boolean amINearFromMyBase(Character source) {
		return amILessThanDistanceFromMyBase(source, NEAR);
	}
	
	/**
	 * Método que comprueba si el personaje 'source' está lejos de su base.
	 * @param source Personaje que realiza la pregunta.
	 * @return true si está lejos de su base, false en caso contrario.
	 */
	public static boolean amIFarFromMyBase(Character source) {
		return !amINearFromMyBase(source);
	}
}
