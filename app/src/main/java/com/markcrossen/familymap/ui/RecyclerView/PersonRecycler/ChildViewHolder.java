package com.markcrossen.familymap.ui.RecyclerView.PersonRecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.markcrossen.familymap.R;

public class ChildViewHolder extends com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder {

    public ImageView personChildImage;
    public TextView personChildTextUpper;
    public TextView personChildTextLower;
    private FamilyMapAdapter adapter;
    private String my_id;
    private int parent_index;

    public ChildViewHolder(View itemView, FamilyMapAdapter adapter)
    {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onElementClicked();
            }
        });

        personChildImage = (ImageView) itemView.findViewById(R.id.childImage);
        personChildTextUpper = (TextView) itemView.findViewById(R.id.childTextUpper);
        personChildTextLower = (TextView) itemView.findViewById(R.id.childTextLower);
        this.adapter = adapter;
    }


    private void onElementClicked()
    {
        adapter.elementClicked(parent_index, my_id);
    }

    public void setCallBack(int parent, String i)
    {
        this.parent_index = parent;
        this.my_id = i;
    }
}
