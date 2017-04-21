package com.gidantinc.supermario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gidantinc.supermario.Screen.PlayScreen;

public class SuperMario extends Game {

    public static final int V_WIDTH=400;
    public static final int V_HEIGHT=208;
    public static final float PPM=100;

    public static final short NOTHING_BIT=0;
    public static final short BRICK_BIT=1;
    public static final short COIN_BIT=2;
    public static final short MARIO_BIT=4;
    public static final short GROUND_BIT=8;
    public static final short OBJECT_BIT=16;
    public static final short HEAD_BIT=32;
    public static final short MOVING_PLATFORM_BIT=64;
    public static final short ENEMY_BIT=128;
    public static final short ENEMY_HEAD=256;
    public static final short MUSHROOM_BIT=512;
    public static final short TURTLE_BIT=1024;
    public static final short TURTLE_HEAD=2048;
    public static final short FIREBALL_BIT=4096;
    public static final short STICK_BIT=8192;
    public static final short END_LEVEL_BIT=16384;



	public SpriteBatch batch;
    public static AssetManager manager;

	
	@Override
	public void create () {
		batch = new SpriteBatch();


        manager=new AssetManager();
        manager.load("audio/music/mario_music.ogg", Music.class);
        manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.load("audio/sounds/bump.wav", Sound.class);
        manager.load("audio/sounds/coin.wav", Sound.class);
        manager.load("audio/sounds/mariodie.wav", Sound.class);
        manager.load("audio/sounds/powerdown.wav", Sound.class);
        manager.load("audio/sounds/powerup.wav", Sound.class);
        manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
        manager.load("audio/sounds/stomp.wav", Sound.class);
        manager.finishLoading();

        setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
        super.render();


    }
}
