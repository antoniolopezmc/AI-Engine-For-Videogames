package com.mygdx.iadevproject.aiReactive.behaviour.delegated;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.iadevproject.aiReactive.behaviour.delegated.TestLookingWhereYouGoing;

public class TestLookingWhereYouGoingLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 2000;
		//config.height = 1000;
		new LwjglApplication(new TestLookingWhereYouGoing(), config);
	}
}
