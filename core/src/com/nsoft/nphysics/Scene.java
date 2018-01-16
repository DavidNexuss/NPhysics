package com.nsoft.nphysics;

import java.util.ArrayList;

import org.omg.CORBA.PolicyTypeHelper;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nsoft.nphysics.GameState.GameCode;
import com.nsoft.nphysics.ui.UIScene;

public class Scene extends Stage {

	private ShapeRenderer shape_renderer = new ShapeRenderer();
	public static final int gridSize = 30;
	public static int currentUnit;
	
	static { //UNIT CONVERSOR
		
		currentUnit = gridSize;
	}
	private static Thread processMove;
	
	public static Table ContextMenu;
	private boolean drawGrid = true;
	private boolean drawLines = true;
	
	public static ArrayList<Polygon> polygons = new ArrayList<>();
	public static Polygon selected;

	private boolean current = false;
	private final Vector3 Pos = new Vector3();
	private final Vector3 Pos1 = new Vector3();
	static {
		
		processMove = new Thread(()->{
			
			try {
				
				while(true) {
					
					Thread.sleep(10);
					if(NPhysics.mouse.isMouseInWindow())NPhysics.scene.processMove(Gdx.input.getX(), Gdx.input.getY());
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public Scene() {
		super();
		shape_renderer.setColor(.8f, .8f, .8f, 1);
		init();
		ContextMenu = new Table();	
		ContextMenu.add(new ContextMenuItem("remove", new Texture(Gdx.files.internal("bin2.png")), ()->{
			
			polygons.remove(selected);
			selected = null;
			UIScene.hideActor(ContextMenu);
		})).center().pad(5);
		ContextMenu.add(new ContextMenuItem("configure", new Texture(Gdx.files.internal("plus2.png")), ()->{
			
			UIScene.showActor(selected.getMenu(), NPhysics.scene);
		})).center().pad(5);
		
		addActor(ContextMenu);
		ContextMenu.setVisible(false);
	}
	
	public void init() {
		
		//processMove.start();	
		
	}
	@Override
	public void draw() {
		
		if(drawGrid)drawGrid();
		if(drawLines)drawLines();
		drawPolys();
		PolygonDefinition.step(getCamera().combined);
		super.draw();
	}

	private void drawPolys() {
		
			shape_renderer.begin(ShapeType.Filled);
			shape_renderer.setColor(0.3f, 0.8f, 0.3f, 0.6f);
		
		for (Polygon polygon : polygons) {
			
			polygon.draw(shape_renderer,selected);
		}
		
			shape_renderer.end();
		
	}
	private void drawLines() {
		
		shape_renderer.setProjectionMatrix(getCamera().combined);
		shape_renderer.begin(ShapeType.Line);

		shape_renderer.setColor(.8f, .8f, .8f, 1);
		
		if(current) {
			
			Pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Vector3 cur = getCamera().unproject(Pos);
			int ycur = (int) cur.y;
			int xcur = (int) cur.x;
			
			xcur = xcur > gridSize/2 ? xcur - (xcur % gridSize) : xcur + (xcur % gridSize);
			ycur = ycur > gridSize/2 ? ycur - (ycur % gridSize) : ycur + (ycur % gridSize);
			
			shape_renderer.setColor(Color.RED);
			shape_renderer.line(Pos1.x, Pos1.y, cur.x, cur.y);
			shape_renderer.setColor(.8f, .8f, .8f, 1);
		}
		
			shape_renderer.setColor(Color.BLACK);
			for (Polygon polygon : polygons) {
				
				polygon.drawLines(shape_renderer);
			}
			shape_renderer.setColor(.8f, .8f, .8f, 1);
		
		shape_renderer.end();
		
	}
	private void drawGrid() {


		shape_renderer.setProjectionMatrix(getCamera().combined);
		shape_renderer.begin(ShapeType.Line);

		shape_renderer.setColor(.8f, .8f, .8f, 1);
		int y = (int) (getCamera().position.y - getHeight()/2);
		int x = (int) (getCamera().position.x - getWidth()/2);
		
		//float zoom = ((OrthographicCamera)getCamera()).zoom;
		
		
		for (int i = y/gridSize; i < (getHeight()/gridSize) + y/gridSize + 1; i++) {
			
			shape_renderer.line(-(Math.abs(x)), i*gridSize, getWidth() + x, i*gridSize);
			
		}
		
		for (int i = x/gridSize; i < (getWidth()/gridSize) + x/gridSize + 1; i++) {
			
			shape_renderer.line(i*gridSize,-(Math.abs(y)), i*gridSize, getHeight()+ y);
		}
		
		
		shape_renderer.end();
		
	}

	public void processMove(int screenX, int screenY) {
		
		if(screenX < getWidth()/10)
			getCamera().translate((float) -Math.sqrt((getWidth()/10 - screenX)), 0, 0);
		if(screenX > getWidth()*9/10)
			getCamera().translate((float) Math.sqrt(screenX - getWidth()*9/10), 0, 0);
		if(screenY < getHeight()/10)
			getCamera().translate(0,(float) Math.sqrt(getHeight()/10 - screenY), 0);
		if(screenY > getHeight()*9/10)
			getCamera().translate(0,(float) -Math.sqrt((screenY - getHeight()*9/10)), 0);
		
		getCamera().update();
		
	}
	
	public void zoom(int zoom) {
		
		OrthographicCamera  c = ((OrthographicCamera)getCamera());
		if(zoom > 0)c.zoom *= 2;
		else c.zoom /= 2;
		System.out.println(c.zoom);
	}
	
	public void newLine(float x1,float y1,float x2,float y2) {
		
		if(!(x1 == x2 && y1 == y2)) {
			
			Line a = new Line(x1, y1, x2, y2);
			Polygon p = polygons.get(polygons.size() -1);
			if(!Line.exists(p,a)) {

				
				p.addLine(a);
				if(p.isEmpty()) { p.addVertex(x1, y1); p.addVertex(x2, y2);}
				else if(p.Vertexs.get(0) == x2 && p.Vertexs.get(1) == y2) {
					
					p.end();
					current = false;
				}else {
					
					p.addVertex(x2, y2);
				}
				
			}
			else System.err.println("Same line");
		}
	}
	public static void select(Polygon p) {
		
		if(p == null)ContextMenu.setVisible(false);
		else if(!ContextMenu.isVisible()) 
			UIScene.showActor(ContextMenu, NPhysics.scene);
		selected = p;
		ContextMenu.setPosition(selected.getCenterX() - ContextMenu.getWidth()/2, selected.getCenterY() - ContextMenu.getHeight()/2);
		
	}
	public void proccessClick(int x, int y) {
		
		if(GameState.current == GameCode.CREATE_SOLID) {
			
			if(!current) {
				
				Pos1.set(x, y, 0);
				Pos1.set(getCamera().unproject(Pos1));

				int ycur = (int) Pos1.y;
				int xcur = (int) Pos1.x;
				

				System.out.println(xcur % gridSize);
				
				xcur = xcur % gridSize > gridSize/2 ? xcur + (gridSize - (xcur % gridSize) ): xcur - (xcur % gridSize);
				ycur = ycur % gridSize > gridSize/2 ? ycur + (gridSize - (ycur % gridSize) ): ycur - (ycur % gridSize);
				
				Pos1.set(xcur, ycur, 0);
				
				current = true;
				polygons.add(new Polygon(BodyType.DynamicBody));
			}else {
				
				Vector3 v = new Vector3(x, y, 0);
				v = getCamera().unproject(v);
				int ycur = (int) v.y;
				int xcur = (int) v.x;
				
				xcur = xcur % gridSize > gridSize/2 ? xcur + (gridSize - (xcur % gridSize) ): xcur - (xcur % gridSize);
				ycur = ycur % gridSize > gridSize/2 ? ycur + (gridSize - (ycur % gridSize) ): ycur - (ycur % gridSize);
					
				if(!(xcur == Pos1.x && ycur == Pos1.y)) {
					newLine(Pos1.x,Pos1.y, xcur, ycur);
					Pos1.x = xcur;
					Pos1.y = ycur;
				}
			}
		}else if(GameState.current == GameCode.IDLE) {
			
			if(!polygons.isEmpty()) {
			
				Vector3 v = new Vector3(x, y, 0);
				v = getCamera().unproject(v);
				int ycur = (int) v.y;
				int xcur = (int) v.x;
				
				for (Polygon polygon : polygons) {
				
					if(polygon.hit(xcur,ycur)) {
						
						select(polygon);
						break;
					};
				}
			}
		}
		
	}
}
