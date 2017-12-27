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
	
	
	boolean isReady = false;
	float[] vertexs;
	List<Integer> indexes;
	public ArrayList<Float> Vertexs = new ArrayList<>();
	public Polygon() {
		
	}
	
	public void addVertex(float x,float y) {
		
		if(!isReady) {

			Vertexs.add(x);
			Vertexs.add(y);
		}
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
	public void end() {
		
		isReady = true;
		generateTraingles();
		System.out.println("End");
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
					
					rend.triangle(getX(indexes.get(i)), getY(indexes.get(i)), 
							getX(indexes.get(i + 1)), getY(indexes.get(i + 1)),
							getX(indexes.get(i + 2)), getY(indexes.get(i + 2)));
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean isEmpty() {return Vertexs.isEmpty();}
	
	
}
