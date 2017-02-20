package Participation;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class Discount_1000_test {

	private void setupDB() {
		Persistence.wipedb() ;
	}
	
	@Test
	public void applicable_ReturnsTrue() 
	{
		System.out.println("Testing applicable() for Discount_1000...");
		
		setupDB() ;
		
		int input = 500000;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		app.awardDiscount(personID, ApplicationLogic.D1000);
		
		Customer C = app.findCustomer(personID);
		Set<Discount> discounts = C.getDiscounts();
		
		for (Discount D : discounts)
		{
			assertTrue(D.applicable(C));
		}
	}
	
	@Test
	public void applicable_ReturnsFalse() 
	{
		System.out.println("Testing applicable() for Discount_1000...");
		
		setupDB() ;
		
		int input = 5000;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		app.awardDiscount(personID, ApplicationLogic.D1000);
		
		Customer C = app.findCustomer(personID);
		Set<Discount> discounts = C.getDiscounts();
		
		for (Discount D : discounts)
		{
			assertFalse(D.applicable(C));
		}
	}
	
	@Test
	public void calcDiscount_test()
	{
		System.out.println("Testing calcDiscount() for Discount_1000...");
		
		setupDB() ;
		
		int input = 500000;
		
		ApplicationLogic app = new ApplicationLogic();
		
		int personID = app.addCustomer("Person", "");
		int serviceID = app.addService("Shop", input);
		app.addParticipation(personID, serviceID);
		app.awardDiscount(personID, ApplicationLogic.D1000);
		
		Customer C = app.findCustomer(personID);
		Set<Discount> discounts = C.getDiscounts();
		
		for (Discount D : discounts)
		{
			assertTrue(D.calcDiscount(C) == 25000);
		}
	}
}
