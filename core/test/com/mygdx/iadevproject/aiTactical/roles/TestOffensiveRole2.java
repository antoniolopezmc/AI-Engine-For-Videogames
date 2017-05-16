package com.mygdx.iadevproject.aiTactical.roles;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.checksAndActions.MoralPoints;
import com.mygdx.iadevproject.mapOfInfluence.SimpleMapOfInfluence;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.userInteraction.InputProcessorIADeVProject;
import com.mygdx.iadevproject.waypoints.Waypoints;

public class TestOffensiveRole2 extends ApplicationAdapter {
	
	public static OrthographicCamera camera;				// Cámara (es pública para que se pueda acceder el InputProcessorIADeVProject)
	public static boolean PRINT_PATH_BEHAVIOUR; 			// Dibujar el camino/recorrido obtenido por la función getSteering de los Behaviours.
	public static BitmapFont font; 							// Para dibujar letras 
	public static SpriteBatch batch; 						// Para dibujar letras hace falta tanto un font como un batch.
	public static ShapeRenderer renderer; 					// Para dibujar líneas
	public static TiledMap tiledMap;												// Mapa real de los dibujitos
	public static TiledMapRenderer tiledMapRenderer;
	public static InputProcessorIADeVProject inputProcessor;		// InputProcessor
	
	public static Character drop;
	
	// Equipo FJAVIER (equipo abajo)
	private static Character FJsoldier1, FJsoldier2;
	private static Character FJarcher1, FJarcher2, FJarcher3, FJarcher4, FJarcher5, FJarcher6, FJarcher7;
	
	// Equipo LDANIEL (equipo arriba)
	private static Character LDsoldier1, LDsoldier2;
	private static Character LDarcher1, LDarcher2, LDarcher3, LDarcher4, LDarcher5, LDarcher6, LDarcher7;
		
	private static List<Character> FJTeam, LDTeam;
	
