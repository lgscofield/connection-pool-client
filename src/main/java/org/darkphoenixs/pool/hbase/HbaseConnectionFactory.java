/**
 * <p>Title: HbaseConnectionFactory.java</p>
 * <p>Description: HbaseConnectionFactory</p>
 * <p>Package: org.darkphoenixs.pool.hbase</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2015</p>
 */
package org.darkphoenixs.pool.hbase;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.darkphoenixs.pool.ConnectionFactory;

/**
 * <p>Title: HbaseConnectionFactory</p>
 * <p>Description: Hbase连接工厂</p>
 *
 * @author Victor
 * @version 1.0
 * @see ConnectionFactory
 * @since 2015年9月19日
 */
class HbaseConnectionFactory implements ConnectionFactory<Connection> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4024923894283696465L;

    /**
     * hadoopConfiguration
     */
    private final Configuration hadoopConfiguration;

    /**
     * <p>Title: HbaseConnectionFactory</p>
     * <p>Description: 构造方法</p>
     *
     * @param hadoopConfiguration hbase配置
     */
    public HbaseConnectionFactory(final Configuration hadoopConfiguration) {
        this.hadoopConfiguration = hadoopConfiguration;
    }

    /**
     * <p>Title: HbaseConnectionFactory</p>
     * <p>Description: 构造方法</p>
     *
     * @param host    zookeeper地址
     * @param port    zookeeper端口
     * @param master  hbase主机
     * @param rootdir hdfs数据目录
     */
    public HbaseConnectionFactory(final String host, final String port, final String master, final String rootdir) {
        this.hadoopConfiguration = new Configuration();
        this.hadoopConfiguration.set("hbase.zookeeper.quorum", host);
        this.hadoopConfiguration.set("hbase.zookeeper.property.clientPort", port);
        this.hadoopConfiguration.set("hbase.master", master);
        this.hadoopConfiguration.set("hbase.rootdir", rootdir);
    }

    @Override
    public PooledObject<Connection> makeObject() throws Exception {
        Connection connection = this.createConnection();
        return new DefaultPooledObject<>(connection);
    }

    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {
        Connection connection = p.getObject();
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<Connection> p) {
        Connection connection = p.getObject();
        return connection != null && ((!connection.isAborted()) && (!connection.isClosed()));
    }

    @Override
    public void activateObject(PooledObject<Connection> p) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void passivateObject(PooledObject<Connection> p) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public Connection createConnection() throws Exception {
        return org.apache.hadoop.hbase.client.ConnectionFactory.createConnection(hadoopConfiguration);
    }

}
