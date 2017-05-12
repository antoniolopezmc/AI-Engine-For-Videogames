package com.mygdx.iadevproject.aiReactive.behaviour.delegated;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.CollisionAvoidance;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

public class TestCollisionAvoidance extends ApplicationAdapter {
	
	public List<WorldObject> worldsObstacles; // Lista de obstáculos del mundo
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	private ShapeRenderer renderer;
	
	private Character collision, drop;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera = new OrthographicCamera(w, h);

        IADeVProject.PRINT_PATH_BEHAVIOUR = true;
        
        IADeVProject.font = new BitmapFont();
        IADeVProject.renderer = new ShapeRenderer();
        IADeVProject.batch = new SpriteBatch();
        font = IADeVProject.font;
        renderer = IADeVProject.renderer;
        batch = IADeVProject.batch;
        
        worldsObstacles = new LinkedList<WorldObject>();
        
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        
        Obstacle obs1 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs1.setBounds(400.0f, 400.0f, 32.0f, 32.0f);
        Obstacle obs2 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs2.setBounds(180.0f, 100.0f, 32.0f, 32.0f);
        Obstacle obs3 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs3.setBounds(100.0f, 300.0f, 32.0f, 32.0f);
        Obstacle obs4 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs4.setBounds(180.0f, 300.0f, 32.0f, 32.0f);
        
        drop = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        drop.setBounds(400, 200, 32, 32);
        drop.setVelocity(new Vector3(0,0,0));
        drop.addToListBehaviour(new Seek_Accelerated(drop, obs4, 40.0f));
        
        worldsObstacles.add(obs1);
        worldsObstacles.add(obs2);
        worldsObstacles.add(obs3);
        worldsObstacles.add(obs4);
        worldsObstacles.add(drop);
        
        collision = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        collision.setBounds(400.0f, 400.0f, 32.0f, 32.0f);
        collision.setOrientation(10.0f);
        collision.setVelocity(new Vector3(-50.0f, -50.0f, 0));
        collision.addToListBehaviour(new CollisionAvoidance(collision, worldsObstacles, 300.0f));
	}
	
	@Override
	public void render() {
		handleInput();
        camera.update();
        // Estas 2 lineas sirven para que los objetos dibujados actualicen su posición cuando se mueva la cámara. (Que se muevan también).
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        IADeVProject.PRINT_PATH_BEHAVIOUR = true;
        
        batch.begin();
		collision.draw(batch);
		for (WorldObject obs : worldsObstacles) {
			obs.draw(batch);
		}
        batch.end();
        
        drop.applyBehaviour();       
        collision.applyBehaviour();
	}
	
	@Override
	public void dispose() {
		for (WorldObject obs : worldsObstacles) {
			obs.getTexture().dispose();
		}
		
		collision.getTexture().dispose();
		batch.dispose();
		font.dispose();
		renderer.dispose();
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
}
