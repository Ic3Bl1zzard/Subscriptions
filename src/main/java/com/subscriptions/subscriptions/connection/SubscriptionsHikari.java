package com.subscriptions.subscriptions.connection;

import com.subscriptions.config.ConfigManager;
import com.subscriptions.string.StringUtils;
import com.subscriptions.subscriptions.enums.PrepareStatements;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubscriptionsHikari {

    private static SubscriptionsHikari instance = null;
    private HikariDataSource dataSource;

    public static synchronized SubscriptionsHikari getInstance() {
        if (instance == null) instance = new SubscriptionsHikari();
        return instance;
    }

    public void connectToDB() {
        String host = ConfigManager.getConfigManager().getString("host");
        int port = ConfigManager.getConfigManager().getInt("port");
        String database = ConfigManager.getConfigManager().getString("database");
        String userName = ConfigManager.getConfigManager().getString("username");
        String password = ConfigManager.getConfigManager().getString("password");
        dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(3);
        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", userName);
        dataSource.addDataSourceProperty("password", password);
        registerTables();
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("&eSubscriptions &aDatabase &7has made a &aconnection&7!"));

    }

    public void disconnect() {
        if (dataSource != null) {
            dataSource.close();
            Bukkit.getConsoleSender().sendMessage(StringUtils.format("&eSubscriptions &aDatabase &7has &cdisconnected&7!"));
        }
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    private void registerTables() {
        createSilverSubscription();
        createGoldSubscription();
        createPlatinumSubscription();
        createParticleTable();
    }

    private void createSilverSubscription() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PrepareStatements.CREATESILVERTABLE.getStatement());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createGoldSubscription() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(PrepareStatements.CREATEGOLDTABLE.getStatement())) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void createPlatinumSubscription() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PrepareStatements.CREATEPLATINUMTABLE.getStatement());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createParticleTable() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PrepareStatements.CREATEPARTICLESTABLE.getStatement());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
