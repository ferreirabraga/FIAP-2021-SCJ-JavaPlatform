package api;

import java.lang.reflect.Method;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import enums.MethodEnum;
import modelos.InfoNomes;

public class IBGE extends API{

	private final String URL_NOME = "https://servicodados.ibge.gov.br/api/v2/censos/nomes/";
	private String nome;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public boolean isNomeComum(String nome) {
		String resposta;
		try {
			resposta = callAPIExternas(URL_NOME, MethodEnum.POST, nome);
			String json = "";
			//TODO:
			//fazer o tratamento de String para json
			return resposta.length() > 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
