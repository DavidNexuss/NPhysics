package com.nsoft.nphysics;

import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class PolygonDefinition {

	Color debugColor;
	Texture mapTexture;
	static Window askForPhysicalInfo;
	
	//PHYSICAL
	float mass;
	ArrayList<Force> Polygonforces = new ArrayList<>();
	
	private final static World mundo = new World(Force.getGravityVector(), true);
	private final static Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	
	public static boolean state = false; //DEBUG
	public static boolean simulate = false;
	private Body body;
	private Fixture f;
	
	public static World getWorld() {
		return mundo;
	}
	
	public static void step(Matrix4 projectionMatrix) {
		
		if(!simulate) return;
		mundo.step(Gdx.graphics.getDeltaTime(), 8, 4);
		renderer.render(mundo, projectionMatrix);
		
	}
	//BOX2D
	public PolygonDefinition(Polygon p) {
		
		getWorld();
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(p.getRelativeVertexs(p.getCenter())[0],p.getRelativeVertexs(p.getCenter())[1]);
		bdef.type = state ? BodyType.DynamicBody : BodyType.StaticBody;

		
		state = true;
		
		body = mundo.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		fdef.friction = .2f;
		fdef.restitution = .5f;
		fdef.density = 1;
		float[][] vertices = p.getTriangles(true,true);
		
		PolygonShape shape = new PolygonShape();
		for (float[] fs : vertices) {
			
			shape = new PolygonShape();
			shape.set(fs);
			fdef.shape = shape;
			body.createFixture(fdef);
			
		}
		shape.dispose();
		simulate = true;
		System.out.println("Mass: " + body.getMass());
	}
	public void addForce(Force c) {
		
		Polygonforces.add(c);
	}
	
	public void removeForce(Force c) {
		
		if(Polygonforces.contains(c))Polygonforces.remove(c);
	}
	public Vector2 sumForces() {
		
		Vector2 sum = Vector2.Zero;
		for (Force f: Polygonforces) {
			
			sum.add(f.getForce());
		}
		
		return sum;
	}
	public boolean isGravitable() {return mass != 0;}
	
	public void drawForces(ShapeRenderer r) {
		
		for (Force f: Polygonforces) {
			
			if(f.isGravityForce())f.draw(r, Color.RED);
			else f.draw(r, Color.BLUE);
		}
	}
	
}


class Force{
	
	private static float Gravity = -98f;
	private static boolean changeGravity = false;
	
	private static Vector2 GravityF;
	private Vector2 initial;
	private Vector2 end;
	private Vector2 force;
	
	private boolean g;
	
	private static ArrayList<Force> allForces = new ArrayList<>();
	
	public Force(Vector2 center,Vector2 force) {
		
		this.force = force;
		initial = center;
		end = initial.add(force);
		allForces.add(this);
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
		
		recalculateGravity(newGravity);
		Gravity = newGravity;
		changeGravity = true;
		
	}
	
	private static void recalculateGravity(float newGravity) {
		
		for (Force force : allForces) {
			
			if (force.isGravityForce()) {
				
				force.setForce(Vector2.Zero.add(0, newGravity/Gravity));
			}
		}
	}
	public static float getGravity() {return Gravity;}
	public static Vector2 getGravityVector() {
		
		if(GravityF == null || changeGravity) { changeGravity = false;return GravityF = new Vector2(0, getGravity());}
		else return GravityF;
	}
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