package com.gidantinc.supermario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



/**
 * Created by IVAN on 12.3.2016 г..
 */
public class Controller {

    Stage stage;
    Viewport viewport;

    boolean upBtn,leftBtn,rightBtn,downBtn;

    public Controller(SpriteBatch sb){

        OrthographicCamera cam=new OrthographicCamera();
        viewport=new FitViewport(SuperMario.V_WIDTH*2,SuperMario.V_HEIGHT*2,cam);
        stage=new Stage(viewport,sb);

        Gdx.input.setInputProcessor(stage);

        Table table= new Table();
        table.bottom().left();

        //UP BUTTON
        Image upImg=new Image(new Texture("upBtn.png"));
        upImg.setSize(50,50);
        upImg.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               upBtn=false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upBtn=true;
                return true;
            }
        });

        //DOWN BUTTON

        Image downImg=new Image(new Texture("downBtn.png"));
        downImg.setSize(50,50);
        downImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downBtn=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downBtn=false;
            }
        });


        //LEFT BUTTON

        Image leftImg=new Image(new Texture("leftBtn.png"));
        leftImg.setSize(50,50);
        leftImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftBtn = true;
                return true ;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftBtn=false;

            }
        });

        //RIGHT BUTTON

        Image rightImg=new Image(new Texture("rightBtn.png"));
        rightImg.setSize(50,50);
        rightImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightBtn=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightBtn=false;
            }
        });


        table.add();
        table.add(upImg).size(upImg.getWidth(),upImg.getHeight());
        table.add();
        table.row().pad(5,5,5,5);
        table.add(leftImg).size(leftImg.getWidth(),leftImg.getHeight());
        table.add();
        table.add(rightImg).size(rightImg.getWidth(),rightImg.getHeight());
        table.row().pad(5);
        table.add();
        table.add(downImg).size(downImg.getWidth(),downImg.getHeight());
        table.add();


        stage.addActor(table);



    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpBtn() {
        return upBtn;
    }

    public boolean isLeftBtn() {
        return leftBtn;
    }

    public boolean isRightBtn() {
        return rightBtn;
    }

    public boolean isDownBtn() {
        return downBtn;
    }

    public void resize(int width,int height){
        viewport.update(width,height);
    }
}
