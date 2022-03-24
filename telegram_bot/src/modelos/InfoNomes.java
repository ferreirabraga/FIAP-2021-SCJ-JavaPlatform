package modelos;

import java.util.List;

public class InfoNomes {
	
	private String nome;
	private String sexo;
	private String localicade;
	private List<RES> res;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getLocalicade() {
		return localicade;
	}
	public void setLocalicade(String localicade) {
		this.localicade = localicade;
	}
	public List<RES> getRes() {
		return res;
	}
	public void setRes(List<RES> res) {
		this.res = res;
	}
	
	
}
