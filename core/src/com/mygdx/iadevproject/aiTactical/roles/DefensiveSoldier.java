package com.mygdx.iadevproject.aiTactical.roles;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.mygdx.iadevproject.aiTactical.roles.states.AttackEnemies;
import com.mygdx.iadevproject.aiTactical.roles.states.CureMe;
import com.mygdx.iadevproject.aiTactical.roles.states.GoToMyBase;
import com.mygdx.iadevproject.aiTactical.roles.states.GoToMyManantial;
import com.mygdx.iadevproject.aiTactical.roles.states.IAmDead;
import com.mygdx.iadevproject.aiTactical.roles.states.PatrolMyBase;
import com.mygdx.iadevproject.checksAndActions.Checks;
import com.mygdx.iadevproject.model.Character;

public class DefensiveSoldier extends Soldier {

	private DefaultStateMachine<Character, State<Character>> stateMachine;
	
	// Estados de la máquina de estados. Están como variables porque la máquina de estados,
	// cuando comprueba si está en un estado, la comprobación no es con el 'equals', si no 
	// que utiliza el '==', por lo que es necesario tener el mismo objeto para hacer la comprobación.
	private PatrolMyBase 		patrolMyBase;
	private AttackEnemies 		attackEnemies;
	private CureMe 				cureMe;
	private GoToMyBase 			goToMyBase;
	private IAmDead 			iAmDead;
	private GoToMyManantial 	goToMyManantial;
	
	public DefensiveSoldier() {
		// Establecemos a null la máquina de estados porque se ha de crear cuando se utilice
		// el método 'initialize()'
		this.stateMachine = null; 
		
		// Creamos los estados de la máquina de estados.
		this.patrolMyBase 		= new PatrolMyBase();
		this.attackEnemies 		= new AttackEnemies();
		this.cureMe 			= new CureMe();
		this.goToMyBase 		= new GoToMyBase();
		this.iAmDead 			= new IAmDead();
		this.goToMyManantial 	= new GoToMyManantial();
	}

	@Override
	public void initialize(Character source) {
		// Si la máquina de estados no ha sido inicializada, la creamos y actualizamos al estado inicial
		if (this.stateMachine == null) {
			// Creamos la máquina de estados con el personaje pasado como parámetro
			this.stateMachine = new DefaultStateMachine<Character, State<Character>>(source);
			// Establecemos el estado inicial como el patrullar la base.
			this.stateMachine.setInitialState(this.patrolMyBase);
			// Actualizamos la máquina de estados.
			this.stateMachine.update();
		}
	}

	@Override
	public void update(Character source) {
		if (this.stateMachine.isInState(this.patrolMyBase)) {
			// Estamos en el estado -> Patrullar la base (estoy en mi base)
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.areThereEnemiesNear(source) && !Checks.amIFarFromMyBase(source)) {
				// Si hay enemigos cerca Y no estoy lejos de mi base, cambio al estado -> Atacar a los enemigos
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
			// Estamos en el estado -> Atacar a los enemigos 
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.amIFarFromMyBase(source) || !Checks.areThereEnemiesNear(source)) { 
				// Si no hay enemigos cerca o estoy lejos de mi base, cambio al estado -> Patrullar la base 
				this.stateMachine.changeState(this.patrolMyBase);
				
			} else if (Checks.haveIDead(source)) {
				// Si el personaje ha muerto, cambio al estado -> Estoy muerto
				this.stateMachine.changeState(this.iAmDead);
			
			} 
			// Si no cambio de estado, no termino, ya que hay que calcular el enemigo más cercano 
			// a atacar (este puede haber cambiado).
			
		} else if (this.stateMachine.isInState(this.iAmDead)) {
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
			// Estamos en el estado -> Curarme (estoy en el manantial)
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.haveIFullyRecoveredMyHealth(source)) {
				// Si ha recuperado toda mi vida, cambio al estado -> Ir a mi base
				this.stateMachine.changeState(this.goToMyBase);
			
			} else {
				// Si no cambio de estado, termino. El personaje ya tendrá el comportamiento de este estado cuando
				// he cambiado a él.
				return;
			}
			
		} else if (this.stateMachine.isInState(this.goToMyBase)) {
			// Estamos en el estado -> Ir a mi base
			
			// Solo cambio de estado, si ocurre lo siguiente:
			if (Checks.haveILittleHealth(source)) {
				// Si me queda poca vida, cambio al estado -> Ir al manantial (me queda poca vida)
				this.stateMachine.changeState(this.goToMyManantial);
			
			} else if (Checks.amIInMyBase(source)) {
				// Si estoy en mi base, cambio al estado -> Patrullar mi base
				this.stateMachine.changeState(this.patrolMyBase);
			} 
			// Si no cambio de estado, no termino, ya que hay seguir aplicando el pathfinding
			// creado al entrar a este estado.
		}
		
		// Actualizamos la máquina de estados -> Se llama el método 'update' del estado actual de la máquina
		this.stateMachine.update();
	}

}
