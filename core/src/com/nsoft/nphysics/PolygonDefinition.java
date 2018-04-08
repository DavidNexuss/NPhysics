package com.nsoft.nphysics;

import java.awt.geom.Path2D;
import java.util.ArrayList;


import static com.nsoft.nphysics.ui.UIScene.skin;

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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.nsoft.nphysics.ui.UIScene;

public class PolygonDefinition {

	Color debugColor;
	Texture mapTexture;
	static Window askForPhysicalInfo;
	
	//PHYSICAL
	private float mass;
	ArrayList<RForce> PolygonRForces = new ArrayList<>();
	
	private static World mundo = new World(RForce.getGravityVector(), true);
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
		mundo = new World(RForce.getGravityVector(), true);
		for (Polygon polygon : Scene.polygons) {
			
			ArrayList<RForce> RForcesB = new ArrayList<>();
			for (RForce RForce : polygon.def.PolygonRForces) {
				
				if(!RForce.isGravityRForce()) RForcesB.add(RForce);
				System.out.println(RForce.isGravityRForce());
			}
			
			RForce[] B = new RForce[RForcesB.size()];
			for (int i = 0; i < B.length; i++) {
				
				B[i] = RForcesB.get(i);
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
			
			polygonDefinition.aplyRForces();
		}
		
	}
	public PolygonDefinition(Polygon p) {
		
		this(p,-1,1,0.5f,0,null);
	}
		
	//BOX2D
	public PolygonDefinition(Polygon p,float mass,float density,float friction,float restitution,RForce ...RForces) {
		
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
		
		if(RForces != null) {
			
			addRForces(RForces);
		}
		//simulate = true;
		System.out.println("Mass: " + body.getMass());
		if(bdef.type == BodyType.DynamicBody)addRForce(RForce.getGravityRForce(new Vector2(p.getCenterX(), p.getCenterY()), body.getMass()));
		
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
	
	public void aplyRForces() {
		
		for (RForce c : PolygonRForces) {
			
			
			if(!c.isGravityRForce() && !c.aplied) {
				
				Vector2 f = c.getRForceForComputing();
				body.applyLinearImpulse(f, c.getAplyPoint(), true);
				System.out.println("v " + c.getRForce().x + " " + c.getRForce().y);
				System.out.println("f "+ c.getAplyPoint().x + " " + c.getAplyPoint().y);
			}
			
		}
	}
	
	public void addRForces(RForce ... RForces) {
		
		for (RForce RForce : RForces) {
			
			addRForce(RForce);
		}
	}
	public void addRForce(RForce c) {
		
		PolygonRForces.add(c);
		System.out.println("v " + c.getRForce().x + " " + c.getRForce().y);
		System.out.println("f "+ c.getAplyPoint().x + " " + c.getAplyPoint().y);
		
	}
	public void removeRForce(RForce c) {
		
		if(PolygonRForces.contains(c))PolygonRForces.remove(c);
	}
	public Vector2 sumRForces() {
		
		Vector2 sum = Vector2.Zero;
		for (RForce f: PolygonRForces) {
			
			sum.add(f.getRForce());
		}
		
		return sum;
	}
	public boolean isGravitable() {return getMass() != 0;}
	
	public void drawRForces(ShapeRenderer r) {
		
		for (RForce f: PolygonRForces) {
			
			if(f.isGravityRForce())f.draw(r, Color.BLUE);
			else f.draw(r, Color.RED);
		}
	}

	public BodyType getType() {
		return body.getType();
	}

	RForce debug;
	public void aplyXRForce(float x) {
		
		if(debug != null) debug.setXRForce(x);
		else {
			debug = new RForce(p.getCenterV(), new Vector2(x, 0));
			PolygonRForces.add(debug);
		}
	}
	
	public void aplyYRForce(float y) {
		
		if(debug != null) debug.setYRForce(y);
		else {
			debug = new RForce(p.getCenterV(), new Vector2(0, y));
			PolygonRForces.add(debug);
		}
	}
}

class NumberField extends TextField{
	
