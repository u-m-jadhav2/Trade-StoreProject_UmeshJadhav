package com.trade.store.service.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trade.store.dao.TradeStoreDao;
import com.trade.store.dto.TradeStoreDto;
import com.trade.store.dto.TradeTxnDto;
import com.trade.store.exception.TradeException;
import com.trade.store.service.TradeStoreService;
import com.trade.store.utility.TradeStoreConstant;

@Service
public class TradeStoreServiceImpl implements TradeStoreService {
	
	private static Logger logger = Logger.getLogger(TradeStoreServiceImpl.class.getName());
	
	@Autowired
	TradeStoreDao tradeStoreDao;
	
	@Override
	public String createStore(TradeStoreDto tradeStoreDto) throws TradeException {
		return null;
	}

	@Override
	public TradeStoreDto isStoreActive(String storeId) throws TradeException {
		return tradeStoreDao.isStoreActive(storeId);
	}

	@Override
	public void tradeStoreTrasmission(TradeTxnDto tradeTxnDto, TradeStoreDto tradeStoreDto) throws TradeException {
		validateStore(tradeStoreDto);
		
		Date maturityDate = tradeTxnDto.getMaturityDate();
		boolean isMaturityDateValid = validateTradeMaturityDate(maturityDate);
		if(!isMaturityDateValid) {
			StringBuilder errorStringBuilder = new StringBuilder("ERROR_CODE=INVALID_MATURITY_DATE");
			errorStringBuilder.append(" Input Trade '").append(tradeTxnDto.getTradeId()).append("' is already matured on (")
			.append(maturityDate).append("). So Its Trading is not allowed.");
			logger.error(errorStringBuilder);
			throw new TradeException(errorStringBuilder.toString()) ;
		}
		
		int[] validationArray = validateTxnByVersion(tradeTxnDto, tradeStoreDto);
		int validationCount = validationArray[0];
		int latestVersion = validationArray[1];
		
		if(validationCount == -1) {
			StringBuilder errorStringBuilder = new StringBuilder("ERROR_CODE=LOWER_VERSION");
			errorStringBuilder.append("Input Trade '").append(tradeTxnDto.getTradeId()).append("' has lower version (")
			.append(tradeTxnDto.getVersionNumber()).append("). So Its Trading is not allowed. It's Latest version '")
			.append(latestVersion).append("'");
			tradeTxnDto.setStatus(TradeStoreConstant.STATUS_REJECTED);
			tradeTxnDto.setComments(errorStringBuilder.toString());
			tradeStoreDao.insertTradeTxn(tradeTxnDto, tradeStoreDto);
			throw new TradeException(errorStringBuilder.toString()) ;	
		}
		
		if(validationCount == 0) {
			tradeTxnDto.setStatus(TradeStoreConstant.STATUS_UPDATED);
			tradeTxnDto.setComments("Updated Trade");
			tradeStoreDao.insertTradeTxn(tradeTxnDto, tradeStoreDto);
		}else {
			tradeTxnDto.setStatus(TradeStoreConstant.STATUS_APPROVED);
			tradeStoreDao.insertTradeTxn(tradeTxnDto, tradeStoreDto);
		}
		
		StringBuilder infoStringBuilder = new StringBuilder();
		infoStringBuilder.append("Input Trade '").append(tradeTxnDto.getTradeId());
		infoStringBuilder.append("' is successfully accepted by StoreId '").append(tradeStoreDto.getStoreId()).append("'");
		logger.info(infoStringBuilder);
		
	}
	
	private void validateStore(TradeStoreDto tradeStoreDto) throws TradeException {
		String storeId = tradeStoreDto.getStoreId();
		TradeStoreDto TradeStoreDto = isStoreActive(storeId);
		
		if(TradeStoreDto==null) {
			StringBuilder errorStringBuilder = new StringBuilder();
			errorStringBuilder.append("Invalid Store. Store Not Found for storeId '").append(storeId).append("'");
			logger.error(errorStringBuilder);
			throw new TradeException(errorStringBuilder.toString()) ;
		}
		
		char StoreActive = TradeStoreDto.getStoreActive();
		if(StoreActive!='Y') {
			StringBuilder errorStringBuilder = new StringBuilder("ERROR_CODE=INACTIVE_STORE");
			errorStringBuilder.append(" Store Found storeId '").append(storeId).append("' but it is not active. Trades transmission can not be done on this store.");
			logger.error(errorStringBuilder);
			throw new TradeException(errorStringBuilder.toString()) ;
		}
		
		StringBuilder infoStringBuilder = new StringBuilder();
		infoStringBuilder.append("StoreId '").append(storeId).append("' is Validated successfully..");
		logger.info(infoStringBuilder);
	}
	
	private int[] validateTxnByVersion(TradeTxnDto tradeTxnDto, TradeStoreDto tradeStoreDto) throws TradeException {
		int[] validationArray = new int[2];
		int validationCount = 1;
		TradeTxnDto tradeTxnDtoOut = tradeStoreDao.getTradeTxnDetailsByLatestVersion(tradeTxnDto, tradeStoreDto);
		if(tradeTxnDtoOut!=null) {
			int versionNumberInput = Integer.parseInt(tradeTxnDto.getVersionNumber());
			if(!StringUtils.isEmpty(tradeTxnDtoOut.getVersionNumber())) {
				int versionNumberLatest = Integer.parseInt(tradeTxnDtoOut.getVersionNumber());
				validationArray[1] = versionNumberLatest;
				StringBuilder infoStringBuilder = new StringBuilder();
				infoStringBuilder.append("StoreId '").append(tradeStoreDto.getStoreId()).append("' ");
				infoStringBuilder.append(" TradeId '").append(tradeTxnDto.getTradeId()).append("' ");
				infoStringBuilder.append("' versionNumberInput '").append(versionNumberInput).append("' ");
				infoStringBuilder.append("' versionNumberLatest '").append(versionNumberLatest).append("' ");
				
				if(versionNumberInput == versionNumberLatest){
					validationCount = 0;
					infoStringBuilder.append(" This Trade will be updated on existing version.");
				}else if(versionNumberInput>versionNumberLatest){
					validationCount = 1;
					infoStringBuilder.append(" This Trade will be newly inserted in store.");
				}else {
					validationCount = -1;
					infoStringBuilder.append(" This Trade will be rejected by store.");
				}
				validationArray[0] = validationCount;
				logger.info(infoStringBuilder);
			}
		}else {
			validationCount = 1;
			validationArray[0] = validationCount;
			StringBuilder infoStringBuilder = new StringBuilder();
			infoStringBuilder.append("StoreId '").append(tradeStoreDto.getStoreId()).append("' ");
			infoStringBuilder.append(" TradeId '").append(tradeTxnDto.getTradeId()).append("' ");
			infoStringBuilder.append(" No Data Found. This Trade will be newly inserted in store.");
		}
		
		return validationArray;
	}
	
	 private boolean validateTradeMaturityDate(Date maturityDate) throws TradeException{
		 try {
			Date current = new Date();
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			 
			 Date date1 = sdf.parse(sdf.format(maturityDate));
			 Date date2 = sdf.parse(sdf.format(current));
			
			 return date1.before(date2)  ? false:true;
		} catch (Exception e) {
			StringBuilder errorStringBuilder = new StringBuilder("Error in validateTradeMaturityDate -");
			errorStringBuilder.append(e);
			e.printStackTrace();
			logger.error(errorStringBuilder);
			
			throw new TradeException(e.getMessage());
		}
		
	 }


}
