package com.nsoft.nphysics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ContextMenuItem extends Actor {

	Texture p;	
	String name;
	
	public ContextMenuItem(String name,Texture p,Runnable target) {
		
		this.name = name;
		if(p == null) {
			
			Pixmap a = new Pixmap(32, 32, Pixmap.Format.RGB888);
			a.setColor(Color.CYAN);
			a.fillRectangle(0, 0, 32, 32);
			p = new Texture(a);
			a.dispose();
		}
		
		this.p = p;
		addListener(new InputListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				target.run();
				return true;
			}
		});
		
		setWidth(32);
		setHeight((getWidth()/p.getWidth()) * p.getHeight());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(p, getX(), getY(), getWidth(), getHeight());
	}
}
