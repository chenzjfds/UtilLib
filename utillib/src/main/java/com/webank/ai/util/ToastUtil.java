package com.webank.ai.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.webank.ai.http.R;



/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/10/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ToastUtil {
    private static Toast toast;
    private static TextView title;

    /**
     * 69      * 将Toast封装在一个方法中，在其它地方使用时直接输入要弹出的内容即可
     * 70
     */
    public static void toastMessage(String messages, Context context) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        if (toast == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//调用Activity的getLayoutInflater()
            View view = inflater.inflate(R.layout.toast, null); //加載layout下的布局
            title = (TextView) view.findViewById(R.id.tv_title);
            toast = new Toast(context.getApplicationContext());
            toast.setGravity(Gravity.TOP, 0, height / 3 * 2);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
            toast.setDuration(Toast.LENGTH_SHORT);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
            toast.setView(view); //添加视图文件
        }
        title.setText(messages); //toast的标题
        toast.show();
        LogUtil.i("ToastUtil","UI  SHOW MSG="+messages);
    }

}
