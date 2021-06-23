package com.trade.store.dao.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.trade.store.dao.TradeStoreDao;
import com.trade.store.dto.TradeStoreDto;
import com.trade.store.dto.TradeTxnDto;
import com.trade.store.exception.TradeException;

@Repository
@PropertySource("classpath:properties/tradeStoreSql.properties")
@PropertySource(value = "classpath:properties/tradeStoreSql.properties", ignoreResourceNotFound = true)
public class TradeStoreDaoImpl implements TradeStoreDao{

	private static Logger logger = Logger.getLogger(TradeStoreDaoImpl.class.getName());
	
	private JdbcTemplate jdbcTemplate;
	
	@Value("${TRADE_STORE_SELECT_SQL}")
	private String tradeStoreSelectSQL;
	
	@Value("${TRADE_TXN_SELECT_LATEST_VERSION_SQL}")
	private String tradeTxnSelectLatestVersionSQL;
	
	@Value("${TRADE_TXN_INSERT_SQL}")
	private String tradeTxnInsertSQL;
	
	@Autowired
	private void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public String insertStore(TradeStoreDto tradeStoreDto) throws TradeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TradeStoreDto isStoreActive(String storeId) throws TradeException {
		StringBuilder infoStringBuilder = new StringBuilder("TradeStoreDaoImpl.isStoreActive started..");
		infoStringBuilder.append("StoreId '").append(storeId).append("' SQL [").append(tradeStoreSelectSQL).append("]");
		logger.info(infoStringBuilder);
		
		return jdbcTemplate.queryForObject(tradeStoreSelectSQL, new Object[]{storeId}, new TradeStoreRowMapper());
	}

	@Override
	public TradeTxnDto getTradeTxnDetailsByLatestVersion(TradeTxnDto tradeTxnDto, TradeStoreDto tradeStoreDto)
			throws TradeException {
		String storeId = tradeStoreDto.getStoreId();
		String tradeId = tradeTxnDto.getTradeId();
		
		StringBuilder infoStringBuilder = new StringBuilder("TradeStoreDaoImpl.getTradeTxnDetailsByLatestVersion started..");
		infoStringBuilder.append("StoreId '").append(storeId);
		infoStringBuilder.append("' tradeId '").append(tradeId);
		infoStringBuilder.append("' SQL [").append(tradeTxnSelectLatestVersionSQL).append("]");
		logger.info(infoStringBuilder);
		
		try {
			return jdbcTemplate.queryForObject(tradeTxnSelectLatestVersionSQL, new Object[]{tradeId, storeId}, new TradeTxnRowMapper());
		} catch (EmptyResultDataAccessException e) {
			StringBuilder errorStringBuilder = new StringBuilder("TradeStoreDaoImpl.isStoreActive No Data Found..");
			errorStringBuilder.append(" StoreId '").append(storeId);
			errorStringBuilder.append("' tradeId '").append(tradeId);
			errorStringBuilder.append("' SQL [").append(tradeTxnSelectLatestVersionSQL).append("]");
			logger.error(errorStringBuilder);
			return null;
		}
	}
	
	@Override
	public void insertTradeTxn(TradeTxnDto tradeTxnDto, TradeStoreDto tradeStoreDto) throws TradeException {
		
		String storeId = tradeStoreDto.getStoreId();
		String tradeId = tradeTxnDto.getTradeId();
		
        // define query arguments
        Object[] params = new Object[] { tradeId, tradeTxnDto.getVersionNumber(), 
        								tradeTxnDto.getCounterPartyId(),tradeTxnDto.getBookId(),
        								tradeTxnDto.getMaturityDate(),storeId,
        								tradeTxnDto.getStatus(), tradeTxnDto.getComments()};
       
        int row = jdbcTemplate.update(tradeTxnInsertSQL, params);
       
        StringBuilder infoStringBuilder = new StringBuilder("TradeStoreDaoImpl.insertTradeTxn Finised..Num of Row Inserted '");
        infoStringBuilder.append(row);
		infoStringBuilder.append("' StoreId '").append(storeId);
		infoStringBuilder.append("' tradeId '").append(tradeId);
		infoStringBuilder.append("' SQL [").append(tradeStoreSelectSQL).append("]");
		logger.info(infoStringBuilder);
		
        
	}

public class TradeStoreRowMapper implements RowMapper<TradeStoreDto> {

    @Override
    public TradeStoreDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    	TradeStoreDto tradeStoreDto = new TradeStoreDto();
    	char ch = 0;
    	String storeActive = rs.getString("STORE_ACTIVE");
    	if(!StringUtils.isEmpty(storeActive)) {
    		ch=storeActive.charAt(0);
    	}
    	tradeStoreDto.setStoreActive(ch);
        return tradeStoreDto;
    }
}

public class TradeTxnRowMapper implements RowMapper<TradeTxnDto> {

    @Override
    public TradeTxnDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    	TradeTxnDto tradeTxnDto = new TradeTxnDto();
    	tradeTxnDto.setTradeId(rs.getString("TRADE_ID"));
		tradeTxnDto.setVersionNumber(rs.getString("VERSION"));
		tradeTxnDto.setCounterPartyId(rs.getString("COUNTER_PARTY_ID"));
		tradeTxnDto.setBookId(rs.getString("BOOK_ID"));
		tradeTxnDto.setMaturityDate(rs.getDate("MATURITY_DATE"));
        return tradeTxnDto;
    }
}

@Override
public void updateTradeTxn(TradeTxnDto tradeTxnDto) throws TradeException {
	// TODO Auto-generated method stub
	
}

@Override
public void updateTradeTxnStatusAndComments(TradeTxnDto tradeTxnDto) throws TradeException {
	// TODO Auto-generated method stub
	
}



}