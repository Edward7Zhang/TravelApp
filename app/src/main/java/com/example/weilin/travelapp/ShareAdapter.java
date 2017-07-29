package com.example.weilin.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 某人说我俗 on 2017/1/3.
 */

public class ShareAdapter extends BaseAdapter{
    private static String[] shareNames = new String[] {"微信", "QQ", "微博","朋友圈", "QQ空间", "短信"};
    private int[] shareIcons = new int[] {R.drawable.sns_weixin_icon, R.drawable.sns_qqfriends_icon,  R.drawable.sns_sina_icon,R.drawable.sns_weixin_timeline_icon,
            R.drawable.sns_qzone_icon,R.drawable.short_message_nor};
    private LayoutInflater inflater;
    public ShareAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return shareNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.share_item, null);
        }
        ImageView shareIcon = (ImageView) convertView.findViewById(R.id.share_icon);
        TextView shareTitle = (TextView) convertView.findViewById(R.id.share_title);
        shareIcon.setImageResource(shareIcons[i]);
        shareTitle.setText(shareNames[i]);

        return convertView;
    }
}
