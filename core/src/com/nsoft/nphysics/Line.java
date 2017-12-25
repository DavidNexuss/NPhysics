package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Line {


	public static ArrayList<Line> lines = new ArrayList<>();
	float x1,x2,y1,y2;
	public Line(float x1, float y1,float x2,float y2) {
		
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public void draw(ShapeRenderer r) {
		
		r.line(x1, y1, x2, y2);
	}
	
	
	public static boolean exists(Line a) {
		
		for (Line line: lines) {
			
			if(line.x1 == a.x1 &&
			   line.y1 == a.y1 &&
			   line.x2 == a.x2 &&
			   line.y2 == a.y2) {
				
				return true;
			}
		}
		
		return false;
	}

}
