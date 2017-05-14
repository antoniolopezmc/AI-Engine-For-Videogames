package com.mygdx.iadevproject;

import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiTactical.roles.DefensiveSoldier;
import com.mygdx.iadevproject.map.Ground;
import com.mygdx.iadevproject.map.MapsCreatorIADeVProject;
import com.mygdx.iadevproject.map.TiledMapIADeVProject;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.Team;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.model.formation.Formation;
import com.mygdx.iadevproject.waypoints.Waypoints;
 
public class IADeVProject extends ApplicationAdapter {
	
	/** CONSTANTES **/
	public static final int GRID_CELL_SIZE 		= 32;						// Longitud del lado de las celdas para los distintos grids.
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
	// IMPORTANTE -> En la lista de objetos del mundo están tanto las formaciones como sus integrantes. Está todo.
	public static List<WorldObject> worldObjects;			// Objetos del mundo
	public static List<WorldObject> worldObstacles;			// Obstáculos del mundo
	public static Set<Character> selectedCharacters; 		// Lista de personajes seleccionados
	public static OrthographicCamera camera;				// Cámara (es pública para que se pueda acceder el InputProcessorIADeVProject)
	public static boolean PRINT_PATH_BEHAVIOUR; 			// Dibujar el camino/recorrido obtenido por la función getSteering de los Behaviours.
	public static BitmapFont font; //= new BitmapFont();		// Para dibujar letras 
	public static SpriteBatch batch; // = new SpriteBatch();	// Para dibujar letras hace falta tanto un font como un batch.
	public static ShapeRenderer renderer; // = new ShapeRenderer();	// Para dibujar líneas
	public static Map<Team, Rectangle> bases;				// Bases de los equipos. Cada equipo tiene su base. 
	public static Map<Team, Rectangle> manantials;			// Manantiales de los equipos. Cada equipo tiene su manantial.
    public static TiledMapRenderer tiledMapRenderer;			// Renderer del mapa
    public static InputProcessorIADeVProject inputProcessor;		// InputProcessor
    
    // *********************************************************************************
    // Equipo LDANIEL (el de arriba)
    public static boolean LDANIEL_win = false;
    // Equipo FJAVIER (el de abajo)
    public static boolean FJAVIER_win = false;
    
    /**
     * Método para establecer una ganador de la partida.
     * @param team Ganador de la partida.
     */
    public static void setWinner (Team team) {
    	if (team == Team.LDANIEL) {
    		LDANIEL_win = true;
    	} else if (team == Team.FJAVIER) {
    		FJAVIER_win = true;
    	}
    }
    // IMPORTANTE -> Si en la misma interación de render se producen las condiciones de victoria de ambos equipos (los 2 booleanos a true), la partida quedaría en EMPATE.
    // *********************************************************************************
	
	public static Character drop, bucket, defensiveSoldier;
	
