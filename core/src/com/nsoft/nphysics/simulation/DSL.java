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
	
	int Xu,Yu;
	
	ArrayList<Force> forces = new ArrayList<>();
	
	ArrayList<Force> xuforces = new ArrayList<>();
	ArrayList<Force> yuforces = new ArrayList<>();
	ArrayList<Force> uforces = new ArrayList<>();
	
	public DSL(Solid d) {
		
		sol = d;
		
		for (Force force : sol.extraForces) {
			
			forces.add(force);
			force.completeUsingTrigonometric();
			if(!force.hasX()) { isXcomplete = false; Xu++; xuforces.add(force);}
			if(!force.hasY()) { isYcomplete = false; Yu++; yuforces.add(force);}
			if(!force.hasX() || !force.hasY()) {
				
				uforces.add(force);
			}
		}
		
		if(isXcomplete || isYcomplete) {
			
			checkSum();
		}
		if(DEBUG) {
			
			say("Problem status:");
			if(uforces.size() > 0) {
				
				String fs = "";
				for (Force force : uforces) {
					
					fs += Dev.getid(force) + ", ";
				}

				say("Unknown forces: " + uforces.size() + " : " + fs);
			}

			say("isXcomplete: " + isXcomplete + ", Unknown X: " + Xu);
			say("isYcomplete: " + isYcomplete + ", Unknown Y: " + Yu);
		}
		
		
	}
	
	public void solve() {
		
		boolean usingMoments = useMoments();
		
		if(DEBUG) {
			
			say("Starting to solve...");
			say("UsingMoments: " + usingMoments);
		}
		
		if(usingMoments) {
			
			
		}else {
			
			if(Xu > 1) throw new IllegalStateException("Impossible X unknown > 1");
			if(Yu > 1) throw new IllegalStateException("Impossible Y unknown > 1");
			
			//SUM X
			
			float x = 0;
			float y = 0;
			
			for (Force force : forces) {
				
				if(!xuforces.contains(force)) {
					
					x+= force.getTemporalFloat()[0];
				}
				
				if(!yuforces.contains(force)) {
					
					y+= force.getTemporalFloat()[1];
				}
			}
			
			xuforces.get(0).setTempX(-x);
			xuforces.get(0).end();
			yuforces.get(0).setTempY(-y);
			yuforces.get(0).end();
			
			isXcomplete = true;
			isYcomplete = true;
		}
		
		if(DEBUG) {say("Solved");}
	}
	public boolean useMoments() {
		
		Vector2 pos = forces.get(0).getPosition();
		for (int i = 1; i < forces.size(); i++) {
			
			Vector2 posb = forces.get(i).getPosition();
			if(!(pos.x == posb.x && pos.y == posb.y)) return true;
		}
		
		return false;
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
