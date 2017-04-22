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
    private float xrama = -150, yrama = -150;
    private Sprite skin, skpl1, skpl2, rama;

    private Texture pl1, pl2, btnPlay, ramatext;
    private Texture chooseProg, chooseVlog;
    public Menu(MainGame game) {
        super();
        this.game = game;
    }

    @Override
    public void show() {
        sp = new SpriteBatch();
        ramatext = new Texture("ramka.png");
        btnPlay = new Texture("btnPlay.png");
        pl1 = new Texture("player1.png");
        pl2 = new Texture("player2.png");
        chooseProg = new Texture("chooseYourProger.png");

        skin = new Sprite(btnPlay); // your image
        skin.setPosition(Gdx.graphics.getWidth()/2-350, Gdx.graphics.getHeight() / 2-200);
        skin.setSize(700, 200);

        skpl1 = new Sprite(pl1);
        skpl1.setPosition(100, Gdx.graphics.getHeight() / 2+25);
        skpl1.setSize(150, 150);

        skpl2 = new Sprite(pl2);
        skpl2.setPosition(300, Gdx.graphics.getHeight() / 2+25);
        skpl2.setSize(150, 150);

        rama = new Sprite(ramatext);
        rama.setSize(150, 150);
        rama.setPosition(xrama, yrama);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sp.begin();
        sp.draw(chooseProg, 10, Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/4);
        skin.draw(sp);
        skpl1.draw(sp);
        skpl2.draw(sp);
        rama.draw(sp);
        if(Gdx.input.justTouched())
        {
            if(Gdx.input.getX()>=skin.getX() && Gdx.input.getX()<=skin.getX()+700 &&
                    Gdx.input.getY()>=skin.getY()+200 && Gdx.input.getY()<=skin.getY()+400) {
                    game.setScreen(new GameScreen(game));

            }
            if(Gdx.input.getX()>=skpl1.getX() && Gdx.input.getX()<=skpl1.getX()+150 &&
                    Gdx.input.getY()>=skpl1.getY()-250 && Gdx.input.getY()<=skpl1.getY()-100){
                xrama = skpl1.getX();
                yrama = skpl1.getY();
                rama.setPosition(xrama, yrama);
            }

            if(Gdx.input.getX()>=skpl2.getX() && Gdx.input.getX()<=skpl2.getX()+150 &&
                    Gdx.input.getY()>=skpl2.getY()-250 && Gdx.input.getY()<=skpl2.getY()-100){
                xrama = skpl2.getX();
                yrama = skpl2.getY();
                rama.setPosition(xrama, yrama);
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
