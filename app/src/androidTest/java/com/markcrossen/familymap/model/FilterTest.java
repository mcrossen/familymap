package com.markcrossen.familymap.model;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.Iterator;


/*
To the TA that grades this code:
I don't completely understand how to test unknown input data from a server, so I basically created
tests that turn on a few filters and check the filtered data to make sure the data doesn't have
those values in it.
 */
public class FilterTest extends TestCase {

    Filter filter = Filter.getInstance();
    Model model = Model.getInstance();
    String hidden_description_1; // randomly chosen description to hide while testing filters (i.e. birth, baptism, etc)
    String hidden_description_2; // randomly chosen description to hide while testing filters (i.e. birth, baptism, etc)

    public void setUp() throws Exception {
        super.setUp();
        SyncTest sync = new SyncTest();
        sync.testSync();

        filter.male_checked = false;// Filter out males for this test
        filter.father_checked = false;// Filter out the father's side of the ancestory for this test
        hidden_description_1 = model.getEvents().iterator().next().getDescription(); // Filter a random event description
        hidden_description_2 = model.getEarliestEvent(filter.getUser()).getID(); // Filter another random event description
        filter.setChecked(Filter.Type.DESCRIPTION, hidden_description_1, false);
        filter.setChecked(Filter.Type.DESCRIPTION, hidden_description_2, false);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        filter.male_checked = true;
        filter.father_checked = true;
        filter.setChecked(Filter.Type.DESCRIPTION, hidden_description_1, true);
        filter.setChecked(Filter.Type.DESCRIPTION, hidden_description_2, true);
    }

    /**
     * tests whether the events were filtered according to the set filters in the setup method.
     * @throws Exception
     */
    public void testFilterEvents() throws Exception {
        Collection<Event> filtered_events = filter.filterEvents(model.getEvents());
        Iterator<Event> current_event = filtered_events.iterator();
        while (current_event.hasNext()) {
            Event test_event = current_event.next();
            assertNotSame(test_event.getDescription(), hidden_description_1);
            assertNotSame(test_event.getDescription(), hidden_description_2);
            Person event_person = model.getPerson(test_event.getPersonID());
            assertNotSame(event_person.getGender(), "Male");
            assertFalse(isFatherAncestor(event_person));
        }
    }

    /**
     * Helper function to determine if person is on the father's side of the user
     * @param person
     * @return
     */
    private boolean isFatherAncestor(Person person)
    {
        Person user = model.getPerson(filter.getUser());
        return isAncestor(person, user.getFather());
    }

    /**
     * A recursive method to determine if a person is on the father's side of the user. This aids in
     * verifying that any data set recieved from the server for this test is filtered properly.
     * @param to_find
     * @param current
     * @return
     */
    private boolean isAncestor(Person to_find, String current) // Recursive helper function to determine ancestory
    {
        if (current != null)
        {
            Person person = model.getPerson(current);
            if (isAncestor(to_find, person.getFather()))
            {
                return true;
            }
            else if (isAncestor(to_find, person.getMother()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * The user's id is kept in the filter since i had no use for it elsewhere
     * This method tests the getter for the user's id.
     * @throws Exception
     */
    public void testGetUser() throws Exception {
        assertNotNull(filter.getUser());

        // Compares against the model
        assertNotNull(model.getPerson(filter.getUser()));
    }

    /**
     * Tests the getter for the method that determines whether or not to display the option to
     * select or deselect the father filter. If the user has no father, this method returns false.
     * @throws Exception
     */
    public void testShowFatherSide() throws Exception {
        if (model.getPerson(filter.getUser()).hasFather())
        {
            assertTrue(filter.showFatherSide());
        }
        else
        {
            assertFalse(filter.showFatherSide());
        }
    }

    /**
     * Tests the getter for the method that determines whether or not to display the option to
     * select or deselect the mother filter. If the user has no mother, this method returns false.
     * @throws Exception
     */
    public void testShowMotherSide() throws Exception {
        if (model.getPerson(filter.getUser()).hasMother())
        {
            assertTrue(filter.showMotherSide());
        }
        else
        {
            assertFalse(filter.showMotherSide());
        }
    }

    /**
     * Tests the getter for the method that determines whether or not to display the option to
     * select or deselect the male filter. If the data has no male people in it, this method returns
     * false.
     * @throws Exception
     */
    public void testShowMales() throws Exception {
        //check if the model has male people
        boolean has_males = false;
        Iterator<Person> person = model.getPeople().iterator();
        while(person.hasNext())
        {
            if (person.next().getGender().equals("Male"))
            {
                has_males = true;
                break;
            }
        }
        //check that the filter will show the option on the menu
        assertEquals(filter.showMales(), has_males);
    }

    /**
     * Tests the getter for the method that determines whether or not to display the option to
     * select or deselect the female filter. If the data has no female people in it, this method returns
     * false.
     * @throws Exception
     */
    public void testShowFemales() throws Exception {
        //check if the model has female people
        boolean has_females = false;
        Iterator<Person> person = model.getPeople().iterator();
        while(person.hasNext())
        {
            if (person.next().getGender().equals("Female"))
            {
                has_females = true;
                break;
            }
        }
        //check that the filter will show the option on the menu
        assertEquals(filter.showMales(), has_females);
    }

    /**
     * returns the set of strings that contains all the unique descriptions of the data events.
     * @throws Exception
     */
    public void testGetFilters() throws Exception {
        Collection<String> filters = filter.getFilters();
        assertNotNull(filters);
        assertTrue(filters.contains(hidden_description_1));
        assertTrue(filters.contains(hidden_description_2));
    }

    /**
     * verifies that the filters are turned on or off as desired.
     * @throws Exception
     */
    public void testIsChecked() throws Exception {
        assertFalse(filter.isChecked(Filter.Type.DESCRIPTION, hidden_description_1));
        assertFalse(filter.isChecked(Filter.Type.DESCRIPTION, hidden_description_2));
        assertFalse(filter.isChecked(Filter.Type.MALE_GENDER, null));
        assertFalse(filter.isChecked(Filter.Type.FATHER_SIDE, null));
        assertTrue(filter.isChecked(Filter.Type.FEMALE_GENDER, null));
        assertTrue(filter.isChecked(Filter.Type.MOTHER_SIDE, null));
    }
}