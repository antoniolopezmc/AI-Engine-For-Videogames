package com.mygdx.iadevproject.aiReactive.behaviour.delegated;

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
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.PathFollowingWithoutPathOffset;
import com.mygdx.iadevproject.model.Character;

public class TestPathFollowingWithoutPathOffset extends ApplicationAdapter {
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	private ShapeRenderer renderer;
	
	private Character gota;
	private Character gota2;
	private Character gota3;
	private Character gota4;
	
	private List<Vector3> listaDePuntos;
	
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
        
        // Creamos la lista de puntos de debe seguir el objeto.
        listaDePuntos = new LinkedList<Vector3>();
        listaDePuntos.add(new Vector3(20.0f, 20.0f, 0));
        listaDePuntos.add(new Vector3(160.0f, 200.0f, 0));
        listaDePuntos.add(new Vector3(280.0f, 20.0f, 0));
        listaDePuntos.add(new Vector3(400.0f, 200.0f, 0));
        listaDePuntos.add(new Vector3(520.0f, 20.0f, 0));
        
        // Creamos el personaje.
        gota = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota.setBounds(50.0f, 50.0f, 32.0f, 32.0f);
        gota.setOrientation(0.0f);
        gota.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota.addToListBehaviour(new PathFollowingWithoutPathOffset(gota, 20.0f, listaDePuntos, 20.0f, PathFollowingWithoutPathOffset.MODO_PARAR_AL_FINAL));
        gota.setMaxSpeed(20.0f); // Limitamos la velocidad del objeto para que no se nos vaya de madre.
        
        // Creamos el personaje.
        gota2 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota2.setBounds(50.0f, 50.0f, 32.0f, 32.0f);
        gota2.setOrientation(0.0f);
        gota2.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota2.addToListBehaviour(new PathFollowingWithoutPathOffset(gota2, 40.0f, listaDePuntos, 20.0f, PathFollowingWithoutPathOffset.MODO_IDA_Y_VUELTA));
        gota2.setMaxSpeed(20.0f); // Limitamos la velocidad del objeto para que no se nos vaya de madre.
        
        // Creamos otro personaje. Con lista de puntos vacia.
        gota3 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota3.setBounds(50.0f, 50.0f, 32.0f, 32.0f);
        gota3.setOrientation(0.0f);
        gota3.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        gota3.addToListBehaviour(new PathFollowingWithoutPathOffset(gota3, 40.0f, new LinkedList<Vector3>(), 20.0f, PathFollowingWithoutPathOffset.MODO_IDA_Y_VUELTA));
        gota3.setMaxSpeed(20.0f); // Limitamos la velocidad del objeto para que no se nos vaya de madre.
        
        // Creamos el personaje. Con una lista de SOLO UN ELEMENTO.
        gota4 = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        gota4.setBounds(50.0f, 50.0f, 32.0f, 32.0f);
        gota4.setOrientation(0.0f);
        gota4.setVelocity(new Vector3(0.0f,0.0f,0.0f));
        List<Vector3> listaSoloUnPunto = new LinkedList<Vector3>();
        listaSoloUnPunto.add(listaDePuntos.get(2));
        gota4.addToListBehaviour(new PathFollowingWithoutPathOffset(gota4, 40.0f, listaSoloUnPunto, 20.0f, PathFollowingWithoutPathOffset.MODO_IDA_Y_VUELTA));
        gota4.setMaxSpeed(20.0f); // Limitamos la velocidad del objeto para que no se nos vaya de madre.
      
	}
	
	@Override
	public void render() {
		handleInput();
        camera.update();
        // Estas 2 lineas sirven para que los objetos dibujados actualicen su posición cuando se mueva la cámara. (Que se muevan también).
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        gota.applyBehaviour();
        gota2.applyBehaviour();
        gota3.applyBehaviour();
        gota4.applyBehaviour();
        
        batch.begin();
        gota.draw(batch);
		gota2.draw(batch);
		gota3.draw(batch);
		gota4.draw(batch);
        batch.end();
    	
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.RED);
		for (Vector3 punto : listaDePuntos) {
			renderer.circle(punto.x, punto.y, 2);
		}
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
