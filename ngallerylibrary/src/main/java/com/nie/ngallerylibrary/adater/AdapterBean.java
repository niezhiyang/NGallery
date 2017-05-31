package com.nie.ngallerylibrary.adater;

import android.os.Build;
import android.util.SparseArray;
import android.view.View;


public class AdapterBean {
    private View[] activeViews = new View[0];
    private int[] activeViewTypes = new int[0];

    private SparseArray<View>[] scrapViews;

    private int viewTypeCount;

    private SparseArray<View> currentScrapViews;

    public void setViewTypeCount(int viewTypeCount) {
        if (viewTypeCount < 1) {
            return;
        }
        //noinspection unchecked
        SparseArray<View>[] scrapViews = new SparseArray[viewTypeCount];
        for (int i = 0; i < viewTypeCount; i++) {
            scrapViews[i] = new SparseArray<View>();
        }
        this.viewTypeCount = viewTypeCount;
        currentScrapViews = scrapViews[0];
        this.scrapViews = scrapViews;
    }

    protected boolean shouldRecycleViewType(int viewType) {
        return viewType >= 0;
    }

    View getScrapView(int position, int viewType) {
        if (viewTypeCount == 1) {
            return retrieveFromScrap(currentScrapViews, position);
        } else if (viewType >= 0 && viewType < scrapViews.length) {
            return retrieveFromScrap(scrapViews[viewType], position);
        }
        return null;
    }

    void addScrapView(View scrap, int position, int viewType) {
        if (viewTypeCount == 1) {
            currentScrapViews.put(position, scrap);
        } else {
            scrapViews[viewType].put(position, scrap);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            scrap.setAccessibilityDelegate(null);
        }
    }

    void scrapActiveViews() {
        final View[] activeViews = this.activeViews;
        final int[] activeViewTypes = this.activeViewTypes;
        final boolean multipleScraps = viewTypeCount > 1;

        SparseArray<View> scrapViews = currentScrapViews;
        final int count = activeViews.length;
        for (int i = count - 1; i >= 0; i--) {
            final View victim = activeViews[i];
            if (victim != null) {
                int whichScrap = activeViewTypes[i];

                activeViews[i] = null;
                activeViewTypes[i] = -1;

                if (!shouldRecycleViewType(whichScrap)) {
                    continue;
                }

                if (multipleScraps) {
                    scrapViews = this.scrapViews[whichScrap];
                }
                scrapViews.put(i, victim);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    victim.setAccessibilityDelegate(null);
                }
            }
        }

        pruneScrapViews();
    }

    /**
     * Makes sure that the size of scrapViews does not exceed the size of activeViews.
     * (This can happen if an adapter does not recycle its views).
     */
    private void pruneScrapViews() {
        final int maxViews = activeViews.length;
        final int viewTypeCount = this.viewTypeCount;
        final SparseArray<View>[] scrapViews = this.scrapViews;
        for (int i = 0; i < viewTypeCount; ++i) {
            final SparseArray<View> scrapPile = scrapViews[i];
            int size = scrapPile.size();
            final int extras = size - maxViews;
            size--;
            for (int j = 0; j < extras; j++) {
                scrapPile.remove(scrapPile.keyAt(size--));
            }
        }
    }

    static View retrieveFromScrap(SparseArray<View> scrapViews, int position) {
        int size = scrapViews.size();
        if (size > 0) {
            // See if we still have a view for this position.
            for (int i = 0; i < size; i++) {
                int fromPosition = scrapViews.keyAt(i);
                View view = scrapViews.get(fromPosition);
                if (fromPosition == position) {
                    scrapViews.remove(fromPosition);
                    return view;
                }
            }
            int index = size - 1;
            View r = scrapViews.valueAt(index);
            scrapViews.remove(scrapViews.keyAt(index));
            return r;
        } else {
            return null;
        }
    }
}
