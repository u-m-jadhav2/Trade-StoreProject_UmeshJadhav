package test;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trade.store.dto.TradeStoreDto;
import com.trade.store.dto.TradeTxnDto;
import com.trade.store.exception.TradeException;
import com.trade.store.service.TradeStoreService;
import com.trade.store.service.serviceImpl.TradeStoreServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {  "/test/applicationContext_test.xml" })
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class JunitTradeStore {
	
	private static Logger logger = Logger.getLogger(TradeStoreServiceImpl.class.getName());
	
	@Autowired
	TradeStoreService tradeStoreService;
	
	//// Test1 Start
	@Autowired
	TradeTxnDto tradeTxnDto_test1;
	
	@Autowired
	TradeStoreDto tradeStoreDto_test1;
	
	@Test
	//Store Id ST4545 is inactive
    public void test1_Validation_StoreIdInvalid() {
		try {
			tradeStoreService.tradeStoreTrasmission(tradeTxnDto_test1, tradeStoreDto_test1);
		} catch (TradeException e) {
			String errorMsg = e.getMessage();
			boolean inMatchFound = false;
			if(errorMsg.contains("ERROR_CODE=INACTIVE_STORE")) {
				inMatchFound = true;
			}
			
			System.out.print("-----------------\n");
			System.out.print("test1_validateIfStoreIsValid Result --" + errorMsg+"\n");
			System.out.print("-----------------\n");
			
			Assert.assertEquals(inMatchFound, true);
		}
    }
	////Test1 End
	
	
////Test2 Start
	
	@Autowired
	TradeTxnDto tradeTxnDto_test2;
	
	@Autowired
	TradeStoreDto tradeStoreDto_test2;
	
	@Test
	//Store Id Valid but Maturity date is before today
   public void test2_Validation_MaturityDateInvalid() {
		try {
			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.DATE, -2);
			Date dt2 = new Date(cal2.getTimeInMillis());
			tradeTxnDto_test2.setMaturityDate(dt2);
			tradeStoreService.tradeStoreTrasmission(tradeTxnDto_test2, tradeStoreDto_test2);
		} catch (TradeException e) {
			String errorMsg = e.getMessage();
			boolean inMatchFound = false;
			if(errorMsg.contains("ERROR_CODE=INVALID_MATURITY_DATE")) {
				inMatchFound = true;
			}
			System.out.print("-----------------\n");
			System.out.print("test2_Validation_MaturityDateInvalid Result --" + errorMsg +"\n");
			System.out.print("-----------------\n");
			Assert.assertEquals(inMatchFound, true);
		}
   }
	////Test2 End
	
////Test3 Start
	
	@Autowired
	TradeTxnDto tradeTxnDto_test3;
	
	@Autowired
	TradeStoreDto tradeStoreDto_test3;
	
	@Test
	//Store Id and Maturity date Valid but version of input trade is less than the one in store so it should be rejected.
   public void test3_Validation_InputTradeVersionIsLessThanStoreVersion() {
		try {
			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.DATE, 1);
			Date dt2 = new Date(cal2.getTimeInMillis());
			tradeTxnDto_test3.setMaturityDate(dt2);
			tradeStoreService.tradeStoreTrasmission(tradeTxnDto_test3, tradeStoreDto_test3);
		} catch (TradeException e) {
			String errorMsg = e.getMessage();
			boolean inMatchFound = false;
			if(errorMsg.contains("ERROR_CODE=LOWER_VERSION")) {
				inMatchFound = true;
			}
			System.out.print("-----------------\n");
			System.out.print("test3_Validation_InputTradeVersionIsLessThanStoreVersion Result --" + errorMsg +"\n");
			System.out.print("-----------------\n");
			Assert.assertEquals(inMatchFound, true);
		}
   }
	////Test3 End
   
////Test4 Start
	
	@Autowired
	TradeTxnDto tradeTxnDto_test4;
	
	@Autowired
	TradeStoreDto tradeStoreDto_test4;
	
	@Test
  public void test4_Positive_InputTradeVersionIsEqualToStoreVersion() {
		boolean inMatchFound = false;
		try {
			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.DATE, 10);
			Date dt2 = new Date(cal2.getTimeInMillis());
			tradeTxnDto_test4.setMaturityDate(dt2);
			tradeStoreService.tradeStoreTrasmission(tradeTxnDto_test4, tradeStoreDto_test4);
			inMatchFound = true;
			
			System.out.print("-----------------\n");
			System.out.print("test4_Positive_InputTradeVersionIsEqualToStoreVersion Result --\n");
			System.out.print("-----------------\n");
			
		} catch (TradeException e) {
			String errorMsg = e.getMessage();
			System.err.println(errorMsg);
		}
		
		Assert.assertEquals(inMatchFound, true);
  }
	////Test4 End
	
	
////Test5 Start
	
	@Autowired
	TradeTxnDto tradeTxnDto_test5;
	
	@Autowired
	TradeStoreDto tradeStoreDto_test5;
	
	@Test
  public void test5_Positive_InputTradeVersionIsGreaterThenStoreVersion() {
		boolean inMatchFound = false;
		try {
			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.DATE, 10);
			Date dt2 = new Date(cal2.getTimeInMillis());
			tradeTxnDto_test5.setMaturityDate(dt2);
			tradeStoreService.tradeStoreTrasmission(tradeTxnDto_test5, tradeStoreDto_test5);
			inMatchFound = true;
			
			System.out.print("-----------------\n");
			System.out.print("test5_Positive_InputTradeVersionIsGreaterThenStoreVersion Result --\n");
			System.out.print("-----------------\n");
			
		} catch (TradeException e) {
			String errorMsg = e.getMessage();
			System.err.println(errorMsg);
		}
		
		Assert.assertEquals(inMatchFound, true);
  }
	////Test5 End
}
