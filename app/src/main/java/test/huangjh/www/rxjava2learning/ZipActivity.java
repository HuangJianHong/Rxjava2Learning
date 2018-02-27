package test.huangjh.www.rxjava2learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.InterruptedIOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class ZipActivity extends AppCompatActivity {

    public static final String TAG = ZipActivity.class.getSimpleName();

    static {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (throwable instanceof InterruptedIOException) {
                    Log.d(TAG, "Io interrupted");
                }
            }
        });
    }

    @BindView(R.id.tvZip)
    TextView tvZip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
        ButterKnife.bind(this);


        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Thread.sleep(1000);

                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Thread.sleep(1000);

                Log.d(TAG, "emit 4");
                emitter.onNext(4);
                Thread.sleep(1000);

                Log.d(TAG, "emit complete1");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());


        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "emit A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.d(TAG, "emit B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.d(TAG, "emit C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.d(TAG, "emit complete2");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());


        //组合, 有概率出现InterruptedIOException异常，所以需要RxJavaPlugins上面的静态代码
        Observable.zip(observable1, stringObservable, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "disposable");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: " + s);
                        tvZip.append(s);
                        tvZip.append("\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError()");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete()");
                    }
                });


    }
}
