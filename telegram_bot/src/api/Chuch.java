package api;

import java.io.IOException;

import enums.MethodEnum;
import enums.PropertiesEnum;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.Propriedades;

public class Chuch extends API{

	public final String URL = "https://api.chucknorris.io/jokes/random";
	
	/**
	 * Metodo usado para recuperar alguma frase aleatoria sobre o Chuck Norris
	 * API : https://api.chucknorris.io/
	 * @return textoChuch
	 * {
		"icon_url" : "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
		"id" : "S99dqRfRTGK4RuvQMH4Wwg",
		"url" : "",
		"value" : "Wanna know what Bill Murray whispered into Scarlett Johanson's ear at the end of Lost In Translation? "I don't love you, I love Chuck Norris.""
	 } *
	 */
	public String callAPI() {
		
		try {
			String json = callAPIExternas(URL, MethodEnum.GET, null);
			//gato para acelerar o desenvolvimento
			String[] dadosJson  =json.split(",");
			for (String dado : dadosJson) {
				if(dado.contains("\"value\"")) {
					System.out.println(dado);
					String textoChuch = dado.replace("\"value\":", "").replace("\"", "");
					//TODO:adicionar uma API de tradução
//					String traducao = traducao(textoChuch);
//					if(traducao.trim().length() > 0) {
						return textoChuch;
//					}else {
//						return textoChuch;
//					}
				}
			}
			return "";


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private String traducao(String texto) {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType, "q="+texto.replace(" ", "%20"));
		Request request = new Request.Builder()
			.url("https://google-translate1.p.rapidapi.com/language/translate/v2/detect")
			.post(body)
			.addHeader("content-type", "application/x-www-form-urlencoded")
			.addHeader("accept-encoding", "application/gzip")
			.addHeader("x-rapidapi-host", "google-translate1.p.rapidapi.com")
			.addHeader("x-rapidapi-key", Propriedades.getValue(PropertiesEnum.RAPID))
			.build();

		Response response;
		try {
			response = client.newCall(request).execute();
			return response.message();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
		
		
	}
	
	
}
