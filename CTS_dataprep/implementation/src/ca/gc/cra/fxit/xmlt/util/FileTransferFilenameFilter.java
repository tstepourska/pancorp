package ca.gc.cra.fxit.xmlt.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * FileTransferFilenameFilter
 *               is a file list filter, that is used with the <code>list</code> or
 *               <code>listFiles</code> method of Java class <code>File</code> to
 *               retrieve only files having certain filenames.
 *               <br>
 */
public class FileTransferFilenameFilter implements FilenameFilter
{
	String regex;

	/**
	 * Constructor
	 *
	 * @param regex  regular expression for filename 
	 */
	public FileTransferFilenameFilter( String regex )
	{
		this.regex = regex;
	}

	/**
	 * Tests if a specified file should be included in a file list
	 *
	 * @param dir  the directory in which the file was found
	 * @param name the name of the file
	 * @return     <code>true</code> if and only if the name of the file matches the regular expression; <code>false</code> otherwise
	 */
	@Override
	public boolean accept(File dir, String name){
		return (name.matches(regex));
	}
}