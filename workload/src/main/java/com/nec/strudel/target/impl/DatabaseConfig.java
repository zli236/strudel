package com.nec.strudel.target.impl;

import java.util.Properties;

import com.nec.strudel.target.TargetConfig;
import com.nec.strudel.util.ClassUtil;

public class DatabaseConfig implements TargetConfig {
	public static final String TAG_NAME = "database";
	private String contextClassPath = "";
	private String className = "";
	private String name = "";
	private String type = "";
	private String classPath = "";
	private Properties params = new Properties();

	public DatabaseConfig() {
	}
	@Override
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets the class path of
	 * the target (which does not include
	 * the class path specified for the
	 * workload).
	 * @return an empty string if
	 * no class path is specified
	 * (the default system class loader
	 * is used). a colon-separated class
	 * paths (just as CLASSPATH environment
	 * variable).
	 */
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public void setContextClassPath(String contextClassPath) {
		this.contextClassPath = contextClassPath;
	}
	public String contextClassPath() {
		return contextClassPath;
	}

	@Override
	public ClassLoader targetClassLoader() {
		String classPath = getClassPath();
		if (!contextClassPath.isEmpty()) {
			if (classPath.isEmpty()) {
				classPath = contextClassPath;
			} else {
				classPath = classPath
						+ ":" + contextClassPath;
			}
		}
		return ClassUtil.loaderFor(classPath);
	}
	@Override
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Properties getProperties() {
		return params;
	}
	public void setParams(Properties params) {
		this.params = params;
	}
	public Properties getParams() {
		return params;
	}

}
