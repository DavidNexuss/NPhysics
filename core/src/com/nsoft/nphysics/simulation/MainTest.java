package com.nsoft.nphysics.simulation;

import com.badlogic.gdx.math.Vector2;
import static com.nsoft.nphysics.simulation.Force.*;
public class MainTest {

	static DSL test;
	public static void main(String[] args) {
		
		Solid a = new Solid(new Vector2(20, 20));
		
		
		a.extraForces.add(force(0, 0, NULL, NULL));
		a.extraForces.add(force(pol(5, 45),0,NULL).setAngleDegrees(-90));
		a.extraForces.add(force(pol(10, 45),0,-40));
		
		test = new DSL(a);
		test.solve();
		
		test.checkSum();
		
	}
}
