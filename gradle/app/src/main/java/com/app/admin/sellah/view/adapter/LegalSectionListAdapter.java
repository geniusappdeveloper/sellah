package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.admin.sellah.R;

import java.util.ArrayList;

public class LegalSectionListAdapter extends BaseAdapter {

    ArrayList<String> optionList;
    LayoutInflater inflater;
    Context context;

    public LegalSectionListAdapter(Context context, ArrayList<String> optionList){
        this.optionList=optionList;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return optionList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LegalSectionViewHolder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.legal_section_adapter_view, null);
            holder = new LegalSectionViewHolder();
            holder.txtOptios = (TextView)convertView.findViewById(R.id.txt_body);
            convertView.setTag(holder);
        }
        else
        {
            holder = (LegalSectionViewHolder) convertView.getTag();
        }

        holder.txtOptios.setText(optionList.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                   /* File myFile = new File( "file:///android_asset/termsandconditions.docx");
//                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",myFile);
//                    File newFile=new File(uri.getPath());
                    try {
                        Global.openFile(context, myFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                }
            }
        });

        return convertView;
    }

    public class LegalSectionViewHolder{
        TextView txtOptios;
    }

}
