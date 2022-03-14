package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import enums.PropertiesEnum;

public class Propriedades {

	private static final String NOME_ARQUIVO = "token.properties";
	
	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
		   fis = new FileInputStream(fileName);
		   prop = new Properties();
		   prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			System.err.println("Arquivo não encontrado.");
		   fnfe.printStackTrace();
		} catch(IOException ioe) {
		   ioe.printStackTrace();
		} finally {
		   fis.close();
		}
		return prop;
	 }

	public static Properties getTokens() {
		Properties prop = null;
				
		try{
			prop = readPropertiesFile(NOME_ARQUIVO);
		}catch(IOException io){
			System.err.println("Algo deu errado.");
			System.err.println(io.getMessage());
		}

		return prop;
	}
	
	public static String getValue(PropertiesEnum propEnum) {
		
		Properties prop = null;		
		try{
			prop = readPropertiesFile(NOME_ARQUIVO);
		}catch(IOException io){
			System.err.println("Algo deu errado.");
			System.err.println(io.getMessage());
		}

		return prop.getProperty(propEnum.name());
	}
}
