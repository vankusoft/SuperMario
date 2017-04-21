package com.gidantinc.supermario.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gidantinc.supermario.SuperMario;


/**
 * Created by IVAN on 26.2.2016 г..
 */
public class GameOverScreen implements Screen{

    SuperMario game;
    public Stage stage;
    public Viewport viewport;

    public boolean gameOver;

    public GameOverScreen(SpriteBatch sb,SuperMario game){
        this.game=game;

        gameOver=false;
        viewport= new FitViewport(SuperMario.V_WIDTH,SuperMario.V_HEIGHT,new OrthographicCamera());
        stage=new Stage(viewport,sb);

        Label.LabelStyle font= new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table= new Table();
        Label gameOverLabel= new Label("GAME OVER",font);
        table.setFillParent(true);
        table.center();
        table.add(gameOverLabel);
        stage.addActor(table);

    }
    @Override
    public void show() {

    }
    public void update(float dt){

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(new PlayScreen(game));
        }

        if(gameOver){
            Preferences pref=Gdx.app.getPreferences("GameOver");
            pref.putString("over","game");
        }

    }
    @Override
    public void render(float delta) {
        stage.draw();
        update(delta);
    }

    @Override
    public void resize(int width, int height) {

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
