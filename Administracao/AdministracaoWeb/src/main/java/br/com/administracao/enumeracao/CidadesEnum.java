package br.com.administracao.enumeracao;

public enum CidadesEnum {
	
	PIRAJU(3, "Estância Turística de Piraju"),
	SANTA_CRUZ_DO_RIO_PARDO(4, "Municipio de Santa Cruz do Rio Pardo");
	
	private int codigo;
    private String nomePrefeitura;
        
	private CidadesEnum(int codigo, String nomePrefeitura) {
		this.codigo = codigo;
		this.nomePrefeitura = nomePrefeitura;
	}

	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNomePrefeitura() {
		return nomePrefeitura;
	}

	public void setNomePrefeitura(String nomePrefeitura) {
		this.nomePrefeitura = nomePrefeitura;
	}
    
    public static CidadesEnum getValue(int codigo) {
        for (CidadesEnum cidade : values()) {
            if (cidade.getCodigo() == codigo) {
                return cidade;
            }
        }

        return null;
    }

//    public static CidadesEnum getValue(String contexto) {
//        for (CidadesEnum cidade : values()) {
//            if (cidade.getContexto().equals(contexto)) {
//                return cidade;
//            }
//        }
//
//        return null;
//    }
    

}
