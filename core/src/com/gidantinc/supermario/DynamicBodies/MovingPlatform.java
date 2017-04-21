package com.gidantinc.supermario.DynamicBodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;

/**
 * Created by IVAN on 10.3.2016 г..
 */
public class MovingPlatform extends Sprite {


    PlayScreen screen;
    Body body;
    World world;

    Vector2 velocity;
    TextureRegion platform;

    boolean jumpOut;
    boolean marioOn;
    boolean marioOff;

    BodyDef bodyDef;

    public MovingPlatform(PlayScreen screen,World world,float x,float y){

        this.screen=screen;
        this.world=world;

        marioOn=false;
        marioOff=false;
        jumpOut=false;

        setPosition(x,y);
        definePlatform();
        setBounds(0,0,32/SuperMario.PPM,16/SuperMario.PPM);

        velocity=new Vector2(0,0.5f);
        platform=new TextureRegion(screen.getAtlas().findRegion("mushroom"),0,0,16,16);


    }

    private void definePlatform(){

                bodyDef=new BodyDef();
                bodyDef.type= BodyDef.BodyType.DynamicBody;
                bodyDef.position.set(getX(),getY());

                body=world.createBody(bodyDef);


                EdgeShape head= new EdgeShape();
                head.set(new Vector2(-15/SuperMario.PPM,7/SuperMario.PPM),new Vector2(15/SuperMario.PPM,7/SuperMario.PPM));

                FixtureDef fixtureDef=new FixtureDef();
                fixtureDef.shape=head;
                fixtureDef.filter.categoryBits=SuperMario.MOVING_PLATFORM_BIT;
                fixtureDef.filter.maskBits=SuperMario.MARIO_BIT;
                body.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float dt){
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
        setRegion(platform);


       body.setLinearVelocity(velocity);

        if(body.getPosition().y>200/SuperMario.PPM){
            Gdx.app.log("Position","Reset");
            body.setTransform(2500/SuperMario.PPM,-10/SuperMario.PPM,0);

            float positionY=body.getPosition().y;
            float position=body.getPosition().x;
            String yPosition=Float.toString(positionY);
            String xPosition=Float.toString(position);
            Gdx.app.log("Position X",xPosition);

            Gdx.app.log("Position Y",yPosition);
        }


    }

    public void marioOnPlatform(){
        marioOn=true;
    }




}
