package kaptainwutax.traders.command;

import kaptainwutax.traders.init.InitConfig;
import kaptainwutax.traders.init.InitTrade;
import kaptainwutax.traders.util.Time;
import kaptainwutax.traders.world.data.WorldDataTime;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandTraders extends CommandBase {
	
	@Override
	public String getName() {
		return "traders";
	}

	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public String getUsage(ICommandSender sender) {
		return "traders";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		
		if(args.length > 0) {
			String subcommand = args[0];

			if("time".equals(subcommand)) {
				this.executeTime(server, sender, args);
				return;
			} else if("config".equals(subcommand)) {
				this.executeConfig(server, sender, args);
				return;
			} else {
				throw new CommandException("Traders commands needs more parameters.", new Object[0]);
			}
		}

		throw new CommandException("Traders commands needs more parameters.", new Object[0]);
	}

	private void executeConfig(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		
		if(args.length > 1) {
			String task = args[1];
			
			if("reload".equals(task)) {
				InitConfig.registerConfigs();
				InitTrade.registryTrades();
				server.sendMessage(new TextComponentString("Configs sucessfully reloaded."));
				return;
			} else {
				throw new CommandException("Unknown task [" + task + "].", new Object[0]);
			}
		}		
		
		throw new CommandException("Config command needs a task.", new Object[0]);		
	}

	private void executeTime(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		
		if(args.length > 1) {
			String task = args[1];
			
			if("reset".equals(task)) {
				WorldDataTime data = WorldDataTime.get(world);
				Time time = data.getTime();
				time.resetTime(world);					
				server.sendMessage(new TextComponentString("Time sucessfully reset."));
				return;
			} else {
				throw new CommandException("Unknown task [" + task + "].", new Object[0]);
			}
		}
		
		throw new CommandException("Time command needs a task.", new Object[0]);
	}

}
