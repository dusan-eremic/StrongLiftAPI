package me.stronglift.api.service;

import me.stronglift.api.service.inmemory.ServiceFactoryInMemoryImpl;

/**
 * Fabrika za kreiranje servisa.
 * 
 * @author Dusan Eremic
 *
 */
public abstract class ServiceFactory {
	
	/** Instanca fabrike */
	private static ServiceFactory serviceFactoryImpl = null;
	
	/** Moguće implementacije */
	public static enum FactoryImpl {
		IN_MEMORY
	}
	
	/** Vraća instancu fabrike za traženu implementaciju */
	public static ServiceFactory get(final FactoryImpl factoryImpl) {
		
		if (factoryImpl == FactoryImpl.IN_MEMORY) {
			if (serviceFactoryImpl == null) {
				serviceFactoryImpl = new ServiceFactoryInMemoryImpl();
			}
			return serviceFactoryImpl;
		}
		
		throw new IllegalArgumentException(
				"Unsupported factory implementation '" + factoryImpl.toString()
						+ "'");
	}
	
	/** Vraća {@link LiftService} */
	public abstract LiftService getLiftService();
	
	/** Vraća {@link UserService} */
	public abstract UserService getUserService();
	
}
