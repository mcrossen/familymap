package com.markcrossen.familymap.ui.RecyclerView.PersonRecycler;

import com.joanzapata.android.iconify.IconDrawable;

public class EventElement extends Element {

    public EventElement(String text_upper, String text_lower, IconDrawable icon, String id)
    {
        super(text_upper, text_lower, icon, id);
    }

    public int getParent()
    {
        return 1;
    }
}