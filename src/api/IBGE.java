package api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import enums.MethodEnum;

public class IBGE extends API{

	public final String URL_NOME = "https://servicodados.ibge.gov.br/api/v2/censos/nomes/";
	private String nome;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String isNomeComum(String nome) {

		try {
			String json = callAPIExternas(URL_NOME+nome, MethodEnum.POST, null);

			//TODO: Ajustar codigo para pegar transformar em Object
//			Gson gson = new Gson();
//			String json = gson.toJson(obj);
			//gato para acelerar o desenvolvimento
			String[] dadosJson  =json.split(",");
			for (String dado : dadosJson) {
				if(dado.contains("\"nome\"")) {
					System.out.println(dado);
					return dado.substring(dado.indexOf(":")+2,dado.length()-1);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
