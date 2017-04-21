package com.gidantinc.supermario.DynamicBodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gidantinc.supermario.LevelUp;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;


/**
 * Created by IVAN on 18.2.2016 г..
 */
public class Mario extends Sprite {


    World world;
    public Body body;
    PlayScreen screen;

    public enum State{STAND,FALL,JUMP,RUN,GROW,SMALLING,DEAD}
    public State currentState,previousState;

    private TextureRegion marioStand;
    private TextureRegion marioJump;
    private TextureRegion marioDead;
    private Animation marioRunAnimation;

    public float stateTimer;
    private boolean runRight;

    Array<TextureRegion> frames;

    private TextureRegion bigMarioJump;
    private TextureRegion bigMarioStand;
    private Animation bigMarioRun,growingMario,smallingMario;

    boolean setMarioGrow;
    boolean marioIsBig;
    boolean marioIsAlreadyBig;
    boolean timeToDefineBigMario;

    boolean timeToRedefineMario;
    boolean setMarioSmall;
    boolean marioIsAlreadySmall;

    public boolean end;
    boolean endDestroy;
    public boolean marioIsDead;

    public boolean setToDestroyMario;
    public boolean marioDestroyed;

    public boolean endLevel1;

    Array<FireBalls> fireB;

    Sound marioDie;


    public Mario(World world,PlayScreen screen){
         this.screen=screen;
        this.world=world;
        defineMario();

        marioDie=SuperMario.manager.get("audio/sounds/mariodie.wav",Sound.class);

        fireB=new Array<FireBalls>();


        marioStand=new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);
        setBounds(0,0,16/SuperMario.PPM,16/SuperMario.PPM);

        frames=new Array<TextureRegion>();

