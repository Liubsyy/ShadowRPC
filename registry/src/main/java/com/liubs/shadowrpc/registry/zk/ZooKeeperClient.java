package com.liubs.shadowrpc.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 对zookeeper的连接，增删改查，获取字节点，监听子节点
 * @author Liubsyy
 * @date 2023/12/18 11:36 PM
 **/
public class ZooKeeperClient {

    private CuratorFramework client;
    private Map<String, CuratorCache> curatorCacheMap = new HashMap<>();

    public ZooKeeperClient(String connectionString) {
        this.client = CuratorFrameworkFactory.newClient(
                connectionString,
                new ExponentialBackoffRetry(1000, 3));
        this.client.start();
    }

    public String create(String path) throws Exception {
        //临时顺序节点，断开自动删除
        return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path);
    }

    public String create(String path, byte[] payload) throws Exception {
        //临时顺序节点，断开自动删除
        return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path,payload);
    }

    public byte[] read(String path) throws Exception {
        return client.getData().forPath(path);
    }

    public void update(String path, byte[] payload) throws Exception {
        client.setData().forPath(path, payload);

    }

    public void delete(String path) throws Exception {
        client.delete().forPath(path);
    }

    public void deleteChildren(String path) throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }

    public boolean exist(String path) throws Exception {
        return null != client.checkExists().forPath(path);
    }

    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    /**
     * 添加节点监听事件时，会先初始化CHILD_ADDED事件，这里处理成同步阻塞初始化完，再处理后续逻辑
     * @param path
     * @param pathChildrenCacheListener
     */
    public void addChildrenListener(String path, PathChildrenCacheListener pathChildrenCacheListener) {
        CuratorCache curatorCache = curatorCacheMap.get(path);
        if(null == curatorCache) {
            curatorCache = CuratorCache.builder(client, path).build();
            curatorCache.start();
            curatorCacheMap.put(path,curatorCache);
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        CuratorCacheListener cacheListener = CuratorCacheListener.builder().forInitialized(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();
            }
        }).forPathChildrenCache(path, client, pathChildrenCacheListener).build();
        curatorCache.listenable().addListener(cacheListener);



        try {
            countDownLatch.await();

            //等待初始化结束，先暂定5s吧，超时了就不等了
//            countDownLatch.await(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        curatorCacheMap.values().forEach(CuratorCache::close);
        if (client != null) {
            this.client.close();
        }
    }
}