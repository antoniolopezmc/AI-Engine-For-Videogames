package com.mygdx.iadevproject.behaviour.delegated;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.iadevproject.behaviour.delegated.TestWallAvoidance;

public class TestWallAvoidanceLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 640;
		new LwjglApplication(new TestWallAvoidance(), config);
	}
}
