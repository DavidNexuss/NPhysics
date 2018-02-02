package com.nsoft.nphysics;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class DSL {

	ArrayList<Polygon> polys;

	public DSL(Polygon ... polygons) {
		
		for (Polygon polygon : polygons) {
			
			polys.add(polygon);
		}
		
		
	}
	
	/*public float getSumForceX(Polygon p) {
		
		float forceX;
		
		for (Polygon polygon : polys) {
			
		}
	}*/
}
