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
    private float xrama = -150, yrama = -150, xrama2 = -150, yrama2=-150;
    private Sprite skin, skpl1, skpl2, rama, rama2, skvl1, skvl2;

    private Texture pl1, pl2, vl1, vl2, btnPlay, ramatext, ramatext2;
    private Texture chooseProg, chooseVlog;
    private String choosenProg = "", choosenVlog="";
    public Menu(MainGame game) {
        super();
        this.game = game;
    }

    @Override
    public void show() {
        sp = new SpriteBatch();
        ramatext = new Texture("ramka.png");
        ramatext2 = new Texture("ramka2.png");
        btnPlay = new Texture("btnPlay.png");

        vl1 = new Texture("myach.png");
        vl2 = new Texture("myach2.png");

        pl1 = new Texture("player1.png");
        pl2 = new Texture("player2.png");

        chooseProg = new Texture("chooseYourProger.png");
        chooseVlog = new Texture("chooseYourVloger.png");

        skin = new Sprite(btnPlay); // your image
        skin.setPosition(Gdx.graphics.getWidth()/2-350, Gdx.graphics.getHeight() / 2-200);
        skin.setSize(700, 200);

        skpl1 = new Sprite(pl1);
        skpl1.setPosition(100, Gdx.graphics.getHeight() / 2+25);
        skpl1.setSize(150, 150);

        skpl2 = new Sprite(pl2);
        skpl2.setPosition(300, Gdx.graphics.getHeight() / 2+25);
        skpl2.setSize(150, 150);

        skvl1 = new Sprite(vl1);
        skvl1.setPosition(Gdx.graphics.getWidth()-250, Gdx.graphics.getHeight() / 2+25);
        skvl1.setSize(150, 150);

        skvl2 = new Sprite(vl2);
        skvl2.setPosition(Gdx.graphics.getWidth()-450, Gdx.graphics.getHeight() / 2+25);
        skvl2.setSize(150, 150);

        rama = new Sprite(ramatext);
        rama.setSize(150, 150);
        rama.setPosition(xrama, yrama);

        rama2 = new Sprite(ramatext2);
        rama2.setSize(150, 150);
        rama2.setPosition(xrama2, yrama2);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sp.begin();

        sp.draw(chooseProg, 10, Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/4);
        sp.draw(chooseVlog, Gdx.graphics.getWidth()-chooseVlog.getWidth()-10, Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/4);


        skin.draw(sp);
        skpl1.draw(sp);
        skpl2.draw(sp);
        skvl1.draw(sp);
        skvl2.draw(sp);

        rama.draw(sp);
        rama2.draw(sp);
        if(Gdx.input.justTouched())
        {
            if(Gdx.input.getX()>=skin.getX() && Gdx.input.getX()<=skin.getX()+700 &&
                    Gdx.input.getY()>=skin.getY()+200 && Gdx.input.getY()<=skin.getY()+400) {
                    if(choosenVlog.length()>0 && choosenProg.length()>0) {
                        game.setScreen(new GameScreen(game, choosenProg, choosenVlog));
                    }else{

                    }

            }

            if(Gdx.input.getX()>=skpl1.getX() && Gdx.input.getX()<=skpl1.getX()+150 &&
                    Gdx.input.getY()>=skpl1.getY()-250 && Gdx.input.getY()<=skpl1.getY()-100){
                xrama = skpl1.getX();
                yrama = skpl1.getY();
                rama.setPosition(xrama, yrama);
                choosenProg = "Tyoma";
            }

            if(Gdx.input.getX()>=skpl2.getX() && Gdx.input.getX()<=skpl2.getX()+150 &&
                    Gdx.input.getY()>=skpl2.getY()-250 && Gdx.input.getY()<=skpl2.getY()-100){
                xrama = skpl2.getX();
                yrama = skpl2.getY();
                rama.setPosition(xrama, yrama);
                choosenProg = "Andrey";
            }
            if(Gdx.input.getX()>=skvl1.getX() && Gdx.input.getX()<=skvl1.getX()+150 &&
                    Gdx.input.getY()>=skvl1.getY()-250 && Gdx.input.getY()<=skvl1.getY()-100){
                xrama2 = skvl1.getX();
                yrama2 = skvl1.getY();
                rama2.setPosition(xrama2, yrama2);
                choosenVlog = "Myach1";
            }
            if(Gdx.input.getX()>=skvl2.getX() && Gdx.input.getX()<=skvl2.getX()+150 &&
                    Gdx.input.getY()>=skvl2.getY()-250 && Gdx.input.getY()<=skvl2.getY()-100){
                xrama2 = skvl2.getX();
                yrama2 = skvl2.getY();
                rama2.setPosition(xrama2, yrama2);
                choosenVlog = "Myach2";
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
