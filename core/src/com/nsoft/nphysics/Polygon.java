package com.nsoft.nphysics;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Polygon{

	public boolean isReady = false;
	ArrayList<Vector2> vertices = new ArrayList<>();
	Path2D path;
	
	public Polygon() {
		
		path = new Path2D.Double();
	}
	
	public void addVertex(float x,float y) {
		
		if(!isReady) {

			if(!isEmpty())path.lineTo(x, y);
			else { path.moveTo(x, y); System.err.println("dawdawdwad");}

			vertices.add(new Vector2(x, y));
		}
	}
	
	public boolean isEnded() {return isReady;}
	public void end() { path.closePath(); isReady = true;}
	public boolean isEmpty() {return vertices.size() == 0;}
	
	public Rectangle getBounds() {return path.getBounds();}
	
}