	@Override
	public void create() {		
		IADeVProject iadevproject = new IADeVProject();
		iadevproject.create();
		
		IADeVProject.PRINT_PATH_BEHAVIOUR = true;
		// Inicializamos las estructuras para el manejo de los waypoints de los puentes.
        Waypoints.initializeBridgesWaypoints();
		
		camera = IADeVProject.camera;
		font = IADeVProject.font;
		batch = IADeVProject.batch;
		renderer = IADeVProject.renderer;
		tiledMap = IADeVProject.tiledMap;
		tiledMapRenderer = IADeVProject.tiledMapRenderer;
		inputProcessor = IADeVProject.inputProcessor;	
		
		// Limpiamos los objetos del IADeVProject para utilizar solamnte los que se crean en esta clase
		// y le añadimos los obstáculos del mundo.
		IADeVProject.worldObjects.clear();
		IADeVProject.worldObjects.addAll(IADeVProject.worldObstacles);
		
        drop = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
    	drop.setBounds(600.0f, 600.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
    	drop.setOrientation(60.0f);
        drop.setVelocity(new Vector3(0,0.0f,0));
        drop.setMaxSpeed(50.0f);
        drop.setTeam(Team.FJAVIER);

   
        // Equipo FJAVIER (abajo)
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
        
        FJarcher1 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher1.setBounds(1706.5f,66.2f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher1.setOrientation(60.0f);
        FJarcher1.setVelocity(new Vector3(0,0.0f,0));
        FJarcher1.setMaxSpeed(50.0f);
        FJarcher1.setTeam(Team.FJAVIER);
        FJarcher1.initializeTacticalRole(new DefensiveArcher());
        
        FJarcher2 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher2.setBounds(1691.0f,148.7f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher2.setOrientation(60.0f);
        FJarcher2.setVelocity(new Vector3(0,0.0f,0));
        FJarcher2.setMaxSpeed(50.0f);
        FJarcher2.setTeam(Team.FJAVIER);
        FJarcher2.initializeTacticalRole(new DefensiveArcher());
        
        FJarcher3 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher3.setBounds(1697.9f,260.5f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher3.setOrientation(60.0f);
        FJarcher3.setVelocity(new Vector3(0,0.0f,0));
        FJarcher3.setMaxSpeed(50.0f);
        FJarcher3.setTeam(Team.FJAVIER);
        FJarcher3.initializeTacticalRole(new DefensiveArcher());
        
        FJarcher4 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher4.setBounds(1720.3f,351.71f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher4.setOrientation(60.0f);
        FJarcher4.setVelocity(new Vector3(0,0.0f,0));
        FJarcher4.setMaxSpeed(50.0f);
        FJarcher4.setTeam(Team.FJAVIER);
        FJarcher4.initializeTacticalRole(new DefensiveArcher());
        
        FJarcher5 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher5.setBounds(1799.4f,392.9f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher5.setOrientation(60.0f);
        FJarcher5.setVelocity(new Vector3(0,0.0f,0));
        FJarcher5.setMaxSpeed(50.0f);
        FJarcher5.setTeam(Team.FJAVIER);
        FJarcher5.initializeTacticalRole(new DefensiveArcher());
        
        FJarcher6 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher6.setBounds(1937.0f,427.3f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher6.setOrientation(60.0f);
        FJarcher6.setVelocity(new Vector3(0,0.0f,0));
        FJarcher6.setMaxSpeed(50.0f);
        FJarcher6.setTeam(Team.FJAVIER);
        FJarcher6.initializeTacticalRole(new DefensiveArcher());

        FJarcher7 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket-archer.png")));
        FJarcher7.setBounds(1600.0f,600.3f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        FJarcher7.setOrientation(60.0f);
        FJarcher7.setVelocity(new Vector3(0,0.0f,0));
        FJarcher7.setMaxSpeed(50.0f);
        FJarcher7.setTeam(Team.FJAVIER);
        FJarcher7.initializeTacticalRole(new OffensiveArcher());
        
        FJTeam = new LinkedList<Character>();
        FJTeam.addAll(Arrays.asList(FJsoldier1, FJsoldier2, FJarcher1, FJarcher2, FJarcher3, FJarcher4, FJarcher5, FJarcher6, FJarcher7));
        
        
        
        // Equipo LDANIEL (arriba)
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
        
        LDarcher1 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher1.setBounds(396.98f,1971.26f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher1.setOrientation(60.0f);
        LDarcher1.setVelocity(new Vector3(0,0.0f,0));
        LDarcher1.setMaxSpeed(50.0f);
        LDarcher1.setTeam(Team.LDANIEL);
        LDarcher1.initializeTacticalRole(new OffensiveArcher());
        
        LDarcher2 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher2.setBounds(395.360f,1859.48f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher2.setOrientation(60.0f);
        LDarcher2.setVelocity(new Vector3(0,0.0f,0));
        LDarcher2.setMaxSpeed(50.0f);
        LDarcher2.setTeam(Team.LDANIEL);
        LDarcher2.initializeTacticalRole(new DefensiveArcher());
        
        LDarcher3 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher3.setBounds(398.6f,1742.8f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher3.setOrientation(60.0f);
        LDarcher3.setVelocity(new Vector3(0,0.0f,0));
        LDarcher3.setMaxSpeed(50.0f);
        LDarcher3.setTeam(Team.LDANIEL);
        LDarcher3.initializeTacticalRole(new DefensiveArcher());
        
        LDarcher4 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher4.setBounds(278.7f,1699.1f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher4.setOrientation(60.0f);
        LDarcher4.setVelocity(new Vector3(0,0.0f,0));
        LDarcher4.setMaxSpeed(50.0f);
        LDarcher4.setTeam(Team.LDANIEL);
        LDarcher4.initializeTacticalRole(new DefensiveArcher());
        
        LDarcher5 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher5.setBounds(178.2f,1635.9f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher5.setOrientation(60.0f);
        LDarcher5.setVelocity(new Vector3(0,0.0f,0));
        LDarcher5.setMaxSpeed(50.0f);
        LDarcher5.setTeam(Team.LDANIEL);
        LDarcher5.initializeTacticalRole(new DefensiveArcher());
        
        LDarcher6 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher6.setBounds(213.9f,1825.4f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher6.setOrientation(60.0f);
        LDarcher6.setVelocity(new Vector3(0,0.0f,0));
        LDarcher6.setMaxSpeed(50.0f);
        LDarcher6.setTeam(Team.LDANIEL);
        LDarcher6.initializeTacticalRole(new DefensiveArcher());
        
        LDarcher7 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet-archer.png")));
        LDarcher7.setBounds(600.9f,1525.4f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        LDarcher7.setOrientation(60.0f);
        LDarcher7.setVelocity(new Vector3(0,0.0f,0));
        LDarcher7.setMaxSpeed(50.0f);
        LDarcher7.setTeam(Team.LDANIEL);
        LDarcher7.initializeTacticalRole(new DefensiveArcher());
        
        LDTeam = new LinkedList<Character>();
        LDTeam.addAll(Arrays.asList(LDsoldier1, LDsoldier2, LDarcher1, LDarcher2, LDarcher3, LDarcher4, LDarcher5, LDarcher6, LDarcher7));
        
        
        
//        drop.addToListBehaviour(new Attack(drop, defensiveSoldier, defensiveSoldier.getRole().getDamageToDone()-20, defensiveSoldier.getRole().getMaxDistanceOfAttack()));
        
        IADeVProject.addToWorldObjectList(FJsoldier1, FJsoldier2, FJarcher1, FJarcher2, FJarcher3, FJarcher4, FJarcher5, FJarcher6, FJarcher7);
        IADeVProject.addToWorldObjectList(LDsoldier1, LDsoldier2, LDarcher1, LDarcher2, LDarcher3, LDarcher4, LDarcher5, LDarcher6, LDarcher7);
        
        SimpleMapOfInfluence.initializeSimpleMapOfInfluence();
	}
	
	@Override
	public void render() {
		processMouseEvent();
		
		// Cada vez que renderiza, indicamos al InputProcessor que procese si hay una tecla pulsada. Esto se hace aquí
        // porque InputProcessor no tiene ningún método para indicar que se mantiene pulsada una tecla. Nosotros queremos
        // que mientras se pulse una de las teclas de movimiento de la cámara, la cámara se mueva sin tener que estar pulsando
        // cada vez la misma tecla.
		inputProcessor.processKeyPressed();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        // Estas 2 lineas sirven para que los objetos dibujados actualicen su posición cuando se mueva la cámara. (Que se muevan también).
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        
        drop.applyBehaviour();
        
        updateTacticalRoleOfWorldCharacters(); // Actualizamos los roles de todos los personajes
        
        drawRegionsOfBasesAndManantials();	// Dibujamos las regiones de las bases y los manantiales.
        drawAllObjects();					// Dibujamos todos los objetos del mundo.
        
		
        Waypoints.drawWaypointsOfBases(); // Dibujamos los waypoints de ambas bases.
        Waypoints.drawWaypointsOfBridges(); // Dibujamos los waypoints de los puentes.
        drawHealthOfWorldCharacters(); // Dibujamos la vida de todos los personajes del mundo
        MoralPoints.drawMoralPointsOfBases(batch, font);
        
        SimpleMapOfInfluence.updateSimpleMapOfInfluence();
        SimpleMapOfInfluence.drawInfluenceMap(renderer, 32, 0, 0, false);
        SimpleMapOfInfluence.drawInfluenceMap(renderer, 10, 2048, 0, true);
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj.getTexture() != null) {
				obj.getTexture().dispose();
			}
		}
		
		font.dispose();
		renderer.dispose();
		tiledMap.dispose();
        batch.dispose();
	}
	
	/**
	 * Método que actualiza todos los personajes actualizando sus roles.
	 */
	private void updateTacticalRoleOfWorldCharacters() {
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj instanceof Character) {
				Character character = (Character)obj;
				character.updateTacticalRole();
			}
		}
	}
	
	/**
	 * Método que dibuja la vida de todos los personajes del mundo.
	 */
	private void drawHealthOfWorldCharacters() {
		for (WorldObject obj : IADeVProject.worldObjects) {
			if (obj instanceof Character) {
				Character character = (Character)obj;
				character.drawHealth(batch, font);
			}
		}
	}
	
	/**
	 * Este método es para evitar llamar al inputProcessor si tenemos que mover personajes con el ratón
	 */
	private void processMouseEvent() {
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

			drop.setPosition(touchPos);
		}
	}
	
	
	/** MÉTODOS DE DIBUJO DE LÍNEAS **/
	/**
	 * Método que dibuja las regiones de las bases y los manantiales, para que 
	 * visualmente se vea lo que abarca cada una de las bases y manantiales.
	 */
	private void drawRegionsOfBasesAndManantials() {
		renderer.begin(ShapeType.Line);
        renderer.setColor(Color.CYAN);
	        for (Rectangle r : IADeVProject.bases.values()) {
	        	renderer.rect(r.x, r.y, r.width, r.height);
	        }
	        for (Rectangle r : IADeVProject.manantials.values()) {
	        	renderer.rect(r.x, r.y, r.width, r.height);
	        }
        renderer.end();
	}
	
	/**
	 * Método que dibuja todos los objetos en el mapa.
	 */
	private void drawAllObjects() {
        batch.begin();

        for (WorldObject obj : IADeVProject.worldObjects) {
        	if (!(obj instanceof Obstacle)) {
        		obj.draw(batch);
        	}
        }
    
        batch.end();
	}
}