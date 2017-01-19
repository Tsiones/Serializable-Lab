package com.github.serializable.service;

import java.util.HashSet;
import java.util.Set;

import com.github.serializable.collection.storage.StorageRepository;
import com.github.serializable.exceptions.PriceOutOfBoundsException;
import com.github.serializable.exceptions.ServiceException;
import com.github.serializable.passwordvalidation.PasswordRequirmentsNotMet;
import com.github.serializable.passwordvalidation.PasswordValidationService;
import com.github.serializable.passwordvalidation.PasswordValidator;
import com.github.serializable.passwordvalidation.validators.DefaultPasswordValidator;

/**
 * the main ECommerce object, handles order/product/users and checks that all is
 * fine, then saves using the selected repository type
 *
 * TODO: rewrite whole ECommerce. - all other files are complete as it is only
 * the eCommerce class that needs to handle logics -
 *
 */
public class ECommerceService
{
	private StorageRepository<User> userRep;
	private StorageRepository<Product> prodRep;
	private StorageRepository<Order> orderRep;

	private PasswordValidationService passwordValidator;

	public static final int MAX_USERNAME_LENGTH = 30;
	public static final int MAX_COST = 50000;

	public ECommerceService(StorageRepository<User> userRep, StorageRepository<Product> prodRep, StorageRepository<Order> orderRep)
	{
		this.userRep = userRep;
		this.prodRep = prodRep;
		this.orderRep = orderRep;
		changePasswordValidator(new DefaultPasswordValidator());
	}

	public void changePasswordValidator(PasswordValidator passwordValidater)
	{
		passwordValidator = new PasswordValidationService(passwordValidater);
	}

	// --Users
	public User add(User user) throws PasswordRequirmentsNotMet
	{
		if (user.getUsername().length() > MAX_USERNAME_LENGTH)
		{
			throw new IllegalArgumentException("Cannot contain more than " + MAX_USERNAME_LENGTH + " characters");
		}
		passwordValidator.validate(user.getPassword()); // will throw exceptions
														// if password
														// requirments arent met
		if(userRep.getUnitById(user.getId()) == null)
		{
			return userRep.createUnit(user);
		}
		return userRep.updateUnit(user);
	}

	public void addUserSet(Set<User> userSet) throws PasswordRequirmentsNotMet
	{
		if(userSet.isEmpty())
		{
			throw new IllegalArgumentException("Cannot instantiate over empty set!");
		}
		for(User user : userSet)
		{
			add(user);
		}
	}

	/**
	 * Adds the order to the users internal list of orderId's
	 * @param order
	 * @param user
	 */
	public void tieOrder(Order order, User user)
	{
		user.addOrder(order);
	}
	// --Orders
	public Order add(Order order)
	{
//
		//Order should be created without user, and the eCom is tasked to tie an order without owner to a user.
//		// Order with no user ID
//		if (order.getBuyerId() == -1)
//		{
//			throw new InvalidIDException("Order is missing a valid buyer ID");
//		}

		// Order with empty set of products
		if (order.productIdSet == null || order.productIdSet.isEmpty())
		{
			throw new IllegalArgumentException("Order must contain atleast one product");
		}

		// Order value over 50k
		if (order.getTotalCost() >= MAX_COST)
		{
			throw new PriceOutOfBoundsException("Order price must be under " + MAX_COST);
		}
		
		if(orderRep.getUnitById(order.getId()) == null)
		{
			return orderRep.createUnit(order);
		}
		//if order already exists we shall update it with passed argument
		return orderRep.updateUnit(order);
	}

	public void addOrderSet(Set<Order> orderSet)
	{
		if(orderSet.isEmpty())
		{
			throw new IllegalArgumentException("Cannot instantiate over empty set!");
		}
		for(Order order : orderSet)
		{
			add(order);
		}
	}

