package telegram_bot;

public enum Opcoes {

    START (0){
        @Override
        public boolean isStart() {
            return true;
        }
    },
    OPCOES (1){
        @Override
        public boolean isOpcoes() {
            return true;
        }
    },
    SAIR (0){
        @Override
        public boolean isSair() {
            return true;
        }
    };
    Opcoes (int opcao) {
        this.opcao = opcao;
    }
    
    public int opcao;
        
    public boolean isStart() {return false;}

    public boolean isOpcoes() {return false;}

    public boolean isSair(){return false;}

    public int getOpcao() {
        return opcao;
    }

}

      
 
    
