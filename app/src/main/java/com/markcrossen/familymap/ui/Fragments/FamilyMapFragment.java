package com.markcrossen.familymap.ui.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.markcrossen.familymap.Constants;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.model.Event;
import com.markcrossen.familymap.model.Filter;
import com.markcrossen.familymap.model.Model;
import com.markcrossen.familymap.model.Person;
import com.markcrossen.familymap.model.Settings;
import com.markcrossen.familymap.ui.Activities.PersonActivity;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FamilyMapFragment extends Fragment implements OnMapReadyCallback {

    private Context context;

    private GoogleMap map;
    private SupportMapFragment map_fragment;
    private TextView title_text_view;
    private TextView details_text_view;
    private ImageView gender_image_view;

    public FamilyMapFragment() {
        // Required empty public constructor
    }

    public static FamilyMapFragment newInstance() {
        FamilyMapFragment fragment = new FamilyMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_familymap, container, false);

        title_text_view = (TextView) v.findViewById(R.id.textClickMarker);
        details_text_view = (TextView) v.findViewById(R.id.textDetails);
        gender_image_view = (ImageView) v.findViewById(R.id.genderImage);

        gender_image_view.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_android).colorRes(R.color.AndroidIcon).sizeDp(50));

        LinearLayout details_layout = (LinearLayout)v.findViewById(R.id.DetailsBar);
        details_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailsClicked();
            }
        });

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (selected_event != null && !Model.getInstance().isEvent(selected_event.getID()))
        {
            selected_event = null;
        }

        FragmentManager fm = getFragmentManager();
        map_fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapFrameLayout);
        if (map_fragment == null || map == null) {
            map_fragment = SupportMapFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.mapFrameLayout, map_fragment)
                    .commit();
            map_fragment.getMapAsync(this);
        }
        else
        {
            setMarkers();
        }
    }

    private void onDetailsClicked()
    {
        if (selected_event != null)
        {
            Intent i = new Intent(getActivity(), PersonActivity.class);
            i.putExtra(Constants.PERSON_ACTIVITY_ARG_1, selected_event.getPersonID());
            startActivity(i);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        setMarkers();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerSelected(marker);
                return true;
            }
        });
    }

    private void setMarkers()
    {
        map.clear();

        Model model = Model.getInstance();
        Filter filter = Filter.getInstance();

        Collection<Event> events = filter.filterEvents(model.getEvents());

        Iterator<Event> events_index = events.iterator();

        while (events_index.hasNext()) {
            Event current_event = events_index.next();
            map.addMarker(new MarkerOptions()
                    .title(current_event.getID())
                    .position(current_event.getCoordinates())
                    .icon(markerColor(current_event.getDescription())));
        }

        if (selected_event != null)
        {
            selectEvent();
        }

        map.setMapType(Settings.getInstance().getMapBackground().getDisplayCode());
    }

    private BitmapDescriptor markerColor(String event_description)
    {
        switch (event_description)
        {
            case "baptism":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            case "birth":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
            case "census":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            case "christening":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            case "death":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
            case "marriage":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            default:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        }
    }

    private void markerSelected(Marker marker)
    {
        marker.hideInfoWindow();
        Model model = Model.getInstance();
        Event selected_event = model.getEvent(marker.getTitle());
        selectEvent(selected_event);
    }

    public void setSelectedEvent(Event event)
    {
        selected_event = event;
        if (map != null)
        {
            selectEvent();
        }
    }

    private Event selected_event;

    private void selectEvent(Event event)
    {
        setSelectedEvent(event);
    }

    private void selectEvent()
    {
        map.moveCamera(CameraUpdateFactory.newLatLng(selected_event.getCoordinates()));

        Model model = Model.getInstance();
        Person selected_person = model.getPerson(selected_event.getPersonID());

        title_text_view.setText(selected_event.getTitle());
        details_text_view.setText(selected_event.getDescription());
        if (selected_person.getGender().equals("Male"))
        {
            gender_image_view.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_male).colorRes(R.color.MaleIcon).sizeDp(50));
        }
        else if (selected_person.getGender().equals("Female"))
        {
            gender_image_view.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_female).colorRes(R.color.FemaleIcon).sizeDp(50));
        }
        else
        {
            gender_image_view.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_android).colorRes(R.color.AndroidIcon).sizeDp(50));
        }


        addSpouseLine(selected_event);
        addPersonLine(selected_event);
        addTreeLines(selected_event);
    }

    private void addSpouseLine(Event selected_event)
    {
        //remove old spouse line
        if (spouse_line != null)
        {
            spouse_line.remove();
            spouse_line = null;
        }
        //add spouse line
        Settings settings = Settings.getInstance();
        if (settings.isSpouseLineEnabled())
        {
            Event spouse_event = Model.getInstance().getSpouseEvent(selected_event.getPersonID());
            if (spouse_event != null) {
                spouse_line = map.addPolyline(new PolylineOptions()
                        .add(selected_event.getCoordinates(), spouse_event.getCoordinates())
                        .width(Constants.MAP_LINE_SIZE)
                        .color(settings.getSpouseLineColor().getColorCode()));
            }
        }
    }
    Polyline spouse_line;

    private void addPersonLine(Event selected_event)
    {
        //remove old person line
        if (person_line != null)
        {
            person_line.remove();
            person_line = null;
        }
        //add person line
        Settings settings = Settings.getInstance();
        if (settings.isLifeLineEnabled())
        {
            Collection person_events = Model.getInstance().getFilteredPersonEvents(selected_event.getPersonID());
            if (person_events.size() > 1) {
                //create list of coordinates
                List<LatLng> coords = new LinkedList<>();
                Iterator<Event> index = person_events.iterator();
                while (index.hasNext()) {
                    coords.add(index.next().getCoordinates());
                }
                person_line = map.addPolyline(new PolylineOptions()
                        .addAll(coords)
                        .width(Constants.MAP_LINE_SIZE)
                        .color(Settings.getInstance().getLifeLineColor().getColorCode()));
            } else {
                person_line = null;
            }
        }
    }
    Polyline person_line;

    private void addTreeLines(Event selected_event)
    {
        //remove the old lines
        if (tree_lines != null)
        {
            Iterator<Polyline> index = tree_lines.iterator();

            while(index.hasNext())
            {
                index.next().remove();
            }
            tree_lines = null;
        }
        //add the new lines
        Settings settings = Settings.getInstance();
        if (settings.isFamilyTreeEnabled())
        {
            tree_lines = new LinkedList<>();

            addTreeLinesHelper(selected_event, Constants.MAP_LINE_SIZE);
        }
    }
    List<Polyline> tree_lines;

    private void addTreeLinesHelper(Event current_event, int width)
    {
        if (width > 0)
        {
            Settings settings = Settings.getInstance();
            Model model = Model.getInstance();
            Person child = model.getPerson(current_event.getPersonID());

            if (child.hasFather()) {
                Event father_event = model.getFilteredEarliestEvent(child.getFather());
                if (father_event != null)
                {
                    tree_lines.add(map.addPolyline(new PolylineOptions()
                            .add(current_event.getCoordinates(), father_event.getCoordinates())
                            .width(width)
                            .color(settings.getFamilyTreeColor().getColorCode())));

                    addTreeLinesHelper(father_event, width - 1);
                }
            }

            if (child.hasMother()) {
                Event mother_event = model.getFilteredEarliestEvent(child.getMother());
                if (mother_event != null)
                {
                    tree_lines.add(map.addPolyline(new PolylineOptions()
                            .add(current_event.getCoordinates(), mother_event.getCoordinates())
                            .width(width)
                            .color(settings.getFamilyTreeColor().getColorCode())));

                    addTreeLinesHelper(mother_event, width - 3);
                }
            }
        }
    }
}