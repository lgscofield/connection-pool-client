/**
 * <p>Title: RedisConnectionFactory.java</p>
 * <p>Description: RedisConnectionFactory</p>
 * <p>Package: org.darkphoenixs.pool.redis</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2015</p>
 */
package org.darkphoenixs.pool.redis;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.darkphoenixs.pool.ConnectionFactory;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

/**
 * <p>Title: RedisConnectionFactory</p>
 * <p>Description: Redis连接工厂</p>
 *
 * @author Victor
 * @version 1.0
 * @see ConnectionFactory
 * @since 2015年9月19日
 */
class RedisConnectionFactory implements ConnectionFactory<Jedis> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5692815845396189037L;

    /**
     * hostAndPort
     */
    private final AtomicReference<HostAndPort> hostAndPort = new AtomicReference<>();
    /**
     * connectionTimeout
     */
    private final int connectionTimeout;
    /**
     * soTimeout
     */
    private final int soTimeout;
    /**
     * password
     */
    private final String password;
    /**
     * database
     */
    private final int database;
    /**
     * clientName
     */
    private final String clientName;

    /**
     * <p>Title: RedisConnectionFactory</p>
     * <p>Description: 构造方法</p>
     *
     * @param host              地址
     * @param port              端口
     * @param connectionTimeout 连接超时
     * @param soTimeout         超时时间
     * @param password          密码
     * @param database          数据库
     * @param clientName        客户端名称
     */
    public RedisConnectionFactory(final String host, final int port, final int connectionTimeout, final int soTimeout, final String password, final int database, final String clientName) {
        this.hostAndPort.set(new HostAndPort(host, port));
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.password = password;
        this.database = database;
        this.clientName = clientName;
    }

    /**
     * <p>Title: setHostAndPort</p>
     * <p>Description: 设置地址和端口</p>
     *
     * @param hostAndPort 地址和端口
     */
    public void setHostAndPort(final HostAndPort hostAndPort) {
        this.hostAndPort.set(hostAndPort);
    }

    @Override
    public PooledObject<Jedis> makeObject() throws Exception {
        Jedis jedis = this.createConnection();
        return new DefaultPooledObject<>(jedis);
    }

    @Override
    public void destroyObject(PooledObject<Jedis> p) throws Exception {
        BinaryJedis jedis = p.getObject();
        if (!(jedis.isConnected())) {
            return;
        }
        try {
            try {
                jedis.quit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            jedis.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validateObject(PooledObject<Jedis> p) {
        BinaryJedis jedis = p.getObject();
        try {
            HostAndPort hostAndPort = this.hostAndPort.get();
            String connectionHost = jedis.getClient().getHost();
            int connectionPort = jedis.getClient().getPort();
            return ((hostAndPort.getHost().equals(connectionHost)) && (hostAndPort.getPort() == connectionPort) && (jedis.isConnected()) && (jedis.ping().equals("PONG")));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    @Override
    public void activateObject(PooledObject<Jedis> p) throws Exception {
        BinaryJedis jedis = p.getObject();
        if (jedis.getDB() != this.database) {
            jedis.select(this.database);
        }
    }

    @Override
    public void passivateObject(PooledObject<Jedis> p) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public Jedis createConnection() throws Exception {
        HostAndPort hostAndPort = this.hostAndPort.get();
        Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), this.connectionTimeout, this.soTimeout);

        jedis.connect();
        if (null != this.password) {
            jedis.auth(this.password);
        }
        if (this.database != 0) {
            jedis.select(this.database);
        }
        if (this.clientName != null) {
            jedis.clientSetname(this.clientName);
        }

        return jedis;
    }

}
