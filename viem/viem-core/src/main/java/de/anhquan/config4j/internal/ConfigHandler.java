/**
 * Copyright 2009 http://anhquan.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.anhquan.config4j.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import de.anhquan.config4j.ConfigItem;
import de.anhquan.config4j.annotation.ConfigContainer;
import de.anhquan.config4j.annotation.ConfigParam;

public class ConfigHandler implements InvocationHandler {

	static final Pattern GETTER_REG = Pattern.compile("^(is|get)(.+)$");
	static final Pattern SETTER_REG = Pattern.compile("^(set)(.+)$");

	final private ConfigurationProvider configuration;
	final private String prefix;
	final private Class<?> configType;

	public ConfigHandler(Class<?> configType,
			ConfigurationProvider configuration) {
		this.configType = configType;
		ConfigContainer annotation = configType
				.getAnnotation(ConfigContainer.class);
		String sPrefix = annotation.Prefix();
		if (sPrefix == null){
			sPrefix = configType.getSimpleName()+".";
		}

		prefix = sPrefix;
		
		this.configuration = configuration;
	}

	public ConfigurationProvider getConfiguration() {
		return this.configuration;
	}

	/**
	 * Get all keys declared in the config interface
	 * 
	 * @return
	 */
	public List<String> getDeclaredKeys() {
		List<String> keys = new ArrayList<String>();

		Method[] methods = configType.getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			Matcher matcher;
			Class returnType = method.getReturnType();
			if ((!returnType.equals(void.class))
					&& ((matcher = GETTER_REG.matcher(methodName)).matches())
					&& (method.getAnnotation(ConfigParam.class) != null)) {
				String key = matcher.group(2);

				String propName = findPropertyName(method);
				keys.add(propName);
			}
		}

		return keys;
	}

	public List<ConfigItem> getDeclaredConfigItems() {
		List<ConfigItem> items = new ArrayList<ConfigItem>();

		Method[] methods = configType.getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			ConfigParam annotation = method.getAnnotation(ConfigParam.class);
			Matcher matcher;
			Class returnType = method.getReturnType();
			if ((!returnType.equals(void.class))
					&& ((matcher = GETTER_REG.matcher(methodName)).matches())
					&& (annotation != null)) {
				String key = matcher.group(2);

				String propName = findPropertyName(method);
				String strReturnType = StringUtils.capitalize(ClassUtils
						.getShortClassName(returnType));
				String configMethodName = "get" + strReturnType;

				try {
					Method getter;
					try {
						getter = ConfigurationProvider.class.getMethod(
								configMethodName, String.class);
						Object value = getter.invoke(configuration, propName);
						ConfigItem item = new ConfigItem();
						item.setKey(propName);
						item.setTitle(annotation.Title());
						item.setDescription(annotation.Description());
						item.setValue(value);
						items.add(item);

					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // String.class is for propName

				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return items;
	}

	public void clearAllProperties(Object proxy){
		try {
			Method clearMethod = ConfigurationProvider.class.getMethod("clear");
			clearMethod.invoke(configuration);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void initAllProperties(Object proxy) {
		System.out.println("init All configs");
		clearAllProperties(proxy);
		Method[] methods = configType.getDeclaredMethods();
		Matcher matcher;

		for (Method method : methods) {
			Class returnType = method.getReturnType();
			String methodName = method.getName();

			if ((matcher = SETTER_REG.matcher(methodName)).matches()
					&& returnType.equals(void.class)) {
				String key = matcher.group(2);

				Method getter = findGetter(key, returnType);
				String propName = findPropertyName(getter);

				Class clsReturnType = getter.getReturnType();

				String strReturnType = StringUtils.capitalize(ClassUtils
						.getShortClassName(clsReturnType));
				String configMethodName = "get" + strReturnType;

				try {
					Method method1 = ConfigurationProvider.class.getMethod(
							"containsKey", String.class);
					boolean keyExisted = (boolean) method1.invoke(
							configuration, propName);
					if (!keyExisted) {
						Object defaultValue = findDefaultValue(getter,
								getter.getReturnType());
						Method createKeyMethod = ConfigurationProvider.class
								.getMethod("createKey", String.class,
										Object.class, String.class,
										String.class, int.class);
						ConfigParam annotation = getter
								.getAnnotation(ConfigParam.class);

						createKeyMethod.invoke(configuration, propName,
								defaultValue, annotation.Title(),
								annotation.Description(),
								annotation.SortValue());
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					Object defaultValue = findDefaultValue(getter,
							getter.getReturnType());
					invokeSetter(proxy, method, propName, defaultValue);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void resetAllToDefaults(Object proxy) {
		Method[] methods = configType.getDeclaredMethods();
		Matcher matcher;

		for (Method method : methods) {
			Class returnType = method.getReturnType();
			String methodName = method.getName();

			if ((matcher = SETTER_REG.matcher(methodName)).matches()
					&& returnType.equals(void.class)) {
				String key = matcher.group(2);
				System.out.println(key);
				Method getter = findGetter(key, returnType);
				String propName = findPropertyName(getter);
				Object defaultValue = findDefaultValue(getter,
						getter.getReturnType());
				invokeSetter(proxy, method, propName, defaultValue);
			}
		}

	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		String methodName = method.getName();
		
		if ("resetAll".compareTo(methodName)==0){
			initAllProperties(proxy);
			return null;
		}

		@SuppressWarnings("unchecked")
		Class returnType = method.getReturnType();

		Matcher matcher;

		if (GETTER_REG.matcher(methodName).matches())
			return invokeGetter(proxy, method);
		else if ((matcher = SETTER_REG.matcher(methodName)).matches()
				&& returnType.equals(void.class)) {

			String key = matcher.group(2);
			Method getter = findGetter(key, returnType);
			String propName = findPropertyName(getter);

			return invokeSetter(proxy, method, propName, args[0]);
		}

		return null;
	}

	private Method findGetter(String key, Class<?> methodType) {
		String getMethodName = "get" + key;
		Method getter = null;
		try {
			getter = configType.getMethod(getMethodName);
		} catch (NoSuchMethodException e) {
			if (Boolean.TYPE.equals(methodType)) {
				try {
					getter = configType.getMethod("is" + key);
				} catch (NoSuchMethodException ex2) {
				}
			}
		}

		return getter;
	}

	private Object invokeGetter(Object proxy, Method method) {
		Class clsReturnType = method.getReturnType();

		String strReturnType = StringUtils.capitalize(ClassUtils
				.getShortClassName(clsReturnType));
		String configMethodName = "get" + strReturnType;

		String propName = findPropertyName(method);

		try {
			Method getter;
			if (method.getName().compareTo("getKeys") == 0) {
				return getDeclaredKeys();
			} else if (method.getName().compareTo("getConfigItems") == 0) {
				return getDeclaredConfigItems();
			} else if (method.getName().compareTo("getConfigItem") == 0) {
				return getDeclaredConfigItems();
			} else {
				getter = ConfigurationProvider.class.getMethod(
						configMethodName, String.class); // String.class is for
															// propName
				return getter.invoke(configuration, propName);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {

			return findDefaultValue(method, clsReturnType);
		}
		return null;
	}

	private String findPropertyName(Method getter) {
		ConfigParam annotation = getter.getAnnotation(ConfigParam.class);
		String key;
		if ((annotation == null) || (annotation.Key().isEmpty())) {
			Matcher matcher;
			if ((matcher = GETTER_REG.matcher(getter.getName())).matches())
				key = matcher.group(2);
			else
				key = "" + "" + "" + "";
		} else
			key = annotation.Key();

		String paramName = prefix + key;
		return paramName;
	}

	@SuppressWarnings("unchecked")
	private Object findDefaultValue(Method getter, Class returnType) {
		ConfigParam annotation = getter.getAnnotation(ConfigParam.class);
		Object defaultValue = null;
		if (annotation != null) {
			if (returnType.equals(Integer.class)
					|| returnType.equals(int.class))
				defaultValue = annotation.DefaultInteger();
			else if (returnType.equals(Double.class)
					|| returnType.equals(double.class))
				defaultValue = annotation.DefaultDouble();
			else if (returnType.equals(Float.class)
					|| returnType.equals(float.class))
				defaultValue = annotation.DefaultFloat();
			else if (returnType.equals(Byte.class)
					|| returnType.equals(byte.class))
				defaultValue = annotation.DefaultByte();
			else if (returnType.equals(Short.class)
					|| returnType.equals(short.class))
				defaultValue = annotation.DefaultShort();
			else if (returnType.equals(Long.class)
					|| returnType.equals(long.class))
				defaultValue = annotation.DefaultLong();
			else if (returnType.equals(Character.class)
					|| returnType.equals(char.class))
				defaultValue = annotation.DefaultChar();
			else if (returnType.equals(String.class))
				defaultValue = annotation.DefaultString();
			else if (returnType.equals(Boolean.class)
					|| returnType.equals(boolean.class))
				defaultValue = annotation.DefaultBoolean();
			else if (returnType.equals(Class.class))
				defaultValue = annotation.DefaultClass();
		}
		return defaultValue;
	}

	private Object invokeSetter(Object proxy, Method method, String key,
			Object value) {
		configuration.setProperty(key, value);
		return null;
	}

}
