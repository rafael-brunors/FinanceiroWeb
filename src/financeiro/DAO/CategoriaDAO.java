package financeiro.DAO;

import java.util.List;

import org.hibernate.Query;

import financeiro.model.Categoria;
import financeiro.util.DAOException;

public class CategoriaDAO extends DAO<Categoria> {

	@Override
	public void salvar(Categoria model) throws DAOException {
	}
	
	public Categoria salvarCategoria(Categoria model) throws DAOException {
		Categoria merged = (Categoria) this.sessao.merge(model);
		this.sessao.flush();
		this.sessao.clear();
		return merged;
	}

	@Override
	public void excluir(Categoria model) throws DAOException {
		model = obterPorId(model);
		sessao.delete(model);		
		sessao.flush();
		sessao.clear();
	}

	@Override
	public Categoria obterPorId(Categoria filtro) {
		return (Categoria) sessao.get(Categoria.class, filtro.getCodigo());
	}

	@Override
	public List<Categoria> pesquisar(Categoria filtros) {
		String hql = "select c from "+Categoria.class.getSimpleName()+" c where "
				+ " c."+Categoria.Fields.PAI+" is null and c."+ Categoria.Fields.USUARIO + " = :usuario";
		
		Query consulta = sessao.createQuery(hql);
		consulta.setInteger("usuario", filtros.getUsuario().getCodigo());
		
		List<Categoria> lista = consulta.list();
		
		return lista;
	}


}
