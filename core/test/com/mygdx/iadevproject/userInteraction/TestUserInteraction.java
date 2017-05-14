package com.mygdx.iadevproject.userInteraction;

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
import com.mygdx.iadevproject.InputProcessorIADeVProject;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiTactical.roles.DefensiveSoldier;
import com.mygdx.iadevproject.checksAndActions.Checks;
import com.mygdx.iadevproject.mapOfInfluence.SimpleMapOfInfluence;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.waypoints.Waypoints;

public class TestUserInteraction extends ApplicationAdapter {
	
	public static OrthographicCamera camera;				// Cámara (es pública para que se pueda acceder el InputProcessorIADeVProject)
	public static boolean PRINT_PATH_BEHAVIOUR; 			// Dibujar el camino/recorrido obtenido por la función getSteering de los Behaviours.
	public static BitmapFont font; 							// Para dibujar letras 
	public static SpriteBatch batch; 						// Para dibujar letras hace falta tanto un font como un batch.
	public static ShapeRenderer renderer; 					// Para dibujar líneas
	public static TiledMap tiledMap;												// Mapa real de los dibujitos
	public static TiledMapRenderer tiledMapRenderer;
	public static InputProcessorIADeVProject inputProcessor;		// InputProcessor
	
	public static Character drop, bucket, defensiveSoldier, enemy;
	
	@Override
	public void create() {		
		IADeVProject iadevproject = new IADeVProject();
		iadevproject.create();
		
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
        
        defensiveSoldier = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        defensiveSoldier.setBounds(660.0f, 660.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        defensiveSoldier.setOrientation(60.0f);
        defensiveSoldier.setVelocity(new Vector3(0,0.0f,0));
        defensiveSoldier.setMaxSpeed(50.0f);
        defensiveSoldier.setTeam(Team.FJAVIER);
        
        bucket = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket.setBounds(500.0f, 900.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        bucket.setOrientation(60.0f);
        bucket.setVelocity(new Vector3(0,0.0f,0));
        bucket.setMaxSpeed(50.0f);
        bucket.setTeam(Team.FJAVIER);
        bucket.initializeTacticalRole(new DefensiveSoldier());
        
        enemy = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        enemy.setBounds(800.0f, 300.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        enemy.setOrientation(60.0f);
        enemy.setVelocity(new Vector3(0,0.0f,0));
        enemy.setMaxSpeed(50.0f);
        enemy.setTeam(Team.LDANIEL);
        
        
        IADeVProject.addToWorldObjectList(drop, bucket, defensiveSoldier, enemy);
        UserInteraction.printFirstUse();
	}
	
	@Override
	public void render() {
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
        
        if (!IADeVProject.paused) {
        	// Si el juego no está pausado, actualizamos a los personajes.
        	updateTacticalRoleOfWorldCharacters(); // Actualizamos los roles de todos los personajes
        }
        
        checkIfAnySelectedCharacterHasBeenDead(); // Comprobamos si alguno de los personajes seleccionados ha muerto.
        
        drawRegionsOfBasesAndManantials();	// Dibujamos las regiones de las bases y los manantiales.
        drawRegionsOfSelectedCharacters();	// Dibujamos las regiones de los personajes seleccionados.
        drawAllObjects();					// Dibujamos todos los objetos del mundo.
        
        Waypoints.drawWaypointsOfBases(); // Dibujamos los waypoints de ambas bases.
        Waypoints.drawWaypointsOfBridges(); // Dibujamos los waypoints de los puentes.
        
        drawHealthOfWorldCharacters(); // Dibujamos la vida de todos los personajes del mundo
        
        SimpleMapOfInfluence.updateSimpleMapOfInfluence();
        if (IADeVProject.showInfluenceMap) {
            SimpleMapOfInfluence.drawInfluenceMap(renderer, 32, 0, 0, false);        	
        }
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
	 * Método que comprueba si algún personaje seleccionado ha muerto. Si es así,
	 * lo elimina de la lista de personajes seleccionados y 
	 */
	private void checkIfAnySelectedCharacterHasBeenDead() {
		for (Character c : IADeVProject.selectedCharacters) {
			if (Checks.haveIDead(c)) {
				IADeVProject.selectedCharacters.remove(c);
				c.haveBeenReleased();
			}
		}
	}
	
	/** MÉTODOS DE DIBUJO DE LÍNEAS **/
	/**
	 * Método que dibuja las regiones de los personajes seleccionados por el usuario.
	 */
	private void drawRegionsOfSelectedCharacters() {
		Rectangle rec;
		for (WorldObject character : IADeVProject.selectedCharacters) {
			rec = character.getBoundingRectangle();
			renderer.begin(ShapeType.Line);
			renderer.setColor(Color.CHARTREUSE);
			renderer.rect(rec.x, rec.y, rec.width, rec.height);
			renderer.end();
		}
	}
	
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
}