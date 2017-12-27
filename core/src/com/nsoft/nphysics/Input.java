package com.nsoft.nphysics;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if(keycode == Keys.ESCAPE) {
			NPhysics.n.dispose();
			System.exit(0);
		}else if(keycode == Keys.B) {
			
			//Scene.p.end();
		}else if(keycode == Keys.R) {
			
			//Scene.p = new Polygon();
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if(!NPhysics.ui.touchDown(screenX, screenY, pointer, button)) return true;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if(!NPhysics.ui.touchUp(screenX, screenY, pointer, button))
			NPhysics.scene.proccessClick(screenX, screenY);
		return true;	
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		if(!NPhysics.ui.touchDragged(screenX, screenY, pointer)) return true;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		if(!NPhysics.ui.mouseMoved(screenX, screenY)) return true;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		NPhysics.scene.zoom(amount);
		System.out.println(amount);
		return true;
	}

	
}
