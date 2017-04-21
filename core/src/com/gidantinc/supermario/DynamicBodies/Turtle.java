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
 * Created by IVAN on 25.2.2016 г..
 */
public class Turtle extends Sprite {

    Body body;
    PlayScreen screen;
    World world;

    Vector2 velocity;

    public enum State{WALKING,STANDING_SHELL,MOVING_SHELL}
    public State currentState,previousState;
    float stateTimer;
    boolean runRight;
    public static boolean movingShell;

    TextureRegion frameRegion;

    TextureRegion turtleShell;
    Animation turtleWalk;
    Array<TextureRegion> frames;

    public Turtle(PlayScreen screen,World world,float x,float y){
        this.screen=screen;
        this.world=world;
        setPosition(x,y);
        defineTurtle();
        setBounds(0,0,16/SuperMario.PPM,24/SuperMario.PPM);

        velocity=new Vector2(0.5f,0);

        turtleShell=new TextureRegion(screen.getAtlas().findRegion("turtle"),64,0,16,24);

        frames=new Array<TextureRegion>();
        for(int i=0;i<2;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),i*16,0,16,24));
        }
        turtleWalk=new Animation(0.1f,frames);
        frames.clear();

        stateTimer=0;


    }

    private void defineTurtle(){
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(),getY());

        body=world.createBody(bodyDef);

        CircleShape circleShape=new CircleShape();
        circleShape.setRadius(6f/ SuperMario.PPM);
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=circleShape;
        fixtureDef.filter.categoryBits=SuperMario.TURTLE_BIT;

        fixtureDef.filter.maskBits=SuperMario.MARIO_BIT| SuperMario.BRICK_BIT| SuperMario.COIN_BIT|
                SuperMario.ENEMY_BIT|SuperMario.GROUND_BIT|SuperMario.OBJECT_BIT | SuperMario.TURTLE_BIT |SuperMario.FIREBALL_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape head= new EdgeShape();
        head.set(new Vector2(-5/SuperMario.PPM,7/SuperMario.PPM),new Vector2(5/SuperMario.PPM,7/SuperMario.PPM));
        fixtureDef.shape=head;
        fixtureDef.restitution=1f;
        fixtureDef.filter.categoryBits=SuperMario.TURTLE_HEAD;
        body.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float dt){
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2+5/SuperMario.PPM);
        setRegion(getFrame(dt));
        body.setLinearVelocity(velocity);

        if(currentState==State.STANDING_SHELL && stateTimer>2){
            velocity.x=0.5f;
        }
    }

    public TextureRegion getFrame(float dt){
        currentState=getState();

        switch (currentState){

          case WALKING:
              frameRegion=turtleWalk.getKeyFrame(stateTimer,true);
              break;

          case STANDING_SHELL:
              case MOVING_SHELL:
              frameRegion=turtleShell;
              break;
        }

        if((body.getLinearVelocity().x>0 || runRight) && !frameRegion.isFlipX()){
            frameRegion.flip(true,false);
            runRight=true;
        }else if((body.getLinearVelocity().x<0 || !runRight )&& frameRegion.isFlipX()){
            frameRegion.flip(true,false);
            runRight=false;
        }

        stateTimer=currentState==previousState?stateTimer+=dt:0;
        previousState=currentState;
        return frameRegion;

    }

    private State getState(){

        if(body.getLinearVelocity().x != 0 && !movingShell)
        {
            return State.WALKING;
        }
        else if(movingShell)
        {
            return State.MOVING_SHELL;
        }
        else
            return State.STANDING_SHELL;

    }

    public void turtleHeadHit(Mario mario){

        if(currentState==State.STANDING_SHELL)
        {

            if(mario.getX()<=this.getX())
            {
                velocity.x=2;
            }

            else if (mario.getX()>=this.getX())
            {
                velocity.x=-2;
            }
            movingShell=true;

        }
        else
        {
            velocity.x=0;
            currentState=State.STANDING_SHELL;
            movingShell=false;
        }

    }

    public void reverseVelocityTurtle(boolean x,boolean y){

        if(x)
        {
            velocity.x=-velocity.x;

        }

        else if(y)
        {
            velocity.y=-velocity.y;
        }
    }


}
