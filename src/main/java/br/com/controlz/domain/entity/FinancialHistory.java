package br.com.controlz.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "financial_history")
public class FinancialHistory implements Serializable {

	@ManyToOne
	@JoinColumn(name = "register_id", updatable = false, insertable = false)
	private Register register;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long financialHistoryId;

	@Column(name = "total_credit")
	private Double totalCredit;

	@Column(name = "total_debt")
	private Double totalDebt;

	@Column(name = "register_id")
	private Long registerId;

	@Column(name = "period")
	private LocalDate period;
}
