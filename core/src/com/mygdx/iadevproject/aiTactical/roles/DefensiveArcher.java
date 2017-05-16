package com.mygdx.iadevproject.aiTactical.roles;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiTactical.roles.states.AttackEnemies;
import com.mygdx.iadevproject.aiTactical.roles.states.BookWaypoint;
import com.mygdx.iadevproject.aiTactical.roles.states.CureMe;
import com.mygdx.iadevproject.aiTactical.roles.states.GoToMyManantial;
import com.mygdx.iadevproject.aiTactical.roles.states.GoToMyWayPoint;
import com.mygdx.iadevproject.aiTactical.roles.states.IAmDead;
import com.mygdx.iadevproject.aiTactical.roles.states.PatrolMyWayPoint;
import com.mygdx.iadevproject.checksAndActions.Checks;
import com.mygdx.iadevproject.model.Character;

public class DefensiveArcher extends Archer {

	private DefaultStateMachine<Character, State<Character>> stateMachine;
	
	// Estados de la máquina de estados. Están como variables porque la máquina de estados,
	// cuando comprueba si está en un estado, la comprobación no es con el 'equals', si no 
	// que utiliza el '==', por lo que es necesario tener el mismo objeto para hacer la comprobación.
	private BookWaypoint 		bookWaypoint;
	private PatrolMyWayPoint 	patrolMyWayPoint;
	private AttackEnemies 		attackEnemies;
	private CureMe 				cureMe;
	private GoToMyWayPoint 		goToMyWayPoint;
	private IAmDead 			iAmDead;
	private GoToMyManantial 	goToMyManantial;
	
	public DefensiveArcher() {
		// Establecemos a null la máquina de estados porque se ha de crear cuando se utilice
		// el método 'initialize()'
		this.stateMachine = null; 
		
		// Creamos los estados de la máquina de estados.
		this.bookWaypoint 		= new BookWaypoint();
		this.patrolMyWayPoint 	= new PatrolMyWayPoint();
		this.attackEnemies 		= new AttackEnemies();
		this.cureMe 			= new CureMe();
		this.goToMyWayPoint 	= new GoToMyWayPoint();
		this.iAmDead 			= new IAmDead();
		this.goToMyManantial 	= new GoToMyManantial();
	}

	@Override
	public void initialize(Character source) {
		// Si la máquina de estados no ha sido inicializada, la creamos y actualizamos al estado inicial
		if (this.stateMachine == null) {
			// Creamos la máquina de estados con el personaje pasado como parámetro
			this.stateMachine = new DefaultStateMachine<Character, State<Character>>(source);
		}
		// Establecemos el estado inicial como reservar waypoint
		this.stateMachine.setInitialState(this.bookWaypoint);
		// Actualizamos la máquina de estados.
		this.stateMachine.update();
	}

