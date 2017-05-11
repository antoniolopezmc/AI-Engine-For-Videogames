package com.mygdx.iadevproject.aiTactical.roles;

import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.map.Ground;

public interface TacticalRole {

	public float getVelocityFactor(Ground ground);
	public int getTacticalCost(Ground ground);
	public void initialize(Character source);
	public void update(Character source); // Puede recibir un Map<Float, Behaviour>()
	
	
	// TODO NOTA PARA EL FUTURO.
	// Cuando dejan de atacarme, AL SALIR de ese estado, previousHealth se le asigna currentHeath para que la comprobación de si me están atando no siga saltando.
	// Esto solo se hace en aquellos roles en los que se use ese check (en lo defensivos no se usa).
}
