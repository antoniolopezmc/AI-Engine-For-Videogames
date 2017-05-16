package com.mygdx.iadevproject.aiTactical.roles;

import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.AttackEnemies;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.CureMe;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.GoToEnemyBase;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.GoToMyManantial;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.IAmDead;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.Node;
import com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node.Win;
import com.mygdx.iadevproject.checksAndActions.Checks;
import com.mygdx.iadevproject.model.Character;

public class OffensiveArcher extends Archer {

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
	
	public OffensiveArcher() {
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
		if (Checks.haveIDead(source)) { // NODO IAmDead.
			// *****
			if (lastNode != iAmDead) {
				if (lastNode != null) {
					lastNode.exit(source);
				}
				lastNode = iAmDead;
				lastNode.enter(source);
			}
			if (lastNode != null) {
				lastNode.update(source);
			}
			
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "IAmDead");
			
			// *****
		} else if (Checks.haveILittleHealth(source)) {
			if (!Checks.amIInMyManantial(source)) { // NODO GoToMyManantial
				// *****
				if (lastNode != goToMyManantial) {
					if (lastNode != null) {
						lastNode.exit(source);
					}
					lastNode = goToMyManantial;
					lastNode.enter(source);
				}
				if (lastNode != null) {
					lastNode.update(source);
				}
				
				// Imprimos el estado táctico del source.
				source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "GoToMyManantial");
				
				// *****
			} else if (Checks.amIInMyManantial(source)) { // NODO CureMe
				// *****
				if (lastNode != cureMe) {
					if (lastNode != null) {
						lastNode.exit(source);
					}
					lastNode = cureMe;
					lastNode.enter(source);
				}
				if (lastNode != null) {
					lastNode.update(source);
				}
				
				// Imprimos el estado táctico del source.
				source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "CureMe");
				
				// *****
			}
		} else if (Checks.amIInMyManantial(source)) {
			if (!Checks.haveIFullyRecoveredMyHealth(source)) { // NODO CureMe
				// *****
				if (lastNode != cureMe) {
					if (lastNode != null) {
						lastNode.exit(source);
					}
					lastNode = cureMe;
					lastNode.enter(source);
				}
				if (lastNode != null) {
					lastNode.update(source);
				}
				
				// Imprimos el estado táctico del source.
				source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "CureMe");
				
				// *****
			} else if (Checks.haveIFullyRecoveredMyHealth(source)) { // NODO GoToEnemyBase
				// *****
				if (lastNode != goToEnemyBase) {
					if (lastNode != null) {
						lastNode.exit(source);
					}
					lastNode = goToEnemyBase;
					lastNode.enter(source);
				}
				if (lastNode != null) {
					lastNode.update(source);
				}
				
				// Imprimos el estado táctico del source.
				source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "GoToEnemyBase");
				
				// *****
			}
		} else if (Checks.areThereEnemiesNear(source)) { // NODO AttackEnemies
			// *****
			if (lastNode != attackEnemies) {
				if (lastNode != null) {
					lastNode.exit(source);
				}
				lastNode = attackEnemies;
				lastNode.enter(source);
			}
			if (lastNode != null) {
				lastNode.update(source);
			}
			
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "AttackEnemies");
			
			// *****
		} else if (Checks.amIInEnemyBase(source)) { 
			if (Checks.haveIWin(source)) { // NODO Win
				// *****
				if (lastNode != win) {
					if (lastNode != null) {
						lastNode.exit(source);
					}
					lastNode = win;
					lastNode.enter(source);
				}
				if (lastNode != null) { 
					lastNode.update(source);
				}
				
				// Imprimos el estado táctico del source.
				source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "Win");
				
				// *****	
			} else if (!Checks.haveIWin(source)) { // NODO GoToEnemyBase
				// *****
				if (lastNode != goToEnemyBase) {
					if (lastNode != null) {
						lastNode.exit(source);
					}
					lastNode = goToEnemyBase;
					lastNode.enter(source);
				}
				if (lastNode != null) {
					lastNode.update(source);
				}
				
				// Imprimos el estado táctico del source.
				source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "GoToEnemyBase");
				
				// *****
			}
		} else if (!Checks.amIInEnemyBase(source)) { // Nodo GoToEnemyBase.
			// *****
			if (lastNode != goToEnemyBase) {
				if (lastNode != null) {
					lastNode.exit(source);
				}
				lastNode = goToEnemyBase;
				lastNode.enter(source);
			}
			if (lastNode != null) {
				lastNode.update(source);
			}
			
			// Imprimos el estado táctico del source.
			source.drawTalticalState(IADeVProject.batch, IADeVProject.font, "GoToEnemyBase");
			
			// *****
		}
	}

}
