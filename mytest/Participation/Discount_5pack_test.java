package Participation;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class Discount_5pack_test {

	private void setupDB() {
		Persistence.wipedb() ;
	}
	
	@Test
	public void applicable_ReturnsTrue() 
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
		Set<Discount> discounts = C.getDiscounts();
		
		int value = C.getDiscountValue();
		
		for (Discount D : discounts)
		{
			assertTrue(D.applicable(C));
		}
	}
	
	@Test
	public void applicable_ReturnsFalse() 
	{
		System.out.println("Testing getDiscountValue for Discount_5pack with applicable input");
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
		Set<Discount> discounts = C.getDiscounts();
		
		int value = C.getDiscountValue();
		
		for (Discount D : discounts)
		{
			assertFalse(D.applicable(C));
		}
	}
	
	@Test
	public void calcDiscount_test()
	{
		System.out.println("Testing calcDiscount() for Discount_5pack with applicable input");
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
		Set<Discount> discounts = C.getDiscounts();
		
		for (Discount D : discounts)
		{
			assertTrue(D.calcDiscount(C) == 10000);
		}
	}
}
