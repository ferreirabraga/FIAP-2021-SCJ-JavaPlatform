package business;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Set;

import enums.SOEnum;

public class Historico {
	
	Properties prop = new Properties();
	private final String  EXTENSAO = ".properties";
	
	public Historico(Object idChat) {
		try{
			prop = readPropertiesFile(idChat.toString());
		}catch (FileNotFoundException e) {
			criadoArquivo(idChat.toString());
		}
		catch(IOException io){
			System.err.println("Algo deu errado.");
			System.err.println(io.getMessage());
			
		}
	}
	
	private void criadoArquivo(String nomeArquivo) {
		
		String outputPath = SOEnum.valueOf(getSO()).getSOPath()+nomeArquivo+EXTENSAO;
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

	public Properties readPropertiesFile(String fileName) throws FileNotFoundException,IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
		   fis = new FileInputStream(SOEnum.valueOf(getSO()).getSOPath()+fileName+EXTENSAO);
		   prop = new Properties();
		   prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			System.err.println("Arquivo não encontrado.");
		   throw fnfe;
		}  finally {
			if(null != fis)
				fis.close();
		}
		return prop;
	 }
	
	 private static String getSO() {
		 Properties prop = System.getProperties();
			Set<Object> keySet = prop.keySet();
			for (Object obj : keySet) {
				if(obj.toString().equals("os.name")) {
					if(System.getProperty(obj.toString()).toUpperCase().contains("MAC")) {
						return "MAC";
					}
					return System.getProperty(obj.toString()).toUpperCase();
				}
			}
			return null;
	 }

	public void setHistorico(String textoEnviadoPeloUsuario) {
		this.prop.put(LocalDate.now().toString(), textoEnviadoPeloUsuario);
	}
	
	public String getProperty(String propertiesName) {
		return prop.getProperty(propertiesName);
	}
	
//	public void save(Object idChat) {
	public void save(Properties prop) {
		try (OutputStream output = new FileOutputStream( SOEnum.valueOf(getSO()).getSOPath()+prop.getProperty("id")+EXTENSAO)) {

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