	@Override
	public void update(Character source) {		
		if (this.stateMachine.isInState(this.bookWaypoint)) {
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "bookWaypoint");
			
			// Estamos en el estado -> Reservar waypoint (y hacer lo que sea)
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.haveILittleHealth(source)) {
				// Si me queda poca vida, cambio al estado -> Ir al manatial
				this.stateMachine.changeState(this.goToMyManantial);
				
			} else if (Checks.haveIGotWayPoint(source)) {
				// Si tengo waypoint, cambio al estado -> Ir a mi waypoint
				this.stateMachine.changeState(this.goToMyWayPoint);
			}
			// Si no cambio de estado no termino, ya que tengo que seguir intentando reservar un waypoint.
			
		} else if (this.stateMachine.isInState(this.goToMyWayPoint)) {
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "goToMyWayPoint");
			
			// Estamos en el estado -> Ir a mi WayPoint
			
			if (Checks.amIInMyWayPoint(source)) {
				// Si estoy en mi waypoint, cambio al estado -> Patrullar mi waypoint.
				this.stateMachine.changeState(this.patrolMyWayPoint);
				
			} else if (Checks.haveILittleHealth(source)) {
				// Si me queda poca vida, cambio al estado -> Ir al manantial
				this.stateMachine.changeState(this.goToMyManantial);
				
			} 
			// Si no cambio de estado, no termino, ya que hay seguir aplicando el pathfinding
			// creado al entrar a este estado.
			
		} else if (this.stateMachine.isInState(this.patrolMyWayPoint)) {
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "patrolMyWayPoint");
			
			// Estamos en el estado -> Patrullar mi waypoint
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (!Checks.amIFarFromMyWayPoint(source) && Checks.areThereEnemiesNear(source)) {
				// Si hay enemigos cerca Y no estoy lejos de mi waypoint, cambio al estado -> Atacar a los enemigos
				this.stateMachine.changeState(this.attackEnemies);
			
			} else if (Checks.haveILittleHealth(source)) {
				// Si me queda poca vida, cambio al estado -> Ir al manantial (me queda poca vida)
				this.stateMachine.changeState(this.goToMyManantial);
			
			} else {
				// Si no cambio de estado, termino. El personaje ya tendrá el comportamiento de este estado cuando
				// he cambiado a él.
				return;
			}
			
		} else if (this.stateMachine.isInState(this.attackEnemies)) { 
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "attackEnemies");
			
			// Estamos en el estado -> Atacar a los enemigos 
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.amIFarFromMyWayPoint(source) || !Checks.areThereEnemiesNear(source)) {
				// Si no hay enemigos cerca o estoy lejos de mi waypoint, cambio al estado -> Patrullar el waypoint
				this.stateMachine.changeState(this.patrolMyWayPoint);
				
			} else if (Checks.haveIDead(source)) {
				// Si el personaje ha muerto, cambio al estado -> Estoy muerto
				this.stateMachine.changeState(this.iAmDead);
			
			} 
			// Si no cambio de estado, no termino, ya que hay que calcular el enemigo más cercano 
			// a atacar (este puede haber cambiado).
			
		} else if (this.stateMachine.isInState(this.iAmDead)) {
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "iAmDead");
			
			// Estamos en el estado -> Estoy muerto
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.amIInMyManantial(source)) {
				// Si estoy en mi manantial, cambio al estado -> Curarme
				this.stateMachine.changeState(this.cureMe);
			
			} else {
				// Si no cambio de estado, termino. El personaje ya tendrá el comportamiento de este estado cuando
				// he cambiado a él.
				return;
			}
			
		} else if (this.stateMachine.isInState(this.goToMyManantial)) {
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "goToMyManantial");
			
			// Estamos en el estado -> Ir al manantial (me queda poca vida)
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.haveIDead(source)) {
				// Si he muerto, cambio al estado -> Estoy muerto
				this.stateMachine.changeState(this.iAmDead);
				
			} else if (Checks.amIInMyManantial(source)) {
				// Si estoy en mi manantial, cambio al estado -> Curarme
				this.stateMachine.changeState(this.cureMe);
			
			} 
			// Si no cambio de estado, no termino, ya que hay seguir aplicando el pathfinding
			// creado al entrar a este estado.
			
		} else if (this.stateMachine.isInState(this.cureMe)) {
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "cureMe");
			
			// Estamos en el estado -> Curarme (estoy en el manantial)
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.haveIFullyRecoveredMyHealth(source)) {
				// Si ha recuperado toda mi vida, dos posibilidades:
				
				if (Checks.haveIGotWayPoint(source)) {
					// Si tengo un waypoint, cambio al estado -> Ir a mi Waypoint
					this.stateMachine.changeState(this.goToMyWayPoint);
					
				} else {
					// Si no tengo waypoint, cambio al estado -> Reservar waypoint
					this.stateMachine.changeState(this.bookWaypoint);
				}
			} else {
				// Si no cambio de estado, termino. El personaje ya tendrá el comportamiento de este estado cuando
				// he cambiado a él.
				return;
			}
		} 
		
		// Actualizamos la máquina de estados -> Se llama el método 'update' del estado actual de la máquina
		this.stateMachine.update();
	}
}
