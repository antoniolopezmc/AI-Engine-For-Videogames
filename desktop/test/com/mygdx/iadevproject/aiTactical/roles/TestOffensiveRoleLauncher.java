package com.mygdx.iadevproject.aiTactical.roles;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class TestOffensiveRoleLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.width = 1024;
		config.height = 700;
		new LwjglApplication(new TestOffensiveRole(), config);
	}
}
