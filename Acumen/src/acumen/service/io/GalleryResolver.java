/**
 * Author:	Guy J. Murphy
 * Date:	May 12, 2010
 * Time:	10:48:49 AM
 * File: GalleryResolver.java
 */
package acumen.service.io;

import acumen.resource.store.IFileStore;

public class GalleryResolver extends BaseRepositoryResolver<ImageDescriptor> {

	// NOTE: The IFileStore parameter is a hangover from the original
	// .NET version of Acumen.CMS and it's no longer clear that there
	// is any actual requirement for the filestore here. I guess one
	// could foresee types of resolver that might need access to the filestore.
	// Or we could add functionality which would use the resolver to
	// handle to pushing of files from the filestore.
	//
	// CONSIDER: Remove IFileStore from the interface.
	public GalleryResolver(String root, IFileStore store) {
		super(root, store);

	}

}
