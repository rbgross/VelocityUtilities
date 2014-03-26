package edu.ncsu.csc563.velocityUtilities;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ObjLoader {	
	
	/**
	 * Writes the .obj file to file with the format pos.x, pos.y, pos.z, nor.x, nor.y, nor.z
	 */
	public static void objToAndroidBinary(String fileName) throws NumberFormatException, IOException {
		BufferedReader buffer = new BufferedReader(new FileReader(fileName));
		System.out.println("Processing: " + fileName);
		String line;
		
		ArrayList<Float> tempVertices = new ArrayList<Float>();
		ArrayList<Float> tempNormals = new ArrayList<Float>();
		//ArrayList<Float> tempTexCoords = new ArrayList<Float>();
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> elements = new ArrayList<Integer>();
		
		while ((line = buffer.readLine()) != null) {			
			StringTokenizer parts = new StringTokenizer(line, " ");
		    int numTokens = parts.countTokens();
		    if (numTokens == 0)
		            continue;
		    String token = parts.nextToken();
			
			if (token.equals("v")) {
				tempVertices.add(Float.parseFloat(parts.nextToken()));
				tempVertices.add(Float.parseFloat(parts.nextToken()));
				tempVertices.add(Float.parseFloat(parts.nextToken()));
			} else if (token.equals("vn")) {
				tempNormals.add(Float.parseFloat(parts.nextToken()));
				tempNormals.add(Float.parseFloat(parts.nextToken()));
				tempNormals.add(Float.parseFloat(parts.nextToken()));
			} 
			
			/*
			else if (token.equals("vt")) {
				tempTexCoords.add(Float.parseFloat(parts.nextToken()));
				tempTexCoords.add(Float.parseFloat(parts.nextToken()));
				tempTexCoords.add(Float.parseFloat(parts.nextToken()));
				
				lineScanner.close();
				continue;
			}
			*/
			
			else if (token.equals("f")) {
				for (int i = 0; i < 3; i++) {
					Scanner faceScanner = new Scanner(parts.nextToken());
					faceScanner.useDelimiter("/");
					
					int vertexIndex = 3 * (faceScanner.nextInt() - 1);
					int texCoordIndex = 3 * (faceScanner.nextInt() - 1);
					int normalIndex = 3 * (faceScanner.nextInt() - 1);
					
					vertices.add(tempVertices.get(vertexIndex + 0));
					vertices.add(tempVertices.get(vertexIndex + 1));
					vertices.add(tempVertices.get(vertexIndex + 2));
					//vertices.add(tempTexCoords.get(texCoordIndex + 0));
					//vertices.add(tempTexCoords.get(texCoordIndex + 1));
					//vertices.add(tempTexCoords.get(texCoordIndex + 2));	
					vertices.add(tempNormals.get(normalIndex + 0));
					vertices.add(tempNormals.get(normalIndex + 1));
					vertices.add(tempNormals.get(normalIndex + 2));				
					
					elements.add(elements.size());
					
					faceScanner.close();
				}
			}
		}
		
		buffer.close();
			
		float rawVertices[] = new float[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			rawVertices[i] = vertices.get(i);
		}
		
		int rawElements[] = new int[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			rawElements[i] = elements.get(i);
		}	
		
		ByteBuffer verticesCount = ByteBuffer.allocateDirect(4);
	    //verticesCount.order(ByteOrder.nativeOrder());
	    IntBuffer verticesCountBuffer = verticesCount.asIntBuffer();
	    verticesCountBuffer.put(rawVertices.length);
	    verticesCountBuffer.position(0);
		
        ByteBuffer vertexBB = ByteBuffer.allocateDirect(rawVertices.length * 4);
        //vertexBB.order(ByteOrder.nativeOrder());
        FloatBuffer outVertices = vertexBB.asFloatBuffer();
        outVertices.put(rawVertices);
        outVertices.position(0);
        
        ByteBuffer elementsCount = ByteBuffer.allocateDirect(4);
	    //count.order(ByteOrder.nativeOrder());
	    IntBuffer elementCountBuffer = elementsCount.asIntBuffer();
	    elementCountBuffer.put(rawElements.length);
	    elementCountBuffer.position(0);
        
        ByteBuffer elementBB = ByteBuffer.allocateDirect(rawElements.length * 4);
        //elementBB.order(ByteOrder.nativeOrder());
        IntBuffer outElements = elementBB.asIntBuffer();
        outElements.put(rawElements);
        outElements.position(0);
        
        String outFileName = "assets/output/" + fileName.substring(7, fileName.length() - 4) + ".vmf";
        FileOutputStream fos = new FileOutputStream(outFileName);
        FileChannel channel = fos.getChannel();
        channel.write(verticesCount);
        channel.write(vertexBB);
        channel.write(elementsCount);
        channel.write(elementBB);
        fos.close();
        
        System.out.println("Done converting " + fileName);
	}
}

