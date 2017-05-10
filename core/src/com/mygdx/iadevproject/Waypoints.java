package com.mygdx.iadevproject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.Character;
/**
 * Clase para encapsular el manejo de los Waypoints. Será todo static.
 *
 */
public class Waypoints {

	// Waypoints de la base del equipo fjavier. Servirán para que los personajes sigan un camino preestablecido al patrullar su base.
	private static List<Vector3> fjavierWayPoints = new LinkedList<Vector3>(Arrays.asList(new Vector3(1592.2f,52.9f,0.0f), 
			new Vector3(1589.8f,197.8f,0.0f),
			new Vector3(1589.8f,335.8f,0.0f),
			new Vector3(1661.2f,434.7f,0.0f),
			new Vector3(1776.2f,439.3f,0.0f),
			new Vector3(1893.5f,439.3f,0.0f),
			new Vector3(1990.1f,439.3f,0.0f)));
	
	// Waypoints de la base del equipo ldaniel. Servirán para que los personajes sigan un camino preestablecido al patrullar su base.
	private static List<Vector3> ldanielWayPoints = new LinkedList<Vector3>(Arrays.asList(new Vector3(49.5f,1613.3f,0.0f), 
			new Vector3(158.6f,1613.3f,0.0f),
			new Vector3(258.2f,1613.3f,0.0f),
			new Vector3(401.1f,1613.3f,0.0f),
			new Vector3(461.2f,1692.2f,0.0f),
			new Vector3(457.5f,1769.3f,0.0f),
			new Vector3(457.5f,1974.2f,0.0f)));
	
	/**
	 * Método que devuelve la lista de los waypoints de la base de un personaje.
	 * @param source Personaje que consulta los waypoints de su base.
	 * @return una lista de puntos que representan los waypoints de la base del personaje.
	 */
	public static List<Vector3> getWaypointsOfMyBase(Character source) {
		if (source.getTeam() == Team.FJAVIER) {
			return fjavierWayPoints;
		} else if (source.getTeam() == Team.LDANIEL) {
			return ldanielWayPoints;
		} else {
			return new LinkedList<Vector3>();
		}
	}
	
