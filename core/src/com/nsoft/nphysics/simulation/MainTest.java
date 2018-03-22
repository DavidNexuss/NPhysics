package com.nsoft.nphysics.simulation;

import com.badlogic.gdx.math.Vector2;
import static com.nsoft.nphysics.simulation.Force.*;
public class MainTest {

	static DSL test;
	public static void main(String[] args) {
		
		Solid a = new Solid(new Vector2(20, 20));
		
		//Force b = new Force(new float[] {15,20},new float[] {NULL,NULL});
		Force c = new Force(new float[] {25,20}, 150,30+90);
		Force d = new Force(new float[] {25,20}, 150,30-90);
		
		a.extraForces.add(d);
		a.extraForces.add(c);
		test = new DSL(a);
		
		
		test.checkSum();
		
	}
}
