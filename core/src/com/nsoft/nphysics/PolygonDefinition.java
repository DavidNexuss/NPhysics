package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class PolygonDefinition {

	Color debugColor;
	Texture mapTexture;
	
	//PHYSICAL
	float mass;
	ArrayList<Vector2> forces = new ArrayList<>();
	
	public Vector2 sumForces() {
		
		Vector2 sum = Vector2.Zero;
		for (Vector2 vector2 : forces) {
			
			sum.add(vector2);
		}
		
		return sum;
	}
	public boolean isGravitable() {return mass != 0;}
	
	public Vector2 getFMass() {
		
		return Vector2.Zero.add(0, mass * Gravity);
	}
	public static void drawForce(Vector2 f,ShapeRenderer rend,Color r) {
		
		
	}
	
	
	
}


class Force{
	
	private static float Gravity = -9.8f;
	
	private Vector2 initial;
	private Vector2 end;
	private Vector2 force;
	
	private boolean g;
	
	public Force(Vector2 center,Vector2 force) {
		
		this.force = force;
		initial = center;
		end = initial.add(force);
	}
	
	public static Force getGravityForce(Vector2 centerMass,float mass) {
		
		return new Force(centerMass, Vector2.Zero.add(0, Gravity * mass)).setGravitable(true);
	}
	
	public Force setGravitable(boolean gravity) {
		
		g= gravity;
		return this;
	}
	
	public boolean isGravityForce() { return g; }
	
	public static void setGravity(float newGravity) {
		
		Gravity = newGravity;
	}
	//TODO: Recalculate procedure
	private void recalculateGravity(float newGravity) {
		
	}
	public static float getGravity() {return Gravity;}
	public void setForce(Vector2 newForce) {
		
		this.force = newForce;
		end = initial.add(newForce);
	}
	public Vector2 getForce() {
		
		return force;
	}
	public void draw(ShapeRenderer rend,Color r) {
	
		rend.setColor(r);
		rend.begin(ShapeType.Line);
		rend.line(initial, end);
		rend.end();
		rend.begin(ShapeType.Filled);
		if(initial.y > end.y)
			rend.triangle(end.x, end.y, end.x + 2, end.y + 2, end.x - 2, end.y + 2);
		else
			rend.triangle(end.x, end.y, end.x + 2, end.y - 2, end.x - 2 , end.y - 2);
		rend.end();
	}
}