package com.mygdx.iadevproject;

import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.iadevproject.aiReactive.arbitrator.PriorityArbitrator;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.Behaviour;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Face;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.WallAvoidance;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.WallAvoidance.RayPosition;
import com.mygdx.iadevproject.aiReactive.pathfinding.Distance;
import com.mygdx.iadevproject.aiReactive.pathfinding.ManhattanDistance;
import com.mygdx.iadevproject.aiReactive.pathfinding.PathFinding;
import com.mygdx.iadevproject.map.Ground;
import com.mygdx.iadevproject.map.MapsCreatorIADeVProject;
import com.mygdx.iadevproject.map.TiledMapIADeVProject;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;
 
public class IADeVProject extends ApplicationAdapter {
	
	/** CONSTANTES **/
	public static final int GRID_CELL_SIZE 			= 32;					// Longitud del lado de las celdas para los distintos grids.
	public static final int WIDTH 				= 2048;						// Anchura del mapa
	public static final int HEIGHT 				= 2048;						// Altura del mapa
	public static final int GRID_WIDTH 			= WIDTH / GRID_CELL_SIZE;	// Anchura del grid
	public static final int GRID_HEIGHT			= HEIGHT / GRID_CELL_SIZE;	// Altura del grid
	public static final int INFINITY 			= Integer.MAX_VALUE;		// Valor infinito
	public static final int DEFAULT_COST 		= 1;						// Coste por defecto
	public static final Ground DEFAULT_GROUND 	= Ground.TRAIL;				// Terreno por defecto
	// Estos dos atributos se obtienen del mapa 'tiledMap' cuando se crea esta clase
	public static int WORLD_OBJECT_WIDTH;									// Anchura de los personajes
	public static int WORLD_OBJECT_HEIGHT;									// Altura de los personajes
	
	
	/** MAPAS **/
	public static int[][] 	 MAP_OF_COSTS 	= new int[GRID_WIDTH][GRID_HEIGHT];		// Mapa de costes
	public static Ground[][] MAP_OF_GROUNDS = new Ground[GRID_WIDTH][GRID_HEIGHT];	// Mapa de terrenos
	/** MAPA REAL CON DIBUJITOS **/
	public static TiledMap tiledMap;												// Mapa real de los dibujitos
	
	
	/** VARIABLES GLOBALES **/
	public static List<WorldObject> worldObjects;		// Objetos del mundo
	public static List<WorldObject> worldObstacles;		// Obstáculos del mundo
	public static Set<WorldObject> selectedObjects; 	// Lista de objetos seleccionados
	public static OrthographicCamera camera;			// Cárama (es pública para que se pueda acceder el InputProcessorIADeVProject)
	public static boolean PRINT_PATH_BEHAVIOUR = false; // Dibujar el camino/recorrido obtenido por la función getSteering de los Behaviours.
	
	/** VARIABLES LOCALES **/
	public static SpriteBatch batch;
	private BitmapFont font;
	public static ShapeRenderer renderer;
    private TiledMapRenderer tiledMapRenderer;
	private InputProcessorIADeVProject inputProcessor;		// InputProcessor
	
	public static Character drop, bucket;
	private WallAvoidance wallAvoidance;
	
	
	PathFinding pf = new PathFinding();
    List<Vector3> listaDePuntos;
	
    
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
        camera 				= new OrthographicCamera(w, h);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        
        batch 				= new SpriteBatch();
        font 				= new BitmapFont();
        renderer 			= new ShapeRenderer();
        tiledMap 			= new TmxMapLoader().load("../core/assets/map.tmx");
        tiledMapRenderer 	= new TiledMapIADeVProject(tiledMap);
        
        // Obtenemos del mapa la anchura y altura de los personajes a raíz del tamaño de las celdas
        WORLD_OBJECT_WIDTH = (Integer) tiledMap.getProperties().get("tilewidth");
        WORLD_OBJECT_HEIGHT = (Integer) tiledMap.getProperties().get("tileheight");
        
        // Creamos los mapas (de costes, de terreno, etc)
        MapsCreatorIADeVProject.createMaps();
        
        // Obtenemos los obstáculos del mapa
        worldObstacles = getObstaclesOfMap();
        
        
        drop = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
    	drop.setBounds(600.0f, 600.0f, WORLD_OBJECT_WIDTH, WORLD_OBJECT_HEIGHT);
        drop.setOrientation(60.0f);
        drop.setVelocity(new Vector3(0,0.0f,0));
        drop.setMaxSpeed(50.0f);
        
