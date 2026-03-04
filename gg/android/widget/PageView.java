package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View.AccessibilityDelegate;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PageView extends ViewGroup {
    interface Decor {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor;

        public LayoutParams() {
            super(-1, -1);
            this.widthFactor = 0.0f;
        }

        public LayoutParams(Context context0, AttributeSet attributeSet0) {
            super(context0, attributeSet0);
            this.widthFactor = 0.0f;
            TypedArray typedArray0 = context0.obtainStyledAttributes(attributeSet0, PageView.LAYOUT_ATTRS);
            this.gravity = typedArray0.getInteger(0, 0x30);
            typedArray0.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams viewGroup$LayoutParams0) {
            super(-1, -1);
            this.widthFactor = 0.0f;
        }
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(BasePageAdapter arg1, BasePageAdapter arg2);
    }

    public interface OnPageChangeListener {
        void onPageChange(View arg1, int arg2);

        void onPageScrollStateChanged(int arg1);

        void onPageScrolled(int arg1, float arg2, int arg3);

        void onPageSelected(int arg1);
    }

    class PageObserver extends DataSetObserver {
        static PageView access$0(PageObserver pageView$PageObserver0) {
            return PageView.this;
        }

        @Override  // android.database.DataSetObserver
        public void onChanged() {
            PageView.this.dataSetChanged();
        }

        @Override  // android.database.DataSetObserver
        public void onInvalidated() {
            PageView.this.dataSetChanged();
        }
    }

    public interface PageTransformer {
        void transformPage(View arg1, float arg2);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator CREATOR;
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        static {
            SavedState.CREATOR = new Parcelable.Creator() {
                @Override
                public SavedState createFromParcel(Parcel parcel0) {
                    return new SavedState(parcel0);
                }

                @Override  // android.os.Parcelable$Creator
                public Object createFromParcel(Parcel parcel0) {
                    return this.createFromParcel(parcel0);
                }

                @Override
                public SavedState[] newArray(int v) {
                    return new SavedState[v];
                }

                @Override  // android.os.Parcelable$Creator
                public Object[] newArray(int v) {
                    return this.newArray(v);
                }
            };
        }

        SavedState(Parcel parcel0) {
            super(parcel0);
            ClassLoader classLoader0 = this.getClass().getClassLoader();
            this.position = parcel0.readInt();
            this.adapterState = parcel0.readParcelable(classLoader0);
            this.loader = classLoader0;
        }

        public SavedState(Parcelable parcelable0) {
            super(parcelable0);
        }

        @Override
        public String toString() {
            return "FragmentPage.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        @Override  // android.view.View$BaseSavedState
        public void writeToParcel(Parcel parcel0, int v) {
            super.writeToParcel(parcel0, v);
            parcel0.writeInt(this.position);
            parcel0.writeParcelable(this.adapterState, v);
        }
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        @Override  // android.widget.PageView$OnPageChangeListener
        public void onPageChange(View view0, int v) {
        }

        @Override  // android.widget.PageView$OnPageChangeListener
        public void onPageScrollStateChanged(int v) {
        }

        @Override  // android.widget.PageView$OnPageChangeListener
        public void onPageScrolled(int v, float f, int v1) {
        }

        @Override  // android.widget.PageView$OnPageChangeListener
        public void onPageSelected(int v) {
        }
    }

    static class ViewPositionComparator implements Comparator {
        @Override
        public int compare(View view0, View view1) {
            LayoutParams pageView$LayoutParams0 = (LayoutParams)view0.getLayoutParams();
            LayoutParams pageView$LayoutParams1 = (LayoutParams)view1.getLayoutParams();
            if(!pageView$LayoutParams0.isDecor) {
                if(!pageView$LayoutParams1.isDecor) {
                    return pageView$LayoutParams0.position - pageView$LayoutParams1.position;
                }
                return pageView$LayoutParams0.isDecor ? 1 : -1;
            }
            if(pageView$LayoutParams1.isDecor) {
                return pageView$LayoutParams0.position - pageView$LayoutParams1.position;
            }
            return pageView$LayoutParams0.isDecor ? 1 : -1;
        }

        @Override
        public int compare(Object object0, Object object1) {
            return this.compare(((View)object0), ((View)object1));
        }
    }

    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator COMPARATOR = null;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS = null;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "PageView";
    private static final boolean USE_CACHE;
    private int mActivePointerId;
    private BasePageAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout;
    private float mFirstOffset;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets;
    private PageObserver mObserver;
    private int mOffscreenPageLimit;
    private OnPageChangeListener mOnPageChangeListener;
    private List mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private EdgeEffect mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private boolean mTouchScrollEnabled;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private static final Interpolator sInterpolator;
    private static final ViewPositionComparator sPositionComparator;

    static {
        PageView.LAYOUT_ATTRS = new int[]{0x10100B3};
        PageView.COMPARATOR = new Comparator() {
            @Override
            public int compare(ItemInfo pageView$ItemInfo0, ItemInfo pageView$ItemInfo1) {
                return pageView$ItemInfo0.position - pageView$ItemInfo1.position;
            }

            @Override
            public int compare(Object object0, Object object1) {
                return this.compare(((ItemInfo)object0), ((ItemInfo)object1));
            }
        };
        PageView.sInterpolator = new Interpolator() {
            @Override  // android.animation.TimeInterpolator
            public float getInterpolation(float f) {
                return (f - 1.0f) * ((f - 1.0f) * (f - 1.0f) * (f - 1.0f) * (f - 1.0f)) + 1.0f;
            }
        };
        PageView.sPositionComparator = new ViewPositionComparator();
    }

    public PageView(Context context0) {
        super(context0);
        this.mItems = new ArrayList();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.402823E+38f;
        this.mLastOffset = 3.402823E+38f;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mScrollState = 0;
        this.mEndScrollRunnable = new Runnable() {
            {
                PageView.this = pageView0;
            }

            static PageView access$0(android.widget.PageView.3 pageView$30) {
                return PageView.this;
            }

            @Override
            public void run() {
                PageView.this.setScrollState(0);
                PageView.this.populate();
            }
        };
        this.mTouchScrollEnabled = true;
        this.initPageView();
    }

    public PageView(Context context0, AttributeSet attributeSet0) {
        super(context0, attributeSet0);
        this.mItems = new ArrayList();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.402823E+38f;
        this.mLastOffset = 3.402823E+38f;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mScrollState = 0;
        this.mEndScrollRunnable = new Runnable() {
            {
                PageView.this = pageView0;
            }

            static PageView access$0(android.widget.PageView.3 pageView$30) {
                return PageView.this;
            }

            @Override
            public void run() {
                PageView.this.setScrollState(0);
                PageView.this.populate();
            }
        };
        this.mTouchScrollEnabled = true;
        this.initPageView();
    }

    static void access$S1000008(int[] arr_v) {
        PageView.LAYOUT_ATTRS = arr_v;
    }

    @Override  // android.view.ViewGroup
    public void addFocusables(ArrayList arrayList0, int v, int v1) {
        int v2 = arrayList0.size();
        int v3 = this.getDescendantFocusability();
        if(v3 != 0x60000) {
            for(int v4 = 0; v4 < this.getChildCount(); ++v4) {
                View view0 = this.getChildAt(v4);
                if(view0.getVisibility() == 0) {
                    ItemInfo pageView$ItemInfo0 = this.infoForChild(view0);
                    if(pageView$ItemInfo0 != null && pageView$ItemInfo0.position == this.mCurItem) {
                        view0.addFocusables(arrayList0, v, v1);
                    }
                }
            }
        }
        if((v3 != 0x40000 || v2 == arrayList0.size()) && this.isFocusable() && ((v1 & 1) != 1 || !this.isInTouchMode() || this.isFocusableInTouchMode())) {
            arrayList0.add(this);
        }
    }

    ItemInfo addNewItem(int v, int v1) {
        ItemInfo pageView$ItemInfo0 = new ItemInfo();
        pageView$ItemInfo0.position = v;
        pageView$ItemInfo0.object = this.mAdapter.instantiateItem(this, v);
        pageView$ItemInfo0.widthFactor = 1.0f;
        if(v1 < 0 || v1 >= this.mItems.size()) {
            this.mItems.add(pageView$ItemInfo0);
            return pageView$ItemInfo0;
        }
        this.mItems.add(v1, pageView$ItemInfo0);
        return pageView$ItemInfo0;
    }

    public void addOnPageChangeListener(OnPageChangeListener pageView$OnPageChangeListener0) {
        if(this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList();
        }
        this.mOnPageChangeListeners.add(pageView$OnPageChangeListener0);
    }

    @Override  // android.view.ViewGroup
    public void addTouchables(ArrayList arrayList0) {
        for(int v = 0; v < this.getChildCount(); ++v) {
            View view0 = this.getChildAt(v);
            if(view0.getVisibility() == 0) {
                ItemInfo pageView$ItemInfo0 = this.infoForChild(view0);
                if(pageView$ItemInfo0 != null && pageView$ItemInfo0.position == this.mCurItem) {
                    view0.addTouchables(arrayList0);
                }
            }
        }
    }

    @Override  // android.view.ViewGroup
    public void addView(View view0, int v, ViewGroup.LayoutParams viewGroup$LayoutParams0) {
        ViewGroup.LayoutParams viewGroup$LayoutParams1 = this.checkLayoutParams(viewGroup$LayoutParams0) ? viewGroup$LayoutParams0 : this.generateLayoutParams(viewGroup$LayoutParams0);
        ((LayoutParams)viewGroup$LayoutParams1).isDecor |= view0 instanceof Decor;
        if(this.mInLayout) {
            if(((LayoutParams)viewGroup$LayoutParams1) != null && ((LayoutParams)viewGroup$LayoutParams1).isDecor) {
                throw new IllegalStateException("Cannot add page decor view during layout");
            }
            ((LayoutParams)viewGroup$LayoutParams1).needsMeasure = true;
            this.addViewInLayout(view0, v, viewGroup$LayoutParams1);
            return;
        }
        super.addView(view0, v, viewGroup$LayoutParams1);
    }

    public boolean arrowScroll(int v) {
        boolean z1;
        boolean z;
        View view1;
        View view0 = this.findFocus();
        if(view0 == this) {
            view1 = null;
        }
        else {
            if(view0 != null) {
                ViewParent viewParent0 = view0.getParent();
                while(true) {
                    if(!(viewParent0 instanceof ViewGroup)) {
                        z = false;
                    }
                    else if(viewParent0 == this) {
                        z = true;
                    }
                    else {
                        goto label_22;
                    }
                    if(z) {
                        break;
                    }
                    StringBuilder stringBuilder0 = new StringBuilder();
                    stringBuilder0.append(view0.getClass().getSimpleName());
                    for(ViewParent viewParent1 = view0.getParent(); viewParent1 instanceof ViewGroup; viewParent1 = viewParent1.getParent()) {
                        stringBuilder0.append(" => ").append(viewParent1.getClass().getSimpleName());
                    }
                    Log.e("PageView", "arrowScroll tried to find focus based on non-child current focused view " + stringBuilder0.toString());
                    view1 = null;
                    goto label_25;
                label_22:
                    viewParent0 = viewParent0.getParent();
                }
            }
            view1 = view0;
        }
    label_25:
        View view2 = FocusFinder.getInstance().findNextFocus(this, view1, v);
        if(view2 == null || view2 == view1) {
            switch(v) {
                case 1: 
                case 17: {
                    z1 = this.pageLeft();
                    break;
                }
                case 66: {
                    z1 = this.pageRight();
                    break;
                }
                default: {
                    z1 = v == 2 ? this.pageRight() : false;
                }
            }
        }
        else if(v != 17) {
            if(v != 66) {
                z1 = false;
            }
            else if(view1 != null && this.getChildRectInPageCoordinates(this.mTempRect, view2).left <= this.getChildRectInPageCoordinates(this.mTempRect, view1).left) {
                z1 = this.pageRight();
            }
            else {
                z1 = view2.requestFocus();
            }
        }
        else if(view1 != null && this.getChildRectInPageCoordinates(this.mTempRect, view2).left >= this.getChildRectInPageCoordinates(this.mTempRect, view1).left) {
            z1 = this.pageLeft();
        }
        else {
            z1 = view2.requestFocus();
        }
        if(z1) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(v));
        }
        return z1;
    }

    public boolean beginFakeDrag() {
        if(this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        this.setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        if(this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        else {
            this.mVelocityTracker.clear();
        }
        long v = SystemClock.uptimeMillis();
        MotionEvent motionEvent0 = MotionEvent.obtain(v, v, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(motionEvent0);
        motionEvent0.recycle();
        this.mFakeDragBeginTime = v;
        return true;
    }

    private void calculatePageOffsets(ItemInfo pageView$ItemInfo0, int v, ItemInfo pageView$ItemInfo1) {
        int v1 = this.mAdapter.getCount();
        int v2 = this.getClientWidth();
        float f = v2 <= 0 ? 0.0f : ((float)this.mPageMargin) / ((float)v2);
        if(pageView$ItemInfo1 != null) {
            int v3 = pageView$ItemInfo1.position;
            if(v3 < pageView$ItemInfo0.position) {
                float f1 = pageView$ItemInfo1.offset + pageView$ItemInfo1.widthFactor + f;
                int v4 = v3 + 1;
                int v5 = 0;
                while(v4 <= pageView$ItemInfo0.position && v5 < this.mItems.size()) {
                    ItemInfo pageView$ItemInfo2;
                    for(pageView$ItemInfo2 = (ItemInfo)this.mItems.get(v5); v4 > pageView$ItemInfo2.position && v5 < this.mItems.size() - 1; pageView$ItemInfo2 = (ItemInfo)this.mItems.get(v5)) {
                        ++v5;
                    }
                    while(v4 < pageView$ItemInfo2.position) {
                        f1 += f + 1.0f;
                        ++v4;
                    }
                    pageView$ItemInfo2.offset = f1;
                    f1 += pageView$ItemInfo2.widthFactor + f;
                    ++v4;
                }
            }
            else if(v3 > pageView$ItemInfo0.position) {
                int v6 = this.mItems.size() - 1;
                float f2 = pageView$ItemInfo1.offset;
                for(int v7 = v3 - 1; v7 >= pageView$ItemInfo0.position && v6 >= 0; --v7) {
                    ItemInfo pageView$ItemInfo3;
                    for(pageView$ItemInfo3 = (ItemInfo)this.mItems.get(v6); v7 < pageView$ItemInfo3.position && v6 > 0; pageView$ItemInfo3 = (ItemInfo)this.mItems.get(v6)) {
                        --v6;
                    }
                    while(v7 > pageView$ItemInfo3.position) {
                        f2 -= f + 1.0f;
                        --v7;
                    }
                    f2 -= pageView$ItemInfo3.widthFactor + f;
                    pageView$ItemInfo3.offset = f2;
                }
            }
        }
        int v8 = this.mItems.size();
        float f3 = pageView$ItemInfo0.offset;
        int v9 = pageView$ItemInfo0.position - 1;
        this.mFirstOffset = pageView$ItemInfo0.position == 0 ? pageView$ItemInfo0.offset : -3.402823E+38f;
        this.mLastOffset = pageView$ItemInfo0.position == v1 - 1 ? pageView$ItemInfo0.offset + pageView$ItemInfo0.widthFactor - 1.0f : 3.402823E+38f;
        for(int v10 = v - 1; v10 >= 0; --v10) {
            ItemInfo pageView$ItemInfo4 = (ItemInfo)this.mItems.get(v10);
            float f4 = f3;
            while(v9 > pageView$ItemInfo4.position) {
                f4 -= f + 1.0f;
                --v9;
            }
            f3 = f4 - (pageView$ItemInfo4.widthFactor + f);
            pageView$ItemInfo4.offset = f3;
            if(pageView$ItemInfo4.position == 0) {
                this.mFirstOffset = f3;
            }
            --v9;
        }
        float f5 = pageView$ItemInfo0.offset + pageView$ItemInfo0.widthFactor + f;
        int v11 = pageView$ItemInfo0.position + 1;
        for(int v12 = v + 1; v12 < v8; ++v12) {
            ItemInfo pageView$ItemInfo5 = (ItemInfo)this.mItems.get(v12);
            float f6 = f5;
            while(v11 < pageView$ItemInfo5.position) {
                f6 += f + 1.0f;
                ++v11;
            }
            if(pageView$ItemInfo5.position == v1 - 1) {
                this.mLastOffset = pageView$ItemInfo5.widthFactor + f6 - 1.0f;
            }
            pageView$ItemInfo5.offset = f6;
            f5 = f6 + (pageView$ItemInfo5.widthFactor + f);
            ++v11;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    protected boolean canScroll(View view0, boolean z, int v, int v1, int v2) {
        if(view0 instanceof ViewGroup) {
            int v3 = view0.getScrollX();
            int v4 = view0.getScrollY();
            int v5 = ((ViewGroup)view0).getChildCount() - 1;
            while(v5 >= 0) {
                View view1 = ((ViewGroup)view0).getChildAt(v5);
                if(v1 + v3 < view1.getLeft() || v1 + v3 >= view1.getRight() || v2 + v4 < view1.getTop() || v2 + v4 >= view1.getBottom() || !this.canScroll(view1, true, v, v1 + v3 - view1.getLeft(), v2 + v4 - view1.getTop())) {
                    --v5;
                    continue;
                }
                return true;
            }
        }
        return z && view0.canScrollHorizontally(-v);
    }

    @Override  // android.view.View
    public boolean canScrollHorizontally(int v) {
        if(this.mAdapter == null) {
            return false;
        }
        int v1 = this.getClientWidth();
        int v2 = this.getScrollX();
        return v >= 0 ? v > 0 && v2 < ((int)(((float)v1) * this.mLastOffset)) : v2 > ((int)(((float)v1) * this.mFirstOffset));
    }

    // 去混淆评级： 低(20)
    @Override  // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams viewGroup$LayoutParams0) {
        return viewGroup$LayoutParams0 instanceof LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams0);
    }

    public void clearOnPageChangeListeners() {
        if(this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.clear();
        }
    }

    private void completeScroll(boolean z) {
        int v = this.mScrollState == 2 ? 1 : 0;
        if(v != 0) {
            this.setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int v1 = this.getScrollX();
            int v2 = this.getScrollY();
            int v3 = this.mScroller.getCurrX();
            int v4 = this.mScroller.getCurrY();
            if(v1 != v3 || v2 != v4) {
                this.scrollTo(v3, v4);
                if(v3 != v1) {
                    this.pageScrolled(v3);
                }
            }
        }
        this.mPopulatePending = false;
        int v6 = v;
        for(int v5 = 0; v5 < this.mItems.size(); ++v5) {
            ItemInfo pageView$ItemInfo0 = (ItemInfo)this.mItems.get(v5);
            if(pageView$ItemInfo0.scrolling) {
                pageView$ItemInfo0.scrolling = false;
                v6 = 1;
            }
        }
        if(v6 != 0) {
            if(z) {
                this.postOnAnimation(this.mEndScrollRunnable);
                return;
            }
            this.mEndScrollRunnable.run();
        }
    }

    @Override  // android.view.View
    public void computeScroll() {
        if(!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int v = this.getScrollX();
            int v1 = this.getScrollY();
            int v2 = this.mScroller.getCurrX();
            int v3 = this.mScroller.getCurrY();
            if(v != v2 || v1 != v3) {
                this.scrollTo(v2, v3);
                if(!this.pageScrolled(v2)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, v3);
                }
            }
            this.postInvalidateOnAnimation();
            return;
        }
        this.completeScroll(true);
    }

    void dataSetChanged() {
        int v = this.mAdapter.getCount();
        this.mExpectedAdapterCount = v;
        boolean z = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < v;
        int v1 = this.mCurItem;
        for(int v2 = 0; v2 < this.mItems.size(); ++v2) {
            ItemInfo pageView$ItemInfo0 = (ItemInfo)this.mItems.get(v2);
        }
        Collections.sort(this.mItems, PageView.COMPARATOR);
        if(z) {
            int v3 = this.getChildCount();
            for(int v4 = 0; v4 < v3; ++v4) {
                LayoutParams pageView$LayoutParams0 = (LayoutParams)this.getChildAt(v4).getLayoutParams();
                if(!pageView$LayoutParams0.isDecor) {
                    pageView$LayoutParams0.widthFactor = 0.0f;
                }
            }
            this.setCurrentItemInternal(v1, false, true);
            this.requestLayout();
        }
    }

    private int determineTargetPage(int v, float f, int v1, int v2) {
        if(Math.abs(v2) <= this.mFlingDistance || Math.abs(v1) <= this.mMinimumVelocity) {
            v = (int)((v < this.mCurItem ? 0.6f : 0.4f) + (((float)v) + f));
        }
        else if(v1 <= 0) {
            ++v;
        }
        if(this.mItems.size() > 0) {
            ItemInfo pageView$ItemInfo0 = (ItemInfo)this.mItems.get(0);
            int v3 = Math.min(v, ((ItemInfo)this.mItems.get(this.mItems.size() - 1)).position);
            return Math.max(pageView$ItemInfo0.position, v3);
        }
        return v;
    }

    // 去混淆评级： 低(20)
    @Override  // android.view.ViewGroup
    public boolean dispatchKeyEvent(KeyEvent keyEvent0) {
        return super.dispatchKeyEvent(keyEvent0) || this.executeKeyEvent(keyEvent0);
    }

    private void dispatchOnPageScrolled(int v, float f, int v1) {
        if(this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(v, f, v1);
        }
        if(this.mOnPageChangeListeners != null) {
            int v2 = this.mOnPageChangeListeners.size();
            for(int v3 = 0; v3 < v2; ++v3) {
                OnPageChangeListener pageView$OnPageChangeListener0 = (OnPageChangeListener)this.mOnPageChangeListeners.get(v3);
                if(pageView$OnPageChangeListener0 != null) {
                    pageView$OnPageChangeListener0.onPageScrolled(v, f, v1);
                }
            }
        }
        if(this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(v, f, v1);
        }
    }

    private void dispatchOnPageSelected(int v) {
        if(this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(v);
            this.mOnPageChangeListener.onPageChange(this.getChildAt(v), v);
        }
        if(this.mOnPageChangeListeners != null) {
            int v1 = this.mOnPageChangeListeners.size();
            for(int v2 = 0; v2 < v1; ++v2) {
                OnPageChangeListener pageView$OnPageChangeListener0 = (OnPageChangeListener)this.mOnPageChangeListeners.get(v2);
                if(pageView$OnPageChangeListener0 != null) {
                    pageView$OnPageChangeListener0.onPageSelected(v);
                }
            }
        }
        if(this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(v);
        }
    }

    private void dispatchOnScrollStateChanged(int v) {
        if(this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrollStateChanged(v);
        }
        if(this.mOnPageChangeListeners != null) {
            int v1 = this.mOnPageChangeListeners.size();
            for(int v2 = 0; v2 < v1; ++v2) {
                OnPageChangeListener pageView$OnPageChangeListener0 = (OnPageChangeListener)this.mOnPageChangeListeners.get(v2);
                if(pageView$OnPageChangeListener0 != null) {
                    pageView$OnPageChangeListener0.onPageScrollStateChanged(v);
                }
            }
        }
        if(this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrollStateChanged(v);
        }
    }

    @Override  // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent0) {
        if(accessibilityEvent0.getEventType() == 0x1000) {
            return super.dispatchPopulateAccessibilityEvent(accessibilityEvent0);
        }
        int v = this.getChildCount();
        for(int v1 = 0; v1 < v; ++v1) {
            View view0 = this.getChildAt(v1);
            if(view0.getVisibility() == 0) {
                ItemInfo pageView$ItemInfo0 = this.infoForChild(view0);
                if(pageView$ItemInfo0 != null && pageView$ItemInfo0.position == this.mCurItem && view0.dispatchPopulateAccessibilityEvent(accessibilityEvent0)) {
                    return true;
                }
            }
        }
        return false;
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float)Math.sin(((float)(((double)(f - 0.5f)) * 0.471239)));
    }

    @Override  // android.view.View
    public void draw(Canvas canvas0) {
        super.draw(canvas0);
        int v = 0;
        int v1 = this.getOverScrollMode();
        if(v1 != 0 && (v1 != 1 || this.mAdapter == null || this.mAdapter.getCount() <= 1)) {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        else {
            if(!this.mLeftEdge.isFinished()) {
                int v2 = canvas0.save();
                int v3 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                int v4 = this.getWidth();
                canvas0.rotate(270.0f);
                canvas0.translate(((float)(this.getPaddingTop() - v3)), this.mFirstOffset * ((float)v4));
                this.mLeftEdge.setSize(v3, v4);
                v = this.mLeftEdge.draw(canvas0);
                canvas0.restoreToCount(v2);
            }
            if(!this.mRightEdge.isFinished()) {
                int v5 = canvas0.save();
                int v6 = this.getWidth();
                int v7 = this.getHeight();
                int v8 = this.getPaddingTop();
                int v9 = this.getPaddingBottom();
                canvas0.rotate(90.0f);
                canvas0.translate(((float)(-this.getPaddingTop())), -(this.mLastOffset + 1.0f) * ((float)v6));
                this.mRightEdge.setSize(v7 - v8 - v9, v6);
                v |= this.mRightEdge.draw(canvas0);
                canvas0.restoreToCount(v5);
            }
        }
        if(v != 0) {
            this.postInvalidateOnAnimation();
        }
    }

    @Override  // android.view.ViewGroup
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable0 = this.mMarginDrawable;
        if(drawable0 != null && drawable0.isStateful()) {
            drawable0.setState(this.getDrawableState());
        }
    }

    private void enableLayers(boolean z) {
        int v = this.getChildCount();
        for(int v1 = 0; v1 < v; ++v1) {
            this.getChildAt(v1).setLayerType((z ? 2 : 0), null);
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if(this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void endFakeDrag() {
        if(!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        VelocityTracker velocityTracker0 = this.mVelocityTracker;
        velocityTracker0.computeCurrentVelocity(1000, ((float)this.mMaximumVelocity));
        int v = (int)velocityTracker0.getXVelocity(this.mActivePointerId);
        this.mPopulatePending = true;
        int v1 = this.getClientWidth();
        int v2 = this.getScrollX();
        ItemInfo pageView$ItemInfo0 = this.infoForCurrentScrollPosition();
        this.setCurrentItemInternal(this.determineTargetPage(pageView$ItemInfo0.position, (((float)v2) / ((float)v1) - pageView$ItemInfo0.offset) / pageView$ItemInfo0.widthFactor, v, ((int)(this.mLastMotionX - this.mInitialMotionX))), true, true, v);
        this.endDrag();
        this.mFakeDragging = false;
    }

    public boolean executeKeyEvent(KeyEvent keyEvent0) {
        if(keyEvent0.getAction() == 0) {
            switch(keyEvent0.getKeyCode()) {
                case 21: {
                    return this.arrowScroll(17);
                }
                case 22: {
                    return this.arrowScroll(66);
                }
                case 61: {
                    if(Build.VERSION.SDK_INT >= 11) {
                        if(keyEvent0.hasNoModifiers()) {
                            return this.arrowScroll(2);
                        }
                        return keyEvent0.hasModifiers(1) ? this.arrowScroll(1) : false;
                    }
                    break;
                }
                default: {
                    return false;
                }
            }
        }
        return false;
    }

    public void fakeDragBy(float f) {
        if(!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        this.mLastMotionX += f;
        float f1 = ((float)this.getScrollX()) - f;
        int v = this.getClientWidth();
        float f2 = ((float)v) * this.mFirstOffset;
        float f3 = ((float)v) * this.mLastOffset;
        ItemInfo pageView$ItemInfo0 = (ItemInfo)this.mItems.get(0);
        ItemInfo pageView$ItemInfo1 = (ItemInfo)this.mItems.get(this.mItems.size() - 1);
        float f4 = pageView$ItemInfo0.position == 0 ? f2 : pageView$ItemInfo0.offset * ((float)v);
        float f5 = pageView$ItemInfo1.position == this.mAdapter.getCount() - 1 ? f3 : pageView$ItemInfo1.offset * ((float)v);
        if(f1 >= f4) {
            f4 = f1 > f5 ? f5 : f1;
        }
        this.mLastMotionX += f4 - ((float)(((int)f4)));
        this.scrollTo(((int)f4), this.getScrollY());
        this.pageScrolled(((int)f4));
        long v1 = SystemClock.uptimeMillis();
        MotionEvent motionEvent0 = MotionEvent.obtain(this.mFakeDragBeginTime, v1, 2, this.mLastMotionX, 0.0f, 0);
        this.mVelocityTracker.addMovement(motionEvent0);
        motionEvent0.recycle();
    }

    @Override  // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override  // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet0) {
        return new LayoutParams(this.getContext(), attributeSet0);
    }

    @Override  // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams viewGroup$LayoutParams0) {
        return this.generateDefaultLayoutParams();
    }

    public BasePageAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override  // android.view.ViewGroup
    protected int getChildDrawingOrder(int v, int v1) {
        if(this.mDrawingOrder == 2) {
            v1 = v - 1 - v1;
        }
        return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(v1)).getLayoutParams()).childIndex;
    }

    private Rect getChildRectInPageCoordinates(Rect rect0, View view0) {
        Rect rect1 = rect0 == null ? new Rect() : rect0;
        if(view0 == null) {
            rect1.set(0, 0, 0, 0);
            return rect1;
        }
        rect1.left = view0.getLeft();
        rect1.right = view0.getRight();
        rect1.top = view0.getTop();
        rect1.bottom = view0.getBottom();
        for(ViewParent viewParent0 = view0.getParent(); viewParent0 instanceof ViewGroup && viewParent0 != this; viewParent0 = ((ViewGroup)viewParent0).getParent()) {
            rect1.left += ((ViewGroup)viewParent0).getLeft();
            rect1.right += ((ViewGroup)viewParent0).getRight();
            rect1.top += ((ViewGroup)viewParent0).getTop();
            rect1.bottom += ((ViewGroup)viewParent0).getBottom();
        }
        return rect1;
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    ItemInfo infoForAnyChild(View view0) {
        while(true) {
            ViewParent viewParent0 = view0.getParent();
            if(viewParent0 == this) {
                return this.infoForChild(view0);
            }
            if(viewParent0 == null || !(viewParent0 instanceof View)) {
                return null;
            }
            view0 = (View)viewParent0;
        }
    }

    ItemInfo infoForChild(View view0) {
        ItemInfo pageView$ItemInfo0;
        for(int v = 0; true; ++v) {
            if(v >= this.mItems.size()) {
                return null;
            }
            pageView$ItemInfo0 = (ItemInfo)this.mItems.get(v);
            if(this.mAdapter.isViewFromObject(view0, pageView$ItemInfo0.object)) {
                break;
            }
        }
        return pageView$ItemInfo0;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        ItemInfo pageView$ItemInfo2;
        int v = this.getClientWidth();
        float f = v <= 0 ? 0.0f : ((float)this.getScrollX()) / ((float)v);
        float f1 = v <= 0 ? 0.0f : ((float)this.mPageMargin) / ((float)v);
        int v1 = -1;
        int v2 = 0;
        ItemInfo pageView$ItemInfo0 = null;
        boolean z = true;
        float f2 = 0.0f;
        for(float f3 = 0.0f; v2 < this.mItems.size(); f3 = f4) {
            ItemInfo pageView$ItemInfo1 = (ItemInfo)this.mItems.get(v2);
            if(z || pageView$ItemInfo1.position == v1 + 1) {
                pageView$ItemInfo2 = pageView$ItemInfo1;
            }
            else {
                this.mTempItem.offset = f3 + f2 + f1;
                this.mTempItem.position = v1 + 1;
                this.mTempItem.widthFactor = 1.0f;
                --v2;
                pageView$ItemInfo2 = this.mTempItem;
            }
            float f4 = pageView$ItemInfo2.offset;
            float f5 = pageView$ItemInfo2.widthFactor + f4 + f1;
            if(!z && f < f4) {
                break;
            }
            if(f < f5 || v2 == this.mItems.size() - 1) {
                return pageView$ItemInfo2;
            }
            v1 = pageView$ItemInfo2.position;
            f2 = pageView$ItemInfo2.widthFactor;
            ++v2;
            pageView$ItemInfo0 = pageView$ItemInfo2;
            z = false;
        }
        return pageView$ItemInfo0;
    }

    ItemInfo infoForPosition(int v) {
        ItemInfo pageView$ItemInfo0;
        for(int v1 = 0; true; ++v1) {
            if(v1 >= this.mItems.size()) {
                return null;
            }
            pageView$ItemInfo0 = (ItemInfo)this.mItems.get(v1);
            if(pageView$ItemInfo0.position == v) {
                break;
            }
        }
        return pageView$ItemInfo0;
    }

    void initPageView() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(0x40000);
        this.setFocusable(true);
        Context context0 = this.getContext();
        this.mScroller = new Scroller(context0, PageView.sInterpolator);
        ViewConfiguration viewConfiguration0 = ViewConfiguration.get(context0);
        float f = context0.getResources().getDisplayMetrics().density;
        this.mTouchSlop = viewConfiguration0.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int)(400.0f * f);
        this.mMaximumVelocity = viewConfiguration0.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context0);
        this.mRightEdge = new EdgeEffect(context0);
        this.mFlingDistance = (int)(25.0f * f);
        this.mCloseEnough = (int)(2.0f * f);
        this.mDefaultGutterSize = (int)(16.0f * f);
        this.setAccessibilityDelegate(new View.AccessibilityDelegate());
        if(Build.VERSION.SDK_INT >= 16 && this.getImportantForAccessibility() == 0) {
            this.setImportantForAccessibility(1);
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private boolean isGutterDrag(float f, float f1) {
        return f < ((float)this.mGutterSize) && f1 > 0.0f || f > ((float)(this.getWidth() - this.mGutterSize)) && f1 < 0.0f;
    }

    @Override  // android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    @Override  // android.view.ViewGroup
    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    @Override  // android.view.View
    protected void onDraw(Canvas canvas0) {
        float f2;
        super.onDraw(canvas0);
        if(this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int v = this.getScrollX();
            int v1 = this.getWidth();
            float f = ((float)this.mPageMargin) / ((float)v1);
            ItemInfo pageView$ItemInfo0 = (ItemInfo)this.mItems.get(0);
            float f1 = pageView$ItemInfo0.offset;
            int v2 = this.mItems.size();
            int v3 = pageView$ItemInfo0.position;
            int v4 = ((ItemInfo)this.mItems.get(v2 - 1)).position;
            int v5 = v3;
            int v6 = 0;
            while(v5 < v4) {
                while(v5 > pageView$ItemInfo0.position && v6 < v2) {
                    ++v6;
                    pageView$ItemInfo0 = (ItemInfo)this.mItems.get(v6);
                }
                if(v5 == pageView$ItemInfo0.position) {
                    f2 = (pageView$ItemInfo0.offset + pageView$ItemInfo0.widthFactor) * ((float)v1);
                    f1 = pageView$ItemInfo0.offset + pageView$ItemInfo0.widthFactor + f;
                }
                else {
                    f2 = (f1 + 1.0f) * ((float)v1);
                    f1 += f + 1.0f;
                }
                if(((float)this.mPageMargin) + f2 > ((float)v)) {
                    this.mMarginDrawable.setBounds(((int)f2), this.mTopPageBounds, ((int)(((float)this.mPageMargin) + f2 + 0.5f)), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas0);
                }
                if(f2 > ((float)(v + v1))) {
                    break;
                }
                ++v5;
            }
        }
    }

    @Override  // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent0) {
        if(this.mTouchScrollEnabled) {
            int v = motionEvent0.getAction();
            switch(v & 0xFF) {
                case 0: {
                label_9:
                    switch(v & 0xFF) {
                        case 0: {
                            float f = motionEvent0.getX();
                            this.mInitialMotionX = f;
                            this.mLastMotionX = f;
                            float f1 = motionEvent0.getY();
                            this.mInitialMotionY = f1;
                            this.mLastMotionY = f1;
                            this.mActivePointerId = motionEvent0.getPointerId(0);
                            this.mIsUnableToDrag = false;
                            this.mScroller.computeScrollOffset();
                            if(this.mScrollState != 2 || Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) <= this.mCloseEnough) {
                                this.completeScroll(false);
                                this.mIsBeingDragged = false;
                            }
                            else {
                                this.mScroller.abortAnimation();
                                this.mPopulatePending = false;
                                this.populate();
                                this.mIsBeingDragged = true;
                                this.requestParentDisallowInterceptTouchEvent(true);
                                this.setScrollState(1);
                            }
                            break;
                        }
                        case 2: {
                            int v1 = this.mActivePointerId;
                            if(v1 != -1) {
                                int v2 = motionEvent0.findPointerIndex(v1);
                                float f2 = motionEvent0.getX(v2);
                                float f3 = f2 - this.mLastMotionX;
                                float f4 = Math.abs(f3);
                                float f5 = motionEvent0.getY(v2);
                                float f6 = Math.abs(f5 - this.mInitialMotionY);
                                if(f3 != 0.0f && !this.isGutterDrag(this.mLastMotionX, f3) && this.canScroll(this, false, ((int)f3), ((int)f2), ((int)f5))) {
                                    this.mLastMotionX = f2;
                                    this.mLastMotionY = f5;
                                    this.mIsUnableToDrag = true;
                                    return false;
                                }
                                if(f4 > ((float)this.mTouchSlop) && 0.5f * f4 > f6) {
                                    this.mIsBeingDragged = true;
                                    this.requestParentDisallowInterceptTouchEvent(true);
                                    this.setScrollState(1);
                                    this.mLastMotionX = f3 > 0.0f ? this.mInitialMotionX + ((float)this.mTouchSlop) : this.mInitialMotionX - ((float)this.mTouchSlop);
                                    this.mLastMotionY = f5;
                                    this.setScrollingCacheEnabled(true);
                                }
                                else if(f6 > ((float)this.mTouchSlop)) {
                                    this.mIsUnableToDrag = true;
                                }
                                if(this.mIsBeingDragged && this.performDrag(f2)) {
                                    this.postInvalidateOnAnimation();
                                }
                            }
                            break;
                        }
                        case 6: {
                            this.onSecondaryPointerUp(motionEvent0);
                        }
                    }
                    if(this.mVelocityTracker == null) {
                        this.mVelocityTracker = VelocityTracker.obtain();
                    }
                    this.mVelocityTracker.addMovement(motionEvent0);
                    return this.mIsBeingDragged;
                }
                case 1: 
                case 3: {
                    this.resetTouch();
                    return false;
                label_6:
                    if(this.mIsBeingDragged) {
                        return true;
                    }
                    if(!this.mIsUnableToDrag) {
                        goto label_9;
                    }
                    break;
                }
                default: {
                    goto label_6;
                }
            }
        }
        return false;
    }

    @Override  // android.view.ViewGroup
    protected void onLayout(boolean z, int v, int v1, int v2, int v3) {
        int v20;
        int v19;
        int v18;
        int v17;
        int v16;
        int v15;
        int v4 = this.getChildCount();
        int v5 = v2 - v;
        int v6 = v3 - v1;
        int v7 = this.getPaddingLeft();
        int v8 = this.getPaddingTop();
        int v9 = this.getPaddingRight();
        int v10 = this.getPaddingBottom();
        int v11 = this.getScrollX();
        int v12 = 0;
        int v13 = 0;
        while(v13 < v4) {
            View view0 = this.getChildAt(v13);
            if(view0.getVisibility() == 8) {
                v20 = v12;
                v16 = v9;
                v17 = v7;
            }
            else {
                LayoutParams pageView$LayoutParams0 = (LayoutParams)view0.getLayoutParams();
                if(pageView$LayoutParams0.isDecor) {
                    int v14 = pageView$LayoutParams0.gravity & 0x70;
                    switch(pageView$LayoutParams0.gravity & 7) {
                        case 1: {
                            v15 = Math.max((v5 - view0.getMeasuredWidth()) / 2, v7);
                            v16 = v9;
                            v17 = v7;
                            break;
                        }
                        case 3: {
                            v17 = v7 + view0.getMeasuredWidth();
                            v15 = v7;
                            v16 = v9;
                            break;
                        }
                        case 5: {
                            v15 = v5 - v9 - view0.getMeasuredWidth();
                            v16 = v9 + view0.getMeasuredWidth();
                            v17 = v7;
                            break;
                        }
                        default: {
                            v15 = v7;
                            v16 = v9;
                            v17 = v7;
                        }
                    }
                    switch(v14) {
                        case 16: {
                            v18 = Math.max((v6 - view0.getMeasuredHeight()) / 2, v8);
                            v19 = v8;
                            break;
                        }
                        case 0x30: {
                            v19 = v8 + view0.getMeasuredHeight();
                            v18 = v8;
                            break;
                        }
                        case 80: {
                            v18 = v6 - v10 - view0.getMeasuredHeight();
                            v10 += view0.getMeasuredHeight();
                            v19 = v8;
                            break;
                        }
                        default: {
                            v18 = v8;
                            v19 = v8;
                        }
                    }
                    view0.layout(v15 + v11, v18, view0.getMeasuredWidth() + (v15 + v11), view0.getMeasuredHeight() + v18);
                    v20 = v12 + 1;
                    v8 = v19;
                }
            }
            ++v13;
            v12 = v20;
            v9 = v16;
            v7 = v17;
        }
        int v21 = v5 - v7 - v9;
        for(int v22 = 0; v22 < v4; ++v22) {
            View view1 = this.getChildAt(v22);
            if(view1.getVisibility() != 8) {
                LayoutParams pageView$LayoutParams1 = (LayoutParams)view1.getLayoutParams();
                if(!pageView$LayoutParams1.isDecor) {
                    ItemInfo pageView$ItemInfo0 = this.infoForChild(view1);
                    if(pageView$ItemInfo0 != null) {
                        int v23 = ((int)(pageView$ItemInfo0.offset * ((float)v21))) + v7;
                        if(pageView$LayoutParams1.needsMeasure) {
                            pageView$LayoutParams1.needsMeasure = false;
                            view1.measure(View.MeasureSpec.makeMeasureSpec(((int)(pageView$LayoutParams1.widthFactor * ((float)v21))), 0x40000000), View.MeasureSpec.makeMeasureSpec(v6 - v8 - v10, 0x40000000));
                        }
                        view1.layout(v23, v8, view1.getMeasuredWidth() + v23, view1.getMeasuredHeight() + v8);
                    }
                }
            }
        }
        this.mTopPageBounds = v8;
        this.mBottomPageBounds = v6 - v10;
        this.mDecorChildCount = v12;
        if(this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, true);
        }
        this.mFirstLayout = false;
    }

    @Override  // android.view.View
    protected void onMeasure(int v, int v1) {
        int v12;
        int v11;
        this.setMeasuredDimension(View.getDefaultSize(0, v), View.getDefaultSize(0, v1));
        int v2 = this.getMeasuredWidth();
        this.mGutterSize = Math.min(v2 / 10, this.mDefaultGutterSize);
        int v3 = v2 - this.getPaddingLeft() - this.getPaddingRight();
        int v4 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int v5 = this.getChildCount();
        for(int v6 = 0; v6 < v5; ++v6) {
            View view0 = this.getChildAt(v6);
            if(view0.getVisibility() != 8) {
                LayoutParams pageView$LayoutParams0 = (LayoutParams)view0.getLayoutParams();
                if(pageView$LayoutParams0 != null && pageView$LayoutParams0.isDecor) {
                    int v7 = pageView$LayoutParams0.gravity & 7;
                    int v8 = 0x80000000;
                    int v9 = 0x80000000;
                    boolean z = (pageView$LayoutParams0.gravity & 0x70) == 0x30 || (pageView$LayoutParams0.gravity & 0x70) == 80;
                    boolean z1 = v7 == 3 || v7 == 5;
                    if(z) {
                        v8 = 0x40000000;
                    }
                    else if(z1) {
                        v9 = 0x40000000;
                    }
                    int v10 = 0x40000000;
                    switch(pageView$LayoutParams0.width) {
                        case -2: {
                            v11 = v3;
                            v10 = v8;
                            break;
                        }
                        case -1: {
                            v11 = v3;
                            break;
                        }
                        default: {
                            v11 = pageView$LayoutParams0.width;
                        }
                    }
                    if(pageView$LayoutParams0.height == -2) {
                        v12 = v4;
                    }
                    else {
                        v9 = 0x40000000;
                        if(pageView$LayoutParams0.height != -1) {
                            v12 = pageView$LayoutParams0.height;
                        }
                    }
                    view0.measure(View.MeasureSpec.makeMeasureSpec(v11, v10), View.MeasureSpec.makeMeasureSpec(v12, v9));
                    if(z) {
                        v4 -= view0.getMeasuredHeight();
                    }
                    else if(z1) {
                        v3 -= view0.getMeasuredWidth();
                    }
                }
            }
        }
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(v3, 0x40000000);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(v4, 0x40000000);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        int v13 = this.getChildCount();
        for(int v14 = 0; v14 < v13; ++v14) {
            View view1 = this.getChildAt(v14);
            if(view1.getVisibility() != 8) {
                LayoutParams pageView$LayoutParams1 = (LayoutParams)view1.getLayoutParams();
                if(pageView$LayoutParams1 == null || !pageView$LayoutParams1.isDecor) {
                    view1.measure(View.MeasureSpec.makeMeasureSpec(((int)(pageView$LayoutParams1.widthFactor * ((float)v3))), 0x40000000), this.mChildHeightMeasureSpec);
                }
            }
        }
    }

    protected void onPageScrolled(int v, float f, int v1) {
        int v8;
        int v9;
        if(this.mDecorChildCount > 0) {
            int v2 = this.getScrollX();
            int v3 = this.getPaddingLeft();
            int v4 = this.getPaddingRight();
            int v5 = this.getWidth();
            int v6 = this.getChildCount();
            int v7 = 0;
            while(v7 < v6) {
                View view0 = this.getChildAt(v7);
                LayoutParams pageView$LayoutParams0 = (LayoutParams)view0.getLayoutParams();
                if(pageView$LayoutParams0.isDecor) {
                    switch(pageView$LayoutParams0.gravity & 7) {
                        case 1: {
                            v9 = Math.max((v5 - view0.getMeasuredWidth()) / 2, v3);
                            v8 = v3;
                            break;
                        }
                        case 3: {
                            v8 = v3 + view0.getWidth();
                            v9 = v3;
                            break;
                        }
                        case 5: {
                            v9 = v5 - v4 - view0.getMeasuredWidth();
                            v4 += view0.getMeasuredWidth();
                            v8 = v3;
                            break;
                        }
                        default: {
                            v9 = v3;
                            v8 = v3;
                        }
                    }
                    int v10 = v9 + v2 - view0.getLeft();
                    if(v10 != 0) {
                        view0.offsetLeftAndRight(v10);
                    }
                }
                else {
                    v8 = v3;
                }
                ++v7;
                v3 = v8;
            }
        }
        this.dispatchOnPageScrolled(v, f, v1);
        if(this.mPageTransformer != null) {
            int v11 = this.getScrollX();
            int v12 = this.getChildCount();
            for(int v13 = 0; v13 < v12; ++v13) {
                View view1 = this.getChildAt(v13);
                if(!((LayoutParams)view1.getLayoutParams()).isDecor) {
                    float f1 = ((float)(view1.getLeft() - v11)) / ((float)this.getClientWidth());
                    this.mPageTransformer.transformPage(view1, f1);
                }
            }
        }
        this.mCalledSuper = true;
    }

    @Override  // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int v, Rect rect0) {
        int v3;
        int v2;
        int v1 = this.getChildCount();
        if((v & 2) == 0) {
            v3 = v1 - 1;
            v1 = -1;
            v2 = -1;
        }
        else {
            v2 = 1;
            v3 = 0;
        }
        while(true) {
            if(v3 == v1) {
                return false;
            }
            View view0 = this.getChildAt(v3);
            if(view0.getVisibility() == 0) {
                ItemInfo pageView$ItemInfo0 = this.infoForChild(view0);
                if(pageView$ItemInfo0 != null && pageView$ItemInfo0.position == this.mCurItem && view0.requestFocus(v, rect0)) {
                    return true;
                }
            }
            v3 += v2;
        }
    }

    @Override  // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable0) {
        if(!(parcelable0 instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable0);
            return;
        }
        super.onRestoreInstanceState(((SavedState)parcelable0).getSuperState());
        if(this.mAdapter != null) {
            this.setCurrentItemInternal(((SavedState)parcelable0).position, false, true);
            return;
        }
        this.mRestoredCurItem = ((SavedState)parcelable0).position;
        this.mRestoredAdapterState = ((SavedState)parcelable0).adapterState;
        this.mRestoredClassLoader = ((SavedState)parcelable0).loader;
    }

    @Override  // android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable0 = new SavedState(super.onSaveInstanceState());
        parcelable0.position = this.mCurItem;
        if(this.mAdapter != null) {
            parcelable0.adapterState = null;
        }
        return parcelable0;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent0) {
        int v = motionEvent0.getActionIndex();
        if(motionEvent0.getPointerId(v) == this.mActivePointerId) {
            int v1 = v == 0 ? 1 : 0;
            this.mLastMotionX = motionEvent0.getX(v1);
            this.mActivePointerId = motionEvent0.getPointerId(v1);
            if(this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    @Override  // android.view.View
    protected void onSizeChanged(int v, int v1, int v2, int v3) {
        super.onSizeChanged(v, v1, v2, v3);
        if(v != v2) {
            this.recomputeScrollPosition(v, v2, this.mPageMargin, this.mPageMargin);
        }
    }

    @Override  // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent0) {
        boolean z = false;
        if(this.mFakeDragging) {
            return true;
        }
        if((motionEvent0.getAction() != 0 || motionEvent0.getEdgeFlags() == 0) && this.mAdapter != null && this.mAdapter.getCount() != 0) {
            if(this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent0);
            switch(motionEvent0.getAction() & 0xFF) {
                case 0: {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    float f = motionEvent0.getX();
                    this.mInitialMotionX = f;
                    this.mLastMotionX = f;
                    float f1 = motionEvent0.getY();
                    this.mInitialMotionY = f1;
                    this.mLastMotionY = f1;
                    this.mActivePointerId = motionEvent0.getPointerId(0);
                    break;
                }
                case 1: {
                    if(this.mIsBeingDragged) {
                        VelocityTracker velocityTracker0 = this.mVelocityTracker;
                        velocityTracker0.computeCurrentVelocity(1000, ((float)this.mMaximumVelocity));
                        int v = (int)velocityTracker0.getXVelocity(this.mActivePointerId);
                        this.mPopulatePending = true;
                        int v1 = this.getClientWidth();
                        int v2 = this.getScrollX();
                        ItemInfo pageView$ItemInfo0 = this.infoForCurrentScrollPosition();
                        this.setCurrentItemInternal(this.determineTargetPage(pageView$ItemInfo0.position, (((float)v2) / ((float)v1) - pageView$ItemInfo0.offset) / pageView$ItemInfo0.widthFactor, v, ((int)(motionEvent0.getX(motionEvent0.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX))), true, true, v);
                        z = this.resetTouch();
                    }
                    break;
                }
                case 2: {
                    if(this.mIsBeingDragged) {
                    label_49:
                        int v4 = this.mIsBeingDragged ? this.performDrag(motionEvent0.getX(motionEvent0.findPointerIndex(this.mActivePointerId))) : 0;
                        z = v4;
                    }
                    else {
                        int v3 = motionEvent0.findPointerIndex(this.mActivePointerId);
                        if(v3 == -1) {
                            z = this.resetTouch();
                        }
                        else {
                            float f2 = motionEvent0.getX(v3);
                            float f3 = Math.abs(f2 - this.mLastMotionX);
                            float f4 = motionEvent0.getY(v3);
                            if(f3 > ((float)this.mTouchSlop) && f3 > Math.abs(f4 - this.mLastMotionY)) {
                                this.mIsBeingDragged = true;
                                this.requestParentDisallowInterceptTouchEvent(true);
                                this.mLastMotionX = f2 - this.mInitialMotionX > 0.0f ? this.mInitialMotionX + ((float)this.mTouchSlop) : this.mInitialMotionX - ((float)this.mTouchSlop);
                                this.mLastMotionY = f4;
                                this.setScrollState(1);
                                this.setScrollingCacheEnabled(true);
                                ViewParent viewParent0 = this.getParent();
                                if(viewParent0 != null) {
                                    viewParent0.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                            goto label_49;
                        }
                    }
                    break;
                }
                case 3: {
                    if(this.mIsBeingDragged) {
                        this.scrollToItem(this.mCurItem, true, 0, false);
                        z = this.resetTouch();
                    }
                    break;
                }
                case 5: {
                    int v5 = motionEvent0.getActionIndex();
                    this.mLastMotionX = motionEvent0.getX(v5);
                    this.mActivePointerId = motionEvent0.getPointerId(v5);
                    break;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent0);
                    this.mLastMotionX = motionEvent0.getX(motionEvent0.findPointerIndex(this.mActivePointerId));
                }
            }
            if(z) {
                this.postInvalidateOnAnimation();
            }
            return true;
        }
        return false;
    }

    boolean pageLeft() {
        if(this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        }
        return false;
    }

    boolean pageRight() {
        if(this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        }
        return false;
    }

    private boolean pageScrolled(int v) {
        if(this.mItems.size() == 0) {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0f, 0);
            if(!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
            return false;
        }
        ItemInfo pageView$ItemInfo0 = this.infoForCurrentScrollPosition();
        int v1 = this.getClientWidth();
        int v2 = this.mPageMargin + v1;
        int v3 = pageView$ItemInfo0.position;
        float f = (((float)v) / ((float)v1) - pageView$ItemInfo0.offset) / (pageView$ItemInfo0.widthFactor + ((float)this.mPageMargin) / ((float)v1));
        this.mCalledSuper = false;
        this.onPageScrolled(v3, f, ((int)(((float)v2) * f)));
        if(!this.mCalledSuper) {
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        return true;
    }

    private boolean performDrag(float f) {
        float f5;
        boolean z1;
        boolean z = true;
        float f1 = this.mLastMotionX - f;
        this.mLastMotionX = f;
        float f2 = ((float)this.getScrollX()) + f1;
        int v = this.getClientWidth();
        float f3 = ((float)v) * this.mFirstOffset;
        float f4 = ((float)v) * this.mLastOffset;
        ItemInfo pageView$ItemInfo0 = (ItemInfo)this.mItems.get(0);
        ItemInfo pageView$ItemInfo1 = (ItemInfo)this.mItems.get(this.mItems.size() - 1);
        if(pageView$ItemInfo0.position == 0) {
            z1 = true;
        }
        else {
            f3 = ((float)v) * pageView$ItemInfo0.offset;
            z1 = false;
        }
        if(pageView$ItemInfo1.position == this.mAdapter.getCount() - 1) {
            f5 = f4;
        }
        else {
            f5 = pageView$ItemInfo1.offset * ((float)v);
            z = false;
        }
        if(f2 >= f3) {
            if(f2 > f5) {
                if(z) {
                    this.mRightEdge.onPull(Math.abs(f2 - f5) / ((float)v));
                }
                f3 = f5;
            }
            else {
                f3 = f2;
            }
        }
        else if(z1) {
            this.mLeftEdge.onPull(Math.abs(f3 - f2) / ((float)v));
        }
        this.mLastMotionX += f3 - ((float)(((int)f3)));
        this.scrollTo(((int)f3), this.getScrollY());
        this.pageScrolled(((int)f3));
        return false;
    }

    void populate() {
        this.populate(this.mCurItem);
    }

    void populate(int v) {
        int v12;
        ItemInfo pageView$ItemInfo1;
        int v2;
        if(this.mCurItem == v) {
            v2 = 2;
            pageView$ItemInfo1 = null;
        }
        else {
            int v1 = this.mCurItem >= v ? 17 : 66;
            ItemInfo pageView$ItemInfo0 = this.infoForPosition(this.mCurItem);
            this.mCurItem = v;
            v2 = v1;
            pageView$ItemInfo1 = pageView$ItemInfo0;
        }
        if(this.mAdapter == null) {
            this.sortChildDrawingOrder();
            return;
        }
        if(this.mPopulatePending) {
            this.sortChildDrawingOrder();
            return;
        }
        if(this.getWindowToken() != null) {
            int v3 = this.mOffscreenPageLimit;
            int v4 = Math.max(0, this.mCurItem - v3);
            int v5 = this.mAdapter.getCount();
            int v6 = Math.min(v5 - 1, v3 + this.mCurItem);
            ItemInfo pageView$ItemInfo2 = null;
            int v7;
            for(v7 = 0; v7 < this.mItems.size(); ++v7) {
                ItemInfo pageView$ItemInfo3 = (ItemInfo)this.mItems.get(v7);
                if(pageView$ItemInfo3.position >= this.mCurItem) {
                    if(pageView$ItemInfo3.position != this.mCurItem) {
                        pageView$ItemInfo3 = null;
                    }
                    pageView$ItemInfo2 = pageView$ItemInfo3;
                    break;
                }
            }
            ItemInfo pageView$ItemInfo4 = pageView$ItemInfo2 != null || v5 <= 0 ? pageView$ItemInfo2 : this.addNewItem(this.mCurItem, v7);
            if(pageView$ItemInfo4 != null) {
                float f = 0.0f;
                int v8 = v7 - 1;
                ItemInfo pageView$ItemInfo5 = v8 < 0 ? null : ((ItemInfo)this.mItems.get(v8));
                int v9 = this.getClientWidth();
                float f1 = v9 > 0 ? 2.0f - pageView$ItemInfo4.widthFactor + ((float)this.getPaddingLeft()) / ((float)v9) : 0.0f;
                int v10 = this.mCurItem - 1;
                int v11 = v7;
                while(v10 >= 0) {
                    if(f >= f1 && v10 < v4) {
                        if(pageView$ItemInfo5 == null) {
                            break;
                        }
                        if(v10 == pageView$ItemInfo5.position && !pageView$ItemInfo5.scrolling) {
                            this.mItems.remove(v8);
                            this.mAdapter.destroyItem(this, v10, pageView$ItemInfo5.object);
                            v12 = v8 - 1;
                            pageView$ItemInfo5 = v12 < 0 ? null : ((ItemInfo)this.mItems.get(v12));
                            --v11;
                            goto label_62;
                        }
                    }
                    else if(pageView$ItemInfo5 == null || v10 != pageView$ItemInfo5.position) {
                        f += this.addNewItem(v10, v8 + 1).widthFactor;
                        ++v11;
                        pageView$ItemInfo5 = v8 < 0 ? null : ((ItemInfo)this.mItems.get(v8));
                    }
                    else {
                        f += pageView$ItemInfo5.widthFactor;
                        v12 = v8 - 1;
                        pageView$ItemInfo5 = v12 >= 0 ? ((ItemInfo)this.mItems.get(v12)) : null;
                        goto label_62;
                    }
                    v12 = v8;
                label_62:
                    --v10;
                    v8 = v12;
                }
                float f2 = pageView$ItemInfo4.widthFactor;
                int v13 = v11 + 1;
                if(f2 < 2.0f) {
                    ItemInfo pageView$ItemInfo6 = v13 >= this.mItems.size() ? null : ((ItemInfo)this.mItems.get(v13));
                    float f3 = v9 > 0 ? ((float)this.getPaddingRight()) / ((float)v9) + 2.0f : 0.0f;
                    int v14 = this.mCurItem + 1;
                    ItemInfo pageView$ItemInfo7 = pageView$ItemInfo6;
                    while(v14 < v5) {
                        if(f2 >= f3 && v14 > v6) {
                            if(pageView$ItemInfo7 == null) {
                                break;
                            }
                            if(v14 == pageView$ItemInfo7.position && !pageView$ItemInfo7.scrolling) {
                                this.mItems.remove(v13);
                                this.mAdapter.destroyItem(this, v14, pageView$ItemInfo7.object);
                                pageView$ItemInfo7 = v13 < this.mItems.size() ? ((ItemInfo)this.mItems.get(v13)) : null;
                            }
                        }
                        else if(pageView$ItemInfo7 == null || v14 != pageView$ItemInfo7.position) {
                            ItemInfo pageView$ItemInfo8 = this.addNewItem(v14, v13);
                            ++v13;
                            f2 += pageView$ItemInfo8.widthFactor;
                            pageView$ItemInfo7 = v13 >= this.mItems.size() ? null : ((ItemInfo)this.mItems.get(v13));
                        }
                        else {
                            f2 += pageView$ItemInfo7.widthFactor;
                            ++v13;
                            pageView$ItemInfo7 = v13 < this.mItems.size() ? ((ItemInfo)this.mItems.get(v13)) : null;
                        }
                        ++v14;
                    }
                }
                this.calculatePageOffsets(pageView$ItemInfo4, v11, pageView$ItemInfo1);
            }
            int v15 = this.getChildCount();
            for(int v16 = 0; v16 < v15; ++v16) {
                View view0 = this.getChildAt(v16);
                LayoutParams pageView$LayoutParams0 = (LayoutParams)view0.getLayoutParams();
                pageView$LayoutParams0.childIndex = v16;
                if(!pageView$LayoutParams0.isDecor && pageView$LayoutParams0.widthFactor == 0.0f) {
                    ItemInfo pageView$ItemInfo9 = this.infoForChild(view0);
                    if(pageView$ItemInfo9 != null) {
                        pageView$LayoutParams0.widthFactor = pageView$ItemInfo9.widthFactor;
                        pageView$LayoutParams0.position = pageView$ItemInfo9.position;
                    }
                }
            }
            this.sortChildDrawingOrder();
            if(this.hasFocus()) {
                View view1 = this.findFocus();
                ItemInfo pageView$ItemInfo10 = view1 == null ? null : this.infoForAnyChild(view1);
                if(pageView$ItemInfo10 == null || pageView$ItemInfo10.position != this.mCurItem) {
                    for(int v17 = 0; v17 < this.getChildCount(); ++v17) {
                        View view2 = this.getChildAt(v17);
                        ItemInfo pageView$ItemInfo11 = this.infoForChild(view2);
                        if(pageView$ItemInfo11 != null && pageView$ItemInfo11.position == this.mCurItem && view2.requestFocus(v2)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void recomputeScrollPosition(int v, int v1, int v2, int v3) {
        if(v1 <= 0 || this.mItems.isEmpty()) {
            ItemInfo pageView$ItemInfo1 = this.infoForPosition(this.mCurItem);
            int v11 = (int)((pageView$ItemInfo1 == null ? 0.0f : Math.min(pageView$ItemInfo1.offset, this.mLastOffset)) * ((float)(v - this.getPaddingLeft() - this.getPaddingRight())));
            if(v11 != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(v11, this.getScrollY());
            }
        }
        else {
            int v4 = this.getPaddingLeft();
            int v5 = this.getPaddingRight();
            int v6 = this.getPaddingLeft();
            int v7 = this.getPaddingRight();
            int v8 = (int)(((float)(v - v4 - v5 + v2)) * (((float)this.getScrollX()) / ((float)(v1 - v6 - v7 + v3))));
            this.scrollTo(v8, this.getScrollY());
            if(!this.mScroller.isFinished()) {
                int v9 = this.mScroller.getDuration();
                int v10 = this.mScroller.timePassed();
                ItemInfo pageView$ItemInfo0 = this.infoForPosition(this.mCurItem);
                this.mScroller.startScroll(v8, 0, ((int)(pageView$ItemInfo0.offset * ((float)v))), 0, v9 - v10);
            }
        }
    }

    private void removeNonDecorViews() {
        for(int v = 0; v < this.getChildCount(); ++v) {
            if(!((LayoutParams)this.getChildAt(v).getLayoutParams()).isDecor) {
                this.removeViewAt(v);
                --v;
            }
        }
    }

    public void removeOnPageChangeListener(OnPageChangeListener pageView$OnPageChangeListener0) {
        if(this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.remove(pageView$OnPageChangeListener0);
        }
    }

    @Override  // android.view.ViewGroup
    public void removeView(View view0) {
        if(this.mInLayout) {
            this.removeViewInLayout(view0);
            return;
        }
        super.removeView(view0);
    }

    private void requestParentDisallowInterceptTouchEvent(boolean z) {
        ViewParent viewParent0 = this.getParent();
        if(viewParent0 != null) {
            viewParent0.requestDisallowInterceptTouchEvent(z);
        }
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        this.endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        return true;
    }

    private void scrollToItem(int v, boolean z, int v1, boolean z1) {
        int v2;
        ItemInfo pageView$ItemInfo0 = this.infoForPosition(v);
        if(pageView$ItemInfo0 == null) {
            v2 = 0;
        }
        else {
            float f = (float)this.getClientWidth();
            v2 = (int)(Math.max(this.mFirstOffset, Math.min(pageView$ItemInfo0.offset, this.mLastOffset)) * f);
        }
        if(z) {
            this.smoothScrollTo(v2, 0, v1);
            if(z1) {
                this.dispatchOnPageSelected(v);
            }
            return;
        }
        if(z1) {
            this.dispatchOnPageSelected(v);
        }
        this.completeScroll(false);
        this.scrollTo(v2, 0);
        this.pageScrolled(v2);
    }

    public void setAdapter(BasePageAdapter basePageAdapter0) {
        if(this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            for(int v = 0; v < this.mItems.size(); ++v) {
                ItemInfo pageView$ItemInfo0 = (ItemInfo)this.mItems.get(v);
                this.mAdapter.destroyItem(this, pageView$ItemInfo0.position, pageView$ItemInfo0.object);
            }
            this.mItems.clear();
            this.removeNonDecorViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }
        BasePageAdapter basePageAdapter1 = this.mAdapter;
        this.mAdapter = basePageAdapter0;
        this.mExpectedAdapterCount = 0;
        if(this.mAdapter != null) {
            if(this.mObserver == null) {
                this.mObserver = new PageObserver(this);
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean z = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if(this.mRestoredCurItem >= 0) {
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            }
            else if(z) {
                this.requestLayout();
            }
            else {
                this.populate();
            }
        }
        if(this.mAdapterChangeListener != null && basePageAdapter1 != basePageAdapter0) {
            this.mAdapterChangeListener.onAdapterChanged(basePageAdapter1, basePageAdapter0);
        }
    }

    void setChildrenDrawingOrderEnabledCompat(boolean z) {
        if(Build.VERSION.SDK_INT >= 7) {
            if(this.mSetChildrenDrawingOrderEnabled == null) {
                try {
                    this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
                }
                catch(NoSuchMethodException noSuchMethodException0) {
                    Log.e("PageView", "Can\'t find setChildrenDrawingOrderEnabled", noSuchMethodException0);
                }
            }
            try {
                this.mSetChildrenDrawingOrderEnabled.invoke(this, new Boolean(z));
            }
            catch(Exception exception0) {
                Log.e("PageView", "Error changing children drawing order", exception0);
            }
        }
    }

    public void setCurrentItem(int v) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(v, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int v, boolean z) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(v, z, false);
    }

    void setCurrentItemInternal(int v, boolean z, boolean z1) {
        this.setCurrentItemInternal(v, z, z1, 0);
    }

    void setCurrentItemInternal(int v, boolean z, boolean z1, int v1) {
        boolean z2 = false;
        if(this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        if(!z1 && this.mCurItem == v && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        if(v < 0) {
            v = 0;
        }
        else if(v >= this.mAdapter.getCount()) {
            v = this.mAdapter.getCount() - 1;
        }
        if(v > this.mCurItem + this.mOffscreenPageLimit || v < this.mCurItem - this.mOffscreenPageLimit) {
            for(int v2 = 0; v2 < this.mItems.size(); ++v2) {
                ((ItemInfo)this.mItems.get(v2)).scrolling = true;
            }
        }
        if(this.mCurItem != v) {
            z2 = true;
        }
        if(this.mFirstLayout) {
            this.mCurItem = v;
            if(z2) {
                this.dispatchOnPageSelected(v);
            }
            this.requestLayout();
            return;
        }
        this.populate(v);
        this.scrollToItem(v, z, v1, z2);
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener pageView$OnPageChangeListener0) {
        OnPageChangeListener pageView$OnPageChangeListener1 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = pageView$OnPageChangeListener0;
        return pageView$OnPageChangeListener1;
    }

    public void setOffscreenPageLimit(int v) {
        if(v < 1) {
            Log.w("PageView", "Requested offscreen page limit " + v + " too small; defaulting to " + 1);
            v = 1;
        }
        if(v != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = v;
            this.populate();
        }
    }

    void setOnAdapterChangeListener(OnAdapterChangeListener pageView$OnAdapterChangeListener0) {
        this.mAdapterChangeListener = pageView$OnAdapterChangeListener0;
    }

    public void setOnPageChangeListener(OnPageChangeListener pageView$OnPageChangeListener0) {
        this.mOnPageChangeListener = pageView$OnPageChangeListener0;
    }

    public void setPageMargin(int v) {
        int v1 = this.mPageMargin;
        this.mPageMargin = v;
        int v2 = this.getWidth();
        this.recomputeScrollPosition(v2, v2, v, v1);
        this.requestLayout();
    }

    public void setPageMarginDrawable(int v) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(v));
    }

    public void setPageMarginDrawable(Drawable drawable0) {
        this.mMarginDrawable = drawable0;
        if(drawable0 != null) {
            this.refreshDrawableState();
        }
        this.setWillNotDraw(drawable0 == null);
        this.invalidate();
    }

    public void setPageTransformer(boolean z, PageTransformer pageView$PageTransformer0) {
        boolean z1;
        int v = 1;
        if(Build.VERSION.SDK_INT >= 11) {
            if(pageView$PageTransformer0 != null) {
                z1 = this.mPageTransformer == null;
            }
            else if(this.mPageTransformer != null) {
                z1 = true;
            }
            else {
                z1 = false;
            }
            this.mPageTransformer = pageView$PageTransformer0;
            this.setChildrenDrawingOrderEnabledCompat(pageView$PageTransformer0 != null);
            if(pageView$PageTransformer0 == null) {
                this.mDrawingOrder = 0;
            }
            else {
                if(z) {
                    v = 2;
                }
                this.mDrawingOrder = v;
            }
            if(z1) {
                this.populate();
            }
        }
    }

    public void setScrollEnabled(boolean z) {
        this.mTouchScrollEnabled = z;
    }

    private void setScrollState(int v) {
        if(this.mScrollState == v) {
            return;
        }
        this.mScrollState = v;
        if(this.mPageTransformer != null) {
            this.enableLayers(v != 0);
        }
        this.dispatchOnScrollStateChanged(v);
    }

    private void setScrollingCacheEnabled(boolean z) {
        if(!this.mScrollingCacheEnabled) {
            if(z) {
                this.mScrollingCacheEnabled = true;
            }
        }
        else if(!z) {
            this.mScrollingCacheEnabled = false;
        }
    }

    public void setTouchEnabled(boolean z) {
        this.mTouchScrollEnabled = z;
    }

    public void showPage(int v) {
        this.setCurrentItem(v, true);
    }

    void smoothScrollTo(int v, int v1) {
        this.smoothScrollTo(v, v1, 0);
    }

    void smoothScrollTo(int v, int v1, int v2) {
        if(this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        int v3 = this.getScrollX();
        int v4 = this.getScrollY();
        int v5 = v - v3;
        int v6 = v1 - v4;
        if(v5 == 0 && v6 == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
            return;
        }
        this.setScrollingCacheEnabled(true);
        this.setScrollState(2);
        int v7 = this.getClientWidth();
        int v8 = Math.abs(v2);
        this.mScroller.startScroll(v3, v4, v5, v6, Math.min((v8 <= 0 ? ((int)((((float)Math.abs(v5)) / (((float)v7) * 1.0f + ((float)this.mPageMargin)) + 1.0f) * 100.0f)) : Math.round(1000.0f * Math.abs((((float)(v7 / 2)) * this.distanceInfluenceForSnapDuration(Math.min(1.0f, ((float)Math.abs(v5)) * 1.0f / ((float)v7))) + ((float)(v7 / 2))) / ((float)v8))) * 4), 600));
        this.postInvalidateOnAnimation();
    }

    private void sortChildDrawingOrder() {
        if(this.mDrawingOrder != 0) {
            if(this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            }
            else {
                this.mDrawingOrderedChildren.clear();
            }
            int v = this.getChildCount();
            for(int v1 = 0; v1 < v; ++v1) {
                View view0 = this.getChildAt(v1);
                this.mDrawingOrderedChildren.add(view0);
            }
            Collections.sort(this.mDrawingOrderedChildren, PageView.sPositionComparator);
        }
    }

    @Override  // android.view.View
    protected boolean verifyDrawable(Drawable drawable0) {
        return super.verifyDrawable(drawable0) || drawable0 == this.mMarginDrawable;
    }
}

