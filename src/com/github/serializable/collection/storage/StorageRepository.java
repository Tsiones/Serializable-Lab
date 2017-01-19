package com.github.serializable.collection.storage;

import java.util.Set;

import com.github.serializable.service.Id;

public interface StorageRepository<T extends Id> 
{
	/**
	 * Creates <tt>unit</tt> with specified type (by implementation) and adds it to the specified repository.
	 * @param <T> unit
	 */
	public T createUnit(T unit);
	/**
	 * Updates by removing existing <tt>unit</tt> and re-adding the <tt>unit</tt> to the repository
	 * Compares argument with internal data by equals(). Keep in mind how your equals() method is
	 * implemented!
	 * @param <T> unit
	 */
	public T updateUnit(T unit);
	/**
	 * Checks if the <tt>unit</tt> exists and removes it from the repository
	 * @param unit
	 */
	public T deleteUnit(T unit);
	/**
	 * Retrieves the <tt>unit</tt> from the repository by looking for matching ID in the repository set
	 * @param unit.getId
	 * @return <T> unit
	 */
	public T getUnitById(int id);
	/**
	 * Retrieves every unit from the specified repository
	 * @return <tt>Set<T></tt> of all units in current repository
	 */
	public Set<T> getAllUnits();
	
	
}