	/**
	 * Método para dibujar los waypoints de ambas bases.
	 */
	public static void drawWaypointsOfBases() {
		ShapeRenderer renderer = IADeVProject.renderer;
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.RED);
		Vector3 puntoAnterior = null;	
		for (Vector3 punto : fjavierWayPoints) {
			renderer.circle(punto.x, punto.y, 2);
			if (puntoAnterior != null) {
				renderer.line(punto, puntoAnterior);
			}
			puntoAnterior = new Vector3(punto);
		}
		puntoAnterior = null;
		for (Vector3 punto : ldanielWayPoints) {
			renderer.circle(punto.x, punto.y, 2);
			if (puntoAnterior != null) {
				renderer.line(punto, puntoAnterior);
			}
			puntoAnterior = new Vector3(punto);
		}
		renderer.end();
	}
	
	// MUY IMPORTANTE.
	// A parte de los waypoints de las bases, cada equipo tendrá también una serie de waypoints en su lado correspondiente de cada uno de los puentes (3 puentes, 6 waypoints por equipo, 12 waypoints en total en los puentes).
	// 		Estos waypoints servirán para patrullar los puentes.
	// Cada puente consta de 2 puntos en cada extremo (2 para cada equipo). Cuando un personaje patrulle un puente, se moverá de un punto al otro y del otro al uno (sin parar de uno a otro).
	
	// Para que a cada personaje se le asigne un waypoint y todas las consultas necesarias se hagan en tiempo constante, vamos a tener las siguientes estructuras:
	// - Un Map formado por Character (clave) Vector3/Waypoint (valor). Este Map almacenará la correspondencia entre un personaje con el waypoint que se le ha asignado.
	// 		No puede haber más de un personaje asignado a un waypoint.
	//		Cuando asignamos un waypoint a un personaje, se almacena la tupla en el Map (y se pone a true un booleano de la siguiente estructura).
	// 		Cuando un personaje que estaba patrullando muere, debemos "desasignarlo". Esto se hace eliminando la tupla de este Map y poniendo un booleano de la siguiente estructura a false.
	// - Un Map formado por un Vector3/Waypoint (clave) y un valor con 2 elementos de distinto tipo: un booleano que indica si ese waypoint está asignado o no y su waypoint vecino del mismo lado del mismo puente.
	// 		Este Map se inicializa al principio y tendrá siempre la misma cantidad de elementos (solo se modificarán los booleanos).
	// 		MUY IMPORTANTE.
	// 		Necesitamos almacenar una tripleta (3 objetos de distinto tipo). Como tal, Java no ofrece ese tipo de estructura.
	// 		Para solucionar este problema hemos usado un Map, tal que su valor es a su vez otro Map (¡CON UNA SOLA ENTRADA!).
	// 				Esto último es lo que nos permie poder almacenar otros 2 objetos de distinto tipo (a parte del primero).
	// ---> Es decir, cada equipo tendrá sus 2 estructuras correspondientes.
	
	// TAMBIEN MUY IMPORTANTE.
	// 		Aunque a un personaje solo se le asigne un waypoint, al patrullar el puente irá de un waypoint a otro de su lado. Por tanto, en la segunda estructura, junto con cada waypoint también se almacena
	//			otro Vector3 que representa el otro waypoint del mismo lado del mismo puente.
	
	// Equipo FJAVIER (el de arriba)
	private static Map<Character, Vector3> bridges_CharacterAndWaypointAssociation_team_FJAVIER;
	private static Map<Vector3, Map<Boolean, Vector3>> bridgesWayPoints_team_FJAVIER; // Da igual que la clave del Map interior sea un Boolean, ya que en el Map interior de un elemento del Map exterior SOLO HABRÁ UNA ENTRADA.
	
	// Equipo LDANIEL (el de abajo)
	private static Map<Character, Vector3> bridges_CharacterAndWaypointAssociation_team_LDANIEL;
	private static Map<Vector3, Map<Boolean, Vector3>> bridgesWayPoints_team_LDANIEL; // Da igual que la clave del Map interior sea un Boolean, ya que en el Map interior de un elemento del Map exterior SOLO HABRÁ UNA ENTRADA.
	
	// Para obtener la estructura bridges_CharacterAndWaypointAssociation del equipo correspondiente.
	private static Map<Character, Vector3> getBridges_CharacterAndWaypointAssociation (Team team) {
		if (team == Team.FJAVIER) {
			return bridges_CharacterAndWaypointAssociation_team_FJAVIER;
		} else if (team == Team.LDANIEL) {
			return bridges_CharacterAndWaypointAssociation_team_LDANIEL;
		} else {
			return null;
		}
	}
	
	// Para obtener la estructura bridgesWayPoints del equipo correspondiente.
	private static Map<Vector3, Map<Boolean, Vector3>> getBridgesWayPoints (Team team) {
		if (team == Team.FJAVIER) {
			return bridgesWayPoints_team_FJAVIER;
		} else if (team == Team.LDANIEL) {
			return bridgesWayPoints_team_LDANIEL;
		} else {
			return null;
		}
	}
	
	// *************************************************************************************************************************
	/**
	 * Devuelve el valor del Map para un elemento de bridgesWayPoints.
	 * @param ocupado Ocupación del waypoint.
	 * @param waypointVecino Vecino del waypoint.
	 * @return el valor del Map para un elemento de bridgesWayPoints.
	 */
	private static Map<Boolean, Vector3> getValueOfBridgeWaypoint (Boolean ocupado, Vector3 waypointVecino) {
		Map<Boolean, Vector3> salida = new HashMap<Boolean, Vector3>();
		salida.put(ocupado, waypointVecino);
		return salida;
	}
	
	/**
	 * Método para inicializar todas las estructuras necesarios para el manejo de los waypoints de los puentes.
	 */
	public static void initializeBridgesWaypoints() {
		// Inicializamos todas las estructuras.
		bridges_CharacterAndWaypointAssociation_team_FJAVIER = new HashMap<Character, Vector3>();
		//bridges_CharacterAndWaypointAssociation_team_FJAVIER.put(new Character(new WeightedBlendArbitrator_Accelerated(2.0f, 2.0f)), new Vector3(2.0f, 2.0f, 2.0f));
		bridges_CharacterAndWaypointAssociation_team_LDANIEL = new HashMap<Character, Vector3>();
		bridgesWayPoints_team_FJAVIER = new HashMap<Vector3, Map<Boolean, Vector3>>();
		bridgesWayPoints_team_LDANIEL = new HashMap<Vector3, Map<Boolean, Vector3>>();
		// Añadimos todos los waypointa en la estructura de los waypoints.
		// 		-> Equipo FJAVIER (el de arriba)
		bridgesWayPoints_team_FJAVIER.put(new Vector3(482.00006f, 683.00006f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(606.0f, 852.0001f, 0.0f)));
		bridgesWayPoints_team_FJAVIER.put(new Vector3(606.0f, 852.0001f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(482.00006f, 683.00006f, 0.0f)));
		
		bridgesWayPoints_team_FJAVIER.put(new Vector3(928.0001f, 1178.0001f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(991.0001f, 1251.0001f, 0.0f)));
		bridgesWayPoints_team_FJAVIER.put(new Vector3(991.0001f, 1251.0001f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(928.0001f, 1178.0001f, 0.0f)));
		
		bridgesWayPoints_team_FJAVIER.put(new Vector3(1670.0001f, 1855.0001f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(1890.0001f, 1905.0001f, 0.0f)));
		bridgesWayPoints_team_FJAVIER.put(new Vector3(1890.0001f, 1905.0001f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(1670.0001f, 1855.0001f, 0.0f)));
		
		// 		-> Equipo LDANIEL (el de abajo)
		bridgesWayPoints_team_FJAVIER.put(new Vector3(693.00006f, 418.00006f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(866.00006f, 551.00006f, 0.0f)));
		bridgesWayPoints_team_FJAVIER.put(new Vector3(866.00006f, 551.00006f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(693.00006f, 418.00006f, 0.0f)));
		
		bridgesWayPoints_team_FJAVIER.put(new Vector3(1122.0f, 845.0f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(1276.0f, 1000.0f, 0.0f)));
		bridgesWayPoints_team_FJAVIER.put(new Vector3(1276.0f, 1000.0f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(1122.0f, 845.0f, 0.0f)));
		
		bridgesWayPoints_team_FJAVIER.put(new Vector3(1694.0001f, 1506.0001f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(1874.0001f, 1601.0001f, 0.0f)));
		bridgesWayPoints_team_FJAVIER.put(new Vector3(1874.0001f, 1601.0001f, 0.0f), getValueOfBridgeWaypoint(false, new Vector3(1694.0001f, 1506.0001f, 0.0f)));
		
	}
	
	/**
	 * Asocia un waypoint a un personaje.
	 * @param source El personaje a asociar.
	 * @return Lista de waypoints (2 waypoints) que el personaje deberá patrullar. Uno de ellos es el que le ha sido asignado (y el otro es el vecino de éste).
	 */
	public List<Vector3> bookBridgeWaypoint (Character source) {
		// Si el personaje es neutral no puede acceder a esta funcionalidad.
		if (source.getTeam() == Team.NEUTRAL) {
			return new LinkedList<Vector3>(); // Al aplicar una lista vacia al comportamiento Pathfollowing, el personaje se quedará quieto.
		} else {
			// Recuperamos las estructuras correspondientes en función del equipo del personaje.
			Map<Character, Vector3> bridges_CharacterAndWaypointAssociation = getBridges_CharacterAndWaypointAssociation(source.getTeam());
			Map<Vector3, Map<Boolean, Vector3>> bridgesWayPoints = getBridgesWayPoints(source.getTeam());
			// Reservamos un waypoint.
			if (bridges_CharacterAndWaypointAssociation.containsKey(source)) {
				// Se el personaje ya está asociado a un waypoint, devolvemos ese y su vecino. (No se modifica la otra estructura).
				Vector3 waypointAsociado = bridges_CharacterAndWaypointAssociation.get(source);
				Vector3 waypointVecino = null;
				Map<Boolean, Vector3> waypointAsociado_bridgesWayPoints = bridgesWayPoints.get(waypointAsociado); // Esto devuelve un Map<Boolean, Vector3> CON UNA ÚNICA ENTRADA.
				// --> EL BOOLEANO ESTARÁ A TRUE PORQUE ESTE WAYPOINT YA ESTABA ASOCIADO A UN PERSONAJE.
				waypointVecino = new LinkedList<Vector3>(waypointAsociado_bridgesWayPoints.values()).get(0);
				List<Vector3> salida = new LinkedList<Vector3>();
				salida.add(waypointAsociado);
				salida.add(waypointVecino);
				return salida;
			} else {
				// Si el personaje no esta asociado, buscamos un sitio para él y lo reservamos. Además añadimos el personaje y el waypoint a la lista de asociaciones.
				// 	-> Primero, buscamos un waypoint libre.
				Vector3 waypointLibre = null;
				Vector3 vecinoDelWaypointLibre = null;
				for (Entry<Vector3, Map<Boolean, Vector3>> entrada: bridgesWayPoints.entrySet()) {
					Boolean disponible = (new LinkedList<Boolean>(entrada.getValue().keySet())).get(0);
					if (disponible) {
						waypointLibre = entrada.getKey();
						vecinoDelWaypointLibre = (new LinkedList<Vector3>(entrada.getValue().values())).get(0);
					}
				}
				if (waypointLibre == null) { // Si no hay ninguno libre.
					return new LinkedList<Vector3>(); // Al aplicar una lista vacia al comportamiento Pathfollowing, el personaje se quedará quieto.
				} else { // Si hemos encontrado un waypoint libre.
					// Añadimos una asociación a la lista de asociaciones.
					bridges_CharacterAndWaypointAssociation.put(source, waypointLibre);
					// Cambiamos la disponibilidad del waypoint elegido.
					bridgesWayPoints.put(waypointLibre, getValueOfBridgeWaypoint(true, vecinoDelWaypointLibre));
					// Devolvemos el waypointLibre y su vecino.
					List<Vector3> salida = new LinkedList<Vector3>();
					salida.add(waypointLibre);
					salida.add(vecinoDelWaypointLibre);
					return salida;
				}
			}
		}
	}
	
	/**
	 * Desligamos un personaje y su waypoints del puente asociado.
	 * @param source Personaje a desligar.
	 */
	public void freeBridgeWaypoint (Character source) {
		if (source.getTeam() != Team.NEUTRAL) { // Si el personaje es neutral, no tiene acceso a esta funcionalidad.
			// Recuperamos las estructuras correspondientes en función del equipo del personaje.
			Map<Character, Vector3> bridges_CharacterAndWaypointAssociation = getBridges_CharacterAndWaypointAssociation(source.getTeam());
			Map<Vector3, Map<Boolean, Vector3>> bridgesWayPoints = getBridgesWayPoints(source.getTeam());
			// Comprobamos si el personaje realmente está asociado a un waypoint del puente.
			if (bridges_CharacterAndWaypointAssociation.containsKey(source)) { // Si lo está, lo desligamos.
				Vector3 waypointAsociado = bridges_CharacterAndWaypointAssociation.get(source); // Obtenemos el waypoint asociado al personaje de entrada.
				// También debemos obtener su waypoint vecino.
				Vector3 waypointVecino = null;
				Map<Boolean, Vector3> waypointAsociado_bridgesWayPoints = bridgesWayPoints.get(waypointAsociado); // Esto devuelve un Map<Boolean, Vector3> CON UNA ÚNICA ENTRADA.
				// --> EL BOOLEANO ESTARÁ A TRUE PORQUE ESTE WAYPOINT YA ESTABA ASOCIADO A UN PERSONAJE.
				waypointVecino = new LinkedList<Vector3>(waypointAsociado_bridgesWayPoints.values()).get(0);
				// Eliminamos la asociación de la lista de asociaciones.
				bridges_CharacterAndWaypointAssociation.remove(source); // Eliminamos de la lista de asociaciones.
				// Ahora, debemos volver a poner el waypoint como desocupado.
				bridgesWayPoints.put(waypointAsociado, getValueOfBridgeWaypoint(false, waypointVecino));
			}
		}
	}
	
	/**
	 * Método para dibujar los waypoints de los puentes.
	 */
	public static void drawWaypointsOfBridges() {
		ShapeRenderer renderer = IADeVProject.renderer;
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.BLACK);
		// Equipo FJAVIER (el de arriba)
		for (Vector3 vector3 : bridgesWayPoints_team_FJAVIER.keySet()) {
			renderer.circle(vector3.x, vector3.y, 4);
		}
		// Equipo FJAVIER (el de arriba)
		for (Vector3 vector3 : bridgesWayPoints_team_LDANIEL.keySet()) {
			renderer.circle(vector3.x, vector3.y, 4);
		}
		renderer.end();
	}
}
