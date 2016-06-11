package com.markcrossen.familymap.ui.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.markcrossen.familymap.Constants;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.model.Event;
import com.markcrossen.familymap.model.Model;
import com.markcrossen.familymap.model.Person;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.Element;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.EventElement;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.PersonElement;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.SearchAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements RecyclerActivity {

    private Model model = Model.getInstance();

    private EditText search_input;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView list_view;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_input = (EditText) findViewById(R.id.searchInput);
        search_input.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if (keyCode == KeyEvent.KEYCODE_CALL) {
                    // Perform action on key press
                    newSearch();
                    return true;
                }
                return false;
            }
        });

        list_view = (RecyclerView) findViewById(R.id.searchRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(manager);

        adapter = new SearchAdapter(this, this, generateElements());

        list_view.setAdapter(adapter);
    }

    private void newSearch()
    {
        /*PersonExpandableAdapter adapter = new PersonExpandableAdapter(this, this, generateElements());
        adapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        list_view.swapAdapter(adapter, true);*/
        adapter.swap(generateElements());
    }

    private ArrayList<Element> generateElements()
    {
        ArrayList<Element> to_return = new ArrayList<>();

        String search_term = search_input.getText().toString();

        if (search_term.length() > 0)
        {
            Iterator<Person> person_index = searchPeople(search_term).iterator();
            while (person_index.hasNext()) {
                Person person = person_index.next();
                if (person.getGender().equals("Male")) {
                    to_return.add(new PersonElement(person.getFullName(), "", new IconDrawable(this, Iconify.IconValue.fa_male).colorRes(R.color.MaleIcon), person.getID()));
                } else if (person.getGender().equals("Female")) {
                    to_return.add(new PersonElement(person.getFullName(), "", new IconDrawable(this, Iconify.IconValue.fa_female).colorRes(R.color.FemaleIcon), person.getID()));
                } else {
                    to_return.add(new PersonElement(person.getFullName(), "", new IconDrawable(this, Iconify.IconValue.fa_android).colorRes(R.color.AndroidIcon), person.getID()));
                }
            }

            Iterator<Event> event_index = searchEvents(search_term).iterator();
            while (event_index.hasNext()) {
                Event current_event = event_index.next();
                to_return.add(new EventElement(current_event.getDescription() + ": " + current_event.getCity() + ", " + current_event.getCountry() + "(" + current_event.getYear() + ")",
                        model.getPerson(current_event.getPersonID()).getFullName(),
                        new IconDrawable(this, Iconify.IconValue.fa_map_marker).colorRes(R.color.EventIcon),
                        current_event.getID()));
            }
        }

        return to_return;
    }

    private Collection<Event> searchEvents(String search_term)
    {
        Model model = Model.getInstance();
        List<Event> to_return = new LinkedList<>();

        Iterator<Event> index = model.getEvents().iterator();

        while(index.hasNext())
        {
            Event current_event = index.next();
            if (searchEvent(current_event, search_term))
            {
                to_return.add(current_event);
            }
        }

        return to_return;
    }

    private Collection<Person> searchPeople(String search_term)
    {
        Model model = Model.getInstance();
        List<Person> to_return = new LinkedList<>();

        Iterator<Person> index = model.getPeople().iterator();

        while(index.hasNext())
        {
            Person current_person = index.next();
            if (searchPerson(current_person, search_term))
            {
                to_return.add(current_person);
            }
        }

        return to_return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean searchPerson(Person person, String search_term)
    {
        return (person.getFullName().indexOf(search_term) >= 0);
    }

    private boolean searchEvent(Event event, String search_term)
    {
        if (event.getCountry().indexOf(search_term) >= 0)
        {
            return true;
        }
        else if (event.getCity().indexOf(search_term) >= 0)
        {
            return true;
        }
        else if (event.getDescription().indexOf(search_term) >= 0)
        {
            return true;
        }
        else if (Integer.toString(event.getYear()).indexOf(search_term) >= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onElementClicked(int parent_index, String child_id) {
        if (parent_index == 2)
        {
            Intent i = new Intent(this, PersonActivity.class);
            i.putExtra(Constants.PERSON_ACTIVITY_ARG_1, child_id);
            startActivity(i);
        }
        else if (parent_index == 1)
        {
            Intent i = new Intent(this, MapActivity.class);
            i.putExtra(Constants.MAP_ACTIVITY_ARG_1, child_id);
            startActivity(i);
        }
        else
        {
            Log.e(Constants.TAG, "Invalid element selection ");
        }
    }
}
