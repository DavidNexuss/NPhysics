package com.nsoft.nphysics;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameState {

	static HashMap<GameCode,String> dic = new HashMap<>();
	static ArrayList<Label> labels = new ArrayList<>();
	static GameCode current = GameCode.CREATE_SOLID;
	static {
		
		dic.put(GameCode.IDLE, "Haz clic en ayuda para empezar");
		dic.put(GameCode.CREATE_SOLID, "Crea un objeto con el cursor");
		dic.put(GameCode.MOVE, "Mueve el plano con arrastrando cursor");
		dic.put(GameCode.DRAW_FORCE, "Dibuja un vector de fuerza");
		
	}
	
	public static String getCurrentStateInfo() { return dic.get(current);}
	
	public static GameCode getCurrentState() {return current;}
	
	public static void setCurrentState(GameCode state) {current = state; updateLabels();}
	
	public static void updateLabels() {
		
		for (Label label : labels) {
			
			label.setText(dic.get(current));
		}
	}
	public static void SwitchGameState(GameCode state) {
		
		if(current == state) current = GameCode.IDLE;
		else current = state;
	}
	public static enum GameCode{
	
		IDLE,
		CREATE_SOLID,
		MOVE, 
		DRAW_FORCE

	}
	public static void addLabel(Label currentOperation) {
	
		labels.add(currentOperation);
	}
}
