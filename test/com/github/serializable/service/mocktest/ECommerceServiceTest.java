package com.github.serializable.service.mocktest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.IsEqual.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import com.github.serializable.collection.storage.StorageRepository;
import com.github.serializable.passwordvalidation.PasswordRequirmentsNotMet;
import com.github.serializable.service.ECommerceService;
import com.github.serializable.service.Order;
import com.github.serializable.service.Product;
import com.github.serializable.service.User;

@RunWith(MockitoJUnitRunner.class)
public class ECommerceServiceTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock(name="userRep")
	private StorageRepository<User> userRepMock;
	@Mock(name="orderRep")
	private StorageRepository<Order> orderRepMock;
	@Mock(name="prodRep")
	private StorageRepository<Product> prodRepMock;
	
	@InjectMocks
	private ECommerceService eCom = new ECommerceService(userRepMock, prodRepMock, orderRepMock);
	private String validUsername = "JohnDoe";
	private String validPassword = "A_12";
	private String validEmail = "john@doe.anon";
	//valid input objects
	private Product validProduct = new Product("fallout4", "sämsta spelet euw", 10.0);
	private User validUser = new User(validUsername, validPassword, validEmail).setId(0);
	
	
	//password requirements:
	//at least 1 capital letter, at least 2 numbers and at least 1 special char
	@Test
	public void passwordMeetsRequirementsNumbers() throws PasswordRequirmentsNotMet
	{
		//1 capital (check), 1 special (check) but not 2 numbers (!!!)
		User userNoNumbers = new User(validUsername, "A_", validEmail);
		thrown.expect(PasswordRequirmentsNotMet.class);
		thrown.expectMessage(equalTo("Does not have atleast two numbers"));
		eCom.add(userNoNumbers);
	}
	
	@Test
	public void passwordMeetsRequirementsCapital() throws PasswordRequirmentsNotMet
	{
		User userNoCapital = new User(validUsername, "a_", validEmail);
		thrown.expect(PasswordRequirmentsNotMet.class);
		thrown.expectMessage(equalTo("Does not have atleast one capital letter"));
		eCom.add(userNoCapital);
	}
	
	@Test
	public void passwordMeetsRequirementsSpecial() throws PasswordRequirmentsNotMet
	{
		User userNoSpecial = new User("JohnDoe", "A12", validEmail);
		thrown.expect(PasswordRequirmentsNotMet.class);
		thrown.expectMessage(equalTo("Does not have atleast one special character"));
		eCom.add(userNoSpecial);
	}
	
	@Test
	public void usernameContainsWithinBounds() throws PasswordRequirmentsNotMet
	{
		User userLongUsername = new User("qoiheqoijencqebvqbuequevoqwveou", validPassword, validEmail);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(equalTo("Cannot contain more than " + ECommerceService.MAX_USERNAME_LENGTH + " characters"));
		eCom.add(userLongUsername);
	}
	
	@Test
	public void canCreateNewProduct()
	{
		/*	currently, IDs are not assigned because the classes that handles assignment is mocked
			mocked classes only retain non-embodied methods (in this case we mock an interface)
			therefore getId() will always be -1 but that will do for now */
		
		//setup mock
		//ensure that when we call prodRepMock.getUnitById, null will be returned because
		//null ensures the object has not already been stored and only through null shall 
		//eCom call prodRep.createUnit
		when(prodRepMock.getUnitById(validProduct.getId())).thenReturn(null);
		eCom.add(validProduct); 
		
		when(prodRepMock.getUnitById(validProduct.getId())).thenReturn(validProduct);
		Product prodInStorage = eCom.getProductById(validProduct.getId());

		//assert equality
		assertThat(prodInStorage, equalTo(validProduct));
		//ensure eCom invokes/calls the createUnit and getUnitById methods on the interface
		verify(prodRepMock).createUnit(validProduct);
		//this needs to verify a call twice due to eCom.add AND eCom.getProductId both calls for this method
		verify(prodRepMock, times(2)).getUnitById(validProduct.getId()); 
	}
	
	@Test
	public void canDeleteProduct()
	{
		when(prodRepMock.getUnitById(validProduct.getId())).thenReturn(validProduct);
		eCom.remove(validProduct);
		
		//verify that the productRepository has its method deleteUnit called
		verify(prodRepMock).deleteUnit(validProduct);
	}
	
	@Test
	public void canUpdateProduct()
	{
		//setup mock for initial creation of validProduct
		when(prodRepMock.getUnitById(validProduct.getId())).thenReturn(null);
		eCom.add(validProduct);
		
		assertThat(validProduct.getPrice(), equalTo(10.0));
		
		//verify creation of first object
		verify(prodRepMock).createUnit(validProduct);
		
		//recreate validProduct with new arguments (15.0 as price than 10.0 as prior)
		validProduct = new Product(validProduct.getProductName(), validProduct.getProductDescription(), 15.0);
		when(prodRepMock.getUnitById(validProduct.getId())).thenReturn(validProduct);
		when(prodRepMock.updateUnit(validProduct)).thenReturn(validProduct);
		Product updatedProduct = eCom.add(validProduct);//calls updateUnit
		
		assertThat(updatedProduct.getPrice(), equalTo(15.0));
		
		//verify eCom called on updateUnit because unit was expected to already have been created
		verify(prodRepMock).updateUnit(validProduct);
	}
	@Test
	public void canRetrieveAllProducts()
	{
		Set<Product> prodSet = new HashSet<>();
		prodSet.add(validProduct);
		eCom.addProdSet(prodSet);
		//initial verification
		verify(prodRepMock).createUnit(validProduct);
		
		//mock initiate: prodRep will return prodSet
		when(prodRepMock.getAllUnits()).thenReturn(prodSet);
		for(Product product : eCom.getAllProducts())
		{
			assertThat(product, equalTo(validProduct));
		}
		
		verify(prodRepMock).getAllUnits();
	}
	
	@Test
	public void canRetrieveAllOrdersFromUser()
	{
		/*
		 * work-flow:
		 * - add an order to eCommerce, check that it is indeed added.
		 * - retrieve an order from eCommerce (through repository) by telling the mocked repository to return an object.
		 * - call method eCom.getAllOrders(validUser) and ensure it returns a validOrder through orderRep.getUnitByID()
		 */
		Order validOrder = new Order();
		validOrder.addProduct(validProduct);
		eCom.add(validOrder);
		eCom.tieOrder(validOrder, validUser);
		
		//initial verification
		verify(orderRepMock).createUnit(validOrder);
				
		//mock setup: whenever a method (that uses validOrder's ID) is invoked, return validOrder.
		//anyInt() due to the vast number of orderIds a user can have
		when(orderRepMock.getUnitById(anyInt())).thenReturn(validOrder);

		assertTrue(eCom.getAllOrders(validUser).contains(validOrder));
		assertTrue(validOrder.getProductIdSet().contains(validProduct.getId()));
		
		//verify that eCom gets the correct data from the orderRepMock.
		//1st invocation by eCom.add(), 2nd by eCom.getAllOrders()
		verify(orderRepMock, times(2)).getUnitById(anyInt());
	}

}
