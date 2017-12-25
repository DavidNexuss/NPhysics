package com.nsoft.nphysics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nsoft.nphysics.ui.UIScene;

public class NPhysics extends ApplicationAdapter {
	
	static Scene scene;
	static UIScene ui;
	static IsInWindow mouse;
	static NPhysics n;
	
	public NPhysics(IsInWindow mouse) {
		
		this.mouse = mouse;
	}
	@Override
	public void create () {
		
		n= this;
		
		scene = new Scene();
		scene.setViewport(new ScreenViewport(scene.getCamera()));
		ui = new UIScene();
		Gdx.input.setInputProcessor(new Input());
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		scene.draw();
		ui.draw();
		ui.act();
	}
	
	@Override
	public void dispose () {
		scene.dispose();
		ui.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		scene.getCamera().translate((width - scene.getCamera().viewportWidth)/2, (height - scene.getCamera().viewportHeight)/2, 0);
		scene.getViewport().update(width, height);
	}
}
