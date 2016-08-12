package financeiro.web;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import financeiro.model.Conta;
import financeiro.model.Usuario;
import financeiro.negocio.IRN;
import financeiro.util.RNException;


/**
 * Pai de todas as actions. Possibilita a criação de métodos
 * genéricos a serem utilizados pelas filhas
 * 
 * @param <T> - Entidade principal a ser manipulada pela action
 */
public abstract class ActionBean<T> {
	
	/**
	 * Representara a instância da classe de Negócio do programa. 
	 * Mais detalhes em: {@link IRN}
	 */
	protected final IRN<T> rn;
	
	public ActionBean(IRN<T> rn) {
		super();
		this.rn = rn;
	}

	/**
	 * Injetamos o contexto bean para podermos utilizar-lo.
	 * Substitui a classe ContextoUtil da pagina 273
	 */
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
	/**
	 * Método para apresentar uma mensagem de erro
	 * @param e - {@link RNException} Contem a mensagem de erro
	 */
	public void apresentarMensagem(String msg, Severity type){
		FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(type, msg,""));
	}
	
	/**
	 * Método para apresentar uma mensagem de erro
	 * @param e - {@link RNException} Contem a mensagem de erro
	 */
	public void apresentarMensagemDeSucesso(String msg){
		apresentarMensagem(msg, FacesMessage.SEVERITY_INFO);
	}

	/**
	 * Método para apresentar uma mensagem de erro
	 * @param e - {@link RNException} Contem a mensagem de erro
	 */
	public void apresentarMensagemDeErro(RNException e){
		apresentarMensagemDeErro(e.getMessage());
	}
	
	/**
	 * Método para apresentar uma mensagem de erro
	 * @param e - Contem a mensagem de erro
	 */
	public void apresentarMensagemDeErro(String msg){
		apresentarMensagem(msg, FacesMessage.SEVERITY_ERROR);
	}
	
	/**
	 * Retorna o usuário logado ao sistema
	 * @return usuário logado
	 */
	public Usuario obterUsuarioLogado(){
		return contextoBean.getUsuarioLogado();
	}
	
	/**
	 * Retorna a conta que esta selecionada para a sessão do usuário
	 * @return conta ativa
	 */
	public Conta obterContaAtiva(){
		return contextoBean.getContaAtiva();
	}
	
	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}
}
