package com.ltc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import javafx.stage.Screen;

public class Menu extends BaseScreen {

    public MainGame game;
    private SpriteBatch sp;
    ImageButton imgBtn;
    private Sprite skin;

    private Texture pl1, pl2, btnPlay;
    public Menu(MainGame game) {
        super();
        this.game = game;
    }

    @Override
    public void show() {
        sp = new SpriteBatch();
        btnPlay = new Texture("btnPlay.png");
        skin = new Sprite(btnPlay); // your image
        skin.setPosition(Gdx.graphics.getWidth()/2-350, Gdx.graphics.getHeight() / 2-200);
        skin.setSize(700, 200);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sp.begin();
        skin.draw(sp);
        if(Gdx.input.justTouched())
        {
            if(Gdx.input.getX()>=skin.getX() && Gdx.input.getX()<=skin.getX()+700 &&
                    Gdx.input.getY()>=skin.getY()+200 && Gdx.input.getY()<=skin.getY()+400) {
                    game.setScreen(new GameScreen(game));
            }

        }
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
