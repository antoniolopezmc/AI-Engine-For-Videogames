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
import com.mygdx.iadevproject.map.Ground;
import com.mygdx.iadevproject.model.Character;

public class DefensiveArcher extends Archer {

	private DefaultStateMachine<Character, State<Character>> stateMachine;
	
	// Estados de la máquina de estados. Están como variables porque la máquina de estados,
	// cuando comprueba si está en un estado, la comprobación no es con el 'equals', si no 
	// que utiliza el '==', por lo que es necesario tener el mismo objeto para hacer la comprobación.
	private PatrolMyBase patrolMyBase;
	private AttackEnemies attackEnemies;
	private CureMe cureMe;
	private GoToMyBase goToMyBase;
	private IAmDead iAmDead;
	private GoToMyManantial goToMyManantial;
	
	public DefensiveArcher() {
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
	public float getVelocityFactor(Ground ground) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTacticalCost(Ground ground) {
		// TODO Auto-generated method stub
		return 0;
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
			
			if (Checks.areThereEnemiesNear(source)) {
				
			}
			
		} else if (this.stateMachine.isInState(this.attackEnemies)) { 
			// Estamos en el estado -> Atacar a los enemigos 
			
		} else if (this.stateMachine.isInState(this.iAmDead)) {
			// Estamos en el estado -> Estoy muerto
		
		} else if (this.stateMachine.isInState(this.goToMyManantial)) {
			// Estamos en el estado -> Me queda poca vida (ir al manantial)
			
		} else if (this.stateMachine.isInState(this.cureMe)) {
			// Estamos en el estado -> Curarme (estoy en el manantial)
			
		} else if (this.stateMachine.isInState(this.goToMyBase)) {
			// Estamos en el estado -> Ir a mi base
			
		}
		
		// Actualizamos la máquina de estados -> Se llama el método 'update' del estado actual de la máquina
		this.stateMachine.update();
	}

}
