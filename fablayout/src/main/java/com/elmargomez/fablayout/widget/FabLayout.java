/*
 * Copyright 2015 Elmar Rhex Gomez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elmargomez.fablayout.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.elmargomez.fablayout.R;

public class FabLayout extends ViewGroup {

    private static final int MAX_FAB_COUNT = 6;

    private Animation showAnimation = null;
    private Animation hideAnimation = null;
    private boolean isHidden = true;
    private boolean alignButtonRight = true;
    private boolean hasTittle = true;
    private boolean animateBottomTop = true;

    public FabLayout(Context context) {
        this(context, null);
    }

    public FabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fl_show_animation);
        hideAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fl_hide_animation);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fl_show_animation);
        hideAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fl_hide_animation);
        initViews();
    }


    private void initViews() {
        if (isHidden && !isInEditMode()) {
            int childSize = getChildCount();
            for (int x = 0; x < childSize; x++) {
                getChildAt(x).setVisibility(INVISIBLE);
            }
            setVisibility(GONE);
        }
    }

    public void showView() {
        if (isHidden) {
            setVisibility(VISIBLE);
            int childSize = getChildCount();
            showAnimation.setDuration(600);
            for (int x = 0; x < childSize; x++) {
                getChildAt(x).startAnimation(showAnimation);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childSize = getChildCount();
        int occupiedWidth = 0;
        int occupiedHeight = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        for (int x = 0; x < childSize; x++) {
            View view = getChildAt(x);
            boolean instanceOfView = view instanceof FloatingActionButton;

            if (instanceOfView && view.getVisibility() != GONE) {
                measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0);
                LayoutParams layout = (LayoutParams) view.getLayoutParams();
                int currentViewWidth = view.getMeasuredWidth() + layout.leftMargin +
                        layout.rightMargin;
                occupiedWidth = Math.max(currentViewWidth, occupiedWidth);
                occupiedHeight += view.getMeasuredHeight() + layout.topMargin
                        + layout.bottomMargin;
            } else if (!instanceOfView) {
                // we want to remove the non FAB view
                view.setVisibility(GONE);
            }
        }

        occupiedWidth += getPaddingLeft() + getPaddingRight();
        occupiedHeight += getPaddingTop() + getPaddingBottom();

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                occupiedWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                occupiedHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }

        setMeasuredDimension(occupiedWidth, occupiedHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childSize = getChildCount();
        // Filtering the visible zone
        int left = l + getPaddingLeft();
        int top = t + getPaddingTop();
        int right = r - getPaddingRight();
        int bottom = b - getPaddingBottom();
        int yCoordinate = bottom;

        for (int x = childSize; x >= 0; x--) {
            View view = getChildAt(x);
            boolean instanceOfView = view instanceof FloatingActionButton;

            if (instanceOfView && view.getVisibility() != GONE) {
                LayoutParams viewLayout = (LayoutParams) view.getLayoutParams();
                int xCoordinate = right - view.getMeasuredWidth() - viewLayout.rightMargin;
                yCoordinate -= (view.getMeasuredHeight() + viewLayout.bottomMargin);
                int width = xCoordinate + view.getMeasuredWidth();
                int height = yCoordinate + view.getMeasuredHeight();
                view.layout(xCoordinate, yCoordinate, width, height);
                yCoordinate -= viewLayout.topMargin;
            }
        }
    }

    public void show() {

    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

}
