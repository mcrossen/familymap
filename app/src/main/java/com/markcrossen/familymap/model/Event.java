package com.markcrossen.familymap.model;

import com.google.android.gms.maps.model.LatLng;

public class Event implements Comparable {

    private String id;
    private String person_id;
    private String person_name = "Unnamed";
    private LatLng coordinates;
    private String country;
    private String city;
    private String description;
    private int year;
    
    public Event(String eventID, String personID, double latitude, double longitude, String country, String city, String description, int year)
    {
        this.id = eventID;
        this.person_id = personID;
        coordinates = new LatLng(latitude, longitude);
        this.country = country;
        this.city = city;
        this.description = description;
        this.year = year;
    }

    public void setPersonName(String name)
    {
        person_name = name;
    }

    public String getID()
    {
        return id;
    }

    public String getPersonID()
    {
        return person_id;
    }

    public String getTitle()
    {
        return person_name;
    }

    public int getYear()
    {
        return year;
    }

    public String getDescription()
    {
        return description;
    }

    public LatLng getCoordinates()
    {
        return coordinates;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    @Override
    public int compareTo(Object another) {
        if (another.getClass() != getClass())
        {
            return -2;
        }
        else
        {
            Event other = (Event)another;
            if (description.equals("birth") && !other.description.equals("birth"))
            {
                return -1;
            }
            else if (!description.equals("birth") && other.description.equals("birth"))
            {
                return 1;
            }
            else
            {
                if (description.equals("death") && !other.description.equals("death"))
                {
                    return 1;
                }
                else if (!description.equals("death") && other.description.equals("death"))
                {
                    return -1;
                }
                else
                {
                    if (year < other.year)
                    {
                        return -1;
                    }
                    else if (year > other.year)
                    {
                        return 1;
                    }
                    else
                    {
                        return description.compareTo(other.description);
                    }
                }
            }
        }

    }
}
