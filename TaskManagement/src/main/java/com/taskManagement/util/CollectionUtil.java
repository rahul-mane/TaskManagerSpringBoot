package com.taskManagement.util;

import java.util.Collection;

public class CollectionUtil {

	public static int sizeOfIterable(Iterable<?> it) {
		if (it instanceof Collection)
			return ((Collection<?>) it).size();

		int i = 0;
		for (Object obj : it)
			i++;
		return i;
	}
		 
	public static <T extends Enum<T>> boolean enumContains(Class<T> enumerator, String value){
	    for (T c : enumerator.getEnumConstants()) {
	        if (c.name().equals(value)) {
	            return true;
	        }
	    }
	    return false;
	}
}
