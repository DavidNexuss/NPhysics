package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Scene extends Stage {

	private ShapeRenderer shape_renderer = new ShapeRenderer();
	public int gridSize = 30;
	private static Thread processMove;
	
	
	static {
		
		processMove = new Thread(()->{
			
			try {
				
				while(true) {
					
					if(NPhysics.mouse.isMouseInWindow())NPhysics.scene.processMove(Gdx.input.getX(), Gdx.input.getY());
					Thread.sleep(10);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public Scene() {
		super();
		shape_renderer.setColor(.8f, .8f, .8f, 1);
		processMove.start();
		
	}
	
	@Override
	public void draw() {
		
		drawGrid();
		super.draw();
	}
	
	private void drawGrid() {


		shape_renderer.setProjectionMatrix(getCamera().combined);
		shape_renderer.begin(ShapeType.Line);
		
		for (int i = 0; i < (getHeight()/gridSize) + 2; i++) {
			
			shape_renderer.line(-(getWidth() + gridSize), i*gridSize, getWidth() + i, i*gridSize);
			
		}
		
		for (int i = 0; i < (getWidth()/gridSize) + 2; i++) {
			
			shape_renderer.line(i*gridSize,-(getHeight()+ gridSize), i*gridSize, getHeight()+ gridSize);
		}
		
		shape_renderer.end();
		
	}

	public void processMove(int screenX, int screenY) {
		
		if(screenX < getWidth()/10)
			getCamera().translate((float) -Math.sqrt((getWidth()/10 - screenX)), 0, 0);
		if(screenX > getWidth()*9/10)
			getCamera().translate((float) Math.sqrt(screenX - getWidth()*9/10), 0, 0);
		if(screenY < getHeight()/10)
			getCamera().translate(0,(float) Math.sqrt(getHeight()/10 - screenY), 0);
		if(screenY > getHeight()*9/10)
			getCamera().translate(0,(float) -Math.sqrt((screenY - getHeight()*9/10)), 0);
		
		getCamera().update();
	}
}
