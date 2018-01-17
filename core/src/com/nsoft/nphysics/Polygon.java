package com.nsoft.nphysics;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.MaskFormatter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.nsoft.nphysics.ui.UIScene;

import earcut4j.Earcut;

public class Polygon{
	
	
	boolean isReady = false;
	float[] vertexs;
	
	private float[] center = new float[2];
	List<Integer> indexes;
	
	private float[] distances;
	private float[] angles;
	
	public ArrayList<Float> Vertexs = new ArrayList<>();
	Path2D path = new Path2D.Double();
	private BodyType t;
	private MenuWindow menuops;
	PolygonDefinition def;
	
	public Polygon(BodyType t) {
		
		this.t = t;
	}
	
	public void addVertex(float x,float y) {
		
		if(isEmpty()) path.moveTo(x, y);
		else path.lineTo(x, y);
		if(!isReady) {

			Vertexs.add(x);
			Vertexs.add(y);
		}
	}
	
	public float getCenterX() {	
		
		if(center[0] != 0) return center[0];
		if(!isReady) throw new IllegalStateException();
		
		float[] v = getVertices();
		float sum = 0;
		for (int i = 0; i < v.length; i+=2) {
			
			sum+= v[i];
		}
		
		return center[0] = sum/(v.length/2);
		
	}
	
	public float getCenterY() {
		
		if(center[1] != 0) return center[1];
		if(!isReady) throw new IllegalStateException();
		
		float[] v = getVertices();
		float sum = 0;
		for (int i = 1; i < v.length; i+=2) {
			
			sum+= v[i];
		}
		
		return center[1] = sum/(v.length/2);
		
	}
	
	public Vector2 getCenterV() {
		
		return new Vector2(getCenterX(), getCenterY());
	}
	public float[] getCenter() {
		
		if(center[0] != 0 && center[1] != 0 ) return center;
		if(!isReady) throw new IllegalStateException();
		
		float[] v = getVertices();
		float sumx = 0;
		float sumy = 0;
		
		for (int i = 0; i < v.length; i++) {
			
			if(i % 2 == 0) sumx+= v[i];
			else sumy+= v[i];
		}
		
		return center = new float[] {sumx/(v.length/2),sumy/(v.length/2)};
		
	}
	public float[] getVertices() {
		
		if(!isReady) throw new IllegalStateException();
		if(vertexs != null) return vertexs;
		vertexs = new float[Vertexs.size()];
		
		for (int i = 0; i < vertexs.length; i++) {
			
			vertexs[i] = Vertexs.get(i);
		}
		
		return getVertices();
	}
	
	public float getX(int vertex) {
		
		if(vertex > getVertices().length/2 - 1) return Float.MIN_VALUE;
		else {
			
			return getVertices()[vertex*2];
		}
	}
	
	public float getY(int vertex) {
		
		if(vertex > getVertices().length/2 - 1) return Float.MIN_VALUE;
		else {
			
			return getVertices()[vertex*2 + 1];
		}
	}
	public boolean isEnded() {return isReady;}
	
	public PolygonDefinition getDefinition() {if(isEnded())return def; else throw new IllegalStateException();}
	
	public void end() {
	
		path.closePath();

		isReady = true;
		
		generateTraingles();				//GENERATES THE TRIANGLES
		computeArea();						//COMPUTES THE AREA
		loadPhysicalDefinition();			//LOADS A PHYSICAL DEF
		loadMenuWindow();					//CREATES THE CONFIG MENU	
		
		angles = new float[vertexs.length/2];
		for (int i = 0; i < angles.length; i++) {
			
			angles[i] = Float.MAX_VALUE;
		}
		
		distances = new float[vertexs.length/2];
		for (int i = 0; i < angles.length; i++) {
			
			distances[i] = Float.MAX_VALUE;
		}
		
	}
	
	public void destroy() {
		
		NPhysics.scene.getActors().removeValue(menuops, true);
	}
	private void loadMenuWindow() {
		
		menuops = new MenuWindow(this, UIScene.skin);
		menuops.setVisible(false);
		NPhysics.ui.addActor(menuops);
	}
	private void loadPhysicalDefinition() {
		
		def = new PolygonDefinition(this);
		
	}
	
	
	private void generateTraingles() {
		
		double[] data = new double[getVertices().length];
		for (int i = 0; i < data.length; i++) {
			
			data[i] = vertexs[i];
		}
		indexes = Earcut.earcut(data, null, 2);
		
		
	}
	
