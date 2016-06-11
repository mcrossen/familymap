package com.markcrossen.familymap.ui.RecyclerView.FilterRecycler;

import com.joanzapata.android.iconify.IconDrawable;
import com.markcrossen.familymap.model.Filter;

public class FilterElement {

    private String text_upper;
    private String text_lower;
    private String id;
    private Filter.Type parent;

    public FilterElement(String description, boolean is_checked, Filter.Type parent) {
        switch(parent)
        {
            case DESCRIPTION:
                text_upper = capitalizeWords(description) + " Events";
                text_lower = "FILTER BY " + description.toUpperCase() + " EVENTS";
                break;
            case MALE_GENDER:
                text_upper = "Male Events";
                text_lower = "FILTER EVENTS BASED ON GENDER";
                break;
            case FEMALE_GENDER:
                text_upper = "Female Events";
                text_lower = "FILTER EVENTS BASED ON GENDER";
                break;
            case FATHER_SIDE:
                text_upper = "Father's Side";
                text_lower = "FILTER BY FATHER'S SIDE OF FAMILY";
                break;
            case MOTHER_SIDE:
                text_upper = "Mother's Side";
                text_lower = "FILTER BY MOTHER'S SIDE OF FAMILY";
                break;
            default:
                assert(false);
        }
        this.parent = parent;
        this.id = description;
    }

    String capitalizeWords(String text)
    {
        for (int index = 0; index < text.length(); index++)
        {
            if (text.charAt(index) == ' ')
            {
                if (index + 1 < text.length())
                {
                    text = text.substring(0, index) + text.substring(index + 1, index + 2).toUpperCase() + text.substring(index + 2);
                }
            }
            if (index == 0)
            {
                text = text.substring(index, index + 1).toUpperCase() + text.substring(index + 1);
            }
        }
        return text;
    }

    public String getTextUpper() {
        return text_upper;
    }

    public String getTextLower() {
        return text_lower;
    }

    public Filter.Type getParent()
    {
        return parent;
    }

    public String getId()
    {
        return id;
    }

}
