package com.gidantinc.supermario.DynamicBodies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;

/**
 * Created by IVAN on 23.2.2016 г..
 */
public class Goomba extends Sprite {

    World world;
    Body body;

    Array<TextureRegion> frames;
    Animation goombaAnimation;
    TextureRegion stompGoomba;

    float stateTimer;
    Vector2 velocity;

    boolean setToDestroy;
     boolean destroyed;

    FixtureDef fixtureDef;

    PlayScreen screen;

    public Goomba(PlayScreen screen,World world,float x, float y){
        this.screen=screen;
        this.world=world;
        setPosition(x,y);
        defineGoomba();

        setBounds(0,0,16/SuperMario.PPM,16/SuperMario.PPM);

        frames=new Array<TextureRegion>();
        for(int i=0;i<2;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"),i*16,0,16,16));
        }
        goombaAnimation=new Animation(0.5f,frames);
        velocity=new Vector2(0.5f,0);

        stompGoomba=new TextureRegion(screen.getAtlas().findRegion("goomba"),32,0,16,16);

        stateTimer=0;

        setToDestroy=false;
        destroyed=false;
    }

    public void update(float dt){
        stateTimer+=dt;

        if(setToDestroy && !destroyed){
            world.destroyBody(body);
            setRegion(stompGoomba);
            destroyed=true;
        }else if(!destroyed) {
            setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
            setRegion(goombaAnimation.getKeyFrame(stateTimer,true));
            body.setLinearVelocity(velocity);
        }
    }

    private void defineGoomba(){
        BodyDef bodyDef= new BodyDef();
        bodyDef.position.set(getX(),getY());
        bodyDef.type= BodyDef.BodyType.DynamicBody;

        body=world.createBody(bodyDef);

        CircleShape circleShape= new CircleShape();
        circleShape.setRadius(6f/ SuperMario.PPM);
        fixtureDef= new FixtureDef();
        fixtureDef.shape=circleShape;
        fixtureDef.filter.categoryBits=SuperMario.ENEMY_BIT;
        fixtureDef.filter.maskBits=SuperMario.MARIO_BIT| SuperMario.BRICK_BIT| SuperMario.COIN_BIT|
                SuperMario.ENEMY_BIT|SuperMario.GROUND_BIT|SuperMario.OBJECT_BIT | SuperMario.TURTLE_BIT |SuperMario.FIREBALL_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape head= new EdgeShape();
        head.set(new Vector2(-5/SuperMario.PPM,7/SuperMario.PPM),new Vector2(5/SuperMario.PPM,7/SuperMario.PPM));
        fixtureDef.shape=head;
        fixtureDef.restitution=1f;
        fixtureDef.filter.categoryBits=SuperMario.ENEMY_HEAD;
        body.createFixture(fixtureDef).setUserData(this);

    }

    public void mirrorVelocity(boolean x,boolean y){

        if(x)
        {
            velocity.x = -velocity.x;
        }
        else if(y)
        {
            velocity.y=-velocity.y;
        }

    }

    public void hitOnHead(){

        screen.hud.setScore(150);
        setToDestroy=true;

        SuperMario.manager.get("audio/sounds/stomp.wav",Sound.class).play();


    }
    public void hitByTurtle(Turtle turtle){


        if(Turtle.movingShell)
        {
            screen.hud.setScore(200);
            setToDestroy=true;
            SuperMario.manager.get("audio/sounds/stomp.wav",Sound.class).play();
        }
        else
        {
            mirrorVelocity(true,false);
             turtle.reverseVelocityTurtle(true,false);

        }
    }
}
