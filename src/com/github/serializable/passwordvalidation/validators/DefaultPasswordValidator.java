package com.github.serializable.passwordvalidation.validators;

import com.github.serializable.passwordvalidation.PasswordRequirmentsNotMet;
import com.github.serializable.passwordvalidation.PasswordValidator;

/**
 * @author EnderCrypt
 * this is the default password validator, all password validators need to implement the
 * PasswordValidator interface
 */
public class DefaultPasswordValidator implements PasswordValidator
{
	private int capitalLetters; // count of capitalLetters
	private int numbers; // count of numbers
	private int special; // count of special characters
	
	
	@Override
	public void reset()
	{
		capitalLetters = 0;
		numbers = 0;
		special = 0;
	}

	@Override
	public void register(char c) throws PasswordRequirmentsNotMet // http://www.asciitable.com/
	{
		if ((c < 32) || (c > 126))
		{
			throw new PasswordRequirmentsNotMet("Contains illegal character");
		}
		if (Character.isUpperCase(c))
		{
			capitalLetters++;
		}
		if (Character.isDigit(c))
		{
			numbers++;
		}
		if ( ((c >= 33) && (c <= 47)) || ((c >= 58) && (c <= 64)) || ((c >= 91) && (c <= 96)) || ((c >= 123) && (c <= 126)) )
		{
			special++;
		}
	}

	@Override
	public void validate() throws PasswordRequirmentsNotMet
	{
		if (capitalLetters < 1)
		{
			throw new PasswordRequirmentsNotMet("Does not have atleast one capital letter");
		}
		if (numbers < 2)
		{
			throw new PasswordRequirmentsNotMet("Does not have atleast two numbers");
		}
		if (special < 1)
		{
			throw new PasswordRequirmentsNotMet("Does not have atleast one special character");
		}
	}

}
