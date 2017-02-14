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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData.AssetData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.behaviour.NoAcceleratedUnifMov.Wander_NoAccelerated;
import com.mygdx.iadevproject.modelo.Character;

public class IADeVProject extends ApplicationAdapter {
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private Sprite bucket;
	private Sprite raindrop;

	private Set<Object> selectedObjects; // Lista de objetos seleccionados

	private float rotationSpeed;
	
	private Sprite cs;
	private Character c;

	@Override
	public void create() {
		rotationSpeed = 0.5f;
		selectedObjects = new HashSet<Object>();
		
		// load the images for the droplet and the bucket, 64x64 pixels each
		raindrop = new Sprite(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
		raindrop.setPosition(0,0);
		raindrop.setOriginCenter(); // IMPORTANTE --> Poner el centro de rotación en el centro de la figura.
		raindrop.setRotation(-10); // IMPORTANTE --> -10 --> Rota 10 grados a la derecha (con respecto a la vertical/eje y)
		
		bucket = new Sprite(new Texture(Gdx.files.internal("../core/assets/bucket.png")));
		bucket.setPosition(50, 50);
		
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera = new OrthographicCamera(w, h);

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        
        // Creamos el personaje.
        c = new Character();
        c.setHeight(64.0f);
        c.setWidth(64.0f);
        c.setMaxRotation(30.0f);
        c.setMaxSpeed(50.0f);
        c.setOrientation(0.0f);
        c.setPosition(new Vector3(200.0f,200.0f,0.0f));
        c.setRotation(30.0f);
        c.setSatisfactionRadius(45.0f);
        c.setVelocity(new Vector3(2500.0f,2500.0f,0.0f));
        c.addToListBehaviour(new Wander_NoAccelerated());
        
        cs = new Sprite(new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        cs.setOriginCenter();
        cs.setBounds(c.getPosition().x, c.getPosition().y, c.getWidth(), c.getHeight());
        cs.setRotation(c.getOrientation());
	}
	
	@Override
	public void render() {
		handleInput();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// begin a new batch and draw the bucket and
		// all drops
		batch.begin();
		bucket.draw(batch);
		raindrop.draw(batch);
		cs.draw(batch);
		batch.end();

		c.applyBehaviour(null);
		
		System.out.println(c.getPosition().x + " - " + c.getPosition().y);
		
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
//		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//			camera.zoom += 0.02;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
//			camera.zoom -= 0.02;
//		}
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
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.rotate(-rotationSpeed, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			camera.rotate(rotationSpeed, 0, 0, 1);
		}

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
		bucket.getTexture().dispose();
		raindrop.getTexture().dispose();
        batch.dispose();
	}
}