package com.github.serializable.service;

public class Id
{
	private int id = -1;
	
	public int getId()
	{
		return id;
	}
	public Object setId(int id) //so the child object can override and decorate constructor. Check ECommerceServiceTest for user dec.
	{
		this.id = id;
		return this;
	}
	
	public boolean hasId()
	{
		return id >= 0;
	}
	
	@Override
	public int hashCode()
	{
		return getId() * 37;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof Id) 
		{
			Id otherId = (Id) obj;
			if ((hasId() == false) || (otherId.hasId() == false))
			{
				return super.equals(obj);
			}
			else
			{
				return this.getId() == otherId.getId();
			}
		}
		return false;
	}
}
