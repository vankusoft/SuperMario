package com.gidantinc.supermario;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gidantinc.supermario.InteractiveTileObjects.Coins;
import com.gidantinc.supermario.Screen.PlayScreen;

/**
 * Created by IVAN on 18.2.2016 г..
 */
public class Hud {

    public Stage stage;
    private Viewport viewport;

    public int score;
    int countDown;

    PlayScreen screen;

    Label levelLabel;
    Label countDownLabel;
    Label scoreLabel;

    float stateTimer;

    public Hud(SpriteBatch sb,PlayScreen screen){

        this.screen=screen;
        score=0;
        countDown=300;

        viewport=new FitViewport(SuperMario.V_WIDTH,SuperMario.V_HEIGHT,new OrthographicCamera());
        stage=new Stage(viewport,sb);

        Table table= new Table();
        table.top();
        table.setFillParent(true);

        Label.LabelStyle font=new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Label marioLabel=new Label("MARIO",font);
        Label worldLabel=new Label("WORLD",font);
        Label timeLabel=new Label("TIME",font);
        scoreLabel=new Label(String.format("%06d",score),font);
        levelLabel=new Label("1-1",font);
        countDownLabel=new Label(String.format("%03d",countDown),font);

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);

        stateTimer=0;


    }

    public void update(float dt){

        if(screen.currentLevel.contentEquals("level2")){
            levelLabel.setText("1-2");
        }

        stateTimer+=dt;
        if(stateTimer>=1){
            countDown--;
           countDownLabel.setText(String.format("%03d",countDown));
            stateTimer=0;
        }

    }

    public void setScore(int score) {
        this.score += score;
        scoreLabel.setText(String.format("%06d",this.score));
    }
}
