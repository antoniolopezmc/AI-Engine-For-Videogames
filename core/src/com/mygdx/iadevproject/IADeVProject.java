package com.mygdx.iadevproject;

import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.arbitrator.PriorityArbitrator;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class IADeVProject extends ApplicationAdapter {
	
	// Anchura del mapa.
	public static int WIDTH = 2048;
	// Altura del mapa
	public static int HEIGHT = 2048;
	public static int[][] MAP_OF_COSTS = new int[WIDTH][HEIGHT];
	public static int INFINITY = Integer.MAX_VALUE;
	public static int DEFAULT_COST = 1;
	
	public static List<WorldObject> worldObjects;		// Objetos del mundo
	public static Set<WorldObject> selectedObjects; 	// Lista de objetos seleccionados
	public static OrthographicCamera camera;			// Cárama (es pública para que se pueda acceder el InputProcessorIADeVProject)
	
	
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer renderer;
	private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
	
	
	private InputProcessorIADeVProject inputProcessor;		// InputProcessor
	
	public static Character drop, bucket;
	

	@Override
	public void create() {
		// Establecemos a LibGDX el InputProcessor implementado en la clase InputProcessorIADeVProject
		inputProcessor = new InputProcessorIADeVProject();
		Gdx.input.setInputProcessor(inputProcessor);
	
		selectedObjects = new HashSet<WorldObject>();
		worldObjects 	= new LinkedList<WorldObject>();
		
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera 				= new OrthographicCamera(w, h);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        
        batch 				= new SpriteBatch();
        font 				= new BitmapFont();
        renderer 			= new ShapeRenderer();
        tiledMap 			= new TmxMapLoader().load("../core/assets/map.tmx");
        tiledMapRenderer 	= new OrthogonalTiledMapRenderer(tiledMap);
        
        drop = new Character(new PriorityArbitrator(1e-5f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
    	drop.setBounds(100.0f, 100.0f, 64.0f, 64.0f);
        drop.setOrientation(60.0f);
        drop.setVelocity(new Vector3(0,0.0f,0));
        
        bucket = new Character(new PriorityArbitrator(1e-5f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
    	bucket.setBounds(200.0f, 200.0f, 64.0f, 64.0f);
        bucket.setOrientation(60.0f);
        bucket.setVelocity(new Vector3(0,0.0f,0));

        addToWorldObjectList(drop, bucket);
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
        
        
        		
        
        batch.begin();
        for (WorldObject obj : worldObjects) {
        	obj.draw(batch);
        }
        batch.end();
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		for (WorldObject obj : worldObjects) {
			obj.getTexture().dispose();
		}
        batch.dispose();
	}
	
	/**
	 * Método para añadir a la lista de objetos del mundo nuevos objetos.
	 * @param objects - Nuevos objetos a añadir
	 */
	public static void addToWorldObjectList(WorldObject ... objects) {
		worldObjects.addAll(Arrays.asList(objects));
	}
	
	/**
	 * Método que limpia la lista de objetos seleccionados.
	 */
	public static void clearSelectedObjectsList() {
		selectedObjects.clear();
	}
	
	/**
	 * Método que comprueba que si el usuario mantiene el botón
	 * CONTROL-IZQUIERDO presionado para añadir de la lista de objetos
	 * seleccionados (o limpiar la lista), el objeto que acaba de seleccionar
	 * 
	 * @param obj
	 */
	public static void addToSelectedObjectsList(WorldObject obj) {
		if (!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			clearSelectedObjectsList();
		}

		selectedObjects.add(obj);
	}

}