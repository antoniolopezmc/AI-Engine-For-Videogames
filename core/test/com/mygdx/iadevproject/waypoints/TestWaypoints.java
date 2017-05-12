package com.mygdx.iadevproject.waypoints;

import static org.junit.Assert.fail;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Team;

public class TestWaypoints {

	// Equipo FJAVIER (el de arriba) 
	private Character personaje1;
	private Character personaje2;
	private Character personaje3;
	private Character personaje4;
	private Character personaje5;
	private Character personaje6;
	private Character personajeExtra1;
	// Equipo LDANIEL (el de abajo)
	private Character personaje7;
	private Character personaje8;
	private Character personaje9;
	private Character personaje10;
	private Character personaje11;
	private Character personaje12;
	private Character personajeExtra2;
	// Personajes neutros.
	private Character personaje13;
	private Character personaje14;
	
	
	private void inicializar() {
		// Inicializamos las estructuras.
		Waypoints.initializeBridgesWaypoints();
		// Creamos los personajes.
		// Equipo FJAVIER (el de arriba) 
		personaje1 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje1.setTeam(Team.FJAVIER);
		personaje2 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje2.setTeam(Team.FJAVIER);
		personaje3 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje3.setTeam(Team.FJAVIER);
		personaje4 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje4.setTeam(Team.FJAVIER);
		personaje5 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje5.setTeam(Team.FJAVIER);
		personaje6 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje6.setTeam(Team.FJAVIER);
		personajeExtra1 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personajeExtra1.setTeam(Team.FJAVIER);
		// Equipo LDANIEL (el de abajo)
		personaje7 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje7.setTeam(Team.LDANIEL);
		personaje8 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje8.setTeam(Team.LDANIEL);
		personaje9 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje9.setTeam(Team.LDANIEL);
		personaje10 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje10.setTeam(Team.LDANIEL);
		personaje11 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje11.setTeam(Team.LDANIEL);
		personaje12 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje12.setTeam(Team.LDANIEL);
		personajeExtra2 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personajeExtra2.setTeam(Team.LDANIEL);
		// Personajes neutros.
		personaje13 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje13.setTeam(Team.NEUTRAL);
		personaje14 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
		personaje14.setTeam(Team.NEUTRAL);
	}
	
