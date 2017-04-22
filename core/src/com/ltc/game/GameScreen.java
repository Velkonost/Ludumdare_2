package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ltc.game.entities.PlayerProgerEntity;
import com.ltc.game.entities.PlayerVlogerEntity;
import com.ltc.game.entities.WallEntiy;

import static com.ltc.game.entities.PlayerProgerEntity.keys;

/**
 * @author Velkonost
 */
public class GameScreen extends BaseScreen {

    private final float UPDATE_TIME = 1 / 60f;

    private Stage stage;

    private World world;

    private Box2DDebugRenderer renderer;

    private OrthographicCamera camera;

    private PlayerVlogerEntity playerVloger;
    private PlayerProgerEntity playerProger;
    private WallEntiy wall;

    Vector2 vector2;

    public boolean collisionBtwPlayers = false;
    float xdo = 0, ydo = 0, xpo = 0, ypo = 0, speed = 2f, x = 0, y = 0;


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

        Texture playerVlogerTexture = game.getManager().get("myach.png");
        Texture playerProgerTexture = game.getManager().get("player2.png");
        //Texture wall = game.getManager().get();

        playerVloger = new PlayerVlogerEntity(playerVlogerTexture, this, world, 1, 2);
        playerProger = new PlayerProgerEntity(playerProgerTexture, this, world, 6.5f, 3.5f);
        //wall = new WallEntiy();

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

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        playerVloger.processInput();
        playerProger.processInput();

       /* if(keys.get(PlayerProgerEntity.KeysProger.LEFT)){
           // stage.getCamera().translate(-1.517f, 0, 0);
            stage.getCamera().position.set(playerProger.getX(),playerProger.getY(),0);
        }
        if(keys.get(PlayerProgerEntity.KeysProger.RIGHT)){
            stage.getCamera().translate(1.517f,0,0);
            stage.getCamera().position.set(playerProger.getX(),playerProger.getY(),0);
        }
        if(keys.get(PlayerProgerEntity.KeysProger.DOWN)){
            stage.getCamera().translate(0,-1.517f,0);
        }
        if(keys.get(PlayerProgerEntity.KeysProger.UP)){
            stage.getCamera().translate(0,1.517f,0);
        }*/
       stage.getCamera().position.set(playerProger.getX(),playerProger.getY(), 0);

        world.step(delta, 6, 2);
        stage.getCamera().position.set(playerProger.getX(), playerProger.getY(), 0);
        camera.lookAt(playerProger.getX(), playerProger.getY(), 0);
        camera.update();
        renderer.render(world, camera.combined);
        stage.draw();
    }

    @Override
    public void hide() {
        playerVloger.detach();
        playerProger.detach();

        playerVloger.remove();
        playerProger.remove();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        renderer.dispose();
    }
}
