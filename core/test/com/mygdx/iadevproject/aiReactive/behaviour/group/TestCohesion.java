package com.mygdx.iadevproject.aiReactive.behaviour.group;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.IADeVProject;
import com.mygdx.iadevproject.aiReactive.arbitrator.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.aiReactive.behaviour.group.Cohesion;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.WorldObject;

public class TestCohesion extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	private ShapeRenderer renderer;
	
	private Character gota2;
	private Character gota3;
	private Character gota4;
	private Character gota5;
	
	private Character cubo;
	private static float threshold = 150.0f;
	
	private Cohesion behaviour;
	
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
        
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        // Creamos el personaje.
        gota2 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota2.setBounds(80.0f, 100.0f, 32.0f, 32.0f);
        gota2.setOrientation(30.0f);
        gota2.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota2.setMaxSpeed(50.0f);
        
        // Creamos el personaje.
        gota3 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota3.setBounds(20.0f, 150.0f, 32.0f, 32.0f);
        gota3.setOrientation(30.0f);
        gota3.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota3.setMaxSpeed(50.0f);
        
        // Creamos el personaje.
        gota4 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota4.setBounds(20.0f, 200.0f, 32.0f, 32.0f);
        gota4.setOrientation(30.0f);
        gota4.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota4.setMaxSpeed(50.0f);
        
        // Creamos el personaje.
        gota5 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota5.setBounds(80.0f, 250.0f, 32.0f, 32.0f);
        gota5.setOrientation(30.0f);
        gota5.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota5.setMaxSpeed(50.0f);
        
        List<WorldObject> listWorldObjects = new LinkedList<WorldObject>();
        listWorldObjects.add(gota2);
        listWorldObjects.add(gota3);
        listWorldObjects.add(gota4);
        listWorldObjects.add(gota5);
        
        // Creamos el personaje.
        cubo = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        cubo.setBounds(350.0f, 200.0f, 32.0f, 32.0f);
        cubo.setOrientation(30.0f);
        cubo.setVelocity(new Vector3(-30.0f,0.0f,0.0f)); // Hacía la izquierda.
        cubo.setMaxSpeed(50.0f);
        behaviour = new Cohesion(cubo, listWorldObjects, 50.0f, threshold);
        cubo.addToListBehaviour(behaviour);
        
	}
	
	@Override
	public void render() {
		handleInput();
        camera.update();
        // Estas 2 lineas sirven para que los objetos dibujados actualicen su posición cuando se mueva la cámara. (Que se muevan también).
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
             
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        cubo.applyBehaviour();
        
        Vector3 centerOfMass = behaviour.getCenterOfMass();
               
        batch.begin();
		gota2.draw(batch);
		gota3.draw(batch);
		gota4.draw(batch);
		gota5.draw(batch);
		cubo.draw(batch);
		font.draw(batch, "Vx: " + cubo.getVelocity().x + " Vy: " + cubo.getVelocity().y, cubo.getPosition().x, cubo.getPosition().y - 20);
		font.draw(batch, "COMx: " + centerOfMass.x + " COMy: " + centerOfMass.y, cubo.getPosition().x, cubo.getPosition().y - 40);
        batch.end();
        
        renderer.begin(ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.circle(cubo.getPosition().x, cubo.getPosition().y, threshold);
        renderer.end();
        
        renderer.begin(ShapeType.Filled);
        renderer.setColor(Color.BLUE);
        renderer.circle(centerOfMass.x, centerOfMass.y, 5);
        renderer.end();
 
	}
	
	@Override
	public void dispose() {
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
