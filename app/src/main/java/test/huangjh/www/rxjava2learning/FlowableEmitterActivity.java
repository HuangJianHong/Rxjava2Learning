package test.huangjh.www.rxjava2learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import test.huangjh.www.rxjava2learning.api.Api;
import test.huangjh.www.rxjava2learning.api.RetrofitProvider;

public class FlowableEmitterActivity extends AppCompatActivity {

    public static final String TAG = FlowableEmitterActivity.class.getSimpleName();
    private Subscription mSubscription;

    @BindView(R.id.tvEmitter)
    TextView tvEmitter;
    @BindView(R.id.tv250)
    TextView tv250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable_emitter);
        ButterKnife.bind(this);

//        demo1();
//        demo2();
//        demo4();

        tvEmitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubscription != null) {
                    mSubscription.request(96);
                }
            }
        });

        RetrofitDemo();
    }

    //同步线程的 上游request处理
    public void demo1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "before emit, requested = " + emitter.requested());

                Log.i(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "after emit 1, requested = " + emitter.requested());

                Log.i(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "after emit 2, requested = " + emitter.requested());

                Log.i(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "after emit 3, requested = " + emitter.requested());

                Log.i(TAG, "emit complete");
                emitter.onComplete();

                Log.d(TAG, "after emit complete, requested = " + emitter.requested());
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                        s.request(2);   //request 2
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


    //异步线程下的上游 request会自动触发128
    public void demo2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "current requested: " + emitter.requested());

            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe()");
                        mSubscription = s;
                        s.request(1000);
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    //下游每消费96个时间后， 上游就会继续发送。
    public void demo4() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Log.d(TAG, "First requested = " + emitter.requested());
                        boolean flag;
                        for (int i = 0; ; i++) {
                            flag = false;
                            while (emitter.requested() == 0) {           //requested == 0的时候，就一直在这里循环，不在发送emitter
                                if (!flag) {
                                    Log.d(TAG, "Oh no! I can't emit value!");
                                    flag = true;
                                }
                            }
                            emitter.onNext(i);
                            Log.d(TAG, "emit " + i + " , requested = " + emitter.requested());
                        }
                    }
                }, BackpressureStrategy.ERROR)
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
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }


    //豆瓣电影Top250
    public  void  RetrofitDemo(){
        Api api = RetrofitProvider.get().create(Api.class);
        api.getTop250(0, 12)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        Log.d(TAG, response.body().toString());
                        tv250.setText(response.body().toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.getMessage());
                    }
                });
    }


}
