package com.mygdx.iadevproject;

import java.util.Set;
import java.util.HashSet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData.AssetData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Align_Accelerated;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Arrive_Accelerated;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.VelocityMatching_Accelerated;
import com.mygdx.iadevproject.behaviour.AcceleratedUnifMov.Wander_Accelerated;
import com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov.*;
import com.mygdx.iadevproject.modelo.Character;
import com.mygdx.iadevproject.steering.Steering_NoAcceleratedUnifMov;

public class IADeVProject extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	
	private Sprite bucket;
	private Sprite raindrop;

	private Set<Object> selectedObjects; // Lista de objetos seleccionados

	
	private Character gota;
	private Character cubo;

	@Override
	public void create() {
	
		selectedObjects = new HashSet<Object>();
		
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
        gota.setBounds(450.0f, 450.0f, 64.0f, 64.0f);
        gota.setOrientation(0.0f);
        gota.addToListBehaviour(new Wander_NoAccelerated(80.0f, 5.0f));
        
        // Creamos otro personaje.
        cubo = new Character(new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        cubo.setBounds(200.0f, 200.0f, 64.0f, 64.0f);
        cubo.setOrientation(0.0f);
        cubo.setVelocity(new Vector3(0.0f, 0.0f, 0));
        cubo.addToListBehaviour(new Seek_Accelerated(80.0f, 50.0f));
	}
	
	@Override
	public void render() {
		handleInput();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        gota.applyBehaviour(null);       
        cubo.applyBehaviour(gota);

		// begin a new batch and draw the bucket and all drops
		batch.begin();
		gota.draw(batch);
		cubo.draw(batch);
		font.draw(batch, "Velocidad : " + cubo.getVelocity().x + " - " + cubo.getVelocity().y, cubo.getPosition().x, cubo.getPosition().y - 10);
		font.draw(batch, "Orientación: " + gota.getOrientation(), gota.getPosition().x, gota.getPosition().y - 25);
		batch.end();
		
		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

			if (bucket.getBoundingRectangle().contains(new Vector2(touchPos.x, touchPos.y))) {
				addToSelectedList(bucket);
			}

			if (raindrop.getBoundingRectangle().contains(new Vector2(touchPos.x, touchPos.y))) {
				addToSelectedList(raindrop);
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
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 3, 0);
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