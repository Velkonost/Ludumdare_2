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
		manager.load("player1hero.png", Texture.class);
		manager.load("player2.png", Texture.class);
		manager.load("player2hero.png", Texture.class);
		manager.load("myach.png", Texture.class);
		manager.load("myachhero.png", Texture.class);
		manager.load("myachheroCamera.png", Texture.class);
		manager.load("myach2.png", Texture.class);
		manager.load("myach2hero.png", Texture.class);
		manager.load("myach2heroCamera.png", Texture.class);
		manager.load("ramka.png", Texture.class);
		manager.load("ramka2.png", Texture.class);
		manager.load("btnPlay.png", Texture.class);
		manager.load("chooseYourProger.png", Texture.class);
		manager.load("chooseYourVloger.png", Texture.class);
		manager.load("mobile.png", Texture.class);
		manager.load("player3hero.png", Texture.class);
		manager.load("table8bit.jpg", Texture.class);
		manager.load("table1.png", Texture.class);
		manager.load("table2.png", Texture.class);
		manager.load("table3.png", Texture.class);
		manager.load("table4.png", Texture.class);
		manager.load("table5.png", Texture.class);
		manager.load("table6.png", Texture.class);
		manager.load("table7.png", Texture.class);


		manager.finishLoading();

		setScreen(new MenuScreen(this));
	}

}
