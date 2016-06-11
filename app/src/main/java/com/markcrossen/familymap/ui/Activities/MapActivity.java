package com.markcrossen.familymap.ui.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.markcrossen.familymap.Constants;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.model.Event;
import com.markcrossen.familymap.model.Model;
import com.markcrossen.familymap.ui.Fragments.FamilyMapFragment;

public class MapActivity extends AppCompatActivity {

    Model model = Model.getInstance();

    Event selected_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent i = getIntent();
        String event_id = i.getStringExtra(Constants.MAP_ACTIVITY_ARG_1);
        selected_event = model.getEvent(event_id);

        FragmentManager fm = this.getSupportFragmentManager();
        FamilyMapFragment familymap_fragment = FamilyMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.familyMapFrameLayout, familymap_fragment).commit();

        familymap_fragment.setSelectedEvent(selected_event);
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
}
