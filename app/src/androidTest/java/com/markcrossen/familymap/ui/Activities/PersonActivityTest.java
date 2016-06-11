package com.markcrossen.familymap.ui.Activities;

import com.markcrossen.familymap.model.Filter;
import com.markcrossen.familymap.model.Model;
import com.markcrossen.familymap.model.Person;
import com.markcrossen.familymap.model.SyncTest;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.PersonElement;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//The code that calculates family relationships is in the Person Activity class, I will test it here as best I can.
public class PersonActivityTest extends TestCase {

    String test_person_id;
    Model model = Model.getInstance();

    public void setUp() throws Exception {
        super.setUp();
        SyncTest sync = new SyncTest();
        sync.testSync();
        if (test_person_id == null)
        {
            test_person_id = Filter.getInstance().getUser();
        }
        assertTrue(model.getPeople().size() > 0);
    }

    /**
     * Since I couldn't test the original code properly due to it being in activity (the debugger didn't like
     * how I tried creating an iconify object), I tested what I could from the original method, but copied a
     * few things into this method and made some small tweaks so it would work (removed iconify objects)
     * @throws Exception
     */
    public void testGetFamily() throws Exception {
        //get the calculated family
        List<Object> family_elements = (new PersonActivity()).getFamily(test_person_id);

        //convert to a list of people
        List<Person> parents = new ArrayList<>();
        List<Person> children = new ArrayList<>();
        Iterator<Object> index = family_elements.iterator();
        while (index.hasNext())
        {
            PersonElement current = (PersonElement) index.next();

            if (current.getTextLower().equals("FATHER") || current.getTextLower().equals("MOTHER"))
            {
                parents.add(model.getPerson(current.getId()));
            }
            else
            {
                children.add(model.getPerson(current.getId()));
            }
        }

        //basically, everyone who isn't the user has at least one child, and the user has at least one parent.
        if (test_person_id.equals(Filter.getInstance().getUser()))
        {
            assertTrue(parents.size() > 0);
        }
        else
        {
            assertTrue(children.size() > 0);
        }
    }

    /**
     * This goes through the entire data set and runs the above test on each person. It isn't doesn't
     * directly test a method used in my code.
     * @throws Exception
     */
    public void testAllFamilies() throws Exception
    {
        Iterator<Person> index = model.getPeople().iterator();
        while (index.hasNext())
        {
            test_person_id = index.next().getID();
            testGetFamily();
        }
    }
}