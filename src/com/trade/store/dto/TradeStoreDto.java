package com.trade.store.dto;

import java.util.Date;

public class TradeStoreDto {
	private String storeId = null;
	private String storeName = null;
	private String storeAddress = null;
	private String storeMobile = null;
	private String storeLandline = null;
	private char storeActive;
	private Date createdDate = null;
	private Date updatedDate = null;
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getStoreMobile() {
		return storeMobile;
	}
	public void setStoreMobile(String storeMobile) {
		this.storeMobile = storeMobile;
	}
	public String getStoreLandline() {
		return storeLandline;
	}
	public void setStoreLandline(String storeLandline) {
		this.storeLandline = storeLandline;
	}
	public char getStoreActive() {
		return storeActive;
	}
	public void setStoreActive(char storeActive) {
		this.storeActive = storeActive;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
