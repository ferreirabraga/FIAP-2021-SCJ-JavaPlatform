package telegram_bot;


import java.io.FileNotFoundException;
import java.io.IOException;
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
import business.Historico;
import business.Resposta;
import enums.OpcoesEnum;
import modelos.OpenWeather;
import modelos.Weather;
import translater.Translater;
import util.Constantes;

public class TratarRespostas implements Resposta {

    private String resposta;
    private String ultimaInteracao;
    private Object chatID;
    private String textoEnviadoPeloUsuario;
    private SendMessage sendMessageResposta;
    Historico historico = null;
    boolean isNome = false;
    String username = null;

    public void setSendMessageResposta(SendMessage sendMessageResposta) {
        this.sendMessageResposta = sendMessageResposta;
    }

    public SendMessage getsendMessageResposta() {
        return this.sendMessageResposta;
    }


    public String getPeriodo() {
        LocalTime time = LocalTime.now();
        if (time.getHour() < 12) {
            return "bom dia";
        } else if (time.getHour() >= 12 && time.getHour() < 18) {
            return "boa tarde";
        } else {
            return "boa noite";
        }
    }


    public TratarRespostas(String textoEnviadoPeloUsuario, Object chatID, String username) {
        this.username = username;
        this.chatID = chatID;
        this.textoEnviadoPeloUsuario = textoEnviadoPeloUsuario;
        historico = new Historico(username);
        historico.setHistorico(textoEnviadoPeloUsuario);
        getPeriodo();
    }

    public String getResposta() {
        return null == this.resposta ? "" : this.resposta;
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
        String confereExpressao = ExpressaoRegular.validaExpressao(texto);
        if (texto.indexOf("/") == -1) {
            isComando = false;
        }
        if (isComando) {

            // String opcoes = "";criadoArquivo
            switch (texto) {
                case "/start":
                    Historico propHistorico = new Historico(this.username);
                    if (null != propHistorico.getProperty("nome")) {
                        setResposta(String.format("Ol� %s, " + getPeriodo() + " como vai, tudo bem? Por onde come�aremos por hoje? Selecione abaixo a op��o:", propHistorico.getProperty("nome")));
                        addInteracaoInicial();
                    } else {
                        setResposta("Ol� " + getPeriodo() + " como vai, tudo bem? Como se chama?");
                    }

                    break;

                default:
                    setResposta(String.format("A /op��o informada foi inv�lida, favor digitar /start"));
                    break;
            }
        } else {
            tratarRespostaTextoAberto(texto, username);
        }

        ultimaInteracao = texto;


    }

