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

	/**
	 * Metodo usado para recuperar a temperatura da cidade escolhida
	 * @param cidade - cidade escolhida no chat
	 * @return weather - temperatura
	 */
	public OpenWeather callAPI(String cidade) {
		
		try {
			String json = callAPIExternas(String.format(URL, cidade, token), MethodEnum.GET, null);
			Gson gson = new Gson(); // Or use new GsonBuilder().create();
			OpenWeather weather = gson.fromJson(json, OpenWeather.class);

			return weather;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
