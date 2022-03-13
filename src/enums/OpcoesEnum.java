package enums;

public enum OpcoesEnum {

	//https://nomics.com/docs/#section/SDKs-and-Libraries/Community-Submissions-(Not-supported-by-Nomics)
    NOMICS ("nomics"){
        @Override
        public boolean nomics() {
            return true;
        }
    },
    CHUCH ("ChuchNorris"){
        @Override
        public boolean isOpcoes() {
            return true;
        }
    },
    SAIR ("sair"){
        @Override
        public boolean isSair() {
            return true;
        }
    };
    OpcoesEnum (String opcao) {
        this.opcao = opcao;
    }
    
    public String opcao;
        
    public boolean nomics() {return false;}

    public boolean isOpcoes() {return false;}

    public boolean isSair(){return false;}

    public String getOpcao() {
        return opcao;
    }

}

      
 
    
