/* Author:	Guy J. Murphy
 * Date:	May 11, 2010
 * Time:	3:40:39 PM
 * File: FileDescriptorTest.java
 */
package test.acumen.service.io;

import static junit.framework.TestCase.assertEquals;
import java.io.IOException;

import org.junit.*;

import acumen.service.io.FileDescriptor;

public class FileDescriptorTest {
	
	private void _testImage1 (FileDescriptor descriptor) {
		assertEquals(descriptor.getName(), "3416767438_cca015f723");
		assertEquals(descriptor.getExtension(), ".jpg");
		assertEquals(descriptor.getOwner(), "Guy Murphy");
		assertEquals(descriptor.getSize(), 74.0);
		assertEquals(descriptor.getDescription(), "fuck you");
	}
	
	@Test
	public void constructor () throws IOException {
		FileDescriptor descriptor = new FileDescriptor("/home/guy/workspace/Acumen.CMS2/public/repositories/default/images/GuyMurphy/3416767438_cca015f723.jpg");
		_testImage1(descriptor);
		System.out.println(descriptor.toXml());
	}
	
	@Test
	public void setFullPath () throws IOException {
		FileDescriptor descriptor = new FileDescriptor("/home/guy/workspace/Acumen.CMS2/public/repositories/default/images/GuyMurphy/3416767438_cca015f723.jpg");
		_testImage1(descriptor);
		System.out.println(descriptor.toXml());
		descriptor.setFullPath("/home/guy/workspace/Acumen.CMS2/public/repositories/default/images/GuyMurphy/3k23o33l11fd12214791g9e5656bfc903152d.jpg");
		System.out.println(descriptor.toXml());
		assertEquals(descriptor.getName(), "3k23o33l11fd12214791g9e5656bfc903152d");
		assertEquals(descriptor.getExtension(), ".jpg");
		assertEquals(descriptor.getOwner(), "Guy Murphy");
		assertEquals(descriptor.getSize(), 18.0);
		assertEquals(descriptor.getDescription(), "more");
	}

}
