package com.mygdx.iadevproject.arbitrator.noAccelerated;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.arbitrator.accelerated.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.arbitrator.noAccelerated.WeightedBlendArbitrator_NoAccelerated;
import com.mygdx.iadevproject.behaviour.noAcceleratedUnifMov.Arrive_NoAccelerated;
import com.mygdx.iadevproject.behaviour.noAcceleratedUnifMov.Flee_NoAccelerated;
import com.mygdx.iadevproject.behaviour.noAcceleratedUnifMov.Seek_NoAccelerated;
import com.mygdx.iadevproject.model.Character;

public class TestWeightedBlendArbitrator_NoAccelerated extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	private ShapeRenderer renderer;
	
	private Character drop;
	private Character bucket, bucket1, bucket2, bucket3;
	
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
        
        drop = new Character(new WeightedBlendArbitrator_NoAccelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        drop.setBounds(50.0f, 50.0f, 64.0f, 64.0f);
        drop.setOrientation(60.0f);
        drop.setVelocity(new Vector3(0,0.0f,0));
        Character target = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f));
        target.setBounds(-600.0f, -600.0f, 64.0f, 64.0f);
        Seek_NoAccelerated seek = new Seek_NoAccelerated(drop, target, 30.0f);
        drop.addToListBehaviour(seek);
     
        bucket = new Character(new WeightedBlendArbitrator_NoAccelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket.setBounds(200.0f, 200.0f, 64.0f, 64.0f);
        bucket.setOrientation(0.0f);
        bucket.setVelocity(new Vector3(0,0,0));
        bucket.addToListBehaviour(new Arrive_NoAccelerated(bucket, drop, 60.0f, 10.0f, 0.5f));
        bucket.addToListBehaviour(new Flee_NoAccelerated(bucket, drop, 20.0f));
        
        bucket1 = new Character(new WeightedBlendArbitrator_NoAccelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket1.setBounds(187.0f, 210.0f, 64.0f, 64.0f);
        bucket1.setOrientation(0.0f);
        bucket1.setVelocity(new Vector3(0,0,0));
        bucket1.addToListBehaviour(new Arrive_NoAccelerated(bucket1, drop, 60.0f, 10.0f, 0.5f));
        bucket1.addToListBehaviour(new Flee_NoAccelerated(bucket1, drop, 20.0f));
        
        bucket2 = new Character(new WeightedBlendArbitrator_NoAccelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket2.setBounds(0.0f, 302.0f, 64.0f, 64.0f);
        bucket2.setOrientation(0.0f);
        bucket2.setVelocity(new Vector3(0,0,0));
        bucket2.addToListBehaviour(new Arrive_NoAccelerated(bucket2, drop, 60.0f, 10.0f, 0.5f));
        bucket2.addToListBehaviour(new Flee_NoAccelerated(bucket2, drop, 20.0f));
                
        bucket3 = new Character(new WeightedBlendArbitrator_NoAccelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        bucket3.setBounds(10.0f, 200.0f, 64.0f, 64.0f);
        bucket3.setOrientation(0.0f);
        bucket3.setVelocity(new Vector3(0,0,0));
        bucket3.addToListBehaviour(new Arrive_NoAccelerated(bucket3, drop, 60.0f, 10.0f, 0.5f));
        bucket3.addToListBehaviour(new Flee_NoAccelerated(bucket3, drop, 20.0f));
        

	}
	
	@Override
	public void render() {
		handleInput();
        camera.update();
        // Estas 2 lineas sirven para que los objetos dibujados actualicen su posición cuando se mueva la cámara. (Que se muevan también).
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        drop.applyBehaviour();       
        bucket.applyBehaviour();
        bucket1.applyBehaviour();
        bucket2.applyBehaviour();
        bucket3.applyBehaviour();
        
        batch.begin();
        drop.draw(batch);
		bucket.draw(batch);
		bucket1.draw(batch);
		bucket2.draw(batch);
		bucket3.draw(batch);
        batch.end();
        
        renderer.begin(ShapeType.Filled);
		renderer.circle(-600.0f, -600.0f, 2);
		renderer.end();
	}
	
	@Override
	public void dispose() {
		drop.getTexture().dispose();
		bucket.getTexture().dispose();
		bucket1.getTexture().dispose();
		bucket2.getTexture().dispose();
		bucket3.getTexture().dispose();
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
