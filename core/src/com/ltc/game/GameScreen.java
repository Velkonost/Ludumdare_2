package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ltc.game.entities.BotIdleEntity;
import com.ltc.game.entities.PlayerProgerEntity;
import com.ltc.game.entities.PlayerVlogerEntity;
import com.ltc.game.entities.WallEntiy;

import java.util.ArrayList;

/**
 * @author Velkonost
 */
public class GameScreen extends BaseScreen {

    private final float UPDATE_TIME = 1 / 60f;

    private Stage stage;

    private World world;

    private Box2DDebugRenderer renderer;

    private OrthographicCamera camera;

    private String choosenProg, choosenVlog;
    private PlayerVlogerEntity playerVloger;
    private PlayerProgerEntity playerProger;
    private WallEntiy wall;


    private Texture playerVlogerTexture;
    private Texture playerVlogerCameraTexture;
    private Texture playerProgerTexture;
    private Texture phoneTexture;
    private Texture botIdleTexture;

    private ArrayList<BotIdleEntity> botsIdle;

    public boolean collisionBtwPlayers = false;
    public boolean collisionVlogerWithBot = false;
    public boolean hasPhone;

    public GameScreen(MainGame game, String choosenProg, String choosenVlog) {
        super(game);
        this.choosenProg = choosenProg;
        this.choosenVlog = choosenVlog;
        stage = new Stage(new FitViewport(1280, 720));
        world = new World(new Vector2(0, 0), true);

        botsIdle = new ArrayList();
    }

    @Override
    public void show() {
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(16, 9);
        camera.translate(0, 1);

        getTextures();

        playerVloger = new PlayerVlogerEntity(playerVlogerTexture, playerVlogerCameraTexture, this, world, 1, 2);
        playerProger = new PlayerProgerEntity(playerProgerTexture, phoneTexture, this, world, 6.5f, 3.5f);

        for (int i = 0; i < 5; i++) {
            botsIdle.add(new BotIdleEntity(botIdleTexture, this, world, i * 5, i * 2));
            stage.addActor(botsIdle.get(i));
        }

        stage.addActor(playerVloger);
        stage.addActor(playerProger);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if ((fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("proger"))
                        || (fixtureA.getUserData().equals("proger") && fixtureB.getUserData().equals("vloger"))) {
                    collisionBtwPlayers = true;
                    hasPhone = playerProger.isHasPhone();
                }

                if (
                        (fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("proger"))
                        || (fixtureA.getUserData().equals("proger") && fixtureB.getUserData().equals("vloger"))
                        || (fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("botidle"))
                        || (fixtureA.getUserData().equals("botidle") && fixtureB.getUserData().equals("vloger"))
                        || (fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("botmove"))
                        || (fixtureA.getUserData().equals("botmove") && fixtureB.getUserData().equals("vloger"))
                        ) {
                    collisionVlogerWithBot = true;

                    if (
                            (fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("botidle"))
                            || (fixtureA.getUserData().equals("botidle") && fixtureB.getUserData().equals("vloger"))
                            || (fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("botmove"))
                            || (fixtureA.getUserData().equals("botmove") && fixtureB.getUserData().equals("vloger"))
                            ) {
                        hasPhone = true;
                    }
                }
            }



            @Override
            public void endContact(Contact contact) {

                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if ((fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("proger"))
                        || (fixtureA.getUserData().equals("proger") && fixtureB.getUserData().equals("vloger"))) {
                    collisionBtwPlayers = false;
                }

                if (
                        (fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("proger"))
                                || (fixtureA.getUserData().equals("proger") && fixtureB.getUserData().equals("vloger"))
                                || (fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("botidle"))
                                || (fixtureA.getUserData().equals("botidle") && fixtureB.getUserData().equals("vloger"))
                                || (fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("botmove"))
                                || (fixtureA.getUserData().equals("botmove") && fixtureB.getUserData().equals("vloger"))
                        ) {
                    collisionVlogerWithBot = false;
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    private void getTextures() {
        playerVlogerTexture = game.getManager().get("myachhero.png");
        playerVlogerCameraTexture = game.getManager().get("myachheroCamera.png");
        playerProgerTexture = game.getManager().get("player2hero.png");
        phoneTexture = game.getManager().get("mobile.png");
        botIdleTexture = game.getManager().get("myach2hero.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        playerVloger.processInput();
        playerProger.processInput();

        for (BotIdleEntity aBotsIdle : botsIdle) aBotsIdle.processInput();

        stage.getCamera().position.set(playerProger.getX(),playerProger.getY(), 0);

        world.step(delta, 6, 2);
        camera.update();
        renderer.render(world, camera.combined);
        stage.draw();
    }

    @Override
    public void hide() {
        playerVloger.detach();
        playerProger.detach();
        for (BotIdleEntity aBotsIdle1 : botsIdle) aBotsIdle1.detach();

        playerVloger.remove();
        playerProger.remove();
        for (BotIdleEntity aBotsIdle : botsIdle) aBotsIdle.remove();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        renderer.dispose();
    }

    public PlayerVlogerEntity getPlayerVloger() {
        return playerVloger;
    }

    public PlayerProgerEntity getPlayerProger() {
        return playerProger;
    }

    public boolean isHasPhone() {
        return hasPhone;
    }
}
