package financeiro.negocio;

import java.util.List;

import financeiro.DAO.CategoriaDAO;
import financeiro.model.Categoria;
import financeiro.model.Usuario;
import financeiro.util.DAOException;
import financeiro.util.RNException;

public class CategoriaRN extends RN<Categoria> {

	public CategoriaRN() {
		super(new CategoriaDAO());
	}

	@Override
	public void salvar(Categoria model) throws RNException {

		Categoria pai = model.getPai();
	
		try {
			if(pai == null) {
			  String msg = "A categoria " + model.getDescricao() + " deve ter um pai definido";
			  throw new RNException(msg);
			}
			
			boolean mudouFator = pai.getFator() != model.getFator();
			
			model.setFator(pai.getFator());
			model = ((CategoriaDAO) dao).salvarCategoria(model);

			
			if (mudouFator) {
				model = obterPorId(model);
				this.replicarFator(model, model.getFator());
			}
		} catch (DAOException e) {
			 throw new RNException("Não foi possível salvar. Erro.: "+e.getMessage(), e);
		}
	}
		
	private void replicarFator(Categoria categoria, int fator) throws DAOException {
		if(categoria.getFilhos() != null) {
			for(Categoria filho : categoria.getFilhos()) {
				filho.setFator(fator);
				((CategoriaDAO) dao).salvarCategoria(filho);
				this.replicarFator(filho, fator);
			}
		}
	}

	
	@Override
	public void excluir(Categoria model) throws RNException {
		try {
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException("Não foi possível excluir. Erro.: "+e.getMessage(), e);
		}
	}
	
	public void excluir(Usuario usuario) throws RNException {
		List<Categoria> lista = pesquisar(new Categoria(usuario));
		for (Categoria categoria : lista) {
			excluir(categoria);
		}
	}
	

	@Override
	public Categoria obterPorId(Categoria filtro) {
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Categoria> pesquisar(Categoria filtros) {
		return dao.pesquisar(filtros);
	}
	
	public void salvaEstruturaPadrao(Usuario usuario) throws DAOException {
		Categoria despesas = new Categoria(null, usuario, "DESPESAS", -1);
		despesas = ((CategoriaDAO) dao).salvarCategoria(despesas);
		
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Moradia", -1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Alimentação", -1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Vestuário", -1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Deslocamento", -1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Cuidados Pessoais", -1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Educação", -1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Saúde", -1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Lazer", -1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(despesas, usuario, "Despesas Financeiras", -1));
		
		Categoria receitas = new Categoria(null, usuario, "RECEITAS", 1);
		receitas = ((CategoriaDAO) dao).salvarCategoria(receitas);
		((CategoriaDAO) dao).salvarCategoria(new Categoria(receitas, usuario, "Salário", 1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(receitas, usuario, "Restituições", 1));
		((CategoriaDAO) dao).salvarCategoria(new Categoria(receitas, usuario, "Rendimento", 1));
		
		
	}

}
