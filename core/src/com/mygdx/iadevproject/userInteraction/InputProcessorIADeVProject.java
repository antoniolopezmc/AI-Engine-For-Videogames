package com.mygdx.iadevproject.userInteraction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.checksAndActions.Checks;
import com.mygdx.iadevproject.model.Character;

/**
 * Clase que implementa el InputProcessor del projecto.
 */
public class InputProcessorIADeVProject implements InputProcessor {
	private boolean keyPressed = false;		// Indica que se ha pulsado una tecla
	private int lastKey;					// Última tecla pulsada
	// Posición que se ha clickado, posición del ancla de la formación, posición al que se va hacer el pathfinding
	// Son tres posiciones distintas porque cada una se utiliza para cada estado de la máquina.
	private Vector3 touchPos, anchorPos, pathFindingPos;
	private Character target; 				// Objetivo al que se realiza la acción (se tiene como variable global porque se tiene que seleccionar antes de elegir la acción)
	
	/**
	 * Enumerado que refleja el estado en el que se está procesando las acciones del usuario
	 */
	private enum UserState { 
		NO_SELECTED_CHARACTERS, 	// Refleja el estado de que el usuario no ha seleccionado ningún personaje (estado inicial)
		JUST_SELECTED_CHARACTERS, 	// Refleja el estado de que el usuario acaba de seleccionar personajes
		ACCELERATED,				// El usuario quiere hacer un comportamiento acelerado
		DELEGATED,					// El usuario quiere hacer un comportamiento delegado
		PATHFINDING,				// El usuario quiere realizar un pathfinding
		GROUP,						// El usuario quiere hacer un comportamiento de grupo
		OTHERS,						// El usuario quiere hacer otros comportamientos
		MAKE_FORMATION				// El usuario quiere hacer una formación.
	};
	
	// Estado de la máquina de estados. Por defecto: NO_SELECTED_CHARACTERS
	private UserState state = UserState.NO_SELECTED_CHARACTERS;
	
	
	@Override
	public boolean keyDown(int keycode) {
		
		keyPressed = true;
		lastKey = keycode;
		
		switch (keycode) {
		case Input.Keys.LEFT: 				// Movimiento hacia la izquierda
			IADeVProject.camera.translate(-5, 0, 0);
			break;
		case Input.Keys.RIGHT:				// Movimiento hacia la derecha
			IADeVProject.camera.translate(5, 0, 0);
			break;
		case Input.Keys.DOWN:				// Movimiento hacia abajo
			IADeVProject.camera.translate(0, -5, 0);
			break;
		case Input.Keys.UP:					// Movimiento hacia arriba
			IADeVProject.camera.translate(0, 5, 0);
			break;
		case Input.Keys.A:					// Alejar la cámara
			IADeVProject.camera.zoom += 0.02;	
			break;
		case Input.Keys.Q:					// Acercar la cámara
			IADeVProject.camera.zoom -= 0.02;
			break;
		case Input.Keys.P:					// Pausar el juego
			IADeVProject.paused = true;		
			break;
		case Input.Keys.O:					// Reanudar el juego
			IADeVProject.paused = false;	
			break;
		case Input.Keys.I:					// Mostrar mapa de influencia encima del mapa
			IADeVProject.showInfluenceMap = true;
			break;
		case Input.Keys.U:					// Ocultar el mapa
			IADeVProject.showInfluenceMap = false;
			break;
		case Input.Keys.X:					// Deshabilitar que haya ganador
			IADeVProject.canBeThereWinner = false;
			// Cuando ha habido un ganador, la variable 'paused' se pone a TRUE, para que los roles de los personajes no se actualicen y se pare
			// el juego. De esta manera, si el usuario quiere que no haya ganador y que siga el juego, tendremos que poner esta variable a FALSE para
			// que los roles de los personajes sigan actualizándose sin ningún problema. 
			IADeVProject.paused = false;
			break;
		case Input.Keys.Z:					// Habilitar que haya ganador
			IADeVProject.canBeThereWinner = true;
			
			// Cuando volvamos a habilitar el que haya un ganador, ponemos ambas variables a false.
			// 	Si mientras el flag 'canBeThereWinner' esta deshabilitado, los puntos de moral de una base llegan a cero, 
			//		el flag correspodiente del equipo correspondiente se cambia a true (y no salta la ventana indicando que hay un ganador).
			//	Sin embargo, si los puntos de moral de esa misma base aumentan, EL FLAG DEL EQUIPO CORRESPONDIENTE SEGUIRÁ A TRUE, por lo que al volver a habilitar
			//		el flag 'canBeThereWinner' saltará la ventana de que hay un ganador SIN QUE, posiblemente, LOS PUNTOS DE MORAL DE NINGUNA BASE ESTÉN A 0.
			// --> Por tanto, hay que volver a poner estas 2 variables a false para que se vuelvan a hacer todas las comprobaciones pertinentes de si ha habido un ganador.
			IADeVProject.FJAVIER_win = false;
			IADeVProject.LDANIEL_win = false;
			
			break;
		case Input.Keys.M:				// Habilitar debug de los comportamientos
			IADeVProject.PRINT_PATH_BEHAVIOUR = false;
			break;
		case Input.Keys.N:				// Deshabilitar debug de los comportamientos
			IADeVProject.PRINT_PATH_BEHAVIOUR = true;
			break;
		default:
			processState(keycode);
			break;
		}
		
		return true;
	}
	
