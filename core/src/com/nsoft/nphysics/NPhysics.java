package com.nsoft.nphysics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class NPhysics extends ApplicationAdapter {
	
	static Scene scene;
	static IsInWindow mouse;
	public NPhysics(IsInWindow mouse) {
		
		this.mouse = mouse;
	}
	@Override
	public void create () {
		
		scene = new Scene();
		scene.setViewport(new ScreenViewport(scene.getCamera()));
		Gdx.input.setInputProcessor(new Input());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		scene.draw();
	}
	
	@Override
	public void dispose () {
		scene.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		scene.getCamera().translate((width - scene.getCamera().viewportWidth)/2, (height - scene.getCamera().viewportHeight)/2, 0);
		scene.getViewport().update(width, height);
	}
}
