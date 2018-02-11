package test.huangjh.www.rxjava2learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ThreadChangeActivity extends AppCompatActivity {
    public static final String TAG = ThreadChangeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_change);


        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emitter:" + Thread.currentThread().getName());
                Log.d(TAG, "onNext=1");
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept:" + Thread.currentThread().getName());
                Log.d(TAG, "accept" + integer);
            }
        };

//        observable.subscribeOn(Schedulers.newThread())           //发送线程
//                .observeOn(AndroidSchedulers.mainThread())       //接收线程
//                .subscribe(consumer);

        observable.subscribeOn(Schedulers.newThread())           //发送线程看第一次
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())       //接收线程看最后一下
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "After observeOn(mainThread), current thread is: " + Thread.currentThread().getName( ) + "  integer： " + integer);
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "After observeOn(io), current thread is : " + Thread.currentThread().getName() +"  integer： " + integer);
                    }
                })
                .subscribe(consumer);

    }





}