	static public final char BACKSPACE = 8;
	static public final char ENTER_DESKTOP = '\r';
	static public final char ENTER_ANDROID = '\n';
	static public final char TAB = '\t';
	static public final char DELETE = 127;
	static public final char BULLET = 149;
	
	Value val;
	private float number;
	private static final char[] accepted = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.',
													BACKSPACE,ENTER_ANDROID,TAB,DELETE,BULLET};
	
	public NumberField(float number,Skin skin,Value val) {
		super(number + "", skin);
		this.number = number;
		this.val = val;
	}
	
	public void setValue(float number) {
		
		this.number = number;
		setText(number + "");
	}
	
	private Thread change() {
		
		return new Thread(()->{
			
			try {
				Thread.sleep(40);
				val.operate(number);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		});
	}
	public float getNumber() { return number = Float.parseFloat(getText()); }
	
	class NumberInputListener extends TextField.TextFieldClickListener{
		
		@Override
		public boolean keyTyped(InputEvent event, char character) {
			
			boolean quit = true;
			for (char c : accepted) {
				
				if(c == character) {
					
					quit = false;
					break;
				}
			}
			
			if(character == ENTER_DESKTOP) {
				
				change().start();
				return true;
			}
			if(quit) return true;
			else return super.keyTyped(event, character);
			
		}
	}
	
}
class RForce{
	
	private static float Gravity = -9.8f;
	private static boolean changeGravity = false;

	public static int forceScale = 10;
	
	private static Vector2 GravityF;
	
	private Vector2 initial;
	private Vector2 end;
	private Vector2 RForce;
	private Vector2 unit;
	
	private boolean g;
	
	public boolean isInstant = true;
	boolean aplied;
	
	private Table root;
	private Window forceWindow;
	
	private static ArrayList<RForce> allRForces = new ArrayList<>();
	
	public static void resetRForces() {
		
		for (RForce RForce : allRForces) {
			
			RForce.aplied = false;
		}
	}
	public RForce(Vector2 center,Vector2 RForce) {
		
		this.RForce = RForce.scl(forceScale);
		initial = center;
		end = new Vector2(center.x + RForce.x, center.y + RForce.y);
		allRForces.add(this);

		unit = new Vector2(RForce.nor().scl((float) Math.sqrt(RForce.len())*30).add(initial));
		
		initUI();
	}
	
	NumberField NX,NY,NMod,NAngle;
	
	private void initUI() {
		
		root = new Table(skin);
		
		Table cartesian,polar;
		
		cartesian = new Table(skin);
		polar = new Table(skin);
		
		Label cartesianTitle = new Label("Expresion cartesiana", skin);
		Label polarTitle = new Label("Expresion polar", skin);
		
		Label X = new Label("X", skin);
		Label Y = new Label("Y", skin);
		
		Label Mod = new Label("Módulo", skin);
		Label Angle = new Label("Angulo", skin);
		

		NX = new NumberField(getForceX(), skin,(v)->{setXRForce(v);});
		NY = new NumberField(getForceY(), skin,(v)->{setYRForce(v);});
		NMod = new NumberField(getForceMod(), skin,(v)->{setMod(v);});
		NAngle = new NumberField(getForceAngle(), skin,(v)->{setAngle(v);});
		
		cartesian.add(cartesianTitle).expand().fill().row();
		cartesian.add(X).expand();
		cartesian.add(NX).expand().row();
		cartesian.add(Y).expand();
		cartesian.add(NY).expand().row();
		
		polar.add(polarTitle).expand().fill().row();
		polar.add(Mod).expand();
		polar.add(NMod).expand().row();
		polar.add(Angle).expand();
		polar.add(NAngle).expand().row();
		
		
		root.add(cartesian).expand();
		root.add(polar).expand();
		
		
	}
	public static RForce getGravityRForce(Vector2 centerMass,float mass) {
		
		return new RForce(centerMass, new Vector2(0, Gravity * mass)).setGravitable(true);
	}
	
