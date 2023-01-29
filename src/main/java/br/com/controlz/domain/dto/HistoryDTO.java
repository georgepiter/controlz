package br.com.controlz.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class HistoryDTO {

	List<FinancialHistoryDTO> financialList = new ArrayList<>();

	public List<FinancialHistoryDTO> getFinancialList() {
		return financialList;
	}

	public void setFinancialList(List<FinancialHistoryDTO> financialList) {
		this.financialList = financialList;
	}
}
