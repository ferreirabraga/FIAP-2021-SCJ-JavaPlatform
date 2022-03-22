package telegram_bot;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import business.Historico;
import enums.PropertiesEnum;
import util.Propriedades;

public class Main {

	// armazena o ultimo texto enviado pelo usuario
	public static String ultimaInteracao = "";

	public static void main(String[] args) {

		// Criacao do objeto bot com as informacoes de acesso.
		TelegramBot bot = new TelegramBot(Propriedades.getValue(PropertiesEnum.TELEGRAM));

		// Objeto responsavel por receber as mensagens.
		GetUpdatesResponse updatesResponse;

		// Objeto responsavel por gerenciar o envio de respostas.
		SendResponse sendResponse;

		// Objeto responsavel por gerenciar o envio de acoes do chat.
		BaseResponse baseResponse;

		// Controle de off-set, isto e, a partir deste ID sera lido as mensagens
		// pendentes na fila.
		int m = 0;

		// Loop infinito pode ser alterado por algum timer de intervalo curto.
		while (true) {
			// Executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial).
			updatesResponse = bot.execute(new GetUpdates().limit(300).offset(m));

			// Lista de mensagens.
			List<Update> updates = updatesResponse.updates();

			if (null != updates) {
				// Analise de cada acao da mensagem.
				for (Update update : updates) {

					// Atualizacao do off-set.
					m = update.updateId() + 1;
					if (null != update.message()) {

						baseResponse = tratarRespostaAberta(bot, update);

					} else {
						baseResponse = trataRespostaOpcao(bot, update);
					}
					update = null;
					// Verificacao de acao de chat foi enviada com sucesso.
					System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());
				}
			}
		}
	}

	/**
	 * Utilizado quando uma reposta foi digitada pelo usuário
	 * 
	 * @param bot
	 * @param update
	 * @return
	 */
	private static BaseResponse tratarRespostaAberta(TelegramBot bot, Update update) {
		SendResponse sendResponse;
		BaseResponse baseResponse;
		TratarRespostas tratarRespostas;
		// contrutor com o texto enviado pelo usuario e o id da mensagem
		tratarRespostas = new TratarRespostas(update.message().text(), 
						update.message().chat().id(),update.message().chat().username());
		// log
		System.out.println("Recebendo mensagem: " + update.message().text());

		// Ja tem a informação carregada, verifica o que foi a ultima interação
		tratarRespostas.escuta(ultimaInteracao);

		// a interação atual será a ultima na proxima interação
		ultimaInteracao = update.message().text();

		// Envio de "Escrevendo" antes de enviar a resposta.
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

		if (null != tratarRespostas.getsendMessageResposta()) {
			// retorna uma resposta para o chat
			sendResponse = bot.execute(tratarRespostas.getsendMessageResposta());
		} else if (null != tratarRespostas.getResposta()) {
			// retorna uma resposta para o chat
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), tratarRespostas.getResposta()));
		} else {
			// retorna uma resposta para o chat
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "huuummm...não entendi"));
		}
		return baseResponse;
	}

	/**
	 * Utilizada quando o usuário clicou em uma opção de algum menu
	 * 
	 * @param bot
	 * @param update
	 * @return
	 */
	private static BaseResponse trataRespostaOpcao(TelegramBot bot, Update update) {
		SendResponse sendResponse;
		BaseResponse baseResponse;
		TratarRespostas tratarRespostas;
		// Envio de "Escrevendo" antes de enviar a resposta.
		baseResponse = bot
				.execute(new SendChatAction(update.callbackQuery().message().chat().id(), ChatAction.typing.name()));

		// contrutor com o texto enviado pelo usuario e o id da mensagem
		tratarRespostas = new TratarRespostas(update.callbackQuery().data(), 
				update.callbackQuery().message().chat().id(),update.callbackQuery().message().chat().username());
		//
		tratarRespostas.escutaCallBack(ultimaInteracao);

		// a interação atual será a ultima na proxima interação
		ultimaInteracao = update.callbackQuery().data();

		if (null != tratarRespostas.getsendMessageResposta()) {
			// retorna uma resposta para o chat
			sendResponse = bot.execute(tratarRespostas.getsendMessageResposta());
		} else if (null != tratarRespostas.getResposta()) {
			// retorna uma resposta para o chat
			sendResponse = bot.execute(
					new SendMessage(update.callbackQuery().message().chat().id(), tratarRespostas.getResposta()));
		} else {
			// retorna uma resposta para o chat
			sendResponse = bot
					.execute(new SendMessage(update.callbackQuery().message().chat().id(), "huuummm...não entendi"));
		}
		return baseResponse;
	}

}
