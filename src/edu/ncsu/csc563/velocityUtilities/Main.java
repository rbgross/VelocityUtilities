package edu.ncsu.csc563.velocityUtilities;

import java.io.File;
import java.io.IOException;

public class Main {
	
	public static void main(String [ ] args) {
		File dir = new File("assets/");
		for (File child : dir.listFiles()) {
			if (child.getName().endsWith(".obj")) {
				try {
					ObjLoader.objToAndroidBinary(child);
				} catch (NumberFormatException | IOException e) {
					System.out.println("An error occured");
					e.printStackTrace();
				}
			}
		}
		System.out.println("THAT'S ALL FOLKS");
	}
}
