package com.ltc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.ltc.game.entities.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.floor;

/**
 * @author Velkonost
 */
public class GameScreen extends BaseScreen {

    private final float UPDATE_TIME = 1/60f;
    private float timer;
    private HashMap<String, PlayerVlogerEntity> friendlyPlayers1;
    private HashMap<String, PlayerProgerEntity> friendlyPlayers2;
    public Socket socket;
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
    private ArrayList<WallEntiy> wall;
    private BotEntity bot;
    private ArrayList<Texture> tableTextures;
    private ArrayList<TableEntity> table;


    private Texture playerVlogerTexture;
    private Texture playerVlogerCameraTexture;
    private Texture playerProgerTexture;
    private Texture phoneTexture;
    private Texture botIdleTexture;
    private Texture botTexture;

    public WinScreen win;
    public LoseScreen lose;

    private boolean checkPlayer = false;

    public boolean isTelephoneCollision = false;

    private ArrayList<Texture> botsIdleTexture;

    private float deltatime, tmp = 0;

    private int a = 180;
    boolean kf = false;

    private GuiMenu guiMenu;
    SpriteBatch batch;
    public boolean myachinWin = false;

    private ArrayList<BotIdleEntity> botsIdle;

    public boolean collisionBtwPlayers = false;
    public boolean collisionVlogerWithBot = false;
    public boolean hasPhone;
    private boolean hasChecked = false;

    private TelephoneEntity telephone;
    private Texture telephoneTexture;

    public GameScreen(MainGame game, String choosenProg, String choosenVlog) {
        super(game);
        this.game = game;
        this.choosenProg = choosenProg;
        this.choosenVlog = choosenVlog;
        stage = new Stage(new FitViewport(1280, 720));
        world = new World(new Vector2(0, 0), true);

        wall = new ArrayList<WallEntiy>();
        table = new ArrayList<TableEntity>();
        tableTextures = new ArrayList<Texture>();
        botsIdle = new ArrayList();
    }

