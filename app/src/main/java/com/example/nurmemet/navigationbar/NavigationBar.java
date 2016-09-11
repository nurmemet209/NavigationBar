package com.example.nurmemet.navigationbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
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
    private final static int navigationWidth = 35;
    /**
     * 以dp为单位
     */
    private final static int toastViewSize = 50;
    /**
     * 以dp为单位
     */
    private final static int navigationItemHeight = 40;

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
        } else {
            abcList = new ArrayList<>();
        }
        if (!abcList.isEmpty()) {
            toastView = new TextView(getContext());
            RelativeLayout.LayoutParams toastParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            toastParam.width=dp2px(getContext(),toastViewSize);
            toastParam.height=toastParam.width;
            toastParam.addRule(CENTER_IN_PARENT);
            toastView.setBackgroundColor(getBackgroundColor());
            toastView.setLayoutParams(toastParam);
            toastView.setTextColor(Color.BLACK);
            toastView.setTextSize(30);
            toastView.setGravity(Gravity.CENTER);
            toastView.setPadding(0,0,0,0);
            addView(toastView);
            navigationContainer = new LinearLayout(getContext());
            navigationContainer.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(getNavigationWidth(), LayoutParams.WRAP_CONTENT);
            param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            param.addRule(RelativeLayout.CENTER_VERTICAL);
            navigationContainer.setLayoutParams(param);
            for (int i = 0; i < abcList.size(); i++) {
                navigationContainer.addView(getTextView(abcList.get(i)));
            }
            addView(navigationContainer);

        }


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateChildRect();
    }

    private void updateChildRect() {
        childRect.left = navigationContainer.getRight() - dp2px(getContext(), navigationWidth);
        childRect.top = navigationContainer.getPaddingTop() + navigationContainer.getTop();
        childRect.bottom = abcList.size() * dp2px(getContext(), navigationItemHeight) + navigationContainer.getPaddingTop() + navigationContainer.getTop();
        childRect.right = navigationContainer.getRight();
    }

    private TextView getTextView(String s) {
        TextView label = new TextView(getContext());
        label.setBackgroundColor(getBackgroundColor());
        label.setText(s);
        label.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.height = dp2px(getContext(), navigationItemHeight);
        label.setLayoutParams(param);
        return label;

    }

    private int getBackgroundColor() {
        return Color.parseColor("#11000000");
    }


    public int getNavigationWidth() {
        return dp2px(getContext(), navigationWidth);
    }

    public int getToastViewPadding() {
        return dp2px(getContext(), toastViewSize);
    }

    private int dp2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        if (!childRect.contains(x, y)) {
            return false;
        }
        int pos = -1;
        String letter;
        int adapterPos;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                pos = (int) (y - navigationContainer.getPaddingTop() - navigationContainer.getTop()) / dp2px(getContext(), navigationItemHeight);
                position = pos;
                letter = abcList.get(position);
                adapterPos = abcMap.get(letter);
                toastView.setText(letter);
                scroll2Position(adapterPos, recycleView);
            }

            break;
            case MotionEvent.ACTION_MOVE:
                pos = (int) (y - navigationContainer.getPaddingTop() - navigationContainer.getTop()) / dp2px(getContext(), navigationItemHeight);
                System.out.println("pos="+pos);
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
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.RED);
        canvas.drawRect(childRect, p);
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
            manager.scrollToPositionWithOffset(index,0);
        }
    }


    private class MyLinearLayoutManager extends LinearLayoutManager{

        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }


        @Override
        public void scrollToPosition(int position) {
            super.scrollToPosition(position);
        }
    }
}
