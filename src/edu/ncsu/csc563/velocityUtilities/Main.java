package edu.ncsu.csc563.velocityUtilities;

import java.io.IOException;

public class Main {
	
	public static void main(String [ ] args) {
		try {
			ObjLoader.objToAndroidBinary("assets/DragonSmall.obj");
		} catch (NumberFormatException | IOException e) {
			System.out.println("An error occured");
			e.printStackTrace();
		}
	}
}
