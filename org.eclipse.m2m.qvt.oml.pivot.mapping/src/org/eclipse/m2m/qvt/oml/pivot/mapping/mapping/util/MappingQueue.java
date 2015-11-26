package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.ocl.pivot.internal.ecore.es2as.Ecore2AS;

public class MappingQueue 
{
	private static MappingQueue instance;
	private Queue<Object> oldMappingArguments;
	public Queue<Object> getOldMappingArguments() {
		return oldMappingArguments;
	}
	public void setOldMappingArguments(Queue<Object> oldMappingArguments) {
		this.oldMappingArguments = oldMappingArguments;
	}
	public HashMap<Object, Object> getIncompleteWork() {
		return incompleteWork;
	}
	public void setIncompleteWork(HashMap<Object, Object> incompleteWork) {
		this.incompleteWork = incompleteWork;
	}
	private HashMap<Object, Object> incompleteWork; 
	public MappingQueue() 
	{	
		incompleteWork = new HashMap<Object,Object>();
		oldMappingArguments= new LinkedList<Object>();
	}
	public static MappingQueue getInstance()
	{
		if(instance==null)
		{
			instance= new MappingQueue();
		}
		return instance;
	}
	public Object getPivotObject(Object key)
	{
		return incompleteWork.get(key);
	}
	public void addOldMappingArgument(Object oldMappingArgument)
	{
		oldMappingArguments.add(oldMappingArgument);
	}
	public Object getOldMappingArgument()
	{
		return oldMappingArguments.poll();
	}
	public void addIncompleteWork(Object key, Object value)
	{
		// add to queue as well
		Object o = incompleteWork.get(key);
		incompleteWork.put(key, value);

	}
}
