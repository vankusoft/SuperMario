package com.gidantinc.supermario.Tools;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by IVAN on 26.2.2016 г..
 */
public class FireballDef {

    public Class<?> type;
    public Vector2 position;

    public FireballDef(Class<?> type, Vector2 position){
        this.type=type;
        this.position=position;
    }
}
