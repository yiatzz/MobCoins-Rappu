package br.com.yiatzz.mobcoins.storage;

import br.com.yiatzz.mobcoins.MobCoinsPlugin;
import com.google.common.util.concurrent.AtomicDouble;
import dev.magicmq.rappu.Database;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.SQLException;

public class MobCoinsRepository {

    @Getter
    private final Database database;

    public MobCoinsRepository(FileConfiguration configuration) {
        database = Database.newDatabase()
                .withConnectionInfo(
                        configuration.getString("databases.mysql.host"),
                        3306,
                        configuration.getString("databases.mysql.database")
                )
                .withUsername(configuration.getString("databases.mysql.user"))
                .withPassword(configuration.getString("databases.mysql.password"))
                .withPluginUsing(MobCoinsPlugin.getInstance())
                .withDefaultProperties()
                .withDebugLogging()
                .open();
    }

    public void close() {
        database.close();
    }

    public boolean isOpen() {
        return !database.isClosed();
    }

    public double fetchMobCoins(String userName) {
        String query = String.format(
                "SELECT * FROM `%s` WHERE `name` = ? LIMIT 1;",
                "mobcoins_users"
        );

        AtomicDouble mobcoins = new AtomicDouble(0.0);

        this.database.queryAsync(query, new Object[]{userName}, resultSet -> {
            if (resultSet.next()) {
                mobcoins.set(resultSet.getDouble("mobcoins"));
            }
        });

        return mobcoins.get();
    }

    public void insertOrUpdate(String userName, double value) {
        String query = String.format(
                "INSERT INTO `%s` (`userName`, `mobcoins`) VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE `mobcoins`=VALUES(mobcoins);",
                "mobcoins_users"
        );

        this.database.updateAsync(query, new Object[]{userName, value}, affectedRows -> {
            if (affectedRows <= 0) {
                System.err.println("Mobcoins has failed to insert " + userName + " into the database.");
            }
        });
    }

    public void deleteAllNonActiveUsers() {
        String query = String.format(
                "DELETE FROM `%s` WHERE `mobcoins` <= 0;",
                "mobcoins_users"
        );

        try {
            this.database.update(query, new Object[0]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

