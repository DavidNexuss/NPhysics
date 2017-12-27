package com.nsoft.nphysics;

import java.util.HashMap;

public class GameState {

	static HashMap<GameCode,String> dic = new HashMap<>();
	static GameCode current = GameCode.CREATE_SOLID;
	static {
		
		dic.put(GameCode.IDLE, "Haz clic en ayuda para empezar");
		dic.put(GameCode.CREATE_SOLID, "Crea un objeto con el cursor");
		dic.put(GameCode.MOVE, "Mueve el plano con arrastrando cursor");
		
	}
	
	public static String getCurrentStateInfo() { return dic.get(current);}
	
	public static GameCode getCurrentState() {return current;}
	public static void setGameState(GameCode code) { current = code;}
	public static void setCurrentState(GameCode state) {current = state;}
	
	public static enum GameCode{IDLE,CREATE_SOLID,MOVE}
}
