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
public class TableEntity extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    float width, higth, x, y, dwidth, dheight;

    public TableEntity(Texture texture, World world,  float x, float y, float widht, float higth, float dwidth, float dheight){
        this.texture = texture;
        this.world = world;

        this.width = widht;
        this.higth = higth;

        this.dwidth = dwidth;
        this.dheight = dheight;

        setPosition(x, y);
//
        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(widht / 2, higth / 2);
        fixture = body.createFixture(shape, 1);
        fixture.setUserData("table");
        shape.dispose();

        setSize(width * PIXELS_IN_METER, higth * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - dwidth) * PIXELS_IN_METER , (body.getPosition().y - dheight) * PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
