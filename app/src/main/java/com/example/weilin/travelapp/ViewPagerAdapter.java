package com.example.weilin.travelapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import org.xutils.x;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by 未来程序猿？ on 2017/1/4.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private static final int NUM_IMG = 20;
    private List<String> mUrlList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Queue<View> mReusableViews;
    private View v;

    public ViewPagerAdapter(Context context, List<String> urlList)
    {
        mInflater = LayoutInflater.from(context);
        mReusableViews = new ArrayDeque<>(urlList.size());
        mUrlList = urlList;
        mUrlList.add(0,mUrlList.get(mUrlList.size()-1));
        mUrlList.add(mUrlList.get(1));
    }

    /**
     * 销毁每个viewpager
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View)
        {
            container.removeView((View)object);
            mReusableViews.add((View)object);
        }
    }

    /**
     * 初始化每个viewpager
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        v = mReusableViews.poll();
        if(v == null)
        {
            v = mInflater.inflate(R.layout.viewpager_item, container, false);
        }
        ImageView imageView = (ImageView) v.findViewById(R.id.image_iv);
        int position2 = position % mUrlList.size();
        if (0 <= position2 && position2 < mUrlList.size())
        {
            String Url = mUrlList.get(position2);
            if (TextUtils.isEmpty(Url))
            {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
            else
            {
                x.image().bind(imageView, Url);
            }
        }
        container.addView(v);
        return v;
    }

    /**
     * 返回viewpager个数
     * @return
     */
    @Override
    public int getCount()
    {
        return mUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

}
