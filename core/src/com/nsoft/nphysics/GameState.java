package com.nsoft.nphysics;

import java.util.HashMap;

public class GameState {

	static HashMap<GameCode,String> dic = new HashMap<>();
	static GameCode current = GameCode.IDLE;
	static {
		
		dic.put(GameCode.CREATE_SOLID, "Crea un objeto con el cursor");
		dic.put(GameCode.MOVE, "Mueve el plano con arrastrando cursor");

	}
	
	public static GameCode getCurrentState() {return current;}
	public static void setCurrentState(GameCode state) {current = state;}
	
	static 
	enum GameCode{IDLE,CREATE_SOLID,MOVE}
}
