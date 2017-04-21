package com.gidantinc.supermario.Screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gidantinc.supermario.Controller;
import com.gidantinc.supermario.DynamicBodies.FireBalls;
import com.gidantinc.supermario.DynamicBodies.Goomba;
import com.gidantinc.supermario.DynamicBodies.Mario;
import com.gidantinc.supermario.DynamicBodies.MovingPlatform;
import com.gidantinc.supermario.DynamicBodies.Mushroom;
import com.gidantinc.supermario.DynamicBodies.Turtle;
import com.gidantinc.supermario.Hud;
import com.gidantinc.supermario.InteractiveTileObjects.Coins;
import com.gidantinc.supermario.LevelUp;
import com.gidantinc.supermario.SuperMario;
import com.gidantinc.supermario.Tools.B2DCreator;
import com.gidantinc.supermario.Tools.FireballDef;
import com.gidantinc.supermario.Tools.ItemDef;
import com.gidantinc.supermario.Tools.WorldContactListener;


import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * Created by IVAN on 18.2.2016 г..
 */
public class PlayScreen implements Screen{

   public SuperMario game;
   public OrthographicCamera gameCam;
   public Viewport viewport;

   public Hud hud;

   public TiledMap map;
   public TmxMapLoader mapLoader;
   public OrthogonalTiledMapRenderer mapRenderer;

   public B2DCreator b2DCreator;
   public Box2DDebugRenderer box2DDebugRenderer;
   public World world;

   public Mario mario;

   public TextureAtlas atlas;

    float stateTimer;

    LevelUp levelUp;

    public String currentLevel;
    public boolean gameOver;

    Music music;

    Controller controller;

        //MUSHROOM
    public Array<Mushroom> mushrooms;
    public LinkedBlockingQueue<ItemDef> spawningItems;


    public PlayScreen(SuperMario game){

        atlas=new TextureAtlas("Mario_and_Enemies.pack");
        this.game=game;

        gameCam=new OrthographicCamera();
        viewport=new FitViewport(SuperMario.V_WIDTH/SuperMario.PPM,SuperMario.V_HEIGHT/SuperMario.PPM,gameCam);

        hud=new Hud(game.batch,this);

        mapLoader=new TmxMapLoader();

        //next level setUp

        world=new World(new Vector2(0,-10),true);
        mario=new Mario(world,PlayScreen.this);



        levelUp=new LevelUp(this,mario);
        Preferences preferences=Gdx.app.getPreferences("LevelUp");
        currentLevel=preferences.getString("key");
        Preferences prefs=Gdx.app.getPreferences("GameOver");
        String gameEnd=prefs.getString("over");
        if(gameEnd.contentEquals("game")){
            map=mapLoader.load("level1.tmx");
        }else {

            if (currentLevel.contentEquals("level2")) {
                map = mapLoader.load("level2.tmx");
            } else {
                map = mapLoader.load("level1.tmx");
            }
        }

        //******************************

        mapRenderer=new OrthogonalTiledMapRenderer(map,1/SuperMario.PPM);

        gameCam.position.set(viewport.getWorldWidth()/2,viewport.getWorldHeight()/2,0);

        box2DDebugRenderer=new Box2DDebugRenderer();
        b2DCreator=new B2DCreator(this,world,map);
        world.setContactListener(new WorldContactListener());

        stateTimer=0;

        //spawning items
        mushrooms=new Array<Mushroom>();
        spawningItems=new LinkedBlockingQueue<ItemDef>();

        controller=new Controller(game.batch);

        gameOver=false;

    }

    public void itemsToSpawn(ItemDef itemDef){
        spawningItems.add(itemDef);
    }

    public void handleSpawnedItems(){

        if(!spawningItems.isEmpty()){
            ItemDef itemDef=spawningItems.poll();
            if(itemDef.type==Mushroom.class){
                mushrooms.add(new Mushroom(this,world,itemDef.position.x,itemDef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }


    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if(!mario.marioIsDead || !mario.end) {

            if(Gdx.app.getType()== Application.ApplicationType.Android){
                if (controller.isUpBtn() && mario.currentState!= Mario.State.JUMP) {
                    mario.body.applyLinearImpulse(new Vector2(0, 4f), mario.body.getWorldCenter(), true);
                }
                if (controller.isRightBtn() && mario.body.getLinearVelocity().x <= 2) {
                    mario.body.applyLinearImpulse(new Vector2(0.2f, 0), mario.body.getWorldCenter(), true);
                }
                if (controller.isLeftBtn() && mario.body.getLinearVelocity().x >= -2) {
                    mario.body.applyLinearImpulse(new Vector2(-0.2f, 0), mario.body.getWorldCenter(), true);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    mario.fire();
                }
            }else{
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && mario.currentState!= Mario.State.JUMP) {
                    mario.body.applyLinearImpulse(new Vector2(0, 4f), mario.body.getWorldCenter(), true);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mario.body.getLinearVelocity().x <= 2) {
                    mario.body.applyLinearImpulse(new Vector2(0.2f, 0), mario.body.getWorldCenter(), true);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && mario.body.getLinearVelocity().x >= -2) {
                    mario.body.applyLinearImpulse(new Vector2(-0.2f, 0), mario.body.getWorldCenter(), true);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    mario.fire();
                }
            }


        }


    }

    public void update(float dt){

        levelUp.update(dt);

        handleInput(dt);

        handleSpawnedItems();

        mapRenderer.setView(gameCam);

        if(!mario.marioIsDead){

            music=SuperMario.manager.get("audio/music/mario_music.ogg",Music.class);
            music.setLooping(true);
            music.play();

            if(mario.body.getPosition().x>200/SuperMario.PPM) {
                gameCam.position.x = mario.body.getPosition().x;
            }
        }else{
            music.stop();

        }

        hud.update(dt);

        gameCam.update();

        world.step(1/60f,6,2);

        mario.update(dt);

        for(Goomba goomba:b2DCreator.getGoombasArray()){
            goomba.update(dt);
        }
        for(Mushroom mush:mushrooms){
            mush.update(dt);
        }
        for(Turtle turtle:b2DCreator.getTurtlesArray()){
            turtle.update(dt);
        }
        for (MovingPlatform platform:b2DCreator.getPlatformArray()){
            platform.update(dt);
        }

        if(mario.marioIsDead && mario.stateTimer>4){

          game.setScreen(new GameOverScreen(game.batch,game));

        }


    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        box2DDebugRenderer.render(world,gameCam.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        game.batch.setProjectionMatrix(gameCam.combined);


        game.batch.begin();
        mario.draw(game.batch);



        for(Goomba goomba:b2DCreator.getGoombasArray()){
            goomba.draw(game.batch);
        }
        for(Mushroom mush:mushrooms){
            mush.draw(game.batch);
        }
        for(Turtle turtle:b2DCreator.getTurtlesArray()){
            turtle.draw(game.batch);
        }
        for(MovingPlatform platform:b2DCreator.getPlatformArray()){
            platform.draw(game.batch);
        }


        game.batch.end();

        controller.draw();

    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        controller.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
