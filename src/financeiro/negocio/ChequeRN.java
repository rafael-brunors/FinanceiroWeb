package financeiro.negocio;

import java.util.Date;
import java.util.List;

import financeiro.DAO.ChequeDAO;
import financeiro.domain.SituacaoCheque;
import financeiro.model.Cheque;
import financeiro.model.ChequeId;
import financeiro.model.Conta;
import financeiro.model.Lancamento;
import financeiro.util.DAOException;
import financeiro.util.RNException;



/**
 * Classe que efetuara as regras de negocio de cheque
 */
public class ChequeRN extends RN<Cheque> {
	
	public ChequeRN() {
		super(new ChequeDAO());
	}

	@Override
	public void salvar(Cheque cheque) throws RNException {
		
		try {
			dao.salvar(cheque);
		} catch (DAOException e) {
			throw new RNException(e);
		}
	}

	@Override
	public void excluir(Cheque cheque) throws RNException {
		// Só permite excluir cheques que estejam com a situação
		// Não emitido
		if (SituacaoCheque.N.equals(cheque.getSituacao())) {
			try {
				dao.excluir(cheque);
			} catch (DAOException e) {
				throw new RNException(e);
			}
		} else {
			throw new RNException("Não é possível excluir cheque, status não permitido para operação.");
		}
	}
	

	/**
	 * Método que salva uma sequencia de cheques (inicial e final)
	 */
	public int salvarSequencia(Conta conta, Integer chequeInicial, Integer chequeFinal) throws RNException {
		
		Cheque cheque = null;
		Cheque chequeVerifica = null;
		ChequeId chequeId = null;
		int contaTotal = 0;
		
		// Loop para salvar cheques de: chequeInicial até: chequeFinal
		for (int i = chequeInicial; i <= chequeFinal; i++) {
			chequeId = new ChequeId(i, conta.getCodigo());
			
			chequeVerifica = obterPorId(new Cheque(chequeId));
			
			//Só insere caso o cheque não existe
			if (chequeVerifica == null) {
				cheque = new Cheque();
				cheque.setId(chequeId);
				// Situação inicial: Não emitido
				cheque.setSituacao(SituacaoCheque.N);
				cheque.setDataCadastro(new Date(System.currentTimeMillis()));
				salvar(cheque);
				// Váriavel que vai informar ao final do processo
				// quantos cheques realmente foram inseridos,
				// eliminando-se os duplicados.
				contaTotal++;
			}
		}
		
		return contaTotal;
	}

	@Override
	public Cheque obterPorId(Cheque cheque) {
		return dao.obterPorId(cheque);
	}

	@Override
	public List<Cheque> pesquisar(Cheque filtros) {
		return dao.pesquisar(filtros);
	}
	
	public void cancelarCheque(Cheque cheque) throws RNException {
		
		// Só é possível cancelar cheques que não tenham sido emitidos
		if (SituacaoCheque.N.equals(cheque.getSituacao())) {
			cheque.setSituacao(SituacaoCheque.C);
			try {
				dao.salvar(cheque);
			} catch (DAOException e) {
				throw new RNException(e);
			}
		} else {
			throw new RNException("Não é possível cancelar cheque, status não permitido para operação.");
		}
	}
	
	/**
	 * Método chamado ao se UTILIZAR um cheque para um lançamento
	 */
	public void baixarCheque(ChequeId chequeId, Lancamento lancamento) throws RNException {
		Cheque chequeAux = new Cheque();
		chequeAux.setId(chequeId);
		Cheque cheque = null;
		cheque = obterPorId(chequeAux);
		
		if (cheque != null) {
			// Baixar o cheque == alterar a situacao de Não Emitido para 
			// Baixado
			cheque.setSituacao(SituacaoCheque.B);
			cheque.setLancamento(lancamento);
			try {
				dao.salvar(cheque);
			} catch (DAOException e) {
				throw new RNException(e);
			}
		}
	}
	
	/**
	 * AO se alterar um lancamento e tirar o cheque
	 * deve-se passar o cheque para não emitido
	 */
	public void desvinculaLancamento(Conta conta, Integer numeroCheque) throws RNException {
		ChequeId chequeId = new ChequeId(numeroCheque, conta.getCodigo());
		
		Cheque chequeAux = new Cheque(chequeId);
		Cheque cheque = null;
		cheque = obterPorId(chequeAux);
		
		if (SituacaoCheque.C.equals(cheque.getSituacao())) {
			throw new RNException("Não é possível usar cheque cancelado.");
		} else {
			cheque.setSituacao(SituacaoCheque.N);
			cheque.setLancamento(null);
			try {
				dao.salvar(cheque);
			} catch (DAOException e) {
				throw new RNException(e);
			}
		}
	}

}
