package financeiro.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import financeiro.domain.SituacaoCheque;


@Entity
@Table(name = "Cheque")
public class Cheque implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	
	public Cheque() {
		super();
	}

	public Cheque(ChequeId id) {
		super();
		this.id = id;
	}

	
	public Cheque(Conta conta) {
		super();
		this.conta = conta;
	}


	@EmbeddedId
	private ChequeId id;
		
	
	@Column(name="data_cadastro", nullable=false, updatable=false)
	private Date dataCadastro;
	
	@Column(nullable=false, precision=1)
	@Enumerated(EnumType.STRING)
	private SituacaoCheque situacao;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(name = "lancamento", referencedColumnName = "codigo", nullable = true)
	@ForeignKey(name = "fk_cheque_lancamento")
	private Lancamento lancamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(name = "conta", referencedColumnName = "cod_conta", insertable = false, updatable = false)
	@ForeignKey(name = "fk_cheque_conta")
	private Conta conta;

	public ChequeId getId() {
		return id;
	}

	public void setId(ChequeId id) {
		this.id = id;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public SituacaoCheque getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoCheque situacao) {
		this.situacao = situacao;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result
				+ ((dataCadastro == null) ? 0 : dataCadastro.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lancamento == null) ? 0 : lancamento.hashCode());
		result = prime * result
				+ ((situacao == null) ? 0 : situacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Cheque))
			return false;
		Cheque other = (Cheque) obj;
		if (conta == null) {
			if (other.conta != null)
				return false;
		} else if (!conta.equals(other.conta))
			return false;
		if (dataCadastro == null) {
			if (other.dataCadastro != null)
				return false;
		} else if (!dataCadastro.equals(other.dataCadastro))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lancamento == null) {
			if (other.lancamento != null)
				return false;
		} else if (!lancamento.equals(other.lancamento))
			return false;
		if (situacao != other.situacao)
			return false;
		return true;
	}
	
}
