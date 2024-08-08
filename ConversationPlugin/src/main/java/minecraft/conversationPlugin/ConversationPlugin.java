package minecraft.conversationPlugin;

import minecraft.conversationPlugin.commands.ConversationCommand;
import minecraft.conversationPlugin.commands.autofill.CommandAutofill;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.listeners.conversation.PlayerConnectionListener;
import minecraft.conversationPlugin.listeners.node.InputListener;
import minecraft.conversationPlugin.listeners.trigger.BlockTriggerListener;
import minecraft.conversationPlugin.listeners.trigger.EntityTriggerListener;
import minecraft.conversationPlugin.listeners.trigger.ItemTriggerListener;
import minecraft.conversationPlugin.listeners.trigger.LocationListener;
import minecraft.conversationPlugin.listeners.trigger.multiple.EntityDeathListener;
import minecraft.conversationPlugin.listeners.trigger.multiple.InteractListener;
import minecraft.conversationPlugin.parsers.conversation.ConversationParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Main class of the plugin.
 *
 * <p>
 * This class is responsible for initializing the plugin, registering commands and listeners and loading and saving
 * conversations.
 *
 * <ul>
 *     <li>CONVERSATIONS: List of loaded conversations.</li>
 *     <li>PLAYERS: Information whether the player is currently in a node, cutscene or input node.</li>
 * </ul>
 */
public final class ConversationPlugin extends JavaPlugin {

    public static ArrayList<Conversation> conversations = new ArrayList<>();
    public static HashMap<String, PlayerFlags> players = new HashMap<>();

    private final File conversationsDirectory;
    private final File pluginDirectory;

    public ConversationPlugin() {
        conversationsDirectory = new File(Paths.get(System.getProperty("user.dir"), "plugins", "ConversationPlugin", "conversations").toString());
        pluginDirectory = new File(Paths.get(System.getProperty("user.dir"), "plugins", "ConversationPlugin", "conversations").toString());
    }

    @Override
    public void onEnable() {
        if (!conversationsDirectory.exists()) {
            if (pluginDirectory.mkdirs()) {
                getLogger().info("Plugin directory created successfully.");
            } else {
                getLogger().warning("Failed to create plugin directory.");
            }
            if (conversationsDirectory.mkdirs()) {
                getLogger().info("Conversations directory created successfully.");
            } else {
                getLogger().warning("Failed to create conversations directory.");
            }
        } else {
            ConversationParser.loadConversations(conversationsDirectory);
        }

        // Register commands
        Objects.requireNonNull(getCommand("conversation")).setExecutor(new ConversationCommand(this));
        Objects.requireNonNull(getCommand("conversation")).setTabCompleter(new CommandAutofill());

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new LocationListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemTriggerListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityTriggerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockTriggerListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new InputListener(this), this);
    }

    @Override
    public void onDisable() {
        ConversationParser.saveConversations(conversationsDirectory);
    }
}
