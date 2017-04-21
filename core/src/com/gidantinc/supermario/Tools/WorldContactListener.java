package com.gidantinc.supermario.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gidantinc.supermario.DynamicBodies.FireBalls;
import com.gidantinc.supermario.DynamicBodies.Goomba;
import com.gidantinc.supermario.DynamicBodies.Mario;
import com.gidantinc.supermario.DynamicBodies.MovingPlatform;
import com.gidantinc.supermario.DynamicBodies.Mushroom;
import com.gidantinc.supermario.DynamicBodies.Turtle;
import com.gidantinc.supermario.InteractiveTileObjects.InteractiveTileObject;
import com.gidantinc.supermario.SuperMario;

/**
 * Created by IVAN on 19.2.2016 г..
 */
public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {


        Fixture fixA=contact.getFixtureA();
        Fixture fixB=contact.getFixtureB();

        int cDef=fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case SuperMario.HEAD_BIT| SuperMario.COIN_BIT:

                if(fixA.getFilterData().categoryBits==SuperMario.HEAD_BIT){
                    if(fixB.getUserData() instanceof InteractiveTileObject){
                        ((InteractiveTileObject)fixB.getUserData()).onHeadHit();
                    }
                }else if(fixB.getFilterData().categoryBits==SuperMario.HEAD_BIT){
                    if(fixA.getUserData() instanceof InteractiveTileObject){
                        ((InteractiveTileObject)fixA.getUserData()).onHeadHit();
                    }
                }
                break;

            case SuperMario.HEAD_BIT| SuperMario.BRICK_BIT:

                if(fixA.getFilterData().categoryBits==SuperMario.HEAD_BIT){
                    if(fixB.getUserData() instanceof InteractiveTileObject){
                        ((InteractiveTileObject)fixB.getUserData()).onHeadHit();
                    }
                }else if(fixB.getFilterData().categoryBits==SuperMario.HEAD_BIT){
                    if(fixA.getUserData() instanceof InteractiveTileObject){
                        ((InteractiveTileObject)fixA.getUserData()).onHeadHit();
                    }
                }

                break;

            case SuperMario.ENEMY_BIT | SuperMario.OBJECT_BIT:
                if(fixB.getFilterData().categoryBits==SuperMario.OBJECT_BIT){
                    ((Goomba)fixA.getUserData()).mirrorVelocity(true,false);
                }else if(fixA.getFilterData().categoryBits==SuperMario.OBJECT_BIT){
                    ((Goomba)fixB.getUserData()).mirrorVelocity(true,false);
                }

                break;

            case SuperMario.MARIO_BIT | SuperMario.ENEMY_HEAD:
                if(fixA.getFilterData().categoryBits==SuperMario.MARIO_BIT){
                    ((Goomba)fixB.getUserData()).hitOnHead();
                }else if(fixB.getFilterData().categoryBits==SuperMario.MARIO_BIT){
                    ((Goomba)fixA.getUserData()).hitOnHead();
                }

                break;

            case SuperMario.ENEMY_BIT | SuperMario.ENEMY_BIT:
                    ((Goomba)fixA.getUserData()).mirrorVelocity(true,false);
                    ((Goomba)fixB.getUserData()).mirrorVelocity(true,false);
                break;

            case SuperMario.MARIO_BIT | SuperMario.MUSHROOM_BIT:
                if(fixA.getFilterData().categoryBits==SuperMario.MARIO_BIT){
                    ((Mushroom)fixB.getUserData()).getMushroom((Mario)fixA.getUserData());
                }else if(fixB.getFilterData().categoryBits==SuperMario.MARIO_BIT){
                    ((Mushroom)fixA.getUserData()).getMushroom((Mario)fixB.getUserData());
                }

                break;

            case SuperMario.MARIO_BIT | SuperMario.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits==SuperMario.ENEMY_BIT){
                    ((Mario)fixB.getUserData()).marioSmall();
                }else if(fixB.getFilterData().categoryBits==SuperMario.ENEMY_BIT){
                    ((Mario)fixA.getUserData()).marioSmall();
                }
                break;

            case SuperMario.MARIO_BIT | SuperMario.TURTLE_BIT:
                if(fixA.getFilterData().categoryBits==SuperMario.TURTLE_BIT){
                    ((Mario)fixB.getUserData()).marioHitByTurtle(((Turtle)fixA.getUserData()));
                }else if(fixB.getFilterData().categoryBits==SuperMario.TURTLE_BIT){
                    ((Mario)fixA.getUserData()).marioHitByTurtle((Turtle)fixB.getUserData());
                }
                break;

            case SuperMario.MARIO_BIT | SuperMario.TURTLE_HEAD:
                if(fixA.getFilterData().categoryBits==SuperMario.MARIO_BIT){
                    ((Turtle)fixB.getUserData()).turtleHeadHit((Mario)fixA.getUserData());
                }else if(fixB.getFilterData().categoryBits==SuperMario.MARIO_BIT){
                    ((Turtle)fixA.getUserData()).turtleHeadHit((Mario)fixB.getUserData());
                }

                break;

            case SuperMario.TURTLE_BIT | SuperMario.OBJECT_BIT:
                if(fixB.getFilterData().categoryBits==SuperMario.OBJECT_BIT){
                    ((Turtle)fixA.getUserData()).reverseVelocityTurtle(true,false);
                }else if(fixA.getFilterData().categoryBits==SuperMario.OBJECT_BIT){
                    ((Turtle)fixB.getUserData()).reverseVelocityTurtle(true,false);
                }

                break;

            case SuperMario.TURTLE_BIT | SuperMario.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits==SuperMario.TURTLE_BIT){
                    ((Goomba)fixB.getUserData()).hitByTurtle((Turtle)fixA.getUserData());
                }else if(fixB.getFilterData().categoryBits==SuperMario.TURTLE_BIT){
                    ((Goomba)fixA.getUserData()).hitByTurtle((Turtle)fixB.getUserData());
                }
                break;

             case SuperMario.FIREBALL_BIT | SuperMario.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits==SuperMario.OBJECT_BIT){
                    ((FireBalls)fixB.getUserData()).setToDestroy();
                }
                else if(fixB.getFilterData().categoryBits==SuperMario.OBJECT_BIT){
                    ((FireBalls)fixA.getUserData()).setToDestroy();
                }
                break;

            case SuperMario.FIREBALL_BIT | SuperMario.GROUND_BIT:
                if (fixA.getFilterData().categoryBits==SuperMario.GROUND_BIT){
                    ((FireBalls)fixB.getUserData()).bounce();
                }
                else if(fixB.getFilterData().categoryBits==SuperMario.GROUND_BIT){
                    ((FireBalls)fixA.getUserData()).bounce();
                }
                break;

            case SuperMario.MARIO_BIT | SuperMario.STICK_BIT:
                if(fixA.getFilterData().categoryBits==SuperMario.STICK_BIT){
                    ((Mario)fixB.getUserData()).endOfLevel();
                }
                else if(fixB.getFilterData().categoryBits==SuperMario.STICK_BIT){
                    ((Mario)fixA.getUserData()).endOfLevel();
                }
                break;

            case SuperMario.MARIO_BIT | SuperMario.MOVING_PLATFORM_BIT:
                if(fixA.getFilterData().categoryBits==SuperMario.MARIO_BIT){
                    ((MovingPlatform)fixB.getUserData()).marioOnPlatform();
                }else if(fixB.getFilterData().categoryBits==SuperMario.MARIO_BIT){
                    ((MovingPlatform)fixA.getUserData()).marioOnPlatform();
                }

                break;



        }


    }

    @Override
    public void endContact(Contact contact) {



    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
