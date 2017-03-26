package com.mygdx.iadevproject;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_NoAccelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.*;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.Wander_Delegated;
import com.mygdx.iadevproject.aiReactive.behaviour.noAcceleratedUnifMov.Seek_NoAccelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.noAcceleratedUnifMov.Wander_NoAccelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.model.formation.CircularFormation;

public class IADeVProject extends ApplicationAdapter {
	
	// Anchura del mapa.
	public static int WIDTH = 1000;
	// Altura del mapa
	public static int HEIGHT = 1000;
	public static int[][] MAP_OF_COSTS = new int[WIDTH][HEIGHT];
	public static int INFINITY = Integer.MAX_VALUE;
	public static int DEFAULT_COST = 1;
	
	public static List<WorldObject> worldsObstacles; // Lista de obstáculos del mundo
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;

	private Set<Object> selectedObjects; // Lista de objetos seleccionados
	
	private Character gota;
	private Character gota2;
	private Character gota3;
	private Character gota4;
	private Character gota5;
	private CircularFormation formacion;
	private Character cubo;
	
	ShapeRenderer renderer;
	List<Vector3> listaDePuntos;

	@Override
	public void create() {
	
		selectedObjects = new HashSet<Object>();
		worldsObstacles = new LinkedList<WorldObject>();
		
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera = new OrthographicCamera(w, h);

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        font = new BitmapFont();
        
        // Creamos el personaje.
        gota = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f),new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota.setBounds(50.0f, 50.0f, 64.0f, 64.0f);
        gota.setOrientation(0.0f);
        gota.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        
        // Creamos el personaje.
        gota2 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f),new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota2.setBounds(150.0f, 150.0f, 64.0f, 64.0f);
        gota2.setOrientation(30.0f);
        gota2.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        
        // Creamos el personaje.
        gota3 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f),new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota3.setBounds(250.0f, 250.0f, 64.0f, 64.0f);
        gota3.setOrientation(30.0f);
        gota3.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        
        // Creamos el personaje.
        gota4 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f),new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota4.setBounds(350.0f, 350.0f, 64.0f, 64.0f);
        gota4.setOrientation(30.0f);
        gota4.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        
        // Creamos el personaje.
        gota5 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f),new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota5.setBounds(450.0f, 450.0f, 64.0f, 64.0f);
        gota5.setOrientation(30.0f);
        gota5.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota5.addToListBehaviour(new Wander_NoAccelerated(gota5, 50.0f, 20.0f)); // En formación, el wander no deberia tenerse en cuenta.
        
        // Creamos la formación.
        formacion = new CircularFormation(new WeightedBlendArbitrator_NoAccelerated(200.0f, 200.0f), 50.0f);
        formacion.setBounds(500.0f, 500.0f, 64.0f, 64.0f);
        formacion.setOrientation(0.0f);
        formacion.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        formacion.addCharacterToCharactersList(gota);
        formacion.addCharacterToCharactersList(gota2);
        formacion.addCharacterToCharactersList(gota3);
        formacion.addCharacterToCharactersList(gota4);
        formacion.addCharacterToCharactersList(gota5);
        formacion.setSeparationDistance(100.0f);
        
       
        
        // Creamos otro personaje.
        cubo = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        cubo.setBounds(510.0f, 510.0f, 64.0f, 64.0f);
        cubo.setOrientation(90.0f);
        cubo.setVelocity(new Vector3(0.0f, 0.0f, 0));
        cubo.setMaxSpeed(60.0f);
        listaDePuntos = new LinkedList<Vector3>();
        listaDePuntos.add(new Vector3(20.0f, 20.0f, 0));
        listaDePuntos.add(new Vector3(160.0f, 200.0f, 0));
        listaDePuntos.add(new Vector3(280.0f, 20.0f, 0));
        listaDePuntos.add(new Vector3(400.0f, 200.0f, 0));
        listaDePuntos.add(new Vector3(520.0f, 20.0f, 0));
        Obstacle obs1 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs1.setBounds(100.0f, 100.0f, 64.0f, 64.0f);
        cubo.addToListBehaviour(new Seek_Accelerated(cubo, gota, 50.0f));
        
