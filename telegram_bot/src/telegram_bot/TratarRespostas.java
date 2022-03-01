package telegram_bot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

public class TratarRespostas implements Resposta{
        
    String resposta;
    String ultimaInteracao;
    Object chatID;
    String textoEnviadoPeloUsuario;
    SendMessage sendMessageResposta;

    public void setSendMessageResposta(SendMessage sendMessageResposta) {
        this.sendMessageResposta = sendMessageResposta;
    }

    public SendMessage getsendMessageResposta(){
        return this.sendMessageResposta;
    }

    public TratarRespostas(String textoEnviadoPeloUsuario, Object chatID){
        this.chatID = chatID;
        this.textoEnviadoPeloUsuario = textoEnviadoPeloUsuario;
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
                default:
                    break;
            }
        }else{
            InlineKeyboardMarkup inlineKeyboardMarkup =  new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        // new InlineKeyboardButton("Opção 1").url("www.google.com"),
                        new InlineKeyboardButton("Opção 1").callbackData("resp1"),
                        new InlineKeyboardButton("Opção 2").callbackData("resp2"),
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
                        new InlineKeyboardButton("Opção 1").callbackData("resp1"),
                        new InlineKeyboardButton("Opção 2").callbackData("resp2"),
                        new InlineKeyboardButton("Opção 3").callbackData("resp3")
                        // new InlineKeyboardButton("Opção 3").switchInlineQuery("switch_inline_query")
                });
       setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));
    }
}