    private void tratarRespostaTextoAberto(String texto, String username) {

        String nomeJaCadastrado = "";
        String confereExpressao = ExpressaoRegular.validaExpressao(texto);
        Properties propHistorico = null;


        try {
            propHistorico = historico.readPropertiesFile(username);
            if (null == propHistorico.getProperty("id")) {
                if (username == null) {
                    propHistorico.setProperty("id", "Jane Doe");
                } else {
                    propHistorico.setProperty("id", username.toString());
                }
            }
            if (null != propHistorico.getProperty("nome")) {
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
                setResposta("Ol� " + nomeJaCadastrado + ", temos muitas op��o de servi�os. Escolha um:");
                addInteracaoInicial();
                break;
            case "nomics":
                setResposta(String.format("Ol� voc� escolheu a op��o %s, temos muitas op��o de servi�os. Escolha um:", texto));
                addInteracaoInicial();
                break;
            case "CRIPTO":
                addInteracaoCripto();
                break;
            case "CHUCK":
                addInteracaoChuch();
                break;
            case "OPEN_WEATHER":
                addInteracaoWeather(propHistorico);
                break;
            case "SIM_CRIPTO":
                addTerceiraInteracaoCripto();
                break;
            case "SIM_OPEN_WEATHER":
                if (ultimaInteracao.equals(OpcoesEnum.OPEN_WEATHER.name())) {
                    OpenWeatherAPI weather = new OpenWeatherAPI();
                    //texto seria o nome da cidade
                    OpenWeather ow = weather.callAPI(propHistorico.getProperty("cidade"));
                    Double d = Double.parseDouble(ow.getMain().getTemp());
                    int temperatura = d.intValue();
                    List<Weather> weatherList = ow.getWeather();
                    setResposta(String.format("O tempo de %s é %s e a temperatura de %s", propHistorico.getProperty("cidade"), weatherList.get(0).getDescription(), temperatura + "°C\n"
                            + medeTemperatura(ow.getMain().getTemp()))
                            + "\nDeseja ver o clima de outra cidade?");
                    addSegundaInteracaoWeather();
                } else {
                    setResposta("Digite o nome da cidade desejada?");
                }
            case "NAO_OPEN_WEATHER":
                setResposta("Digite o nome da cidade desejada?");
                break;
            case "MENU":
                addReinteracaoInicio();
                break;
            default:
                if (ultimaInteracao.equals(OpcoesEnum.CRIPTO.getOpcao()) || ultimaInteracao.equals(OpcoesEnum.SIM_CRIPTO.getOpcao())) {

                    this.addSegundaInteracaoCripto(texto);

                } else if (ultimaInteracao.toUpperCase().equals(OpcoesEnum.CHUCK.getOpcao()) && texto.toUpperCase().equals(OpcoesEnum.CHUCK.getOpcao())) {
                    addSegundaInteracaoChuck();

                } else if (ultimaInteracao.toUpperCase().equals(OpcoesEnum.OPEN_WEATHER.getOpcao()) ||
                        ultimaInteracao.toUpperCase().equals(OpcoesEnum.SIM_OPEN_WEATHER.getOpcao()) ||
                        ultimaInteracao.toUpperCase().equals(OpcoesEnum.NAO_OPEN_WEATHER.getOpcao())) {
                    OpenWeatherAPI weather = new OpenWeatherAPI();
                    //texto seria o nome da cidade
                    OpenWeather ow = weather.callAPI(texto);

                    if (null == ow) {
                        setResposta("Essa cidade eu n�o conhe�o, voc� digitou o nome corretamente, acredito que n�o. Vamos tentar novamente? \nEntre com o nome da cidade desejada");
                        ultimaInteracao = OpcoesEnum.OPEN_WEATHER.getOpcao();
                    } else {

                        propHistorico.setProperty("cidade", texto);

                        List<Weather> weatherList = ow.getWeather();
                        setResposta(String.format("O tempo de %s é %s e a temperatura de %s", texto, weatherList.get(0).getDescription(), ow.getMain().getTemp() + "°C\n"
                                + medeTemperatura(ow.getMain().getTemp()))
                                + "\nDeseja ver o clima de outra cidade?");
                        addSegundaInteracaoWeather();

                    }
                } else {
                    //List<String> despedidas = new ArrayList<String>();
                    //despedidas.add("TCHAU");
                    //despedidas.add("AT� MAIS");
                    //despedidas.add("AT� LOGO");
                    //despedidas.add("OBRIGADO");

                    //for (String despedida : despedidas) {
                    //if(despedida.equals(texto.toUpperCase())) {
                    //		setResposta(String.format(texto+" %s", propHistorico.getProperty("nome")));
                    //		break;
                    if (confereExpressao == "Adeus") {
                        setResposta(String.format(texto + "! %s", propHistorico.getProperty("nome")));
                        break;


                    }
                }
                if (getResposta().trim().length() == 0) {
                    try {
                        if ((nomeJaCadastrado.trim().length() > 0) && ((propHistorico.getProperty("nome") == null))) {
                            switch (confereExpressao) {
                                case "Bom dia":
                                    setResposta("Desejo a voc� " + getPeriodo() + ". Por favor, escolha uma das op��es: " + nomeJaCadastrado);
                                    addInteracaoInicial();
                                    break;
                                case "Ola":
                                    setResposta("Ol�.  Por favor, escolha uma das op��es: " + nomeJaCadastrado);
                                    addInteracaoInicial();
                                    break;
                                case "Adeus":
                                    setResposta("Mas j�, " + nomeJaCadastrado + " ? Fica, tem caf�. Escolha uma das op��es:  ");
                                    addInteracaoInicial();
                                    break;
                                default:
                                    setResposta("Bem vindo, " + nomeJaCadastrado + ". N�o entendi o que voc� quis dizer. Para iniciarmos nossa conversa me enviando a op��o /start ou seu nome. ");
                                    addInteracaoInicial();
                                    break;

                            }

                        } else {
                            ;
                            IBGE ibge = new IBGE();
                            String isNomeComum = ibge.isNomeComum(texto);
                            if (!isNomeComum.contains("[]") && !isNomeComum.equals("")) {
                                propHistorico.setProperty("nome", texto);
                                setResposta(String.format("Ol� %s, " + getPeriodo() + " como vai tudo bem? Escolha uma das op��es abaixo:", texto));
                                addInteracaoInicial();
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (getResposta().length() == 0) {
                        System.out.println("expressao: " + confereExpressao);

                        switch (confereExpressao) {
                            case "Bom dia":
                                setResposta(getPeriodo() + " como vai tudo bem? Para iniciarmos nossa conversa me enviando a op��o  /start ou seu nome. ");
                                break;
                            case "Ola":
                                setResposta("Ol�. Para iniciarmos nossa conversa me enviando a op��o /start ou seu nome. ");
                                break;
                            case "Adeus":
                                setResposta("Mas j�? Fica, tem caf�. Mas para tomar o caf�, tem que digitar a op��o /start ou seu nome. ");
                                break;
                            case "Start":
                                setResposta("Voc� quase acertou. � s� digitar /start para come�armos");
                                break;
                            default:
                                setResposta("Joinha? Para iniciarmos nossa conversa me enviando a op��o  /start ou seu nome Belezinha?. ");

                                break;
                        }

                    }
                }
        }
        historico.save(propHistorico);

    }

    private String medeTemperatura(String temp) {
        Double d = Double.parseDouble(temp);
        int temperatura = d.intValue();
        if (temperatura < 10) {
            return "Eita friaaaca. S� de processar meu componentes j� est�o congelando";
        } else if (temperatura >= 10 && temperatura < 20) {
            return "� aconselh�vel um casaco, o vento certamente est� gelaaaado.";
        } else if (temperatura >= 20 && temperatura < 25) {
            return "Huuum, tempinho para apreciar um bom vinho";
        } else if (temperatura >= 25 && temperatura < 30) {
            return "Oportunidade de fazer um exercício ao ar livre.";
        } else if (temperatura >= 30 && temperatura < 35) {
            return "Opa, hoje vai dar praia,mas se n�o tiver praia rola uma �gua de coco no parque.";
        } else {
            return "Muito cuidado com o sol e n�o esque�a de se hidratar";
        }

    }

    private void addReinteracaoInicio() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        // new InlineKeyboardButton("Op��o 1").url("www.google.com"),
                        new InlineKeyboardButton(Constantes.CRIPTO).callbackData(OpcoesEnum.CRIPTO.getOpcao()),
                        new InlineKeyboardButton(Constantes.CHUCK).callbackData(OpcoesEnum.CHUCK.getOpcao()),
                        new InlineKeyboardButton(Constantes.OPEN_WEATHER).callbackData(OpcoesEnum.OPEN_WEATHER.getOpcao())
                        // new InlineKeyboardButton("Op��o 3").switchInlineQuery("switch_inline_query")
                });
        sendMessageResposta = montaResposta(this.chatID, "Ok, vamos l�, deseja selecionar qual op��o agora?", inlineKeyboardMarkup);


    }

    private void addInteracaoWeather(Properties historico) {

        String cidade = historico.getProperty("cidade");
        if (null != cidade && cidade.trim().length() > 0) {

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton(Constantes.SIM).callbackData(OpcoesEnum.SIM_OPEN_WEATHER.getOpcao()),
                            new InlineKeyboardButton(Constantes.NAO).callbackData(OpcoesEnum.NAO_OPEN_WEATHER.getOpcao())
                    });

            setResposta("A �ltima cidade verifica foi a " + cidade + " deseja consult�-la novamente?");

            setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));


        } else {
            setResposta("Entre com o nome da sua cidade para receber informa��es do clima. \nSe desejar volta ao menu clique aqui nesse /start ");
        }

    }

    private void addSegundaInteracaoWeather() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(Constantes.SIM).callbackData(OpcoesEnum.SIM_OPEN_WEATHER.getOpcao()),
                        new InlineKeyboardButton(Constantes.MENU).callbackData(OpcoesEnum.MENU.getOpcao())
                });

        setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));

    }

    private String buscaPrincipaisCripto() {
        Cripto cripto = new Cripto();
        List<String> moedas = new ArrayList();
        moedas.add("BTC");
        String precos = cripto.getPrecoAgora(moedas);
        return precos;
    }


