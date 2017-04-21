package com.gidantinc.supermario.InteractiveTileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.gidantinc.supermario.DynamicBodies.Mushroom;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;
import com.gidantinc.supermario.Tools.ItemDef;

/**
 * Created by IVAN on 19.2.2016 г..
 */
public class Coins extends InteractiveTileObject {

     TiledMapTileSet tile;

    private static final int BLANK_COIN=4;
    PlayScreen screen;


    MapObject object;
    public Coins(World world, MapObject object,TiledMap map,PlayScreen screen) {
        super(world, object,map,screen);
        this.object=object;
        this.screen=screen;

        fixture.setUserData(this);
        setCategoryFilter(SuperMario.COIN_BIT);

        tile=map.getTileSets().getTileSet("tileset_gutter");


    }

    @Override
    public void onHeadHit() {

        if(getCell().getTile().getId()==BLANK_COIN){
            Gdx.app.log("COIN","ALREADY HIT");
            SuperMario.manager.get("audio/sounds/bump.wav",Sound.class).play();
        }else {
            getCell().setTile(tile.getTile(BLANK_COIN));
            screen.hud.setScore(50);
            if(!object.getProperties().containsKey("mushroom")){
                SuperMario.manager.get("audio/sounds/coin.wav",Sound.class).play();
            }

            if(object.getProperties().containsKey("mushroom")){
                screen.itemsToSpawn(new ItemDef(new Vector2(body.getPosition().x,
                        body.getPosition().y+16/SuperMario.PPM),Mushroom.class));
                SuperMario.manager.get("audio/sounds/powerup_spawn.wav",Sound.class).play();
            }else{
                Gdx.app.log("NO MUSHROOM HERE","CHECK ANOTHER COIN");
            }

        }


    }

}
