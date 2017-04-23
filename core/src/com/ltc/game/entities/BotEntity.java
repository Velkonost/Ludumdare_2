package com.ltc.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

import static com.ltc.game.Constants.PIXELS_IN_METER;

/**
 * Created by Danila on 23.04.2017.
 */
public class BotEntity extends Actor{
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    float width, higth, x, y;
    Vector2 previousPosition = new Vector2(1, 1);

    public enum KeysBot {
        LEFT, RIGHT, UP, DOWN
    }

    private Map<BotEntity.KeysBot, Boolean> keys = new HashMap<BotEntity.KeysBot, Boolean>();

    {
        keys.put(BotEntity.KeysBot.LEFT, false);
        keys.put(BotEntity.KeysBot.RIGHT, false);
        keys.put(BotEntity.KeysBot.UP, false);
        keys.put(BotEntity.KeysBot.DOWN, false);

    }


    public BotEntity(Texture texture, World world,  float x, float y, float widht, float higth){

        this.texture = texture;
//        this.phoneTexture = phoneTexture;
        this.world = world;
//        this.game = game;
        previousPosition = new Vector2(getX(), getY());
        setPosition(x, y);

        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(def);
        body.setFixedRotation(true);

        final PolygonShape box = new PolygonShape();
        box.setAsBox(0.25f, 0.5f);

        fixture = body.createFixture(box, 1000);
        fixture.setUserData("proger");

        box.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - width / 2) * PIXELS_IN_METER , (body.getPosition().y - higth / 2) * PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public void processInput () {
        if ((keys.get(BotEntity.KeysBot.LEFT) && keys.get(BotEntity.KeysBot.RIGHT)) || (!keys.get(BotEntity.KeysBot.LEFT) && (!keys.get(BotEntity.KeysBot.RIGHT))))
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        if ((keys.get(BotEntity.KeysBot.UP) && keys.get(BotEntity.KeysBot.DOWN)) || (!keys.get(BotEntity.KeysBot.UP) && (!keys.get(BotEntity.KeysBot.DOWN))))
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
    }

}
