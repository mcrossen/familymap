package com.markcrossen.familymap.ui.RecyclerView.FilterRecycler;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.markcrossen.familymap.R;
import com.markcrossen.familymap.model.Filter;
import com.markcrossen.familymap.ui.RecyclerView.PersonRecycler.FamilyMapAdapter;

public class FilterViewHolder extends com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder {

    public TextView filterTextUpper;
    public TextView filterTextLower;
    public Switch filterSwitch;
    private String my_id;
    private Filter.Type parent_index;

    public FilterViewHolder(View itemView)
    {
        super(itemView);

        filterTextUpper = (TextView) itemView.findViewById(R.id.FilterTitle);
        filterTextLower = (TextView) itemView.findViewById(R.id.FilterDescription);
        filterSwitch = (Switch) itemView.findViewById(R.id.FilterToggle);

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (filterSwitch != null)
                {
                    Filter.getInstance().setChecked(parent_index, my_id, isChecked);
                }
            }
        });
    }


    public void setCallBack(Filter.Type parent, String i)
    {
        this.parent_index = parent;
        this.my_id = i;
    }
}
