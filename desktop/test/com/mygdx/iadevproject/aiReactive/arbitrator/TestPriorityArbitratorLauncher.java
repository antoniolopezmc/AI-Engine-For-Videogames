package com.mygdx.iadevproject.aiReactive.arbitrator;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.iadevproject.aiReactive.arbitrator.TestPriorityArbitrator;

public class TestPriorityArbitratorLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 2000;
		//config.height = 1000;
		new LwjglApplication(new TestPriorityArbitrator(), config);
	}

}
