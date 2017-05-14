package com.mygdx.iadevproject.checksAndActions;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.formation.Formation;

public class MoralPoints {
	
	// CONDICIONES DE VICTORIA.
	// Inicialmente, ambas bases tienen una cantidad de puntos de moral por defecto.
	// - Cuando un personaje está en su base, los puntos de moral de su base se incrementan (hasta llegar al máximo).
	// - Cuando hay jugadores del equipo contrario en una base Y ESA BASE ESTÁ DESPROTEGIDA, los puntos de moral de esa base van decreciendo. 
	// 		EL VALOR DE RECUPERACIÓN DE LOS PUNTOS DE MORAL DEBE SER MAYOR QUE EL DE PÉRDIDA.
	// - Cuando los puntos de moral de una base llegan a 0, ESE EQUIPO PIERDE.
	// IMPORTANTE -> Si en la misma interación de render se producen las condiciones de victoria de ambos equipos (los 2 booleanos de IADeVProject a true), la partida quedaría en EMPATE.
	private static int moralPointsByDefault = 10000;
	private static int moralPointsSubtractedByCharacter = 5;
	private static int moralPointsAddedByCharacter = moralPointsSubtractedByCharacter + 2; // IMPORTANTE -> ESTE VALOR DEBE SER SIEMPRE MAYOR QUE EL DE ARRIBA.
	public static int moralPoints_base_LDANIEL = 1;
	public static int moralPoints_base_FJAVIER = 1;
	
	/**
	 * Resetea los puntos de moral de mi base.
	 * @param source Personaje.
	 */
	public static void resetMoralPointsOfMyBase (Character source) {
		Team myTeam = source.getTeam();
		if (myTeam == Team.LDANIEL) {
			moralPoints_base_LDANIEL = moralPointsByDefault;
		} else if (myTeam == Team.FJAVIER) {
			moralPoints_base_FJAVIER = moralPointsByDefault;
		}
	}
	
	/**
	 * Añade puntos de moral a mi base.
	 * @param source Personaje.
	 */
	public static void addMoralPointsToMyBase (Character source) {
		Team myTeam = source.getTeam();
		int moralPointToAdd = moralPointsAddedByCharacter;
		// Si el personaje es una formación, hay que contar a todos los integrantes de la misma.
		if (source instanceof Formation) {
			Formation f = (Formation) source;
			moralPointToAdd = moralPointsAddedByCharacter * f.getNumberOfCharacters();
		}
		if (myTeam == Team.LDANIEL) {
			moralPoints_base_LDANIEL = Math.min(moralPointsByDefault, moralPoints_base_LDANIEL + moralPointToAdd);
		} else if (myTeam == Team.FJAVIER) {
			moralPoints_base_FJAVIER = Math.min(moralPointsByDefault, moralPoints_base_FJAVIER + moralPointToAdd);
		}
	}
	
	/**
	 * Resta puntos de moral a la base del equipo contrario.
	 * @param source Personaje.
	 */
	public static void subtractMoralPointsToEnemyBase (Character source) {
		Team enemyTeam = source.getTeam().getEnemyTeam();
		int moralPointToSubtract = moralPointsSubtractedByCharacter;
		// Si el personaje es una formación, hay que contar a todos los integrantes de la misma.
		if (source instanceof Formation) {
			Formation f = (Formation) source;
			moralPointToSubtract = moralPointsSubtractedByCharacter * f.getNumberOfCharacters();
		}
		if (enemyTeam == Team.LDANIEL) {
			moralPoints_base_LDANIEL = Math.max(0, moralPoints_base_LDANIEL - moralPointToSubtract);
		} else if (enemyTeam == Team.FJAVIER) {
			moralPoints_base_FJAVIER = Math.max(0, moralPoints_base_FJAVIER - moralPointToSubtract);
		}
	}
	
	/**
	 * Dibuja los puntos de moral de ambas bases.
	 * @param batch Batch.
	 * @param font Font.
	 */
	public static void drawMoralPointsOfBases (SpriteBatch batch, BitmapFont font) {
		Rectangle base_LDANIEL = IADeVProject.getBaseOfTeam(Team.LDANIEL);
		Rectangle base_FJAVIER = IADeVProject.getBaseOfTeam(Team.FJAVIER);
		
		batch.begin();
		font.draw(batch, "Moral de LDANIEL: " + moralPoints_base_LDANIEL, base_LDANIEL.x, base_LDANIEL.y + base_LDANIEL.height + 15);
		font.draw(batch, "Moral de FJAVIER: " + moralPoints_base_FJAVIER, base_FJAVIER.x, base_FJAVIER.y);
	       batch.end();
	}	
}
