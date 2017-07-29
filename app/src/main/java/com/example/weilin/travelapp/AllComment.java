package com.example.weilin.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weilin.travelapp.bean.Sense;
import com.example.weilin.travelapp.tools.RefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class AllComment extends Activity {

    private List<Sense.Comment> commentList;
    private RefreshListView mList;
    private Sense sense;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
                mList.onRefreshComplete();
                commentList = pullJason((String) msg.obj);
                mAdapter.notifyDataSetChanged();
            }else if(msg.arg1 == 2){

            }

        }
    };
    private MyBaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        sense = (Sense) extras.getSerializable(MainActivity.SENSE_KEY);
        System.out.println("在AllComment中" + sense);
        commentList = sense.getSenseComments();
        System.out.println("commentList:" + commentList);
        mList = (RefreshListView) findViewById(R.id.all_comment_lv);
        mAdapter = new MyBaseAdapter();
        mList.setAdapter(mAdapter);
        mList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                getJsonFromNet();
            }

            @Override
            public void onLoadingMore() {
                Toast.makeText(AllComment.this,"没有更多的数据",Toast.LENGTH_SHORT).show();
                mList.loadMoreComplete();

            }
        });

    }

    private class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int i) {
            return commentList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = null;
            ViewHolder holder = null;
            if (convertView == null) {

                holder = new ViewHolder();
                view = View.inflate(AllComment.this, R.layout.comment_list_layout, null);
                holder.personName = (TextView) view.findViewById(R.id.personName_tv);
                holder.commentContent = (TextView) view.findViewById(R.id.comment_tv);
                view.setTag(holder);

            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();

            }
            Sense.Comment comment = commentList.get(position);
            if (comment != null) {
                holder.personName.setText(comment.getCommentName());
                holder.commentContent.setText(comment.getCommentList().get(0));
            }
            return view;
        }
    }

    private class ViewHolder {
        public TextView personName;
        public TextView commentContent;
    }

    /**
     * 从网络中下载json数据
     */
    private void getJsonFromNet() {
        RequestParams params = new RequestParams("http://115.159.150.136:8080/travel/Fservlet");
        params.setHeader("Content-Type", "text/html;charset=utf-8");
        params.addParameter("SenseCode", sense.getSenseCode());
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                message.arg1 = 1;
                message.obj = result;
                System.out.println(result);
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("下载失败......");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private List<Sense.Comment> pullJason(String jsondata) {
        Gson gson = new Gson();
        List<Sense.Comment> comment_list = gson.fromJson(jsondata, new TypeToken<List<Sense.Comment>>() {
        }.getType());
        System.out.println(comment_list);
        return comment_list;

    }
}

