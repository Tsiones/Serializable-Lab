package com.github.serializable.collection.file;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.github.serializable.collection.storage.StorageRepository;
import com.github.serializable.service.Id;

/**
 * handles order objects by saving/loading them to files using serialization
 */
public class FileRepository<T extends Id> extends FileRepoAbstract<T> implements StorageRepository<T>
{	
	
	public FileRepository(String directory) throws IOException
	{
		super(directory);
	}

	
	@Override
	public T createUnit(T unit)
	{
		if(!unit.hasId())
		{
			unit.setId(nextId());
		}
		if(set.add(unit))
		{
			return unit;
		}
		return null;
	}

	@Override
	public T updateUnit(T unit)
	{
		if (set.contains(unit))
		{
			set.remove(unit);
			set.add(unit);
			return unit;
		}
		return null;
	}

	@Override
	public T deleteUnit(T unit)
	{
		if(set.remove(unit))
		{
			return unit;
		}
		return null;
	}
	
	@Override
	public T getUnitById(int id)
	{
		for(T unit : set)
		{
			if(unit.getId() == id)
			{
				return unit;
			}
		}
		return null;
	}
	
	@Override
	public Set<T> getAllUnits()
	{
		return new HashSet<T>(set);
	}
}
