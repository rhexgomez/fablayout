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
import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FabLayout extends ViewGroup {

    private static final int MAX_FAB_COUNT = 6;

    private boolean alignButtonRight = true;
    private boolean hasTittle = true;
    private boolean animateBottomTop = true;

    public FabLayout(Context context) {
        super(context);
    }

    public FabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
                int currentViewWidth = view.getMeasuredWidth() + layout.leftMargin + layout.rightMargin;
                occupiedWidth = Math.max(currentViewWidth, occupiedWidth);
                occupiedHeight += view.getMeasuredHeight() + layout.topMargin + layout.bottomMargin;
            } else if (!instanceOfView) {
                // we want to remove the non FAB view
                view.setVisibility(GONE);
            }
        }

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
        Log.e("OnLayout",l+" "+t+" "+r+" "+b);
        final int childSize = getChildCount();
        for (int x = 0; x < childSize; x++) {
            View view = getChildAt(x);
            boolean instanceOfView = view instanceof FloatingActionButton;
            if (instanceOfView && view.getVisibility() != GONE) {
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                int xCoordinate = (r - view.getMeasuredWidth()) - layoutParams.leftMargin;
                int yCoordinate = (b - view.getMeasuredHeight()) - layoutParams.bottomMargin;
                int width = r - layoutParams.rightMargin;
                int height = b - layoutParams.topMargin;
                view.layout(xCoordinate, yCoordinate, width, height);
                b -= view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }
        }
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
