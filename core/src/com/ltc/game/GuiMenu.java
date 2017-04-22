package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sun.java.accessibility.util.GUIInitializedListener;

/**
 * Created by Olga on 22.04.2017.
 */
public class GuiMenu extends BaseScreen {

    private Texture texture;
    private SpriteBatch sp;
    private int r;
    public GuiMenu(MainGame game, char result, int secs)
    {
        if (result == 'w') {
            texture = new Texture("table1.png");
            r = 0;
        }else if(result == 'l')
        {
            texture = new Texture("table2.png");
            r = 1;
        }
    }

    @Override
    public void show() {
        sp = new SpriteBatch();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        sp.begin();
        sp.draw(texture, 0+100, Gdx.graphics.getHeight()/2);
        sp.end();
    }

}
