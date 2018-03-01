package test.huangjh.www.rxjava2learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BackPressureActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = BackPressureActivity.class.getSimpleName();
    public Subscription mSubscription;

    @BindView(R.id.tvRequest)
    TextView tvRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_presure);
        ButterKnife.bind(this);

        /*主线程过于频繁操作会无法打印日志，想要输出日志记得切换到其他的线程*/ //TODO
//        demo1();
//        demo2();
//        demo3Latest();
          demo4();



        tvRequest.setOnClickListener(this);

    }

    public void demo1Buffer() {
        //增大上游水缸的缓存容量， buffer
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    Log.d(TAG, "emit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }


    public void demo2Drop() {
        //Drop丢弃掉 多余的事件
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10000; i++) {
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())                   //不要在主线程打印这边的日志，主线程太频繁操作，无法打印出来
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                        s.request(128);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }


    public void demo3Latest() {
        //latest模式，保留最后的事件
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10000; i++) {
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                        s.request(128);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    public void demo4() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Long along) {
                        Log.d(TAG, "onNext: " + along);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRequest:
                if (mSubscription != null) {
                    mSubscription.request(128);
                }
                break;
        }
    }
}
