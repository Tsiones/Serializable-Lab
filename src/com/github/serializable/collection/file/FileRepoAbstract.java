package com.github.serializable.collection.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;


public abstract class FileRepoAbstract<T>
{
	private File saveDirectory; 
	private int id = 0;
	protected File saveFile;
	protected Set<T> set = new HashSet<>();
	
	public FileRepoAbstract(String directory) throws IOException
	{
		// init variables
		saveDirectory = new File(directory);
		saveFile = new File(directory+"/data");
		// create directory
		if (!saveDirectory.exists())
		{
			if (!saveDirectory.mkdirs()) // for example sending in the directory "#€&&()&=" into the constructor fails to create a dir
			{
				throw new RuntimeException("Failed to create directory");
			}
		}
		if(saveFile.createNewFile())
		{
			requestSave();
		}
	}

	public void requestSave()
	{
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile)))
		{
			out.writeObject(set);
			out.writeInt(id);
		} 
		catch(IOException e)
		{
			e.getMessage();
			e.printStackTrace();		
		}
	}
	
	@SuppressWarnings("unchecked")
	public void readAllToMemory()
	{
		if (set.size() > 0)
		{
			throw new RuntimeException("Data has already been read");
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile)))
		{
			id = ois.readInt();
			set = (Set<T>) ois.readObject(); //reads whole set as object
		}

		// Catch
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
		}
	}
	
	@Override
	public String toString()
	{
		return set.toString();
	}
	
	int nextId()
	{
		return id++;
	}
	
}
