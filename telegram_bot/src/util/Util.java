package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

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
	
	public static String getSO() {
		 Properties prop = System.getProperties();
			Set<Object> keySet = prop.keySet();
			for (Object obj : keySet) {
				if(obj.toString().equals("os.name")) {
					final String osName = System.getProperty(obj.toString());
					System.out.println("OS: "+ osName);

					if(osName.toUpperCase().contains("MAC")) {
						return "MAC";
					}

					if(osName.toUpperCase().contains("WINDOWS")) {
						return "WINDOWS";
					}

					return osName.toUpperCase();
				}
			}
			return null;
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
