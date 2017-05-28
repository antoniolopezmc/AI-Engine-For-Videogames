package com.mygdx.iadevproject.userInteraction;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.AntiAlign_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Arrive_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Flee_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.VelocityMatching_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Evade;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Face;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.LookingWhereYouGoing;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.PathFollowingWithoutPathOffset_Arrive;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Persue;
import com.mygdx.iadevproject.aiReactive.behaviour.group.Cohesion;
import com.mygdx.iadevproject.aiReactive.behaviour.group.Separation;
import com.mygdx.iadevproject.aiReactive.pathfinding.continuous.Continuous_PathFinding;
import com.mygdx.iadevproject.aiTactical.roles.TacticalRole;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.map.Ground;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.model.formation.CircularFormation;
import com.mygdx.iadevproject.model.formation.Formation;
import com.mygdx.iadevproject.model.formation.LineFormation;
import com.mygdx.iadevproject.model.formation.StarFormation;

public class UserInteraction {

	/** MÉTODOS DE ACCIONES **/
	
	/** ACCELERATED BEHAVIOURS **/
	/**
	 * Método que aplica el comportamiento Align a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyAlign(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new Align_Accelerated(source, target, 20.0f, 20.0f, 5.0f, 10.0f, 1.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Anti-Align a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyAntiAlign(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new AntiAlign_Accelerated(source, target, 20.0f, 20.0f, 5.0f, 10.0f, 1.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Arrive a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyArrive(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new Arrive_Accelerated(source, target, 30.0f, 50.0f, 5.0f, 10.0f, 1.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Flee a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyFlee(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new Flee_Accelerated(source, target, 20.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Seek a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applySeek(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new Seek_Accelerated(source, target, 20.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Velocity Matching a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyVelocityMatching(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new VelocityMatching_Accelerated(source, target, 50.0f, 1.0f));
		}
	}
	
	
	/** DELEGATED BEHAVIOURS **/
	/**
	 * Método que aplica el comportamiento Evade a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyEvade(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new Evade(source, target, 20.0f, 2.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Face a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyFace(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new Face(source, target, 30.0f, 30.0f, 5.0f, 10.0f, 1.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Looking Where You Going a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyLookingWhereYouGoing() {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new LookingWhereYouGoing(source, 30.0f, 30.0f, 5.0f, 10.0f, 1.0f));
		}
	}
	
	/**
	 * Método que comprueba si la posición pasada como parámetro es correcta (no está en un terreno infranqueable)
	 * @param position Posición que se quiere saber si es correcta
	 * @return true si es correcta, false en caso contrario.
	 */
	private static boolean checkIfCorrectPosition(Vector3 position) {
		Ground ground = IADeVProject.getGroundOfPosition(position);
		
		return (ground != Ground.MOUNTAINS && ground != Ground.WATER);
	}
	
	/** IMPORTANTE: ESTE MÉTODO UTILIZA EL PATHFINDING CONTINUO, NO EL PUNTO A PUNTO **/
	/**
	 * Método que aplica el comportamiento PathFinding CONTINUO a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyContinuousPathFinding(Vector3 position) {
		if (!checkIfCorrectPosition(position)){
			System.out.println("Destination not allowed. Select another position.");
			return;
		}
		
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			Continuous_PathFinding pf = new Continuous_PathFinding();
			List<Vector3> pointsList = pf.applyPathFinding(IADeVProject.MAP_OF_COSTS, IADeVProject.GRID_CELL_SIZE, Continuous_PathFinding.EUCLIDEAN_DISTANCE, 
					IADeVProject.GRID_WIDTH, IADeVProject.GRID_HEIGHT, source.getPosition().x, source.getPosition().y, position.x, position.y);
			
			source.addToListBehaviour(new LookingWhereYouGoing(source, 30.0f, 30.0f, 5.0f, 10.0f, 1.0f));
			source.addToListBehaviour(new PathFollowingWithoutPathOffset_Arrive(source, 30.0f, 50.0f, 30.0f, 40.0f, 1.0f, pointsList, PathFollowingWithoutPathOffset_Arrive.MODO_PARAR_AL_FINAL));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Persue a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyPersue(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.addToListBehaviour(new Persue(source, target, 20.0f, 2.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Wander a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyWander() {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.getListBehaviour().putAll(Actions.doRandomThings(150.0f, source));
		}
	}
	
	
	
	/** GROUP BEHAVIOURS **/
	/**
	 * Método que aplica el comportamiento Cohesion al personaje 'source' sobre la lista
	 * de personajes 'targets'
	 * @param source Personaje fuente.
	 * @param targets Personajes objetivos.
	 */
	public static void applyCohesion(Character source, List<WorldObject> targets) {
		// Establecemos la lista de comportamientos de no colisionar
		source.setListBehaviour(Actions.notCollide(200.0f, source));
		// Creamos el comportamiento y se lo añadimos al personaje
		// IMPORTANTE: SE LE PONE EL VALOR DE threshold a 3000.0f para que tenga en cuenta
		// todo el mapa y esté donde esté el personaje, realice el comportamiento.
		source.addToListBehaviour(new Cohesion(source, targets, 50.0f, 3000.0f));
	}
	