	/**
	 * Método que procesa el estado en el que se está procesando las acciones del usuario conjunto
	 * a la tecla pulsada 'keycode'
	 * @param keycode tecla pulsada
	 */
	private void processState(int keycode) {
		IADeVProject.resetSelectedCharacters();
		keyPressed = false;
		switch (state) {
			case NO_SELECTED_CHARACTERS: 
				break;
			case JUST_SELECTED_CHARACTERS: 
				processJustSelectedCharacters(keycode);
				break;
			case ACCELERATED:
				if (target == null) {
					System.out.println("You must select a character.");
					return;
				}
				processAccelerated(keycode);
				break;
			case DELEGATED:
				if (target == null) { 
					System.out.println("You must select a character.");
					return;
				}
				processDelegated(keycode);
				break;
			case PATHFINDING:
				if (pathFindingPos == null) {
					System.out.println("You must select a valid position");
					return;
				}
				processPathFinding(keycode);
				break;
			case GROUP:
				if (target == null) {
					System.out.println("You must select a character.");
					return;
				} else if (!IADeVProject.selectedCharacters.contains(target)) {
					// Si el personaje seleccionado no estaba seleccionado ya, mostrarmos
					// mensaje de error.
					System.out.println("The selected characters is not in selected characters list.");	
					return;
				}
				processGroup(keycode);
				break;
			case OTHERS:
				if (target == null) {
					System.out.println("You must select a character.");
					return;
				}
				processOthers(keycode);
				break;
			case MAKE_FORMATION:
				if (IADeVProject.selectedCharacters.size() < 2) {
					System.out.println("For make formation, you must select almost 3 characters");
					return;
				}
				if (anchorPos == null) {
					System.out.println("You must select anchor position");
					return;
				}
				processMakeFormation(keycode);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Método que procesa la tecla pulsada para cambiar de estado cuando se ha seleccionado personajes
	 * y se quiere hacer algo con ellos
	 * @param keycode Código de la tecla pulsada.
	 */
	private void processJustSelectedCharacters(int keycode) {		
		switch (keycode) {
		case Input.Keys.NUM_1: // Apply accelerated behaviours
			UserInteraction.printAcceleratedBehaviours();
			System.out.println("Select the character with which you want to apply the behaviour");
			state = UserState.ACCELERATED;
			break;
		case Input.Keys.NUM_2: // Apply delegated behaviours
			UserInteraction.printDelegatedBehaviours();
			System.out.println("Select the character with which you want to apply the behaviour");
			state = UserState.DELEGATED;
			break;
		case Input.Keys.NUM_3: // Apply group behaviours
			UserInteraction.printGroupBehaviours();
			System.out.println("Select the character (it must have been selected yet) with which you want to apply the behaviour");
			state = UserState.GROUP;
			break;
		case Input.Keys.NUM_4: // Pathfinding
			UserInteraction.printPathFinding();
			System.out.println("Select the position where you want to apply pathfinding");
			state = UserState.PATHFINDING;
			break;
		case Input.Keys.NUM_5: // Others behaviours
			UserInteraction.printOthersBehaviours();
			System.out.println("Select the character with which you want to apply the behaviour");
			state = UserState.OTHERS;
			break;
		case Input.Keys.NUM_6: // Make formation
			UserInteraction.printMakeFormation();
			System.out.println("Select the point where situate the anchor");
			state = UserState.MAKE_FORMATION;
			break;
		default:
			return;
		}
	}
	
	/**
	 * Método que encapsula las acciones que se aplica cuando se quiere volver en las acciones
	 * del usuario.
	 */
	private void returnActions() {
		state = UserState.JUST_SELECTED_CHARACTERS;
		IADeVProject.resetSelectedCharacters();
		UserInteraction.printPossibleUserActions();
		this.target = null;
	}
	
	/**
	 * Método que procesa la interacción de comportamientos acelerados
	 * @param keycode tecla pulsada
	 */
	private void processAccelerated(int keycode) {
		switch (keycode) {
		case Input.Keys.NUM_1: // Align
			System.out.println("1) Align");
			UserInteraction.applyAlign(target);
			break;
		case Input.Keys.NUM_2: // Anti-Align
			System.out.println("2) Anti-Align");
			UserInteraction.applyAntiAlign(target);
			break;
		case Input.Keys.NUM_3: // Arrive
			System.out.println("3) Arrive");
			UserInteraction.applyArrive(target);
			break;
		case Input.Keys.NUM_4: // Flee
			System.out.println("4) Flee");
			UserInteraction.applyFlee(target);
			break;
		case Input.Keys.NUM_5: // Seek
			System.out.println("5) Seek");
			UserInteraction.applySeek(target);
			break;
		case Input.Keys.NUM_6: // Velocity matching
			System.out.println("6) Velocity matching");
			UserInteraction.applyVelocityMatching(target);
			break;
		case Input.Keys.NUM_7: // Return
			returnActions();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Método que procesa la interacción de comportamientos delegados
	 * @param keycode tecla pulsada
	 */
	private void processDelegated(int keycode) {
		switch (keycode) {
		case Input.Keys.NUM_1: // Evade
			System.out.println("1) Evade");
			UserInteraction.applyEvade(target);
			break;
		case Input.Keys.NUM_2: // Face
			System.out.println("2) Face");
			UserInteraction.applyFace(target);
			break;
		case Input.Keys.NUM_3: // Looking where you going
			System.out.println("3) Looking where you going");
			UserInteraction.applyLookingWhereYouGoing();
			break;
		case Input.Keys.NUM_4: // Pathfinding
			System.out.println("4) Pathfinding");
			UserInteraction.applyContinuousPathFinding(touchPos);
			break;
		case Input.Keys.NUM_5: // Persue
			System.out.println("5) Persue");
			UserInteraction.applyPersue(target);
			break;
		case Input.Keys.NUM_6: // Wander
			System.out.println("6) Wander");
			UserInteraction.applyWander();
			break;
		case Input.Keys.NUM_7: // Return
			returnActions();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Método que procesa la interacción de comportamientos de grupo
	 * @param keycode tecla pulsada
	 */
	private void processGroup(int keycode) {
		List<WorldObject> list;
		switch (keycode) {
		case Input.Keys.NUM_1: // Cohesion
			System.out.println("1) Cohesion");
			// Creamos la lista de personajes a las que aplicar el comportamiento quitando el personaje seleccionado
			list = new LinkedList<WorldObject>(IADeVProject.selectedCharacters);
			list.remove(target);
			UserInteraction.applyCohesion(target, list);
			break;
		case Input.Keys.NUM_2: // Separation
			System.out.println("2) Separation");
			// Creamos la lista de personajes a las que aplicar el comportamiento quitando el personaje seleccionado
			list = new LinkedList<WorldObject>(IADeVProject.selectedCharacters);
			list.remove(target);
			UserInteraction.applySeparation(target, list);
			break;
		case Input.Keys.NUM_3: // Return
			returnActions();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Método que procesa la interacción de realizar un pathfinding
	 */
	private void processPathFinding(int keycode) {
		switch (keycode) {
		case Input.Keys.NUM_1:
			UserInteraction.applyContinuousPathFinding(pathFindingPos);
			break;
		case Input.Keys.NUM_2:// Return
			returnActions();
			break;
		default:
			break;
			
		}
		
	}
	
	/**
	 * Método que procesa la interacción de otros comportamientos
	 * @param keycode tecla pulsada
	 */
	private void processOthers(int keycode) {
		switch (keycode) {
		case Input.Keys.NUM_1: // Attack
			System.out.println("1) Attack");
			UserInteraction.applyAttack(target);
			break;
		case Input.Keys.NUM_2: // Cure
			System.out.println("2) Cure");
			UserInteraction.applyCure();
			break;
		case Input.Keys.NUM_3: // Return
			returnActions();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Método que encapsula las acciones que se aplican después de crear una formación.
	 * IMPORTANTE: Después de crear una formación, esta no está seleccionada. Para eliminarla
	 * hay que seleccionar y deseleccionarla.
	 */
	private void afterMakeFormation() {
		state = UserState.NO_SELECTED_CHARACTERS;
		IADeVProject.clearSelectedCharactersList();
		anchorPos = null;
	}
	
	/**
	 * Método que procesa la interacción de crear una formación
	 * @param keycode tecla pulsada
	 */
	private void processMakeFormation(int keycode) {
		switch (keycode) {
		case Input.Keys.NUM_1: // Circular Look outside
			System.out.println("\t1) Circular - Look outside");
			UserInteraction.applyCircularLookOutSideFormation(anchorPos);
			afterMakeFormation();
			break;
		case Input.Keys.NUM_2: // Circular Look inside
			System.out.println("\t2) Circular - Look inside");
			UserInteraction.applyCircularLookInSideFormation(anchorPos);
			afterMakeFormation();
			break;	
		case Input.Keys.NUM_3: // Line
			System.out.println("\t3) Line");
			UserInteraction.applyLineFormation(anchorPos);
			afterMakeFormation();
			break;
		case Input.Keys.NUM_4: // Star Look outside
			System.out.println("\t4) Star - Look outside");
			UserInteraction.applyStarLookOutSideFormation(anchorPos);
			afterMakeFormation();
			break;
		case Input.Keys.NUM_5: // Star Look inside
			System.out.println("\t5) Star - Look inside");
			UserInteraction.applyStarLookInSideFormation(anchorPos);
			afterMakeFormation();
			break;
		case Input.Keys.NUM_6: // Return
			returnActions();
			break;
		default:
			break;
		}
	}
	
	

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		// Si el botón presionado es el botón IZQUIERDO, se quiere seleccionar a un personaje
		if (button == Input.Buttons.LEFT) {
			
			// Obtenemos las coordenadas de la cámara correspondiente a las coordenadas de la pantalla
			touchPos = new Vector3();
			touchPos.set(screenX, screenY, 0);
			IADeVProject.camera.unproject(touchPos);
			
			// Solo dejamos que seleccione un personaje cuando no estamos ejecutando ninguna comportamiento
			if (state == UserState.NO_SELECTED_CHARACTERS || state == UserState.JUST_SELECTED_CHARACTERS) {
				Character character = getCharacterOfPosition(touchPos);
				
				if (character != null) {
					if (isItSameTeamOfSelectedTeam(character)) {
						// Si el personaje es del mismo equipo que la lista de personajes seleccionados, lo incluimos.
						// Si se ha pinchado sobre él, añadimos a la lista de objetos seleccionados
						IADeVProject.addToSelectedCharactersList(character);
						// Mostramos las posibles acciones del usuario
						UserInteraction.printPossibleUserActions();
						// Cambiamos de estado
						state = UserState.JUST_SELECTED_CHARACTERS;
					}
				}
			} else {
				// Si estamos ejecutando algún comportamiento, obtenemos el personaje que se ha seleccionado
				target = getCharacterOfPosition(touchPos);
				if (target != null) { 
					System.out.println("The character has been selected. Press some number to apply a behaviour");
				} else if (state == UserState.MAKE_FORMATION) {
					anchorPos = touchPos;
					System.out.println("The anchor has been selected. Press some number to make formation");
				} else if (state == UserState.PATHFINDING) {
					pathFindingPos = touchPos;
					System.out.println("The position has been selected. Press some number to apply pathfinding");
				} else {
					System.out.println("The target character has been released. Select another character.");
				}
			}
			
		} else if (button == Input.Buttons.RIGHT) {
			// Si el botón presionado es el botón DERECHO, limpiamos la lista de objetos seleccionados.
			IADeVProject.clearSelectedCharactersList();
			state = UserState.NO_SELECTED_CHARACTERS;
		}
		
		// Devolvemos true indicando que se ha procesado el evento
		return true;
	}
	
	/**
	 * Método que comprueba si el personaje 'target' es del mismo equipo que los personajes
	 * seleccionados. 
	 * @param target Personaje que se acaba de seleccionar
	 * @return true si es del mismo equipo o la lista de personajes seleccionados está vacía, false en caso contrario.
	 */
	private static boolean isItSameTeamOfSelectedTeam(Character target) {
		Iterator<Character> it = IADeVProject.selectedCharacters.iterator();
		
		while (it.hasNext()) {
			Character next = it.next();
			return Checks.isItFromMyTeam(next, target);
		}
		
		return true;
	}
	
	/**
	 * Método que dada una posición devuelve el personaje que se encuentra en la posición
	 * pasada como parámetro. 
	 * @param position Posición
	 * @return Personaje en esa posición, null en caso contrario.
	 */
	private static Character getCharacterOfPosition(Vector3 position) {
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj instanceof Character) {
				Character c = (Character)obj;
				// Comprobamos si se ha pinchado sobre él
				if (c.getBoundingRectangle().contains(new Vector2(position.x, position.y))) { 
					return c;
				}
			}
		}
		return null;
	}
	
	/**
	 * Este método ha sido creado porque InputProcessor no tiene ningún método para controlar
	 * que una tecla se mantenga pulsada. Lo que hace es comprobar si hay una tecla pulsada
	 * y si es así, llama al método 'keyDown' con la tecla pulsada para procesarla.
	 */
	public void processKeyPressed() {
		if (keyPressed) {
			keyDown(lastKey);
		}
	}
	
	@Override
	public boolean keyUp(int keycode) {
		// Con esto indicamos que si la tecla que se suelta no es la última que se ha
		// pulsado, no hacemos nada, se sigue haciendo el efecto de la última tecla pulsada
		if (keycode != lastKey) return true;
		
		keyPressed = false;
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
