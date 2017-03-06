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
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.*;
import com.mygdx.iadevproject.behaviour.Delegated.CollisionAvoidance;
import com.mygdx.iadevproject.behaviour.Delegated.Evade;
import com.mygdx.iadevproject.behaviour.Delegated.Face;
import com.mygdx.iadevproject.behaviour.Delegated.LookingWhereYouGoing;
import com.mygdx.iadevproject.behaviour.Delegated.PathFollowingWithoutPathOffset;
import com.mygdx.iadevproject.behaviour.Delegated.Persue;
import com.mygdx.iadevproject.behaviour.Delegated.Wander_Delegated;
import com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov.Arrive_NoAccelerated;
import com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov.Flee_NoAccelerated;
import com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov.Seek_NoAccelerated;
import com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov.Wander_NoAccelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

public class IADeVProject extends ApplicationAdapter {
	
	public static List<WorldObject> worldsObstacles; // Lista de obstáculos del mundo
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;

	private Set<Object> selectedObjects; // Lista de objetos seleccionados
	
	private Character gota;
	private Character cubo;
	private Character collision;
	
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
        gota = new Character(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota.setBounds(100.0f, 410.0f, 64.0f, 64.0f);
        gota.setOrientation(30.0f);
        gota.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        
        // Creamos otro personaje.
        cubo = new Character(new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        cubo.setBounds(-10.0f, -10.0f, 64.0f, 64.0f);
        cubo.setOrientation(90.0f);
        cubo.setVelocity(new Vector3(0.0f, 0.0f, 0));
        cubo.setMaxSpeed(50.0f);
        listaDePuntos = new LinkedList<Vector3>();
        listaDePuntos.add(new Vector3(20.0f, 20.0f, 0));
        listaDePuntos.add(new Vector3(160.0f, 200.0f, 0));
        listaDePuntos.add(new Vector3(280.0f, 20.0f, 0));
        listaDePuntos.add(new Vector3(400.0f, 200.0f, 0));
        listaDePuntos.add(new Vector3(520.0f, 20.0f, 0));
        Obstacle obs1 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        cubo.addToListBehaviour(new Seek_Accelerated(cubo, obs1, 1000.0f));
        
//        cubo.addToListBehaviour(new LookingWhereYouGoing(cubo, 20.0f, 50.0f, 0.0f, 10.0f, 1.0f));
        renderer = new ShapeRenderer();
                
        gota.addToListBehaviour(new Wander_Delegated(gota, 50.0f, 60.0f, 0.0f, 10.0f, 1.0f, 20.0f, 5.0f, 20.0f, 0.0f, 50.0f));
        
        
       
        obs1.setBounds(100.0f, 100.0f, 64.0f, 64.0f);
        Obstacle obs2 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs2.setBounds(180.0f, 100.0f, 64.0f, 64.0f);
        Obstacle obs3 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs3.setBounds(100.0f, 300.0f, 64.0f, 64.0f);
        Obstacle obs4 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs4.setBounds(180.0f, 300.0f, 64.0f, 64.0f);
        
        worldsObstacles.add(obs1);
        worldsObstacles.add(obs2);
        worldsObstacles.add(obs3);
        worldsObstacles.add(obs4);
        
        worldsObstacles.add(gota);
        
        collision = new Character(new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        collision.setBounds(400.0f, 400.0f, 64.0f, 64.0f);
        collision.setOrientation(10.0f);
        collision.setVelocity(new Vector3(-10.0f, -10.0f, 0));
        collision.addToListBehaviour(new CollisionAvoidance(collision, worldsObstacles, 80.0f));
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
        cubo.applyBehaviour();
//        collision.applyBehaviour();
        
		// begin a new batch and draw the bucket and all drops
		batch.begin();
		
		for (WorldObject obs : worldsObstacles) {
			obs.draw(batch);
		}
		
		gota.draw(batch);
		cubo.draw(batch);
		collision.draw(batch);
		font.draw(batch, "CenterOfMass : " + collision.getCenterOfMass().x + " - " + collision.getCenterOfMass().y, collision.getCenterOfMass().x, collision.getCenterOfMass().y - 10);
		font.draw(batch, "CenterOfMass : " + gota.getCenterOfMass().x + " - " + gota.getCenterOfMass().y, gota.getCenterOfMass().x, gota.getCenterOfMass().y - 10);
		
//		font.draw(batch, "Velocidad : " + cubo.getVelocity().x + " - " + cubo.getVelocity().y, cubo.getPosition().x, cubo.getPosition().y - 10);
//		font.draw(batch, "Orientación: " + cubo.getOrientation(), cubo.getPosition().x, cubo.getPosition().y - 25);
//		font.draw(batch, "Orientación: " + gota.getOrientation(), gota.getPosition().x, gota.getPosition().y - 25);
		batch.end();
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.RED);
		for (Vector3 punto : listaDePuntos) {
			renderer.circle(punto.x, punto.y, 2);
		}
		
		renderer.end();
		
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.CYAN);
		renderer.circle(cubo.getCenterOfMass().x, cubo.getCenterOfMass().y, cubo.getBoundingRadius());
		renderer.circle(gota.getCenterOfMass().x, gota.getCenterOfMass().y, gota.getBoundingRadius());
		renderer.circle(collision.getCenterOfMass().x, collision.getCenterOfMass().y, collision.getBoundingRadius());
		//renderer.rect(cubo.getBoundingRectangle().x, cubo.getBoundingRectangle().y, cubo.getBoundingRectangle().width, cubo.getBoundingRectangle().height);
		
		for (WorldObject obs : worldsObstacles) {
			renderer.circle(obs.getCenterOfMass().x, obs.getCenterOfMass().y, obs.getBoundingRadius());
		}
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