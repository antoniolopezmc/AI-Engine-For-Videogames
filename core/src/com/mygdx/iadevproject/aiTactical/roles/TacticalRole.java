package com.mygdx.iadevproject.aiTactical.roles;

import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.map.Ground;

public interface TacticalRole {

	public float getVelocityFactor(Ground ground);
	public int getTacticalCost(Ground ground);
	public void initialize(Character source);
	public void update(Character source); // Puede recibir un Map<Float, Behaviour>()
	
}
