package com.mygdx.iadevproject;

import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.iadevproject.aiReactive.arbitrator.PriorityArbitrator;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.CollisionAvoidance;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Face;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.WallAvoidance;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.WallAvoidance.RayPosition;
import com.mygdx.iadevproject.map.TiledMapIADeVProject;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;
 
public class IADeVProject extends ApplicationAdapter {
	
	public static int WIDTH = 2048;									// Anchura del mapa.
	public static int HEIGHT = 2048;								// Altura del mapa
	public static int[][] MAP_OF_COSTS = new int[WIDTH][HEIGHT];	// Mapa de costes
	public static int INFINITY = Integer.MAX_VALUE;					// Valor infinito
	public static int DEFAULT_COST = 1;								// Coste por defecto
	
	// Estos dos atributos se obtienen del mapa 'tiledMap' cuando se crea esta clase
	public static int WORLD_OBJECT_WIDTH;				// Anchura de los personajes
	public static int WORLD_OBJECT_HEIGHT;				// Altura de los personajes
	
	public static List<WorldObject> worldObjects;		// Objetos del mundo
	public static List<WorldObject> worldObstacles;		// Obstáculos del mundo
	public static Set<WorldObject> selectedObjects; 	// Lista de objetos seleccionados
	public static OrthographicCamera camera;			// Cárama (es pública para que se pueda acceder el InputProcessorIADeVProject)
	
	
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer renderer;
	private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
	
	private InputProcessorIADeVProject inputProcessor;		// InputProcessor
	
	public static Character drop, bucket;
	private WallAvoidance wallAvoidance;

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
        tiledMapRenderer 	= new TiledMapIADeVProject(tiledMap);
        
        // Obtenemos del mapa la anchura y altura de los personajes a raíz del tamaño de las celdas
        WORLD_OBJECT_WIDTH = 32; //(Integer) tiledMap.getProperties().get("tilewidth");
        WORLD_OBJECT_HEIGHT = 32; //(Integer) tiledMap.getProperties().get("tileheight");
        
        worldObstacles = getObstaclesOfMap();
        
        
        drop = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
    	drop.setBounds(100.0f, 100.0f, WORLD_OBJECT_WIDTH, WORLD_OBJECT_HEIGHT);
        drop.setOrientation(60.0f);
        drop.setVelocity(new Vector3(0,0.0f,0));
        drop.setMaxSpeed(50.0f);
        
        bucket = new Character(new PriorityArbitrator(1e-5f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
    	bucket.setBounds(200.0f, 200.0f, WORLD_OBJECT_WIDTH, WORLD_OBJECT_HEIGHT);
        bucket.setOrientation(60.0f);
        bucket.setVelocity(new Vector3(0,0.0f,0));

        wallAvoidance = new WallAvoidance(drop, 300.0f, worldObstacles, 300.0f, 30.0f, 200.0f);
        drop.addToListBehaviour(wallAvoidance, 60);
        Seek_Accelerated seek = new Seek_Accelerated(drop, bucket, 20.0f);
        seek.setMode(Seek_Accelerated.SEEK_ACCELERATED_MILLINGTON);
        drop.addToListBehaviour(seek);
        drop.addToListBehaviour(new Face(drop, bucket, 30.0f, 30.0f, 1.0f, 10.0f, 1.0f));
        
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
        
        drop.applyBehaviour();
        
//        TiledMapTileLayer obstacleLayer =  (TiledMapTileLayer)tiledMap.getLayers().get("way");
//        int height = obstacleLayer.getHeight();
//        int width = obstacleLayer.getWidth();
//        
////        System.out.println("height = "+ height+ " - width ="+width);
//        
        
//        
//        for (int i=0; i < obstacleLayer.getHeight(); i++)
//        {
//        	for (int j=0; j < obstacleLayer.getWidth(); j++)
//        	{
//        		Cell cell = obstacleLayer.getCell(i,j);
//        		if (cell != null)
//        		{
////        			System.out.println(i + "," + j);
//        			int x = cell.getTile().getTextureRegion().getRegionX();
//        			int y = cell.getTile().getTextureRegion().getRegionY();
//        			
//        			Vector3 pos = new Vector3(x,y,0);
//        			Obstacle obs = new Obstacle(cell.getTile().getTextureRegion().getTexture());
//        			
////        			camera.unproject(pos);
////            		System.out.println("x = "+pos.x+", y = "+pos.y);
//        			
//        			obs.setPosition(pos);
//        			
//        			obstacles.add(obs);
//        		}
//        		
//        	}
//        }
        
        		
        
        batch.begin();

	        for (WorldObject obj : worldObjects) {
	        	if (!(obj instanceof Obstacle)) {
	        		obj.draw(batch);
	        	}
	        }
        
        batch.end();

        // DESCOMENTAR PARA MOSTRAR LOS CENTROS DE LOS OBSTÁCULOS
//        renderer.begin(ShapeType.Filled);
//	        for (WorldObject obs : worldObstacles) {
//	        	renderer.circle(obs.getPosition().x, obs.getPosition().y, 2);
//	        }
//		renderer.end();

		
		// ESTO ES PARA MOSTRAR LAS LÍNEAS DEL WALL AVOIDANCE
//		renderer.begin(ShapeType.Line);
//		renderer.setColor(Color.RED);
//		Ray ray = wallAvoidance.getRays().get(RayPosition.CENTER);
//		Vector3 endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.CENTER));
//		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
//		
//		ray = wallAvoidance.getRays().get(RayPosition.LEFT);
//		endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.LEFT));
//		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
//
//		ray = wallAvoidance.getRays().get(RayPosition.RIGHT);
//		endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.RIGHT));
//		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
//		renderer.end();
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		for (WorldObject obj : worldObjects) {
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
	 * Método que obtiene los obstáculo del mapa 'tiledMap' y lo devuelve en forma de Lista
	 * @return Lista de los obstáculos del mundo
	 */
	private List<WorldObject> getObstaclesOfMap() {
		List<WorldObject> obstacles = new LinkedList<WorldObject>();
        
        MapLayer obstacleLayer =  tiledMap.getLayers().get("obstacles");        
        for (MapObject obj : obstacleLayer.getObjects()) {
        	
        	MapProperties properties = obj.getProperties();
        	
        	float x = (Float) properties.get("x");
        	x += ((Float) properties.get("width"))/2;
        	
        	float y = (Float) properties.get("y");
        	y += ((Float) properties.get("height"))/2;
        	
			Obstacle obs = new Obstacle();
			obs.setBounds(x, y, (Float) properties.get("width"), (Float) properties.get("height"));
			
			obstacles.add(obs);
        }
        
        return obstacles;
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