package com.example.surveymayor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author Adonis
 */
public class GenericAdapter extends ArrayAdapter<ListViewItems> {

    private List<ListViewItems> objects;
    private Context context;
    private int resource;

    public GenericAdapter(Context context, int resource, List<ListViewItems> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        GenericAdapterHolder holder = null;
        int resourseUtil = resource;
        if (row == null) {
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            holder = new GenericAdapterHolder();
            ListViewItems elemento = objects.get(position);

            int textElement1 = 0;
            int textElement2 = 0;
            int textElement3 = 0;
            int textElement4 = 0;
            if (elemento != null && !elemento.getTitulo1().equals("")) {
                resourseUtil = R.layout.listview_1_element;
                textElement1 = R.id.text1element;
                if (elemento.getTitulo2() != null && !elemento.getTitulo2().equals("")) {
                    resourseUtil = R.layout.listview_2_element;
                    textElement1 = R.id.text2element1;
                    textElement2 = R.id.text2element2;
//                    holder.textViewTitulo = (TextView) row.findViewById(R.id.text2element1);
//                    holder.textView2 = (TextView) row.findViewById(R.id.text2element2);
                    if (elemento.getTitulo3() != null && !elemento.getTitulo3().equals("")) {
                        resourseUtil = R.layout.listview_3_element;
                        textElement1 = R.id.text3element1;
                        textElement2 = R.id.text3element2;
                        textElement3 = R.id.text3element3;
                        if (elemento.getTitulo4() != null && !elemento.getTitulo4().equals("")) {
                            resourseUtil = R.layout.listview_4_element;
                            textElement1 = R.id.text4element1;
                            textElement2 = R.id.text4element2;
                            textElement3 = R.id.text4element3;
                            textElement4 = R.id.text4element4;
                        }
//                        holder.textViewTitulo = (TextView) row.findViewById(R.id.text3element1);
//                        holder.textView2 = (TextView) row.findViewById(R.id.text3element2);
//                        holder.textView3 = (TextView) row.findViewById(R.id.text3element3);
                    }
                }
            }

            row = layoutInflater.inflate(resourseUtil, parent, false);
            if (textElement1 != 0) {
                holder.textViewTitulo = (TextView) row.findViewById(textElement1);
                holder.textViewTitulo.setText(elemento.getTitulo1());
            }
            if (textElement2 != 0) {
                holder.textView2 = (TextView) row.findViewById(textElement2);
                holder.textView2.setText(elemento.getTitulo2());
            }
            if (textElement3 != 0) {
                holder.textView3 = (TextView) row.findViewById(textElement3);
                holder.textView3.setText(elemento.getTitulo3());
            }
            if (textElement4 != 0) {
                holder.textView4 = (TextView) row.findViewById(textElement4);
                holder.textView4.setText(elemento.getTitulo4());
            }
        }
        return row;
    }

    static class GenericAdapterHolder {
        TextView textViewTitulo;
        TextView textView2;
        TextView textView3;
        TextView textView4;
    }
}
