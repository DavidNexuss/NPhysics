package com.nsoft.nphysics.simulation;

import com.badlogic.gdx.math.Vector2;
import static com.nsoft.nphysics.simulation.Force.*;
public class MainTest {

	static DSL test;
	public static void main(String[] args) {
		
		Solid a = new Solid(new Vector2(20, 20));
		
		//Force b = new Force(new float[] {15,20},new float[] {NULL,NULL});
		Force r = new Force(new float[] {20,20}, new float[] {NULL,20});
		Force x = new Force(new float[] {20,20}, new float[] {-25,NULL});
		Force t = new Force(new float[] {20,20}).setAngle(180 - 30).setMod(100);

		Force p = new Force(new float[] {20,20}).setAngle(180 - 45).setMod(200);
		
		a.extraForces.add(r);
		a.extraForces.add(t);
		a.extraForces.add(x);
		a.extraForces.add(p);
		
		test = new DSL(a);
		test.solve();
		
		test.checkSum();
		
	}
}
