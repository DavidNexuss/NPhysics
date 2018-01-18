package com.nsoft.nphysics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Grid{

	private int centerX,centerY;
	private int sizeX,sizeY;
	private int unit;
	private float angle;
	private ShapeRenderer shape_renderer;
	
	public Grid(ShapeRenderer rend,int centerX,int centerY,int unit,int angle,int sizeX,int sizeY) {
	
		this.shape_renderer = rend;
		this.centerX = centerX;
		this.centerY = centerY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.unit = unit;
		this.angle = MathUtils.degreesToRadians * angle;
	
	}

	public void setPos(int centerx,int centery) {
		
		this.centerX = centerx;
		this.centerY = centery;
	}
	public float snapLenght(Vector3 pos) {
		
		float x = pos.x/unit;
		float y = pos.y/unit;
		return (float) (Math.pow((Math.round(x) - x),2) + Math.pow((Math.round(y) - y),2));
	}
	
	public int snap(float v) {
		
		return unit*Math.round(v/unit);
	}
	public int[] snap(Vector3 pos) {
		
		return new int[] {
				
				snap(pos.x),
				snap(pos.y),
				0
		};
	}
	public void draw() {
		
		if(angle != 0) {
			
			//TODO: Angle stuff;
		}else {
			
			//HORIZONTAL
			for (int y = centerY - sizeY/2; y < (centerY + sizeY/2) + 1; y+= unit) {
				
				shape_renderer.line(centerX - sizeX/2, y,centerX + sizeX/2, y);
			}
			
			//VERTICAL
			for (int x = centerX - sizeX/2; x < (centerX + sizeX/2) + 1; x+=unit) {
				
				shape_renderer.line(x,centerY - sizeY/2,x,centerY + sizeY/2);
			}
		}
		
	}
}
