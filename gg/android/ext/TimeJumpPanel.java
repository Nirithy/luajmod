package android.ext;

import android.content.Context;
import android.fix.LayoutInflater;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;

public class TimeJumpPanel extends FloatPanel implements View.OnClickListener {
    Button jumpAsk;
    Button jumpLast;

    public TimeJumpPanel(Context context) {
        super(context);
    }

    public TimeJumpPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeJumpPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeJumpPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private static long getLast() {
        return (long)(((double)Config.timeJumpLast) / 1000.0 * 1000000000.0);
    }

    @Override  // android.ext.FloatPanel
    protected String getPrefName() {
        return "time-jump";
    }

    private void init() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TimeJumpPanel.this.jumpLast = (Button)TimeJumpPanel.this.findViewById(0x7F0B0146);  // id:time_jump_last
                TimeJumpPanel.this.jumpAsk = (Button)TimeJumpPanel.this.findViewById(0x7F0B0145);  // id:time_jump
                TimeJumpPanel.this.jumpLast.setOnClickListener(TimeJumpPanel.this);
                TimeJumpPanel.this.jumpAsk.setOnClickListener(TimeJumpPanel.this);
                TimeJumpPanel.this.setLast();
                HotPoint hotPoint = MainService.instance.hotPoint;
                float f = hotPoint.getLayoutAlpha();
                TimeJumpPanel.this.setLayoutAlpha(f);
                int v = hotPoint.getSizePx();
                TimeJumpPanel.this.setMinSize(v);
            }
        });
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        if(v != null) {
            switch(v.getId()) {
                case 0x7F0B0145: {  // id:time_jump
                    TimeJump tj = TimeJump.lastInstance;
                    if(tj != null) {
                        tj.onClick(null);
                        return;
                    }
                    break;
                }
                case 0x7F0B0146: {  // id:time_jump_last
                    long v = TimeJumpPanel.getLast();
                    TimeJump.makeJump(v);
                    Tools.showToast((Tools.stripColon(0x7F07014A) + ": " + Tools.doubleToTime(((double)v) / 1000000000.0) + " (" + Double.toString(((double)v) / 1000000000.0).replaceFirst("\\D?0+$", "") + ')'), 0);  // string:time_jump "Time jump"
                    return;
                }
                default: {
                    Log.d(("Unknown id: " + Integer.toHexString(v.getId())), new RuntimeException());
                }
            }
        }
    }

    public void setLast() {
        long v = TimeJumpPanel.getLast();
        this.jumpLast.setVisibility((v == 0L ? 8 : 0));
        this.jumpLast.setText(Tools.doubleToTime(((double)v) / 1000000000.0));
    }

    public void setMinSize(int px) {
        if(this.jumpLast == null) {
            return;
        }
        this.jumpLast.setMinWidth(px);
        this.jumpLast.setMinHeight(px);
        this.jumpAsk.setMinWidth(px);
        this.jumpAsk.setMinHeight(px);
    }

    public static void toggle(boolean show) {
        TimeJumpPanel panel = MainService.instance.timeJumpPanel;
        boolean enabled = (Config.configClient & 0x80) != 0;
        if(show && enabled) {
            if(panel == null) {
                panel = (TimeJumpPanel)LayoutInflater.inflateStatic(0x7F04002A, null);  // layout:time_jump_panel
                panel.init();
                MainService.instance.timeJumpPanel = panel;
            }
            panel.show();
            return;
        }
        if(panel != null) {
            panel.hide();
            if(!enabled) {
                MainService.instance.timeJumpPanel = null;
            }
        }
    }
}

