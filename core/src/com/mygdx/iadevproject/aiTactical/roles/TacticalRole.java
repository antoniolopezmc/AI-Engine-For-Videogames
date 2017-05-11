package com.mygdx.iadevproject.aiTactical.roles;

import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.map.Ground;

public interface TacticalRole {
	
	/**
	 * Todas las unidades, independientemente de su tipo o rol, recuperarán la vida al mismo ritmo cuando
	 * se estén curando. Este valor es una constacte para todo el mundo.
	 */
	public static final float health_cure = 50.0f;

	/**
	 * Devuelve el factor de velocidad de un terreno.
	 * @param ground Terreno de entrada.
	 * @return factor de velocidad asociado a este terreno en el rol.
	 */
	// Cada rol se moverá a distinta velocidad según que terreno.
	// El factor devuelto (entre 0 y 1) se usará en el método update del personaje para ralentizarlo.
	public float getVelocityFactor(Ground ground);
	/**
	 * Devuelve el coste táctico del terreno pasado como parámetro.
	 * @param ground Terreno de entrada.
	 * @return Devuelve el coste táctico del terreno que se pasa como parámetro.
	 */
	public int getTacticalCost(Ground ground);
	/**
	 * Distancia máxima a la que un personaje con este rol puede atacar.
	 * @return distancia máxima. 
	 */
	public float getMaxDistanceOfAttack();
	/**
	 * Daño que un personaje de este rol provoca al atacar a otro personaje.
	 * @return daño que se provocará.
	 */
	public float getDamageToDone();
	/**
	 * Método que inicializa la estructura táctica usada.
	 * @param source Personaje de entrada (es necesario para poder tener acceso a su lista de comportamientos y poder modificarla).
	 */
	public void initialize(Character source);
	/**
	 * Método que actualiza/llama/ejecuta la estructura táctica usada.
	 * @param source Personaje de entrada (es necesario para poder tener acceso a su lista de comportamientos y poder modificarla).
	 */
	public void update(Character source); // Puede recibir un Map<Float, Behaviour>()
	
	
	// TODO NOTA PARA EL FUTURO.
	// Cuando dejan de atacarme, AL SALIR de ese estado, previousHealth se le asigna currentHeath para que la comprobación de si me están atando no siga saltando.
	// Esto solo se hace en aquellos roles en los que se use ese check (en lo defensivos no se usa).
}
