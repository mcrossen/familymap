package com.markcrossen.familymap.ui.RecyclerView.PersonRecycler;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

public class Section implements ParentObject {

    private List<Object> children = new ArrayList<>();

    public Section(String title)
    {
        super();
        this.title = title;
    }

    @Override
    public List<Object> getChildObjectList()
    {
        return children;
    }

    @Override
    public void setChildObjectList(List<Object> list)
    {
        children = list;
    }

    public void addChild(Object child)
    {
        children.add(child);
    }

    String title;

    public String getTitle()
    {
        return title;
    }
}
