package com.markcrossen.familymap.ui.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.markcrossen.familymap.Constants;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.model.Event;
import com.markcrossen.familymap.model.Model;
import com.markcrossen.familymap.model.Person;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.EventElement;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.PersonElement;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.PersonExpandableAdapter;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.Section;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonActivity extends AppCompatActivity implements RecyclerActivity {

    private Model model = Model.getInstance();

    private Person selected_person;

    private RecyclerView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent i = getIntent();
        String person_id = i.getStringExtra(Constants.PERSON_ACTIVITY_ARG_1);
        selected_person = model.getPerson(person_id);

        TextView text_first_name = (TextView) findViewById(R.id.textFirstName);
        TextView text_last_name = (TextView) findViewById(R.id.textLastName);
        TextView text_gender = (TextView) findViewById(R.id.textGender);

        text_first_name.setText(selected_person.getFirstName());
        text_last_name.setText(selected_person.getLastName());
        text_gender.setText(selected_person.getGender());

        list_view = (RecyclerView) findViewById(R.id.personRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(manager);

        PersonExpandableAdapter adapter = new PersonExpandableAdapter(this, this, generateElements());
        adapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        list_view.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);

        MenuItem menu_top = menu.findItem(R.id.map_menu_top).setIcon(
                new IconDrawable(this, Iconify.IconValue.fa_angle_double_up).colorRes(R.color.MenuIcons).sizeDp(20));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.map_menu_top:
                goToTop();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToTop()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public List<Object> getFamily(String person_id) // Used for testing. basically same method as below but without drawables (which aren't accessable within tests)
    {
        selected_person = model.getPerson(person_id);
        Section family = new Section("FAMILY");
        if (selected_person.hasFather())
        {
            Person father = model.getPerson(selected_person.getFather());
            family.addChild(new PersonElement(father.getFullName(), "FATHER", null, father.getID()));
        }
        if (selected_person.hasMother())
        {
            Person mother = model.getPerson(selected_person.getMother());
            family.addChild(new PersonElement(mother.getFullName(), "MOTHER", null, mother.getID()));
        }
        Iterator<Person> person_index = model.getPeople().iterator();
        while(person_index.hasNext())
        {
            Person current = person_index.next();
            boolean add = false;
            if (current.hasMother() && current.getMother().equals(selected_person.getID()))
            {
                add = true;
            }
            if (current.hasFather() && current.getFather().equals(selected_person.getID()))
            {
                add = true;
            }
            if (add)
            {
                if (current.getGender().equals("Male"))
                {
                    family.addChild(new PersonElement(current.getFullName(), "SON", null, current.getID()));

                }
                else if (current.getGender().equals("Female"))
                {
                    family.addChild(new PersonElement(current.getFullName(), "DAUGHTER", null, current.getID()));

                }
                else
                {
                    family.addChild(new PersonElement(current.getFullName(), "CHILD", null, current.getID()));
                }
            }

        }
        return family.getChildObjectList();
    }

    private ArrayList<ParentObject> generateElements()
    {
        ArrayList<ParentObject> to_return = new ArrayList<>();

        Section life_events = new Section("LIFE EVENTS");
        Iterator<Event> event_index = model.getPersonEvents(selected_person.getID()).iterator();
        while(event_index.hasNext())
        {
            Event current_event = event_index.next();
            life_events.addChild(new EventElement(current_event.getDescription() + ": " + current_event.getCity() + ", " + current_event.getCountry() + "(" + current_event.getYear() + ")",
                    selected_person.getFullName(),
                    new IconDrawable(this, Iconify.IconValue.fa_map_marker).colorRes(R.color.EventIcon),
                    current_event.getID()));
        }
        to_return.add(life_events);

        Section family = new Section("FAMILY");
        if (selected_person.hasFather())
        {
            Person father = model.getPerson(selected_person.getFather());
            family.addChild(new PersonElement(father.getFullName(), "FATHER", new IconDrawable(this, Iconify.IconValue.fa_male).colorRes(R.color.MaleIcon), father.getID()));
        }
        if (selected_person.hasMother())
        {
            Person mother = model.getPerson(selected_person.getMother());
            family.addChild(new PersonElement(mother.getFullName(), "MOTHER", new IconDrawable(this, Iconify.IconValue.fa_female).colorRes(R.color.FemaleIcon), mother.getID()));
        }
        Iterator<Person> person_index = model.getPeople().iterator();
        while(person_index.hasNext())
        {
            Person current = person_index.next();
            boolean add = false;
            if (current.hasMother() && current.getMother().equals(selected_person.getID()))
            {
                add = true;
            }
            if (current.hasFather() && current.getFather().equals(selected_person.getID()))
            {
                add = true;
            }
            if (add)
            {
                if (current.getGender().equals("Male"))
                {
                    family.addChild(new PersonElement(current.getFullName(), "SON", new IconDrawable(this, Iconify.IconValue.fa_male).colorRes(R.color.MaleIcon), current.getID()));

                }
                else if (current.getGender().equals("Female"))
                {
                    family.addChild(new PersonElement(current.getFullName(), "DAUGHTER", new IconDrawable(this, Iconify.IconValue.fa_female).colorRes(R.color.FemaleIcon), current.getID()));

                }
                else
                {
                    family.addChild(new PersonElement(current.getFullName(), "CHILD", new IconDrawable(this, Iconify.IconValue.fa_android).colorRes(R.color.AndroidIcon), current.getID()));
                }
            }

        }
        to_return.add(family);

        return to_return;
    }


    @Override
    public void onElementClicked(int parent_index, String child_id)
    {
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
