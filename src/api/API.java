package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import enums.MethodEnum;

public class API {
	
	
	/**
	 * Metodo usado para requisitar APIs externas
	 * @param url
	 * @param metodo
	 * @param dados - Ex: {\"userId\": 100,\"id\": 100,\"title\": \"main title\",\"body\": \"main body\"}"
	 * @return - O retorno provavelmente é um JSON mas é retornado em forma de String para ser tratato pelo 
	 * metodo de origem tratar
	 * @throws Exception
	 */
    public String callAPIExternas(String url, MethodEnum metodo, String dados) throws Exception {
        String[] details = {};
        System.out.println(Arrays.toString(details));
        String payload = null;
        
        URL line_api_url = new URL(url);
        
        if(null != dados)
        	payload = dados;

        HttpURLConnection linec = (HttpURLConnection) line_api_url.openConnection();
        linec.setDoInput(true);
        linec.setDoOutput(true);
        linec.setRequestMethod(metodo.name());
        linec.setRequestProperty("Content-Type", "application/json");
//        linec.setRequestProperty("Authorization", "Bearer "+ "1djCb/mXV+KtryMxr6i1bXw");

        //envia parametros somente se foi passado no métodos
        if(null !=payload) {
        	OutputStreamWriter writer = new OutputStreamWriter(linec.getOutputStream(), "UTF-8");
        	writer.write(payload);
        }
        //pega os dados 
        BufferedReader in = new BufferedReader(new InputStreamReader(linec.getInputStream()));
        //cria objeto
        StringBuffer inputLineSB = new StringBuffer();
        String inputLine="";
        //carrega todos os dados
        while ((inputLine = in.readLine()) != null)
        	inputLineSB.append(in.readLine());

        in.close();
        //retorna tudo com String
        return inputLineSB.toString();
    } 
}
