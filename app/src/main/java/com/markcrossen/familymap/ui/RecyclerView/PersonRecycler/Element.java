package com.markcrossen.familymap.ui.RecyclerView.PersonRecycler;

import com.joanzapata.android.iconify.IconDrawable;

public abstract class Element {

    private String text_upper;
    private String text_lower;
    private IconDrawable icon;
    private String id;

    public Element(String text_upper, String text_lower, IconDrawable icon, String id) {
        this.text_upper = text_upper;
        this.text_lower = text_lower;
        this.icon = icon;
        this.id = id;
    }

    public String getTextUpper() {
        return text_upper;
    }

    public String getTextLower() {
        return text_lower;
    }

    public IconDrawable getIcon() {
        return icon;
    }

    public abstract int getParent();

    public String getId()
    {
        return id;
    }

}
