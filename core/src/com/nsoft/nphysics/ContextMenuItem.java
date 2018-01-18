package com.nsoft.nphysics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
				addAction(Actions.sequence(Actions.fadeOut(0.005f),Actions.fadeIn(0.005f)));
				return true;
			}
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				
				addAction(Actions.scaleTo(1.2f, 1.2f, 0.1f));
				super.enter(event, x, y, pointer, fromActor);
			}
			
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				
				addAction(Actions.scaleTo(1, 1, 0.1f));
				
				super.exit(event, x, y, pointer, toActor);
			}
		});
		
		setWidth(32);
		setHeight((getWidth()/p.getWidth()) * p.getHeight());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.draw(p, getX() - (getWidth()*getScaleX() - getWidth())/2, getY() - (getHeight()*getScaleY() - getHeight())/2, getWidth() * getScaleX(), getHeight() * getScaleX());
	}
}
