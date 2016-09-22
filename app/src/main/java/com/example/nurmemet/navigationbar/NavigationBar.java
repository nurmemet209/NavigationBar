package com.example.nurmemet.navigationbar;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by nurmemet on 9/10/2016.
 */
public class NavigationBar extends RelativeLayout {
    private List<String> mAbcList;
    private LinearLayout mNavigationContainer;
    private TextView mToastView;
    private RectF mChildRect = new RectF();
    private int mPosition = -1;
    private Map<String, Integer> mAbcMap;



    private RecyclerView mRecycleView;
    /**
     * 以dp为单位
     */
    private final static int navigationWidth = 25;
    /**
     * 以dp为单位
     */
    private final static int toastViewSize = 50;
    /**
     * 以dp为单位
     */
    private final static int navigationItemHeight = 40;
    private final static int navigationMaxAlpha=127;

    private int mLetterItemHeight;
    private int mToastViewSize;
    private int mNavigationWidth;
    /**
     * 以sp为单位
     */
    private int mNavigationItemTextSize=12;
    /**
     * 以sp为单位
     */
    private int mToastViewTextSize=30;
    private ColorDrawable mNavigationBackground;

    public void setToastViewSiz(int mToastViewSiz) {
        this.mToastViewSize = mToastViewSiz;
    }

    public void setNavigationWidth(int mNavigationWidth) {
        this.mNavigationWidth = mNavigationWidth;
    }

    public void setLetterItemHeight(int mLetterItemHeight) {
        this.mLetterItemHeight = mLetterItemHeight;
    }


    public NavigationBar(Context context) {
        super(context);
        init();
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        mLetterItemHeight = dp2px(getContext(), navigationItemHeight);
        mNavigationWidth = dp2px(getContext(), navigationWidth);
        mToastViewSize = dp2px(getContext(), toastViewSize);
        mNavigationBackground = new ColorDrawable(Color.GRAY);
        mNavigationBackground.setAlpha(0);


    }

    public void setAbcMap(Map<String, Integer> map) {
        if (map != null && !map.isEmpty()) {
            mAbcMap = map;
            setNavigationList(map.keySet());
        }
    }

