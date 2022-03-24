package api;

import com.google.gson.Gson;

import enums.MethodEnum;
import enums.PropertiesEnum;
import modelos.OpenWeather;
import modelos.Weather;
import util.Propriedades;

public class OpenWeatherAPI extends API{
	//https://openweathermap.org/current
	private final String URL= "https://api.openweathermap.org/data/2.5/weather?q=%s&lang=pt_br&appid=%s&units=metric";
	String token= null;
	
	public OpenWeatherAPI() {
		token = Propriedades.getValue(PropertiesEnum.OPEN_WEATHER);
	}
	
	public OpenWeather callAPI(String cidade) {
		
		try {
			String json = callAPIExternas(String.format(URL, cidade, token), MethodEnum.GET, null);
			Gson gson = new Gson(); // Or use new GsonBuilder().create();
			OpenWeather weather = gson.fromJson(json, OpenWeather.class);
			
			//gato para acelerar o desenvolvimento
//			String[] dadosJson  =json.split(",");
//			for (String dado : dadosJson) {
//				if(dado.contains("\"value\"")) {
//					System.out.println(dado);
//					String textoChuch = dado.replace("\"value\":", "").replace("\"", "");
//					//TODO:adicionar uma API de tradução
////					String traducao = traducao(textoChuch);
////					if(traducao.trim().length() > 0) {
//						return textoChuch;
////					}else {
////						return textoChuch;
////					}
//				}
//			}
			return weather;


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
