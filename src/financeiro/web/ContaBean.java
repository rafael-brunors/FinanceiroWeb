package financeiro.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import financeiro.model.Conta;
import financeiro.negocio.ContaRN;
import financeiro.util.RNException;

@ManagedBean
@RequestScoped
public class ContaBean extends ActionBean<Conta> {
	
	public ContaBean() {
		super(new ContaRN());
	}
	
	private Conta selecionada = new Conta();
	private List<Conta> lista = null;
	
	public void salvar() {
	  try { 
    	selecionada.setUsuario(obterUsuarioLogado());
    	rn.salvar(selecionada);
    	selecionada = new Conta();
    	lista = null;
    	apresentarMensagemDeSucesso("Conta incluída com sucesso");
	} catch (RNException e) {
		apresentarMensagemDeErro(e);
	     }
	}
	
	public void excluir(){
		try {
			rn.excluir(selecionada);
			selecionada = new Conta();
			lista = null;
			apresentarMensagemDeErro("Conta excluida com sucesso");
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}

	public List<Conta> getLista() {
		if(lista == null) {
			lista = rn.pesquisar(new Conta(obterUsuarioLogado()));
		}
		return lista;
	}
	public void tornarFavorita() { 
	  try {
		((ContaRN) rn).tornarFavorita(selecionada);
		selecionada = new Conta();
	  } catch (RNException e) {
		  apresentarMensagemDeErro(e);
	  }
	}
	
	public Conta getSelecionada() {
		return selecionada;
	}

	public void setSelecionada(Conta selecionada) {
		this.selecionada = selecionada;
	}


	public void setLista(List<Conta> lista) {
		this.lista = lista;
	}
}
