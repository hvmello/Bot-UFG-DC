package comum.model.enums;

public enum EnumTipoMensagem {

    DEFAULT,
    PADRAO_RETORNO_LIGAR_192,
    ENCHENTE_INUNDACAO;

    public boolean isPadraoRetornoLigar192() {
        return this.equals(EnumTipoMensagem.PADRAO_RETORNO_LIGAR_192);
    }


}