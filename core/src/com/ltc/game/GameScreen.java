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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Velkonost
 */
public class GameScreen extends BaseScreen {

    private final float UPDATE_TIME = 1 / 60f;

    //направление движения
    enum Keys {
        LEFT, RIGHT, UP, DOWN
    }

    static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();

    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
    };

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
//        player.setPosition(1, 2);

        stage.addActor(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        processInput();
        player.update(delta);

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

    private void processInput() {
        if (keys.get(Keys.LEFT))
            player.getBody().setLinearVelocity(-PlayerEntity.SPEED, player.getBody().getLinearVelocity().y);
        if (keys.get(Keys.RIGHT))
            player.getBody().setLinearVelocity(PlayerEntity.SPEED, player.getBody().getLinearVelocity().y);
        if (keys.get(Keys.UP))
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, PlayerEntity.SPEED);
        if (keys.get(Keys.DOWN))
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, -PlayerEntity.SPEED);
//если не выбрано направление, то стоим на месте
        if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) || (!keys.get(Keys.LEFT) && (!keys.get(Keys.RIGHT))))
            player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
        if ((keys.get(Keys.UP) && keys.get(Keys.DOWN)) || (!keys.get(Keys.UP) && (!keys.get(Keys.DOWN))))
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);

    }
}
