package com.github.serializable.service;

import java.io.Serializable;

/**
 * this is the public product object that will be saved/loaded by the
 * application
 */
public class Product extends Id implements Serializable
{
	private static final long serialVersionUID = 525630403812799901L;
	private String name;
	private String productDescription;
	private double price;

	// default constructor ensures only eCommerce can create new instances
	public Product(String name, String productDescription, double price)
	{
		this.name = name;
		this.productDescription = productDescription;
		this.price = price;
	}

	public String getProductName()
	{
		return name;
	}

	public String getProductDescription()
	{
		return productDescription;
	}

	public double getPrice()
	{
		return price;
	}

	@Override
	public String toString()
	{
		return getId() + " : " + getProductName() + " : " + getProductDescription() + "\n";
	}

}
