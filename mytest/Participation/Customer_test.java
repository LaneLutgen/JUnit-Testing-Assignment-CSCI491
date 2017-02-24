package Participation;
import org.junit.* ;

import Participation.Customer.ServiceInfo;

import static org.junit.Assert.* ;

import java.util.Map;

/**
 * This is just a simple template for a JUnit test-class for testing 
 * the class Customer.
 */
public class Customer_test {

	private void setupDB() {
		Persistence.wipedb() ;
	}
	
	@Test
	public void test1() {
		System.out.println("Provide here a short description of your test purpose here...") ;
		Customer C = new Customer(0,"Duffy Duck","") ;
		assertTrue(C.getCostToPay() == 0) ; 
	}
	
//	@Test
//	public void test2() { 
//		System.out.println("Provide a short description...") ;
//		// an example of test that fails, in this case trivially:
//		assertTrue(1/0 == 0) ;
//	}
	
	
	// and so on ...
	
	/*
	 * This test creates a Customer with 2 services of $25 and $50
	 * 
	 * Expected Result: Total participation value is $75
	 */
	@Test
	public void getParticipationValue_test()
	{
		System.out.println("Testing getParticipationValue...");
		setupDB() ;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", 25);
		int serviceIDTwo = app.addService("Other shop", 50);
		app.addParticipation(personID, serviceID);
		app.addParticipation(personID, serviceIDTwo);
		
		Customer C = app.findCustomer(personID);
		
		int value = C.participationValue();
		
		assertTrue(value == 75);
	}
	
	/*
	 * 
	 */
	@Test
	public void getDiscountValue_DiscountApplicable1000()
	{
		System.out.println("Testing getDiscountValue for Discount_1000 with applicable input...");
		setupDB() ;
		
		int input = 500000;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		app.awardDiscount(personID, ApplicationLogic.D1000);
		
		Customer C = app.findCustomer(personID);
		
		int actualValue = C.getDiscountValue();
		
		int expectedValue = (input / 1000) * 50;
		
		assertTrue(actualValue == expectedValue);
	}
	
	/*
	 * 
	 */
	@Test
	public void getDiscountValue_DiscountNotApplicable1000()
	{
		System.out.println("Testing getDiscountValue for Discount_1000 with non-applicable input...");
		setupDB() ;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", 5000);
		app.addParticipation(personID, serviceID);
		app.awardDiscount(personID, ApplicationLogic.D1000);
		
		Customer C = app.findCustomer(personID);
		
		int value = C.getDiscountValue();
		
		assertTrue(value == 0);
	}
	
	/*
	 * 
	 */
	@Test
	public void getDiscountValue_DiscountApplicable5pack()
	{
		System.out.println("Testing getDiscountValue for Discount_5pack with applicable input");
		setupDB() ;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceOneID = app.addService("Shop", 10000);
		int serviceTwoID = app.addService("Shop2", 20000);
		int serviceThreeID = app.addService("Shop3", 30000);
		int serviceFourID = app.addService("Shop4", 40000);
		int serviceFiveID = app.addService("Shop5", 50000);
		app.addParticipation(personID, serviceOneID);
		app.addParticipation(personID, serviceTwoID);
		app.addParticipation(personID, serviceThreeID);
		app.addParticipation(personID, serviceFourID);
		app.addParticipation(personID, serviceFiveID);
		app.awardDiscount(personID, ApplicationLogic.D5p);
		
		Customer C = app.findCustomer(personID);
		
		int value = C.getDiscountValue();
		
		//This test fails because Discount_5pack is not expressed in euro cents
		assertTrue(value == 10000);
	}
	
	/*
	 * 
	 */
	@Test
	public void getDiscountValue_DiscountNotApplicable5pack()
	{
		System.out.println("Testing getDiscountValue for Discount_5pack with non applicable input");
		setupDB() ;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceOneID = app.addService("Shop", 10000);
		int serviceTwoID = app.addService("Shop2", 20000);
		int serviceThreeID = app.addService("Shop3", 30000);
		int serviceFourID = app.addService("Shop4", 40000);
		app.addParticipation(personID, serviceOneID);
		app.addParticipation(personID, serviceTwoID);
		app.addParticipation(personID, serviceThreeID);
		app.addParticipation(personID, serviceFourID);
		app.awardDiscount(personID, ApplicationLogic.D5p);
		
		Customer C = app.findCustomer(personID);
		
		int value = C.getDiscountValue();
		
		assertTrue(value == 0);
	}
	
	
	@Test
	public void getCostToPay_test()
	{
		System.out.println("Testing getCostToPay...");
		setupDB() ;
		
		int input = 500000;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		app.awardDiscount(personID, ApplicationLogic.D1000);
		
		Customer C = app.findCustomer(personID);
		
		int actualValue = C.getCostToPay();
		
		int expectedValue = C.participationValue() - C.getDiscountValue();
		
		assertTrue(actualValue == expectedValue);
	}
	
	@Test
	public void getParticipationGroups_ServiceInfoNotNull()
	{
		System.out.println("Testing getParticipationGroups service info present...");
		setupDB() ;
		
		int input = 500000;
		
		ApplicationLogic app = new ApplicationLogic();
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		
		Customer C = app.findCustomer(personID);
		
		Map<Service, ServiceInfo> result = C.getParticipationGroups();
		
		for(ServiceInfo info : result.values())
		{
			assertTrue(info.totalParticipationValue == input);
		}
	}
	
	@Test
	public void getParticipationGroups_DuplicateParticipation()
	{
		System.out.println("Testing getParticipationGroups service info present...");
		setupDB() ;
		
		int input = 500000;
		
		ApplicationLogic app = new ApplicationLogic();
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		app.addParticipation(personID, serviceID);
		
		Customer C = app.findCustomer(personID);
		
		Map<Service, ServiceInfo> result = C.getParticipationGroups();
		
		for(ServiceInfo info : result.values())
		{
			assertTrue(info.totalParticipationValue == input + input);
		}
	}
}
