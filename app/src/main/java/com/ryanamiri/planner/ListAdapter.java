package com.ryanamiri.planner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.text.ParseException;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Task> {
    private int layout;
    private MainActivity mainActivity;
    public ListAdapter(MainActivity m, Context context, int resource, ArrayList<Task> objects){
        super(context, resource, objects);
        mainActivity = m;
        layout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.listName = convertView.findViewById(R.id.listName);
            viewHolder.listTime = convertView.findViewById(R.id.listTime);
            viewHolder.listButton = convertView.findViewById(R.id.listButton);
            viewHolder.listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    boolean result = mainActivity.getDB().delete(mainActivity.getTasks().get(position).getId());
                    if (result) {
                        try {
                            mainActivity.setTasks();
                        } catch (ParseException e) {
                            Log.d("TASKS", e.toString());
                        }
                        Log.d("DATABASE", "Task has been deleted successfully.");
                    }
                    else
                        Log.d("DATABASE", "Task failed to delete.");
                }
            });
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.listName.setText(getItem(position).getName());
        viewHolder.listTime.setText(getItem(position).getTime());
        return convertView;
    }
}
