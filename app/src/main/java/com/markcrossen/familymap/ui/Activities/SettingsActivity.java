package com.markcrossen.familymap.ui.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.markcrossen.familymap.Constants;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.ServerAccess.ServerInfo;
import com.markcrossen.familymap.model.Model;
import com.markcrossen.familymap.model.Settings;
import com.markcrossen.familymap.model.SyncActivity;

public class SettingsActivity extends AppCompatActivity implements SyncActivity {

    Settings settings = Settings.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spinner_story = (Spinner)findViewById(R.id.LifeStorySpinner);
        spinner_story.setAdapter(new ArrayAdapter<Settings.LineColor>(this, android.R.layout.simple_spinner_item, Settings.LineColor.values()));
        spinner_story.setSelection(settings.getLifeLineColor().getIndex());
        spinner_story.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Settings.LineColor color = (Settings.LineColor) parent.getItemAtPosition(pos);
                settings.setLifeLineColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Spinner spinner_tree = (Spinner)findViewById(R.id.FamilyTreeSpinner);
        spinner_tree.setAdapter(new ArrayAdapter<Settings.LineColor>(this, android.R.layout.simple_spinner_item, Settings.LineColor.values()));
        spinner_tree.setSelection(settings.getFamilyTreeColor().getIndex());
        spinner_tree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Settings.LineColor color = (Settings.LineColor) parent.getItemAtPosition(pos);
                settings.setFamilyTreeColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
                
        Spinner spinner_spouse = (Spinner)findViewById(R.id.SpouseSpinner);
        spinner_spouse.setAdapter(new ArrayAdapter<Settings.LineColor>(this, android.R.layout.simple_spinner_item, Settings.LineColor.values()));
        spinner_spouse.setSelection(settings.getSpouseLineColor().getIndex());
        spinner_spouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Settings.LineColor color = (Settings.LineColor) parent.getItemAtPosition(pos);
                settings.setSpouseLineColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
                
        Spinner spinner_map = (Spinner)findViewById(R.id.BackgroundSpinner);
        spinner_map.setAdapter(new ArrayAdapter<Settings.MapBackground>(this, android.R.layout.simple_spinner_item, Settings.MapBackground.values()));
        spinner_map.setSelection(settings.getMapBackground().getIndex());
        spinner_map.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Settings.MapBackground bg = (Settings.MapBackground) parent.getItemAtPosition(pos);
                settings.setMapBackground(bg);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        LinearLayout sync_layout = (LinearLayout)findViewById(R.id.SyncRow);
        sync_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSyncClicked();
            }
        });

        LinearLayout logout_layout = (LinearLayout)findViewById(R.id.LogoutRow);
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClicked();
            }
        });

        Switch life_story_toggle = (Switch)findViewById(R.id.LifeStoryToggle);
        life_story_toggle.setChecked(settings.isLifeLineEnabled());
        life_story_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLifeLine(isChecked);
            }
        });

        Switch family_tree_toggle = (Switch)findViewById(R.id.FamilyTreeToggle);
        family_tree_toggle.setChecked(settings.isFamilyTreeEnabled());
        family_tree_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFamilyTree(isChecked);
            }
        });

        Switch spouse_toggle = (Switch)findViewById(R.id.SpouseToggle);
        spouse_toggle.setChecked(settings.isSpouseLineEnabled());
        spouse_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSpouseLine(isChecked);
            }
        });
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

    private void onSyncClicked()
    {
        Model.getInstance().syncData(this);
    }

    private void onLogoutClicked()
    {
        ServerInfo.getInstance().clearLogin();

        goToTop();
    }

    private void goToTop()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void SyncError(Exception error)
    {
        Log.e(Constants.TAG, "Unable to retrieve data. " + error.getMessage());
        Toast.makeText(this, "ERROR: unable to retrieve data.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void SyncDone()
    {
        Toast.makeText(this, "Sync Finished", Toast.LENGTH_LONG).show();
        goToTop();
    }
}
