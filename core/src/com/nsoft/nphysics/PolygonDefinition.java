package com.nsoft.nphysics;

import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class PolygonDefinition {

	Color debugColor;
	Texture mapTexture;
	static Window askForPhysicalInfo;
	
	//PHYSICAL
	private float mass;
	ArrayList<Force> Polygonforces = new ArrayList<>();
	
	private static World mundo = new World(Force.getGravityVector(), true);
	private static World backup;
	private final static Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private static ArrayList<PolygonDefinition> currentPolys = new ArrayList<>();
	
	public static boolean state = false; //DEBUG
	public static boolean simulate = false;
	private Body body;
	private Polygon p;
	private Fixture f;
	
	public static World getWorld() {
		return mundo;
	}
	
	public static void reset() {
		
		state = false;
		currentPolys.clear();
		mundo = new World(Force.getGravityVector(), true);
		for (Polygon polygon : Scene.polygons) {
			
			ArrayList<Force> forcesB = new ArrayList<>();
			for (Force force : polygon.def.Polygonforces) {
				
				if(!force.isGravityForce()) forcesB.add(force);
				System.out.println(force.isGravityForce());
			}
			
			Force[] B = new Force[forcesB.size()];
			for (int i = 0; i < B.length; i++) {
				
				B[i] = forcesB.get(i);
				System.out.println("aaa");
			}
			polygon.def = new PolygonDefinition(polygon, polygon.def.mass, polygon.def.density, polygon.def.friction, polygon.def.restitution, B);
		}
	}
	public static void step(Matrix4 projectionMatrix) {
		
		if(!simulate) return;
		mundo.step(Gdx.graphics.getDeltaTime(), 12, 8);
		renderer.render(mundo, projectionMatrix);
		
		for (PolygonDefinition polygonDefinition : currentPolys) {
			
			polygonDefinition.aplyForces();
		}
		
	}
	public PolygonDefinition(Polygon p) {
		
		this(p,-1,1,0.5f,0,null);
	}
		
	//BOX2D
	public PolygonDefinition(Polygon p,float mass,float density,float friction,float restitution,Force ...forces) {
		
		this.p = p;
		
		getWorld();
		
		BodyDef bdef = new BodyDef();
		System.out.println("c " + p.getCenterX()/Scene.currentUnit +" " + p.getCenterY()/Scene.currentUnit);
		bdef.position.set(p.getCenterX()/Scene.currentUnit,p.getCenterY()/Scene.currentUnit);
		bdef.type = state ? BodyType.DynamicBody : BodyType.StaticBody;

		state = true;
		
		body = mundo.createBody(bdef);
		FixtureDef fdef = new FixtureDef();
		fdef.friction = friction;
		fdef.restitution = restitution;
		fdef.density = density;
		
		float[][] vertices = p.getTriangles(true,true);
		
		PolygonShape shape = new PolygonShape();
		for (float[] fs : vertices) {
			
			shape = new PolygonShape();
			shape.set(fs);
			fdef.shape = shape;
			body.createFixture(fdef);
			
		}
		shape.dispose();
		

		if(mass != -1)setMass(mass);
		else this.mass = -1;
		this.friction = friction;
		this.restitution = restitution;
		this.density = density;
		
		if(forces != null) {
			
			addForces(forces);
		}
		//simulate = true;
		System.out.println("Mass: " + body.getMass());
		if(bdef.type == BodyType.DynamicBody)addForce(Force.getGravityForce(new Vector2(p.getCenterX(), p.getCenterY()), body.getMass()));
		
		currentPolys.add(this);
	}
	
	public float getMass() { return body.getMass();}
	public void setMass(float v) {
		
		mass = v;
		for (int i = 0; i < body.getFixtureList().size; i++) {
			
			Fixture f = body.getFixtureList().get(i);
			f.setDensity(p.getAreaOfTriangleByIndex(i)*(v/body.getFixtureList().size));
		}
		
		body.resetMassData();
	}
	public float getDensity() {return body.getFixtureList().get(0).getDensity();};
	float density;
	public void setDensity(float v) {
		
		density = v;
		for (Fixture f : body.getFixtureList()) {
			
			f.setDensity(v*2/body.getFixtureList().size);
		}
		
		body.resetMassData();
	}
	
	public float getFriction() {return body.getFixtureList().get(0).getFriction();}
	float friction;
	public void setFriction(float f) {
		
		friction = f;
		for (Fixture fi : body.getFixtureList()) {
			
			fi.setFriction(f);
		}
	}
	
	public float getRestitution() {return body.getFixtureList().get(0).getRestitution();}
	float restitution;
	public void setRestitution(float f) {
		
		restitution = f;
		for (Fixture fi : body.getFixtureList()) {
			
			fi.setRestitution(f);
		}
	}
	public Vector2 getPosition() {
		return body.getPosition();
	}
	public float getAngle() {
		
		return body.getAngle();
	}
	
	public float getRotation() {
		
		return getAngle()* MathUtils.degRad;
	}
	
	public void aplyForces() {
		
		for (Force c : Polygonforces) {
			
			
			if(!c.isGravityForce() && !c.aplied) {
				
				Vector2 f = c.getForceForComputing();
				body.applyLinearImpulse(f, c.getAplyPoint(), true);
				System.out.println("v " + c.getForce().x + " " + c.getForce().y);
				System.out.println("f "+ c.getAplyPoint().x + " " + c.getAplyPoint().y);
			}
			
		}
	}
	
	public void addForces(Force ... forces) {
		
		for (Force force : forces) {
			
			addForce(force);
		}
	}
	public void addForce(Force c) {
		
		Polygonforces.add(c);
		System.out.println("v " + c.getForce().x + " " + c.getForce().y);
		System.out.println("f "+ c.getAplyPoint().x + " " + c.getAplyPoint().y);
		
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
	public boolean isGravitable() {return getMass() != 0;}
	
	public void drawForces(ShapeRenderer r) {
		
		for (Force f: Polygonforces) {
			
			if(f.isGravityForce())f.draw(r, Color.RED);
			else f.draw(r, Color.BLUE);
		}
	}

	public BodyType getType() {
		return body.getType();
	}

	Force debug;
	public void aplyXForce(float x) {
		
		Polygonforces.remove(debug);
		if(debug != null) debug = new Force(p.getCenterV(), new Vector2(x, debug.getForce().y/Force.forceScale)); 
		else  debug = new Force(p.getCenterV(), new Vector2(x, 0)); 
		Polygonforces.add(debug);
	}
	
	public void aplyYForce(float y) {
		
		Polygonforces.remove(debug);
		if(debug != null)debug = new Force(p.getCenterV(), new Vector2(debug.getForce().x/Force.forceScale, y));
		else debug = new Force(p.getCenterV(), new Vector2(0, y));
		Polygonforces.add(debug);
	}
}


class Force{
	
	private static float Gravity = -9.8f;
	private static boolean changeGravity = false;

	public static int forceScale = 10;
	
	private static Vector2 GravityF;
	private Vector2 initial;
	private Vector2 end;
	private Vector2 force;
	
	private boolean g;
	
	public boolean isInstant = true;
	boolean aplied;
	
	private static ArrayList<Force> allForces = new ArrayList<>();
	
	public static void resetForces() {
		
		for (Force force : allForces) {
			
			force.aplied = false;
		}
	}
	public Force(Vector2 center,Vector2 force) {
		
		this.force = force.scl(forceScale);
		initial = center;
		end = new Vector2(center.x + force.x, center.y + force.y);
		allForces.add(this);
	}
	
	public static Force getGravityForce(Vector2 centerMass,float mass) {
		
		return new Force(centerMass, new Vector2(0, Gravity * mass)).setGravitable(true);
	}
	
	public Force setGravitable(boolean gravity) {
		
		if(gravity && !g) {
			
			force.scl(1/forceScale);
			end = new Vector2(initial.x + force.x, initial.y + force.y);
		}
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
	public Vector2 getAplyPoint() {
		
		return new Vector2(initial).scl(1f/Scene.currentUnit, 1f/Scene.currentUnit);
	}
	public Vector2 getForce() {
		
		return new Vector2(force).scl(1f/Scene.currentUnit, 1f/Scene.currentUnit);
	}
	public void setInstant() {
		
		isInstant = true;
		aplied = false;
	}
	public Vector2 getForceForComputing() {

		System.out.println("Force");
		if(isGravityForce()) return null;
		if(isInstant) {
			
			if(!aplied) {
				
				aplied = true;
				return getForce();
				
			}else return null;
		}else {
			
			return getForce();
		}
	}
	public void draw(ShapeRenderer rend,Color r) {
	
		rend.setColor(r);
		rend.begin(ShapeType.Filled);
		float angle = MathUtils.atan2(end.y - initial.y, end.x - initial.x);
		rend.rectLine(initial.x, initial.y, end.x, end.y, 1);
		float[] pos = new float[]{0,0,-2,2,-2,-2};
		Matrix3 a = new Matrix3().rotate(angle).translate(end.x, end.y);
		rend.triangle(end.x, end.y, end.x + (-2*MathUtils.cos(angle) - 2*MathUtils.sin(angle)), 
					end.y + (-2*MathUtils.cos(angle) + 2*MathUtils.sin(angle)), 
					end.x + (-2*MathUtils.cos(angle) + 2*MathUtils.sin(angle)), 
					end.y + (-2*MathUtils.cos(angle) - 2*MathUtils.sin(angle)));
		rend.end();
	}
}