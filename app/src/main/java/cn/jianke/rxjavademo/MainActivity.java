package cn.jianke.rxjavademo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @className:MainActivity
 * @classDescription: 测试Rxjava
 * @author: leibing
 * @createTime: 2016/09/13
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    // 图片显示
    private ImageView showIv;
    // 加载转圈
    private SwipeRefreshLayout mProgressSwip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // findView
        showIv = (ImageView) findViewById(R.id.iv_show);
        mProgressSwip = (SwipeRefreshLayout) findViewById(R.id.swip_progress);
        // onClick
        findViewById(R.id.btn_create_observable).setOnClickListener(this);
        findViewById(R.id.btn_adjust).setOnClickListener(this);
        findViewById(R.id.btn_from).setOnClickListener(this);
        findViewById(R.id.btn_repeat).setOnClickListener(this);
        findViewById(R.id.btn_range).setOnClickListener(this);
        findViewById(R.id.btn_interval).setOnClickListener(this);
        findViewById(R.id.btn_timer).setOnClickListener(this);
        // onFresh
        mProgressSwip.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_observable:
                // create observable
                createObservable();
                break;
            case R.id.btn_adjust:
                // just observable
                justShowImg();
                break;
            case R.id.btn_from:
                // from observable
                fromObservable();
                break;
            case R.id.btn_repeat:
                // repeat observable
                repeatObservable();
                break;
            case R.id.btn_range:
                // range observable
                rangeObservable();
                break;
            case R.id.btn_interval:
                // interval observable
                intervalObservable();
                break;
            case R.id.btn_timer:
                // timer observable
                timerObservable();
                break;
            default:
                break;
        }
    }

    /**
     * create Observable
     * @author leibing
     * @createTime 2016/09/13
     * @lastModify 2016/09/13
     * @param
     * @return
     */
    private void createObservable() {
        // 通过create创建observable对象，在call中调用subscriber的onnext方法
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 20; i++) {
                    subscriber.onNext("i = " + i);
                }
                subscriber.onCompleted();
            }
        });
        // 上面的代码我们已经构建了一个观察者，我们接下来新建一个订阅者
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("dddddddddddd create complete");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("ddddddddddd create error");
            }

            @Override
            public void onNext(String s) {
                System.out.println("dddddddddddd  create " + s);
            }
        };
        // 通过调用subscribe方法使观察者和订阅者产生关联，一旦订阅就观察者就开始发送消息
        observable.subscribe(subscriber);
    }

    /**
     * 定时器操作
     * @author leibing
     * @createTime 2016/09/18
     * @lastModify 2016/09/18
     * @param
     * @return
     */
    private void timerObservable() {
        // 指定一定时间后才发射
        Observable.timer(4, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println("ddddddddddddddddd timer along = " + aLong);
            }
        });
    }

    /**
     * 轮询操作
     * @author leibing
     * @createTime 2016/09/18
     * @lastModify 2016/09/18
     * @param
     * @return
     */
    private void intervalObservable() {
        // 指定轮询时间(第一个参数)为x,轮询时间单位（第二个参数）为n开始轮询处理事件.
        Observable.interval(2, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println("ddddddddddddddddd interval aLong = " + aLong);
            }
        });
    }

    /**
     * 范围操作
     * @author leibing
     * @createTime 2016/09/18
     * @lastModify 2016/09/18
     * @param
     * @return
     */
    private void rangeObservable() {
        // 从指定的数字x开始发射n个数字
        Observable.range(15, 5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("ddddddddddddddddd range integer = " + integer);
            }
        });
    }

    /**
     * 集合操作
     * @author leibing
     * @createTime 2016/09/18
     * @lastModify 2016/09/18
     * @param
     * @return
     */
    private void fromObservable() {
        List<String> fromList = new ArrayList<>();
        fromList.add("1");
        fromList.add("2");
        fromList.add("3");
        Observable.from(fromList).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("ddddddddddddddd from s = " + s);
            }
        });
    }

    /**
     * 重复操作
     * @author leibing
     * @createTime 2016/09/08
     * @lastModify 2016/09/08
     * @param
     * @return
     */
    private void repeatObservable(){
        List<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(1);
        integers.add(2);
        integers.add(3);
        Observable.from(integers).repeat(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("ddddddddddddddddddd repeat integer = " + integer);
            }
        });
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     * @author leibing
     * @createTime 2016/09/17
     * @lastModify 2016/09/17
     * @param url
     * @return
     */
    private Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 用map变换字符串为bitmap
     * @author leibing
     * @createTime 2016/09/17
     * @lastModify 2016/09/17
     * @param
     * @return
     */
    private void justShowImg() {
        mProgressSwip.setRefreshing(true);
        // 图片地址
        String url = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&" +
                "quality=100&size=b4000_4000&sec=1474106737&di=8432956a8f4868587" +
                "5f64ca01246f81f&src=http://g.hiphotos.baidu.com/imgad/pic/item/" +
                "f603918fa0ec08fa9f0b7dd85eee3d6d55fbda42.jpg";
        // map变换将url转换成bitmap
        Observable.just(url).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                // 通过url拿去bitmap
                return returnBitmap(s);
            }
        }).subscribeOn(Schedulers.io()). // 订阅在io线程
                observeOn(AndroidSchedulers.mainThread()) //发送在UI线程（处理更新UI）
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        // 发送后处理更新UI
                        mProgressSwip.setRefreshing(false);
                        showIv.setImageBitmap(bitmap);
                    }
                });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressSwip.setRefreshing(false);
            }
        }, 2000);
    }
}
