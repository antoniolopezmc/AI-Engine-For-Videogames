package com.mygdx.iadevproject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiTactical.roles.DefensiveArcher;
import com.mygdx.iadevproject.aiTactical.roles.DefensiveSoldier;
import com.mygdx.iadevproject.aiTactical.roles.OffensiveArcher;
import com.mygdx.iadevproject.aiTactical.roles.OffensiveSoldier;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Team;

/**
 * Clase estática que crea todos los personajes del juego. 
 *
 */
public class CreateCharacters {

	// Equipo FJAVIER (equipo abajo)
	private static Character FJsoldier1, FJsoldier2;
	private static Character FJarcher1, FJarcher2, FJarcher3, FJarcher4, FJarcher5, FJarcher6, FJarcher7;
	private static Character FJsoldier3, FJsoldier4;
	private static Character FJarcher8, FJarcher9;
	
	// Equipo LDANIEL (equipo arriba)
	private static Character LDsoldier1, LDsoldier2;
	private static Character LDarcher1, LDarcher2, LDarcher3, LDarcher4, LDarcher5, LDarcher6, LDarcher7;
	private static Character LDsoldier3, LDsoldier4;
	private static Character LDarcher8, LDarcher9;
		
	private static List<Character> FJTeam, LDTeam;
	
