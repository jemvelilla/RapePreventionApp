package com.build1.rapepreventionapp.Contacts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.build1.rapepreventionapp.Model.UserModel;
import com.build1.rapepreventionapp.R;

import java.util.List;

/**
 * Created by JEMYLA VELILLA on 05/02/2018.
 */

public class CustomAdapter extends BaseAdapter {

    List<UserModel> users;
    LayoutInflater inflater;

    public CustomAdapter(Context context, List<UserModel> users){
        this.users = users;
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_contact, viewGroup, false);

            holder = new ViewHolder();

            holder.tvName = (TextView) view.findViewById(R.id.tv_Name);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        UserModel model = users.get(i);
        holder.tvName.setText(model.getName() + " / " + model.getUserId());
        String user_id = users.get(i).userId;

        if (model.isSelected()){
            holder.ivCheckBox.setBackgroundResource(R.drawable.checkbox_checked);
            holder.ivCheckBox.getLayoutParams().width = 50;
        } else if (!model.isAppUser()){
            holder.ivCheckBox.setBackgroundResource(R.drawable.invite_button2);
            holder.ivCheckBox.getLayoutParams().width = 120;
        } else {
            holder.ivCheckBox.setBackgroundResource(R.drawable.checkbox_unchecked);
        }

        return view;
    }

    public void updateRecords(List<UserModel> users){
        this.users = users;

        notifyDataSetChanged();
    }

    class ViewHolder{

        TextView tvName;
        ImageView ivCheckBox;
    }
}
