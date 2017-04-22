package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ltc.game.entities.BotIdleEntity;
import com.ltc.game.entities.PlayerProgerEntity;
import com.ltc.game.entities.PlayerVlogerEntity;
import com.ltc.game.entities.WallEntiy;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import io.socket.emitter.Emitter;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.floor;

/**
 * @author Velkonost
 */
public class GameScreen extends BaseScreen {

    private final float UPDATE_TIME = 1/60f;
    private float timer;
    private HashMap<String, PlayerVlogerEntity> friendlyPlayers;
    private Socket socket;
    private String id;

    private Stage stage;

    private World world;
    public MainGame game;

    private Box2DDebugRenderer renderer;

    BitmapFont font;
    SpriteBatch sp;
    CharSequence str;

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

    private ArrayList<Texture> botsIdleTexture;

    private float deltatime, tmp = 0;

    private int a = 180;
    boolean kf = false;

    private GuiMenu guiMenu;

    private ArrayList<BotIdleEntity> botsIdle;

    public boolean collisionBtwPlayers = false;
    public boolean collisionVlogerWithBot = false;
    public boolean hasPhone;

    public GameScreen(MainGame game, String choosenProg, String choosenVlog) {
        super(game);
        this.game = game;
        this.choosenProg = choosenProg;
        this.choosenVlog = choosenVlog;
        stage = new Stage(new FitViewport(1280, 720));
        world = new World(new Vector2(0, 0), true);

        botsIdle = new ArrayList();
    }

    @Override
    public void show() {
        connectSocket();
        configSocketEvents();
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(4, 2.25f);
        camera.translate(0, 1);
        botsIdleTexture = new ArrayList<Texture>();
        getTextures();
        friendlyPlayers = new HashMap<String, PlayerVlogerEntity>();

        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                a--;
                if(a==0)
                {
                    kf = true;
                    cancel();
                }
            }

        }, 1, 1);
        playerVloger = new PlayerVlogerEntity(playerVlogerTexture, playerVlogerCameraTexture, this, world, 1, 2);
        playerProger = new PlayerProgerEntity(playerProgerTexture, phoneTexture, this, world, 6.5f, 3.5f);
        //guiMenu = new GuiMenu()
        font = new BitmapFont();
        sp = new SpriteBatch();


//        for (int i = 1; i <= 3; i++) {
//            botsIdle.add(new BotIdleEntity(botsIdleTexture.get(i - 1), this, world, i * 5, i * 2));
//            stage.addActor(botsIdle.get(i - 1));
//        }

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

        if (choosenVlog.equals("myach1")) {
            playerVlogerTexture = game.getManager().get("myachhero.png");
            playerVlogerCameraTexture = game.getManager().get("myachheroCamera.png");
        }
        else {
            playerVlogerTexture = game.getManager().get("myach2hero.png");
            playerVlogerCameraTexture = game.getManager().get("myach2heroCamera.png");
        }


        if(choosenProg.equals("player1")) {
            playerProgerTexture = game.getManager().get("player1hero.png");
        } else if (choosenProg.equals("player2")) {
            playerProgerTexture = game.getManager().get("player2hero.png");
        }

        for (int i = 1; i <= 3; i++) {

            botsIdleTexture.add(
                    (Texture) game.getManager().get("player" + i + "hero.png")
            );
        }

        phoneTexture = game.getManager().get("mobile.png");
        botIdleTexture = game.getManager().get("myach2hero.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateServer(Gdx.graphics.getDeltaTime());

        if(kf)
        {
            game.setScreen(new GuiMenu(game, 'l', 0));
        }
        if(a%60>9){
            str = ""+(int)floor(a/60)+":"+a%60;
        }else{
            str = ""+(int)floor(a/60)+":0"+a%60;
        }

        sp.begin();
        font.getData().setScale(3, 3);
        font.draw(sp, str, Gdx.graphics.getWidth()-200,  Gdx.graphics.getHeight()-50);
        sp.end();

        stage.act();
//
        playerVloger.processInput();
        playerProger.processInput();
        for(HashMap.Entry<String, PlayerVlogerEntity> entry : friendlyPlayers.entrySet()){

            stage.addActor(entry.getValue());
        }
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

    public void updateServer(float dt)
    {
        timer+=dt;
        if(timer >= UPDATE_TIME && playerVloger!=null && playerVloger.hasMoved())
        {
            JSONObject data = new JSONObject();
                try {
                    data.put("x", playerVloger.getBody().getPosition().x);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                try {
                    data.put("y", playerVloger.getBody().getPosition().y);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("playerMoved", data);

        }
    }

    public void connectSocket(){
        try {
            socket = IO.socket("http://localhost:3000");
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }
    public void configSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
               /* Texture playerTexture = game.getManager().get("hero.png");
                player = new PlayerEntity(playerTexture, world, new Vector2(1, 2));*/
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + playerId);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String playerId = null;
                try {
                    playerId = data.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gdx.app.log("SocketIO", "New Player Connect: " + id);
                Texture floorTexture = game.getManager().get("badlogic.jpg");
                PlayerVlogerEntity playerEntity = new PlayerVlogerEntity(playerVlogerTexture, playerVlogerCameraTexture, GameScreen.this, world, 1, 2);
                // playerEntity.setPosition(1,2);
                //  stage.addActor(playerEntity);
                friendlyPlayers.put(playerId, playerEntity);
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    friendlyPlayers.get(playerId).remove();
                    friendlyPlayers.remove(playerId);

                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = null;
                    try {
                        playerId = data.getString("id");
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    Vector2 vector2 = new Vector2();
                    vector2.add(x.floatValue(), y.floatValue());
                    if(friendlyPlayers.get(playerId)!=null)
                    {
                        friendlyPlayers.get(playerId).getBody().setTransform(vector2.x, vector2.y, 0 );
                    }
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    for(int i = 0; i < objects.length(); i++){
                        Texture floorTexture = game.getManager().get("badlogic.jpg");
                        PlayerVlogerEntity coopPlayer = new PlayerVlogerEntity(playerVlogerTexture, playerVlogerCameraTexture, GameScreen.this, world, 1, 2);
                        //coopPlayer.setPosition(1,2);
                        Vector2 position = new Vector2();
                        position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                        coopPlayer.setPosition(position.x, position.y);
                        //  stage.addActor(coopPlayer);
                        friendlyPlayers.put(objects.getJSONObject(i).getString("id"), coopPlayer);
                    }
                } catch(JSONException e){

                }
            }
        });
    }
}
