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
    private List<String> abcList;
    private LinearLayout navigationContainer;
    private TextView toastView;
    private RectF childRect = new RectF();
    private int position = -1;
    private Map<String, Integer> abcMap;

    public RecyclerView getRecycleView() {
        return recycleView;
    }

    public void setRecycleView(RecyclerView recycleView) {
        this.recycleView = recycleView;
    }

    private RecyclerView recycleView;
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
            abcMap = map;
            setNavigationList(map.keySet());
        }
    }

    private void setNavigationList(Set<String> set) {

        if (set != null && !set.isEmpty()) {
            abcList = new ArrayList<>(set);
            Collections.sort(abcList);
        } else {
            abcList = new ArrayList<>();
        }
        if (!abcList.isEmpty()) {
            toastView = new TextView(getContext());
            RelativeLayout.LayoutParams toastParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            toastParam.width = mToastViewSize;
            toastParam.height = toastParam.width;
            toastParam.addRule(CENTER_IN_PARENT);
            toastView.setBackground(getToastViewBackground());
            toastView.setLayoutParams(toastParam);
            toastView.setTextColor(Color.WHITE);
            toastView.setTextSize(mToastViewTextSize);
            toastView.setGravity(Gravity.CENTER);
            toastView.setPadding(0, 0, 0, 0);
            toastView.setAlpha(0.0F);
            toastView.setText(abcList.get(0));
            addView(toastView);
            navigationContainer = new LinearLayout(getContext());
            navigationContainer.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(mNavigationWidth, LayoutParams.WRAP_CONTENT);
            param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param.addRule(RelativeLayout.CENTER_VERTICAL);
            navigationContainer.setLayoutParams(param);
            for (int i = 0; i < abcList.size(); i++) {
                navigationContainer.addView(getTextView(abcList.get(i)));
            }
            navigationContainer.setBackground(mNavigationBackground);
            addView(navigationContainer);

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
        if (navigationContainer != null) {
            int parentHeight = getMeasuredHeight();
            if (parentHeight < abcList.size() * mLetterItemHeight) {
                int height = parentHeight / abcList.size();
                int specWidth = MeasureSpec.makeMeasureSpec(mNavigationWidth, MeasureSpec.EXACTLY);
                int specHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                for (int i = 0; i < abcList.size(); i++) {
                    navigationContainer.getChildAt(i).measure(specWidth, specHeight);
                }
            }
        }
    }

    private void updateChildRect() {
        childRect.left = navigationContainer.getRight() - mNavigationWidth;
        childRect.top = navigationContainer.getPaddingTop() + navigationContainer.getTop();
        childRect.bottom = abcList.size() * mLetterItemHeight + navigationContainer.getPaddingTop() + navigationContainer.getTop();
        childRect.right = navigationContainer.getRight();
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

    private int getBackgroundColor() {
        return Color.parseColor("#11000000");
    }


    private int dp2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

//    @Override
//    public boolean onInterceptHoverEvent(MotionEvent event) {
//
//        final float x = event.getX();
//        final float y = event.getY();
//        if (!childRect.contains(x, y)) {
//            return super.onInterceptHoverEvent(event);
//        }
//        return true;
//    }

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
        toastView.animate().alpha(1.0F).setDuration(500).start();
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
        toastView.animate().alpha(0.0F).setDuration(500).start();
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
                if (!childRect.contains(x, y)) {
                    return false;
                }
                bacgroundIn();
                pos = (int) (y - navigationContainer.getPaddingTop() - navigationContainer.getTop()) / getLetterHeight();
                position = pos;
                letter = abcList.get(position);
                adapterPos = abcMap.get(letter);
                toastView.setText(letter);
                scroll2Position(adapterPos, recycleView);
            }

            break;
            case MotionEvent.ACTION_MOVE:
                pos = (int) (y - navigationContainer.getPaddingTop() - navigationContainer.getTop()) / getLetterHeight();
                System.out.println("pos=" + pos);
                if (pos != position) {
                    letter = abcList.get(pos);
                    adapterPos = abcMap.get(letter);
                    scroll2Position(adapterPos, recycleView);
                    toastView.setText(letter);
                }
                position = pos;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                backgroundOut();
                position = -1;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
//        p.setColor(Color.RED);
//        canvas.drawRect(childRect, p);
    }

    private int getLetterHeight(){
        if (navigationContainer.getHeight()<abcList.size() * mLetterItemHeight){
            return navigationContainer.getHeight()/abcList.size();
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


}
