package com.android.betterway.myview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.betterway.other.ButtonSwith;
import com.android.betterway.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;



/**
 * @author Jackdow
 * @version 1.0
 * 实现一个FloatingActionButton的容器
 */
public class FloatingActionButtonMenu extends ViewGroup {
    private static final int SHOW = 1; //子控件可见时的状态
    private static final int HIDE = 2; //子控件不可见时的状态
    private static int state = HIDE; //保存状态，默认不可见
    private final float rotation = 45f; //点击时旋转角度
    private final long duration = 600; //动画事件间隔
    private final long otherDuration = 600; //其他动画的间隔
    private static final float SCALING = 1.2f; //拉伸比
    private static final String TAG = "FloatingButtonMenu"; //打印事件的标签

    public FloatingActionButtonMenu(Context context) {
        super(context);
        LogUtil.v(TAG, "constructor");
    }

    public FloatingActionButtonMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        LogUtil.v(TAG, "constructor");
    }

    public FloatingActionButtonMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtil.v(TAG, "constructor");
    }


    /**
     * 实现自带的margin属性
     * @param attrs 实现测量margin的接口
     * @return 返回测量margin的类
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        LogUtil.d(TAG, "return MarginLayoutParams");
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 测量最终容器的大小
     * @param widthMeasureSpec 测量宽度所需
     * @param heightMeasureSpec 测量高度所需
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtil.v(TAG, "onMeasure");
        // 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        // 所得既为设置match_parent时的大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 记录如果是wrap_content是设置的宽和高
        int childCount = getChildCount();
        MarginLayoutParams layoutParams;
        //宽为各个子View的最大值，高为总和
        int width = 0, height = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int cWidth = childView.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin;
            int cHeight = childView.getMeasuredHeight();
            if (cWidth > width) {
                width = cWidth;
            }
            height += cHeight + layoutParams.bottomMargin + layoutParams.topMargin;
        }
        // 若为match_parent则将宽高设置为上级推荐的大小
        //否则则设置为计算出的大小，即为wrap_parent
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY)
                ? sizeWidth : width, (heightMode == MeasureSpec.EXACTLY)
                ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.v(TAG, "onLayout");
        int cCount = getChildCount();
        MarginLayoutParams layoutParams;
         //遍历所有childView根据其宽和高,指定相应位置
        int height = 0;
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int cl = layoutParams.leftMargin;
            int cr = cl + childView.getMeasuredWidth();
            int ct = layoutParams.topMargin + height;
            int cb = ct + childView.getMeasuredHeight();
            childView.layout(cl, ct, cr, cb);
            height += childView.getMeasuredHeight() + layoutParams.bottomMargin;
            if (i != cCount - 1) {
                childView.setAlpha(0f);
                childView.setClickable(false);
            }
        }
        state = HIDE;
        setClick(getChildAt(cCount - 1));
    }

    /**设置按钮的点击监听
     * @param view 指定监听事件
     */
    private void setClick(final View view) {
        LogUtil.d(TAG, "setClick");
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == HIDE) {
                    sendMessage(ButtonSwith.OPEN);
                    showOtherView(view);
                } else {
                    sendMessage(ButtonSwith.CLOSE);
                    hideOtherView(view);
                }
            }
        });
    }

    /**
     * 发送开关闭合的消息
     * @param buttonSwith 开关状态
     */
    private void sendMessage(ButtonSwith buttonSwith) {
        EventBus.getDefault().post(buttonSwith);
    }

    /**
     * 显示其他的child view
     * @param view 按钮
     */
    private void showOtherView(View view) {
        LogUtil.d(TAG, "show other views");
        //当状态为不可见时，点击按钮弹出所有隐藏view
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, rotation);
        animator.setDuration(duration);
        animator.start();
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i);
            //简单属性动画
            ObjectAnimator childAnimator = ObjectAnimator
                    .ofFloat(childView, "alpha", 0f, 1f);
            ObjectAnimator otherAnimator = ObjectAnimator
                    .ofFloat(childView, "scaleX", 1.0f, SCALING, 1.0f);
            otherAnimator.setDuration(otherDuration);
            childAnimator.setDuration(otherDuration);
            otherAnimator.start();
            childAnimator.start();
            childView.setClickable(true);
            state = SHOW;
        }
    }

    /**
     * 隐藏其他child view
     * @param view 按钮
     */
    private void hideOtherView(View view) {
        LogUtil.d(TAG, "hide other views");
        //当状态为可见时，点击按钮隐藏所有view
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", rotation, 0f);
        animator.setDuration(duration);
        animator.start();
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i);
            ObjectAnimator childAnimator = ObjectAnimator.ofFloat(childView, "alpha", 1f, 0f);
            childAnimator.setDuration(otherDuration);
            childAnimator.start();
            childView.setClickable(false);
            state = HIDE;
        }
    }

    /**
     * 提供外部调用的函数，关闭菜单
     */
    public void closeMenu() {
        hideOtherView(getChildAt(getChildCount() - 1));
        LogUtil.v(TAG, "closeMenu");
    }
}
