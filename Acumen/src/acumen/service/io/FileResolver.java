/* Author:	Guy J. Murphy
 * Date:	May 11, 2010
 * Time:	3:40:39 PM
 * File:	FileResolver.java
 */

package acumen.service.io;

import java.io.File;
import java.io.IOException;

public class FileResolver {
	private String _root;
	private String _relativePath;
	private File _file;
	
	public FileResolver (String root) {
		_root = root;
	}
	
	public String getRoot () {
		return _root;
	}
	
	public String setRoot (String root) {
		_file = null;
		_root = root;
		return _root;
	}
	
	public String getRelativePath () {
		return _relativePath;
	}
	
	public String setRelativePath (String path) {
		_file = null;
		_relativePath = path;
		return _relativePath;
	}
	
	public String getFullPath () {
		if (this.getRelativePath() != null) {
			return this.getRoot() + "/" + this.getRelativePath();
		} else {
			return this.getRoot();
		}
	}
	
	public File getFile () throws IOException {
		if (_file == null) {
			_file = new File(this.getFullPath());
		}
		return _file;
	}
	
	public boolean exists () throws IOException {
		return this.getFile().exists();
	}
	
	public void remove () throws IOException {
		this.getFile().delete();
	}
	
	public void create () throws IOException {
		this.getFile().createNewFile();
	}
	
}
