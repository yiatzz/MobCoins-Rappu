package br.com.yiatzz.mobcoins;

import br.com.yiatzz.mobcoins.cache.local.MobCoinsLocalCache;
import br.com.yiatzz.mobcoins.storage.MobCoinsRepository;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class MobCoinsPlugin extends JavaPlugin {

    @Getter
    private static MobCoinsPlugin instance;

    @Getter
    private static MobCoinsRepository repository;

    @Getter
    private static MobCoinsLocalCache cache;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        saveDefaultConfig();

        repository = new MobCoinsRepository(getConfig());

        try {
            repository.getDatabase().createTableFromStatement(
                    "CREATE TABLE IF NOT EXISTS `mobcoins_users` (`userName` VARCHAR(16) NOT NULL" +
                            " PRIMARY KEY, `mobcoins` DOUBLE NOT NULL) ENGINE = INNODB;"
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        cache = new MobCoinsLocalCache();
    }
}