	@Override
	public void create() {
		// Establecemos a LibGDX el InputProcessor implementado en la clase InputProcessorIADeVProject
		inputProcessor = new InputProcessorIADeVProject();
		Gdx.input.setInputProcessor(inputProcessor);
	
		// Creación de las variables globales
		selectedCharacters 		= new HashSet<Character>();
		worldObjects 			= new LinkedList<WorldObject>();
		PRINT_PATH_BEHAVIOUR 	= true;				
		bases 					= new HashMap<Team, Rectangle>();	
		manantials 				= new HashMap<Team, Rectangle>();		
		font 					= new BitmapFont();
		batch 					= new SpriteBatch();
		renderer 				= new ShapeRenderer();	
		
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        camera 				= new OrthographicCamera(w, h);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        
        tiledMap 			= new TmxMapLoader().load("../core/assets/map.tmx");
        tiledMapRenderer 	= new TiledMapIADeVProject(tiledMap);
        
        // Obtenemos del mapa la anchura y altura de los personajes a raíz del tamaño de las celdas
        WORLD_OBJECT_WIDTH = (Integer) tiledMap.getProperties().get("tilewidth");
        WORLD_OBJECT_HEIGHT = (Integer) tiledMap.getProperties().get("tileheight");
        
        // Inicializamos las estructuras para el manejo de los waypoints de los puentes.
        Waypoints.initializeBridgesWaypoints();
        
        initializeMaps();		// Inicializamos los mapas a los valores por defecto.
        MapsCreatorIADeVProject.createMaps(tiledMap, GRID_CELL_SIZE); 				// Creamos los mapas (de costes, de terreno, etc)
        bases = MapsCreatorIADeVProject.getBasesOfMap(tiledMap, HEIGHT);			// Obtenemos las bases
        manantials = MapsCreatorIADeVProject.getManantialsOfMap(tiledMap, HEIGHT); 	// Obtenemos los manantiales
        
        // Obtenemos los obstáculos del mapa
        worldObstacles = MapsCreatorIADeVProject.getObstaclesOfMap(tiledMap);
        // Los añadimos a los objetos del mundo
        worldObjects.addAll(worldObstacles);
        
        drop = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
    	drop.setBounds(600.0f, 600.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        drop.setOrientation(60.0f);
        drop.setVelocity(new Vector3(0,0.0f,0));
        drop.setMaxSpeed(50.0f);
        drop.setTeam(Team.FJAVIER);

        bucket = new Character(new WeightedBlendArbitrator_Accelerated(50.0f, 20.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket.setBounds(500.0f, 900.0f, IADeVProject.WORLD_OBJECT_WIDTH, IADeVProject.WORLD_OBJECT_HEIGHT);
        bucket.setOrientation(60.0f);
        bucket.setVelocity(new Vector3(0,0.0f,0));
        bucket.setMaxSpeed(50.0f);
        bucket.setTeam(Team.FJAVIER);
        bucket.initializeTacticalRole(new DefensiveSoldier());
        
        IADeVProject.addToWorldObjectList(drop, bucket);
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
        
        updateTacticalRoleOfWorldCharacters(); // Actualizamos los roles de todos los personajes
        
        drawRegionsOfBasesAndManantials();	// Dibujamos las regiones de las bases y los manantiales.
        drawRegionsOfSelectedCharacters();	// Dibujamos las regiones de los personajes seleccionados.
        drawAllObjects();					// Dibujamos todos los objetos del mundo.
        
        Waypoints.drawWaypointsOfBases(); // Dibujamos los waypoints de ambas bases.
        Waypoints.drawWaypointsOfBridges(); // Dibujamos los waypoints de los puentes.
        
        drawHealthOfWorldCharacters(); // Dibujamos la vida de todos los personajes del mundo  
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
	 * Método que dado un equipo, devuelve su base.
	 * @param team Equipo.
	 * @return Base del equipo.
	 */
	public static Rectangle getBaseOfTeam(Team team) {
		return bases.get(team);
	}
	
	/**
	 * Método que dado un equipo, devuelve su manantial de curación.
	 * @param team Equipo.
	 * @return Manantial del equipo.
	 */
	public static Rectangle getManantialOfTeam(Team team) {
		return manantials.get(team);
	}
	
	/**
	 * Método que dado un equipo, devuelve la posición de su base.
	 * @param team Equipo.
	 * @return Posición de la base.
	 */
	public static Vector3 getPositionOfTeamBase(Team team) {
		Rectangle base = getBaseOfTeam(team); 
		// Ahora mismo se calcula como el centro de la base. Ya que las coordenadas (x,y) están 
		// abajo izquierda.
		return new Vector3(base.x+base.width/2, base.y+base.height/2, 0.0f);
	}
	
	/**
	 * Método que dado un equipo, devuelve la posición de la base enemiga.
	 * @param team Equipo.
	 * @return Posición de la base enemiga.
	 */
	public static Vector3 getPositionOfEnemyBase(Team team) {
		// Primero, obtenemos el equipo enemigo.
		Team enemyTeam = team.getEnemyTeam();
		
		// Ahora, llamamos al método superior.
		return getPositionOfTeamBase(enemyTeam);
	}
	
	/**
	 * Método que dado un equipo, devuelve la posición de su manantial.
	 * @param team Equipo.
	 * @return Posición del manantial.
	 */
	public static Vector3 getPositionOfTeamManantial(Team team) {
		Rectangle manantial = getManantialOfTeam(team);
		// Ahora mismo se calcula como el centro del manantial. Ya que las coordenadas (x,y) están 
		// abajo izquierda.
		return new Vector3(manantial.x+manantial.width/2, manantial.y+manantial.height/2, 0.0f);
	}
	
	
	/**
	 * Método que inicializa los mapas a los valores por defecto. 
	 */
	private static void initializeMaps() {
		for (int i=0; i < IADeVProject.GRID_WIDTH; i++) {
			for (int j=0; j < IADeVProject.GRID_HEIGHT; j++) {
				IADeVProject.MAP_OF_COSTS[i][j] = IADeVProject.DEFAULT_COST;
				IADeVProject.MAP_OF_GROUNDS[i][j] = IADeVProject.DEFAULT_GROUND;
			}
		}
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
		int gridPosition_z = 0;
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
		float mapPosition_z = 0.0f;
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
	 * Método que reseta los personajes seleccionados. Como si hubiera sido
	 * seleccionado otra vez.
	 */
	public static void resetSelectedCharacters() {
		for (Character c : selectedCharacters) {
			// Reseteamos el personaje como si hubiera sido seleccionado otra vez.
			c.haveBeenSelected();
		}
	}
	
	/**
	 * Método que limpia la lista de objetos seleccionados.
	 */
	public static void clearSelectedCharactersList() {
		for (Character c : selectedCharacters) {
			// Indicamos que el personaje ha sido deseleccionado
			c.haveBeenReleased();
			
			if (c instanceof Formation) {
				// Y si el personaje es una formación, la eliminamos de los objetos
				// del mundo.
				worldObjects.remove(c);
			}
		}
		// Limpiamos la lista de personajes seleccionados
		selectedCharacters.clear();
	}
	
	/**
	 * Método que comprueba que si el usuario mantiene el botón
	 * CONTROL-IZQUIERDO presionado para añadir de la lista de objetos
	 * seleccionados (o limpiar la lista), el objeto que acaba de seleccionar
	 * 
	 * @param obj
	 */
	public static void addToSelectedCharactersList(Character obj) {
		if (!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			clearSelectedCharactersList();
		}
		// Indicamos que el personaje ha sido seleccionado.
		Character c = obj.haveBeenSelected();
		// Lo añadimos a la lista de seleccionados
		selectedCharacters.add(c);
	}

	
	/** MÉTODOS DE DIBUJO DE LÍNEAS **/
	/**
	 * Método que dibuja las regiones de los personajes seleccionados por el usuario.
	 */
	private void drawRegionsOfSelectedCharacters() {
		Rectangle rec;
		for (WorldObject character : selectedCharacters) {
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
	        for (Rectangle r : bases.values()) {
	        	renderer.rect(r.x, r.y, r.width, r.height);
	        }
	        for (Rectangle r : manantials.values()) {
	        	renderer.rect(r.x, r.y, r.width, r.height);
	        }
        renderer.end();
	}
	
	/**
	 * Método que dibuja todos los objetos en el mapa.
	 */
	private void drawAllObjects() {
        batch.begin();

        for (WorldObject obj : worldObjects) {
        	if (!(obj instanceof Obstacle)) {
        		obj.draw(batch);
        	}
        }
    
        batch.end();
	}
}