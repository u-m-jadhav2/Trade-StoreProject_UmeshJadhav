package test;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.trade.store.dto.TradeStoreDto;
import com.trade.store.dto.TradeTxnDto;
import com.trade.store.service.TradeStoreService;

@Configuration
@ComponentScan(basePackages="com")
public class TestTradeStore {

	private static Logger logger = Logger.getLogger(TestTradeStore.class.getName());
	
	public static void main(String[] args) {
		logger.info("Started TestTradeStore............" );
		new TestTradeStore().testTradeStoreTrasmission();
	}
	
	
	private static ApplicationContext getApplicationContext() {
		ApplicationContext context = 
		    	  new ClassPathXmlApplicationContext(new String[] {"resources/applicationContext.xml"});
		logger.info("ApplicationContext is created " + context);
		return context;
	}
	
	private void testTradeStoreTrasmission() {
		ApplicationContext context = getApplicationContext();
		TradeStoreService tradeStoreService = (TradeStoreService)context.getBean("tradeStoreServiceImpl") ;
		
		TradeTxnDto tradeTxnDto = new TradeTxnDto();
		TradeStoreDto tradeStoreDto = new TradeStoreDto();
		setStoreStubData(tradeTxnDto, tradeStoreDto);
		tradeStoreService.tradeStoreTrasmission(tradeTxnDto, tradeStoreDto);
	}
	
	private void setStoreStubData(TradeTxnDto tradeTxnDto, TradeStoreDto tradeStoreDto) {
		tradeStoreDto.setStoreId("ST1111");
		
		tradeTxnDto.setTradeId("T1");
		tradeTxnDto.setVersionNumber("1");
		tradeTxnDto.setCounterPartyId("CP-1");
		tradeTxnDto.setBookId("B5");
		tradeTxnDto.setMaturityDate(new Date());
	}
	
}
