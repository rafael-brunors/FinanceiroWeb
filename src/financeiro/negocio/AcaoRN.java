package financeiro.negocio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import financeiro.DAO.AcaoDAO;
import financeiro.model.Acao;
import financeiro.model.AcaoVirtual;
import financeiro.model.Usuario;
import financeiro.util.DAOException;
import financeiro.util.RNException;
import financeiro.util.YahooFinanceUtil;

public class AcaoRN extends RN<Acao>{

	public AcaoRN() {
		super(new AcaoDAO());
	}

	@Override
	public void salvar(Acao model) throws RNException {
		
		try {
			dao.salvar(model);
		} catch (DAOException e) {
			throw new RNException(e);
		}
	}

	@Override
	public void excluir(Acao model) throws RNException {
		
		try {
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException(e);
		}
	}

	@Override
	public Acao obterPorId(Acao filtro) {
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Acao> pesquisar(Acao filtros) {
		return dao.pesquisar(filtros);
	}
	
	public List<AcaoVirtual> listarAcaoVirtual(Usuario usuario) throws RNException {
		
		List<Acao> listaAcao = null;
		List<AcaoVirtual> listaAcaoVirtual = new ArrayList<AcaoVirtual>();
		AcaoVirtual acaoVirtual = null;
		String cotacao = null;
		
		float ultimoPreco = 0.0f;
		float total = 0.0f;
		int quantidade = 0;
		try {
			// d�vida corrigida
			listaAcao = pesquisar(new Acao(usuario));
			for(Acao acao : listaAcao) {
				acaoVirtual = new AcaoVirtual();
				acaoVirtual.setAcao(acao);
				cotacao = retornaCotacao(YahooFinanceUtil.ULTIMO_PRECO_DIA_ACAO_INDICE, acao);
				if (cotacao != null) {
					ultimoPreco = new Float(cotacao).floatValue();
					quantidade = acao.getQuantidade();
					total = ultimoPreco * quantidade;
					acaoVirtual.setUltimoPreco(ultimoPreco);
					acaoVirtual.setTotal(total);
					listaAcaoVirtual.add(acaoVirtual);
				}
			}
		} catch (RNException e) {
			throw new RNException("N�o foi poss�vel listar a��es. Erro: " + e.getMessage());
		}
		return listaAcaoVirtual;
	}
	
	public String retornaCotacao(int indiceInformacao, Acao acao) throws RNException {
		YahooFinanceUtil yahooFinanceUtil = null;
		String informacao = null;
		try {
			yahooFinanceUtil = new YahooFinanceUtil(acao);
			informacao = yahooFinanceUtil.retornaCotacao(indiceInformacao, acao.getSigla());
		} catch (IOException e) {
			throw new RNException("N�o foi poss�vel recuperar cota��o. Erro: " + e.getMessage());
		}
		return informacao;
	}
	
	public String montaLinkAcao(Acao acao) {
		String link = null;
		if (acao != null) {
			if (acao.getOrigem() != null) {
				if (acao.getOrigem() == YahooFinanceUtil.ORIGEM_BOVESPA) {
					link = acao.getSigla() + YahooFinanceUtil.POSFIXO_ACAO_BOVESPA;
				} else {
					link = acao.getSigla();
				}
			} else {
				link = YahooFinanceUtil.INDICE_BOVESPA;
			}
		} else {
			link = YahooFinanceUtil.INDICE_BOVESPA;
		}
		return link;
	}
}
