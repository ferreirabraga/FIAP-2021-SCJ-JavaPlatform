package enums;

public enum OpcoesEnum {

	//https://nomics.com/docs/#section/SDKs-and-Libraries/Community-Submissions-(Not-supported-by-Nomics)
    CRIPTO ("CRIPTO"),
    CHUCH ("CHUCH"),
    CLIMA ("CLIMA ");
	
    OpcoesEnum (String opcao) {
        this.opcao = opcao;
    }
    
    public String opcao;
        

    public String getOpcao() {
        return opcao;
    }

}

      
 
    
