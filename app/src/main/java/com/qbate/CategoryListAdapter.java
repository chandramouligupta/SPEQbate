package com.qbate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

public class CategoryListAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryItem> categoryItemsList;

    public CategoryListAdapter(Context context, List<CategoryItem> categoryItemsList) {
        this.categoryItemsList = categoryItemsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(context,R.layout.category_item,null);
        Button categoryItem = v.findViewById(R.id.category_name);

        //setting data to the list

        categoryItem.setText(categoryItemsList.get(position).getCategoryName());
        final int i = position;
        categoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,TopicsDisplay.class);
                intent.putExtra("categoryId","" + categoryItemsList.get(i).getCategoryId());
                intent.putExtra("categoryName","" + categoryItemsList.get(i).getCategoryName());
                context.startActivity(intent);
            }
        });

        //saving product id to the tag

        v.setTag(categoryItemsList.get(position).getCategoryId());
        return v;
    }
}
