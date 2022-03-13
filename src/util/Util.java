package util;

import java.io.FileWriter;
import java.io.IOException;

public class Util {

	public void createFile(String nomeArquivo) {
	    try {
	      FileWriter myWriter = new FileWriter("filename.txt");
	      myWriter.close();
	      System.out.println("Arquivo ");
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	  }
	
//	public void escreve(String texto) 
//	  {
//	    Path filePath = Paths.get("C:/", "temp", "test.txt");
//	 
//	    try
//	    {
//	      //Write content to file
//	      Files.writeString(filePath, "Hello World !!", StandardOpenOption.APPEND);
//	 
//	      //Verify file content
//	      String content = Files.readString(filePath);
//	 
//	      System.out.println(content);
//	    } 
//	    catch (IOException e) 
//	    {
//	      e.printStackTrace();
//	    }
//	  }
}
