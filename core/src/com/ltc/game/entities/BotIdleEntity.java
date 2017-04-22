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
    public enum KeysProger {
        LEFT, RIGHT, UP, DOWN
    }

    public static Map<KeysProger, Boolean> keys = new HashMap<KeysProger, Boolean>();

    static {
        keys.put(KeysProger.LEFT, false);
        keys.put(KeysProger.RIGHT, false);
        keys.put(KeysProger.UP, false);
        keys.put(KeysProger.DOWN, false);

    }

    private Texture texture;
    private Texture phoneTexture;

    private boolean hasPhone = true, isPhoneCoords = false;
    private float phoneX, phoneY;

    private World world;

    private GameScreen game;

    private Body body;

    private Fixture fixture;


    public static final float SPEED_PROGER = 2f;

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
        box.setAsBox(0.5f, 0.5f);

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

        if (!hasPhone) {
            if (!isPhoneCoords) {
                phoneX = this.getX();
                phoneY = this.getY();
                isPhoneCoords = true;
            }

            batch.draw(phoneTexture, phoneX, phoneY, getWidth() / 2, getHeight() / 2);

        }
    }


}
