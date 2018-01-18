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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
	public static Grid mainGrid;
	public static ArrayList<Grid> grids = new ArrayList<>();
	
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
			ContextMenu.setVisible(false);
		})).center().pad(5);
		ContextMenu.add(new ContextMenuItem("configure", new Texture(Gdx.files.internal("plus2.png")), ()->{
			
			UIScene.showActor(selected.getMenu(), NPhysics.ui);
		})).center().pad(5);
		
		addActor(ContextMenu);
		ContextMenu.setVisible(false);
		
		addGrid(new Grid(shape_renderer, (int)(getWidth()/2), (int)getHeight()/2, gridSize, 0, (int)getWidth(), (int)getHeight()));
		mainGrid = grids.get(0);
		addGrid(new Grid(shape_renderer, 20, 20, 10, 0, 200, 100));
		
	}
	
	
	public void init() {
		
		//processMove.start();	
		
	}
	
	public void addGrid(Grid d) {
		
		grids.add(d);
	}
	@Override
	public void draw() {
		
		mainGrid.setPos((int)getCamera().position.x, (int)getCamera().position.y);
		//if(drawGrid)drawGrid();
		drawGrids();
		if(drawLines)drawLines();
		drawPolys();
		PolygonDefinition.step(getCamera().combined);
		super.draw();
	}
	
	private void drawGrids() {
		
		shape_renderer.begin(ShapeType.Line);
		shape_renderer.setColor(0.8f, 0.8f, 0.8f, 1f);
		for (Grid grid : grids) {
			
			grid.draw();
		}
		
		shape_renderer.end();
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
		if(zoom > 0) {
			c.zoom *= 2;
		}
		else {
			c.zoom /= 2;
		}
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
		
		if(p == null) {ContextMenu.setVisible(false); return;}
		selected = p;
		if(!ContextMenu.isVisible()) {
			
			ContextMenu.setVisible(true);
			ContextMenu.setPosition(selected.getCenterX() - ContextMenu.getWidth()/2, selected.getCenterY() - ContextMenu.getHeight()/2);
		} 
		else {
			
			ContextMenu.addAction(Actions.moveTo(selected.getCenterX() - ContextMenu.getWidth()/2, selected.getCenterY() - ContextMenu.getHeight()/2, 0.5f,Interpolation.pow3Out));
		}
		selected = p;
		
	}
	public void proccessClick(int x, int y) {
		
		if(GameState.current == GameCode.CREATE_SOLID) {
			
			if(!current) {
				
				Pos1.set(x, y, 0);
				Pos1.set(getCamera().unproject(Pos1));
				
				float dist = 0;
				Grid d = grids.get(0);
				
				if(grids.size() > 1) {
				for (int i = 0; i < grids.size(); i++) {
				
					float r = grids.get(i).snapLenght(Pos1);
					if(r < dist) d = grids.get(i);
				}
				}
				Pos1.set(d.snap(Pos1.x),d.snap(Pos1.y),0);
				
				current = true;
				polygons.add(new Polygon(BodyType.DynamicBody));
			}else {
				
				Vector3 v = new Vector3(x, y, 0);
				v = getCamera().unproject(v);
				int ycur = (int) v.y;
				int xcur = (int) v.x;
				
				float dist = 0;
				Grid d = grids.get(0);
				
				if(grids.size() > 1) {
				for (int i = 0; i < grids.size(); i++) {
				
					float r = grids.get(i).snapLenght(Pos1);
					if(r < dist) d = grids.get(i);
				}
				}
				
				xcur = d.snap(xcur);
				ycur = d.snap(ycur);
				
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
