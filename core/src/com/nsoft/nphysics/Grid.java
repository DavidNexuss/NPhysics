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
	private float[][] sin;
	private float[][] cos;
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
		
		if(angle != 0) {

			cos= new float[sizeX/unit + 1][sizeY/unit + 1];
			sin= new float[sizeX/unit + 1][sizeY/unit + 1];
			
			for (int i = 0; i < cos.length; i++) {
				
				for (int j = 0; j < cos.length; j++) {
					
					cos[i][j] = Float.MAX_VALUE;
				}
			}
			
			for (int i = 0; i < sin.length; i++) {
				
				for (int j = 0; j < sin.length; j++) {
					
					sin[i][j] = Float.MAX_VALUE;
				}
			}
		}
	
	}

	public float sin(int vx,int vy) {
		
		if(sin[vx + (sizeX/2)/unit][vy + (sizeY/2)/unit] == Float.MAX_VALUE) {
			
			return sin[vx + (sizeX/2)/unit][vy + (sizeY/2)/unit]  = vx*unit * MathUtils.cos(angle) + vy*unit*MathUtils.sin(angle);
		}else {
			
			return sin[vx + (sizeX/2)/unit][vy + (sizeY/2)/unit] ;
		}
	}
	public float cos(int vx,int vy) {
		
		if(cos[vx + (sizeX/2)/unit][vy + (sizeY/2)/unit] == Float.MAX_VALUE) {
			
			return cos[vx + (sizeX/2)/unit][vy + (sizeY/2)/unit]  = vx*unit * MathUtils.cos(angle) - vy*unit*MathUtils.sin(angle);
		}else {
			
			return cos[vx + (sizeX/2)/unit][vy + (sizeY/2)/unit] ;
		}
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
	private float tan(float a) {
		
		return (float) (MathUtils.sin(a)/Math.cos(a));
	}
	public void draw() {
		
		if(angle != 0) {
			
			//TODO: Angle stuff;
			
			int yoffset = 3;
			//HORIZONTAL
			for (int y = -sizeY/2; y < sizeY/2; y+= unit) {
				
				
				float x1 = -sizeX/2;
				float y1 = y;
				float x2 = sizeX/2;
				float y2 = y;
				
				float x1a = x1 * MathUtils.cos(angle) - y1*MathUtils.sin(angle);
				float y1a = x1 * MathUtils.cos(angle) + y1*MathUtils.sin(angle);
				float x2a = x2 * MathUtils.cos(angle) - y2*MathUtils.sin(angle);
				float y2a = x2 * MathUtils.cos(angle) + y2*MathUtils.sin(angle);
				
				shape_renderer.line(x1a + centerX,y1a + centerY +yoffset,x2a + centerX,y2a + centerY + yoffset);
			}
			
			//VERTICAL
			for (int x = -sizeX/2; x < sizeX/2; x+= unit) {
				
				float x1 = x;
				float y1 = -sizeY/2;
				float x2 = x;
				float y2 = sizeY/2;
				
				/**
				 * float x1a = (float) (x1 * Math.cos(angle) - y1*Math.sin(angle));
				 *float y1a = (float) (x1 * Math.cos(angle) + y1*Math.sin(angle));
				 *float x2a = (float) (x2 * Math.cos(angle) - y2*Math.sin(angle));
				 *float y2a = (float) (x2 * Math.cos(angle) + y2*Math.sin(angle));
				 */
				float x1a = x1 * MathUtils.cos(angle) - y1*MathUtils.sin(angle);
				float y1a = x1 * MathUtils.cos(angle) + y1*MathUtils.sin(angle);
				float x2a = x2 * MathUtils.cos(angle) - y2*MathUtils.sin(angle);
				float y2a = x2 * MathUtils.cos(angle) + y2*MathUtils.sin(angle);
				
				
				shape_renderer.line(x1a + centerX,y1a + centerY +yoffset,x2a + centerX,y2a + centerY + yoffset);
			}
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
