package op65n.tech.vaultmanager.util;

import org.bukkit.command.CommandSender;

public final class Message {

    /**
     * Colorizes and sends a message to the specified {@link CommandSender}
     *
     * @param sender Receiver of the message
     * @param message Message to be sent
     */
    public static void send(final CommandSender sender, final String message) {
        sender.sendMessage(Color.translate(message));
    }

}
