package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import enums.MethodEnum;

public class API {

	/**
	 * Metodo usado para requisitar APIs externas
	 * 
	 * @param url
	 * @param metodo
	 * @param dados  - Ex: {\"userId\": 100,\"id\": 100,\"title\": \"main
	 *               title\",\"body\": \"main body\"}"
	 * @return - O retorno provavelmente é um JSON mas é retornado em forma de
	 *         String para ser tratato pelo metodo de origem tratar
	 * @throws Exception
	 */
    protected String callAPIExternas(String url, MethodEnum metodo, String dados) throws Exception {
//      String[] details = {};
//      System.out.println(Arrays.toString(details));
//      String payload = null;
//      
//      URL line_api_url = new URL(url);
//      
//      if(null != dados)
//      	payload = dados;
//
//      HttpURLConnection linec = (HttpURLConnection) line_api_url.openConnection();
//      linec.setDoInput(true);
//      linec.setDoOutput(true);
//      linec.setRequestMethod(metodo.name());
//      linec.setRequestProperty("Content-Type", "application/json");
////      linec.setRequestProperty("Authorization", "Bearer "+ "1djCb/mXV+KtryMxr6i1bXw");
//
//      //envia parametros somente se foi passado no métodos
//      if(null !=payload) {
//      	OutputStreamWriter writer = new OutputStreamWriter(linec.getOutputStream(), "UTF-8");
//      	writer.write(payload);
//      }
//      //pega os dados 
//      BufferedReader in = new BufferedReader(new InputStreamReader(linec.getInputStream()));
//      //cria objeto
//      StringBuffer inputLineSB = new StringBuffer();
//      String inputLine="";
//      //carrega todos os dados
//      while ((inputLine = in.readLine()) != null) {
//      	System.out.println(in.readLine());
//      	inputLineSB.append(in.readLine());
//      }
//
//      in.close();
//      //retorna tudo com String
//      return inputLineSB.toString();
		System.setProperty("http.agent", "Mozilla");
  	URL url1 = new URL(url);
		url1.openConnection().addRequestProperty("User-Agent", "Mozilla");
		url1.openConnection().setReadTimeout(5000);
		url1.openConnection().setConnectTimeout(5000);
      try (BufferedReader br = new BufferedReader(new InputStreamReader(url1.openStream()))) {

          String line;

          StringBuilder sb = new StringBuilder();

          while ((line = br.readLine()) != null) {

              sb.append(line);
              sb.append(System.lineSeparator());
          }
          System.out.println(sb);
          return sb.toString();
      }

//      System.out.println(response.body());
  } 

//	public String doRequestPost(String aRota, String dados) throws HttpException, IOException {
//		HttpClient httpClient = new HttpClient();
////            PostMethod httpPost = new PostMethod("http://%s:%s/"+aRota);
//		PostMethod httpPost = new PostMethod(aRota);
//		System.out.println(dados);
//		httpPost.addParameter("nome", "valor");
//		httpClient.executeMethod((HttpMethod) httpPost);
//		return httpPost.getResponseBodyAsString();
//	}

//	public String doRequestGet(String url) throws IOException, InterruptedException  {
//		
//		OkHttpClient client = new OkHttpClient();
//
//		Request request = new Request.Builder()
//		  .url(url)
//		  .get()
//		  .addHeader("cache-control", "no-cache")
//		  .build();
//		TimeUnit.SECONDS.sleep(3);
//		Response response = client.newCall(request).execute();
//		return response.toString();
//
//}

	}
