package com.nsoft.nphysics;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.nsoft.nphysics.ui.UIScene;

public class MenuWindow extends Window {

	ArrayList<Definer> defs = new ArrayList<>();
	HashMap<String, Definer> defs_map = new HashMap<>();
	
	Polygon p;
	Table root;
	
	Table configs = new Table();
	
	public MenuWindow(Polygon p,Skin skin) {	
		
		super("Configuracion", skin);
		this.p = p;
		root = UIScene.createDefaultWindowStructure(this);
		root.add(configs).expand().fill();
		setWidth(500);
		setHeight(400);
		
		if(p.def.getType() == BodyType.DynamicBody) {
		addDefiner("Densidad", new Slider(1, 100, 1, false, skin), "kg/m^2",(v)->{
			
			p.def.setDensity(v);
			defs_map.get("Massa").setNewValue(p.def.getMass());
			
		},1f);
		addDefiner("Massa", new Slider(1, 100, 1, false, skin), "kg",(v)->{
			
			p.def.setMass(v);
			defs_map.get("Densidad").setNewValue(p.def.getDensity());
		},()->{return p.def.getMass();});
		
		addDefiner("Friccion", new Slider(0, 100, 1f, false, skin), "%",(v)->{
			
			p.def.setFriction(v/100);
		},50);
		addDefiner("Rebote", new Slider(0, 100, 1f, false, skin), "%",(v)->{
			
			p.def.setRestitution(v/100);
		});
		addDefiner("Fuerza X", new Slider(-1000, 1000, 10, false, skin), "N",(v)->{
			
			p.def.aplyXForce(v);
		},0);
		addDefiner("Fuerza Y", new Slider(-1000, 1000, 10, false, skin), "N",(v)->{
			
			p.def.aplyYForce(v);
		},0);
		}
	}
	public void addDefiner(String name,Slider a,String append,Value v,GetValue g) {
		
		addDefiner(name, a, append, v, a.getValue(),g);
	}
	public void addDefiner(String name,Slider a,String append,Value v,float def) {
		
		addDefiner(name, a, append, v, def,null);
	}
	public void addDefiner(String name,Slider a,String append,Value v) {
		
		addDefiner(name, a, append, v, a.getValue(),null);
	}
	public void addDefiner(String name,Slider a,String append,Value v,float defValue,GetValue g) {
		
		
		
		Label def = new Label(a.getValue() + append, getSkin());
		Definer p = new Definer(name, a, def, append, v,defValue,g);
		
		configs.add(new Label(name, getSkin())).pad(5).expandX();
		configs.add(a).pad(5).expandX().fill().center();
		configs.add(def).pad(5).expandX().width(100).center().row();
		
		defs.add(p);
		defs_map.put(name, p);
		
	}
	
	@Override
	public void act(float delta) {
		
		super.act(delta);
		
		if(!isVisible()) return;
		for (Definer definer : defs) {
			
			definer.act();
		}
	}
	class Definer {
		
		String name;
		String append;
		Value v;
		GetValue val;
		Slider p;
		Label def;
		
		public boolean useCustomGetter() {return val != null;}
		private float old;
		private boolean bubbles = false;
		
		public Definer(String name,Slider p, Label def,String append,Value v) {
			
			this(name, p, def, append, v, p.getValue(),null);
		}
		public Definer(String name,Slider p, Label def,String append,Value v,float defaultValue,GetValue val) {

			this.name = name;
			this.append = append;
			this.v = v;
			this.p = p;
			this.def = def;
			this.val = val;
			if(useCustomGetter()) defaultValue = val.getValue();
			System.out.println("Def val " + defaultValue);	
			setNewValue(defaultValue);
			old = p.getValue();
		}
		
		public void setNewValue(float f) {
			
			p.setValue(f);
			def.setText(f + " " + append);
			bubbles = true;
		}
		public void act() {
			
			if(old != p.getValue()) {
				
				old = p.getValue();
				if(!useCustomGetter())
				def.setText(old + " " + append);
				else def.setText(Math.round(val.getValue()) + " " + append);
				if(!bubbles)v.operate(old);
				else bubbles = false;
				
			}
		}
	
	}
}
