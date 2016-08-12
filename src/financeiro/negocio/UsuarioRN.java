package financeiro.negocio;

import java.util.List;

import financeiro.DAO.UsuarioDAO;
import financeiro.model.Usuario;
import financeiro.util.DAOException;
import financeiro.util.RNException;

public class UsuarioRN extends RN<Usuario> {

	public UsuarioRN() {
		super(new UsuarioDAO());
	}

	@Override
	public void salvar(Usuario model) throws RNException {
		try {
		  if(model.getCodigo() == null || model.getCodigo() == 0) {
			
			// Validamos se existe um usuario com o login informado
		    Usuario usuarioExistente = buscarPorLogin(model.getLogin());
			if(usuarioExistente != null) {
				throw new RNException("Já existe um usuário com o login informado");
			}
			
			// Adicionamos a permissão básica ao usuário
			model.getPermissao().add("ROLE_USUARIO");
			
			dao.salvar(model);
			
			CategoriaRN categoriaRN = new CategoriaRN();
			categoriaRN.salvaEstruturaPadrao(model);
						
		    
		  } else {
			  //UsuarioDAO userDAO = (UsuarioDAO) dao;
			  //userDAO.atualizar(model);
			  ((UsuarioDAO) dao).atualizar(model);
			  
		  }
		} catch (DAOException e) {
			throw new RNException("Não foi possível salvar. Erro: " 
		       + e.getMessage(), e);
		}
	}
	
	
	public Usuario buscarPorLogin(String login) {
		return ((UsuarioDAO) dao).buscarPorLogin(login);
	}

	@Override
	public void excluir(Usuario model) throws RNException {
		try {
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException("Não foi possível excluir. Erro: " 
				       + e.getMessage(), e);
		}
	}

	@Override
	public Usuario obterPorId(Usuario filtro) {
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Usuario> pesquisar(Usuario filtros) {
		return dao.pesquisar(filtros);
	}
	
	public void  validarLogin(String login) throws RNException {
		// valida se existe um usuario com o login informado
		Usuario usuarioExistente = buscarPorLogin(login);
		if (usuarioExistente != null) {
			throw new RNException("Já existe um usuário com o login informado");
		}
	}

}
