/**
 * User: Guy J. Murphy
 * Date: Jan 24, 2010
 * Time: 1:14:25 PM
 * File: MySqlFileStoreTest.java
 */
package test.acumen.resource.store;

import static junit.framework.Assert.assertNotNull;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.*;

import acumen.resource.store.MySqlFileStore;
import acumen.data.StoreException;
import acumen.resource.model.*;

/**
 * @author Guy J. Murphy
 */
public class MySqlFileStoreTest {
	
	private MySqlFileStore _store;

    public MySqlFileStoreTest () throws StoreException {
        _reset();
    }

    private void _reset() throws StoreException {
        if (_store != null) {
            if (_store.isOpen()) {
                _store.stop();
            }
        }
        _store = new MySqlFileStore("leviathan.acumen.es", "acumen_london", "root", "redacted");
    }
    
    @Test
    public void problemWithNullCharacterInsertionInResources () throws StoreException, ResourceException {
    	MySqlFileStore store = new MySqlFileStore("leviathan.acumen.es", "acumen", "root", "redacted");
    	try {
    		store.start();
    		Resource resource = store.getMostRecentInstance("DevWiki", "TestTopic");
    		resource.setStringData(resource.getStringData());
    		store.updateResource(resource);
    		resource = store.getMostRecentInstance("DevWiki", "TestTopic");
    		System.out.println(String.format("***%s***", resource.getStringData()));
    	} finally {
    		store.stop();
    	}
    }
    
