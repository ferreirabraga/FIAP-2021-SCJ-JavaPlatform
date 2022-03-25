package translater;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public abstract class Translater {
    private static String DEFAULT_ANSWER = "Desculpe, não foi possível traduzir";
    private static String TRANSLATION_API_URL = "https://libretranslate.de/translate";



    /**
	 * Method used to translate English Strings to Portuguese
	 * 
	 * @param text - String Text that shoud be translated to English
	 * @return - String text translated to Portuguese
	*/
    public static String translateEnglish2Portuguese (String text) {
		JSONObject json = new JSONObject();
		json.put("q", text);
		json.put("source", "en");
		json.put("target", "pt");
		json.put("format", "text");  
		String resposta = DEFAULT_ANSWER;
            HttpEntity entity = requestAPI(TRANSLATION_API_URL, json);
			if (entity != null) {
                try {
				InputStream instream = entity.getContent();
				resposta = Utilities.convertStreamToJson(instream).getString("translatedText");
                instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }	
			}

		return resposta;
	}

    private static HttpEntity requestAPI(String url, JSONObject payload){
        HttpPost request = new HttpPost(url);
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            StringEntity params = new StringEntity(payload.toString());
            request.addHeader("Content-Type", "application/json");
			request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
            return entity;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
