/**
 * <p>Title: HbaseConnectionPool.java</p>
 * <p>Description: HbaseConnectionPool</p>
 * <p>Package: org.darkphoenixs.pool.hbase</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2015</p>
 */
package org.darkphoenixs.pool.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.darkphoenixs.pool.ConnectionPool;
import org.darkphoenixs.pool.PoolBase;
import org.darkphoenixs.pool.PoolConfig;

/**
 * <p>Title: HbaseConnectionPool</p>
 * <p>Description: Hbase连接池</p>
 *
 * @author Victor
 * @version 1.0
 * @see PoolBase
 * @see ConnectionPool
 * @since 2015年9月19日
 */
public class HbaseConnectionPool extends PoolBase<Connection> implements ConnectionPool<Connection> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -9126420905798370243L;

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 默认构造方法</p>
     */
    public HbaseConnectionPool() {
        this("localhost", "2181");
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param host 地址
     * @param port 端口
     */
    public HbaseConnectionPool(final String host, final String port) {
        this(new PoolConfig(), host, port, null, null);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param host    地址
     * @param port    端口
     * @param master  hbase主机
     * @param rootdir hdfs目录
     */
    public HbaseConnectionPool(final String host, final String port, final String master, final String rootdir) {
        this(new PoolConfig(), host, port, master, rootdir);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param hadoopConfiguration hbase配置
     */
    public HbaseConnectionPool(final Configuration hadoopConfiguration) {
        this(new PoolConfig(), hadoopConfiguration);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param host       地址
     * @param port       端口
     */
    public HbaseConnectionPool(final PoolConfig poolConfig, final String host, final String port) {
        this(poolConfig, host, port, null, null);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig          池配置
     * @param hadoopConfiguration hbase配置
     */
    public HbaseConnectionPool(final PoolConfig poolConfig, final Configuration hadoopConfiguration) {
        super(poolConfig, new HbaseConnectionFactory(hadoopConfiguration));
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param host       地址
     * @param port       端口
     * @param master     hbase主机
     * @param rootdir    hdfs目录
     */
    public HbaseConnectionPool(final PoolConfig poolConfig, final String host, final String port, final String master, final String rootdir) {
        super(poolConfig, new HbaseConnectionFactory(host, port, master, rootdir));
    }

    @Override
    public Connection getConnection() {
        return super.getResource();
    }

    @Override
    public void returnConnection(Connection conn) {
        super.returnResource(conn);
    }

    @Override
    public void invalidateConnection(Connection conn) {
        super.invalidateResource(conn);
    }

}
