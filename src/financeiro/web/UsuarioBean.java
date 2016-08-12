package financeiro.web;

import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import financeiro.model.Usuario;
import financeiro.negocio.UsuarioRN;
import financeiro.util.RNException;


@ManagedBean
@RequestScoped
public class UsuarioBean extends ActionBean<Usuario> {

	private Usuario usuario = new Usuario();
	private String confirmaSenha;
	private String destinoSalvar;
	private List<Usuario> lista;

	public UsuarioBean() {
		super(new UsuarioRN());
	}
	
	public void validarLogin() {
		try {
			((UsuarioRN) rn).validarLogin(usuario.getLogin());
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}
	
	
	public String novo() {
		usuario = new Usuario();
		usuario.setAtivo(true);
		destinoSalvar = "usuarioSucesso";
		return "usuario";
	}

	public String editar() {
		confirmaSenha = usuario.getSenha();
		return "/publico/usuario";
	}
	
	public String salvar() {

		try {
			if (!usuario.getSenha().equals(confirmaSenha)) {
				apresentarMensagemDeErro("Senhas diferentes");
				apresentarMensagemDeSucesso("Usuário incluído com sucesso");
				return "usuario";
			}

			rn.salvar(usuario);
			
			return destinoSalvar;
			
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
		
		return null;
	}
	
	public String excluir(){
		try {
			rn.excluir(usuario);
			lista = null;
			apresentarMensagemDeErro("Usuário excluído com sucesso");
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
		return null;
	}
	
	public String ativar(){
//		try {
//			if(usuario.isAtivo()){
//				usuario.setAtivo(false);
//			} else {
//				usuario.setAtivo(true);
//			}
//			
//			UsuarioRN urn = new UsuarioRN();
//			urn.salvar(usuario);
//		} catch (RNException e) {
//			apresentarMensagemDeErro(e);
//		}
		
		usuario.setAtivo(!usuario.isAtivo());
		
		
		return null;
	}

	public String atribuiPermissao(Usuario usuario, String permissao) {

		this.usuario = usuario;

		Set<String> permissoes = this.usuario.getPermissao();

		if (permissoes.contains(permissao)) {
			permissoes.remove(permissao);
		} else {
			permissoes.add(permissao);
		}
		return null;
	}

	public List<Usuario> getLista() {
		if(lista == null){
			lista = rn.pesquisar(null);
		}
		
		return lista;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public void setLista(List<Usuario> lista) {
		this.lista = lista;
	}

}
