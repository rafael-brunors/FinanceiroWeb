package financeiro.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import financeiro.model.Cheque;
import financeiro.negocio.ChequeRN;
import financeiro.util.MensagemUtil;
import financeiro.util.RNException;


@ManagedBean
@RequestScoped
public class ChequeBean extends ActionBean<Cheque> {
	
	private Cheque selecionado = new Cheque();
	private List<Cheque> lista = null;
	private Integer chequeInicial;
	private Integer chequeFinal;
	
	public ChequeBean() {
		super(new ChequeRN());
	}
	
	public void salvar() {
		
		int totalCheques = 0;
		if (chequeInicial == null || chequeFinal == null) {
			apresentarMensagemDeErro(MensagemUtil.getMensagem("cheque_erro_sequencia"));
			
		} else if (chequeFinal.intValue() < chequeInicial.intValue()) {
			apresentarMensagemDeErro(MensagemUtil.getMensagem("cheque_erro_inicial_final", chequeInicial, chequeFinal));
			
		} else {
			try {
				// Vai ao banco salvar sequencia de cheques
				totalCheques = ((ChequeRN) rn).salvarSequencia(obterContaAtiva(), chequeInicial, chequeFinal);
			} catch (RNException e) {
				apresentarMensagemDeErro("Erro ao salvar cheque: " + e.getMessage());
				return;
			}
			
			// Mensagem de sucesso
			apresentarMensagemDeSucesso(MensagemUtil.getMensagem("cheque_info_cadastro", chequeFinal, totalCheques,120));
			lista = null;
		}
	}
	
	public void excluir() {
		try {
			rn.excluir(selecionado);
		} catch (RNException e) {
			apresentarMensagemDeErro(MensagemUtil.getMensagem("cheque_erro_excluir"));
			return;
		}
		
		lista = null;
	}
	
	public void cancelar() {
		try {
			((ChequeRN) rn).cancelarCheque(selecionado);
		} catch (RNException e) {
			apresentarMensagemDeErro(MensagemUtil.getMensagem("cheque_erro_cancelar"));
			return;
		}
		
		lista = null;
	}

	public List<Cheque> getLista() {
		if (lista == null) {
			lista = rn.pesquisar(new Cheque(obterContaAtiva()));
		}		
		
		return lista;
	}


	public Cheque getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Cheque selecionado) {
		this.selecionado = selecionado;
	}
	
	public void setLista(List<Cheque> lista) {
		this.lista = lista;
	}

	public Integer getChequeInicial() {
		return chequeInicial;
	}

	public void setChequeInicial(Integer chequeInicial) {
		this.chequeInicial = chequeInicial;
	}

	public Integer getChequeFinal() {
		return chequeFinal;
	}

	public void setChequeFinal(Integer chequeFinal) {
		this.chequeFinal = chequeFinal;
	}
}