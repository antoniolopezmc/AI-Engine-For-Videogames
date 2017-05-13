package com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node;

import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.model.Character;

public class Win implements Node {

	@Override
	public void update(Character entity) {
		IADeVProject.setWinner(entity.getTeam());
		
	}

	@Override
	public void enter(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub
		
	}

}
