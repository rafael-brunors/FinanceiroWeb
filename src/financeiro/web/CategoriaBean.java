package financeiro.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import financeiro.model.Categoria;
import financeiro.negocio.CategoriaRN;
import financeiro.util.RNException;

@ManagedBean(name = "categoriaBean")
@RequestScoped
public class CategoriaBean extends ActionBean<Categoria> {

	private TreeNode categoriasTree;
	private Categoria editada = new Categoria();
	private List<SelectItem> categoriaSelect;
	private boolean mostraEdicao = false;
	
	public CategoriaBean() {
		super(new CategoriaRN());
	}
	
	public void novo() {
		Categoria pai = null;
		if (this.editada.getCodigo() != null) {
			pai = rn.obterPorId(editada);
		}
		this.editada = new Categoria();
		this.editada.setPai(pai);
		this.mostraEdicao = true;
	}
	
	public void selecionar(NodeSelectEvent event) {
		this.editada = (Categoria) event.getTreeNode().getData();
		Categoria pai = this.editada.getPai();
		if (this.editada != null && pai != null && pai.getCodigo() != null) {
			this.mostraEdicao = true;
		} else {
			this.mostraEdicao = false;
		}
	}
	
	public void salvar() {
		try {
			this.editada.setUsuario(obterUsuarioLogado());
			rn.salvar(this.editada);
			
			editada = null;
			mostraEdicao = false;
			categoriasTree = null;
			categoriaSelect = null;
			apresentarMensagemDeSucesso("Categoria incluída com sucesso");
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}
	
	public void excluir() { 
		try {
			rn.excluir(this.editada);
			
			editada = null;
			mostraEdicao = false;
			categoriasTree = null;
			categoriaSelect = null;
			apresentarMensagemDeErro("Categoria excluída com sucesso");
		} catch (RNException e) {
			apresentarMensagemDeErro(e); 
		}
	}
	
	public TreeNode getCategoriasTree() {
		if (this.categoriasTree == null) {
			List<Categoria> categorias = rn.pesquisar(new Categoria(obterUsuarioLogado()));
			this.categoriasTree = new DefaultTreeNode(null, null);
			this.montaDadosTree(this.categoriasTree, categorias);
		}
		return this.categoriasTree;
	}
	
	private void montaDadosTree(TreeNode pai, List<Categoria> lista) {
		if (lista != null && lista.size() > 0) {
			TreeNode filho = null;
			for (Categoria categoria : lista) {
				filho = new DefaultTreeNode(categoria, pai);
				this.montaDadosTree(filho, categoria.getFilhos());
			}
		}
	}
	
	public List<SelectItem> getCategoriasSelect() {
		if (categoriaSelect == null) {
			categoriaSelect = new ArrayList<SelectItem>();
			List<Categoria> categorias = rn.pesquisar(new Categoria(obterUsuarioLogado()));
			montaDadosSelect(this.categoriaSelect, categorias, "");
		}
		return categoriaSelect;
	}
	
	public void montaDadosSelect(List<SelectItem> select, List<Categoria> categorias,
			String prefixo) {
		SelectItem item = null;
		if(categorias != null) {
			for (Categoria categoria : categorias) {
				item = new SelectItem(categoria, prefixo + categoria.getDescricao());
				item.setEscape(false);
				select.add(item);
				montaDadosSelect(select,  categoria.getFilhos(), prefixo + "&nbsp&nbsp;");
			}
		}
	}

	public Categoria getEditada() {
		return editada;
	}

	public void setEditada(Categoria editada) {
		this.editada = editada;
	}

	public List<SelectItem> getCategoriaSelect() {
		return categoriaSelect;
	}

	public void setCategoriaSelect(List<SelectItem> categoriaSelect) {
		this.categoriaSelect = categoriaSelect;
	}

	public boolean isMostraEdicao() {
		return mostraEdicao;
	}

	public void setMostraEdicao(boolean mostraEdicao) {
		this.mostraEdicao = mostraEdicao;
	}

	public void setCategoriasTree(TreeNode categoriasTree) {
		this.categoriasTree = categoriasTree;
	}
}
