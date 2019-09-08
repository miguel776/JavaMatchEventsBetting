package com.fsbtech.interviews;

import com.fsbtech.interviews.entities.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collection;
import java.util.Vector;

public class ClientImpl implements Client {
    
   /**
    * Adding event to event repository
    *
    *  The repository
    *      - The events repository is a Map<Integer, Event> static from the class EventsRepository.
    *      - A static variable can be used without the need for creating an instance of a class.
    *
    *  Exception handling
    *      - It is handled a NullPointerException.
    *      - NullPointerException is a run-time exception which is not recommended to catch it.
    *      - We can catch the NullPointerException and except using an if-condition.
    *
    *  Thread safe
    *      - The Map is not synchronized (not thread-safe). Collections.synchronizedMap() method returns
    *        a synchronized (thread-safe) map backed by the specified map.
    *      - In order to guarantee serial access, it is critical that all access to
    *        the backing map is accomplished through the returned map.
    *      - It is done in the class EventsRepository by the implementation of static methods.
    *
    * @param event - Event object representing a new Event to add to the repository.
    */
    public void addEvent(Event event) {
        if (event != null) {
            EventsRepository.putToMap(event.getId(), event);
        }
    }

   /**
    * Update event status to completed.
    *
    * > Exception handling
    *
    *		- NullPointerException is a run-time exception which is not recommended to catch it.
    *		- We catch the NullPointerException and except using an if-condition.
    *
    * > Thread safe
    *
    *		- The Repository is synchronized,  it is defined like this in the class.
    *		- The event object is an immutable object, this improves the arrangement to be treated as multi thread.
    *		- An  immutable object unmodifiable or unchangeable, Once string object is
    *		  created its data or state can't be changed and it has to create a new string object is created.
    *
    * @param id - Identifier for event to be marked as completed.
    */
    public void eventCompleted(Integer id) {

        Event event = EventsRepository.getFromMap(id);
        if (event == null) {
            return;
        }
        EventsRepository.putToMap(event.getId(), new Event(
                event.getId(),
                event.getName(),
                event.getSubCategory(),
                event.getMarketRefTypes(),
                true));
    }

   /**
    * Add marketRefType to an existing event
    *
    * > Exception handling
    *
    *		- It is handled a NullPointerException,  an IllegalAccessException and an InstantiationException
    *
    * > Multithreading
    *
    *		- The Repository is synchronized,  it is defined like this in the class.
    *		- The event object is an immutable object, this improves the arrangement to be treated as multi-thread.
    *
    * > immutable object
    *
    *		- An  immutable object unmodifiable or unchangeable, Once string object is
    *		  created its data or state can't be changed and it has to create a new string object is created.
    *		- In this method is used Shallow copy to create, shallow copy concept copies everything values/type/fields
    *		- bit by bit and returns the new instance which has an exact copy of the values in the original object.
    *
    * @param id - Identifier for event to add marketRefType to.
    * @param marketRefType - Market supported by Event.
    */
    public void attachMarketRefTypeToEvent(Integer id, MarketRefType marketRefType) {
        try {

            Event event = EventsRepository.getFromMap(id);
            if (event == null) {
                return;
            }
            Collection<MarketRefType> oldMarketRefTypes = event.getMarketRefTypes();
            Collection<MarketRefType> newMarketRefTypes = oldMarketRefTypes.getClass().newInstance();
            newMarketRefTypes.addAll(oldMarketRefTypes);
            newMarketRefTypes.add(marketRefType);
            EventsRepository.putToMap(event.getId(), new Event(
                    event.getId(),
                    event.getName(),
                    event.getSubCategory(),
                    newMarketRefTypes,
                    event.getCompleted()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

   /**
    * Remove marketRefType from an existing event
    *
    * > Exception handling
    *
    *      - It is handled a NullPointerException,  an IllegalAccessException and an InstantiationException
    *
    * > Multithreading
    *
    *      - EventRepository is thread-safe, it is defined like this in the EventRepository class.
    *      - The event object is an immutable object, this improves the arrangement to be treated as multi-thread.
    *
    * > immutable object
    *
    *      - An immutable object is unmodifiable or unchangeable, once created its data or state can't be changed
    *        and a new object has to be created.
    *      - In this method a shallow copy of the collection of MarketRefTypes is created
    *
    * @param id - Identifier for event to add marketRefType to.
    * @param marketRefType - Market supported by Event.
    */
    public void removeMarketRefTypeFromEvent(Integer id, MarketRefType marketRefType) {
        try {
            Event event = EventsRepository.getFromMap(id);
            if (event == null) {
                return;
            }
            Collection<MarketRefType> oldMarketRefTypes = event.getMarketRefTypes();
            Collection<MarketRefType> newMarketRefTypes = oldMarketRefTypes.getClass().newInstance();
            newMarketRefTypes.addAll(oldMarketRefTypes);

            boolean wasRemoved = newMarketRefTypes.remove(marketRefType);
            if (wasRemoved) {

                EventsRepository.putToMap(event.getId(), new Event(
                        event.getId(),
                        event.getName(),
                        event.getSubCategory(),
                        newMarketRefTypes,
                        event.getCompleted()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

   /**
    * Collection of incomplete event names based on provided filter.
    * If argument is null, it should include every possible values.
    *
    * 	   - It is implemented an algorithm to iterate and filter the events repository
    *  	 according to the category, subcategory,  the betting markets and getCompleted.
    *
    * @param cat - Cetegory ref identify the category to filter by.
    * @param subcat - SubCategory ref identify the sub-category to filter by.
    * @param marketRefName - MarketRefType name to filter by.
    *
    * @return Collection of the names of incompleted events from the relevant category ref, subcategort ref and marketRefName.
    */
    public Collection<String> futureEventNamesCollection(String cat, String subcat, String marketRefName) {
        Collection<String> eventNamesCollection = new Vector<String>();

        for (Integer id : EventsRepository.keySetToMap()) {
            Event event = EventsRepository.getFromMap(id);
            SubCategory eventSubCategory = event.getSubCategory();
            Category eventCategory = eventSubCategory.getCategory();

            boolean matchesIncomplete = !event.getCompleted();
            boolean matchesCat = cat == null || cat.equals(eventCategory.getRef());
            boolean matchesSubcat = subcat == null || subcat.equals(eventSubCategory.getRef());
            boolean matchesMarketRefName = marketRefName == null;

            if (!matchesMarketRefName) {
                for (MarketRefType eventMarketRefType : event.getMarketRefTypes()) {
                    matchesMarketRefName = marketRefName.equals(eventMarketRefType.getMarketRefName());
                    if (matchesMarketRefName) {
                        break;
                    }
                }
            }
            if (matchesIncomplete && matchesCat && matchesSubcat && matchesMarketRefName) {
                eventNamesCollection.add(event.getName());
            }
        }

        return eventNamesCollection;
    }

   /**
    * Formatted structure
    *
    *  - The events repository is formatted using Google's Gson library.
    *  - Java serialization/deserialization library that can convert Java Objects into JSON and back.
    *
    * @return formatted dump or the data structure
    */
    public String dumpFullStructure() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(EventsRepository.repository);
    }
}
