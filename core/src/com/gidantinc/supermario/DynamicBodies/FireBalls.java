package com.gidantinc.supermario.DynamicBodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;

import java.util.Iterator;

/**
 * Created by IVAN on 26.2.2016 г..
 */
public class FireBalls extends Sprite {

    PlayScreen screen;
    World world;
    public Body body;

    Animation fireballAnimation;
    Array<TextureRegion> frames;

    float stateTimer;

    Vector2 velocity;

    boolean setToDestroy;
     boolean destroy;




    public FireBalls(PlayScreen screen,World world,float x,float y){
        this.screen=screen;
        this.world=world;
        setPosition(x,y);

        defineFireball();


        setBounds(0,0,8/ SuperMario.PPM,8/SuperMario.PPM);

        frames=new Array<TextureRegion>();
        for(int i=0;i<5;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"),i*8,0,8,8));
        }
        fireballAnimation=new Animation(0.1f,frames);
        velocity=new Vector2(2f,-0.1f);

        stateTimer=0;

        setToDestroy=false;
        destroy=false;

    }


    private void defineFireball(){


        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(),getY());

        if(!world.isLocked()) {
            body = world.createBody(bodyDef);
        }


        FixtureDef fixtureDef=new FixtureDef();
        CircleShape circleShape=new CircleShape();
        circleShape.setRadius(3f/SuperMario.PPM);
        fixtureDef.shape=circleShape;
        fixtureDef.filter.categoryBits=SuperMario.FIREBALL_BIT;
        fixtureDef.filter.maskBits=SuperMario.ENEMY_BIT | SuperMario.MUSHROOM_BIT | SuperMario.TURTLE_BIT |
                SuperMario.BRICK_BIT | SuperMario.COIN_BIT | SuperMario.ENEMY_BIT | SuperMario.GROUND_BIT |
                SuperMario.OBJECT_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float dt){
        stateTimer+=dt;

        if((setToDestroy || stateTimer>2) && !destroy) {
            world.destroyBody(body);
            destroy = true;
        }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(fireballAnimation.getKeyFrame(stateTimer, true));
        body.setLinearVelocity(velocity);

    }

    public void draw(SpriteBatch  sb){
        if(!destroy){
            super.draw(sb);
        }
    }

    public void setToDestroy(){
        setToDestroy=true;

    }

    public boolean isDestroyed(){
        return destroy;
    }

    public void bounce(){
        body.applyLinearImpulse(new Vector2(0,6f),body.getWorldCenter(),true);
        body.applyAngularImpulse(3f,true);


    }




}
