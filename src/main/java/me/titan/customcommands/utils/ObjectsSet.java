package me.titan.customcommands.utils;


public class ObjectsSet<FIRST, SECOND> {

	FIRST first;
	SECOND second;
	public ObjectsSet(FIRST first, SECOND second){
		this.first = first;
		this.second = second;
	}
	public FIRST getFirst(){
		return first;
	}
	public SECOND getSecond(){
		return second;
	}



}
