package com.mygdx.iadevproject.behaviour.delegated;

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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.arbitrator.weightedBlend.WeightedBlendArbitrator_Accelerated;
import com.mygdx.iadevproject.behaviour.acceleratedUnifMov.Seek_Accelerated;
import com.mygdx.iadevproject.behaviour.delegated.WallAvoidance;
import com.mygdx.iadevproject.model.Character;
import com.mygdx.iadevproject.model.Obstacle;
import com.mygdx.iadevproject.model.WorldObject;

public class TestWallAvoidance extends ApplicationAdapter {
	
	public List<WorldObject> worldsObstacles; // Lista de obstáculos del mundo
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	private ShapeRenderer renderer;
	
	private Character collision, drop;
	private WallAvoidance wallAvoidance;
	
	TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
	
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
        worldsObstacles = new LinkedList<WorldObject>();
        
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        
//        tiledMap = new TmxMapLoader().load("../core/assets/example.tmx");
//        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
//        
        
        Obstacle obs1 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs1.setBounds(100.0f, 100.0f, 64.0f, 64.0f);
        Obstacle obs2 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs2.setBounds(180.0f, 130.0f, 64.0f, 64.0f);
        Obstacle obs3 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs3.setBounds(100.0f, 250.0f, 64.0f, 64.0f);
        Obstacle obs4 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs4.setBounds(210.0f, 300.0f, 64.0f, 64.0f);
        Obstacle obs5 = new Obstacle(new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        obs5.setBounds(300.0f, 100.0f, 64.0f, 64.0f);
        
        drop = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/droplet.png")));
        drop.setBounds(600, 100, 64, 64);
        drop.setVelocity(new Vector3(0,0,0));
        Seek_Accelerated seek = new Seek_Accelerated(drop, obs4, 40.0f);
//        seek.setMode(Seek_Accelerated.SEEK_ACCELERATED_REYNOLDS);
        drop.addToListBehaviour(seek);
        
//        worldsObstacles.add(obs1);
//        worldsObstacles.add(obs2);
//        worldsObstacles.add(obs3);
        worldsObstacles.add(obs4);
        worldsObstacles.add(obs5);
        worldsObstacles.add(drop);
        
        collision = new Character(new WeightedBlendArbitrator_Accelerated(200.0f, 200.0f), new Texture(Gdx.files.internal("../core/assets/bucket.png")));
        collision.setBounds(150.0f, -200.0f, 64.0f, 64.0f);
        collision.setOrientation(10.0f);
        collision.setVelocity(new Vector3(10.0f, 100.0f, 0));
        wallAvoidance = new WallAvoidance(collision, 1000.0f, worldsObstacles, 100.0f, 20.0f, 200.0f);
        collision.addToListBehaviour(wallAvoidance);
    }
	
	@Override
	public void render() {
		handleInput();
        camera.update();
        
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
//        tiledMap.getLayers().get(0).setVisible(true);
//        
//        tiledMapRenderer.setView(camera);
//        tiledMapRenderer.render();
        
        // Estas 2 lineas sirven para que los objetos dibujados actualicen su posición cuando se mueva la cámara. (Que se muevan también).
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        
        drop.applyBehaviour();
        collision.applyBehaviour();
        
        batch.begin();
		collision.draw(batch);
		for (WorldObject obs : worldsObstacles) {
			obs.draw(batch);
		}
        batch.end();
      
        // DESCOMENTAR SI SE QUIEREN VER LOS PUNTOS, LOS CÍRCULOS DE INTERSECIÓN Y LOS RAYOS
        
//        for (WorldObject target : worldsObstacles) {
//			Rectangle boundingRectangle = target.getBoundingRectangle();
//			Vector3 minimum = new Vector3(boundingRectangle.x - boundingRectangle.height/2, boundingRectangle.y - boundingRectangle.width/2, 0.0f);
//			Vector3 maximum = new Vector3(minimum.x + boundingRectangle.height, minimum.y + boundingRectangle.width, 0.0f);
//			BoundingBox boundingBox = new BoundingBox(minimum, maximum);
//			
//			renderer.begin(ShapeType.Line);
//			renderer.setColor(Color.CYAN);
////			renderer.rect(boundingBox.getCenterX() - boundingBox.getHeight()/2, boundingBox.getCenterY() - boundingBox.getWidth()/2, boundingBox.getHeight(), boundingBox.getWidth());
//			renderer.circle(target.getCenterOfMass().x, target.getCenterOfMass().y, target.getBoundingRadius());
//			renderer.end();
//		}
		
//		renderer.begin(ShapeType.Line);
//		renderer.setColor(Color.RED);
//		Ray ray = wallAvoidance.getRays().get(RayPosition.CENTER);
//		Vector3 endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.CENTER));
//		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
//		
//		ray = wallAvoidance.getRays().get(RayPosition.LEFT);
//		endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.LEFT));
//		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
//
//		ray = wallAvoidance.getRays().get(RayPosition.RIGHT);
//		endPoint = ray.getEndPoint(new Vector3(0,0,0), wallAvoidance.getRaysLength().get(RayPosition.RIGHT));
//		renderer.line(ray.origin.x, ray.origin.y, endPoint.x, endPoint.y);
//		renderer.end();
//		
//		renderer.begin(ShapeType.Filled);
//		renderer.circle(wallAvoidance.getIntersection().x, wallAvoidance.getIntersection().y, 2);
//		renderer.end();
//		
////		renderer.begin(ShapeType.Line);
////		Vector3 inter = wallAvoidance.getIntersection();
////		renderer.line(inter.x, inter.y, inter.x + wallAvoidance.normal.len(), inter.y + wallAvoidance.normal.len());
////		renderer.end();
////		
//		renderer.begin(ShapeType.Filled);
//		renderer.circle(wallAvoidance.targetSeek.getPosition().x, wallAvoidance.targetSeek.getPosition().y, 2);
//		renderer.end();
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
