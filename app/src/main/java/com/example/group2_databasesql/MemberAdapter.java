package com.example.group2_databasesql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MemberAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Member> memberList;

    public MemberAdapter(Context context, int layout, List<Member> memberList) {
        this.context = context;
        this.layout = layout;
        this.memberList = memberList;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);

        TextView txtEmail = convertView.findViewById(R.id.email);
        TextView txtCompanyName = convertView.findViewById(R.id.name); // company name

        Member member = memberList.get(position);

        txtEmail.setText("User: " + member.getEmail());
        txtCompanyName.setText("Company Name: " + member.getCompanyName());
       // txtCity.setText("City: " + member.getCity());

        return convertView;
    }
}
