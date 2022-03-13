package telegram_bot;

import java.io.IOException;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import business.Historico;
import business.Resposta;
import enums.OpcoesEnum;

public class TratarRespostas implements Resposta{
        
    private String resposta;
    private String ultimaInteracao;
    private Object chatID;
    private String textoEnviadoPeloUsuario;
    private SendMessage sendMessageResposta;
    Historico historico = null;
    boolean isNome = false;

	public void setSendMessageResposta(SendMessage sendMessageResposta) {
        this.sendMessageResposta = sendMessageResposta;
    }

    public SendMessage getsendMessageResposta(){
        return this.sendMessageResposta;
    }

    public TratarRespostas(String textoEnviadoPeloUsuario, Object chatID){
        this.chatID = chatID;
        this.textoEnviadoPeloUsuario = textoEnviadoPeloUsuario;
       	historico = new Historico(chatID);
       	historico.setHistorico(textoEnviadoPeloUsuario);
    }

    public String getResposta() {
        return null==this.resposta?"":this.resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    @Override
    public void escuta(String ultimaInteracao) {
        this.ultimaInteracao = ultimaInteracao;
        trataTextoRecebido(this.textoEnviadoPeloUsuario);
    }

    private void trataTextoRecebido(String texto) {

        boolean isComando = true;
        
        
        if(texto.indexOf("/") == -1){
            isComando = false;
        }
        if(isComando){
            // String opcoes = "";
            switch (texto) {
                case "/start":
                    setResposta("Olá como vai, tudo bem? Como se chama?");
                    break;
            
                default:
                    break;
            }
        }else{
            tratarRespostaTextoAberto(texto);
        }

        ultimaInteracao = texto;


    }

    private void tratarRespostaTextoAberto(String texto) {
        if(ultimaInteracao != null){
            switch (ultimaInteracao) {
                case "/start":
                    setResposta("Olá "+texto+", temos muitas opção de serviços. Escolha um:");
                    addInteracaoInicial();
                    break;
                case "resp1":
                    setResposta("Olá você escolheu a opção "+texto+", temos muitas opção de serviços. Escolha um:");
                    addInteracaoInicial();
                    break;
                default:
					try {
						historico.readPropertiesFile(chatID.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                setResposta("Olá como vai tudo bem? Para iniciarmos nossa conversa me enviando a opção  /start ou seu nome. ");
            }
        }else{
            InlineKeyboardMarkup inlineKeyboardMarkup =  new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        // new InlineKeyboardButton("Opção 1").url("www.google.com"),
                        new InlineKeyboardButton("chuck Norris").callbackData("chucknorris"),
                        new InlineKeyboardButton("Conselhos").callbackData("adviceslip"),
                        new InlineKeyboardButton("Opção 3").callbackData("resp3")
                        // new InlineKeyboardButton("Opção 3").switchInlineQuery("switch_inline_query")
                });
            sendMessageResposta = montaResposta(this.chatID, texto, inlineKeyboardMarkup);
        }
    }

    @Override
    public String responde() {
        return getResposta();
    }

    public SendMessage montaResposta(Object chat_id, String texto, InlineKeyboardMarkup inlineKeyboardMarkup){
        return new SendMessage(chat_id, texto).replyMarkup(inlineKeyboardMarkup);
    }

    public void escutaCallBack(String ultimaInteracao) {
        this.ultimaInteracao = ultimaInteracao;
        trataTextoRecebido(this.textoEnviadoPeloUsuario);
    }
    
    private void addInteracaoInicial(){
        InlineKeyboardMarkup inlineKeyboardMarkup =  new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        // new InlineKeyboardButton("Opção 1").url("www.google.com"),
                        new InlineKeyboardButton("Preços de Criptomoedas").callbackData(OpcoesEnum.NOMICS.getOpcao()),
                        new InlineKeyboardButton("Chuch Norris").callbackData(OpcoesEnum.CHUCH.getOpcao()),
                        new InlineKeyboardButton("Opção 3").callbackData("resp3")
                        // new InlineKeyboardButton("Opção 3").switchInlineQuery("switch_inline_query")
                });
       setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));
    }
}
