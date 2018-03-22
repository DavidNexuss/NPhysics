package com.nsoft.nphysics.simulation;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.Polygon;
import static com.nsoft.nphysics.simulation.Core.*;

public class DSL implements Dev{

	static ArrayList<Solid> solids;
	static ArrayList<Vertex> vertexs;
	
	Solid sol;
	
	boolean isXcomplete = true;
	boolean isYcomplete = true;
	boolean isMcomplete = true;
	
	ArrayList<Force> forces = new ArrayList<>();
	ArrayList<Force> uforces = new ArrayList<>();
	
	public DSL(Solid d) {
		
		sol = d;
		
		for (Force force : sol.extraForces) {
			
			forces.add(force);
			force.completeUsingTrigonometric();
			if(!force.hasX()) isXcomplete = false;
			if(!force.hasY()) isYcomplete = false;
			if(!force.hasX() && !force.hasY()) {
				
				uforces.add(force);
			}
		}
		
		if(DEBUG) {
			
			say("Problem status:");
			say("isXcomplete: " + isXcomplete);
			say("isYcomplete: " + isYcomplete);
			say("isZcomplete: " + isMcomplete);
		}
	}
	
	public void checkSum() {
		
		if(!(isXcomplete && isYcomplete)) {
		
			System.err.println("Incomplete problem");
			return;
		}
		
		float x = sumX();
		float y = sumY();
		
		if(Math.abs(x) < 10e-5) x = 0;
		if(Math.abs(y) < 10e-5) y = 0;
		
		if(x == 0 && y == 0) {
			
			System.out.println("CheckSum Correct!");
		}else {
			
			System.out.println("Checksum Incorrect: ");
			System.out.println("X: " + x);
			System.out.println("Y: " + y);
		}
	}
	public float sum(Axis a) {
		
		if(a == Axis.X) {
				
			if(isXcomplete) {
				
				float sum = 0;
				
				for (Force force : forces) {
					
					sum+= force.mod() * Math.cos(force.getAngle());
				}
				return sum;
			}else throw new IllegalStateException();
		}else {
			
			if(isYcomplete) {
				
				float sum = 0;
				
				for (Force force : forces) {
					
					sum+= force.mod() * Math.sin(force.getAngle());
				}
				return sum;
			}else throw new IllegalStateException();
			
		}
		
	}
	
	public float sumX() {
		
		return sum(Axis.X);
	}
	
	public float sumY() {
		
		return sum(Axis.Y);
	}
	
	
	public void destroySolid(Solid d) {
		
		if(solids.contains(d))solids.remove(d);
		else throw new IllegalStateException();
		
		for (Vertex v : d.v) {
			
			vertexs.remove(v);
		}
	}
	
	/*
	public Vertex getNearestV(Vector2 pos) {
		
		float len = Float.MAX_VALUE;
		Vertex r = null;
		for (Solid solid : solids) {
			if(solid.closestPoint(a))
			
		}
	}*/
	
	static enum Axis{X,Y}
}
