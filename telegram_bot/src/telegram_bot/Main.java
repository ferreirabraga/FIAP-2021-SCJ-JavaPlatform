package telegram_bot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class Main {

	//armazena o ultimo texto enviado pelo usuario
	public static String  ultimaInteracao = "";

	
	public static void main(String[] args) {

		Properties prop = null;

		try{
			prop = readPropertiesFile("token.properties");
		}catch(IOException io){
			System.err.println("Algo deu errado.");
			System.err.println(io.getMessage());
		}

		// Criacao do objeto bot com as informacoes de acesso.
		TelegramBot bot = new TelegramBot(prop.getProperty("myToken"));

		// Objeto responsavel por receber as mensagens.
		GetUpdatesResponse updatesResponse;

		// Objeto responsavel por gerenciar o envio de respostas.
		SendResponse sendResponse;

		// Objeto responsavel por gerenciar o envio de acoes do chat.
		BaseResponse baseResponse;

		// Controle de off-set, isto e, a partir deste ID sera lido as mensagens
		// pendentes na fila.
		int m = 0;	

		//nome do usuario
		String nome;

		// Loop infinito pode ser alterado por algum timer de intervalo curto.
		while (true) {
			// Executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial).
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// Lista de mensagens.
			List<Update> updates = updatesResponse.updates();
			TratarRespostas tratarRespostas = null;
			
			// Analise de cada acao da mensagem.
			for (Update update : updates) {

				// Atualizacao do off-set.
				m = update.updateId() + 1;
				if(null != update.message()){

					//contrutor com o texto enviado pelo usuario e o id da mensagem
					tratarRespostas = new TratarRespostas(update.message().text(), update.message().chat().id());
					//log
					System.out.println("Recebendo mensagem: " + update.message().text());
					
					//Ja tem a informação carregada, verifica o que foi a ultima interação
					tratarRespostas.escuta(ultimaInteracao);
					//a interação atual será a ultima na proxima interação
					ultimaInteracao = update.message().text();
					
					// Envio de "Escrevendo" antes de enviar a resposta.
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

					//retorna uma resposta para o chat
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), tratarRespostas.getResposta()));

				}else{

					// Envio de "Escrevendo" antes de enviar a resposta.
					baseResponse = bot.execute(new SendChatAction(update.callbackQuery().message().chat().id(), ChatAction.typing.name()));

					//contrutor com o texto enviado pelo usuario e o id da mensagem
					tratarRespostas = new TratarRespostas(update.callbackQuery().data(), update.callbackQuery().message().chat().id());
					//
					tratarRespostas.escutaCallBack(ultimaInteracao);
										
					//a interação atual será a ultima na proxima interação
					ultimaInteracao = update.callbackQuery().data();

					//retorna uma resposta para o chat
					sendResponse = bot.execute(tratarRespostas.getsendMessageResposta());
				}

				// Verificacao de acao de chat foi enviada com sucesso.
				System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());
				
			}
		}
	}

	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
		   fis = new FileInputStream(fileName);
		   prop = new Properties();
		   prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			System.err.println("Arquivo não encontrado.");
		   fnfe.printStackTrace();
		} catch(IOException ioe) {
		   ioe.printStackTrace();
		} finally {
		   fis.close();
		}
		return prop;
	 }
}
