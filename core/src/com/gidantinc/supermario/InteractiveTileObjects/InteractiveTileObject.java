package com.gidantinc.supermario.InteractiveTileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gidantinc.supermario.DynamicBodies.Mario;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;

/**
 * Created by IVAN on 19.2.2016 г..
 */
public abstract class InteractiveTileObject {

    Body body;

    Fixture fixture;
    TiledMap map;
    Rectangle rectangle;

    public InteractiveTileObject(World world,MapObject object,TiledMap map,PlayScreen screen){
        this.rectangle=((RectangleMapObject)object).getRectangle();
        this.map=map;
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ SuperMario.PPM,(rectangle.getY()+rectangle.getHeight()/2)/SuperMario.PPM);

        body=world.createBody(bodyDef);


        PolygonShape polygonShape=new PolygonShape();
        polygonShape.setAsBox(rectangle.getWidth()/2/SuperMario.PPM,rectangle.getHeight()/2/SuperMario.PPM);
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=polygonShape;

        fixture=body.createFixture(fixtureDef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter(short categoryFilter){
        Filter filter= new Filter();
        filter.categoryBits=categoryFilter;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
       TiledMapTileLayer layer=(TiledMapTileLayer)map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x*SuperMario.PPM/16),
                (int)(body.getPosition().y*SuperMario.PPM/16));
    }

}
