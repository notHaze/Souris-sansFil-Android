package com.remotedesktopcontrol.message;
import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = -3997768276005110075L;

	static void describe(Class<?> clazz, String pad, String leadin) {
		if (clazz == null) return;
		String type =
				clazz.isInterface() ? "interface" :
						clazz.isArray() ? "array" :
								clazz.isPrimitive() ? "primitive" :
										clazz.isEnum() ? "enum" :
												"class";
		System.out.printf("%s%s%s %s ( %s )%n",
				pad, leadin, type, clazz.getSimpleName(), clazz.getName());
		for (Class<?> interfaze : clazz.getInterfaces()) {
			describe(interfaze, pad + "   ", "implements ");
		}
		describe(clazz.getComponentType(), pad + "   ", "elements are ");
		describe(clazz.getSuperclass(), pad + "   ", "extends ");
	}

}
