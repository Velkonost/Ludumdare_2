package com.ltc.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ltc.game.GameScreen;

import java.util.HashMap;
import java.util.Map;

import static com.ltc.game.Constants.PIXELS_IN_METER;

/**
 * @author Velkonost
 */
public class BotIdleEntity extends Actor {

    //направление движения
    public enum KeysBot {
        LEFT, RIGHT, UP, DOWN
    }

    public static Map<KeysBot, Boolean> keys = new HashMap<KeysBot, Boolean>();

    static {
        keys.put(KeysBot.LEFT, false);
        keys.put(KeysBot.RIGHT, false);
        keys.put(KeysBot.UP, false);
        keys.put(KeysBot.DOWN, false);

    }

    private Texture texture;

    private World world;

    private GameScreen game;

    private Body body;

    private Fixture fixture;

    public boolean hasPhone = true;

    public static final float SPEED = 2f;

    public BotIdleEntity(Texture texture, GameScreen game, World world, float x, float y) {
        this.texture = texture;
        this.world = world;
        this.game = game;

        setPosition(x, y);

        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(def);
        body.setFixedRotation(true);

        final PolygonShape box = new PolygonShape();
        box.setAsBox(0.25f, 0.5f);

        fixture = body.createFixture(box, 3);
        fixture.setUserData("botidle");

        box.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);

    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x) * PIXELS_IN_METER,
                (body.getPosition().y) * PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void processInput() {
        if (keys.get(KeysBot.LEFT))
            body.setLinearVelocity(-SPEED, body.getLinearVelocity().y);
        if (keys.get(KeysBot.RIGHT))
            body.setLinearVelocity(SPEED, body.getLinearVelocity().y);
        if (keys.get(KeysBot.UP))
            body.setLinearVelocity(body.getLinearVelocity().x, SPEED);
        if (keys.get(KeysBot.DOWN))
            body.setLinearVelocity(body.getLinearVelocity().x, -SPEED);
        //если не выбрано направление, то стоим на месте
        if ((keys.get(KeysBot.LEFT) && keys.get(KeysBot.RIGHT)) || (!keys.get(KeysBot.LEFT) && (!keys.get(KeysBot.RIGHT))))
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        if ((keys.get(KeysBot.UP) && keys.get(KeysBot.DOWN)) || (!keys.get(KeysBot.UP) && (!keys.get(KeysBot.DOWN))))
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
    }

    public boolean isHasPhone() {
        return hasPhone;
    }
}
