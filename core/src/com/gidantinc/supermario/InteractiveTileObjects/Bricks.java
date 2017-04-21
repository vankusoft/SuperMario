package com.gidantinc.supermario.InteractiveTileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.gidantinc.supermario.DynamicBodies.Mario;
import com.gidantinc.supermario.Screen.PlayScreen;
import com.gidantinc.supermario.SuperMario;

/**
 * Created by IVAN on 19.2.2016 г..
 */
public class Bricks extends InteractiveTileObject {

    PlayScreen screen;

    public Bricks(World world,MapObject object,TiledMap map,PlayScreen screen) {
        super(world, object,map,screen);
        this.screen=screen;
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.BRICK_BIT);

    }


    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick","Collusion");
        screen.hud.setScore(20);
        setCategoryFilter(SuperMario.NOTHING_BIT);
        SuperMario.manager.get("audio/sounds/breakblock.wav",Sound.class).play();
        getCell().setTile(null);

    }
}
