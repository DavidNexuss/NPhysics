package com.nsoft.nphysics.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.nsoft.nphysics.ContextMenuItem;
import com.nsoft.nphysics.GameState;
import com.nsoft.nphysics.GameState.GameCode;

public class UIScene extends Stage{

	Skin skin;
	TextButton createMode;
	TextButton moveMode;
	Window helpWindow;
	Label currentOperation;
	
	public UIScene() {
		
		skin = new Skin(Gdx.files.internal("skin-files/skin/neutralizer-ui.json"));
		createMode = new TextButton("Crear Objeto",skin);
		
		VisUI.load(SkinScale.X2);

		Table main = new Table(skin);
		Menu crear = new Menu("Crear Objeto");

		MenuItem solid = new MenuItem("Crear Solido");
			solid.addListener(new ClickListener() {
				
				@Override
				public void clicked(InputEvent event, float x, float y) {
					
					GameState.setGameState(GameCode.CREATE_SOLID);
					super.clicked(event, x, y);
				}
			});
		MenuItem barra= new MenuItem("Crear Barra");
		
		crear.addItem(solid);
		crear.addSeparator();
		crear.addItem(barra);
		
		Menu ver = (Menu) new Menu("Ver");
		Menu parametros = new Menu("Parametros");
			MenuItem opgraf= new MenuItem("Opciones Graficas");
			
			parametros.addItem(opgraf);
		
		Menu ayuda = new Menu("Ayuda");
		MenuItem ayudap = new MenuItem("Panel de ayuda");
		ayudap.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			
			super.clicked(event, x, y);
			showWindow(helpWindow);
		}});
		ayuda.addItem(ayudap);
		Menu simulacion = new Menu("Simulacion");
			
		MenuBar menuBar = new MenuBar();
			
		menuBar.addMenu(crear);
		menuBar.addMenu(ver);
		menuBar.addMenu(parametros);
		menuBar.addMenu(ayuda);
		menuBar.addMenu(simulacion);
		
		menuBar.getTable().pad(5);
		menuBar.getTable().padLeft(5);
		Table options = new Table(skin);
		options.add(menuBar.getTable()).fillX().expand();
		
		Table bar = new Table(skin);
		bar.add(options).fillX().expand();
		currentOperation = new Label(GameState.getCurrentStateInfo(), skin);
		currentOperation.setColor(Color.BLACK);
		currentOperation.setFontScale(1.5f);
		bar.setSize(getWidth(), 70);
		
		Table menu = new Table(skin);
		menu.setWidth(50);
		menu.add(new ContextMenuItem("test", new Texture(Gdx.files.internal("bin2.png")), ()->{}));
		
		
		main.add(bar).align(Align.top).fillX().expand().row();
	//	main.add(menu).align(Align.left).fillY();
		main.setFillParent(true);
		
		main.add(currentOperation).align(Align.bottomLeft).pad(10);
		addActor(main);
		
		
		createHelpWindow();
		
		main.debug();
		showWindow(helpWindow);
		//getRoot().debug();
	}
	
	public void createDefaultWindowStructure(Window w) {
		
		Table t = new Table(skin);
		t.setFillParent(true);
		TextButton cerrar = new TextButton("Cerrar", skin);
		cerrar.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			
			super.clicked(event, x, y);
			hideWindow(w);
		}});
		t.add(cerrar).center().bottom();	
		
		w.add(t).expand().fill();
		
		getRoot().debug();
	}
	public void createHelpWindow() {
		
		helpWindow = new Window("Ayuda", skin);
		createDefaultWindowStructure(helpWindow);
		helpWindow.setVisible(false);
		
		addActor(helpWindow);
	}
	
	public void showWindow(Window w) {
		
		if(w.isVisible())return;
		w.setPosition(getWidth()/2 - w.getWidth()/2, getHeight()/2 - w.getHeight()/2 - 10);
		w.center();
		w.setColor(1, 1, 1, 0);
		w.setVisible(true);
		w.addAction(Actions.parallel(Actions.fadeIn(0.2f), Actions.moveBy(0, 10, 0.2f)));
	}
	
	public void hideWindow(Window w) {
		
		if(!w.isVisible()) return;
		w.addAction(Actions.parallel(Actions.fadeOut(0.2f), Actions.moveBy(0, -10, 0.2f)));
		new Thread(()->{
			
			try {
				
				Thread.sleep(200);
				w.setVisible(false);
				
			} catch (Exception e) {
				
			}
		});
		
	}
}
