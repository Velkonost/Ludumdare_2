package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Olga on 22.04.2017.
 */
public class GuiMenu extends BaseScreen {

    private Texture texture;
    private SpriteBatch sp;
    private int r;
    private char result;
    public GuiMenu(MainGame game, char result, int secs)
    {
        this.game = game;
        this.result = result;

        sp = new SpriteBatch();
    }

    @Override
    public void show() {



        if (result == 'w') {
            texture = game.getManager().get("table1.png");
            r = 0;
        }else if(result == 'l')
        {
            texture = game.getManager().get("table1.png");
            r = 1;
        }
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