        bucket = new Character(new PriorityArbitrator(1e-5f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
    	bucket.setBounds(800.0f, 600.0f, WORLD_OBJECT_WIDTH, WORLD_OBJECT_HEIGHT);
        bucket.setOrientation(60.0f);
        bucket.setVelocity(new Vector3(0,0.0f,0));

        wallAvoidance = new WallAvoidance(drop, 300.0f, worldObstacles, 300.0f, 30.0f, 200.0f);
        drop.addToListBehaviour(wallAvoidance, 60);
        Seek_Accelerated seek = new Seek_Accelerated(drop, bucket, 20.0f);
        seek.setMode(Seek_Accelerated.SEEK_ACCELERATED_MILLINGTON);
        drop.addToListBehaviour(seek);
        drop.addToListBehaviour(new Face(drop, bucket, 30.0f, 30.0f, 1.0f, 10.0f, 1.0f));
        
        addToWorldObjectList(drop, bucket);
        
//        System.out.println(MAP_OF_COSTS[(int) 538.0f][(int) 414.00003f]);
//        System.out.println(MAP_OF_COSTS[(int) 537.0f][(int) 305.00003f]);
//        System.out.println(MAP_OF_COSTS[(int) 537.0f][(int) 261.00003f]);
//        System.out.println("********");        
//        System.out.println(MAP_OF_COSTS[(int) 588.0001f][(int) 350.0f]);
        
        // --> La distancia de Manhattan es una basura. El personaje da muchísimas vueltas. La mejor es la de CHEBYSHEV.
        listaDePuntos = pf.applyPathFinding(MAP_OF_COSTS, IADeVProject.GRID_CELL_SIZE, PathFinding.CHEBYSHEV_DISTANCE, GRID_WIDTH, GRID_HEIGHT, 588.00006f, 286.00003f, 543.00006f, 620.00006f);
        
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
        
        batch.begin();

	        for (WorldObject obj : worldObjects) {
	        	if (!(obj instanceof Obstacle)) {
	        		obj.draw(batch);
	        	}
	        }
        
        batch.end();

        // DESCOMENTAR PARA MOSTRAR LOS CENTROS DE LOS OBSTÁCULOS
//        drawCenterOfObstacles();

		
		// ESTO ES PARA MOSTRAR LAS LÍNEAS DEL WALL AVOIDANCE
//        drawLinesOfWallAvoidance();
             
        
        // DESCOMENTAR SI SE QUIERE SABER EL COSTE Y EL TERRENO POR EL QUE VA PASANDO EL OBJETO 'drop'
//      System.out.println("COST = "+getCostOfPosition(drop.getPosition()));
//      System.out.println("GROUND = "+getGroundOfPosition(drop.getPosition()));
        
        
        
        renderer.begin(ShapeType.Filled);
        renderer.setColor(Color.RED);
		Vector3 puntoAnterior = null;
		for (Vector3 punto : listaDePuntos) {
			renderer.circle(punto.x, punto.y, 2);
			if (puntoAnterior != null) {
				renderer.line(punto, puntoAnterior);
			}
			puntoAnterior = new Vector3(punto);
		}
		renderer.end();
		
		System.out.println(MAP_OF_COSTS[(int)bucket.getPosition().x/GRID_CELL_SIZE][(int)bucket.getPosition().y/GRID_CELL_SIZE]);
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
	
	
	/** MÉTODOS ÚTILES **/
	/**
	 * Método que obtiene los obstáculo del mapa 'tiledMap' y lo devuelve en forma de Lista
	 * @return Lista de los obstáculos del mundo
	 */
	private List<WorldObject> getObstaclesOfMap() {
		List<WorldObject> obstacles = new LinkedList<WorldObject>();
        
		// Obtenemos la capa del mapa de los obstáculos
        MapLayer obstacleLayer =  tiledMap.getLayers().get("obstacles");        
        for (MapObject obj : obstacleLayer.getObjects()) {
        	
        	// Para cada objeto, obtenemos sus propiedades
        	MapProperties properties = obj.getProperties();
        	
        	// Calculamos la coordenada X como la coordenada x + la mitad del ancho del obstáculo (ya que la coordenada X se encuentra abajo izquierda del objeto)
        	float x = (Float) properties.get("x");
        	x += ((Float) properties.get("width"))/2;
        	
        	// Calculamos la coordenada Y como la coordenada y + la mitad del alto del obstáculo (ya que la coordenada Y se encuentra abajo izquierda del objeto)
        	float y = (Float) properties.get("y");
        	y += ((Float) properties.get("height"))/2;
        	
        	// Creamos el obstáculo con las coordenadas calculadas y las propiedades de alto y ancho del obstáculo
			Obstacle obs = new Obstacle();
			obs.setBounds(x, y, (Float) properties.get("width"), (Float) properties.get("height"));
			
			// Lo añadimos a la lista de obstáculos
			obstacles.add(obs);
        }
        
        // Devolvemos la lista de obstáculos
        return obstacles;
	}
	
	/**
	 * Método que dado la posición 'position' devuelve el coste del mapa de coste
	 * asociado a esa posición.
	 * @param position Posición de la que se quiere saber su coste
	 * @return Coste de esa posición
	 */
	public static int getCostOfPosition(Vector3 position) {
		if (position.x < 0 || position.y < 0) throw new IllegalArgumentException("Coordenates must be positives");
		if (position.x >= WIDTH || position.y >= HEIGHT) throw new IllegalArgumentException("Coordenates must be less than WIDHT and HEIGHT constants");
		
		return MAP_OF_COSTS[(int)position.x/IADeVProject.GRID_CELL_SIZE][(int)position.y/IADeVProject.GRID_CELL_SIZE];
	}
	
	/**
	 * Método que dado la posición 'position' devuelve el terreno del mapa de terrenos
	 * asociado a esa posición.
	 * @param position Posición de la que se quiere saber su terreno
	 * @return Terreno de esa posición
	 */
	public static Ground getGroundOfPosition(Vector3 position) {
		if (position.x < 0 || position.y < 0) throw new IllegalArgumentException("Coordenates must be positives");
		if (position.x >= WIDTH || position.y >= HEIGHT) throw new IllegalArgumentException("Coordenates must be less than WIDHT and HEIGHT constants");

		return MAP_OF_GROUNDS[(int)position.x/IADeVProject.GRID_CELL_SIZE][(int)position.y/IADeVProject.GRID_CELL_SIZE];
	}
	
	/**
	 * Método que transforma una posición del plano o mapa real en una posición del grid.
	 * @param grid_cell_size Longitud del lado de las celdas del grid.
	 * @param mapPosition Posición real del mapa.
	 * @return Posición del grid.
	 */
	public static Vector3 mapPositionTOgridPosition (int grid_cell_size, Vector3 mapPosition) {
		// Eliminamos los decimales haciendo el casting.
		int gridPosition_x = (int) mapPosition.x/grid_cell_size;
		int gridPosition_y = (int) mapPosition.y/grid_cell_size;
		int gridPosition_z = (int) mapPosition.z/grid_cell_size;
		return new Vector3(gridPosition_x, gridPosition_y, gridPosition_z);
	}
	
	/**
	 * Método que transforma una posición del grid a una posición del plano o mapa real.
	 * @param grid_cell_size Longitud del lado de las celdas del grid.
	 * @param gridPosition Posición real del grid.
	 * @return Posición real del mapa.
	 */
	public static Vector3 gridPositionTOmapPosition (int grid_cell_size, Vector3 gridPosition) {
		// Además de la propia tranformación también se aplica un desplazamiento para situar el objeto en el centro del tile.
		float mapPosition_x = (gridPosition.x * ((float) grid_cell_size)) + ((float) grid_cell_size/2);
		float mapPosition_y = (gridPosition.y * ((float) grid_cell_size)) + ((float) grid_cell_size/2);
		float mapPosition_z = (gridPosition.z * ((float) grid_cell_size)) + ((float) grid_cell_size/2);
		return new Vector3(mapPosition_x, mapPosition_y, mapPosition_z);
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

	
	/** MÉTODOS DE DIBUJO DE LÍNEAS **/
	/**
	 * Método para dibujar los centros de los obstáculos en el mapa
	 */
	private void drawCenterOfObstacles() {
		renderer.begin(ShapeType.Filled);
        	for (WorldObject obs : worldObstacles) {
        		renderer.circle(obs.getPosition().x, obs.getPosition().y, 2);
        	}
        renderer.end();
	}
	
	/**
	 * Método para dibujar las líneas relevantes del WallAvoidance
	 */
	private void drawLinesOfWallAvoidance() {
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.RED);
		Ray ray = wallAvoidance.getRays().get(RayPosition.CENTER);
		Vector3 endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.CENTER));
		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
		
		ray = wallAvoidance.getRays().get(RayPosition.LEFT);
		endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.LEFT));
		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);

		ray = wallAvoidance.getRays().get(RayPosition.RIGHT);
		endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.RIGHT));
		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
		renderer.end();
	}
	
}