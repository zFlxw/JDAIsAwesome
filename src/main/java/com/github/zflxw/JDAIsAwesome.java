package com.github.zflxw;

import com.github.zflxw.listeners.MessageListener;
import com.github.zflxw.manager.CommandManager;
import com.github.zflxw.sql.SQLManager;
import com.github.zflxw.sql.SQLite;
import com.github.zflxw.util.Secrets;
import com.github.zflxw.util.Utilities;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class JDAIsAwesome {

    public static JDAIsAwesome instance;

    public final String DEFAULT_PREFIX = ">";
    public final String BOT_ID = "732189708305563680";

    private static final String VERSION = "1.0.2";

    private ShardManager shardManager;
    private CommandManager commandManager;
    private SQLite sqlite;
    private Utilities utilities;
    private SQLManager sqlManager;

    public static void main(String[] args) {
        new JDAIsAwesome();
    }

    public JDAIsAwesome() {
        fetchClasses();

        getSQLite().connect();
        getSQLManager().createTable();

        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();

        builder.setToken(Secrets.TOKEN);
        builder.setActivity(Activity.watching("v"+VERSION));
        builder.setStatus(OnlineStatus.ONLINE);

        builder.addEventListeners(new MessageListener());

        try {
            shardManager = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        shutdown();
    }

    public void shutdown() {
        new Thread(() -> {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        if (shardManager != null) {
                            shardManager.setStatus(OnlineStatus.OFFLINE);
                            shardManager.shutdown();
                            getSQLite().disconnect();
                            System.out.println("Bot offline");
                        }
                        reader.close();
                    }
                }
            } catch (IOException ignored) {

            }
        }).start();
    }

    private void fetchClasses() {
        instance = this;
        commandManager = new CommandManager();
        sqlite = new SQLite();
        utilities = new Utilities();
        sqlManager = new SQLManager();
    }

    public static JDAIsAwesome getInstance() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public SQLite getSQLite() {
        return sqlite;
    }

    public Utilities getUtils() {
        return utilities;
    }

    public SQLManager getSQLManager() {
        return sqlManager;
    }
}