        marioJump=new TextureRegion(screen.getAtlas().findRegion("little_mario"),80,0,16,16);
        marioStand=new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);

        for(int i=1;i<4;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"),i*16,0,16,16));
        }
        marioRunAnimation=new Animation(0.05f,frames);
        frames.clear();

        //BIG MARIO

        bigMarioStand=new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32);
        for(int i=1;i<4;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),i*16,0,16,32));
        }
        bigMarioRun=new Animation(0.05f,frames);
        frames.clear();
        bigMarioJump=new TextureRegion(screen.getAtlas().findRegion("big_mario"),80,0,16,32);

        //GROWING MARIO
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        growingMario=new Animation(0.1f,frames);
        frames.clear();

        //SMALLING MARIO
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16));
        smallingMario=new Animation(0.1f,frames);
        frames.clear();

        //MARIO DEAD
        marioDead=new TextureRegion(screen.getAtlas().findRegion("little_mario"),96,0,16,16);

        stateTimer=0;
        currentState=State.STAND;
        previousState=State.STAND;

        //boolean values
        runRight=true;
        setMarioGrow=false;
        marioIsBig=false;
        marioIsAlreadyBig=false;
        timeToDefineBigMario=false;

        setMarioSmall=false;
        timeToRedefineMario=false;

        marioIsAlreadySmall=true;
        marioIsDead=false;

       end=false;
        endDestroy=false;

        setToDestroyMario=false;
        marioDestroyed=false;

        endLevel1=false;


    }

    public void update(float dt){

        if(timeToDefineBigMario) {
            defineBIGMario();
        }
        if(timeToRedefineMario){
            redefineMario();
            marioIsBig=false;
        }

        if(marioIsBig)
        {
            setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2-6/SuperMario.PPM);
        }
        else
        {
            setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
        }

        for(FireBalls  ball : fireB) {
            ball.update(dt);
            if(ball.isDestroyed())
            {
               fireB.removeValue(ball,true);
            }
        }


        if(end){
            if(stateTimer>2) {
                body.applyLinearImpulse(new Vector2(2.3f, 0), body.getWorldCenter(), true);
                end=false;


            }
        }

        if(endDestroy && !marioDestroyed){
            if(stateTimer>4){
                if(body.getLinearVelocity().x==0){
                    world.destroyBody(body);
                    marioDestroyed=true;
                    //LEVEL END
                    endLevel1=true;
                }
            }
        }

        if(body.getPosition().y<0){
            marioIsDead=true;
        }
        setRegion(getFrame(dt));

    }


    public void DieMusic(){
        if(marioIsDead){
            marioDie.play();
        }
    }

    public void defineMario(){
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(53/ SuperMario.PPM,36/SuperMario.PPM);

        body=world.createBody(bodyDef);

        FixtureDef fixtureDef= new FixtureDef();
        CircleShape circleShape= new CircleShape();
        circleShape.setRadius(6f/SuperMario.PPM);
        fixtureDef.shape=circleShape;
        fixtureDef.filter.categoryBits=SuperMario.MARIO_BIT;
        fixtureDef.filter.maskBits=SuperMario.BRICK_BIT| SuperMario.COIN_BIT|
                SuperMario.ENEMY_BIT|SuperMario.GROUND_BIT|SuperMario.OBJECT_BIT | SuperMario.MUSHROOM_BIT |
        SuperMario.ENEMY_HEAD | SuperMario.TURTLE_BIT | SuperMario.TURTLE_HEAD | SuperMario.STICK_BIT
                | SuperMario.MOVING_PLATFORM_BIT;
        body.createFixture(fixtureDef).setUserData(this);



        EdgeShape head= new EdgeShape();
        head.set(new Vector2(-2/SuperMario.PPM,6/SuperMario.PPM),new Vector2(2/SuperMario.PPM,6/SuperMario.PPM));
        fixtureDef.shape=head;
        fixtureDef.isSensor=true;
        fixtureDef.filter.categoryBits=SuperMario.HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);


    }

    public void defineBIGMario(){
        Vector2 currentPosition=body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(currentPosition.add(0,10/SuperMario.PPM));

        body=world.createBody(bodyDef);

        FixtureDef fixtureDef= new FixtureDef();
        CircleShape circleShape= new CircleShape();
        circleShape.setRadius(6f/SuperMario.PPM);
        fixtureDef.shape=circleShape;
        fixtureDef.filter.categoryBits=SuperMario.MARIO_BIT;
        fixtureDef.filter.maskBits=SuperMario.BRICK_BIT | SuperMario.COIN_BIT|
                SuperMario.ENEMY_BIT|SuperMario.GROUND_BIT|SuperMario.OBJECT_BIT | SuperMario.MUSHROOM_BIT |
                SuperMario.ENEMY_HEAD | SuperMario.TURTLE_BIT | SuperMario.TURTLE_HEAD | SuperMario.STICK_BIT
                | SuperMario.MOVING_PLATFORM_BIT;

        fixtureDef.shape=circleShape;
        body.createFixture(fixtureDef).setUserData(this);
        circleShape.setPosition(new Vector2(0,-14/SuperMario.PPM));
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape head= new EdgeShape();
        head.set(new Vector2(-2/SuperMario.PPM,6/SuperMario.PPM),new Vector2(2/SuperMario.PPM,6/SuperMario.PPM));
        fixtureDef.shape=head;
        fixtureDef.isSensor=true;
        fixtureDef.filter.categoryBits=SuperMario.HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        timeToDefineBigMario=false;

    }

    public void redefineMario(){
        Vector2 currentPosition=body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(currentPosition);

        body=world.createBody(bodyDef);

        FixtureDef fixtureDef= new FixtureDef();
        CircleShape circleShape= new CircleShape();
        circleShape.setRadius(6f/SuperMario.PPM);
        fixtureDef.shape=circleShape;
        fixtureDef.filter.categoryBits=SuperMario.MARIO_BIT;
        fixtureDef.filter.maskBits=SuperMario.BRICK_BIT| SuperMario.COIN_BIT|
                SuperMario.ENEMY_BIT|SuperMario.GROUND_BIT|SuperMario.OBJECT_BIT | SuperMario.MUSHROOM_BIT |
                SuperMario.ENEMY_HEAD | SuperMario.TURTLE_BIT | SuperMario.TURTLE_HEAD | SuperMario.STICK_BIT
                | SuperMario.MOVING_PLATFORM_BIT;

        fixtureDef.shape=circleShape;
        body.createFixture(fixtureDef).setUserData(this);


        EdgeShape head= new EdgeShape();
        head.set(new Vector2(-2/SuperMario.PPM,6/SuperMario.PPM),new Vector2(2/SuperMario.PPM,6/SuperMario.PPM));
        fixtureDef.shape=head;
        fixtureDef.isSensor=true;
        fixtureDef.filter.categoryBits=SuperMario.HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        timeToRedefineMario=false;

    }

    public TextureRegion getFrame(float dt){
        currentState=getState();
        TextureRegion region;

        switch (currentState){

            case SMALLING:
                region=smallingMario.getKeyFrame(stateTimer);
                if(smallingMario.isAnimationFinished(stateTimer)){
                    setMarioSmall=false;
                }

            case GROW:
                region=growingMario.getKeyFrame(stateTimer);
                if(growingMario.isAnimationFinished(stateTimer)){
                    setMarioGrow=false;
                }
                break;
            case JUMP:
                region=marioIsBig?bigMarioJump:marioJump;
                break;

            case RUN:
                region=marioIsBig?bigMarioRun.getKeyFrame(stateTimer,true):marioRunAnimation.getKeyFrame(stateTimer,true);
                break;

            case DEAD:
                region=marioDead;
                break;

            case FALL:
                case STAND:
                    default:
                region=marioIsBig?bigMarioStand:marioStand;
                break;

        }

        if((body.getLinearVelocity().x>0 ||runRight) && region.isFlipX()){
            region.flip(true,false);
            runRight=true;
        }else if((body.getLinearVelocity().x<0 || !runRight )&& !region.isFlipX()){
            region.flip(true,false);
            runRight=false;
        }


        stateTimer=currentState==previousState?stateTimer+=dt:0;
        previousState=currentState;
        return region;

    }

    private State getState(){

        if((body.getLinearVelocity().y>0 || (body.getLinearVelocity().y<0 && previousState==State.JUMP))&& !marioIsDead){
            return State.JUMP;

        } else if(body.getLinearVelocity().y<0 && !marioIsDead){
            return State.FALL;
        }

        else if(body.getLinearVelocity().x!=0 && !marioIsDead) {
            return State.RUN;
        }
        else if(setMarioGrow){
            return State.GROW;
        }
        else if(setMarioSmall){
            return State.SMALLING;
        }
        else if(marioIsDead){
            return State.DEAD;
        }
        else return State.STAND;

    }

    public void marioGrow(){

        marioIsAlreadySmall=false;
        if(!marioIsAlreadyBig){
            setMarioGrow=true;
            marioIsBig=true;
            setBounds(getX(),getY(),getWidth(),getHeight()*2);
            marioIsAlreadyBig=true;
            timeToDefineBigMario=true;
        }

    }

    public void marioSmall(){
        marioIsAlreadyBig=false;
        if(!marioIsAlreadySmall) {
            SuperMario.manager.get("audio/sounds/powerdown.wav",Sound.class).play();
            setMarioSmall = true;
            timeToRedefineMario = true;
            marioIsAlreadySmall=true;
            setBounds(getX(),getY(),getWidth(),getHeight()/2);
        }else {
            Filter filter= new Filter();
            filter.maskBits=SuperMario.NOTHING_BIT;

            for(Fixture fixture:body.getFixtureList()){
                fixture.setFilterData(filter);
            }
            body.applyLinearImpulse(new Vector2(0,5f),body.getWorldCenter(),true);

            marioIsDead=true;

            SuperMario.manager.get("audio/sounds/mariodie.wav",Sound.class).play();
        }

    }

    public void marioHitByTurtle(Turtle turtle){

        if((turtle.currentState== Turtle.State.MOVING_SHELL || turtle.currentState== Turtle.State.WALKING) && marioIsAlreadyBig){
            marioSmall();
            SuperMario.manager.get("audio/sounds/powerdown.wav",Sound.class).play();
        }

        else if((turtle.currentState== Turtle.State.MOVING_SHELL || turtle.currentState== Turtle.State.WALKING) && marioIsAlreadySmall){

            Filter filter= new Filter();
            filter.maskBits=SuperMario.NOTHING_BIT;

            for(Fixture fixture:body.getFixtureList()){
                fixture.setFilterData(filter);
            }
            body.applyLinearImpulse(new Vector2(0,5f),body.getWorldCenter(),true);

            marioIsDead=true;
            SuperMario.manager.get("audio/sounds/mariodie.wav",Sound.class).play();
        }


    }

    public void draw(SpriteBatch batch){

        for(FireBalls ball : fireB)
            ball.draw(batch);

        if(!marioDestroyed){
            super.draw(batch);
        }


    }
    public void fire(){
              fireB.add(new FireBalls(screen,world,body.getPosition().x,body.getPosition().y));
    }

    public void endOfLevel(){

        Gdx.app.log("LEVEL","END");

        Filter filter=new Filter();
        filter.categoryBits=SuperMario.END_LEVEL_BIT;
        filter.maskBits=SuperMario.GROUND_BIT;
        for(Fixture fixture:body.getFixtureList()){
            fixture.setFilterData(filter);
        }
        end=true;
        endDestroy=true;
    }

  }


