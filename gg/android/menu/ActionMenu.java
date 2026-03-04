package android.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.ArrayList;
import java.util.List;

public class ActionMenu implements Menu {
    protected Context mContext;
    private boolean mIsQwerty;
    private ArrayList mItems;
    protected Resources mResources;

    public ActionMenu(Context context) {
        this.mContext = context;
        this.mItems = new ArrayList();
        this.mResources = this.mContext.getResources();
    }

    @Override  // android.view.Menu
    public MenuItem add(int titleRes) {
        return this.add(0, 0, 0, titleRes);
    }

    @Override  // android.view.Menu
    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return this.add(groupId, itemId, order, this.mResources.getString(titleRes));
    }

    @Override  // android.view.Menu
    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        MenuItem menuItem0 = new ActionMenuItem(this.getContext(), groupId, itemId, 0, order, title);
        this.mItems.add(order, menuItem0);
        return menuItem0;
    }

    @Override  // android.view.Menu
    public MenuItem add(CharSequence title) {
        return this.add(0, 0, 0, title);
    }

    @Override  // android.view.Menu
    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        PackageManager packageManager0 = this.mContext.getPackageManager();
        List list0 = packageManager0.queryIntentActivityOptions(caller, specifics, intent, 0);
        int v4 = list0 == null ? 0 : list0.size();
        if((flags & 1) == 0) {
            this.removeGroup(groupId);
        }
        for(int i = 0; i < v4; ++i) {
            ResolveInfo ri = (ResolveInfo)list0.get(i);
            Intent intent1 = new Intent((ri.specificIndex >= 0 ? specifics[ri.specificIndex] : intent));
            intent1.setComponent(new ComponentName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.name));
            MenuItem menuItem0 = this.add(groupId, itemId, order, ri.loadLabel(packageManager0)).setIcon(ri.loadIcon(packageManager0)).setIntent(intent1);
            if(outSpecificItems != null && ri.specificIndex >= 0) {
                outSpecificItems[ri.specificIndex] = menuItem0;
            }
        }
        return v4;
    }

    @Override  // android.view.Menu
    public SubMenu addSubMenu(int titleRes) {
        return this.addSubMenu(0, 0, 0, this.mResources.getString(titleRes));
    }

    @Override  // android.view.Menu
    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return this.addSubMenu(groupId, itemId, order, this.mResources.getString(titleRes));
    }

    @Override  // android.view.Menu
    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        MenuItem menuItem0 = this.add(groupId, itemId, order, title);
        return new SubMenuImpl(this.mContext, this, menuItem0);
    }

    @Override  // android.view.Menu
    public SubMenu addSubMenu(CharSequence title) {
        return this.addSubMenu(0, 0, 0, title);
    }

    @Override  // android.view.Menu
    public void clear() {
        this.mItems.clear();
        this.mItems.trimToSize();
    }

    @Override  // android.view.Menu
    public void close() {
    }

    @Override  // android.view.Menu
    public MenuItem findItem(int id) {
        int v1 = this.findItemIndex(id);
        return v1 < 0 ? null : ((ActionMenuItem)this.mItems.get(v1));
    }

    private int findItemIndex(int id) {
        ArrayList items = this.mItems;
        int v1 = items.size();
        int i;
        for(i = 0; true; ++i) {
            if(i >= v1) {
                return -1;
            }
            if(((ActionMenuItem)items.get(i)).getItemId() == id) {
                break;
            }
        }
        return i;
    }

    private ActionMenuItem findItemWithShortcut(int keyCode, KeyEvent event) {
        ActionMenuItem item;
        boolean qwerty = this.mIsQwerty;
        ArrayList items = this.mItems;
        int v1 = items.size();
        for(int i = 0; true; ++i) {
            if(i >= v1) {
                return null;
            }
            item = (ActionMenuItem)items.get(i);
            if(keyCode == (qwerty ? item.getAlphabeticShortcut() : item.getNumericShortcut())) {
                break;
            }
        }
        return item;
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override  // android.view.Menu
    public MenuItem getItem(int index) {
        return (MenuItem)this.mItems.get(index);
    }

    @Override  // android.view.Menu
    public boolean hasVisibleItems() {
        ArrayList items = this.mItems;
        int v = items.size();
        for(int i = 0; true; ++i) {
            if(i >= v) {
                return false;
            }
            if(((ActionMenuItem)items.get(i)).isVisible()) {
                return true;
            }
        }
    }

    @Override  // android.view.Menu
    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return this.findItemWithShortcut(keyCode, event) != null;
    }

    @Override  // android.view.Menu
    public boolean performIdentifierAction(int id, int flags) {
        int v2 = this.findItemIndex(id);
        return v2 >= 0 ? ((ActionMenuItem)this.mItems.get(v2)).invoke() : false;
    }

    @Override  // android.view.Menu
    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        ActionMenuItem actionMenuItem0 = this.findItemWithShortcut(keyCode, event);
        return actionMenuItem0 == null ? false : actionMenuItem0.invoke();
    }

    @Override  // android.view.Menu
    public void removeGroup(int groupId) {
        ArrayList items = this.mItems;
        int itemCount = items.size();
        int i = 0;
        while(i < itemCount) {
            if(((ActionMenuItem)items.get(i)).getGroupId() == groupId) {
                items.remove(i);
                --itemCount;
            }
            else {
                ++i;
            }
        }
    }

    @Override  // android.view.Menu
    public void removeItem(int id) {
        this.mItems.remove(this.findItemIndex(id));
    }

    @Override  // android.view.Menu
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        ArrayList items = this.mItems;
        int v1 = items.size();
        for(int i = 0; i < v1; ++i) {
            ActionMenuItem item = (ActionMenuItem)items.get(i);
            if(item.getGroupId() == group) {
                item.setCheckable(checkable);
                item.setExclusiveCheckable(exclusive);
            }
        }
    }

    @Override  // android.view.Menu
    public void setGroupEnabled(int group, boolean enabled) {
        ArrayList items = this.mItems;
        int v1 = items.size();
        for(int i = 0; i < v1; ++i) {
            ActionMenuItem item = (ActionMenuItem)items.get(i);
            if(item.getGroupId() == group) {
                item.setEnabled(enabled);
            }
        }
    }

    @Override  // android.view.Menu
    public void setGroupVisible(int group, boolean visible) {
        ArrayList items = this.mItems;
        int v1 = items.size();
        for(int i = 0; i < v1; ++i) {
            ActionMenuItem item = (ActionMenuItem)items.get(i);
            if(item.getGroupId() == group) {
                item.setVisible(visible);
            }
        }
    }

    @Override  // android.view.Menu
    public void setQwertyMode(boolean isQwerty) {
        this.mIsQwerty = isQwerty;
    }

    @Override  // android.view.Menu
    public int size() {
        return this.mItems.size();
    }
}

