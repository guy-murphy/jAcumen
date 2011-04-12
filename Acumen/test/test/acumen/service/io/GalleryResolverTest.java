/**
 * Author:	Guy J. Murphy
 * Date: May 12, 2010
 * Time: 3:31:43 PM
 * File: GalleryResolverTest.java
 */
package test.acumen.service.io;

import java.io.IOException;
import org.junit.*;

import acumen.service.io.GalleryResolver;
import acumen.service.io.ImageDescriptor;
import acumen.util.AcumenDictionary;

public class GalleryResolverTest {
	
	@Test
	public void constructor () throws IOException {
		GalleryResolver resolver = new GalleryResolver("/home/guy/workspace/Acumen.CMS2", null);
		resolver.setRelativePath("public/repositories/default/images/GuyMurphy");
		AcumenDictionary<String,ImageDescriptor> files = resolver.getFiles();
		files.setPrettyPrint(true);
		System.out.println(files.toXml());
		
		GalleryResolver resolver2 = new GalleryResolver("/home/guy/workspace/Acumen.CMS2", null);
		resolver2.setRelativePath("public/repositories/default/images/TestTopic");
		if (resolver2.exists()) {
			AcumenDictionary<String,ImageDescriptor> files2 = resolver2.getFiles();
			files2.setPrettyPrint(true);
			System.out.println(files2.toXml());
		}
	}

}
