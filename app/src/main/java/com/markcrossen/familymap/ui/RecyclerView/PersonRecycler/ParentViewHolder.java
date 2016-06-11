package com.markcrossen.familymap.ui.RecyclerView.PersonRecycler;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markcrossen.familymap.R;

public class ParentViewHolder extends com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder {

    public TextView personTitleText;
    public ImageButton parentDropDown;

    public ParentViewHolder(View item_view)
    {
        super(item_view);

        personTitleText = (TextView) itemView.findViewById(R.id.parent_list_item_title);
        parentDropDown = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
    }
}
