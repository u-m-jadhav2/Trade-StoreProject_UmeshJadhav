package com.trade.store.service;

import com.trade.store.dto.TradeStoreDto;
import com.trade.store.dto.TradeTxnDto;
import com.trade.store.exception.TradeException;


public interface TradeStoreService {
	public String createStore(TradeStoreDto tradeStoreDto) throws TradeException;
	public TradeStoreDto isStoreActive(String storeId) throws TradeException;
	
	public void tradeStoreTrasmission(TradeTxnDto tradeTxnDto, TradeStoreDto tradeStoreDto) throws TradeException;
	
}
