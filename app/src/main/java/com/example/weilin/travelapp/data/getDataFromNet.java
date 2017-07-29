package com.example.weilin.travelapp.data;

import android.content.Context;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hulinfa on 2017/1/4.
 */

public class getDataFromNet {
    final static String TAG = "getDataFromNet";


    /**
     * 从网络中获得图片的列表
     *
     * @param context
     * @param pictures 图片地址的链表
     * @return List<Bitmap>
     */

    public static List<String> getPictureList(Context context,List<String> pictures) {
        ArrayList<String> picList = new ArrayList<String>();
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tu01);
//        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tu02);
//        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tu03);
//        Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tu04);
//        picList.add(bitmap);
//        picList.add(bitmap2);
//        picList.add(bitmap3);
//        picList.add(bitmap1);
        for (int i = 0; i < pictures.size(); i++) {
            String filepath = context.getFilesDir() + "/img/tu" + i;
            loadImgList(pictures.get(i), filepath);
            picList.add(filepath);
        }

        return picList;
    }

    /**
     * 下载文件
     */
    public static void loadImgList(String url, String filepath) {
        String imgUri = "";
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        params.setAutoRename(false);
        x.http().get(params, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File file) {
                Log.v(TAG, "下载成功" + file);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.v(TAG, "下载完成......");
            }
        });
    }

}
