package com.ltc.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.ltc.game.Constants.PIXELS_IN_METER;

/**
 * @author Velkonost
 */
public class PlayerEntity extends Actor implements InputProcessor {

    private Texture texture;

    private World world;

    private Body body;

    private Fixture fixture;

    public static final float SPEED = 2f;

    //позиция в мире
    Vector2 position = new Vector2();

    //используется для вычисления движения
    Vector2 velocity = new Vector2();

    public PlayerEntity(Texture texture, World world, float x, float y) {
        this.texture = texture;
        this.world = world;
        setPosition(x, y);
        position = new Vector2(x, y);


        Gdx.input.setInputProcessor(this);
        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        final PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);

        fixture = body.createFixture(box, 3);
        fixture.setUserData("player");
        box.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);

//        addListener(new InputListener() {
//            @Override
//            public boolean keyDown(InputEvent event, int keycode) {
//                if (keycode == Input.Keys.D) {
//
//                    body.getPosition().x += PlayerEntity.SPEED;
////                    body.setLinearVelocity(PlayerEntity.SPEED, body.getLinearVelocity().y);
//                }
//
//                return true;
//            }
//        });
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

    public void update(float delta) {
        position.add(velocity.scl(delta));
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.D) {
            System.out.println(keycode);

//            body.getPosition().x += 200f;
                    body.setLinearVelocity(200f, body.getLinearVelocity().y);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