	/**
	 * Método que aplica el comportamiento Separation al personaje 'source' sobre la lista
	 * de personajes 'targets'
	 * @param source Personaje fuente.
	 * @param targets Personajes objetivos.
	 */
	public static void applySeparation(Character source, List<WorldObject> targets) {
		// Establecemos la lista de comportamientos de no colisionar
		source.setListBehaviour(Actions.notCollide(200.0f, source));
		// Creamos el comportamiento y se lo añadimos al personaje
		// Para más información, váyase al test del Separation. Gracias =D
		source.addToListBehaviour(new Separation(source, 20.0f, targets, 300.0f, 10000.0f));
	}
	
	
	
	/** OTHERS BEHAVIOURS **/
	/**
	 * Método que aplica el comportamiento Attack a todos los personajes seleccionados 
	 * hacia el objetivo 'target'
	 * @param target Objetivo al que aplicar el comportamiento.
	 */
	public static void applyAttack(Character target) {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			
			float damageToDone = 50;	// Daño por defecto
			float maxDistance = 250;	// Máxima distancia por defecto
			
			// Si el personaje tiene un rol, entonces obtenemos sus valores de daño y distancia
			if (source.getRole() != null) { 
				damageToDone = source.getRole().getDamageToDone();
				maxDistance = source.getRole().getMaxDistanceOfAttack();
			}
			
			// Creamos el comportamiento y se lo añadimos al personaje
			source.getListBehaviour().putAll(Actions.attack(source, target, damageToDone, maxDistance));
			// Cuando atacamos, vamos a por él.
			source.getListBehaviour().putAll(Actions.arrive(30.0f, source, target, 30.0f));
		}
	}
	
	/**
	 * Método que aplica el comportamiento Cure a todos los personajes seleccionados.
	 */
	public static void applyCure() {
		for (Character source : IADeVProject.selectedCharacters) {
			// Establecemos la lista de comportamientos de no colisionar
			source.setListBehaviour(Actions.notCollide(200.0f, source));
			// Creamos el comportamiento y se lo añadimos al personaje
			source.getListBehaviour().putAll(Actions.cure(source, TacticalRole.health_cure));
		}
	}
	
	
	
	/** MAKE FORMATION **/
	/**
	 * Método que realiza la formación circular mirando hacia fuera cuya ancla es la posición 'position'
	 * @param position Posición del ancla.
	 */
	public static void applyCircularLookOutSideFormation(Vector3 position) {
		if (!checkIfCorrectPosition(position)){
			System.out.println("Anchor position not allowed");
			return;
		}
		CircularFormation formation = new CircularFormation(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), 50.0f);
        formation.setBounds(position.x, position.y, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        formation.setOrientation(0.0f);
        formation.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        for (Character c : IADeVProject.selectedCharacters) {
        	formation.addCharacterToCharactersList(c);
        }
        formation.setSeparationDistance(150.0f);
        formation.setComponentFormationOrientationMode(Formation.LOOK_OUTSIDE);
        Iterator<Character> it = IADeVProject.selectedCharacters.iterator();
        if (it.hasNext()) formation.setTeam(it.next().getTeam()); 
        
        IADeVProject.addToWorldObjectList(formation);
	}

	/**
	 * Método que realiza la formación circular mirando hacia dentro cuya ancla es la posición 'position'
	 * @param position Posición del ancla.
	 */
	public static void applyCircularLookInSideFormation(Vector3 position) {
		if (!checkIfCorrectPosition(position)){
			System.out.println("Anchor position not allowed");
			return;
		}
		CircularFormation formation = new CircularFormation(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), 50.0f);
        formation.setBounds(position.x, position.y, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        formation.setOrientation(0.0f);
        formation.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        for (Character c : IADeVProject.selectedCharacters) {
        	formation.addCharacterToCharactersList(c);
        }
        formation.setSeparationDistance(150.0f);
        formation.setComponentFormationOrientationMode(Formation.LOOK_INSIDE);
        Iterator<Character> it = IADeVProject.selectedCharacters.iterator();
        if (it.hasNext()) formation.setTeam(it.next().getTeam());
        
        IADeVProject.addToWorldObjectList(formation);
	}
	
	/**
	 * Método que realiza la formación en línea cuya ancla es la posición 'position'
	 * @param position Posición del ancla.
	 */
	public static void applyLineFormation(Vector3 position) {
		if (!checkIfCorrectPosition(position)){
			System.out.println("Anchor position not allowed");
			return;
		}
		
		LineFormation formation = new LineFormation(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), 50.0f);
		formation.setBounds(position.x, position.y, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        formation.setOrientation(0.0f);
        formation.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        for (Character c : IADeVProject.selectedCharacters) {
        	formation.addCharacterToCharactersList(c);
        }
        formation.setSeparationDistance(150.0f);
        formation.setComponentFormationOrientationMode(Formation.FREE_ORIENTATION);
        Iterator<Character> it = IADeVProject.selectedCharacters.iterator();
        if (it.hasNext()) formation.setTeam(it.next().getTeam());
        
        IADeVProject.addToWorldObjectList(formation);
	}
	
	/**
	 * Método que realiza la formación en estrella mirando hacia fuera cuya ancla es la posición 'position'
	 * @param position Posición del ancla.
	 */
	public static void applyStarLookOutSideFormation(Vector3 position) {
		if (!checkIfCorrectPosition(position)){
			System.out.println("Anchor position not allowed");
			return;
		}
		
		StarFormation formation = new StarFormation(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), 50.0f);
		formation.setBounds(position.x, position.y, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        formation.setOrientation(0.0f);
        formation.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        for (Character c : IADeVProject.selectedCharacters) {
        	formation.addCharacterToCharactersList(c);
        }
        formation.setSeparationDistance(150.0f);
        formation.setComponentFormationOrientationMode(Formation.LOOK_OUTSIDE);
        Iterator<Character> it = IADeVProject.selectedCharacters.iterator();
        if (it.hasNext()) formation.setTeam(it.next().getTeam());

        IADeVProject.addToWorldObjectList(formation);
	}
	
	/**
	 * Método que realiza la formación en estrella mirando hacia dentro cuya ancla es la posición 'position'
	 * @param position Posición del ancla.
	 */
	public static void applyStarLookInSideFormation(Vector3 position) {
		if (!checkIfCorrectPosition(position)){
			System.out.println("Anchor position not allowed");
			return;
		}
		
		StarFormation formation = new StarFormation(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), 50.0f);
		formation.setBounds(position.x, position.y, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        formation.setOrientation(0.0f);
        formation.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        for (Character c : IADeVProject.selectedCharacters) {
        	formation.addCharacterToCharactersList(c);
        }
        formation.setSeparationDistance(150.0f);
        formation.setComponentFormationOrientationMode(Formation.LOOK_INSIDE);
        Iterator<Character> it = IADeVProject.selectedCharacters.iterator();
        if (it.hasNext()) formation.setTeam(it.next().getTeam());
        
        IADeVProject.addToWorldObjectList(formation);
	}

	
	/** MÉTODOS DE MOSTRAR POR CONSOLA **/
	
	/**
	 * Método que muestra por consola los primeros usos del videojuego.
	 */
	public static void printFirstUse() {
		System.out.println();
		System.out.println("/** WELCOME TO OUR VIDEOGAME **/");
		System.out.println("Use the following keys for:");
		System.out.println("\t- Left-Arrow: Move camera to left");
		System.out.println("\t- Right-Arrow: Move camera to right");
		System.out.println("\t- Up-Arrow: Move camera to up");
		System.out.println("\t- Down-Arrow: Move camera to down");
		System.out.println("\t- A: Zoom out camera");
		System.out.println("\t- Q: Zoom in camera");
		System.out.println("\t- Control.LEFT: Allow select several characters");
		
		System.out.println("Use the following mouse buttons for:");
		System.out.println("\t- Left: Select character");
		System.out.println("\t- Right: Free selected characters");
	}
	
	/**
	 * Método que muestra por consola las posibles acciones del usuario cuando
	 * ha seleccionado personajes.
	 */
	public static void printPossibleUserActions() {
		System.out.println();
		System.out.println("What would you want to do?:");
		System.out.println("\t1) Apply accelerated behaviours");
		System.out.println("\t2) Apply delegated behaviours");
		System.out.println("\t3) Apply group behaviours");
		System.out.println("\t4) Pathfinding");
		System.out.println("\t5) Others behaviours");
		System.out.println("\t6) Make formation");
	}
	
	/**
	 * Método que muestra por consola los comportamientos accelerados que puede
	 * realizar el usuario.
	 */
	public static void printAcceleratedBehaviours() {
		System.out.println();
		System.out.println("/** ACCELERATED BEHAVIOURS **/");
		System.out.println("\t1) Align");
		System.out.println("\t2) Anti-Align");
		System.out.println("\t3) Arrive");
		System.out.println("\t4) Flee");
		System.out.println("\t5) Seek");
		System.out.println("\t6) Velocity matching");
		System.out.println("\t7) Return");
	}
	
	/**
	 * Método que muestra por consola los comportamientos delegados que puede
	 * realizar el usuario.
	 */
	public static void printDelegatedBehaviours() {
		System.out.println();
		System.out.println("/** DELEGATED BEHAVIOURS **/");
		System.out.println("\t1) Evade");
		System.out.println("\t2) Face");
		System.out.println("\t3) Looking where you going");
		System.out.println("\t4) Persue");
		System.out.println("\t5) Wander");
		System.out.println("\t6) Return");
	}
	
	/**
	 * Método que muestra por consola la acción del pathfinding que quiere 
	 * usar el usuario.
	 */
	public static void printPathFinding() {
		System.out.println();
		System.out.println("/** PATHFINDING **/");
		System.out.println("\t1) Continuous PathFinding");
		System.out.println("\t2) Return");
	}
	
	/**
	 * Método que muestra por consola los comportamientos de grupo que puede
	 * realizar el usuario.
	 */
	public static void printGroupBehaviours() {
		System.out.println();
		System.out.println("/** GROUP BEHAVIOURS **/");
		System.out.println("\t1) Cohesion");
		System.out.println("\t2) Separation");
		System.out.println("\t3) Return");
	}
	
	/**
	 * Método que muestra por consola otros comportamientos que puede
	 * realizar el usuario.
	 */
	public static void printOthersBehaviours() {
		System.out.println();
		System.out.println("/** OTHERS BEHAVIOURS **/");
		System.out.println("\t1) Attack");
		System.out.println("\t2) Healing spell");
		System.out.println("\t3) Return");
	}
	
	/**
	 * Método que muestra por consola los pasos para crear una formación.
	 */
	public static void printMakeFormation() {
		System.out.println();
		System.out.println("/** MAKE FORMATION **/");
		System.out.println("\t1) Circular - Look outside");
		System.out.println("\t2) Circular - Look inside");
		System.out.println("\t3) Line");
		System.out.println("\t4) Star - Look outside");
		System.out.println("\t5) Star - Look inside");
		System.out.println("\t6) Return");
	}
}