	public RForce setGravitable(boolean gravity) {
		
		if(gravity && !g) {
			
			RForce.scl(1/forceScale);
			end = new Vector2(initial.x + RForce.x, initial.y + RForce.y);
		}
		g= gravity;
		return this;
	}
	
	public boolean isGravityRForce() { return g; }
	
	public static void setGravity(float newGravity) {
		
		recalculateGravity(newGravity);
		Gravity = newGravity;
		changeGravity = true;
		
	}
	
	private static void recalculateGravity(float newGravity) {
		
		for (RForce RForce : allRForces) {
			
			if (RForce.isGravityRForce()) {
				
				RForce.setRForce(Vector2.Zero.add(0, newGravity/Gravity));
			}
		}
	}
	public static float getGravity() {return Gravity;}
	public static Vector2 getGravityVector() {
		
		if(GravityF == null || changeGravity) { changeGravity = false;return GravityF = new Vector2(0, getGravity());}
		else return GravityF;	
	}
	public void setRForce(Vector2 newRForce) {
		
		end = new Vector2(initial).sub(RForce);
		this.RForce = newRForce;
		end = new Vector2(initial).add(RForce);
		unit = new Vector2(RForce).nor().scl((float) Math.sqrt(RForce.len())*30).add(initial);
	}
	
	public void setXRForce(float x) {
		
		setRForce(new Vector2(x, RForce.y));
	}
	public void setYRForce(float y) {
	
		setRForce(new Vector2(RForce.x,y));
	}
	
	//TODO: Implement Polar setter
	public void setMod(float mod) {}
	public void setAngle(float angle) {}
	
	public Vector2 getAplyPoint() {
		
		return new Vector2(initial).scl(1f/Scene.currentUnit, 1f/Scene.currentUnit);
	}
	public Vector2 getRForce() {
		
		return new Vector2(RForce).scl(1f/Scene.currentUnit, 1f/Scene.currentUnit);
	}
	
	public float getForceX() { return RForce.x; }
	public float getForceY() { return RForce.y; }
	
	public float getForceMod() { return RForce.len(); }
	
	public float getForceAngle(boolean degrees) { return degrees ? RForce.angle() : RForce.angleRad(); }
	/**@return Angle in degrees*/ 
	public float getForceAngle() {return getForceAngle(true);}
	
	
	public void setInstant() {
		
		isInstant = true;
		aplied = false;
	}
	public Vector2 getRForceForComputing() {

		System.out.println("RForce");
		if(isGravityRForce()) return null;
		if(isInstant) {
			
			if(!aplied) {
				
				aplied = true;
				return getRForce();
				
			}else return null;
		}else {
			
			return getRForce();
		}
	}
	public void draw(ShapeRenderer rend,Color r) {
	
		rend.setColor(r);
		rend.begin(ShapeType.Filled);
		float angle = MathUtils.atan2(unit.y - initial.y, unit.x - initial.x);
		rend.rectLine(initial.x, initial.y, unit.x, unit.y, 1);
		float[] pos = new float[]{0,0,-2,2,-2,-2};
		Matrix3 a = new Matrix3().rotate(angle).translate(unit.x, unit.y);
		rend.triangle(unit.x, unit.y, unit.x + (-2*MathUtils.cos(angle) - 2*MathUtils.sin(angle)), 
				unit.y + (-2*MathUtils.cos(angle) + 2*MathUtils.sin(angle)), 
				unit.x + (-2*MathUtils.cos(angle) + 2*MathUtils.sin(angle)), 
				unit.y + (-2*MathUtils.cos(angle) - 2*MathUtils.sin(angle)));
		rend.end();
	}
}