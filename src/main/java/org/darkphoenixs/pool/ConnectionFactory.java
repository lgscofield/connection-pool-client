/**
 * <p>Title: ConnectionFactory.java</p>
 * <p>Description: ConnectionFactory</p>
 * <p>Package: org.darkphoenixs.pool</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2015</p>
 */
package org.darkphoenixs.pool;

import java.io.Serializable;

import org.apache.commons.pool2.PooledObjectFactory;

/**
 * <p>Title: ConnectionFactory</p>
 * <p>Description: 连接工厂接口</p>
 *
 * @author Victor
 * @version 1.0
 * @see PooledObjectFactory
 * @see Serializable
 * @since 2015年9月19日
 */
public interface ConnectionFactory<T> extends PooledObjectFactory<T>, Serializable {

    /**
     * <p>Title: createConnection</p>
     * <p>Description: 创建连接</p>
     *
     * @return 连接
     * @throws Exception
     */
    T createConnection() throws Exception;
}