	public static void createCharacters() {		   
		// Equipo FJAVIER (abajo)
		
		// 2 soldados defensivos.
		FJsoldier1 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-soldier.png")));
        FJsoldier1.setBounds(1899.2f, 74.8f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJsoldier1.setOrientation(60.0f);
        FJsoldier1.setVelocity(new Vector3(0,0.0f,0));
        FJsoldier1.setMaxSpeed(50.0f);
        FJsoldier1.setTeam(Team.FJAVIER);
        FJsoldier1.initializeTacticalRole(new DefensiveSoldier());
        
        FJsoldier2 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-soldier.png")));
        FJsoldier2.setBounds(1943.9f,245.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJsoldier2.setOrientation(60.0f);
        FJsoldier2.setVelocity(new Vector3(0,0.0f,0));
        FJsoldier2.setMaxSpeed(50.0f);
        FJsoldier2.setTeam(Team.FJAVIER);
        FJsoldier2.initializeTacticalRole(new DefensiveSoldier());
        
        // 2 soldados ofensivos.
		FJsoldier3 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-soldier.png")));
		FJsoldier3.setBounds(1899.2f, 74.8f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
		FJsoldier3.setOrientation(60.0f);
		FJsoldier3.setVelocity(new Vector3(0,0.0f,0));
		FJsoldier3.setTeam(Team.FJAVIER);
		FJsoldier3.initializeTacticalRole(new OffensiveSoldier());
		FJsoldier3.setMaxSpeed(FJsoldier3.getRole().getMaxSpeed());
		        
		FJsoldier4 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-soldier.png")));
		FJsoldier4.setBounds(1943.9f,245.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
		FJsoldier4.setOrientation(60.0f);
		FJsoldier4.setVelocity(new Vector3(0,0.0f,0));
		FJsoldier4.setTeam(Team.FJAVIER);
		FJsoldier4.initializeTacticalRole(new OffensiveSoldier());
		FJsoldier4.setMaxSpeed(FJsoldier4.getRole().getMaxSpeed());
        
        // 7 arqueros defensivos (solo 6 conseguirán un waypoint del puente).
        FJarcher1 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher1.setBounds(1706.5f,66.2f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher1.setOrientation(60.0f);
        FJarcher1.setVelocity(new Vector3(0,0.0f,0));
        FJarcher1.setTeam(Team.FJAVIER);
        FJarcher1.initializeTacticalRole(new DefensiveArcher());
        FJarcher1.setMaxSpeed(FJarcher1.getRole().getMaxSpeed());
        
        FJarcher2 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher2.setBounds(1691.0f,148.7f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher2.setOrientation(60.0f);
        FJarcher2.setVelocity(new Vector3(0,0.0f,0));
        FJarcher2.setTeam(Team.FJAVIER);
        FJarcher2.initializeTacticalRole(new DefensiveArcher());
        FJarcher2.setMaxSpeed(FJarcher2.getRole().getMaxSpeed());
        
        FJarcher3 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher3.setBounds(1697.9f,260.5f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher3.setOrientation(60.0f);
        FJarcher3.setVelocity(new Vector3(0,0.0f,0));
        FJarcher3.setTeam(Team.FJAVIER);
        FJarcher3.initializeTacticalRole(new DefensiveArcher());
        FJarcher3.setMaxSpeed(FJarcher3.getRole().getMaxSpeed());
        
        FJarcher4 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher4.setBounds(1720.3f,351.71f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher4.setOrientation(60.0f);
        FJarcher4.setVelocity(new Vector3(0,0.0f,0));
        FJarcher4.setTeam(Team.FJAVIER);
        FJarcher4.initializeTacticalRole(new DefensiveArcher());
        FJarcher4.setMaxSpeed(FJarcher4.getRole().getMaxSpeed());
        
        FJarcher5 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher5.setBounds(1799.4f,392.9f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher5.setOrientation(60.0f);
        FJarcher5.setVelocity(new Vector3(0,0.0f,0));
        FJarcher5.setTeam(Team.FJAVIER);
        FJarcher5.initializeTacticalRole(new DefensiveArcher());
        FJarcher5.setMaxSpeed(FJarcher5.getRole().getMaxSpeed());
        
        FJarcher6 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher6.setBounds(1937.0f,427.3f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher6.setOrientation(60.0f);
        FJarcher6.setVelocity(new Vector3(0,0.0f,0));
        FJarcher6.setTeam(Team.FJAVIER);
        FJarcher6.initializeTacticalRole(new DefensiveArcher());
        FJarcher6.setMaxSpeed(FJarcher6.getRole().getMaxSpeed());

        FJarcher7 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher7.setBounds(1600.0f,600.3f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher7.setOrientation(60.0f);
        FJarcher7.setVelocity(new Vector3(0,0.0f,0));
        FJarcher7.setTeam(Team.FJAVIER);
        FJarcher7.initializeTacticalRole(new DefensiveArcher());
        FJarcher7.setMaxSpeed(FJarcher7.getRole().getMaxSpeed());
        
        // 2 arqueros ofensivos.
        FJarcher8 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher8.setBounds(1937.0f,427.3f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher8.setOrientation(60.0f);
        FJarcher8.setVelocity(new Vector3(0,0.0f,0));
        FJarcher8.setTeam(Team.FJAVIER);
        FJarcher8.initializeTacticalRole(new OffensiveArcher());
        FJarcher8.setMaxSpeed(FJarcher8.getRole().getMaxSpeed());

        FJarcher9 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher9.setBounds(1600.0f,600.3f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher9.setOrientation(60.0f);
        FJarcher9.setVelocity(new Vector3(0,0.0f,0));
        FJarcher9.setTeam(Team.FJAVIER);
        FJarcher9.initializeTacticalRole(new OffensiveArcher());
        FJarcher9.setMaxSpeed(FJarcher9.getRole().getMaxSpeed());
        
        FJTeam = new LinkedList<Character>();
        FJTeam.addAll(Arrays.asList(FJsoldier1, FJsoldier2, FJsoldier3, FJsoldier4, FJarcher1, FJarcher2, FJarcher3, FJarcher4, FJarcher5, FJarcher6, FJarcher7, FJarcher8, FJarcher9));
        
        // Equipo LDANIEL (arriba)
        
        // 2 soldados defensivos
        LDsoldier1 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-soldier.png")));
        LDsoldier1.setBounds(61.64f,1836.8f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDsoldier1.setOrientation(60.0f);
        LDsoldier1.setVelocity(new Vector3(0,0.0f,0));
        LDsoldier1.setMaxSpeed(50.0f);
        LDsoldier1.setTeam(Team.LDANIEL);
        LDsoldier1.initializeTacticalRole(new DefensiveSoldier());
        
        LDsoldier2 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-soldier.png")));
        LDsoldier2.setBounds(199.34f,1958.3f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDsoldier2.setOrientation(60.0f);
        LDsoldier2.setVelocity(new Vector3(0,0.0f,0));
        LDsoldier2.setMaxSpeed(50.0f);
        LDsoldier2.setTeam(Team.LDANIEL);
        LDsoldier2.initializeTacticalRole(new DefensiveSoldier());
        
        // 2 soldados ofensivos.
        LDsoldier3 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-soldier.png")));
        LDsoldier3.setBounds(61.64f,1836.8f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDsoldier3.setOrientation(60.0f);
        LDsoldier3.setVelocity(new Vector3(0,0.0f,0));
        LDsoldier3.setTeam(Team.LDANIEL);
        LDsoldier3.initializeTacticalRole(new OffensiveSoldier());
        LDsoldier3.setMaxSpeed(LDsoldier3.getRole().getMaxSpeed());
        
        LDsoldier4 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-soldier.png")));
        LDsoldier4.setBounds(199.34f,1958.3f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDsoldier4.setOrientation(60.0f);
        LDsoldier4.setVelocity(new Vector3(0,0.0f,0));
        LDsoldier4.setTeam(Team.LDANIEL);
        LDsoldier4.initializeTacticalRole(new OffensiveSoldier());   
        LDsoldier4.setMaxSpeed(LDsoldier4.getRole().getMaxSpeed());
        
        // 7 arqueros defensivos (solo 6 conseguirán un waypoint del puente).
        LDarcher1 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher1.setBounds(396.98f,1971.26f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher1.setOrientation(60.0f);
        LDarcher1.setVelocity(new Vector3(0,0.0f,0));
        LDarcher1.setTeam(Team.LDANIEL);
        LDarcher1.initializeTacticalRole(new DefensiveArcher());
        LDarcher1.setMaxSpeed(LDarcher1.getRole().getMaxSpeed());
        
        LDarcher2 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher2.setBounds(395.360f,1859.48f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher2.setOrientation(60.0f);
        LDarcher2.setVelocity(new Vector3(0,0.0f,0));
        LDarcher2.setTeam(Team.LDANIEL);
        LDarcher2.initializeTacticalRole(new DefensiveArcher());
        LDarcher2.setMaxSpeed(LDarcher2.getRole().getMaxSpeed());
        
        LDarcher3 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher3.setBounds(398.6f,1742.8f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher3.setOrientation(60.0f);
        LDarcher3.setVelocity(new Vector3(0,0.0f,0));
        LDarcher3.setTeam(Team.LDANIEL);
        LDarcher3.initializeTacticalRole(new DefensiveArcher());
        LDarcher3.setMaxSpeed(LDarcher3.getRole().getMaxSpeed());
        
        LDarcher4 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher4.setBounds(278.7f,1699.1f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher4.setOrientation(60.0f);
        LDarcher4.setVelocity(new Vector3(0,0.0f,0));
        LDarcher4.setTeam(Team.LDANIEL);
        LDarcher4.initializeTacticalRole(new DefensiveArcher());
        LDarcher4.setMaxSpeed(LDarcher4.getRole().getMaxSpeed());
        
        LDarcher5 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher5.setBounds(178.2f,1635.9f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher5.setOrientation(60.0f);
        LDarcher5.setVelocity(new Vector3(0,0.0f,0));
        LDarcher5.setTeam(Team.LDANIEL);
        LDarcher5.initializeTacticalRole(new DefensiveArcher());
        LDarcher5.setMaxSpeed(LDarcher5.getRole().getMaxSpeed());
        
        LDarcher6 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher6.setBounds(213.9f,1825.4f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher6.setOrientation(60.0f);
        LDarcher6.setVelocity(new Vector3(0,0.0f,0));
        LDarcher6.setTeam(Team.LDANIEL);
        LDarcher6.initializeTacticalRole(new DefensiveArcher());
        LDarcher6.setMaxSpeed(LDarcher6.getRole().getMaxSpeed());
        
        LDarcher7 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher7.setBounds(600.9f,1525.4f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher7.setOrientation(60.0f);
        LDarcher7.setVelocity(new Vector3(0,0.0f,0));
        LDarcher7.setTeam(Team.LDANIEL);
        LDarcher7.initializeTacticalRole(new DefensiveArcher());
        LDarcher7.setMaxSpeed(LDarcher7.getRole().getMaxSpeed());
        
        // 2 arqueros ofensivos.
        LDarcher8 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher8.setBounds(213.9f,1825.4f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher8.setOrientation(60.0f);
        LDarcher8.setVelocity(new Vector3(0,0.0f,0));
        LDarcher8.setTeam(Team.LDANIEL);
        LDarcher8.initializeTacticalRole(new OffensiveArcher());
        LDarcher8.setMaxSpeed(LDarcher8.getRole().getMaxSpeed());
        
        LDarcher9 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher9.setBounds(600.9f,1525.4f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher9.setOrientation(60.0f);
        LDarcher9.setVelocity(new Vector3(0,0.0f,0));
        LDarcher9.setTeam(Team.LDANIEL);
        LDarcher9.initializeTacticalRole(new OffensiveArcher());
        LDarcher9.setMaxSpeed(LDarcher9.getRole().getMaxSpeed());
        
        LDTeam = new LinkedList<Character>();
        LDTeam.addAll(Arrays.asList(LDsoldier1, LDsoldier2, LDsoldier3, LDsoldier4, LDarcher1, LDarcher2, LDarcher3, LDarcher4, LDarcher5, LDarcher6, LDarcher7, LDarcher8, LDarcher9));
        
        for (Character character : FJTeam) {
			IADeVProject.addToWorldObjectList(character);
		}
        
        for (Character character : LDTeam) {
        	IADeVProject.addToWorldObjectList(character);
		}
	}
}
