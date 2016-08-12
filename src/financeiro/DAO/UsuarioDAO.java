package financeiro.DAO;

import java.util.List;

import org.hibernate.Query;

import financeiro.model.Usuario;
import financeiro.util.DAOException;

public class UsuarioDAO extends DAO<Usuario> {

	@Override
	public void salvar(Usuario model) throws DAOException {
		sessao.save(model);
		
	}
	
	// não coloca o @override pois é especifico para esta classe.
	// não herda da IDAO
	public void atualizar(Usuario model) throws DAOException {
		if(model.getPermissao().isEmpty() || model.getPermissao().size() == 0) {
			Usuario usuarioDoBanco = obterPorId(model);
			model.setPermissao(usuarioDoBanco.getPermissao());
			sessao.evict(usuarioDoBanco);
		}
		sessao.update(model);
		
	}

	@Override
	public void excluir(Usuario model) throws DAOException {
		sessao.delete(model);
		
	}

	@Override
	public Usuario obterPorId(Usuario filtro) {
		return (Usuario) sessao.get(Usuario.class, filtro.getCodigo());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> pesquisar(Usuario filtros) {
		return sessao.createCriteria(Usuario.class).list();
	}
	
	/*public Usuario buscarPorLogin(String login) {
		String hql = "select u from Usuario u where u.login = :login";
		
		Query query = sessao.createQuery(hql);
		query.setString("login", login);
		
		return (Usuario) query.uniqueResult();
	}*/
	
	// Usando ENUM na classe Usuario
	public Usuario buscarPorLogin(String login){
		String hql = "select u from "+Usuario.class.getSimpleName()+" u where "
				+ " u."+Usuario.Fields.LOGIN+" = :login";

		Query query = sessao.createQuery(hql);
		query.setString("login", login);
		
		return (Usuario) query.uniqueResult();
	}

}
