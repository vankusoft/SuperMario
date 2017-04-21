package com.gidantinc.supermario.Tools;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gidantinc.supermario.DynamicBodies.Goomba;
import com.gidantinc.supermario.DynamicBodies.MovingPlatform;
import com.gidantinc.supermario.DynamicBodies.Turtle;
import com.gidantinc.supermario.InteractiveTileObjects.Bricks;
import com.gidantinc.supermario.InteractiveTileObjects.Coins;
import com.gidantinc.supermario.InteractiveTileObjects.InteractiveTileObject;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;

/**
 * Created by IVAN on 18.2.2016 г..
 */
public class B2DCreator {

    PlayScreen screen;
    World world;
    TiledMap map;
    Body body;

    Array<Goomba> goombasArray;
    Array<Turtle> turtlesArray;
    Array<MovingPlatform> platformArray;

    public B2DCreator(PlayScreen screen,World world,TiledMap map){
        this.screen=screen;
        this.world=world;
        this.map=map;

        goombasArray=new Array<Goomba>();
        turtlesArray=new Array<Turtle>();
        platformArray=new Array<MovingPlatform>();

        for(MapObject object:map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle=((RectangleMapObject)object).getRectangle();
            BodyDef bodyDef=new BodyDef();
            bodyDef.type= BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ SuperMario.PPM,(rectangle.getY()+rectangle.getHeight()/2)/SuperMario.PPM);

            body=world.createBody(bodyDef);

            PolygonShape polygonShape=new PolygonShape();
            polygonShape.setAsBox(rectangle.getWidth()/2/SuperMario.PPM,rectangle.getHeight()/2/SuperMario.PPM);
            FixtureDef fixtureDef=new FixtureDef();
            fixtureDef.filter.categoryBits=SuperMario.GROUND_BIT;
            fixtureDef.filter.maskBits=SuperMario.ENEMY_BIT| SuperMario.MARIO_BIT | SuperMario.MUSHROOM_BIT | SuperMario.TURTLE_BIT |SuperMario.FIREBALL_BIT |
                    SuperMario.END_LEVEL_BIT;
            fixtureDef.shape=polygonShape;
            body.createFixture(fixtureDef).setUserData(this);
        }

        for(MapObject object:map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle=((RectangleMapObject)object).getRectangle();
            BodyDef bodyDef=new BodyDef();
            bodyDef.type= BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ SuperMario.PPM,(rectangle.getY()+rectangle.getHeight()/2)/SuperMario.PPM);

            body=world.createBody(bodyDef);

            PolygonShape polygonShape=new PolygonShape();
            polygonShape.setAsBox(rectangle.getWidth()/2/SuperMario.PPM,rectangle.getHeight()/2/SuperMario.PPM);
            FixtureDef fixtureDef=new FixtureDef();
            fixtureDef.filter.categoryBits=SuperMario.OBJECT_BIT;
            fixtureDef.filter.maskBits=SuperMario.ENEMY_BIT| SuperMario.MARIO_BIT | SuperMario.MUSHROOM_BIT | SuperMario.TURTLE_BIT |SuperMario.FIREBALL_BIT;
            fixtureDef.shape=polygonShape;
            body.createFixture(fixtureDef).setUserData(this);
        }
        //coins
        for(MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Coins coins= new Coins(world,object,map,screen);
        }
        //bricks
        for(MapObject object:map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Bricks bricks= new Bricks(world,object,map,screen);

        }
        //goombas
        for(MapObject object:map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle=((RectangleMapObject)object).getRectangle();
            goombasArray.add(new Goomba(screen,world,rectangle.getX()/SuperMario.PPM,rectangle.getY()/SuperMario.PPM));

        }
        //turtles
        for(MapObject object:map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle=((RectangleMapObject)object).getRectangle();
            turtlesArray.add(new Turtle(screen,world,rectangle.getX()/SuperMario.PPM,rectangle.getY()/SuperMario.PPM));
        }
        //moving platform level2
        if(screen.currentLevel.contentEquals("level2")) {
            for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                platformArray.add(new MovingPlatform(screen, world, rectangle.getX() / SuperMario.PPM,
                        rectangle.getY() / SuperMario.PPM));
            }
        }else{
            Gdx.app.log("Level","1");
        }

        //STICK END OF THE LEVEL 1
        for(MapObject object:map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle=((RectangleMapObject)object).getRectangle();
            BodyDef bodyDef=new BodyDef();
            bodyDef.type= BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ SuperMario.PPM,(rectangle.getY()+rectangle.getHeight()/2)/SuperMario.PPM);

            body=world.createBody(bodyDef);

            PolygonShape polygonShape=new PolygonShape();
            polygonShape.setAsBox(rectangle.getWidth()/2/SuperMario.PPM,rectangle.getHeight()/2/SuperMario.PPM);
            FixtureDef fixtureDef=new FixtureDef();
            fixtureDef.filter.categoryBits=SuperMario.STICK_BIT;
            fixtureDef.filter.maskBits=SuperMario.MARIO_BIT | SuperMario.FIREBALL_BIT;
            fixtureDef.shape=polygonShape;
            body.createFixture(fixtureDef).setUserData(this);

            }


            
    }

    public Array<Goomba> getGoombasArray() {
        return goombasArray;
    }

    public Array<Turtle> getTurtlesArray(){
        return turtlesArray;
    }

    public Array<MovingPlatform> getPlatformArray() {
        return platformArray;
    }
}
