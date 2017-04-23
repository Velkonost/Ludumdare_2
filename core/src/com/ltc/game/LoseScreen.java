package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.compression.lzma.Base;

/**
 * Created by Danila on 23.04.2017.
 */
public class LoseScreen extends BaseScreen{
    public MainGame game;
    private SpriteBatch LS;
    float CAM_W = 1280, CAM_H = 720;
    private Texture losePik;
    private Sprite losePikch;
    ///winPikch 720*480

    public LoseScreen(MainGame game){
        super();
        this.game = game;
    }

    public void show() {
        LS = new SpriteBatch();
        losePik = game.getManager().get("lose.png");
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        LS.begin();
        LS.draw(losePik, 180, 120);
        LS.end();
    }
}
