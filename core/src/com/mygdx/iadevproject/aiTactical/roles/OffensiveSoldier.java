package com.mygdx.iadevproject.aiTactical.roles;

import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.AttackEnemies;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.CureMe;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.GoToEnemyBase;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.GoToMyManantial;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.IAmDead;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.Node;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.Win;
import com.mygdx.iadevproject.map.Ground;
import com.mygdx.iadevproject.model.Character;

public class OffensiveSoldier extends Soldier {

	// Para el caso de los roles ofensivos, el árbol de decisión será el mismo para los arqueros y para los soldados.
	// 	Ya que, al fin y cabo, un personaje ofensivo debe ir a la base contraria y reventar todo lo que se le ponga por delantes. Aquí no hay distinción.
	
	// Último nodo al que hemos accedido.
	private Node lastNode;
	
	// Todos los nodos del árbol.
	private IAmDead iAmDead;
	private GoToMyManantial goToMyManantial;
	private CureMe cureMe;
	private GoToEnemyBase goToEnemyBase;
	private AttackEnemies attackEnemies;
	private Win win;
	
	public OffensiveSoldier() {
		lastNode = null;
		
		iAmDead = new IAmDead();
		goToMyManantial = new GoToMyManantial();
		cureMe = new CureMe();
		goToEnemyBase = new GoToEnemyBase();
		attackEnemies = new AttackEnemies();
		win = new Win();
	}

	@Override
	public void initialize(Character source) {
		// Los árboles de decisión no necesitan inicialización.
		
	}

	@Override
	public void update(Character source) {
		// El árbol se va a implementar como una serie de REGLAS.
		
	}

}
