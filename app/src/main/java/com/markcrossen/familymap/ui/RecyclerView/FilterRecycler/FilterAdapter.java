package com.markcrossen.familymap.ui.RecyclerView.FilterRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markcrossen.familymap.R;
import com.markcrossen.familymap.model.Filter;

import java.util.List;

public class FilterAdapter  extends RecyclerView.Adapter<FilterViewHolder> {

    private LayoutInflater mInflater;
    private List<FilterElement> items;

    public FilterAdapter(Context context, List<FilterElement> objects)
    {
        mInflater = LayoutInflater.from(context);
        items = objects;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup viewGroup, int view_type) {
        View view = mInflater.inflate(R.layout.filter_element, viewGroup, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterViewHolder filterViewHolder, int i) {
        FilterElement filter = items.get(i);
        filterViewHolder.filterTextUpper.setText(filter.getTextUpper());
        filterViewHolder.filterTextLower.setText(filter.getTextLower());
        filterViewHolder.setCallBack(filter.getParent(), filter.getId());
        filterViewHolder.filterSwitch.setChecked(Filter.getInstance().isChecked(filter.getParent(), filter.getId()));
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}
