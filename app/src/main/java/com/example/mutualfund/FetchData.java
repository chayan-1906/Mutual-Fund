package com.example.mutualfund;

public class FetchData {
	
	private Integer investmentId;
	private Integer installmentDate;
	private String folioNo;
	private String name;
	private String mutualFundName;
	private String schemeName;
	private String amount;
	private String lastInstallmentDate;
	private String blank1;
	private String blank2;
	private String createDate;
	private String updateDate;
	
	public FetchData() {
	}

	public FetchData(Integer investmentId, Integer installmentDate, String folioNo, String name, String mutualFundName, String schemeName, String lastInstallmentDate, String amount, String blank1, String blank2, String createDate) {
		this.investmentId = investmentId;
		this.installmentDate = installmentDate;
		this.folioNo = folioNo;
		this.name = name;
		this.mutualFundName = mutualFundName;
		this.schemeName = schemeName;
		this.amount = amount;
		this.lastInstallmentDate = lastInstallmentDate;
		this.blank1 = blank1;
		this.blank2 = blank2;
		this.createDate = createDate;
	}
	public FetchData(Integer investmentId, Integer installmentDate, String folioNo, String name, String mutualFundName, String schemeName, String lastInstallmentDate, String amount, String blank1, String blank2, String createDate, String updateDate) {
		this.investmentId = investmentId;
		this.installmentDate = installmentDate;
		this.folioNo = folioNo;
		this.name = name;
		this.mutualFundName = mutualFundName;
		this.schemeName = schemeName;
		this.amount = amount;
		this.lastInstallmentDate = lastInstallmentDate;
		this.blank1 = blank1;
		this.blank2 = blank2;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}
	
	public Integer getInvestmentId() {
		return investmentId;
	}
	
	public void setInvestmentId(Integer investmentId) {
		this.investmentId = investmentId;
	}
	
	public Integer getInstallmentDate() {
		return installmentDate;
	}
	
	public void setInstallmentDate(Integer installmentDate) {
		this.installmentDate = installmentDate;
	}
	
	public String getFolioNo() {
		return folioNo;
	}
	
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMutualFundName() {
		return mutualFundName;
	}
	
	public void setMutualFundName(String mutualFundName) {
		this.mutualFundName = mutualFundName;
	}
	
	public String getSchemeName() {
		return schemeName;
	}
	
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getLastInstallmentDate() {
		return lastInstallmentDate;
	}
	
	public void setLastInstallmentDate(String lastInstallmentDate) {
		this.lastInstallmentDate = lastInstallmentDate;
	}
	
	public String getBlank1() {
		return blank1;
	}
	
	public void setBlank1(String blank1) {
		this.blank1 = blank1;
	}
	
	public String getBlank2() {
		return blank2;
	}
	
	public void setBlank2(String blank2) {
		this.blank2 = blank2;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
}
