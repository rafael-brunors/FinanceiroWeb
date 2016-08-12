package financeiro.DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import financeiro.model.Cheque;
import financeiro.util.DAOException;

public class ChequeDAO extends DAO<Cheque> {
	
	@Override
	public void salvar(Cheque model) throws DAOException {
		sessao.saveOrUpdate(model);
	}

	@Override
	public void excluir(Cheque model) throws DAOException {
		sessao.delete(model);
	}
	
	@Override
	public Cheque obterPorId(Cheque filtro) {
		return (Cheque) sessao.get(Cheque.class, filtro.getId());
	}
	
	@Override
	public List<Cheque> pesquisar(Cheque filtros) {
		Criteria criteria = sessao.createCriteria(Cheque.class);
		criteria.add(Restrictions.eq("conta", filtros.getConta()));
		return criteria.list();
	}

	
}
