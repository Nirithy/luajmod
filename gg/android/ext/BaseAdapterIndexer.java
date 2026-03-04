package android.ext;

import android.widget.SectionIndexer;

public abstract class BaseAdapterIndexer extends BaseAdapterLC implements SectionIndexer {
    public static final int SECTIONS_COUNT = 20;

    @Override  // android.widget.SectionIndexer
    public int getPositionForSection(int sectionIndex) {
        int v1 = this.getCount();
        int ret = ((int)(((double)sectionIndex) * (((double)v1) / 20.0))) >= 0 ? ((int)(((double)sectionIndex) * (((double)v1) / 20.0))) : 0;
        return ret < v1 ? ret : v1 - 1;
    }

    @Override  // android.widget.SectionIndexer
    public int getSectionForPosition(int position) {
        int ret = (int)(((double)position) / (((double)this.getCount()) / 20.0));
        if(ret < 0) {
            ret = 0;
        }
        return ret < 20 ? ret : 19;
    }

    @Override  // android.widget.SectionIndexer
    public Object[] getSections() {
        Object[] ret = new Object[20];
        double sectionSize = ((double)this.getCount()) / 20.0;
        for(int i = 0; i < 20; ++i) {
            ret[i] = (int)(((double)i) * sectionSize);
        }
        return ret;
    }
}

