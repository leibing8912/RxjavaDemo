package cn.jianke.rxjavademo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @className: AppContext
 * @classDescription: 应用类
 * @author: leibing
 * @createTime: 2016/09/18
 */
public class AppContext extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
