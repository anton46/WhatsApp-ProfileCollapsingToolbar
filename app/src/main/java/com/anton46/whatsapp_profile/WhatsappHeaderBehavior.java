package com.anton46.whatsapp_profile;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by anton on 11/12/15.
 */

public class WhatsappHeaderBehavior extends CoordinatorLayout.Behavior<HeaderView> {

    private Context mContext;

    private int mStartMarginLeft;
    private int mEndMarginLeft;
    private int mMarginRight;
    private int mStartMarginBottom;
    private float mTitleStartSize;
    private float mTitleEndSize;
    private boolean isHide;

    public WhatsappHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public WhatsappHeaderBehavior(Context context, AttributeSet attrs, Context mContext) {
        super(context, attrs);
        this.mContext = mContext;
    }

    public static int getToolbarHeight(Context context) {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return result;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, HeaderView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, HeaderView child, View dependency) {
        shouldInitProperties();

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;
        float childPosition = dependency.getHeight()
                + dependency.getY()
                - child.getHeight()
                - (getToolbarHeight(mContext) - child.getHeight()) * percentage / 2;

        childPosition = childPosition - mStartMarginBottom * (1f - percentage);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (Math.abs(dependency.getY()) >= maxScroll / 2) {
            float layoutPercentage = (Math.abs(dependency.getY()) - (maxScroll / 2)) / Math.abs(maxScroll / 2);
            lp.leftMargin = (int) (layoutPercentage * mEndMarginLeft) + mStartMarginLeft;
            child.setTextSize(getTranslationOffset(mTitleStartSize, mTitleEndSize, layoutPercentage));
        } else {
            lp.leftMargin = mStartMarginLeft;
        }
        lp.rightMargin = mMarginRight;
        child.setLayoutParams(lp);
        child.setY(childPosition);

        if (isHide && percentage < 1) {
            child.setVisibility(View.VISIBLE);
            isHide = false;
        } else if (!isHide && percentage == 1) {
            child.setVisibility(View.GONE);
            isHide = true;
        }
        return true;
    }

    protected float getTranslationOffset(float expandedOffset, float collapsedOffset, float ratio) {
        return expandedOffset + ratio * (collapsedOffset - expandedOffset);
    }

    private void shouldInitProperties() {
        if (mStartMarginLeft == 0) {
            mStartMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_start_margin_left);
        }

        if (mEndMarginLeft == 0) {
            mEndMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_left);
        }

        if (mStartMarginBottom == 0) {
            mStartMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom);
        }

        if (mMarginRight == 0) {
            mMarginRight = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_right);
        }

        if (mTitleStartSize == 0) {
            mTitleEndSize = mContext.getResources().getDimensionPixelSize(R.dimen.header_view_end_text_size);
        }

        if (mTitleStartSize == 0) {
            mTitleStartSize = mContext.getResources().getDimensionPixelSize(R.dimen.header_view_start_text_size);
        }
    }


}
