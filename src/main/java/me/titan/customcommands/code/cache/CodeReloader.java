package me.titan.customcommands.code.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.titan.customcommands.code.CodeVariable;
import me.titan.customcommands.customcommands.ICustomCommand;
import me.titan.customcommands.utils.Util;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class CodeReloader {

	final ICustomCommand command;
	final List<String> code;
	List<CodeVariable> variables = new ArrayList<>();

	public void reload() {

		for (String c : code) {
			if (command.getCodeMethods().containsKey(c)) {

				PremadeFunction pf = command.getCodeMethods().get(c);
				if (pf.getPropose().contains("vardef")) {
					String[] ss = pf.propose.split(":");
					String name = ss[0];
					CodeVariable.VariableType vt = Util.getEnum(ss[1], CodeVariable.VariableType.class);

				}

			}
		}

	}

}
