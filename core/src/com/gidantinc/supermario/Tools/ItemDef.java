package com.gidantinc.supermario.Tools;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by IVAN on 24.2.2016 г..
 */
public class ItemDef {

    public Class<?> type;
    public Vector2 position;

    public ItemDef(Vector2 position,Class<?> type){
        this.type=type;
        this.position=position;
    }
}
