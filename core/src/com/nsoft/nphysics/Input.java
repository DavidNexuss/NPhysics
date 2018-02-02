package com.nsoft.nphysics;

import com.badlogic.gdx.Input.Keys;
import com.nsoft.nphysics.GameState.GameCode;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Input implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if(keycode == Keys.UP) {
			
			((OrthographicCamera)NPhysics.scene.getCamera()).translate(0, 30);
		}else if(keycode == Keys.DOWN) {
		
			((OrthographicCamera)NPhysics.scene.getCamera()).translate(0, -30);
		}else if(keycode == Keys.LEFT) {
		
			((OrthographicCamera)NPhysics.scene.getCamera()).translate(-30,0);
		}else if(keycode == Keys.RIGHT) {
	
			((OrthographicCamera)NPhysics.scene.getCamera()).translate(30,0);
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if(keycode == Keys.ESCAPE) {
			NPhysics.n.dispose();
			System.exit(0);
		}else if(keycode == Keys.B) {
			
			GameState.SwitchGameState(GameCode.CREATE_SOLID);
		}else if(keycode == Keys.R) {
			
			PolygonDefinition.reset();
		}else if(keycode == Keys.S) {
			
			PolygonDefinition.simulate = !PolygonDefinition.simulate;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			
		NPhysics.scene.proccessClick(screenX, screenY);
			return true;	
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
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
