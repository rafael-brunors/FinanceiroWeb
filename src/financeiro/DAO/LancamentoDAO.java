package financeiro.DAO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import financeiro.model.Conta;
import financeiro.model.Lancamento;
import financeiro.util.DAOException;


public class LancamentoDAO extends DAO<Lancamento> {

	@Override
	public void salvar(Lancamento model) throws DAOException {
		sessao.saveOrUpdate(model);
	}

	@Override
	public void excluir(Lancamento model) throws DAOException {
		sessao.delete(model);
	}

	@Override
	public Lancamento obterPorId(Lancamento filtro) {
		return (Lancamento) sessao.get(Lancamento.class, filtro.getCodigo());
	}

	@Override
	public List<Lancamento> pesquisar(Lancamento filtros) {

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Lancamento> pesquisarLancamento(Conta conta, Date dataInicio, Date dataFim) {

		Criteria criteria = sessao.createCriteria(Lancamento.class);

		if (dataInicio != null && dataFim != null) {
			criteria.add(Restrictions.between("data", dataInicio, dataFim));
			
		} else if (dataInicio != null) {
			criteria.add(Restrictions.ge("data", dataInicio));

		} else if (dataFim != null) {
			criteria.add(Restrictions.le("data", dataFim));
		}

		criteria.add(Restrictions.eq("conta", conta));
		criteria.addOrder(Order.asc("data"));

		return criteria.list();

	}

	public float saldo(Conta conta, Date data) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(l.valor * c.fator)");
		sql.append(" from Lancamento l, Categoria c");
		sql.append(" where l.categoria = c.codigo and l.conta = :conta and l.data <= :data");
		
		SQLQuery query = sessao.createSQLQuery(sql.toString());
		query.setParameter("conta", conta.getCodigo());
		query.setParameter("data", data);
		
		BigDecimal saldo = (BigDecimal) query.uniqueResult();
		
		if (saldo != null) {
			return saldo.floatValue();
		}
		
		return 0f;
	}
}