    @Test
    public void getMostRecentInstance () throws StoreException {
    	_reset();
    	try {
    		_store.start();
    		Resource resource = _store.getMostRecentInstance("DevWiki","CharitySite.chy");
    		assertNotNull(resource);
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test
    public void createResourceFromFileTest() throws StoreException {
    	_reset();
    	try {
    		_store.start();
    		try {
				_store.createResourceFromFile("images", "Firefox_wallpaper.png", "Daniel", "png", "/home/daniel/Firefox_wallpaper.png");
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//assertNotNull(null);
    	} finally {
    		_store.stop();
    	}
    }
    @Test
    public void createResourceFromFileTestWithMetadata() throws StoreException {
    	_reset();
    	try {
    		_store.start();
    		try {
    			ResourceMetadata metadata = new ResourceMetadata();
    			metadata.put("Description","Test metadata description");
    			String file = loadFile();
    			
    			//String nameSpace, String userId, String path, ResourceMetadata metadata
				_store.createResourceFromFile("images", "Daniel", file,metadata);
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//assertNotNull(null);
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test
    public void createResourceWithByte() throws StoreException, FileNotFoundException {
    	_reset();
    	try {
    		_store.start();
    		try {
    			String filePath = loadFile();
    			String[] dir_name = filePath.split("[/]");
    			byte[] data;
    			FileInputStream file = new FileInputStream(filePath);
    			int size = file.available();
    			data = new byte[size];
    			file.read(data);
    			
    			ResourceMetadata metadata = new ResourceMetadata();
    			metadata.put("Owner","Daniel");
    			metadata.put("Description","Test metadata description");
    			
    			//Resource resource = _store.getMostRecentInstance("images","test/careers/Firefox_wallpaper.png");
    			//Images
    			//Resource resource = _store.getLockedInstance("images","chy/home/Firefox_wallpaper.png");
    			//Attach
    			Resource resource = _store.getLockedInstance("attachments","chy/donations/SB-2010-01_ES.pdf");
    			//resource = @store.getMostRecentInstance(nameSpace,resource_name);
    			if (resource != null){	
	    	        if (resource.getLockedBy() != null ){
	    	          resource = _store.updateResource("attachments","chy/donations/SB-2010-01_ES.pdf", "Daniel", data, resource.getMetadata());
	    	        }
	    	        else {
	    	          resource = _store.openResourceForUpdate("attachments","chy/donations/SB-2010-01_ES.pdf", "Daniel");
	    	          if (resource !=null){
	    	            resource.setData(data);
	    	            resource = _store.updateResource(resource);
	    	          }
	    	        }
    			}
	    	    else  {
//	    	    	resource = _store.getLockedInstance("images","test/careers/Firefox_wallpaper.png");
//	    	    	if resource != null
	    	    	resource = _store.createResource("attachments", "chy/donations/SB-2010-01_ES.pdf", "Daniel", "png", data,metadata);	    	    		    	    		    	            	    	           
	    	    }	   		
	    	        
    			
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test  
    public void createResourceWithoutNameSpace() throws StoreException, FileNotFoundException {
    	_reset();
    	try {
    		_store.start();
    		try {
    			ResourceMetadata metadata = new ResourceMetadata();
    			metadata.put("Description","Test metadata description");
    			
    			String filePath = loadFile();
    			String[] dir_name = filePath.split("[/]");
    			byte[] data;
    			FileInputStream file = new FileInputStream(filePath);
    			int size = file.available();
    			data = new byte[size];
    			file.read(data);
    			//String name, String userId, String fileType, byte[] data, ResourceMetadata metadata
				_store.createResource(dir_name[3] ,"Daniel", "png", data, metadata);
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test  
    public void createResourceString() throws StoreException, FileNotFoundException {
    	_reset();
    	try {
    		_store.start();
    		try {
    			
    			String data = "TestCreateStringResource";
    			//String nameSpace, String name, String userId, String fileType, String data
				_store.createResource("DevWiki", "TestString" ,"Daniel", "wiki", data);
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} finally {
    		_store.stop();
    	}
    }    
    
    @Test  
    public void markResourceAsDeletedAndRemove() throws StoreException {
    	_reset();
    	try {
    		_store.start();
    		try {
    			    			
    			//String nameSpace, String name, String userId, String fileType, String data
				_store.markResourceAsDeleted("images","chy/home/Firefox_wallpaper.png","Daniel");
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} finally {
    		_store.stop();
    	}
    }      
        
    @Test  
    public void updateMetaData() throws StoreException {
    	_reset();
    	try {
    		_store.start();
    		try {
    			String filePath = loadFile();
    			String[] dir_name = filePath.split("[/]");
    			byte[] data;
    			FileInputStream file = new FileInputStream(filePath);
    			int size = file.available();
    			data = new byte[size];
    			file.read(data);
    			
    			ResourceMetadata metadata = new ResourceMetadata();
    			metadata.put("Owner","Daniel");
    			metadata.put("Description","Test metadata description111");
    			
    			Resource resource = _store.getLockedInstance("images","chy/home/Firefox_wallpaper.png");
    			
    			if (resource == null){		    	    	
		    		//resource = _store.getMostRecentInstance("images","test/careers/Firefox_wallpaper.png");
		    		resource = _store.openResourceForUpdate("images","chy/home/Firefox_wallpaper.png", "Daniel");
		            
		            String metadata1;
		            
		            metadata1 = resource.getMetadata().toString();
		            
		            //Date date = new Date();
		            //resource.setCommited(date);
		            resource.setMetadata(metadata);
		            //resource.setLockedBy(null);
		            
		            //_store.addResource(resource);
		            _store.updateResource(resource);
		            
		            @SuppressWarnings("unused")
					byte[] byteimage = resource.getData();
		            
		            ResourceMetadata metadata2 = new ResourceMetadata();
		            metadata2 = resource.getMetadata();		    				

    			} else{
//	    	        if (resource.getLockedBy().equals("DELETED")){
//	    	          resource = _store.updateResource("images","test/careers/Firefox_wallpaper.png", "Daniel", data);
//	    	        }
//	    	        else {
//	    	          resource = _store.openResourceForUpdate("images","test/careers/Firefox_wallpaper.png", "Daniel");
//	    	          if (resource !=null){
//	    	            resource.setData(data);
//	    	            resource = _store.updateResource(resource);
//	    	          }
//	    	        }    				
    			}
	    	        
    			
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} finally {
    		_store.stop();
    	}
    }       
    
	private String loadFile() {
		Frame fr=new Frame();
		fr.setVisible(true);
		FileDialog fd=new FileDialog(fr,"Select file",FileDialog.LOAD);
		fd.toFront();
		fr.dispose();
		fd.setVisible(true);
		String path =fd.getDirectory();
		String name =fd.getFile();
	  return path + name;
	  }
	
}
