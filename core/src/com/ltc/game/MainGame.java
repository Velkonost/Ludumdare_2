package com.ltc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {


	private AssetManager manager;

	public AssetManager getManager() {
		return manager;
	}

	@Override
	public void create () {
		manager = new AssetManager();
		manager.load("badlogic.jpg", Texture.class);
		manager.load("player1.png", Texture.class);
		manager.load("player2.png", Texture.class);
		manager.load("myach.png", Texture.class);

		manager.finishLoading();

		setScreen(new GameScreen(this));
	}

}
