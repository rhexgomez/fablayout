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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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

        int viewWidth = 0;
        int viewHeight = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);

        for (int x = 0; x < childSize; x++) {
            View view = getChildAt(x);
            boolean instanceOfView = view instanceof FloatingActionButton;
            if (instanceOfView && view.getVisibility() != GONE) {
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                viewWidth = Math.max(view.getMeasuredWidth(), viewWidth);
                viewHeight += view.getMeasuredHeight();
                Log.e("OnMeasure", ">>>> width: " + viewWidth + " height: " + viewHeight);
            } else if (!instanceOfView) {
                // we want to remove the non FAB view
                view.setVisibility(GONE);
            }
        }

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                viewWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                viewHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childSize = getChildCount();
        for (int x = 0; x < childSize; x++) {
            View view = getChildAt(x);
            boolean instanceOfView = view instanceof FloatingActionButton;
            if (instanceOfView && view.getVisibility() != GONE) {
                view.layout(r - view.getMeasuredWidth(), b - view.getMeasuredHeight(), r, b);
                b -= view.getMeasuredHeight();
            }
        }
    }

}
