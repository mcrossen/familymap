package com.markcrossen.familymap.ui.RecyclerView.PersonRecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.markcrossen.familymap.R;
import com.markcrossen.familymap.ui.Activities.RecyclerActivity;

import java.util.List;

public class PersonExpandableAdapter extends ExpandableRecyclerAdapter<ParentViewHolder, ChildViewHolder> implements FamilyMapAdapter {

    private LayoutInflater mInflater;
    private RecyclerActivity activity;

    public PersonExpandableAdapter(Context context, RecyclerActivity activity, List<ParentObject> objects)
    {
        super(context, objects);
        this.activity = activity;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.person_parent_element, viewGroup, false);
        return new ParentViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.person_child_element, viewGroup, false);
        return new ChildViewHolder(view, this);
    }

    @Override
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int i, Object parentObject) {
        Section section = (Section) parentObject;
        parentViewHolder.personTitleText.setText(section.getTitle());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int i, Object childObject) {
        Element child = (Element) childObject;
        childViewHolder.personChildImage.setImageDrawable(child.getIcon());
        childViewHolder.personChildTextUpper.setText(child.getTextUpper());
        childViewHolder.personChildTextLower.setText(child.getTextLower());
        childViewHolder.setCallBack(child.getParent(), child.getId());
    }

    public void elementClicked(int parent_index, String id)
    {
        activity.onElementClicked(parent_index, id);
    }
}
