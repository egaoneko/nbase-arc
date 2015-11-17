package com.navercorp.nbasearc.confmaster;

import java.io.File;
import java.io.IOException;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.springframework.util.FileSystemUtils;

public class EmbeddedZooKeeper extends ZooKeeperServerMain implements Runnable {
    private static String configPath;
    private static EmbeddedZooKeeper zookeeper;
    private static ServerConfig config;
    private static boolean start = false;
    private static Thread thread;
    
    public EmbeddedZooKeeper() throws ConfigException {
    }
    
    public EmbeddedZooKeeper(ServerConfig config) {
        EmbeddedZooKeeper.config = config;
    }
    
    public static boolean start() throws ConfigException {
        loadConfig();
        
        if (start) {
            return false;
        }
        start = true;
        
        zookeeper = new EmbeddedZooKeeper(config);
        thread = new Thread(zookeeper);
        thread.start();
    
        return true;
    }
    
    public static void stop() throws InterruptedException {
        if (start) {
            try {
                zookeeper.shutdown();
            } catch (Exception e) {
                // Ignore...
            }
            cleanup();
            thread.join();
            start = false;
        }
    }
    
    private static void cleanup() {
        FileSystemUtils.deleteRecursively(new File(config.getDataDir() + "version-2"));
    }
    
    @Override
    public void run() {
        try {
            runFromConfig(config);
        } catch (IOException e) {
            // Ignore...
        }
    }
    
    private static void loadConfig() throws ConfigException {
        config = new ServerConfig();
        try {
            config.parse(getConfigPath());
        } catch (ConfigException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String getConfigPath() {
        return configPath;
    }

    // This method called from applicationContext.xml of spring framework.
    public static void setConfigPath(String configPath) {
        EmbeddedZooKeeper.configPath = configPath;
    }
}
