package me.stronglift.api.service;

import me.stronglift.api.service.inmemory.ServiceFactoryInMemoryImpl;

/**
 * An abstract factory for DAO services.
 * 
 * @author Dusan Eremic TODO - complete JavaDoc
 *
 */
public abstract class ServiceFactory {
	
	private static ServiceFactory serviceFactoryImpl = null;
	
	public static enum FactoryImpl {
		IN_MEMORY
	}
	
	public static ServiceFactory get(final FactoryImpl factoryImpl) {
		
		if (factoryImpl == FactoryImpl.IN_MEMORY) {
			if (serviceFactoryImpl == null) {
				serviceFactoryImpl = new ServiceFactoryInMemoryImpl();
			}
			return serviceFactoryImpl;
		}
		
		throw new IllegalArgumentException("Unsupported factory implementation '" + factoryImpl.toString() + "'");
	}
	
	public abstract LiftService getLiftService();
	
	public abstract UserService getUserService();
	
}
