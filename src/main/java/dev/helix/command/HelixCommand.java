package dev.helix.command;

import dev.helix.HelixClient;
import dev.helix.module.Module;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public final class HelixCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "helix";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/helix toggle <module>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length >= 2 && "toggle".equalsIgnoreCase(args[0])) {
            String name = join(args, 1);
            Module module = HelixClient.MODULES.find(name);
            if (module != null) {
                module.toggle();
                HelixClient.CONFIG.save(HelixClient.MODULES, HelixClient.HUD);
                sender.addChatMessage(new ChatComponentText("Helix: " + module.getName() + " " + (module.isEnabled() ? "enabled" : "disabled")));
                return;
            }
        }

        sender.addChatMessage(new ChatComponentText("Helix modules: " + HelixClient.MODULES.names()));
    }

    private String join(String[] values, int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < values.length; i++) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(values[i]);
        }
        return builder.toString();
    }
}
