package com.github.serializable;

import java.io.IOException;

import com.github.serializable.collection.file.FileRepository;
import com.github.serializable.collection.storage.StorageRepository;
import com.github.serializable.service.ECommerceService;
import com.github.serializable.service.Order;
import com.github.serializable.service.Product;
import com.github.serializable.service.User;


/*STATUS: 29/10-15 01:50
 * Endast via EComService får vi säga åt FileRepository att spara till minnet. 
 * Anledning för icke-tomma Repository/Xxx/data filer är pga att en tom HashSet läggs
 *  på disk automatiskt (via klass implementationerna, FileXxxRepository).
 * 
 * TODO: 
 * - finish eComService methods
 * - User/Product/Order.class not dependant on ID. FileRepository assigns ID upon creation to file
 * - check TODOs on Tasks window (Window->Show View->Tasks)
 * 	
 */

public final class Main
{

	public static void main(String[] args)
	{
		StorageRepository<User> userRep = null;
		StorageRepository<Product> productRep = null;
		StorageRepository<Order> orderRep = null;
		try
		{
			// create file repositories
			userRep = new FileRepository<>("Repository/User/");
			orderRep = new FileRepository<>("Repository/Order/");
			productRep = new FileRepository<>("Repository/Product/");
		}
		catch (IOException e)
		{
			System.err.println("Failed to create files properly");
			e.printStackTrace();
		}
		ECommerceService eCom = new ECommerceService(userRep, productRep, orderRep);
		
		
		System.out.println(eCom);
	}

}
