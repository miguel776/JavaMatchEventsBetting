package com.fsbtech.interviews.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  The repository
 *
 *     - The events repository is  a Map<Integer, Event> static from the class EventsRepository.
 *	   - A static variable can be used without the need for creating an instance of a class.
 *
 *
 *  Thread safe
 *
 *		- The Map is not synchronized (not thread-safe). Collections.synchronizedMap() method returns
 *		  a synchronized (thread-safe) map backed by the specified map.
 *		- In order to guarantee serial access, it is critical that all access to
 *		  the backing map is accomplished through the returned map.
 *     - It is done in the class EventsRepository by the implementation of static methods.
 *
 * @author miguelangel
 *
 */
public class EventsRepository {

    public static Map<Integer, Event> repository = Collections.synchronizedMap(new HashMap<Integer, Event>());

    //put(key,value)
    public static void putToMap(Integer key, Event value) {
        synchronized (repository) {
            repository.put(key, value);
        }
    }

    //get(key)
    public static Event getFromMap(Integer key) {
        synchronized (repository) {
            if (repository.containsKey(key)) {
                return repository.get(key);
            }
        }
        return null;
    }

    //keySet()
    public static Set<Integer> keySetToMap() {
        synchronized (repository) {
            return repository.keySet();
        }
    }

    //size()
    public static int sizeToMap() {
        synchronized (repository) {
            return repository.size();
        }
    }

    //clear()
    public static void clearToMap() {
        synchronized (repository) {
            repository.clear();
        }
    }
}
