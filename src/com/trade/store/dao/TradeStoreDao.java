package com.trade.store.dao;

import org.springframework.stereotype.Repository;

import com.trade.store.dto.TradeStoreDto;
import com.trade.store.dto.TradeTxnDto;
import com.trade.store.exception.TradeException;
@Repository
public interface TradeStoreDao {
	public String insertStore(TradeStoreDto tradeStoreDto) throws TradeException;
	public TradeStoreDto isStoreActive(String storeId) throws TradeException;
	public TradeTxnDto getTradeTxnDetailsByLatestVersion(TradeTxnDto tradeTxnDto, TradeStoreDto tradeStoreDto)throws TradeException;
	public void insertTradeTxn(TradeTxnDto tradeTxnDto, TradeStoreDto tradeStoreDto) throws TradeException;
	public void updateTradeTxn(TradeTxnDto tradeTxnDto) throws TradeException;
	public void updateTradeTxnStatusAndComments(TradeTxnDto tradeTxnDto) throws TradeException;
}
