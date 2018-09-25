package xp.com.animation;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ValueAnimator valueAnimator ;

    private Button stretchAnimal ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        showStretchAnimation();//显示拉伸动画
    }

    //初始化视图
    private void initView(){
        stretchAnimal = findViewById(R.id.anim_stretch);
    }


    //拉伸动画
    private void showStretchAnimation(){
        valueAnimator = ValueAnimator.ofInt(stretchAnimal.getLayoutParams().width,500,200,350);
        //设置动画播放的各种属性
        valueAnimator.setDuration(5000);//持续时长

        valueAnimator.setStartDelay(500);//延时播放

        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);//设置动画重复的时候翻转方向

        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//设置重复播放次数

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                Log.i(TAG,String.format("当前参数为：%s",currentValue));
                stretchAnimal.getLayoutParams().width = currentValue ;

                //更新视图信息
                stretchAnimal.requestLayout();
            }
        });
        valueAnimator.start();
    }
}