	// --Products
	public Product add(Product product)
	{
		if (product.getProductName() == null || product.getProductName().isEmpty())
		{
			throw new IllegalArgumentException("Product name must not be empty or null");
		}

		if (product.getProductDescription() == null || product.getProductDescription().isEmpty())
		{
			throw new IllegalArgumentException("Product description must not be empty or null");
		}

		if (product.getPrice() <= 0 || product.getPrice() >= MAX_COST)
		{
			throw new PriceOutOfBoundsException("Price must be more than 0.0 or under " + MAX_COST);
		}
		if(prodRep.getUnitById(product.getId()) == null)
		{
			return prodRep.createUnit(product);
		}
		return prodRep.updateUnit(product);
	}

	/**Verifies and proceeds to add <tt>Set</tt> of <tt>Product</tt> to the appropriate repository.
	 * @param prodSet
	 */
	public void addProdSet(Set<Product> prodSet)
	{
		if(prodSet.isEmpty())
		{
			throw new IllegalArgumentException("Cannot instantiate over empty set!");
		}
		for(Product product : prodSet)
		{
			add(product);
		}
	}
	
	public User getUserById(int id)
	{
		User userInStorage = userRep.getUnitById(id);
		if(userInStorage == null)
		{
			throw new ServiceException("ID doesn't match any existing unit!");
		}
		return userInStorage;
	}
	
	public Product getProductById(int id)
	{
		Product productInStorage = prodRep.getUnitById(id);
		if(productInStorage == null)
		{
			throw new ServiceException("ID doesn't match any existing unit!");
		}
		return productInStorage;
	}
	
	public Order getOrderById(int id)
	{
		Order orderInStorage = orderRep.getUnitById(id);
		if(orderInStorage == null)
		{
			throw new ServiceException("ID doesn't match any existing unit!");
		}
		return orderInStorage;
	}
	
	public void remove(User user)
	{
		if(userRep.getUnitById(user.getId()) == null)
		{
			throw new ServiceException("Specified user does not exist!");
		}
		userRep.deleteUnit(user);
	}
	
	public void remove(Product product)
	{
		if(prodRep.getUnitById(product.getId()) == null)
		{
			throw new ServiceException("Specified product does not exist!");
		}
		prodRep.deleteUnit(product);
	}
	
	public void remove(Order order)
	{
		if(orderRep.getUnitById(order.getId()) == null)
		{
			throw new ServiceException("Specified order does not exist!");
		}
		orderRep.deleteUnit(order);
	}

	/**
	 * 
	 * By retrieving the buyer id of each order in the orderRep, consolidate
	 * every assigned order and return a new list containing every order for the
	 * user in argument
	 * 
	 * So by example:
	 * <p>
	 * {@code ECommerceService eCom = new ECommerceService(..., ..., ...) }
	 * <p>
	 * {@code User user = new User(); }
	 * <p>
	 * {@code Order order1 = new Order(user); }
	 * <p>
	 * {@code Order order2 = new Order(user); }
	 * <p>
	 * {@code Order order3 = new Order(user); }
	 * <p>
	 * {@code eCom.add(order1); eCom.add(order2); eCom.add(order3); }
	 * <p>
	 * {@code eCom.getAllOrders(user) //<- returns hashSet }
	 * 
	 * @param user
	 * @return new set of Order objects by getting every order object associated
	 *         with user
	 */
	public Set<Order> getAllOrders(User user)
	{
		if(user.getOrderIdSet().isEmpty())
		{
			throw new ServiceException("User " + user.toString() + " has no orders on record");
		}
		Set<Order> orders = new HashSet<>();
		for(int orderId : user.getOrderIdSet())
		{
			orders.add(orderRep.getUnitById(orderId));
		}
		return orders;
	}
	
	public Set<Product> getAllProducts()
	{
		return prodRep.getAllUnits();
	}
	
	public Set<User> getAllUsers()
	{
		return userRep.getAllUnits();
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Users:");
		sb.append(userRep);
		sb.append("\n");

		sb.append("Orders:");
		sb.append(orderRep);
		sb.append("\n");

		sb.append("Products:");
		sb.append(prodRep);
		sb.append("\n");

		return sb.toString();
	}

}
