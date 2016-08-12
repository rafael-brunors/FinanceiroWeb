package financeiro.negocio;

import java.util.Date;
import java.util.List;

import financeiro.DAO.LancamentoDAO;
import financeiro.domain.SituacaoCheque;
import financeiro.model.Cheque;
import financeiro.model.ChequeId;
import financeiro.model.Conta;
import financeiro.model.Lancamento;
import financeiro.util.DAOException;
import financeiro.util.RNException;



public class LancamentoRN extends RN<Lancamento> {

	public LancamentoRN() {
		super(new LancamentoDAO());
	}

	@Override
	public void salvar(Lancamento model) throws RNException {
		
	}
	
	public void salvar(Lancamento model, Integer numeroCheque) throws RNException {
		try {
			if (model.getCategoria().getPai() == null) {
				throw new RNException("Categoria inv�lida");
			}
				
				// O lancamento pode possuir um cheque
				if (numeroCheque != null) {
					ChequeRN chequeRN = new ChequeRN();
					ChequeId chequeId = new ChequeId(numeroCheque, model.getConta().getCodigo());

					Cheque chequeAux = new Cheque();
					chequeAux.setId(chequeId);	
					Cheque cheque = chequeRN.obterPorId(chequeAux);

					// Antes de baixar o cheque, valida-se se o mesmo existe
					// e n�o esteja cancelado...
					if (cheque == null) {
						throw new RNException("Cheque n�o cadastrado");
					} else if (SituacaoCheque.C.equals(cheque.getSituacao())) {
						throw new RNException("Cheque j� cancelado");
					} // Realizado teste para n�o permitir que cheques sejam reutilizados (LGA)
					  // else if (SituacaoCheque.B.equals(cheque.getSituacao())) {
					  //	throw new RNException("Cheque j� utilizado em outro lan�amento");
					 else {
						model.setCheque(cheque);
						try {
							// baixar o cheque, ou seja, alterar sua situa��o para Baixado
							chequeRN.baixarCheque(chequeId, model);
						} catch (RNException e) {
							throw new RNException("Erro ao baixar cheque: " + e.getMessage());
						}
					}
				}

			dao.salvar(model);
		} catch (DAOException e) {
			throw new RNException("N�o foi possivel salvar. Erro: " + e.getMessage(), e);
		}
	}

	@Override
	public void excluir(Lancamento model) throws RNException {
		try {
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException("N�o foi possivel excluir. Erro: " + e.getMessage(), e);
		}
	}

	@Override
	public Lancamento obterPorId(Lancamento filtro) {
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Lancamento> pesquisar(Lancamento filtros) {
		return null;
	}

	public float saldo(Conta conta, Date data) {
		float saldoInicial = conta.getSaldoInicial();
		float saldoNaData = ((LancamentoDAO) dao).saldo(conta, data);
		return saldoInicial + saldoNaData;
	}

	public List<Lancamento> pesquisarLancamento(Conta conta, Date dataInicio, Date dataFim) {
		return ((LancamentoDAO) dao).pesquisarLancamento(conta, dataInicio, dataFim);
	}

}
