/**
 * Author:	Guy J. Murphy
 * Date:	May 12, 2010
 * Time:	10:48:49 AM
 * File: RepositoryResolver.java
 */
package acumen.service.io;

import acumen.resource.store.IFileStore;

public abstract class BaseRepositoryResolver<T extends FileDescriptor> extends DirectoryFileResolver<T> {
	
	private IFileStore _store;

	public BaseRepositoryResolver(String root, IFileStore store) {
		super(root);
		_store = store;
	}
	
	public IFileStore getStore () {
		return _store;
	}

}
