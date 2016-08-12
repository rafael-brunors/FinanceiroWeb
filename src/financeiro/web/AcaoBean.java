package financeiro.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import financeiro.model.Acao;
import financeiro.model.AcaoVirtual;
import financeiro.negocio.AcaoRN;
import financeiro.util.RNException;
import financeiro.util.YahooFinanceUtil;

@ManagedBean
@RequestScoped
public class AcaoBean extends ActionBean<Acao> {

	private AcaoVirtual selecionada = new AcaoVirtual();
	private List<AcaoVirtual> lista = null;
	private String linkCodigoAcao   = null;
	
	public AcaoBean() {
		super(new AcaoRN());
	}
	
	public void salvar() {
		try {
			Acao acao = selecionada.getAcao();
			acao.setSigla(acao.getSigla().toUpperCase());
			acao.setUsuario(obterUsuarioLogado());
			rn.salvar(acao);
			selecionada = new AcaoVirtual();
			lista = null;
			apresentarMensagemDeSucesso("Ação incluída com sucesso");
		}
		catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}
	
	public void excluir() { 
		try {
			rn.excluir(selecionada.getAcao());
			selecionada = new AcaoVirtual();
			lista=null;
			apresentarMensagemDeErro("Categoria excluída com sucesso");
		} catch (RNException e) {
			apresentarMensagemDeErro(e); 
		}
	}
	
	public List<AcaoVirtual> getLista() { 
		
		
		try { 
			if (lista == null) {
				lista = ((AcaoRN) rn).listarAcaoVirtual(obterUsuarioLogado());
			}
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
		return lista;
	}
	
	public String getLinkCodigoAcao() { 
		if (selecionada != null) {
			linkCodigoAcao = ((AcaoRN) rn).montaLinkAcao(selecionada.getAcao());
		} else {
			linkCodigoAcao = YahooFinanceUtil.INDICE_BOVESPA;
		}
		return linkCodigoAcao;
	}

	public AcaoVirtual getSelecionada() {
		return selecionada;
	}

	public void setSelecionada(AcaoVirtual selecionada) {
		this.selecionada = selecionada;
	}

	public void setLista(List<AcaoVirtual> lista) {
		this.lista = lista;
	}

	public void setLinkCodigoAcao(String linkCodigoAcao) {
		this.linkCodigoAcao = linkCodigoAcao;
	}

}
