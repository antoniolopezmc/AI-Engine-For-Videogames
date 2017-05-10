package com.mygdx.iadevproject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
	public static List<Vector3> getWaypointsOfMyBase(Character source) {
		if (source.getTeam() == Team.FJAVIER) {
			return fjavierWayPoints;
		} else if (source.getTeam() == Team.LDANIEL) {
			return ldanielWayPoints;
		} else {
			return new LinkedList<Vector3>();
		}
	}
	
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
	// Necesitamos almacenar una tripleta (3 objetos de distinto tipo). Como tal, Java no ofrece ese tipo de estructura.
	// Para solucionar este problema hemos usado un Map, tal que su valor es a su vez una entrada una entrada de un Map.
	// 		Esto último es lo que nos permie poder almacenar otros 2 objetos de distinto tipo (a parte del primero).
	private static Map<Vector3, Map.Entry<Boolean, Vector3>> bridgesWayPoints;
}
