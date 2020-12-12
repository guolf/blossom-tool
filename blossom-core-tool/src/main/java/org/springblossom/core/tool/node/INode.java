
package org.springblossom.core.tool.node;

import java.io.Serializable;
import java.util.List;

/**
 * Created by blossom.
 *
 * @author Chill
 */
public interface INode extends Serializable {

	/**
	 * 主键
	 *
	 * @return Integer
	 */
	Integer getId();

	/**
	 * 父主键
	 *
	 * @return Integer
	 */
	Integer getParentId();

	/**
	 * 子孙节点
	 *
	 * @return List
	 */
	List<INode> getChildren();

}
