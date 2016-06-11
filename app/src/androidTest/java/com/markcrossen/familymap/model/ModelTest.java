package com.markcrossen.familymap.model;

import junit.framework.TestCase;

import java.util.Iterator;

/*
To the TA that grades this test:
My model class has the sync functionality built directly into it. Because of that, the sync methods
are tested in the setUp method so that the data can be used in the rest of the test.
I don't completely understand how to test unknown input data from a server, so I basically tested
what I do know about the data (there is at least 1 person and 1 event per person, etc)
*/
public class ModelTest extends TestCase {

    private Model model = Model.getInstance();
    private String user_id;

    public void setUp() throws Exception
    {
        SyncTest sync = new SyncTest();
        sync.testSync();
        user_id = Filter.getInstance().getUser(); // Where I store my user (I didn't use the user anywhere but the filter)
    }

    /*
    Since we can't know exactly what is in the returned data from the server, we can only check what
    we do know about it.
     */
    public void testGetPeople() throws Exception {
        // Check that people exist
        assertTrue(model.getPeople().size() > 0);

        // Check that the user is inside the people list
        assertNotNull(user_id);
        Person user = model.getPerson(user_id); // This line pulls the user info from the list of people. Returns null if error.
        assertNotNull(user_id);
    }

    public void testGetEvents() throws Exception {
        // Check that events exist
        assertTrue(model.getEvents().size() > 0);
    }

    public void testGetPersonEvents() throws Exception {
        // Check that there are events for the user and one other user
        assertTrue(model.getPersonEvents(user_id).size() > 0);
        String other_id = model.getPeople().iterator().next().getID();
        assertTrue(model.getPersonEvents(other_id).size() > 0);

        // Check that birth is always first.
        Iterator<Event> event_index = model.getPersonEvents(user_id).iterator();
        event_index.next();// Increment iterator to make sure 'birth' isn't after the first position
        while (event_index.hasNext())
        {
            if (event_index.next().getDescription().equals("birth"))
            {
                throw new Exception();
            }
        }

        // Check that death is always last.
        event_index = model.getPersonEvents(user_id).iterator();
        while (event_index.hasNext())
        {
            if (event_index.next().getDescription().equals("death") && event_index.hasNext())
            {
                throw new Exception();
            }
        }
    }

    public void testGetPerson() throws Exception {
        // test that the main user exists
        assertNotNull(model.getPerson(user_id));
        // test that another user is returned properly
        String other_id = model.getPeople().iterator().next().getID();
        assertNotNull(model.getPerson(other_id));
    }

    public void testGetEvent() throws Exception {
        // Compare with results from one of the user's events
        Event user_event = model.getPersonEvents(user_id).iterator().next();
        assertEquals(user_event, model.getEvent(user_event.getID()));

        // Compare with another event
        Event other_event = model.getEvents().iterator().next();
        assertEquals(other_event, model.getEvent(other_event.getID()));
    }

    public void testIsEvent() throws Exception {
        //make sure the following aren't event keys
        assertFalse(model.isEvent(null));
        assertFalse(model.isEvent("this is a test"));
        assertFalse(model.isEvent("aaaa"));

        //make sure the following are keys
        assertTrue(model.isEvent(model.getEarliestEvent(user_id).getID()));
        assertTrue(model.isEvent(model.getEvents().iterator().next().getID()));
        assertTrue(model.isEvent(model.getPersonEvents(user_id).iterator().next().getID()));
    }

    public void testGetEarliestEvent() throws Exception {
        // test that the user's first event is either a "birth" or earliest by year
        Event earliest = model.getEarliestEvent(user_id);
        if (earliest.getDescription() != "birth")
        {
            Iterator<Event> users_events = model.getPersonEvents(user_id).iterator();
            while (users_events.hasNext())
            {
                assertFalse(users_events.next().getYear() < earliest.getYear());
            }
        }

        // same thing, but with a different person
        String other_id = model.getPeople().iterator().next().getID();
        earliest = model.getEarliestEvent(other_id);
        if (earliest.getDescription() != "birth")
        {
            Iterator<Event> users_events = model.getPersonEvents(other_id).iterator();
            while (users_events.hasNext())
            {
                assertFalse(users_events.next().getYear() < earliest.getYear());
            }
        }
    }

    public void testGetSpouseEvent() throws Exception {
        // The spouse event should be the earliest event of one's spouse.

        // Find someone with a spouse
        Person current = model.getPerson(user_id);
        Iterator<Person> find = model.getPeople().iterator();
        while (find.hasNext())
        {
            current = find.next();
            if (current.getSpouse() != null)
            {
                break;
            }
        }

        if (find.hasNext())
        {
            // check that the spouse event matches the spouse's first event
            Event spouse_first_event = model.getEarliestEvent(current.getSpouse());
            assertEquals(model.getSpouseEvent(current.getID()), spouse_first_event);
        }
    }
}