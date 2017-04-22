package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import javafx.stage.Screen;

public class Menu extends BaseScreen {

    public MainGame game;
    private SpriteBatch sp;

    private Texture pl1, pl2, btnPlay;
    public Menu(MainGame game) {
        super();
        this.game = game;
    }

    @Override
    public void show() {
        btnPlay = new Texture("btnPlay.png");
        sp = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sp.begin();
        sp.draw(btnPlay,(float) Gdx.graphics.getWidth()/2-350,(float) Gdx.graphics.getHeight() / 2-200, 700, 200);
        sp.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
