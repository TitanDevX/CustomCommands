package me.titan.customcommands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class DataShortcut {

	final String name;

	Object value;

	public abstract String getValueString();

}
