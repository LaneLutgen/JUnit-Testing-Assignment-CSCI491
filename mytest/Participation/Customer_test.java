package Participation;
import org.junit.* ;
import static org.junit.Assert.* ;

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
	
	@Test
	public void test2() { 
		System.out.println("Provide a short description...") ;
		// an example of test that fails, in this case trivially:
		assertTrue(1/0 == 0) ;
	}
	
	
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
	public void getDiscountValue_DiscountApplicable()
	{
		System.out.println("Testing getDiscountValue with applicable input...");
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
	public void getDiscountValue_DiscountNotApplicable()
	{
		System.out.println("Testing getDiscountValue with non-applicable input...");
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
	
	
	
	
	@Test
	public void getCostToPay_test()
	{
		
	}
	
	@Test
	public void getParticipationGroups_test()
	{
		
	}
}
