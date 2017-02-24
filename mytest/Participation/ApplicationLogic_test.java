package Participation;
import org.junit.* ;
import static org.junit.Assert.* ;

import java.util.Collection;
import java.util.Map;

/**
 * This is just a simple template for Junit test-class for testing
 * the class ApplicationLogic. Testing this class is a bit more
 * complicated. It uses database, which form an implicit part of
 * the state of ApplicationLogic. After each test case, you need to
 * reset the content of the database to the value it had, before
 * the test case. Otherwise, multiple test cases will interfere
 * with each other, which makes debugging hard should you find error.
 * 
 */
public class ApplicationLogic_test {

	/**
	 * Provide a functionality to reset the database. Here I will just
	 * delete the whole database file. 
	 */
	private void setupDB() {
		Persistence.wipedb() ;
	}
	
	
	@Test
	public void test1() {
		// We'll always begin by reseting the database. This makes sure
		// the test start from a clean, well defined state of the database.
		// In this case it would be just an empty database, though it 
		// doesn't have to be like that.
		setupDB() ;
		
		System.out.println("** Testing add customer...") ;
		
		// Creating the target thing you want to test:
		ApplicationLogic SUT = new ApplicationLogic() ;
		
		// Now let's perform some testing. If we add a customer to the system,
		// test that this customer should then be really added to the system:
		int duffyID = SUT.addCustomer("Duffy Duck", "") ;
		Customer C = SUT.findCustomer(duffyID) ;
		assertTrue(C.name.equals("Duffy Duck")) ;
		assertTrue(C.email.equals("")) ;		
	}
	
	// Another example...
	@Test
	public void test2() {
		setupDB() ;
		ApplicationLogic SUT = new ApplicationLogic() ;
		
		System.out.println("** Testing getCostToPay ...") ;
		
		int duffyID = SUT.addCustomer("Duffy Duck", "") ;
		int flowerServiceID = SUT.addService("Flowers online shop", 100) ;
		// let Duffy but 2x participations on Flower-online:
		SUT.addParticipation(duffyID, flowerServiceID) ;
		SUT.addParticipation(duffyID, flowerServiceID) ;

		// Now let's check if the system correctly calculates the participation
		// cost of Duffy:
		Customer C = SUT.findCustomer(duffyID) ;
		assertTrue(C.getCostToPay() == 200) ;
	}
	
	@Test
	public void removeService_ServiceIsRemovedSuccessfully() {
		setupDB() ;
		ApplicationLogic SUT = new ApplicationLogic() ;
		
		System.out.println("Testing removeService...") ;
		
		int duffyID = SUT.addCustomer("Duffy Duck", "") ;
		int flowerServiceID = SUT.addService("Flowers online shop", 100);
		SUT.addParticipation(duffyID, flowerServiceID) ;
		
		SUT.removeService(flowerServiceID);
		
		Customer C = SUT.findCustomer(duffyID) ;

		assertTrue(C.getServices().isEmpty()) ;
	}
	
	@Test
	public void removeService_MultipleServices() {
		setupDB() ;
		ApplicationLogic SUT = new ApplicationLogic() ;
		
		System.out.println("Testing removeService...") ;
		
		int duffyID = SUT.addCustomer("Duffy Duck", "") ;
		int flowerServiceID = SUT.addService("Flowers online shop", 100);
		int otherServiceID = SUT.addService("Other", 300);
		SUT.addParticipation(duffyID, flowerServiceID) ;
		SUT.addParticipation(duffyID, otherServiceID) ;
		
		SUT.removeService(flowerServiceID);
		
		Customer C = SUT.findCustomer(duffyID) ;

		assertTrue(C.getServices().size() == 1) ;
	}
	
	@Test
	public void removeService_RemoveServiceNotTiedToParticipation() {
		setupDB() ;
		ApplicationLogic SUT = new ApplicationLogic() ;
		
		System.out.println("Testing removeService...") ;
		
		int duffyID = SUT.addCustomer("Duffy Duck", "") ;
		int otherServiceID = SUT.addService("Other", 300);
		
		SUT.removeService(otherServiceID);
		
		Customer C = SUT.findCustomer(duffyID) ;

		//Service is not removed
		assertTrue(C.getServices().size() == 0) ;
	}
	
	@Test
	public void removeService_InvalidServiceID() {
		setupDB() ;
		ApplicationLogic SUT = new ApplicationLogic() ;
		
		System.out.println("Testing removeService...") ;
		
		int duffyID = SUT.addCustomer("Duffy Duck", "") ;
		int otherServiceID = SUT.addService("Other", 300);
		SUT.addParticipation(duffyID, otherServiceID) ;
		
		//Try to remove invalid service ID
		SUT.removeService(1000);
		
		Customer C = SUT.findCustomer(duffyID) ;

		//Service is not removed
		assertTrue(C.getServices().size() == 1) ;
	}
	
	@Test
	public void resolve_ApplicableDiscount() {		
		System.out.println("Testing resolve()...");
		setupDB() ;
		
		int input = 500000;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		app.awardDiscount(personID, ApplicationLogic.D1000);
		
		Map<Customer, Integer> payment = app.resolve();
		
		Collection<Integer> values = payment.values();
		
		for(Integer val : values)
		{
			//Payment after discount is applied
			assertTrue(val == 475000);
		}
	}
	
	@Test
	public void resolve_NonApplicableDiscount() {		
		System.out.println("Testing resolve() with non-applicable discount...");
		setupDB() ;
		
		int input = 500;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		app.awardDiscount(personID, ApplicationLogic.D1000);
		
		Map<Customer, Integer> payment = app.resolve();
		
		Collection<Integer> values = payment.values();
		
		for(Integer val : values)
		{
			assertTrue(val == 500);
		}
	}
}
