package com.mygdx.iadevproject.aiTactical.roles;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.mygdx.iadevproject.aiTactical.roles.states.AttackEnemies;
import com.mygdx.iadevproject.aiTactical.roles.states.CureMe;
import com.mygdx.iadevproject.aiTactical.roles.states.GoToMyBase;
import com.mygdx.iadevproject.aiTactical.roles.states.GoToMyManantial;
import com.mygdx.iadevproject.aiTactical.roles.states.IAmDead;
import com.mygdx.iadevproject.aiTactical.roles.states.PatrolMyBase;
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

	}

	@Override
	public void update(Character source) {
		
	}

}