    private void setNavigationList(Set<String> set) {

        if (set != null && !set.isEmpty()) {
            mAbcList = new ArrayList<>(set);
            Collections.sort(mAbcList);
        } else {
            mAbcList = new ArrayList<>();
        }
        if (!mAbcList.isEmpty()) {
            mToastView = new TextView(getContext());
            RelativeLayout.LayoutParams toastParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            toastParam.width = mToastViewSize;
            toastParam.height = toastParam.width;
            toastParam.addRule(CENTER_IN_PARENT);
            mToastView.setBackground(getToastViewBackground());
            mToastView.setLayoutParams(toastParam);
            mToastView.setTextColor(Color.WHITE);
            mToastView.setTextSize(mToastViewTextSize);
            mToastView.setGravity(Gravity.CENTER);
            mToastView.setPadding(0, 0, 0, 0);
            mToastView.setAlpha(0.0F);
            mToastView.setText(mAbcList.get(0));
            addView(mToastView);
            mNavigationContainer = new LinearLayout(getContext());
            mNavigationContainer.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(mNavigationWidth, LayoutParams.WRAP_CONTENT);
            param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param.addRule(RelativeLayout.CENTER_VERTICAL);
            mNavigationContainer.setLayoutParams(param);
            for (int i = 0; i < mAbcList.size(); i++) {
                mNavigationContainer.addView(getTextView(mAbcList.get(i)));
            }
            mNavigationContainer.setBackground(mNavigationBackground);
            addView(mNavigationContainer);

        }


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateChildRect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mNavigationContainer != null) {
            int parentHeight = getMeasuredHeight();
            if (parentHeight < mAbcList.size() * mLetterItemHeight) {
                int height = parentHeight / mAbcList.size();
                int specWidth = MeasureSpec.makeMeasureSpec(mNavigationWidth, MeasureSpec.EXACTLY);
                int specHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                for (int i = 0; i < mAbcList.size(); i++) {
                    mNavigationContainer.getChildAt(i).measure(specWidth, specHeight);
                }
            }
        }
    }

    private void updateChildRect() {
        mChildRect.left = mNavigationContainer.getRight() - mNavigationWidth;
        mChildRect.top = mNavigationContainer.getPaddingTop() + mNavigationContainer.getTop();
        mChildRect.bottom = mAbcList.size() * mLetterItemHeight + mNavigationContainer.getPaddingTop() + mNavigationContainer.getTop();
        mChildRect.right = mNavigationContainer.getRight();
    }

    private TextView getTextView(String s) {
        TextView label = new TextView(getContext());
        label.setBackgroundColor(Color.TRANSPARENT);
        label.setTextSize(mNavigationItemTextSize);
        label.setText(s);
        label.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.height = mLetterItemHeight;
        label.setLayoutParams(param);
        return label;

    }



    private int dp2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void bacgroundIn() {
        final ValueAnimator anim = ValueAnimator.ofInt(0, navigationMaxAlpha);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mNavigationBackground.setAlpha(value.intValue());

            }
        });
        anim.start();
        mToastView.animate().alpha(1.0F).setDuration(500).start();
    }

    private void backgroundOut() {
        final ValueAnimator anim = ValueAnimator.ofInt(navigationMaxAlpha, 0);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mNavigationBackground.setAlpha(value.intValue());

            }
        });
        anim.start();
        mToastView.animate().alpha(0.0F).setDuration(500).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final float x = event.getX();
        final float y = event.getY();

        int pos = -1;
        String letter;
        int adapterPos;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                if (!mChildRect.contains(x, y)) {
                    return false;
                }
                bacgroundIn();
                pos = (int) (y - mNavigationContainer.getPaddingTop() - mNavigationContainer.getTop()) / getLetterHeight();
                mPosition = pos;
                letter = mAbcList.get(mPosition);
                adapterPos = mAbcMap.get(letter);
                mToastView.setText(letter);
                scroll2Position(adapterPos, mRecycleView);
            }

            break;
            case MotionEvent.ACTION_MOVE:
                pos = (int) (y - mNavigationContainer.getPaddingTop() - mNavigationContainer.getTop()) / getLetterHeight();
                System.out.println("pos=" + pos);
                if (pos != mPosition) {
                    letter = mAbcList.get(pos);
                    adapterPos = mAbcMap.get(letter);
                    scroll2Position(adapterPos, mRecycleView);
                    mToastView.setText(letter);
                }
                mPosition = pos;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                backgroundOut();
                mPosition = -1;
                break;
            default:
                break;
        }
        return true;
    }

    private int getLetterHeight(){
        if (mNavigationContainer.getHeight()< mAbcList.size() * mLetterItemHeight){
            return mNavigationContainer.getHeight()/ mAbcList.size();
        }
        return mLetterItemHeight;
    }


    private void scroll2Position(int index, RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstPosition = manager.findFirstVisibleItemPosition();
        int lastPosition = manager.findLastVisibleItemPosition();
        if (index <= firstPosition) {
            manager.scrollToPosition(index);
        } else if (index <= lastPosition) {
            int top = recyclerView.getChildAt(index - firstPosition).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            manager.scrollToPositionWithOffset(index, 0);
        }
    }
    private Drawable getToastViewBackground(){
        RoundRectShape shape=new RoundRectShape(new float[]{16,16,16,16,16,16,16,16},null,null);
        ShapeDrawable shapeDrawable=new ShapeDrawable(shape);
        Paint p=shapeDrawable.getPaint();
        p.setColor(Color.BLACK);
        p.setAlpha(navigationMaxAlpha);
        p.setStyle(Paint.Style.FILL);
        ;
        return shapeDrawable;
    }

    public RecyclerView getRecycleView() {
        return mRecycleView;
    }

    public void setRecycleView(RecyclerView recycleView) {
        this.mRecycleView = recycleView;
    }


}
