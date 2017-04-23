package com.ltc.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.security.Key;

/**
 * Created by Danila on 23.04.2017.
 */
public class WinScreen extends BaseScreen{
    public MainGame game;
    GameScreen GS;
    private SpriteBatch WS;
    float CAM_W = 1280, CAM_H = 720;
    private Texture winPik;
    private Sprite winPikch;
    ///winPikch 720*480

    public WinScreen(MainGame game){
        super();
        this.game = game;
    }

    public void show() {
        WS = new SpriteBatch();
        winPik = game.getManager().get("myach.png");
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        WS.begin();
        WS.draw(winPik, 180, 120);
        WS.end();
    }
}
