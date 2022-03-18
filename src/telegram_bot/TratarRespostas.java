package telegram_bot;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import api.Chuch;
import api.Cripto;
import api.IBGE;
import api.OpenWeatherAPI;
import modelos.OpenWeather;
import business.Historico;
import business.Resposta;
import enums.MethodEnum;
import enums.OpcoesEnum;
import modelos.Weather;
import util.Constantes;

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
    
    
    public String getPeriodo() {
    	LocalTime time = LocalTime.now();
    	if(time.getHour() < 12 ) {
    		return "bom dia";
    	}else if (time.getHour() >= 12 && time.getHour() < 18) {
        	return "boa tarde";
	    }else {
	    	return "boa noite";
		}
}


	public TratarRespostas(String textoEnviadoPeloUsuario, Object chatID){
        this.chatID = chatID;
        this.textoEnviadoPeloUsuario = textoEnviadoPeloUsuario;
       	historico = new Historico(chatID);
       	historico.setHistorico(textoEnviadoPeloUsuario);
       	getPeriodo();
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
                	Historico propHistorico = new Historico(this.chatID);
                	if(null != propHistorico.getProperty("nome")) {
        				setResposta(String.format("Olá %s, "+getPeriodo()+" como vai, tudo bem? Por onde começaremos por hoje? Selecione abaixo a opção:", propHistorico.getProperty("nome")));
        				addInteracaoInicial();
        			}else {
        				setResposta("Olá "+getPeriodo()+"como vai, tudo bem? Como se chama?");
        			}
                    
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
    	
    	String nomeJaCadastrado ="";
    	
		Properties propHistorico = null;
		
		try {
			propHistorico = historico.readPropertiesFile(chatID.toString());
			if(null == propHistorico.getProperty("id")) {
				propHistorico.setProperty("id",chatID.toString());
			}
			if(null != propHistorico.getProperty("nome")) {
				nomeJaCadastrado = propHistorico.getProperty("nome");
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
    	
//    	if(ultimaInteracao != null){
            switch (texto) {
                case "/start":
                    setResposta("Olá "+texto+", temos muitas opção de serviços. Escolha um:");
                    addInteracaoInicial();
                    break;
                case "nomics":
                    setResposta(String.format("Olá você escolheu a opção %s, temos muitas opção de serviços. Escolha um:", texto));
                    addInteracaoInicial();
                    break;
                case "CRIPTO":
                    addInteracaoCripto();
                    break;
                case "CHUCK":
                	addInteracaoChuch();
                    break;
                case "OPEN_WEATHER":
                	addInteracaoWeather();
                    break;
                default:
                	if(ultimaInteracao.equals(OpcoesEnum.CRIPTO.getOpcao())) {
                		Cripto cripto = new Cripto();
                		List<String> moedas = new ArrayList<String>() {
                            {
                                add(texto);
                            }
                        };
                        setResposta(String.format("O cotação da cripto %s hoje é de %s ",texto, cripto.getPrecoAgora(moedas)));
                	}else if(ultimaInteracao.toUpperCase().equals(OpcoesEnum.CHUCK.getOpcao()) && texto.toUpperCase().equals(OpcoesEnum.CHUCK.getOpcao())) {
                    	Chuch chuch = new Chuch();
                    	setResposta(chuch.callAPI());
                	}else if(ultimaInteracao.toUpperCase().equals(OpcoesEnum.OPEN_WEATHER.getOpcao())) {
                		OpenWeatherAPI weather = new OpenWeatherAPI();
                		//texto seria o nome da cidade
                		OpenWeather ow = weather.callAPI(texto);
                		if(null == ow) {
                			setResposta("Essa cidade eu não conheço, você digitou o nome corretamente?");
                		}else {
                			List<Weather> weatherList = ow.getWeather();
                			setResposta(String.format("O tempo de %s é %s e a temperatura de %s", texto, weatherList.get(0).getDescription(), ow.getMain().getTemp()+"°C"));
                			addInteracaoInicial();
                		}
                	}else {
                		List<String> despedidas = new ArrayList<String>();
                		despedidas.add("tchau");
                		despedidas.add("até mais");
                		despedidas.add("até logo");
                		despedidas.add("obrigado");
                		
                		for (String despedida : despedidas) {
							if(despedida.equals(texto)) {
								setResposta(String.format("Até mais %s", propHistorico.getProperty("nome")));
								break;
							}
						}
                		if(getResposta().trim().length() == 0) {
							try {
								if(nomeJaCadastrado.trim().length() > 0) {
									setResposta(String.format("Olá %s,"+getPeriodo()+" como vai tudo bem? Esolha uma das opções: ",nomeJaCadastrado));
									addInteracaoInicial();
								} else {
									;
									IBGE ibge = new IBGE();
									String isNomeComum = ibge.isNomeComum(texto);
									if(!isNomeComum.contains("[]") && !isNomeComum.equals("")) {
										propHistorico.setProperty("nome", texto);
										setResposta(String.format("Olá %s, "+getPeriodo()+" como vai tudo bem? Escolha uma das opções abaixo:", texto));
										addInteracaoInicial();
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(getResposta().length() == 0)
								setResposta("Olá, "+getPeriodo()+" como vai tudo bem? Para iniciarmos nossa conversa me enviando a opção  /start ou seu nome. ");
                		}
                	}
            }
            historico.save(propHistorico);
//        }else{
//            primeirasOpcoes(texto);
//        }
    }

	private void addInteracaoWeather() {
		setResposta("Entre com o nome da sua cidade para receber informações do clima. \nSe desejar sair digite /start " );
	}
	
//	private void addSegundaInteracaoWeather(String nomeCidade) {
//		OpenWeather weather = new OpenWeather();
//		weather.callAPI(nomeCidade);
//	}

	private String buscaPrincipaisCripto() {
		Cripto cripto = new Cripto();
		List<String> moedas = new ArrayList();
		moedas.add("BTC");
		String precos = cripto.getPrecoAgora(moedas);
		return precos;
	}
	
	

	private void primeirasOpcoes(String texto) {
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
                        new InlineKeyboardButton(Constantes.CRIPTO).callbackData(OpcoesEnum.CRIPTO.getOpcao()),
                        new InlineKeyboardButton(Constantes.CHUCK).callbackData(OpcoesEnum.CHUCK.getOpcao()),
                        new InlineKeyboardButton(Constantes.OPEN_WEATHER).callbackData(OpcoesEnum.OPEN_WEATHER.getOpcao())
                        // new InlineKeyboardButton("Opção 3").switchInlineQuery("switch_inline_query")
                });
       setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));
    }
    
    /**
     * Utilizado para primeira interação com o usuario após clicar na opção CRIPTO
     */
    private void addInteracaoCripto(){
    	String preco = buscaPrincipaisCripto();
    	setResposta("Digite a sigla da moeda que deseja ver o preço, Ex: BTC para Bitcoin, ETH para Etherium etc. \nSegue última cotação do BTC/BRL: "+preco+"\nSe desejar volta sair digite /start " );
    }
    
    /**
     * Trata nome composto para verificação no IBGE
     * @param nome
     * @return
     */
    private String trataNomeComposto(String nome) {

		//trata nome composto
		if(nome.indexOf(" ") > 0) {
			//caso tenham mais de 1 nome pega somente o primeiro
			return (nome.split(" "))[0];
		}
		return nome;

    }
    /**
     * Utilizado para primeira interação com o usuario após clicar na opção Chuck Norris
     */
    private void addInteracaoChuch(){
    	Chuch chuch = new Chuch();
    	setResposta("Nessa opção você saberá fatos sobre o Chuck Norris. Se sim digite 'Chuck' caso contrário digite o comando /start. \nUma degustação:"+chuch.callAPI());
    	
    }
}
