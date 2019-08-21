package kaptainwutax.traders.command;

import kaptainwutax.traders.util.Time;
import kaptainwutax.traders.world.data.WorldDataTime;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommandResetTime extends CommandBase {

	@Override
	public String getName() {
		return "resettime";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "resettime";
	}

	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		
		WorldDataTime data = WorldDataTime.get(world);
		Time time = data.getTime();
		time.resetTime(world);
	}

}
