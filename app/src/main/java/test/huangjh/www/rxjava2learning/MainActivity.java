package test.huangjh.www.rxjava2learning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.textView2)
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6, R.id.textView7,R.id.textView8})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.textView2:
                startActivity(new Intent(this, BaseUseActivity.class));
                break;
            case R.id.textView3:
                startActivity(new Intent(this, ThreadChangeActivity.class));
                break;
            case R.id.textView4:
                startActivity(new Intent(this, ObjectChangeActivity.class));
                break;
            case R.id.textView5:
                startActivity(new Intent(this, ZipActivity.class));
                Toast.makeText(this, "OpenActivity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView6:
                startActivity(new Intent(this, FlowableActivity.class));
                break;
            case R.id.textView7:
                startActivity(new Intent(this, BackPressureActivity.class));
                break;
            case R.id.textView8:
                startActivity(new Intent(this, FlowableEmitterActivity.class));
                break;
        }

    }

}
