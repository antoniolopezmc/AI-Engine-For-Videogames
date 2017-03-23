package com.mygdx.iadevproject.behaviour.group;

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
import com.mygdx.iadevproject.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.behaviour.delegated.Wander_Delegated;
import com.mygdx.iadevproject.behaviour.noAcceleratedUnifMov.Wander_NoAccelerated;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;
import com.mygdx.iadevproject.model.formation.LineFormation;

public class TestSeparation extends ApplicationAdapter {
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	private ShapeRenderer renderer;
	
	private Character gota;
	private Character gota2;
	private Character gota3;
	private Character gota4;
	private Character gota5;
	private Character gota6;
	
	private Character cubo;
	private static float threshold = 110.0f;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera = new OrthographicCamera(w, h);
        batch = new SpriteBatch();
        font = new BitmapFont();
        renderer = new ShapeRenderer();
        
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        
        // Creamos el personaje.
        gota = new Character(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota.setBounds(50.0f, 60.0f, 64.0f, 64.0f);
        gota.setOrientation(30.0f);
        gota.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota.setMaxSpeed(50.0f);
        
        // Creamos el personaje.
        gota2 = new Character(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota2.setBounds(50.0f, 80.0f, 64.0f, 64.0f);
        gota2.setOrientation(30.0f);
        gota2.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota2.setMaxSpeed(50.0f);
        
        // Creamos el personaje.
        gota3 = new Character(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota3.setBounds(50.0f, 100.0f, 64.0f, 64.0f);
        gota3.setOrientation(30.0f);
        gota3.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota3.setMaxSpeed(50.0f);
        
        // Creamos el personaje.
        gota4 = new Character(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota4.setBounds(50.0f, 120.0f, 64.0f, 64.0f);
        gota4.setOrientation(30.0f);
        gota4.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota4.setMaxSpeed(50.0f);
        
        // Creamos el personaje.
        gota5 = new Character(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota5.setBounds(50.0f, 140.0f, 64.0f, 64.0f);
        gota5.setOrientation(30.0f);
        gota5.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota5.setMaxSpeed(50.0f);
        
        // Creamos el personaje.
        gota6 = new Character(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota6.setBounds(50.0f, 160.0f, 64.0f, 64.0f);
        gota6.setOrientation(30.0f);
        gota6.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota6.setMaxSpeed(50.0f);
        
        List<WorldObject> listWorldObjects = new LinkedList<WorldObject>();
        listWorldObjects.add(gota);
        listWorldObjects.add(gota2);
        listWorldObjects.add(gota3);
        listWorldObjects.add(gota4);
        listWorldObjects.add(gota5);
        listWorldObjects.add(gota6);
        
        // Creamos el personaje.
        cubo = new Character(new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        cubo.setBounds(350.0f, 100.0f, 64.0f, 64.0f);
        cubo.setOrientation(30.0f);
        cubo.setVelocity(new Vector3(-10.0f,0.0f,0.0f)); // Hacía la izquierda.
        cubo.setMaxSpeed(50.0f);
        cubo.addToListBehaviour(new Separation(cubo, 500.0f, listWorldObjects, threshold, 500.0f));
        
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
               
        batch.begin();
        gota.draw(batch);
		gota2.draw(batch);
		gota3.draw(batch);
		gota4.draw(batch);
		gota5.draw(batch);
		gota6.draw(batch);
		cubo.draw(batch);
		font.draw(batch, "Vx: " + cubo.getVelocity().x + " Vy: " + cubo.getVelocity().y, cubo.getPosition().x, cubo.getPosition().y - 20);
        batch.end();
        
        renderer.begin(ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.circle(cubo.getPosition().x, cubo.getPosition().y, threshold);
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
