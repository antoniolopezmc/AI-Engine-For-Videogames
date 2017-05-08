package com.mygdx.iadevproject.aiTactical.roles;

import java.util.Map;

import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.map.Ground;

public class OffensiveSoldier extends Soldier {

	public OffensiveSoldier() {
		// TODO Auto-generated constructor stub
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
	public Map<Float, Behaviour> initialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Float, Behaviour> update() {
		// TODO Auto-generated method stub
		return null;
	}

}
