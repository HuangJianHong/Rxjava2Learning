package test.huangjh.www.rxjava2learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import test.huangjh.www.rxjava2learning.api.Api;
import test.huangjh.www.rxjava2learning.api.RetrofitProvider;

public class ObjectChangeActivity extends AppCompatActivity {


    @BindView(R.id.tvShow)
    TextView tvShow;
    @BindView(R.id.tvFlatMap)
    TextView tvFlatMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obejcet_change);
        ButterKnife.bind(this);

        Observable.create(new ObservableOnSubscribe<Integer>() {               //map变换操作
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "a" + integer;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
                tvShow.append(s);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();

            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {       //flatMap操作，顺序可能无序; conCatMap,顺序按照发送的来。
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("This value is = " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                tvFlatMap.append(s);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        //登录和 注册的链式调用  举例
        final Api api = RetrofitProvider.get().create(Api.class);
        /*api.register(new RegisterRequest())                      //发起注册请求
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<RegisterResponse>() {     //在subscribe之前
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        //先根据注册的响应结果去做一些操作
                    }
                })
                .observeOn(Schedulers.io())              //回到IO线程去发起登录请求
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                        //registerResponse, 注册的响应结果
                        return api.login(new LoginRequest());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())   //回到主线程去处理请求登录的结果
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        //登录成功
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //登录失败
                    }
                });
*/

    }
}
