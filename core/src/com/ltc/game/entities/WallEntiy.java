package com.ltc.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.ltc.game.Constants.PIXELS_IN_METER;

/**
 * Created by Danila on 22.04.2017.
 */
public class WallEntiy extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    float width, higth;

    public WallEntiy(Texture texture, World world, Vector2 position, float widht, float higth){
        this.texture = texture;
        this.world = world;

        BodyDef def = new BodyDef();
        def.position.set(position);
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(widht, higth);
        fixture = body.createFixture(shape, 1);
        fixture.setUserData("wall");
        shape.dispose();

        setSize(width * PIXELS_IN_METER, higth * PIXELS_IN_METER);
    }

     @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x) * PIXELS_IN_METER, (body.getPosition().y) * PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
