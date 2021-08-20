package com.uof.uof_mobile.listview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uof.uof_mobile.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LobbyListViewItemAdapter extends BaseAdapter {
    ArrayList<LobbyListViewItem> items = new ArrayList<LobbyListViewItem>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.rows_noworderlist, viewGroup, false);
        }
        //화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView rowslobbyordernumber = view.findViewById(R.id.rows_lobby_ordernumber);
        TextView rowslobbyorderlist = view.findViewById(R.id.rows_lobby_orderlist);

        //데이터 세팅하기
        LobbyListViewItem lobbylistviewitem = items.get(i);
        StringBuilder sumlist = new StringBuilder();
        try {
            final JSONArray list = lobbylistviewitem.getMenulist();
            for (int j = 0; j < list.length(); j++) {
                JSONObject jsonObject = list.getJSONObject(j);
                if (j == (list.length() - 1)) {
                    sumlist.append(jsonObject.getString("name") + " " + jsonObject.getInt("count") + "개");
                } else {
                    sumlist.append(jsonObject.getString("name") + " " + jsonObject.getInt("count") + "개, ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("Sss", sumlist.toString());
        rowslobbyordernumber.setText(String.valueOf(lobbylistviewitem.getOrdernum()));
        rowslobbyorderlist.setText(sumlist.toString());
        return view;
    }

    public void addItem(LobbyListViewItem item) {
        items.add(item);
    }
}
