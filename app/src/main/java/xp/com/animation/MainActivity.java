package xp.com.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ValueAnimator stretchValueAnimator ;//拉伸效果动画

    private ObjectAnimator translateObjectAnimal ;//平移动画

    private AnimatorSet animatorSet ;//组合动画，通过简单的平移实现

    private AnimatorSet cycleAnimatorSet ;//圆运动轨迹动画，通过插值器实现

    private AnimatorSet roundAnimatorSet ;//围绕图片轨迹运动动画

    private Button stretchAnimal ;//拉伸的对象

    private ImageView translateAnimal ;//平移的对象

    private ImageView animationSetAnim ;//组合动画的对象

    private ImageView cycleTranslateAnimal ;//圆轨迹运动的对象

    private ImageView viewPropertyAnim ;//对对象操作实现动画

    private ImageView imgBg ;//被围绕的背景图

    private ImageView roundBgAnim ; //围绕动画

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        showStretchAnimation();//显示拉伸动画
        showTranslateXAnim();
        showAnimatorSet();
        showCycleTranslateAnim();
        viewPropertyAnim.animate().alpha(0.5f).setDuration(3000).x(600).y(500);
    }

    //初始化视图
    private void initView(){
        stretchAnimal = findViewById(R.id.anim_stretch);
        translateAnimal = findViewById(R.id.anim_x_translate);
        animationSetAnim = findViewById(R.id.anim_animationSet);
        cycleTranslateAnimal = findViewById(R.id.anim_cycle_translate);
        viewPropertyAnim = findViewById(R.id.anim_view_property_anim);
        imgBg = findViewById(R.id.img_bg);
        //在背景图加载完成后启动背景动画
        imgBg.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
               showRoundImgAnim();
               imgBg.removeOnLayoutChangeListener(this);
            }
        });
        imgBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roundAnimatorSet != null){
                    //动画开启
                    if(roundAnimatorSet.isRunning()) {
                        //动画暂停
                        if (!roundAnimatorSet.isPaused()) {
                            roundAnimatorSet.pause();
                        } else {
                            roundAnimatorSet.resume();
                        }
                    }else{
                        roundAnimatorSet.start();
                    }
                }
            }
        });
        roundBgAnim = findViewById(R.id.anim_round_bg);
        roundBgAnim.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                showRoundImgAnim();
                roundBgAnim.removeOnLayoutChangeListener(this);
            }
        });
    }


    //拉伸动画
    private void showStretchAnimation(){
        stretchValueAnimator = ValueAnimator.ofInt(stretchAnimal.getLayoutParams().width,500,1080,350,1080,350);
        //设置动画播放的各种属性
        stretchValueAnimator.setDuration(5000);//持续时长

        stretchValueAnimator.setStartDelay(500);//延时播放

        stretchValueAnimator.setRepeatMode(ValueAnimator.REVERSE);//设置动画重复的时候翻转方向

        stretchValueAnimator.setRepeatCount(ValueAnimator.INFINITE);//设置重复播放次数

        stretchValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                Log.i(TAG + 1,String.format("当前参数为：%s",currentValue));
                stretchAnimal.getLayoutParams().width = currentValue ;

                //更新视图信息
                stretchAnimal.requestLayout();
            }
        });
        stretchValueAnimator.start();
    }

    //平移X
    private void showTranslateXAnim(){

        float x = translateAnimal.getTranslationX() ;

        /**
         * 预设的propertyName参数有：
         * Alpha    控制View的透明度
         * TranslationX     控制X方向的位移
         * TranslationY 	控制Y方向的位移
         * ScaleX	控制X方向的缩放倍数
         * ScaleY   控制Y方向的缩放倍数
         * Rotation 控制以屏幕方向为轴的旋转度数
         * RotationX    控制以X轴为轴的旋转度数
         * RotationY    控制以Y轴为轴的旋转度数
         */
        translateObjectAnimal = ObjectAnimator.ofFloat(translateAnimal,"translationX",x,x + 100,x+150,x+60);
        translateObjectAnimal.setDuration(3000);
        translateObjectAnimal.setRepeatCount(ObjectAnimator.INFINITE);
        translateObjectAnimal.setRepeatMode(ObjectAnimator.REVERSE);
        translateObjectAnimal.start();
    }

    //显示组合动画
    private void showAnimatorSet(){

        float x = animationSetAnim.getTranslationX();
        float y = animationSetAnim.getTranslationY();

        ObjectAnimator translationXStartAnim = ObjectAnimator.ofFloat(animationSetAnim,"translationX",x,x+300);
        ObjectAnimator translationYStartAnim = ObjectAnimator.ofFloat(animationSetAnim,"translationY",y,y+200);
        ObjectAnimator translationXEndAnim = ObjectAnimator.ofFloat(animationSetAnim,"translationX",x+300,x);
        ObjectAnimator translationYEndAnim = ObjectAnimator.ofFloat(animationSetAnim,"translationY",y+200,y);


        animatorSet = new AnimatorSet();

        //语文逻辑理解,顺序播放translationYStartAnim，translationXStartAnim，translationYEndAnim，translationXEndAnim
        animatorSet.play(translationYStartAnim).before(translationXStartAnim);
        animatorSet.play(translationXStartAnim).before(translationYEndAnim);
        animatorSet.play(translationYEndAnim).before(translationXEndAnim);

        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                //TODO 插值器，可以在这里控制动画的显示播放进度
                return input;
            }
        });
        animatorSet.start();
    }

    //按照圆来进行路径移动
    private void showCycleTranslateAnim(){
        float x = cycleTranslateAnimal.getTranslationX();
        float y = cycleTranslateAnimal.getTranslationY();
        cycleAnimatorSet = new AnimatorSet();

        //从左边逆时针移动到右端，完成下半个圆的运动轨迹
        ObjectAnimator translateLeftX = ObjectAnimator.ofFloat(cycleTranslateAnimal,"translationX",x,x+300);
        translateLeftX.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                float x = fraction * ((float)endValue - (float)startValue) + (float)startValue;
                Log.i(TAG + 5,"当前位置1：x =" + x);
                return x;
            }
        });
        ObjectAnimator translateLeftY = ObjectAnimator.ofFloat(cycleTranslateAnimal,"translationY",y,y+300);
        translateLeftY.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                //计算圆的半径
                float r = Math.abs((float) startValue - (float) endValue) / 2;
                /**
                 * 在匀速增长的情况下计算y的变化。依据如下方程推出：
                 * (x-a)(x-a) + (y-b)(y-b)=r*r
                 * 其中点(x1,0为圆心建立直角坐标系)，在这里，设定x随时间匀速增长，即 x = (1 - fraction) * 2 * r + x1;(x1为圆的最左边的点的x坐标)
                 *   由 y*y = r*r - (x-x1)*(x-x1)  计算得 y
                 */
                double y = Math.sqrt(r*r -(0.5-fraction)*(0.5-fraction)*2 * r*2 * r);
                Log.i(TAG + 5,"当前位置1：y =" + y);
                return y;
            }
        });

        //从最右边逆时针移动到最左端，完成上半个圆的运动轨迹
        ObjectAnimator translateRightX = ObjectAnimator.ofFloat(cycleTranslateAnimal,"translationX",x + 300,x);
        translateRightX.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                float x = fraction * ((float)endValue - (float)startValue) + (float)startValue;
                Log.i(TAG + 5,"当前位置2：x =" + x);
                return x;
            }
        });
        ObjectAnimator translateRightY = ObjectAnimator.ofFloat(cycleTranslateAnimal,"translationY",y,y + 300);
        translateRightY.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                //计算圆的半径
                float r = Math.abs((float) startValue - (float) endValue) / 2;
                /**
                 * 在匀速增长的情况下计算y的变化。依据如下方程推出：
                 * (x-a)(x-a) + (y-b)(y-b)=r*r
                 * 其中点(x1,0为圆心建立直角坐标系)，在这里，设定x随时间匀速增长，即 x = (1 - fraction) * 2 * r + x1;(x1为圆的最左边的点的x坐标)
                 *   由 y*y = r*r - (x-x1)*(x-x1)  计算得 y
                 */
                double y = Math.sqrt(r*r -(0.5-fraction)*(0.5-fraction)*2 * r*2 * r);
                Log.i(TAG + 5,"当前位置2：y =" + y);
                return -y;
            }
        });

        cycleAnimatorSet.play(translateLeftX).with(translateLeftY);
        cycleAnimatorSet.play(translateRightX).after(translateLeftX);
        cycleAnimatorSet.play(translateRightY).after(translateLeftX);

        cycleAnimatorSet.setDuration(3000);
        cycleAnimatorSet.start();
    }

    private void showRoundImgAnim(){

        float x1 = imgBg.getX(),y1 = imgBg.getY(),width1 = imgBg.getWidth(),height1 = imgBg.getHeight();
        float width2 = roundBgAnim.getWidth(),height2 = roundBgAnim.getHeight();
        if(width1 == 0|| width2 == 0 || height1 == 0 || width2 == 0){
            return;
        }
        float x = x1 - width2/2 ;
        float y = y1 - height2/2;

        Log.i(TAG + 6,String.format("x = %s,y = %s,width = %s,height = %s",x1,y1,width1,height1));
        roundAnimatorSet = new AnimatorSet();
        //向下运动
        ObjectAnimator initLocation = ObjectAnimator.ofFloat(roundBgAnim,"TranslationX",x,x);
        ObjectAnimator downAnim = ObjectAnimator.ofFloat(roundBgAnim,"TranslationY",y,y+height1);
        //向右运动
        ObjectAnimator rightAnim = ObjectAnimator.ofFloat(roundBgAnim,"TranslationX",x,x+width1);
        ObjectAnimator upAnim = ObjectAnimator.ofFloat(roundBgAnim,"TranslationY",y + height1,y);
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(roundBgAnim,"TranslationX",x+width1,x);

        roundAnimatorSet.play(initLocation).with(downAnim);
        roundAnimatorSet.play(downAnim).before(rightAnim);
        roundAnimatorSet.play(rightAnim).before(upAnim);
        roundAnimatorSet.play(upAnim).before(leftAnim);
        roundAnimatorSet.setDuration(3000);
        roundAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                Log.i(TAG + 6,"onAnimationCancel");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.i(TAG + 6,"onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                Log.i(TAG + 6,"onAnimationRepeat");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Log.i(TAG + 6,"onAnimationStart");
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
                Log.i(TAG + 6,"onAnimationPause");
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
                Log.i(TAG + 6,"onAnimationResume");
            }
        });
        roundAnimatorSet.start();

    }
}
