package com.mygdx.iadevproject.aiTactical.roles;

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
import com.mygdx.iadevproject.aiReactive.arbitrator.PriorityArbitrator;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.CollisionAvoidance;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Face;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.WallAvoidance;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.waypoints.Waypoints;

public class TestDefensiveSoldier extends ApplicationAdapter {
	
	public static OrthographicCamera camera;				// Cámara (es pública para que se pueda acceder el InputProcessorIADeVProject)
	public static boolean PRINT_PATH_BEHAVIOUR; 			// Dibujar el camino/recorrido obtenido por la función getSteering de los Behaviours.
	public static BitmapFont font; 							// Para dibujar letras 
	public static SpriteBatch batch; 						// Para dibujar letras hace falta tanto un font como un batch.
	public static ShapeRenderer renderer; 					// Para dibujar líneas
	public static TiledMap tiledMap;												// Mapa real de los dibujitos
	public static TiledMapRenderer tiledMapRenderer;
	public static InputProcessorIADeVProject inputProcessor;		// InputProcessor
	
	public static Character drop, bucket, defensiveSoldier;
	
	@Override
	public void create() {		
		IADeVProject iadevproject = new IADeVProject();
		iadevproject.create();
		
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
       
        
        bucket = new Character(new PriorityArbitrator(1e-5f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
    	bucket.setBounds(800.0f, 600.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        bucket.setOrientation(60.0f);
        bucket.setVelocity(new Vector3(0,0.0f,0));

        WallAvoidance wallAvoidance = new WallAvoidance(drop, 300.0f, IADeVProject.worldObstacles, 300.0f, 20.0f, 100.0f);
        drop.addToListBehaviour(wallAvoidance, 60);
        Seek_Accelerated seek = new Seek_Accelerated(drop, bucket, 20.0f);
        seek.setMode(Seek_Accelerated.SEEK_ACCELERATED_MILLINGTON);
        drop.addToListBehaviour(seek);
        drop.addToListBehaviour(new Face(drop, bucket, 30.0f, 30.0f, 1.0f, 10.0f, 1.0f));
        drop.addToListBehaviour(new CollisionAvoidance(drop, IADeVProject.worldObstacles, 200.0f)); 
                
        defensiveSoldier = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        defensiveSoldier.setBounds(1418.0f,170.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        defensiveSoldier.setOrientation(60.0f);
        defensiveSoldier.setVelocity(new Vector3(0,0.0f,0));
        defensiveSoldier.setMaxSpeed(50.0f);
        defensiveSoldier.setTeam(Team.FJAVIER);
        defensiveSoldier.initializeTacticalRole(new DefensiveSoldier());
        
        
        IADeVProject.addToWorldObjectList(drop, bucket, defensiveSoldier);
        
        // Inicializamos las estructuras para el manejo de los waypoints de los puentes.
        Waypoints.initializeBridgesWaypoints();
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
        
        
        drop.applyBehaviour();
        
        defensiveSoldier.updateTacticalRole();
        
        drawRegionsOfBasesAndManantials();	// Dibujamos las regiones de las bases y los manantiales.
        drawAllObjects();					// Dibujamos todos los objetos del mundo.
       
		
        Waypoints.drawWaypointsOfBases(); // Dibujamos los waypoints de ambas bases.
        Waypoints.drawWaypointsOfBridges(); // Dibujamos los waypoints de los puentes.
        drop.drawHealth(batch, font);
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