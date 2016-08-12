package financeiro.DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import financeiro.model.Conta;
import financeiro.model.Usuario;
import financeiro.util.DAOException;

public class ContaDAO extends DAO<Conta> {
	
	
	//Herda a sessao da DAO
	@Override
	public void salvar(Conta model) throws DAOException {
		sessao.saveOrUpdate(model);
	}

	@Override
	public void excluir(Conta model) throws DAOException {
		sessao.delete(model);
	}

	@Override
	public Conta obterPorId(Conta filtro) {
		return (Conta) sessao.get(Conta.class, filtro.getCodigo());
	}

	@Override
	public List<Conta> pesquisar(Conta filtros) {
		Criteria criteria = sessao.createCriteria(Conta.class);
		criteria.add(Restrictions.eq(Conta.Fields.USUARIO.toString(), filtros.getUsuario()));
		return criteria.list();
	}
	
	public Conta buscarFavorita(Usuario usuario){
		Criteria criteria = sessao.createCriteria(Conta.class);
		
		criteria.add(Restrictions.eq(Conta.Fields.USUARIO.toString(), usuario));
		criteria.add(Restrictions.eq(Conta.Fields.FAVORITA.toString(), true));
		
		return (Conta) criteria.uniqueResult();
	}
		
//		public static void main(String[] args) {
//		String sql = "select * from "+Conta.class.getDeclaredAnnotation(Table.class).name() +
//				 	  " where "+Conta.Fields.DAT_CADASTRO.name()+"= :datCadastro";
//		System.out.println(sql);
//	}

}
