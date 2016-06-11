package com.markcrossen.familymap.ui.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.markcrossen.familymap.Constants;
import com.markcrossen.familymap.ServerAccess.ServerInfo;
import com.markcrossen.familymap.model.Model;
import com.markcrossen.familymap.model.SyncActivity;
import com.markcrossen.familymap.ui.Fragments.LoginFragment;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.ui.Fragments.FamilyMapFragment;

public class MainActivity extends AppCompatActivity implements LoginActivity, SyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        LoginFragment login_fragment = (LoginFragment) fm.findFragmentById(R.id.mainFrameLayout);
        if (login_fragment == null) {
            startLoginFragment();
        }
        menu_enabled = false;

    }

    MenuItem menu_search;
    MenuItem menu_filter;
    MenuItem menu_settings;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu_search = menu.findItem(R.id.main_menu_search).setIcon(
                new IconDrawable(this, Iconify.IconValue.fa_search).colorRes(R.color.MenuIcons).sizeDp(20));
        menu_filter = menu.findItem(R.id.main_menu_filter).setIcon(
                new IconDrawable(this, Iconify.IconValue.fa_filter).colorRes(R.color.MenuIcons).sizeDp(20));
        menu_settings = menu.findItem(R.id.main_menu_settings).setIcon(
                new IconDrawable(this, Iconify.IconValue.fa_gear).colorRes(R.color.MenuIcons).sizeDp(20));

        refreshMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.main_menu_search:
                startSearchActivity();
                return true;
            case R.id.main_menu_filter:
                startFilterActivity();
                return true;
            case R.id.main_menu_settings:
                startSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loginSuccessful()
    {
        Model.getInstance().syncData(this);
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
        //go to the map fragment
        startMapFragment();
    }

    private boolean menu_enabled = false;

    private void enableMenu()
    {
        menu_enabled = true;
        refreshMenu();
    }

    private void disableMenu()
    {
        menu_enabled = false;
        refreshMenu();
    }

    private void refreshMenu()
    {
        if (menu_search != null)
        {
            menu_search.setVisible(menu_enabled);
        }
        if (menu_filter != null)
        {
            menu_filter.setVisible(menu_enabled);
        }
        if (menu_settings != null)
        {
            menu_settings.setVisible(menu_enabled);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!ServerInfo.getInstance().isLoggedOn())
        {
            startLoginFragment();
        }
    }

    private void startLoginFragment()
    {
        disableMenu();

        FragmentManager fm = this.getSupportFragmentManager();
        LoginFragment login_fragment = LoginFragment.newInstance();
        login_fragment.setloginActivity(this);
        fm.beginTransaction().replace(R.id.mainFrameLayout, login_fragment).commit();
    }

    private void startMapFragment()
    {
        enableMenu();

        FragmentManager fm = this.getSupportFragmentManager();
        FamilyMapFragment familymap_fragment = FamilyMapFragment.newInstance();
        fm.beginTransaction().replace(R.id.mainFrameLayout, familymap_fragment).commit();
    }

    private void startSearchActivity()
    {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
    }

    private void startFilterActivity()
    {
        Intent i = new Intent(this, FilterActivity.class);
        startActivity(i);
    }

    private void startSettingsActivity()
    {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
}