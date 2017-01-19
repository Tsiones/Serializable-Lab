package com.github.serializable.service.junit;

import java.io.IOException;
import com.github.serializable.collection.file.FileRepository;
import com.github.serializable.collection.storage.StorageRepository;
import com.github.serializable.passwordvalidation.PasswordRequirmentsNotMet;
import com.github.serializable.service.ECommerceService;
import com.github.serializable.service.Order;
import com.github.serializable.service.Product;
import com.github.serializable.service.User;

public class Global // not a JUnit test file
{
	static ECommerceService eCom;
	
	public static void init()
	{
		StorageRepository<User> userRep = null;
		StorageRepository<Product> productRep = null;
		StorageRepository<Order> orderRep = null;
		try
		{
			// create file repositories
			userRep = new FileRepository<>("TestRepository/User/");
			orderRep = new FileRepository<>("TestRepository/Order/");
			productRep = new FileRepository<>("TestRepository/Product/");
		}
		catch (IOException e)
		{
			System.err.println("Failed to create files properly");
			e.printStackTrace();
		}
		Global.eCom = new ECommerceService(userRep, productRep, orderRep);
	}
	
	public static char generateLetter(boolean capital)
	{
		if (capital)
			return (char) (65+Math.random()*(90-65));
		else
			return (char) (97+Math.random()*(122-97));
	}
	
	public static String generateString()
	{
		return generateString(10);
	}
	
	public static String generateString(int letters)
	{
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<letters;i++)
		{
			sb.append(generateLetter(Math.random()<0.5?true:false));
		}
		return sb.toString();
	}
	
	public static Product generateFreeProduct()
	{
		return new Product(generateString(), generateString(), Math.random()*1000);
	}
	
	public static Product generateProduct()
	{
		Product product = generateFreeProduct();
		Global.eCom.add(product);
		return product;
	}
	
	public static User generateFreeUser()
	{
		return new User(generateString(), "A12_" + generateString(), generateString());
	}
	
	public static User generateUser()
	{
		User user = generateFreeUser();
		try
		{
			Global.eCom.add(user);
		}
		catch (PasswordRequirmentsNotMet e)
		{
			System.err.println("Failed to generate proper password for global basic user pbject");
			e.printStackTrace();
		}
		return user;
	}
	
	public static Order generateFreeOrder()
	{
		return new Order();
	}
	
	public static Order generateOrder(User user)
	{
		Order order = generateFreeOrder();
		Global.eCom.add(order);
		return order;
	}
	
	public static Order generatePopulatedOrder(Product product)
	{
		Order order = new Order();
		order.addProduct(product);
		Global.eCom.add(order);
		return order;
	}
	
	public static Order generatePopulatedOrder(Product[] products)
	{
		Order order = new Order();
		for (Product product : products)
		{
			order.addProduct(product);
		}
		Global.eCom.add(order);
		return order;
	}
}
