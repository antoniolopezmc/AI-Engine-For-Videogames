package com.mygdx.iadevproject.aiReactive.flocking;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.VelocityMatching_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.CollisionAvoidance;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.LookingWhereYouGoing;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Wander_Delegated;
import com.mygdx.iadevproject.aiReactive.behaviour.group.Cohesion;
import com.mygdx.iadevproject.aiReactive.behaviour.group.Separation;
import com.mygdx.iadevproject.checksAndActions.Actions;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.userInteraction.InputProcessorIADeVProject;
import com.mygdx.iadevproject.waypoints.Waypoints;

public class TestFlocking extends ApplicationAdapter {
	
	public static OrthographicCamera camera;				// Cámara (es pública para que se pueda acceder el InputProcessorIADeVProject)
	public static boolean PRINT_PATH_BEHAVIOUR; 			// Dibujar el camino/recorrido obtenido por la función getSteering de los Behaviours.
	public static BitmapFont font; 							// Para dibujar letras 
	public static SpriteBatch batch; 						// Para dibujar letras hace falta tanto un font como un batch.
	public static ShapeRenderer renderer; 					// Para dibujar líneas
	public static TiledMap tiledMap;												// Mapa real de los dibujitos
	public static TiledMapRenderer tiledMapRenderer;
	public static InputProcessorIADeVProject inputProcessor;		// InputProcessor
	
	public static Character drop, bucket1, bucket2, bucket3, bucket4, bucket5;
	
