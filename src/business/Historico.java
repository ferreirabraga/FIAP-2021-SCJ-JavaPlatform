package business;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Properties;

import enums.SOEnum;
import okhttp3.internal.Util;

public class Historico {
	
	Properties prop = new Properties();
	private final String  EXTENSAO = ".properties";
	
	public Historico(Object username) {
		try{
			prop = readPropertiesFile(username.toString());
		}catch (FileNotFoundException e) {
			criadoArquivo(username.toString());
		}
		catch(IOException io){
			System.err.println("Algo deu errado.");
			System.err.println(io.getMessage());
			
		}
	}
	
	private void criadoArquivo(String nomeArquivo) {
		
		String outputPath = SOEnum.valueOf(util.Util.getSO()).getSOPath()+nomeArquivo+EXTENSAO;
	    FileOutputStream outputStrem;
		try {
			outputStrem = new FileOutputStream(outputPath);
			prop.store(outputStrem, "Esse arquivo é referente a conversa "+nomeArquivo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Não foi possível criar o histórico da conversa "+nomeArquivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Não foi possível criar o histórico da conversa "+nomeArquivo);
		}
		
	}

	public Properties readPropertiesFile(String fileName) throws FileNotFoundException  {
		FileInputStream fis = null;
		Properties prop = null;
		try {
		   fis = new FileInputStream(SOEnum.valueOf(util.Util.getSO()).getSOPath()+fileName+EXTENSAO);
		   prop = new Properties();
		   prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			System.err.println("Arquivo não encontrado.");
		   criadoArquivo(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			if(null != fis)
				try {
					fis.close();
				} catch (IOException e) {
					System.err.println("Falou o comando fis.close()");
					e.printStackTrace();
				}
		}
		return prop;
	 }
	
	 

	public void setHistorico(String textoEnviadoPeloUsuario) {
		this.prop.put(LocalDate.now().toString(), textoEnviadoPeloUsuario);
	}
	
	public String getProperty(String propertiesName) {
		return prop.getProperty(propertiesName);
	}
	
//	public void save(Object idChat) {
	public void save(Properties prop) {
		try (OutputStream output = new FileOutputStream( SOEnum.valueOf(util.Util.getSO()).getSOPath()+prop.getProperty("id")+EXTENSAO)) {

//            Properties prop = new Properties();
//            
//            // set the properties value
//            prop.setProperty("db.url", "localhost");
//            prop.setProperty("db.user", "mkyong");
//            prop.setProperty("db.password", "password");

            // save properties to project root folder
            prop.store(output, null);

//            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
	}
	 
	
}
