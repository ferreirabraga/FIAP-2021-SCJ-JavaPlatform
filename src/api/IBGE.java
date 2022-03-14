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
		String resposta;
		try {
			resposta = callAPIExternas(URL_NOME+nome, MethodEnum.POST, null);
			String json = "";
			JsonObject jObject  = new JsonObject(); // json
			JsonObject data = jObject.getAsJsonObject(resposta); // get data object
			JsonElement atributo = data.get("name"); // 
			return atributo.getAsString();


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