	void draw(ShapeRenderer rend, Polygon selected) {
		
		if(isReady) {

			if(PolygonDefinition.simulate) {
				
				for (int i = 0; i < indexes.size(); i+=3) {
					
					try {

						int v1 = indexes.get(i);
						int v2 = indexes.get(i + 1);
						int v3 = indexes.get(i + 2);
						
						float h1 = getDistance(v1);
						float h2 = getDistance(v2);
						float h3 = getDistance(v3);
						
						float a1 = getAngle(v1) + def.getAngle();
						float a2 = getAngle(v2) + def.getAngle();
						float a3 = getAngle(v3) + def.getAngle();
						
				  rend.triangle(
						  		def.getPosition().x * Scene.currentUnit + (MathUtils.cos(a1)*h1), //1
						  		def.getPosition().y * Scene.currentUnit + (MathUtils.sin(a1)*h1),
						  		def.getPosition().x * Scene.currentUnit + (MathUtils.cos(a2)*h2), //2
						  		def.getPosition().y * Scene.currentUnit + (MathUtils.sin(a2)*h2),
						  		def.getPosition().x * Scene.currentUnit + (MathUtils.cos(a3)*h3), //3
						  		def.getPosition().y * Scene.currentUnit + (MathUtils.sin(a3)*h3));
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
				
			}else {
			if(selected == this) rend.setColor(0.3f, 0.3f, 0.8f, 0.6f);
			for (int i = 0; i < indexes.size(); i+=3) {
				
				try {
					
					rend.triangle(getX(indexes.get(i)), getY(indexes.get(i)), 
							getX(indexes.get(i + 1)), getY(indexes.get(i + 1)),
							getX(indexes.get(i + 2)), getY(indexes.get(i + 2)));
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}	
			
			rend.end();
			def.drawForces(rend);
			rend.begin(ShapeType.Filled);
			rend.setColor(0.3f, 0.8f, 0.3f, 0.6f);
			}
		}
	}
	
	public boolean isEmpty() {return Vertexs.isEmpty();}

	public boolean hit(int x, int y) {
		
		if(path.contains(x, y)) {
			
			return true;
		}
		return false;
	}

	private float area;
	public float getArea() {return area;}
	public float getAreaOfTriangleByIndex(int i) {
		
		return getAreaOfTraingle(getTriangles(true, true)[i]);
	}
	public float getAreaOfTraingle(float[] fs) {
		
		return (fs[0]*(fs[3] - fs[5]) + fs[2]*(fs[5] - fs[1]) + fs[4]*(fs[1] - fs[3]));
	}
	private void computeArea() {
		
		float[][] triangles = getTriangles(true, true);
		float area = 0;
		
		for (float[] fs : triangles) {
		
			area += getAreaOfTraingle(fs);
		}
			
		this.area = area;
	}
	public float[][] getTriangles(boolean relative,boolean usePhysicalValue) {
		
		if(!isReady) throw new IllegalStateException();
		float[][] buf = new float[indexes.size()/3][6];
		
		for (int i = 0; i < indexes.size(); i+=3) {
		
			float[] triangle = new float[6];
			
			triangle[0] = getX(indexes.get(i));
			triangle[1] = getY(indexes.get(i));
			triangle[2] = getX(indexes.get(i + 1));
			triangle[3] = getY(indexes.get(i + 1));
			triangle[4] = getX(indexes.get(i + 2));
			triangle[5] = getY(indexes.get(i + 2));
			
			buf[i/3] = relative ? getRelativeVertexs(triangle) : triangle;
		}
		
		if(usePhysicalValue) {
			
			for (float[] fs : buf) {
				
				for (int i = 0; i < fs.length; i++) {
					
					fs[i] /= Scene.currentUnit;
				}
			}
		}
		return buf;
	}
	
	public float getDistance(int vertex) {
		
		if(distances[vertex] != Float.MAX_VALUE) return distances[vertex];
		else {
			
			return distances[vertex] = (float) Math.sqrt(Math.pow(getRelativeToCenterX(getX(vertex)), 2) + Math.pow(getRelativeToCenterY(getY(vertex)), 2));
		}
		
	}
	
	public float getAngle(int vertex) {
		
		if(angles[vertex] != Float.MAX_VALUE) return angles[vertex];
		else {
			
			return angles[vertex] = MathUtils.atan2(getRelativeToCenterY(getY(vertex)), getRelativeToCenterX(getX(vertex)));
		}
	}
	public float getRelativeToCenterX(float x) {
		
		return x - getCenterX();
	}
	
	public float getRelativeToCenterY(float y) {
		
		return y - getCenterY();
	}
	
	public float[] getRelativeToCenterXY(float[] pos) {
		
		return pos = new float[] {getRelativeToCenterX(pos[0]),getRelativeToCenterY(pos[1])};
	}
	
	public float getRelativeToCornerX(float x) {
		
		return x - path.getBounds().x;
	}
	
	public float getRelativeToCornerY(float y) {
		
		return y - path.getBounds().y;
	}
	
	public float[] getRelativeVertexs(float vertexA[]) {
		// TODO Auto-generated method stub
		if(!isReady) throw new IllegalStateException();
		float[] rel = new float[vertexA.length];
		
		for (int i = 0; i < vertexA.length; i++) {
			
			if(i % 2 == 0) rel[i] = vertexA[i] - getCenterX();
			else rel[i] = vertexA[i] - getCenterY();
		}
		return rel;
	}

	public ArrayList<Line> lines = new ArrayList<>();
	public void addLine(Line a) {
		
		lines.add(a);
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	public void drawLines(ShapeRenderer shape_renderer) {
		
		for (Line a : lines) {
			
			a.draw(shape_renderer);
		}
	}

	public MenuWindow getMenu() {
		
		if(isEnded()) return menuops;
		else throw new IllegalStateException();
	}

	
	
	
	// PHYSICAL VALUES
	public void setNewDensity(float v) {
		
		
	}

}