//        cubo.addToListBehaviour(new LookingWhereYouGoing(cubo, 20.0f, 50.0f, 0.0f, 10.0f, 1.0f));
        renderer = new ShapeRenderer();
                
        gota.addToListBehaviour(new Wander_Delegated(gota, 50.0f, 60.0f, 0.0f, 10.0f, 1.0f, 20.0f, 5.0f, 20.0f, 0.0f, 50.0f));
        
        formacion.addToListBehaviour(new Seek_NoAccelerated(formacion, cubo, 50.0f));
       
	}
	
	@Override
	public void render() {
		handleInput();
        camera.update();
        // Estas 2 lineas sirven para que los objetos dibujados actualicen su posición cuando se mueva la cámara. (Que se muevan también).
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
//        gota.applyBehaviour();       
//        cubo.applyBehaviour();
        
        formacion.applyBehaviour();
//        gota5.applyBehaviour(); // No debería hacer nada.
        
		// begin a new batch and draw the bucket and all drops
		batch.begin();
		
		gota.draw(batch);
		gota2.draw(batch);
		gota3.draw(batch);
		gota4.draw(batch);
		gota5.draw(batch);
		
		formacion.draw(batch);
		
//		font.draw(batch, gota.getPosition().x + " - " + gota.getPosition().y, gota.getPosition().x, gota.getPosition().y - 10);
//		font.draw(batch, gota2.getPosition().x + " - " + gota2.getPosition().y, gota2.getPosition().x, gota2.getPosition().y - 10);
//		font.draw(batch, gota3.getPosition().x + " - " + gota3.getPosition().y, gota3.getPosition().x, gota3.getPosition().y - 10);
//		font.draw(batch, gota4.getPosition().x + " - " + gota4.getPosition().y, gota4.getPosition().x, gota4.getPosition().y - 10);
//		font.draw(batch, gota5.getPosition().x + " - " + gota5.getPosition().y, gota5.getPosition().x, gota5.getPosition().y - 10);
		
//		font.draw(batch, "Velocidad : " + cubo.getVelocity().x + " - " + cubo.getVelocity().y, cubo.getPosition().x, cubo.getPosition().y - 10);
//		font.draw(batch, "Orientación: " + cubo.getOrientation(), cubo.getPosition().x, cubo.getPosition().y - 25);
//		font.draw(batch, "Orientación: " + gota.getOrientation(), gota.getPosition().x, gota.getPosition().y - 25);
		batch.end();
		
		renderer.begin(ShapeType.Filled);
		/*renderer.setColor(Color.RED);
		for (Vector3 punto : listaDePuntos) {
			renderer.circle(punto.x, punto.y, 2);
		}*/
		renderer.end();
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.RED);
		renderer.circle(formacion.getPosition().x, formacion.getPosition().y, 5.0f);
		/*renderer.circle(cubo.getCenterOfMass().x, cubo.getCenterOfMass().y, cubo.getBoundingRadius());
		renderer.circle(gota.getCenterOfMass().x, gota.getCenterOfMass().y, gota.getBoundingRadius());
		//renderer.rect(cubo.getBoundingRectangle().x, cubo.getBoundingRectangle().y, cubo.getBoundingRectangle().width, cubo.getBoundingRectangle().height);
		*/
		renderer.setColor(Color.CYAN);
		renderer.circle(gota.getPosition().x, gota.getPosition().y, 5.0f);
		renderer.circle(gota2.getPosition().x, gota2.getPosition().y, 5.0f);
		renderer.circle(gota3.getPosition().x, gota3.getPosition().y, 5.0f);
		renderer.circle(gota4.getPosition().x, gota4.getPosition().y, 5.0f);
		renderer.circle(gota5.getPosition().x, gota5.getPosition().y, 5.0f);
		
		renderer.end();
		

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

			if (cubo.getBoundingRectangle().contains(new Vector2(touchPos.x, touchPos.y))) {
				addToSelectedList(cubo);
			}
			
			if (gota.getBoundingRectangle().contains(new Vector2(touchPos.x, touchPos.y))) {
				addToSelectedList(gota);
			}

			System.out.println("\n--------------\nSelected objects:");
			for (Object obj : selectedObjects) {
				if (obj instanceof Sprite){
					Sprite sprite = (Sprite)obj;
					System.out.println(sprite.getX() + " - " + sprite.getY());					
				}
			}
		}
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-3, 0, 0);
//			float x = gota.getPosition().x - 3;
//			float y = gota.getPosition().y;
//			gota.setPosition(x, y);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(3, 0, 0);
//			float x = gota.getPosition().x + 3;
//			float y = gota.getPosition().y;
//			gota.setPosition(x, y);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -3, 0);
//			float x = gota.getPosition().x;
//			float y = gota.getPosition().y - 3;
//			gota.setPosition(x, y);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 3, 0);
//			float x = gota.getPosition().x;
//			float y = gota.getPosition().y + 3;
//			gota.setPosition(x, y);
		}
//		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//			camera.rotate(-rotationSpeed, 0, 0, 1);
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
//			camera.rotate(rotationSpeed, 0, 0, 1);
//		}

//		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1);
//
//		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
//		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
//
//		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f,
//				100 - effectiveViewportWidth / 2f);
//		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f,
//				100 - effectiveViewportHeight / 2f);
	}

	/**
	 * Método que comprueba que si el usuario mantiene el botón
	 * CONTROL-IZQUIERDO presionado para añadir de la lista de objetos
	 * seleccionados (o limpiar la lista), el objeto que acaba de seleccionar
	 * 
	 * @param obj
	 */
	private void addToSelectedList(Object obj) {
		if (!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			selectedObjects.clear();
		}

		selectedObjects.add(obj);
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		gota.getTexture().dispose();
		cubo.getTexture().dispose();
        batch.dispose();
	}
}