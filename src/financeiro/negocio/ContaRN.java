package financeiro.negocio;

import java.util.Date;
import java.util.List;

import financeiro.DAO.ContaDAO;
import financeiro.model.Conta;
import financeiro.model.Usuario;
import financeiro.util.DAOException;
import financeiro.util.RNException;

public class ContaRN extends RN<Conta> {

	public ContaRN() {
		super(new ContaDAO());
		
	}

	@Override
	public void salvar(Conta model) throws RNException {
	  try {
		  model.setDataCadastro(new Date());
		  dao.salvar(model);
	  } catch (DAOException e) {
		  throw new RNException("Não foi possível salvar. Erro.: "+e.getMessage(), e);
	  }
	}
	
	public void tornarFavorita(Conta conta) throws RNException {
		Conta contaFavorita = buscarFavorita(conta.getUsuario());
		
		if(contaFavorita != null) {
			contaFavorita.setFavorita(false);
			salvar(contaFavorita);
		}
		
		conta.setFavorita(true);
		salvar(conta);
	}
	
	public Conta buscarFavorita(Usuario usuario) {
		ContaDAO cDAO = (ContaDAO) dao;
		return cDAO.buscarFavorita(usuario);
	}

	@Override
	public void excluir(Conta model) throws RNException {
		try {
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException("Não foi possível excluir. Erro.: "+e.getMessage(), e);
		}
	}

	@Override
	public Conta obterPorId(Conta filtro) {
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Conta> pesquisar(Conta filtros) {
		return dao.pesquisar(filtros);
	}

}
