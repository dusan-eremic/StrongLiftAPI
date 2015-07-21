package me.stronglift.api.util;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for classes.
 */
@SuppressWarnings("rawtypes")
public class ClassUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);
	
	/**
	 * Returns the specified resource by checking the current thread's
	 * {@link Thread#getContextClassLoader() context class loader}, then the
	 * current ClassLoader (<code>ClassUtils.class.getClassLoader()</code>),
	 * then the system/application ClassLoader (
	 * <code>ClassLoader.getSystemClassLoader()</code>, in that order, using
	 * {@link ClassLoader#getResourceAsStream(String) getResourceAsStream(name)}
	 * .
	 *
	 * @param name the name of the resource to acquire from the classloader(s).
	 * @return the InputStream of the resource found, or <code>null</code> if
	 *         the resource cannot be found from any of the three mentioned
	 *         ClassLoaders.
	 */
	public static InputStream getResourceAsStream(String name) {
		
		InputStream is = THREAD_CL_ACCESSOR.getResourceStream(name);
		
		if (is == null) {
			if (log.isTraceEnabled()) {
				log.trace("Resource ["
						+ name
						+ "] was not found via the thread context ClassLoader.  Trying the "
						+ "current ClassLoader...");
			}
			is = CLASS_CL_ACCESSOR.getResourceStream(name);
		}
		
		if (is == null) {
			if (log.isTraceEnabled()) {
				log.trace("Resource ["
						+ name
						+ "] was not found via the current class loader.  Trying the "
						+ "system/application ClassLoader...");
			}
			is = SYSTEM_CL_ACCESSOR.getResourceStream(name);
		}
		
		if (is == null && log.isTraceEnabled()) {
			log.trace("Resource ["
					+ name
					+ "] was not found via the thread context, current, or "
					+ "system/application ClassLoaders.  All heuristics have been exhausted.  Returning null.");
		}
		
		return is;
	}
	
	private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
		@Override
		protected ClassLoader doGetClassLoader() throws Throwable {
			return Thread.currentThread().getContextClassLoader();
		}
	};
	
	private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
		@Override
		protected ClassLoader doGetClassLoader() throws Throwable {
			return ClassUtils.class.getClassLoader();
		}
	};
	
	private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
		@Override
		protected ClassLoader doGetClassLoader() throws Throwable {
			return ClassLoader.getSystemClassLoader();
		}
	};
	
	private static interface ClassLoaderAccessor {
		Class loadClass(String fqcn);
		
		InputStream getResourceStream(String name);
	}
	
	private static abstract class ExceptionIgnoringAccessor implements
			ClassLoaderAccessor {
		
		public Class loadClass(String fqcn) {
			Class clazz = null;
			ClassLoader cl = getClassLoader();
			if (cl != null) {
				try {
					clazz = cl.loadClass(fqcn);
				} catch (ClassNotFoundException e) {
					if (log.isTraceEnabled()) {
						log.trace("Unable to load clazz named [" + fqcn
								+ "] from class loader [" + cl + "]");
					}
				}
			}
			return clazz;
		}
		
		public InputStream getResourceStream(String name) {
			InputStream is = null;
			ClassLoader cl = getClassLoader();
			if (cl != null) {
				is = cl.getResourceAsStream(name);
			}
			return is;
		}
		
		protected final ClassLoader getClassLoader() {
			try {
				return doGetClassLoader();
			} catch (Throwable t) {
				if (log.isDebugEnabled()) {
					log.debug("Unable to acquire ClassLoader.", t);
				}
			}
			return null;
		}
		
		protected abstract ClassLoader doGetClassLoader() throws Throwable;
	}
	
}
