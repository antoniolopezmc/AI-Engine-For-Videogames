package com.mygdx.iadevproject.aiTactical.roles.decisionTree.Node;

import com.mygdx.iadevproject.model.Character;

public interface Node {

	public void update(Character entity);
	public void enter(Character entity);
	public void exit(Character entity);
}
