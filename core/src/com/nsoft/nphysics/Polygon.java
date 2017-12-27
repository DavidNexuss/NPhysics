package com.nsoft.nphysics;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.MaskFormatter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

import earcut4j.Earcut;

public class Polygon{

	static Texture textureSolid;
	static PolygonSpriteBatch batch;
	public boolean isReady = false;
	List<Integer> indexes;
	public float[] vertexs;
	ArrayList<Vector2> vertices = new ArrayList<>();
	PolygonSprite sprite;
	Path2D path;
	
	public static void setProjectionMatrix(Matrix4 m) {
		

		batch = new PolygonSpriteBatch();
		//batch.setTransformMatrix(m);
		
		Pixmap p = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
		p.setColor(0.3f, 0.8f, 0.3f, 0.8f);
		p.fillRectangle(0, 0, 10, 10);
		textureSolid = new Texture(p);
		p.dispose();
		
	}
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
	
	public float[] getVertices() {
		
		if(vertexs != null) return vertexs;
		vertexs = new float[vertices.size()*2];
		
		for (int i = 0; i < vertexs.length; i++) {
			
			int index = i/2;
			if(i % 2 == 0 ) vertexs[i] = vertices.get(index).x;
			else vertexs[i] = vertices.get(index).y;
		}
		
		return getVertices();
	}
	
	public boolean isEnded() {return isReady;}
	public void end() {
		
		path.closePath(); 
		isReady = true;
		sprite = new PolygonSprite(new PolygonRegion(new TextureRegion(textureSolid), getVertices(), new short[10]));
		generateTraingles();
	}
	
	private void generateTraingles() {
		
		double[] data = new double[getVertices().length];
		for (int i = 0; i < data.length; i++) {
			
			data[i] = vertexs[i];
		}
		indexes = Earcut.earcut(data, null, 2);
		
		
	}
	void draw(ShapeRenderer rend) {
		
		if(isReady) {
			
			for (int i = 0; i < indexes.size(); i+=3) {
				
				try {
					

					rend.triangle(vertexs[indexes.get(i)], vertexs[indexes.get(i) + 1], 
							vertexs[indexes.get(i + 1)], vertexs[indexes.get(i + 1) + 1],
							vertexs[indexes.get(i + 2)], vertexs[indexes.get(i + 2) + 1]);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
	
	public boolean isEmpty() {return vertices.size() == 0;}
	
	public Rectangle getBounds() {return path.getBounds();}
	
}
