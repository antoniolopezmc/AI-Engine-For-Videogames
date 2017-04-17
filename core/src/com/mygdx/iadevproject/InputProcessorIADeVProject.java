package com.mygdx.iadevproject;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.iadevproject.model.WorldObject;

/**
 * Clase que implementa el InputProcessor del projecto.
 */
public class InputProcessorIADeVProject implements InputProcessor {
	private boolean keyPressed = false;		// Indica que se ha pulsado una tecla
	private int lastKey;					// Última tecla pulsada
	
	/**
	 * Este método ha sido creado porque InputProcessor no tiene ningún método para controlar
	 * que una tecla se mantenga pulsada. Lo que hace es comprobar si hay una tecla pulsada
	 * y si es así, llama al método 'keyDown' con la tecla pulsada para procesarla.
	 */
	public void processKeyPressed() {
		if (keyPressed) {
			keyDown(lastKey);
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		keyPressed = true;
		lastKey = keycode;
		
		switch (keycode) {
		case Input.Keys.LEFT: 				// Movimiento hacia la izquierda
			IADeVProject.camera.translate(-5, 0, 0);
//			IADeVProject.drop.setPosition(IADeVProject.drop.getPosition().add(-5,0,0));
			break;
		case Input.Keys.RIGHT:				// Movimiento hacia la derecha
			IADeVProject.camera.translate(5, 0, 0);
//			IADeVProject.drop.setPosition(IADeVProject.drop.getPosition().add(5, 0, 0));
			break;
		case Input.Keys.DOWN:				// Movimiento hacia abajo
			IADeVProject.camera.translate(0, -5, 0);
//			IADeVProject.drop.setPosition(IADeVProject.drop.getPosition().add(0, -5, 0));
			break;
		case Input.Keys.UP:					// Movimiento hacia arriba
			IADeVProject.camera.translate(0, 5, 0);
//			IADeVProject.drop.setPosition(IADeVProject.drop.getPosition().add(0, 5, 0));
			break;
		case Input.Keys.A:					// Alejar la cámara
			IADeVProject.camera.zoom += 0.02;	
			break;
		case Input.Keys.Q:					// Acercar la cámara
			IADeVProject.camera.zoom -= 0.02;
			break;
		default:
			break;
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyPressed = false;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		// Si el botón presionado es el botón IZQUIERDO, se quiere seleccionar a un personaje
		if (button == Input.Buttons.LEFT) {
			
			// Obtenemos las coordenadas de la cámara correspondiente a las coordenadas de la pantalla
			Vector3 touchPos = new Vector3();
			touchPos.set(screenX, screenY, 0);
			IADeVProject.camera.unproject(touchPos);
			
			
			
			IADeVProject.bucket.setPosition(touchPos);
			
			
			
			
			// Para todo objeto del mundo
			for (WorldObject obj : IADeVProject.worldObjects) {
				// Comprobamos si se ha pinchado sobre él
				if (obj.getBoundingRectangle().contains(new Vector2(touchPos.x, touchPos.y))) { 
					// Si se ha pinchado sobre él, añadimos a la lista de objetos seleccionados
					IADeVProject.addToSelectedObjectsList(obj);
				}
			}
		} else if (button == Input.Buttons.RIGHT) {
			// Si el botón presionado es el botón DERECHO, limpiamos la lista de objetos seleccionados.
			IADeVProject.clearSelectedObjectsList();
		}
		
		// Mostramos la lista de objetos seleccionados para asegurarnos de que se ha introducido.
		if (!IADeVProject.selectedObjects.isEmpty()) {
			System.out.println("\n--------------\nSelected objects:");
			for (WorldObject obj : IADeVProject.selectedObjects) {
				System.out.println(obj.getX() + " - " + obj.getY());
			}
		}
		
		// Devolvemos true indicando que se ha procesado el evento
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
