package com.mygdx.iadevproject.aiTactical.roles;

import java.util.Map;

import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.map.Ground;

public interface TacticalRole {

	public float getVelocityFactor(Ground ground);
	public int getTacticalCost(Ground ground);
	public Map<Float, Behaviour> initialize();
	public Map<Float, Behaviour> update(); // Puede recibir un Map<Float, Behaviour>()
	
}
