package com.example.macbookpro.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;

import com.example.macbookpro.myapplication.Beans.Reclamation;
import com.example.macbookpro.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ReclamationAdapter extends ArrayAdapter<Reclamation> implements View.OnClickListener {

    private ArrayList<Reclamation> mData;
    private Context mCtx;

    public ReclamationAdapter(Context context, ArrayList<Reclamation> objects) {
        super(context, R.layout.reclamation_adapter, objects);
        mData = objects;
        mCtx = context;
    }

    @Override
    public void onClick(View view) {

    }
    private static class ViewHolder {
        TextView txtCategorie;
        TextView txtType;
        TextView txtRue;
        TextView txtMessage;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reclamation reclamation = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reclamation_adapter, parent, false);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type_lv);
            viewHolder.txtCategorie = (TextView) convertView.findViewById(R.id.categorie_lv);
            viewHolder.txtRue = (TextView) convertView.findViewById(R.id.rue_lv);
            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.message_lv);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtRue.setText(reclamation.getRue());
        viewHolder.txtType.setText(reclamation.getType());
        viewHolder.txtCategorie.setText(reclamation.getCategorie());
        viewHolder.txtMessage.setText(reclamation.getMessage());


        return convertView;
    }
}
