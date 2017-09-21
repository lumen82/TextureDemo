package com.example.lumen.texturedemo;

import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by lumen on 2017/9/21.
 */

public class MyTextureView extends TextureView {
    private String TAG = MyTextureView.class.getSimpleName();
    private Matrix matrix;
    public MyTextureView(Context context) {
        super(context);
    }

    //需求:视频等比例放大,直至一边铺满View的某一边,另一边超出View的另一边,再移动到View的正中央,这样长边两边会被裁剪掉同样大小的区域,视频看起来不会变形
    //也即是:先把视频区(实际的大小显示区)与View(定义的大小)区的两个中心点重合, 然后等比例放大或缩小视频区,直至一条边与View的一条边相等,另一条边超过
    //View的另一条边,这时再裁剪掉超出的边, 使视频区与View区大小一样. 这样在不同尺寸的手机上,视频看起来不会变形,只是水平或竖直方向的两端被裁剪了一些.
    private void transformVideo(int videoWidth, int videoHeight) {
        if (getHeight() == 0 || getWidth() == 0) {
            Log.d(TAG, "transformVideo, getHeight=" + getHeight() + "," + "getWidth=" + getWidth());
            return;
        }
        float sx = (float) getWidth() / (float) videoWidth;
        float sy = (float) getHeight() / (float) videoHeight;
        Log.d(TAG, "transformVideo, sx=" + sx);
        Log.d(TAG, "transformVideo, sy=" + sy);

        float maxScale = Math.max(sx, sy);
        if (this.matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }

        //第2步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((getWidth() - videoWidth) / 2, (getHeight() - videoHeight) / 2);

        //第1步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(videoWidth / (float) getWidth(), videoHeight / (float) getHeight());

        //第3步,等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等. 因为超过的部分超出了View的范围,所以是不会显示的,相当于裁剪了.
        matrix.postScale(maxScale, maxScale, getWidth() / 2, getResizedHeight() / 2);//后两个参数坐标是以整个View的坐标系以参考的

        Log.d(TAG, "transformVideo, maxScale=" + maxScale);

        setTransform(matrix);
        postInvalidate();
        Log.d(TAG, "transformVideo, videoWidth=" + videoWidth + "," + "videoHeight=" + videoHeight);
    }
}
