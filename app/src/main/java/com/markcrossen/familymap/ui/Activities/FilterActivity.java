package com.markcrossen.familymap.ui.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.model.Event;
import com.markcrossen.familymap.model.Filter;
import com.markcrossen.familymap.model.Model;
import com.markcrossen.familymap.model.Person;
import com.markcrossen.familymap.ui.RecyclerView.FilterRecycler.FilterAdapter;
import com.markcrossen.familymap.ui.RecyclerView.FilterRecycler.FilterElement;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.Element;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.EventElement;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.PersonElement;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.SearchAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class FilterActivity extends AppCompatActivity {

    Filter filter = Filter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        RecyclerView list_view = (RecyclerView) findViewById(R.id.filterRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(manager);

        FilterAdapter adapter = new FilterAdapter(this, generateElements());

        list_view.setAdapter(adapter);
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

    private ArrayList<FilterElement> generateElements()
    {
        ArrayList<FilterElement> to_return = new ArrayList<>();

        Filter filter = Filter.getInstance();
        Iterator<String> index = filter.getFilters().iterator();

        while(index.hasNext())
        {
            String description = index.next();
            to_return.add(new FilterElement(description, filter.isChecked(Filter.Type.DESCRIPTION, description), Filter.Type.DESCRIPTION));
        }

        if (filter.showFatherSide())
        {
            to_return.add(new FilterElement(null, filter.isChecked(Filter.Type.FATHER_SIDE, null), Filter.Type.FATHER_SIDE));
        }

        if (filter.showMotherSide())
        {
            to_return.add(new FilterElement(null, filter.isChecked(Filter.Type.MOTHER_SIDE, null), Filter.Type.MOTHER_SIDE));
        }

        if (filter.showMales())
        {
            to_return.add(new FilterElement(null, filter.isChecked(Filter.Type.MALE_GENDER, null), Filter.Type.MALE_GENDER));
        }

        if (filter.showFemales())
        {
            to_return.add(new FilterElement(null, filter.isChecked(Filter.Type.FEMALE_GENDER, null), Filter.Type.FEMALE_GENDER));
        }

        return to_return;
    }

}