	@Override
	public void create() {		
		IADeVProject iadevproject = new IADeVProject();
		iadevproject.create();
		
		// Inicializamos las estructuras para el manejo de los waypoints de los puentes.
        Waypoints.initializeBridgesWaypoints();
		
		camera = IADeVProject.camera;
		camera.translate(new Vector3(0.0f, 800.0f, 0));
		font = IADeVProject.font;
		batch = IADeVProject.batch;
		renderer = IADeVProject.renderer;
		tiledMap = IADeVProject.tiledMap;
		tiledMapRenderer = IADeVProject.tiledMapRenderer;
		inputProcessor = IADeVProject.inputProcessor;	
		
		IADeVProject.PRINT_PATH_BEHAVIOUR = false;
		// Limpiamos los objetos del IADeVProject para utilizar solamnte los que se crean en esta clase
		// y le añadimos los obstáculos del mundo.
		IADeVProject.worldObjects.clear();
//		IADeVProject.worldObjects.addAll(IADeVProject.worldObstacles);
		
        drop = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
    	drop.setBounds(351.0f,1375.5f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        drop.setOrientation(60.0f);
        drop.setVelocity(new Vector3(0,0.0f,0));
        drop.setMaxSpeed(50.0f);
                               
        bucket1 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket1.setBounds(222.92f,1327.44f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        bucket1.setOrientation(0.0f);
        bucket1.setVelocity(new Vector3(0,0.0f,0));
        bucket1.setMaxSpeed(50.0f);
        
        bucket2 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket2.setBounds(324.3f,1476.9f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        bucket2.setOrientation(0.0f);
        bucket2.setVelocity(new Vector3(0,0.0f,0));
        bucket2.setMaxSpeed(50.0f);
        
        bucket3 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket3.setBounds(242.5f,1405.7f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        bucket3.setOrientation(0.0f);
        bucket3.setVelocity(new Vector3(0,0.0f,0));
        bucket3.setMaxSpeed(50.0f);
        
        bucket4 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket4.setBounds(530.8f,1441.35f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        bucket4.setOrientation(0.0f);
        bucket4.setVelocity(new Vector3(0,0.0f,0));
        bucket4.setMaxSpeed(50.0f);
        
        bucket5 = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket5.setBounds(532.6f,1343.4f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        bucket5.setOrientation(0.0f);
        bucket5.setVelocity(new Vector3(0,0.0f,0));
        bucket5.setMaxSpeed(50.0f);
        
        IADeVProject.addToWorldObjectList(drop, bucket1, bucket2, bucket3, bucket4, bucket5);
        
        WorldObject target = new Obstacle();
        target.setPosition(new Vector3(600.0f, 600.0f, 0.0f));
        drop.setListBehaviour(Actions.arrive(30.0f, drop, target, 30.0f));
        
         // Creamos la lista de personajes a las que aplicar el comportamiento quitando el personaje seleccionado
        List<WorldObject> list = new LinkedList<WorldObject>(IADeVProject.worldObjects);
		list.remove(bucket1);
		bucket1.addToListBehaviour(new CollisionAvoidance(bucket1, list, 300.0f), 300.0f);
		bucket1.addToListBehaviour(new Separation(bucket1, 30.0f, list, 300.0f, 100000.0f), 80.0f);
		bucket1.addToListBehaviour(new Cohesion(bucket1, list, 50.0f, 200.0f), 70.0f);
		bucket1.addToListBehaviour(new VelocityMatching_Accelerated(bucket1, drop, 30.0f, 1.0f), 60.0f);
		bucket1.addToListBehaviour(new LookingWhereYouGoing(bucket1, 30.0f, 30.0f, 5.0f, 10.0f, 1.0f), 100.0f);
		bucket1.addToListBehaviour(new Wander_Delegated(bucket1, 50.0f, 50.0f, 5.0f, 15.0f, 1.0f, 100.0f, 50.0f, 30.0f, bucket1.getOrientation(), 20.0f), 40.0f);
		
		list = new LinkedList<WorldObject>(IADeVProject.worldObjects);
		list.remove(bucket2);
		bucket2.addToListBehaviour(new CollisionAvoidance(bucket2, list, 300.0f), 300.0f);
		bucket2.addToListBehaviour(new Separation(bucket2, 30.0f, list, 300.0f, 100000.0f), 80.0f);
		bucket2.addToListBehaviour(new Cohesion(bucket2, list, 50.0f, 200.0f), 70.0f);
		bucket2.addToListBehaviour(new VelocityMatching_Accelerated(bucket2, drop, 30.0f, 1.0f), 60.0f);
		bucket2.addToListBehaviour(new LookingWhereYouGoing(bucket2, 30.0f, 30.0f, 5.0f, 10.0f, 1.0f), 100.0f);
		bucket2.addToListBehaviour(new Wander_Delegated(bucket2, 50.0f, 50.0f, 5.0f, 15.0f, 1.0f, 100.0f, 50.0f, 30.0f, bucket2.getOrientation(), 20.0f), 40.0f);
		
		list = new LinkedList<WorldObject>(IADeVProject.worldObjects);
		list.remove(bucket3);
		bucket3.addToListBehaviour(new CollisionAvoidance(bucket3, list, 300.0f), 300.0f);
		bucket3.addToListBehaviour(new Separation(bucket3, 30.0f, list, 300.0f, 100000.0f), 80.0f);
		bucket3.addToListBehaviour(new Cohesion(bucket3, list, 50.0f, 200.0f), 70.0f);
		bucket3.addToListBehaviour(new VelocityMatching_Accelerated(bucket3, drop, 30.0f, 1.0f), 60.0f);
		bucket3.addToListBehaviour(new LookingWhereYouGoing(bucket3, 30.0f, 30.0f, 5.0f, 10.0f, 1.0f), 100.0f);
		bucket3.addToListBehaviour(new Wander_Delegated(bucket3, 50.0f, 50.0f, 5.0f, 15.0f, 1.0f, 100.0f, 50.0f, 30.0f, bucket3.getOrientation(), 20.0f), 40.0f);
		
		list = new LinkedList<WorldObject>(IADeVProject.worldObjects);
		list.remove(bucket4);
		bucket4.addToListBehaviour(new CollisionAvoidance(bucket4, list, 300.0f), 300.0f);
		bucket4.addToListBehaviour(new Separation(bucket4, 30.0f, list, 300.0f, 100000.0f), 80.0f);
		bucket4.addToListBehaviour(new Cohesion(bucket4, list, 50.0f, 200.0f), 70.0f);
		bucket4.addToListBehaviour(new VelocityMatching_Accelerated(bucket4, drop, 30.0f, 1.0f), 60.0f);
		bucket4.addToListBehaviour(new LookingWhereYouGoing(bucket4, 30.0f, 30.0f, 5.0f, 10.0f, 1.0f), 100.0f);
		bucket4.addToListBehaviour(new Wander_Delegated(bucket4, 50.0f, 50.0f, 5.0f, 15.0f, 1.0f, 100.0f, 50.0f, 30.0f, bucket4.getOrientation(), 20.0f), 40.0f);
		
		list = new LinkedList<WorldObject>(IADeVProject.worldObjects);
		list.remove(bucket5);
		bucket5.addToListBehaviour(new CollisionAvoidance(bucket5, list, 300.0f), 300.0f);
		bucket5.addToListBehaviour(new Separation(bucket5, 30.0f, list, 300.0f, 100000.0f), 80.0f);
		bucket5.addToListBehaviour(new Cohesion(bucket5, list, 50.0f, 200.0f), 70.0f);
		bucket5.addToListBehaviour(new VelocityMatching_Accelerated(bucket5, drop, 30.0f, 1.0f), 60.0f);
		bucket5.addToListBehaviour(new LookingWhereYouGoing(bucket5, 30.0f, 30.0f, 5.0f, 10.0f, 1.0f), 100.0f);
		bucket5.addToListBehaviour(new Wander_Delegated(bucket5, 50.0f, 50.0f, 5.0f, 15.0f, 1.0f, 100.0f, 50.0f, 30.0f, bucket5.getOrientation(), 20.0f), 40.0f);
        
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
//        tiledMapRenderer.setView(camera);
//        tiledMapRenderer.render();
        
        updateTacticalRoleOfWorldCharacters();
        drawAllObjects();					// Dibujamos todos los objetos del mundo.
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
	 * Este método es para evitar llamar al inputProcessor si tenemos que mover personajes con el ratón
	 */
	private void processMouseEvent() {
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

			WorldObject target = new Obstacle();
	        target.setPosition(touchPos);
	        drop.setListBehaviour(Actions.arrive(100.0f, drop, target, 30.0f));
			System.out.println(touchPos);
		}
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
	
	
	/** MÉTODOS DE DIBUJO DE LÍNEAS **/
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