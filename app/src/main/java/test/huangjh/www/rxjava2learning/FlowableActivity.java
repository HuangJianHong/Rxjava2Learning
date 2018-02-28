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
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FlowableActivity extends AppCompatActivity {
    public static final String TAG = FlowableActivity.class.getSimpleName();

    @BindView(R.id.tvRequest)
    TextView tvRequest;
    @BindView(R.id.tvValue)
    TextView tvValue;

    private Subscription mSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable);
        ButterKnife.bind(this);


        Flowable flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                mSubscription = s;
                Log.d(TAG, "onSubscribe");
                //下游没有处理能力的时候，同步线程FC，异步线程的时候上游发送进Flowable的缓存大小为128的水箱
//                mSubscription.request(1);
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "onNext:  value= " + integer);
                tvValue.append(integer + "\n");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);         //报错日志在这里输出
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }

    @OnClick(R.id.tvRequest)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvRequest:
                mSubscription.request(1);
                break;

            default:
                break;
        }
    }

    //下游处理能力控制
    public void start() {
        if (mSubscription != null) {
            mSubscription.request(1);
        }
    }

}
