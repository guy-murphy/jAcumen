/**
 * User: guy
 * Date: May 12, 2010
 * Time: 1:54:14 PM
 * File: IFileStore.java
 */
package acumen.resource.store;

import java.nio.charset.Charset;
import java.util.List;

import acumen.data.StoreException;
import acumen.resource.model.IResource;
import acumen.resource.model.Resource;
import acumen.resource.model.ResourceException;
import acumen.resource.model.ResourceMetadata;
import acumen.util.AcumenDictionary;
import acumen.util.AcumenList;

/**
 * @author guy
 */
public interface IFileStore {

	public abstract Charset getEncoding();

	public abstract void setEncoding(Charset encoding);

	public abstract void setNamespace(String namespace);

	public abstract String getNamespace();

	public abstract int getSize() throws StoreException;

	public abstract String[] getIds() throws StoreException;

	public abstract String[] getResourceNames() throws StoreException;

	public abstract Resource[] getResources() throws StoreException;

	public abstract Resource[] getStubs() throws StoreException;

	public abstract boolean resourceInstanceExists(String id)
			throws StoreException;

	public abstract boolean resourceExists(String nameSpace, String name)
			throws StoreException;

	public abstract boolean resourceExists(String name) throws StoreException;

	public abstract void addResource(IResource resource) throws StoreException;

	public abstract void removeResourceInstance(String id)
			throws StoreException;

	public abstract void removeResource(String name) throws StoreException;

	public abstract void removeResource(String nameSpace, List<String> names);

	public abstract void removeResource(String nameSpace, String... names)
			throws StoreException;

	public abstract void removeResource(String nameSpace, String name)
			throws StoreException;

	public abstract Resource getResourceInstance(String id)
			throws StoreException;

	public abstract Resource getLockedInstance(String name)
			throws StoreException;

	public abstract Resource getLockedInstance(String nameSpace, String name)
			throws StoreException;

	public abstract Resource getPreviousLockedInstance(String name)
			throws StoreException;

	public abstract Resource getPreviousLockedInstance(String nameSpace,
			String name) throws StoreException;

	public abstract boolean isLocked(String name) throws StoreException;

	public abstract boolean isLocked(String nameSpace, String name)
			throws StoreException;

	public abstract Resource getMostRecentInstance(String name)
			throws StoreException;

	public abstract int getInstanceCountFor(String nameSpace, String name)
			throws StoreException;

	public abstract boolean hasInstances(String nameSpace, String name)
			throws StoreException;

	public abstract AcumenList<Resource> getInstancesFor(String nameSpace,
			String name) throws StoreException;

	public abstract Resource getMostRecentInstance(String nameSpace, String name)
			throws StoreException;

	public abstract Resource getOriginalInstance(String name)
			throws StoreException;

	public abstract Resource getOriginalInstance(String nameSpace, String name)
			throws StoreException;

	public abstract AcumenDictionary<String, Resource> getMostRecentForNamespace(
			String nameSpace) throws StoreException;

	public abstract Resource createResource(String nameSpace, String name,
			String userId, String fileType, byte[] data,
			ResourceMetadata metadata) throws ResourceException, StoreException;

	public abstract Resource createResource(String name, String userId,
			String fileType, String data) throws StoreException,
			ResourceException;

	public abstract Resource createResource(String nameSpace, String name,
			String userId, String fileType, String data) throws StoreException,
			ResourceException;

	public abstract Resource createResource(String name, String userId,
			String fileType, byte[] data) throws StoreException,
			ResourceException;

	public abstract Resource createResource(String name, String userId,
			String fileType, byte[] data, ResourceMetadata metadata)
			throws StoreException, ResourceException;

	public abstract Resource createResource(String nameSpace, String name,
			String userId, String fileType, byte[] data) throws StoreException,
			ResourceException;

	public abstract Resource createResourceFromFile(String nameSpace,
			String resourceName, String userId, String fileType, String path,
			ResourceMetadata metadata) throws StoreException, ResourceException;

	public abstract Resource createResourceFromFile(String nameSpace,
			String resourceName, String userId, String fileType, String path)
			throws StoreException, ResourceException;

	public abstract Resource createResourceFromFile(String nameSpace,
			String userId, String path, ResourceMetadata metadata)
			throws StoreException, ResourceException;

	public abstract Resource openResourceForUpdate(String nameSpace,
			String name, String userId) throws StoreException,
			ResourceException;

	public abstract Resource openResourceForUpdate(String name, String userId)
			throws StoreException, ResourceException;

	public abstract void replaceInResources(String fileType, String find,
			String replace) throws StoreException;

	public abstract Resource updateResource(String nameSpace, String name,
			String userId, byte[] data, ResourceMetadata metadata)
			throws StoreException, ResourceException;

	public abstract Resource updateResource(String name, String userId,
			byte[] data, ResourceMetadata metadata) throws StoreException,
			ResourceException;

	public abstract Resource updateResource(IResource resource)
			throws StoreException, ResourceException;

	public abstract void markResourceAsDeleted(String nameSpace, String name,
			String userId) throws StoreException, ResourceException;

	public abstract void markResourceAsDeleted(String name, String userId)
			throws StoreException, ResourceException;

	public abstract void bindNamespace(String nameSpace, String path)
			throws StoreException;

	public abstract void unbindNamespace(String nameSpace, String path)
			throws StoreException;

	public abstract AcumenList<String> getBindingPathsForNameSpace(
			String nameSpace) throws StoreException;

	public abstract void importDirectoryToNamespace(String nameSpace,
			String prefix, String path) throws StoreException,
			ResourceException;

	public abstract void importDirectoryToNamespace(String nameSpace,
			String path) throws ResourceException, StoreException;

	public abstract void synchroniseResource(IResource resource)
			throws StoreException;

	public abstract void cleanupResource(String nameSpace, String resourceName)
			throws StoreException;

	public abstract void cleanupResource(IResource resource)
			throws StoreException;

	public abstract void cleanupResource(String name) throws StoreException;

	public abstract void synchroniseNamespace(String nameSpace)
			throws StoreException;

	public abstract void synchroniseNamespace() throws StoreException;

	public abstract void nukeEverythingInTheStore() throws StoreException;

}