	public void ejecutarPruebas () {
		// ********************************************************************************************************************
		List<Vector3> reservaPersonaje1 = Waypoints.bookBridgeWaypoint(personaje1);
		if (reservaPersonaje1.size() == 2) {
			System.out.println("El personaje 1 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje 1 no se ha realizado correctamente");
		}
		List<Vector3> reservaPersonaje1_yaAsociado = Waypoints.bookBridgeWaypoint(personaje1);
		if ((reservaPersonaje1_yaAsociado.size() == 2) && 
				(reservaPersonaje1_yaAsociado.get(0).equals(reservaPersonaje1.get(0))) && 
				(reservaPersonaje1_yaAsociado.get(1).equals(reservaPersonaje1.get(1)))) {
			System.out.println("El personaje 1 ya habia reservado. Se han obtenido los mismos waypoints.");
		} else {
			System.err.println("Personaje 1 reserva de nuevo. Salida incorrecta.");
		}
		// ********************************************************************************************************************
		
		List<Vector3> reservaPersonaje2 = Waypoints.bookBridgeWaypoint(personaje2);
		if (reservaPersonaje2.size() == 2) {
			System.out.println("El personaje 2 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje 2 no se ha realizado correctamente");
		}
		// ********************************************************************************************************************
		
		List<Vector3> reservaPersonaje3 = Waypoints.bookBridgeWaypoint(personaje3);
		if (reservaPersonaje3.size() == 2) {
			System.out.println("El personaje 3 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje 3 no se ha realizado correctamente");
		}
		List<Vector3> reservaPersonaje1_yaAsociado2 = Waypoints.bookBridgeWaypoint(personaje1);
		if ((reservaPersonaje1_yaAsociado2.size() == 2) && 
				(reservaPersonaje1_yaAsociado2.get(0).equals(reservaPersonaje1.get(0))) && 
				(reservaPersonaje1_yaAsociado2.get(1).equals(reservaPersonaje1.get(1)))) {
			System.out.println("El personaje 1 ya habia reservado. Se han obtenido los mismos waypoints.");
		} else {
			System.err.println("Personaje 1 reserva de nuevo. Salida incorrecta.");
		}
		// ********************************************************************************************************************
		
		List<Vector3> reservaPersonaje4 = Waypoints.bookBridgeWaypoint(personaje4);
		if (reservaPersonaje4.size() == 2) {
			System.out.println("El personaje 4 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje 4 no se ha realizado correctamente");
		}
		List<Vector3> reservaPersonaje2_yaAsociado = Waypoints.bookBridgeWaypoint(personaje2);
		if ((reservaPersonaje2_yaAsociado.size() == 2) && 
				(reservaPersonaje2_yaAsociado.get(0).equals(reservaPersonaje2.get(0))) && 
				(reservaPersonaje2_yaAsociado.get(1).equals(reservaPersonaje2.get(1)))) {
			System.out.println("El personaje 2 ya habia reservado. Se han obtenido los mismos waypoints.");
		} else {
			System.err.println("Personaje 2 reserva de nuevo. Salida incorrecta.");
		}
		List<Vector3> reservaPersonaje4_yaAsociado = Waypoints.bookBridgeWaypoint(personaje4);
		if ((reservaPersonaje4_yaAsociado.size() == 2) && 
				(reservaPersonaje4_yaAsociado.get(0).equals(reservaPersonaje4.get(0))) && 
				(reservaPersonaje4_yaAsociado.get(1).equals(reservaPersonaje4.get(1)))) {
			System.out.println("El personaje 4 ya habia reservado. Se han obtenido los mismos waypoints.");
		} else {
			System.err.println("Personaje 4 reserva de nuevo. Salida incorrecta.");
		}
		// ********************************************************************************************************************
		
		List<Vector3> reservaPersonaje5 = Waypoints.bookBridgeWaypoint(personaje5);
		if (reservaPersonaje5.size() == 2) {
			System.out.println("El personaje 5 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje 5 no se ha realizado correctamente");
		}
		// ********************************************************************************************************************
		
		List<Vector3> reservaPersonaje6 = Waypoints.bookBridgeWaypoint(personaje6);
		if (reservaPersonaje6.size() == 2) {
			System.out.println("El personaje 6 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje 6 no se ha realizado correctamente");
		}
		// ********************************************************************************************************************
		List<Vector3> reservaPersonaje8 = Waypoints.bookBridgeWaypoint(personaje8);
		if (reservaPersonaje8.size() == 2) {
			System.out.println("El personaje 8 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje 8 no se ha realizado correctamente");
		}
		// ********************************************************************************************************************
		List<Vector3> reservaPersonajeExtra1 = Waypoints.bookBridgeWaypoint(personajeExtra1);
		if (reservaPersonajeExtra1.size() == 0) {
			System.out.println("Personaje extra 1 intenta reservar. Ya no quedan más waypoints.");
		} else {
			System.err.println("Salida incorrecta.");
		}
		// ********************************************************************************************************************
		List<Vector3> reservaPersonajeExtra1_otroIntento = Waypoints.bookBridgeWaypoint(personajeExtra1);
		if (reservaPersonajeExtra1_otroIntento.size() == 0) {
			System.out.println("Personaje extra 1 intenta reservar. Ya no quedan más waypoints.");
		} else {
			System.err.println("Salida incorrecta.");
		}
		// ********************************************************************************************************************
		List<Vector3> reservaPersonaje9 = Waypoints.bookBridgeWaypoint(personaje9);
		if (reservaPersonaje9.size() == 2) {
			System.out.println("El personaje 9 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje 9 no se ha realizado correctamente.");
		}
		// ********************************************************************************************************************
		List<Vector3> reservaPersonajeExtra1_otroOtroIntento = Waypoints.bookBridgeWaypoint(personajeExtra1);
		if (reservaPersonajeExtra1_otroOtroIntento.size() == 0) {
			System.out.println("Personaje extra 1 intenta reservar. Ya no quedan más waypoints.");
		} else {
			System.err.println("Salida incorrecta.");
		}
		// ********************************************************************************************************************
		List<Vector3> consultaPersonaje1 = Waypoints.getAssociatedWaypointAndNeighboring(personaje1);
		if ((consultaPersonaje1.size() == 2) && 
				(consultaPersonaje1.get(0).equals(reservaPersonaje1.get(0))) && 
				(consultaPersonaje1.get(1).equals(reservaPersonaje1.get(1)))) {
			System.out.println("La consulta del personaje 1 se ha realizado correctamente.");
		} else {
			System.err.println("Salida incorrecta.");
		}
		// ********************************************************************************************************************
		Waypoints.freeBridgeWaypoint(personaje2);
		System.out.println("El personaje 2 libera su waypoint del puente.");
		// ********************************************************************************************************************
		List<Vector3> reservaPersonajeExtra1_otroOtroOtroIntento = Waypoints.bookBridgeWaypoint(personajeExtra1);
		if (reservaPersonajeExtra1_otroOtroOtroIntento.size() == 2) {
			System.out.println("El personaje extra 1 ha reservado el waypoint correctamente.");
		} else {
			System.err.println("La reserva del Waypoint por el personaje extra 1 no se ha realizado correctamente.");
		}
	}
	
	
	public static void main(String[] args) {
		TestWaypoints tests = new TestWaypoints();
		tests.inicializar();
		tests.ejecutarPruebas();
	}
}
