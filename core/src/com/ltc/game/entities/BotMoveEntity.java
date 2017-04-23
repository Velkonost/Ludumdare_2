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
 * @author Velkonost
 */
public class BotMoveEntity extends Actor {

    private World world;
    private Texture texture;
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

    public BotMoveEntity(Texture texture, World world, float x, float y) {
        this.texture = texture;
//        this.phoneTexture = phoneTexture;
        this.world = world;
//        this.game = game;
//        previousPosition = new Vector2(getX(), getY());
        setPosition(x, y);

        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(def);
        body.setFixedRotation(true);

        final PolygonShape box = new PolygonShape();
        box.setAsBox(0.25f, 0.5f);

        fixture = body.createFixture(box, 1000);
        fixture.setUserData("botmove");

        box.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);

    }

    public Body getBody() {
        return body;
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
        if ((keys.get(KeysBot.LEFT) && keys.get(KeysBot.RIGHT)) || (!keys.get(KeysBot.LEFT) && (!keys.get(KeysBot.RIGHT))))
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        if ((keys.get(KeysBot.UP) && keys.get(KeysBot.DOWN)) || (!keys.get(KeysBot.UP) && (!keys.get(KeysBot.DOWN))))
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
    }
}