//
//	private void primeirasOpcoes(String texto) {
//		InlineKeyboardMarkup inlineKeyboardMarkup =  new InlineKeyboardMarkup(
//		    new InlineKeyboardButton[]{
//		            // new InlineKeyboardButton("Op��o 1").url("www.google.com"),
//		            new InlineKeyboardButton("chuck Norris").callbackData("chucknorris"),
//		            new InlineKeyboardButton("Conselhos").callbackData("adviceslip"),
//		            new InlineKeyboardButton("Op��o 3").callbackData("resp3")
//		            // new InlineKeyboardButton("Op��o 3").switchInlineQuery("switch_inline_query")
//		    });
//		sendMessageResposta = montaResposta(this.chatID, texto, inlineKeyboardMarkup);
//	}

    @Override
    public String responde() {
        return getResposta();
    }

    public SendMessage montaResposta(Object chat_id, String texto, InlineKeyboardMarkup inlineKeyboardMarkup) {
        return new SendMessage(chat_id, texto).replyMarkup(inlineKeyboardMarkup);
    }

    public void escutaCallBack(String ultimaInteracao) {
        this.ultimaInteracao = ultimaInteracao;
        trataTextoRecebido(this.textoEnviadoPeloUsuario);
    }

    private void addInteracaoInicial() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        // new InlineKeyboardButton("Op��o 1").url("www.google.com"),
                        new InlineKeyboardButton(Constantes.CRIPTO).callbackData(OpcoesEnum.CRIPTO.getOpcao()),
                        new InlineKeyboardButton(Constantes.CHUCK).callbackData(OpcoesEnum.CHUCK.getOpcao()),
                        new InlineKeyboardButton(Constantes.OPEN_WEATHER).callbackData(OpcoesEnum.OPEN_WEATHER.getOpcao())
                        // new InlineKeyboardButton("Op��o 3").switchInlineQuery("switch_inline_query")
                });
        setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));
    }

    /**
     * Utilizado para primeira intera��o com o usuario ap�s clicar na op��o CRIPTO
     */
    private void addInteracaoCripto() {
        String preco = buscaPrincipaisCripto();
        setResposta("Digite a sigla da moeda que deseja ver o pre�o, Ex: BTC para Bitcoin, ETH para Etherium etc. \nSegue �ltima cota��o do BTC/BRL: " + preco + "\nSe desejar volta sair digite /start ");
    }

    private void addSegundaInteracaoCripto(String texto) {

        List<String> moedas = new ArrayList<String>() {
            {
                add(texto.toUpperCase());
            }
        };

        Cripto cripto = new Cripto();
        setResposta(String.format("O cota��o da cripto %s hoje é de %s .\nSe desejar outra cota��o clique em SIM se quiser sair clique na op��o MENU", texto, cripto.getPrecoAgora(moedas)));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(Constantes.SIM).callbackData(OpcoesEnum.SIM_CRIPTO.getOpcao()),
                        new InlineKeyboardButton(Constantes.MENU).callbackData(OpcoesEnum.MENU.getOpcao())
                });

        setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));

    }

    private void addTerceiraInteracaoCripto() {
        setResposta("Digite a sigla da cripto desejada.");
    }

    /**
     * Trata nome composto para verifica��o no IBGE
     *
     * @param nome
     * @return
     */
    private String trataNomeComposto(String nome) {

        //trata nome composto
        if (nome.indexOf(" ") > 0) {
            //caso tenham mais de 1 nome pega somente o primeiro
            return (nome.split(" "))[0];
        }
        return nome;

    }

    /**
     * Utilizado para primeira intera��o com o usuario ap�s clicar na op��o Chuck Norris
     */
    private void addInteracaoChuch() {

        Chuch chuch = new Chuch();
        setResposta("Nessa op��o voc� saber� fatos sobre o Chuck Norris. Veja um exemplo:\n" + Translater.translateEnglish2Portuguese(chuch.callAPI())
                + "\nEu adoro essas mensagens, toda vez que eu respondo à voc�s eu dou tanta risada que meus circuitos doem. hahahaha. Muito bom. Desejar rir mais um pouco? ");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(Constantes.SIM).callbackData(OpcoesEnum.CHUCK.getOpcao().toLowerCase()),
                        new InlineKeyboardButton(Constantes.MENU).callbackData(OpcoesEnum.MENU.getOpcao())
                });
        setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));

    }

    private void addSegundaInteracaoChuck() {

        Chuch chuch = new Chuch();
        setResposta(Translater.translateEnglish2Portuguese(chuch.callAPI()) + "\nHummm, essa foi mais ou menos. hehehehehe. Mais uma risada?");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(Constantes.SIM).callbackData(OpcoesEnum.CHUCK.getOpcao().toLowerCase()),
                        new InlineKeyboardButton(Constantes.MENU).callbackData(OpcoesEnum.MENU.getOpcao())
                });

        setSendMessageResposta(montaResposta(this.chatID, getResposta(), inlineKeyboardMarkup));

    }

    private void addTerceiraInteracaoChuch() {
        Chuch chuch = new Chuch();
        setResposta("Vamos dar mais risadas. Ai vai!\n " + Translater.translateEnglish2Portuguese(chuch.callAPI()));

    }

}
