package test.huangjh.www.rxjava2learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseUseActivity extends AppCompatActivity {

    public static final String TAG = BaseUseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_use);


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onNext("3");
                emitter.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe:  ");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext： " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete()");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                Log.d(TAG,"onNext= 1" );
                emitter.onNext("2");
                Log.d(TAG,"onNext= 2" );
                emitter.onNext("3");
                Log.d(TAG,"onNext= 3" );
                emitter.onComplete();
                Log.d(TAG,"onComplete_emitter" );
                emitter.onNext("4");
                Log.d(TAG,"onNext= 4" );
            }
        }).subscribe(new Observer<String>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe:  ");
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext： " + s);
                if (s.equals("2")){
                    disposable.dispose();                             //截断水流
                    Log.d(TAG,"disposable: = " + disposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        });

    }
}



















