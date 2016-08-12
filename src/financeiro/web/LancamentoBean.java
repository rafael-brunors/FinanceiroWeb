package financeiro.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import financeiro.model.Categoria;
import financeiro.model.Cheque;
import financeiro.model.Conta;
import financeiro.model.Lancamento;
import financeiro.negocio.ChequeRN;
import financeiro.negocio.LancamentoRN;
import financeiro.util.RNException;



@ManagedBean(name = "lancamentoBean")
@RequestScoped
public class LancamentoBean extends ActionBean<Lancamento> {

	private List<Lancamento> lista;
	private List<Double> saldos = new ArrayList<Double>();
	private float saldoGeral;

	private Lancamento editado = new Lancamento();

	private List<Lancamento> listaAteHoje;
	private List<Lancamento> listaFuturos;
	private Integer numeroCheque;

	public LancamentoBean() {
		super(new LancamentoRN());
	}

	public void novo() {

		editado = new Lancamento();
		editado.setData(new Date());
		numeroCheque = null;
	}

	public void editar() {
		Cheque cheque = editado.getCheque();
		if (cheque != null) {
			numeroCheque = cheque.getId().getNumero();
		}
	}

	public void salvar() {
		try {
			editado.setUsuario(obterUsuarioLogado());
			editado.setConta(obterContaAtiva());
			//rn.salvar(editado);
			((LancamentoRN)rn).salvar(editado, numeroCheque);
			novo();
			lista = null;
			apresentarMensagemDeSucesso("Lançamento incluído com sucesso");
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}

	public void excluir() {
		try {
			rn.obterPorId(editado);
			rn.excluir(editado);
			lista = null;
			apresentarMensagemDeErro("Lancamento excluído com sucesso");
		} catch (RNException e) {
			apresentarMensagemDeErro(e);
		}
	}
	
	public void mudouCheque(ValueChangeEvent event) {
		Integer chequeAnterior = (Integer) event.getOldValue();
		if (chequeAnterior != null) {
			try {
				ChequeRN chequeRN = new ChequeRN();
				chequeRN.desvinculaLancamento(obterContaAtiva(), chequeAnterior);
			} catch (RNException e) {
				apresentarMensagemDeErro(e);
				return;
			}
		}
	}

	public List<Lancamento> getLista() {

		if (lista == null) {
			Calendar dataSaldo = new GregorianCalendar();
			dataSaldo.add(Calendar.MONTH, -1);
			dataSaldo.add(Calendar.DAY_OF_MONTH, -1);
			
			
			// Pega-se uma instancia de calendar com a data atual
			Calendar inicio = new GregorianCalendar();
			
			//tirando um mes da data atual
			inicio.add(Calendar.MONTH, -1);
			
			saldoGeral = ((LancamentoRN) rn).saldo(obterContaAtiva(), dataSaldo.getTime());
			lista = ((LancamentoRN) rn).pesquisarLancamento(obterContaAtiva(), inicio.getTime(), null);
			
			Categoria categoria = null;
			double saldo = saldoGeral;
			for (Lancamento lancamento : lista) {
				categoria = lancamento.getCategoria();
				saldo += (lancamento.getValor().floatValue() * categoria.getFator());
				saldos.add(saldo);
			}			
		}	
		
		return lista;
	}

	public void setLista(List<Lancamento> lista) {
		this.lista = lista;
	}

	public List<Double> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<Double> saldos) {
		this.saldos = saldos;
	}

	public float getSaldoGeral() {
		return saldoGeral;
	}

	public void setSaldoGeral(float saldoGeral) {
		this.saldoGeral = saldoGeral;
	}

	public Lancamento getEditado() {
		return editado;
	}

	public void setEditado(Lancamento editado) {
		this.editado = editado;
	}

	public List<Lancamento> getListaAteHoje() {

		if (listaAteHoje == null) {

			Conta conta = obterContaAtiva();

			Calendar hoje = new GregorianCalendar();

			listaAteHoje = ((LancamentoRN) rn).pesquisarLancamento(conta, null,
					new Date());

		}

		return listaAteHoje;
	}

	public void setListaAteHoje(List<Lancamento> listaAteHoje) {
		this.listaAteHoje = listaAteHoje;
	}

	public List<Lancamento> getListaFuturos() {

		if (listaFuturos == null) {

			Conta conta = obterContaAtiva();

			Calendar amanha = new GregorianCalendar();
			amanha.add(Calendar.DAY_OF_MONTH, 1);

			listaFuturos = ((LancamentoRN) rn).pesquisarLancamento(conta,
					amanha.getTime(), null);

		}

		return listaFuturos;
	}


	public void setListaFuturos(List<Lancamento> listaFuturos) {
		this.listaFuturos = listaFuturos;
	}

	public Integer getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(Integer numeroCheque) {
		this.numeroCheque = numeroCheque;
	}
	
	

}
