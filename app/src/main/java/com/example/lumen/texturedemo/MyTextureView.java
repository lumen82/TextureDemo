package com.example.lumen.texturedemo;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by lumen on 2017/9/21.
 */

public class MyTextureView extends TextureView {
    private String TAG = MyTextureView.class.getSimpleName();

    public static final int SCALE_TYPE_X = 1; /* 水平缩放 直至边界重合 */
    public static final int SCALE_TYPE_Y = 2; /* 竖直缩放 直至边界重合 */
    public static final int SCALE_TYPE_XY = 3;    /* 水平竖直缩放直至边界重合 */
    public static final int SCALE_TYPE_FIT_MAX = 4;   /* 最大边与边界重合 */
    public static final int SCALE_TYPE_FIT_MIN = 5;   /* 最小边与边界重合 */

    private Matrix matrix;
    private int fixedWidth;
    private int fixedHeight;

    public MyTextureView(Context context) {
        super(context);
    }

    public MyTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MyTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void setFixedSize(int width, int height) {
        fixedHeight = height;
        fixedWidth = width;
        Log.d(TAG, "setFixedSize,width=" + width + "height=" + height);
        requestLayout();
    }

    public int getResizedWidth() {
        if (fixedWidth == 0) {
            return getWidth();
        } else {
            return fixedWidth;
        }
    }

    public int getResizedHeight() {
        if (fixedHeight== 0) {
            return getHeight();
        } else {
            return fixedHeight;
        }
    }

    //需求:视频等比例放大,直至一边铺满View的某一边,另一边超出View的另一边,再移动到View的正中央,这样长边两边会被裁剪掉同样大小的区域,视频看起来不会变形
    //也即是:先把视频区(实际的大小显示区)与View(定义的大小)区的两个中心点重合, 然后等比例放大或缩小视频区,直至一条边与View的一条边相等,另一条边超过
    //View的另一条边,这时再裁剪掉超出的边, 使视频区与View区大小一样. 这样在不同尺寸的手机上,视频看起来不会变形,只是水平或竖直方向的两端被裁剪了一些.
    public void transformVideo(int scaleType, int videoWidth, int videoHeight) {
        if (getResizedHeight() == 0 || getResizedWidth() == 0) {
            Log.d(TAG, "transformVideo, getResizedHeight=" + getResizedHeight() + "," + "getResizedWidth=" + getResizedWidth());
            return;
        }
        float sx = (float) getResizedWidth() / (float) videoWidth;
        float sy = (float) getResizedHeight() / (float) videoHeight;
        Log.d(TAG, "transformVideo, sx=" + sx);
        Log.d(TAG, "transformVideo, sy=" + sy);

        float maxScale = Math.max(sx, sy);
        float minScale = Math.min(sx, sy);
        if (this.matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }

        //第2步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((getResizedWidth() - videoWidth) / 2, (getResizedHeight() - videoHeight) / 2);

        //第1步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(videoWidth / (float) getResizedWidth(), videoHeight / (float) getResizedHeight());

        //第3步,等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等. 因为超过的部分超出了View的范围,所以是不会显示的,相当于裁剪了.
//        matrix.postScale(maxScale, maxScale, getResizedWidth() / 2, getResizedHeight() / 2);//后两个参数坐标是以整个View的坐标系以参考的

        switch (scaleType){
            case SCALE_TYPE_X:
                matrix.postScale(sx, sx, getResizedWidth() / 2, getResizedHeight() / 2);
                break;
            case SCALE_TYPE_Y:
                matrix.postScale(sy, sy, getResizedWidth() / 2, getResizedHeight() / 2);
                break;
            case SCALE_TYPE_XY:
                matrix.postScale(sx, sy, getResizedWidth() / 2, getResizedHeight() / 2);
                break;
            case SCALE_TYPE_FIT_MIN:
                matrix.postScale(maxScale, maxScale, getResizedWidth() / 2, getResizedHeight() / 2);
                break;
            case SCALE_TYPE_FIT_MAX:
                matrix.postScale(minScale, minScale, getResizedWidth() / 2, getResizedHeight() / 2);
                break;
            default:
                matrix.postScale(sx, sy, getResizedWidth() / 2, getResizedHeight() / 2);
        }

        Log.d(TAG, "transformVideo, maxScale=" + maxScale);
        Log.d(TAG, "transformVideo, minScale=" + minScale);

        setTransform(matrix);
        postInvalidate();
        Log.d(TAG, "transformVideo, videoWidth=" + videoWidth + "," + "videoHeight=" + videoHeight);
    }

}
