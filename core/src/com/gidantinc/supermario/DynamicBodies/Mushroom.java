package com.gidantinc.supermario.DynamicBodies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;

/**
 * Created by IVAN on 23.2.2016 г..
 */
public class Mushroom extends Sprite {

    PlayScreen screen;

    Body body;
    World world;
    TextureRegion mushroom;
    Vector2 velocity;

    boolean setToDestroy,destroyed;

    public Mushroom(PlayScreen screen,World world,float x,float y){
        this.screen=screen;
        this.world=world;
        setPosition(x,y);
        defineMushroom();
        setBounds(0,0,16/SuperMario.PPM,16/SuperMario.PPM);
        mushroom=new TextureRegion(screen.getAtlas().findRegion("mushroom"),0,0,16,16);

        velocity=new Vector2(1f,-1.5f);
        setToDestroy=false;
        destroyed=false;

    }
    public void update(float dt){

        if(setToDestroy && !destroyed){
            world.destroyBody(body);
            destroyed=true;
        }else{
            setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
            setRegion(mushroom);
            body.setLinearVelocity(velocity);

        }

    }
    public void draw(SpriteBatch sb) {
        if (!destroyed)
        {
            super.draw(sb);
        }
    }

    private void defineMushroom(){
        BodyDef bodyDef= new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(),getY());

        body=world.createBody(bodyDef);

        CircleShape shape= new CircleShape();
        shape.setRadius(6f/ SuperMario.PPM);
        FixtureDef fixtureDef= new FixtureDef();
        fixtureDef.shape=shape;
        fixtureDef.filter.categoryBits=SuperMario.MUSHROOM_BIT;
        fixtureDef.filter.maskBits=SuperMario.MARIO_BIT | SuperMario.BRICK_BIT | SuperMario.COIN_BIT |
                SuperMario.GROUND_BIT | SuperMario.OBJECT_BIT |SuperMario.FIREBALL_BIT;
        body.createFixture(fixtureDef).setUserData(this);


    }

    public void getMushroom(Mario mario){
        setToDestroy=true;
        screen.hud.setScore(100);
        mario.marioGrow();
        SuperMario.manager.get("audio/sounds/powerup.wav",Sound.class).play();

    }
}
