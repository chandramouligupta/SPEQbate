package com.qbate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TopicListAdapter extends BaseAdapter {

    ArrayList<TopicItem> topicItemList;
    private Context context;

    public TopicListAdapter(Context context, ArrayList<TopicItem> topicItemList) {
        this.context = context;
        this.topicItemList = topicItemList;
    }

    @Override
    public int getCount() {
        return topicItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return topicItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View v = View.inflate(context,R.layout.activity_topic_item,null);
        TextView topicTitle = v.findViewById(R.id.topic_heading);

        //setting data to the list

        topicTitle.setText(topicItemList.get(position).getTopicTitle());

        //saving product id to the tag

        v.setTag(topicItemList.get(position).getTopicId());
        return v;
    }
}
