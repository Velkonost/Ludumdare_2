package com.ltc.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
public class PlayerVlogerEntity extends Actor implements InputProcessor {

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

    }

    private Texture texture;

    private World world;

    private Body body;

    private Fixture fixture;

    public static final float SPEED = 2f;

    //позиция в мире
    Vector2 position = new Vector2();

    //используется для вычисления движения
    Vector2 velocity = new Vector2();

    public PlayerVlogerEntity(Texture texture, World world, float x, float y) {
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
            rightPressed();
        } else if (keycode == Input.Keys.A) {
            leftPressed();
        } else if (keycode == Input.Keys.W) {
            upPressed();
        } else if (keycode == Input.Keys.S) {
            downPressed();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.D) {
            rightReleased();
        } else if (keycode == Input.Keys.A) {
            leftReleased();
        } else if (keycode == Input.Keys.W) {
            upReleased();
        } else if (keycode == Input.Keys.S) {
            downReleased();
        }
        return true;
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

    //флаг устанавливаем, что движемся влево
    public void leftPressed() {
        keys.get(keys.put(Keys.LEFT, true));
    }

    //флаг устанавливаем, что движемся вправо
    public void rightPressed() {
        keys.get(keys.put(Keys.RIGHT, true));
    }

    //флаг устанавливаем, что движемся вверх
    public void upPressed() {
        keys.get(keys.put(Keys.UP, true));
    }

    //флаг устанавливаем, что движемся вниз
    public void downPressed() {
        keys.get(keys.put(Keys.DOWN, true));
    }

    //освобождаем флаги
    public void leftReleased() {
        keys.get(keys.put(Keys.LEFT, false));
    }

    public void rightReleased() {
        keys.get(keys.put(Keys.RIGHT, false));
    }

    public void upReleased() {
        keys.get(keys.put(Keys.UP, false));
    }

    public void downReleased() {
        keys.get(keys.put(Keys.DOWN, false));
    }

    public void resetWay(){
        rightReleased();
        leftReleased();
        downReleased();
        upReleased();
    }

    //в зависимости от выбранного направления движения выставляем новое направление движения для персонажа
    public void processInput() {
        if (keys.get(Keys.LEFT))
            body.setLinearVelocity(-SPEED, body.getLinearVelocity().y);
        if (keys.get(Keys.RIGHT))
            body.setLinearVelocity(SPEED, body.getLinearVelocity().y);
        if (keys.get(Keys.UP))
            body.setLinearVelocity(body.getLinearVelocity().x, SPEED);
        if (keys.get(Keys.DOWN))
            body.setLinearVelocity(body.getLinearVelocity().x, -SPEED);
        //если не выбрано направление, то стоим на месте
        if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) || (!keys.get(Keys.LEFT) && (!keys.get(Keys.RIGHT))))
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        if ((keys.get(Keys.UP) && keys.get(Keys.DOWN)) || (!keys.get(Keys.UP) && (!keys.get(Keys.DOWN))))
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
    }
}
