package acumen.map.model;

import acumen.util.IDataType;

/**
 * User: gmurphy
 * Date: 30-Sep-2009
 * Time: 12:09:48
 */
public interface INodeChild extends IDataType {
    public INode getParent();
    public boolean isRoot();
}
