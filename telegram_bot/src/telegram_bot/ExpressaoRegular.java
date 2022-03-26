package telegram_bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressaoRegular {

	
		static boolean validaExpressao (String expressao, String texto ) {
		boolean matchFound = false;
							
		Pattern pattern = Pattern.compile(expressao, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(texto);
	    matchFound = matcher.find();
	    	    
	    return matchFound;
	}
	
		static String validaExpressao (String texto ) {
			String resposta = "";
			
			String bomDia = "[b]{1}[o]{1}[ma]{1}[\\s]{1}[dtn]{1}[iao]{1}[ari]{1}";//[dtn][iao][ari][td][e]
			String ola = "[o]{1}[li]{1}";
			String adeus =  "[tao]{1}[ctb]{1}[her]{1}[\\\\sai]{1}[umlg]{1}";
			String start = "start";
			
			if (ExpressaoRegular.validaExpressao( bomDia, texto)) {
				return resposta  = "Bom dia";
				}  else if (ExpressaoRegular.validaExpressao( ola , texto)) {
					return resposta  = "Ola";
					} else if (ExpressaoRegular.validaExpressao( adeus , texto)) {
						return resposta  = "Adeus";
						} else if (ExpressaoRegular.validaExpressao( start , texto)) {
							System.out.println("expressao regular "+ExpressaoRegular.validaExpressao( start , texto));
							return resposta  = "Start";
							}    
			
			return resposta = "Sem resultado";
			
		}
		
		
}


