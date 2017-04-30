package com.fsbtech.interviews;

import com.fsbtech.interviews.entities.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ClientImplTest {

    private final static Client client = new ClientImpl();

    @BeforeClass
    public static void setup() throws Exception {

        // Event one.
        Collection<MarketRefType> listmarketRefType = new ArrayList<MarketRefType>();
        listmarketRefType.add(new MarketRefType(1, "Over/Under"));
        listmarketRefType.add(new MarketRefType(2, "spread betting"));
        listmarketRefType.add(new MarketRefType(3, "Banker"));

        EventsRepository.putToMap(1, new Event(
                1,
                "Manchester United v Arsenal",
                new SubCategory(1, "Premier League", new Category(1, "Football")),
                listmarketRefType,
                false));

        // Event two.
        listmarketRefType = new ArrayList<MarketRefType>();
        listmarketRefType.add(new MarketRefType(4, "Home/Draw/Away"));
        listmarketRefType.add(new MarketRefType(5, "Banker"));
        listmarketRefType.add(new MarketRefType(6, "spread betting"));

        EventsRepository.putToMap(2, new Event(
                2,
                "Georgetown Basketball Gamewatch vs Villanova",
                new SubCategory(2, "Melcombe Place", new Category(2, "Basketball")),
                listmarketRefType,
                false));
    }

    //......................................................................
    //Adding new Event.
    //......................................................................

    /**
     * - The correct functionality is checked according to the length of the event repository.
     * - There must be a record more after adding a new event
     */
    @Test
    public void testAddEvent() throws Exception {
        Collection<MarketRefType> marketRefTypes = new ArrayList<MarketRefType>();
        marketRefTypes.add(new MarketRefType(7, "livebet"));
        marketRefTypes.add(new MarketRefType(8, "Home/Draw/Away"));
        marketRefTypes.add(new MarketRefType(9, "Split bets"));

        Event event = new Event(
                3,
                "Andy Murray v Novak Djokovic",
                new SubCategory(3, "French Open", new Category(3, "Tennis")),
                marketRefTypes,
                false);

        int prevCount = EventsRepository.sizeToMap();
        client.addEvent(event);
        assertEquals(prevCount + 1, EventsRepository.sizeToMap());
    }


    //......................................................................
    //Adding new Event.
    //......................................................................

    /**
     * - The correct functionality is checked according value of Completed from event.
     * - It must be True after update event status to completed. 
     */
    @Test
    public void testEventCompleted() throws Exception {
        int eventId = 1;

        Event eventBefore = EventsRepository.getFromMap(eventId);
        client.eventCompleted(eventId);
        Event eventAfter = EventsRepository.getFromMap(eventId);

        Assert.assertTrue(!eventBefore.getCompleted() && eventAfter.getCompleted());
    }

    //......................................................................
    // Add marketRefType to an existing event
    //......................................................................

    /**
     * - The correct functionality is checked to MarketRefType .
     * - The new MarketRefType it must be in the list of market after Add
     */

    @Test
    public void testAttachMarketRefTypeToEvent() throws Exception {
        int eventId = 1;

        MarketRefType marketRefType = new MarketRefType(10, "Picks");

        Event eventBefore = EventsRepository.getFromMap(eventId);
        Collection<MarketRefType> marketRefTypesBefore = eventBefore.getMarketRefTypes();

        client.attachMarketRefTypeToEvent(eventId, marketRefType);

        Event eventAfter = EventsRepository.getFromMap(eventId);
        Collection<MarketRefType> marketRefTypesAfter = eventAfter.getMarketRefTypes();

        Assert.assertTrue(!marketRefTypesBefore.contains(marketRefType) &&
                marketRefTypesAfter.contains(marketRefType));
    }

    //......................................................................
    //Remove marketRefType from an existing event
    //......................................................................

    /**
     * - The correct functionality is checked to MarketRefType from the event.
     * - The old MarketRefType it should not be there after remove
     */
    @Test
    public void testRemoveMarketRefTypeFromEvent() throws Exception {
        int eventId = 1;
        MarketRefType marketRefType = new MarketRefType(1, "Over/Under");

        //Event before to be modified
        Event eventBefore = EventsRepository.getFromMap(eventId);
        Collection<MarketRefType> marketRefTypesBefore = eventBefore.getMarketRefTypes();
        client.removeMarketRefTypeFromEvent(eventId, marketRefType);

        //Event after to be modified
        Event eventAfter = EventsRepository.getFromMap(eventId);
        Collection<MarketRefType> marketRefTypesAfter = eventAfter.getMarketRefTypes();
        Assert.assertTrue(marketRefTypesBefore.contains(marketRefType) &&
                !marketRefTypesAfter.contains(marketRefType));
    }

    //......................................................................
    // Collection of incomplete event names based on provided filter.
    //......................................................................

    /**
     * - The correct functionality is checked the Collection of incomplete event names.
     * - It could be  a collection of the names of incomplete events from the relevant category
     *   ref, subcategory ref and marketRefName.
     */

    @Test
    public void testFutureEventNamesCollection() throws Exception {
        Collection<String> expectedEventNames = new Vector<String>();
        expectedEventNames.add("Georgetown Basketball Gamewatch vs Villanova");

        Collection<String> eventNames = client.futureEventNamesCollection("Basketball", "Melcombe Place", "Home/Draw/Away");
        assertThat(eventNames, is(expectedEventNames));

        eventNames = client.futureEventNamesCollection(null, "Melcombe Place", "Home/Draw/Away");
        assertThat(eventNames, is(expectedEventNames));

        eventNames = client.futureEventNamesCollection("Basketball", null, "Home/Draw/Away");
        assertThat(eventNames, is(expectedEventNames));

        eventNames = client.futureEventNamesCollection(null, null, "Home/Draw/Away");
        assertThat(eventNames, is(expectedEventNames));
    }

    //......................................................................
    // Collection of incomplete event names based on provided filter.
    //......................................................................

    /**
     * - The correct functionality is checked the String that the method return.
     * - It must contain the data structure of events repository in Json format
     */

    @Test
    public void testDumpFullStructure() throws Exception {
        //  ClientImpl client = new ClientImpl();
        EventsRepository.clearToMap();
        Collection<MarketRefType> listmarketRefType = new ArrayList<MarketRefType>();
        listmarketRefType.add(new MarketRefType(1, "Over/Under"));
        listmarketRefType.add(new MarketRefType(2, "spread betting"));
        listmarketRefType.add(new MarketRefType(3, "Banker"));
        EventsRepository.putToMap(1, new Event(
                1,
                "Manchester United v Arsenal",
                new SubCategory(1, "Premier League", new Category(1, "Football")),
                listmarketRefType,
                false));
        String expected = "{\n" + "  \"1\": {\n" + "    \"id\": 1,\n" + "    \"name\": \"Manchester United v Arsenal\",\n" + "    \"subCategory\": {\n" + "      \"id\": 1,\n" + "      \"ref\": \"Premier League\",\n" + "      \"category\": {\n" + "        \"id\": 1,\n" + "        \"ref\": \"Football\"\n" + "      }\n" + "    },\n" + "    \"marketRefTypes\": [\n" + "      {\n" + "        \"marketRefId\": 1,\n" + "        \"marketRefName\": \"Over/Under\"\n" + "      },\n" + "      {\n" + "        \"marketRefId\": 2,\n" + "        \"marketRefName\": \"spread betting\"\n" + "      },\n" + "      {\n" + "        \"marketRefId\": 3,\n" + "        \"marketRefName\": \"Banker\"\n" + "      }\n" + "    ],\n" + "    \"completed\": false\n" + "  }\n" + "}";
        String json = client.dumpFullStructure();
        assertEquals(json, expected);
    }
}
