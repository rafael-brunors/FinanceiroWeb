package financeiro.DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import financeiro.model.Acao;
import financeiro.model.Cheque;
import financeiro.util.DAOException;

public class AcaoDAO extends DAO<Acao> {

	@Override
	public void salvar(Acao model) throws DAOException {
		sessao.saveOrUpdate(model);
	}

	@Override
	public void excluir(Acao model) throws DAOException {
		sessao.delete(model);
	}

	@Override
	public Acao obterPorId(Acao filtro) {
		return (Acao) sessao.get(Acao.class, filtro.getCodigo());
	}

	@Override
	public List<Acao> pesquisar(Acao filtros) {
		Criteria criteria = sessao.createCriteria(Acao.class);
		criteria.add(Restrictions.eq("usuario", filtros.getUsuario()));
		return criteria.list();
	}

}
