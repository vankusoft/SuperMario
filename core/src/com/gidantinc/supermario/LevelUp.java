package com.gidantinc.supermario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.gidantinc.supermario.DynamicBodies.Mario;
import com.gidantinc.supermario.Screen.GameOverScreen;
import com.gidantinc.supermario.Screen.PlayScreen;



/**
 * Created by IVAN on 8.3.2016 г..
 */
public class LevelUp {

    PlayScreen screen;
    Mario mario;

    Preferences preferences;


    public LevelUp(PlayScreen screen,Mario mario){
        this.screen=screen;
        this.mario=mario;
        preferences=Gdx.app.getPreferences("LevelUp");


    }

    public void update(float dt){
        if (mario.endLevel1){
            preferences.putString("key","level2");
            screen.game.setScreen(new PlayScreen(screen.game));

        }
    }

}
