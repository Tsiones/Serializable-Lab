package com.github.serializable.service.junit;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.github.serializable.passwordvalidation.PasswordRequirmentsNotMet;
import com.github.serializable.service.Order;
import com.github.serializable.service.Product;
import com.github.serializable.service.User;

public class IdObjectTest
{
	@Before
	public void init()
	{
		Global.init();
	}
	
	@Test
	public void checkThatUserGetsIdWhenAdded() throws PasswordRequirmentsNotMet
	{
		User user = Global.generateFreeUser();
		assertEquals(user.getId(), -1);
		Global.eCom.add(user);
		assertNotEquals(user.getId(), -1);
	}
	
	@Test
	public void checkThatProductGetsIdWhenAdded() throws PasswordRequirmentsNotMet
	{
		Product product = Global.generateFreeProduct();
		assertEquals(product.getId(), -1);
		Global.eCom.add(product);
		assertNotEquals(product.getId(), -1);
	}
	
	@Test
	public void checkThatOrderGetsIdWhenAdded() throws PasswordRequirmentsNotMet
	{
		Product product = Global.generateProduct();
		User user = Global.generateUser();
		Order order = Global.generateFreeOrder();
		Global.eCom.tieOrder(order, user);
		order.addProduct(product);
		
		assertEquals(order.getId(), -1);
		Global.eCom.add(order);
		assertNotEquals(order.getId(), -1);
	}

}
