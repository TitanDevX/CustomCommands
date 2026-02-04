package me.titan.customcommands.container;

import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanCommand;

import java.util.ArrayList;
import java.util.List;

public class ParentCustomCommand extends TitanCommand implements CustomCommand {

	final String label;

	int id;
	List<String> helpMessageHeader = null;
	String helpMessageEach = null;
	List<String> helpMessageFooter = null;


	final List<SubCustomCommand> subCustomCommands = new ArrayList<>();

	public ParentCustomCommand(String label) {
		super(label);
		this.label = label;

	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public TitanCommand getTitanCommand() {
		return this;
	}

	@Override
	public CommandType getType() {
		return CommandType.PARENT;
	}

	public void setHelpMessageHeader(List<String> helpMessageHeader) {
		this.helpMessageHeader = helpMessageHeader;
	}

	public List<SubCustomCommand> getSubCustomCommands() {
		return subCustomCommands;
	}

	public void setHelpMessageEach(String helpMessageEach) {
		this.helpMessageEach = helpMessageEach;
	}

	public void setHelpMessageFooter(List<String> helpMessageFooter) {
		this.helpMessageFooter = helpMessageFooter;
	}

	@Override
	public void registerSubCommands() {

		for (SubCustomCommand subCustomCommand : subCustomCommands) {

			registerSubCommand(subCustomCommand);
			subCustomCommand.setParent(this);
		}

	}

	@Override
	protected boolean onCommand(CommandContext context) {
		return true;
	}

	@Override
	public Object[] getHelpMessageCustom() {
		if (helpMessageHeader != null && helpMessageHeader.isEmpty()) helpMessageHeader = null;
		if (helpMessageEach != null && helpMessageEach.isEmpty()) helpMessageEach = null;
		if (helpMessageFooter != null && helpMessageFooter.isEmpty()) helpMessageFooter = null;

		return new Object[]{helpMessageHeader, helpMessageEach, helpMessageFooter};
	}
}