    @Override
    public void show() {
        win = new WinScreen(game);
        lose = new LoseScreen(game);
        connectSocket();
        configSocketEvents();
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(16, 9);
        camera.translate(0, 1);
        botsIdleTexture = new ArrayList<Texture>();
        getTextures();
        friendlyPlayers1 = new HashMap<String, PlayerVlogerEntity>();
        friendlyPlayers2 = new HashMap<String, PlayerProgerEntity>();
        batch = new SpriteBatch();
        Texture wallT = game.getManager().get("table8bit.jpg");
        Texture botTexture = game.getManager().get("player1hero.png");
        //for (int i = 0; i < 3; i++) bot.add((Texture) game.getManager().get("player" + (i + 1) + "hero.png"));
        for (int i = 0; i < 7; i++) tableTextures.add((Texture) game.getManager().get("table" + (i + 1) + ".png"));
        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                a--;
                if(a==0)
                {
                    kf = true;
                    game.setScreen(win);
                    cancel();
                }
            }

        }, 1, 1);

        wall.add(new WallEntiy(wallT, world, 14.5f, 0.5f, 21f, 1f, 10f, 0));
        wall.add(new WallEntiy(wallT, world, 25.5f, 6f, 1f, 12f, 0, 6.0f));
        wall.add(new WallEntiy(wallT, world, 24f, 12.0f, 4f, 1f, 1.5f, 0));
        wall.add(new WallEntiy(wallT, world, 4.0f, 5.0f, 1f, 9f, 0, 4.0f));
        wall.add(new WallEntiy(wallT, world, 22.5f, 14f, 1f, 2f, 0, 1f));
        wall.add(new WallEntiy(wallT, world, 22.5f, 18.5f, 1f, 9f, 0, 4f));
        wall.add(new WallEntiy(wallT, world, 21f, 22.5f, 6f, 1f, 2.5f, 0));
        wall.add(new WallEntiy(wallT, world, 18.5f, 20f, 1f, 4f, 0, 1.5f));
        wall.add(new WallEntiy(wallT, world, 16f, 18.5f, 4f, 1f, 1.5f, 0));
        wall.add(new WallEntiy(wallT, world, 14.5f, 20f, 1f, 2f, 0, 0.5f));
        wall.add(new WallEntiy(wallT, world, 12f, 20.5f, 4f, 1f, 1.5f, 0));
        wall.add(new WallEntiy(wallT, world, 10.5f, 19.5f, 1f, 1f, 0, 0));
        wall.add(new WallEntiy(wallT, world, 5.5f, 18.5f, 11f,1f, 5f, 0));
        wall.add(new WallEntiy(wallT, world, 0.5f, 14f, 1f, 8f, 0, 3.5f));
        wall.add(new WallEntiy(wallT, world, 2f, 9.5f, 4f,1f, 1f, 0));
        wall.add(new WallEntiy(wallT, world, 11.5f, 9.5f, 1f,1f, 0, 0));
        wall.add(new WallEntiy(wallT, world, 18.5f, 9.5f, 1f,1f, 0, 0));

        table.add(new TableEntity(tableTextures.get(6), world, 7f, 17f, 6f, 2f, 2.5f, 0.5f));
        table.add(new TableEntity(tableTextures.get(1), world, 20.f, 4.f, 2f, 6f, 0.5f, 2.5f));
        table.add(new TableEntity(tableTextures.get(2), world, 5.5f, 4.f, 2f, 6f, 0.5f, 2.5f));
        table.add(new TableEntity(tableTextures.get(3), world, 24.f, 4.f, 2f, 6f, 0.5f, 2.5f));
        table.add(new TableEntity(tableTextures.get(4), world, 10.5f, 4.f, 2f, 6f, 0.5f, 2.5f));
        table.add(new TableEntity(tableTextures.get(5), world, 15.f, 4.f, 2f, 6f, 0.5f, 2.5f));

        bot = new BotEntity(botTexture, world, 7.5f, 4.5f, 1f, 1f);

        playerVloger = new PlayerVlogerEntity(playerVlogerTexture, playerVlogerCameraTexture, this, world, 9f, 7f);
        playerProger = new PlayerProgerEntity(playerProgerTexture, phoneTexture, this, world, 6.5f, 3.5f);
        telephone = new TelephoneEntity(telephoneTexture, world, 19.5f, 21.5f, 1, 1, 0, 0);
        //guiMenu = new GuiMenu()
        font = new BitmapFont();
        sp = new SpriteBatch();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        for (int i = 1; i <= 3; i++) {
//            botsIdle.add(new BotIdleEntity(botsIdleTexture.get(i - 1), this, world, i * 5, i * 2));
//            stage.addActor(botsIdle.get(i - 1));
//        }

        stage.addActor(telephone);
        stage.addActor(bot);

        for (WallEntiy aWall : wall) {
            stage.addActor(aWall);
        }

        for (TableEntity aTable : table) {
            stage.addActor(aTable);
        }

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (playerProger != null) playerProger.processInput();
                if (playerVloger != null) playerVloger.processInput();

                if ((fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("telephone"))
                        || (fixtureA.getUserData().equals("telephone") && fixtureB.getUserData().equals("vloger"))) {
                    isTelephoneCollision = true;
                }

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


                if ((fixtureA.getUserData().equals("vloger") && fixtureB.getUserData().equals("telephone"))
                        || (fixtureA.getUserData().equals("telephone") && fixtureB.getUserData().equals("vloger"))) {
                    isTelephoneCollision = false;
                }

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

        telephoneTexture = game.getManager().get("telephone.png");

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


        //////////////////////////////////////для прогера
//        if(checkPlayer) game.setScreen(lose);
//        else if (kf) game.setScreen(win);
        ///////////////////////////////для блогера
        /*if(checkPlayer) game.setScreen(win);
        else if (kf) game.setScreen(lose);*/

        if(checkPlayer && !hasChecked)
        {
            stage.addActor(playerVloger);
            playerVloger.boom(true);
        }else if(!checkPlayer && !hasChecked){
            stage.addActor(playerProger);
            playerProger.boom(true);
        }


        if (bot != null) bot.processInput();

        /*if(kf)
        {
            game.setScreen(new GuiMenu(game, 'l', 0));
        }*/
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

        if (playerVloger != null) playerVloger.processInput();
        if (playerProger != null) playerProger.processInput();
        if(checkPlayer) {
            stage.getCamera().position.set(playerVloger.getX(),playerVloger.getY(), 0);
            for (HashMap.Entry<String, PlayerProgerEntity> entry : friendlyPlayers2.entrySet()) {

                stage.addActor(entry.getValue());
                entry.getValue().processInput();
            }
        }else{
            stage.getCamera().position.set(playerProger.getX(),playerProger.getY(), 0);
            for (HashMap.Entry<String, PlayerVlogerEntity> entry : friendlyPlayers1.entrySet()) {
               stage.addActor(entry.getValue());
                entry.getValue().processInput();

            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            {
                JSONObject data = new JSONObject();
                try {
                    data.put("x", playerProger.phoneX);
                    data.put("y", playerProger.phoneY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("phoneDropped", data);
            }
            playerProger.processInput();
            stage.getCamera().position.set(playerProger.getX(),playerProger.getY(), 0);
        }
        for (BotIdleEntity aBotsIdle : botsIdle) aBotsIdle.processInput();

      //  stage.getCamera().position.set(playerProger.getX(),playerProger.getY(), 0);

        world.step(delta, 6, 2);
        camera.update();
        renderer.render(world, camera.combined);
        stage.draw();
    }

    @Override
    public void hide() {
        playerVloger.detach();
        playerProger.detach();
        telephone.detach();
        for (BotIdleEntity aBotsIdle1 : botsIdle) aBotsIdle1.detach();

        playerVloger.remove();
        playerProger.remove();
        for (BotIdleEntity aBotsIdle : botsIdle) aBotsIdle.remove();
        for(int i = 0; i < wall.size(); i++){wall.get(i).detach();}
        for(int i = 0; i < wall.size(); i++){wall.get(i).remove();}
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
        timer += dt;
        if(checkPlayer)
        {
            if (timer >= UPDATE_TIME && playerVloger != null && playerVloger.hasMoved()) {
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
        }else{
            if(timer >= UPDATE_TIME && playerProger != null && playerProger.hasMoved()) {
                JSONObject data = new JSONObject();
                try {
                    data.put("x", playerProger.getBody().getPosition().x);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                try {
                    data.put("y", playerProger.getBody().getPosition().y);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("playerMoved", data);
            }
        }
    }

    public void connectSocket(){
        try {
            socket  = IO.socket("http://25.36.204.209:5665");
//            socket = IO.socket("http://766ee2e4.ngrok.io");
//            socket = IO.socket("http://94.251.109.165:80");
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
                try {
                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connect: " + playerId);

                    PlayerProgerEntity playerEntity = new PlayerProgerEntity(playerProgerTexture, playerProgerTexture, GameScreen.this, world, 1, 2);
                    friendlyPlayers2.put(playerId, playerEntity);

                }catch (JSONException e)
                {

                }


                // playerEntity.setPosition(1,2);
                //  stage.addActor(playerEntity);
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    if(!checkPlayer)
                    {
                        friendlyPlayers1.get(playerId).remove();
                        friendlyPlayers1.remove(playerId);
//                        game.setScreen(lose);
                    }else{
                        friendlyPlayers2.get(playerId).remove();
                        friendlyPlayers2.remove(playerId);

                    }


//                    game.setScreen(new GuiMenu(game, 'l', 0));
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    if(objects.length()>0) {
                        checkPlayer = false;
                        for (int i = 0; i < objects.length(); i++) {
                            PlayerVlogerEntity coopPlayer = new PlayerVlogerEntity(playerVlogerTexture, playerVlogerTexture, GameScreen.this, world, 1, 2);
                            //coopPlayer.setPosition(1,2);
                            Vector2 position = new Vector2();
                            position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                            position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                            coopPlayer.setPosition(position.x, position.y);
                            //  stage.addActor(coopPlayer);
                            friendlyPlayers1.put(objects.getJSONObject(i).getString("id"), coopPlayer);

                        }
                        Gdx.app.log("SocketIO", "TRUE NE GAVNO");
                    }else{
                        // stage.addActor(playerVloger);
                        checkPlayer = true;
                        /*for (int i = 0; i < objects.length(); i++) {
                            PlayerVlogerEntity coopPlayer = new PlayerVlogerEntity(playerVlogerTexture, playerVlogerTexture, GameScreen.this, world, 1, 2);
                            //coopPlayer.setPosition(1,2);
                            Vector2 position = new Vector2();
                            position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                            position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                            coopPlayer.setPosition(position.x, position.y);
                            //  stage.addActor(coopPlayer);
                            friendlyPlayers1.put(objects.getJSONObject(i).getString("id"), coopPlayer);
                        }*/
                        Gdx.app.log("SocketIO", "TRUE GAVNO");
                    }
                } catch(JSONException e){

                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String  playerId = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    Vector2 vector2 = new Vector2();
                    vector2.add(x.floatValue(), y.floatValue());
                    if(checkPlayer) {
                        if (friendlyPlayers2.get(playerId) != null) {
                            friendlyPlayers2.get(playerId).getBody().setTransform(vector2.x, vector2.y, 0);
                            playerProger.setPosition(vector2.x, vector2.y);
                        }
                    }else{
                        if (friendlyPlayers1.get(playerId) != null) {
                            friendlyPlayers1.get(playerId).getBody().setTransform(vector2.x, vector2.y, 0);
                            playerVloger.setPosition(vector2.x, vector2.y);
                        }
                    }
                } catch (org.json.JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }

            }
        }).on("getPhone", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONArray objects = (JSONArray) args[0];
                if(objects.length()>0) {
                    try {
                        Gdx.app.log("SOCK", "VERC");
                        String playerId = objects.getJSONObject(0).getString("id");
                        stage.addActor(new TelephoneEntity(telephoneTexture, world, (float) objects.getJSONObject(0).getDouble("x"), (float) objects.getJSONObject(0).getDouble("y"), friendlyPlayers1.get(playerId).getWidth() / 2, friendlyPlayers1.get(playerId).getHeight() / 2, 0, 0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}