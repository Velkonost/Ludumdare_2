package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ltc.game.entities.PlayerEntity;

/**
 * @author Velkonost
 */
public class GameScreen extends BaseScreen {

    private final float UPDATE_TIME = 1 / 60f;

    private Stage stage;

    private World world;

    private Box2DDebugRenderer renderer;

    private OrthographicCamera camera;

    private PlayerEntity player;


    public GameScreen(MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        world = new World(new Vector2(0, 0), true);
    }

    @Override
    public void show() {
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(16, 9);
        camera.translate(0, 1);
        Texture playerTexture = game.getManager().get("badlogic.jpg");

        player = new PlayerEntity(playerTexture, world, 1, 2);

        stage.addActor(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        player.processInput();
        world.step(delta, 6, 2);
        camera.update();
        renderer.render(world, camera.combined);
        stage.draw();
    }

    @Override
    public void hide() {
        player.detach();
        player.remove();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        renderer.dispose();
    }
}
