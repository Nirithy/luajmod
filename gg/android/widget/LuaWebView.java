package android.widget;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DownloadManager.Request;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.ext.BaseActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import luaj.Globals;
import luaj.LuaError;
import luaj.LuaFunction;
import luaj.LuaString;
import luaj.LuaValue;
import luaj.lib.ModLib;

public class LuaWebView extends WebView {
    class Download implements DownloadListener {
        EditText file_input_field;
        private String mContentDisposition;
        private long mContentLength;
        private String mFilename;
        private String mMimetype;
        private String mUrl;
        private String mUserAgent;

        private Download() {
        }

        Download(android.widget.LuaWebView.1 luaWebView$10) {
        }

        private long download(boolean z) {
            if(LuaWebView.this.mDownloadBroadcastReceiver == null) {
                IntentFilter intentFilter0 = new IntentFilter();
                intentFilter0.addAction("android.intent.action.DOWNLOAD_COMPLETE");
                DownloadBroadcastReceiver luaWebView$DownloadBroadcastReceiver0 = new DownloadBroadcastReceiver(LuaWebView.this, null);
                LuaWebView.this.mDownloadBroadcastReceiver = luaWebView$DownloadBroadcastReceiver0;
                LuaWebView.this.mContext.registerReceiver(LuaWebView.this.mDownloadBroadcastReceiver, intentFilter0);
            }
            DownloadManager downloadManager0 = (DownloadManager)LuaWebView.this.mContext.getSystemService("download");
            Uri uri0 = Uri.parse(this.mUrl);
            uri0.getLastPathSegment();
            DownloadManager.Request downloadManager$Request0 = new DownloadManager.Request(uri0);
            downloadManager$Request0.setDestinationInExternalPublicDir("sdcard/Download", this.mFilename);
            downloadManager$Request0.setTitle(this.mFilename);
            downloadManager$Request0.setDescription(this.mUrl);
            if(z) {
                downloadManager$Request0.setAllowedNetworkTypes(2);
            }
            File file0 = new File("/sdcard/Download", this.mFilename);
            if(file0.exists()) {
                file0.delete();
            }
            downloadManager$Request0.setMimeType(this.mMimetype);
            long v = downloadManager0.enqueue(downloadManager$Request0);
            LuaWebView.this.mDownload.put(v, new String[]{new File("/sdcard/Download", this.mFilename).getAbsolutePath(), this.mMimetype});
            return v;
        }

        @Override  // android.webkit.DownloadListener
        public void onDownloadStart(String s, String s1, String s2, String s3, long v) {
            this.mUrl = s;
            this.mUserAgent = s1;
            this.mContentDisposition = s2;
            this.mMimetype = s3;
            this.mContentLength = v;
            this.mFilename = Uri.parse(s).getLastPathSegment();
            if(s2 != null) {
                int v1 = s2.indexOf("filename=\"");
                if(v1 != -1) {
                    int v2 = s2.indexOf(34, v1 + 10);
                    if(v2 > v1 + 10) {
                        this.mFilename = s2.substring(v1 + 10, v2);
                    }
                }
            }
            EditText editText0 = new EditText(LuaWebView.this.mContext);
            this.file_input_field = editText0;
            editText0.setText(this.mFilename);
            String s4 = v + "B";
            if(v > 0x100000L) {
                s4 = String.format("%.2f MB", ((double)(v.doubleValue() / 1048576.0)));
            }
            else if(v > 0x400L) {
                s4 = String.format("%.2f KB", ((double)(v.doubleValue() / 1024.0)));
            }
            new AlertDialog.Builder(LuaWebView.this.mContext).setTitle("Download").setMessage("Type: " + s3 + "\nSize: " + s4).setView(this.file_input_field).setPositiveButton("Download", new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialogInterface0, int v) {
                    String s = Download.this.file_input_field.getText().toString();
                    Download.this.mFilename = s;
                    Download.this.download(false);
                }
            }).setNegativeButton(0x1040000, null).setNeutralButton("Only Wifi", new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialogInterface0, int v) {
                    String s = Download.this.file_input_field.getText().toString();
                    Download.this.mFilename = s;
                    Download.this.download(true);
                }
            }).create().show();
        }
    }

    class DownloadBroadcastReceiver extends BroadcastReceiver {
        private DownloadBroadcastReceiver() {
        }

        DownloadBroadcastReceiver(android.widget.LuaWebView.1 luaWebView$10) {
        }

        @Override  // android.content.BroadcastReceiver
        public void onReceive(Context context0, Intent intent0) {
            long v = intent0.getLongExtra("extra_download_id", 0L);
            intent0.getExtras();
            if(LuaWebView.this.mDownload.containsKey(v) && LuaWebView.this.mOnDownloadCompleteListener != null) {
                String[] arr_s = (String[])LuaWebView.this.mDownload.get(v);
                LuaWebView.this.mOnDownloadCompleteListener.onDownloadComplete(arr_s[0], arr_s[1]);
            }
        }
    }

    public interface JsInterface {
        @JavascriptInterface
        String execute(String arg1);
    }

    class JsObject {
        private JsInterface mJs;

        public JsObject(JsInterface luaWebView$JsInterface0) {
            this.mJs = luaWebView$JsInterface0;
        }

        @JavascriptInterface
        public String execute(String s) {
            return this.mJs.execute(s);
        }
    }

    class LuaJavaScriptInterface {
        private Globals mMain;

        public LuaJavaScriptInterface(Globals globals0) {
            this.mMain = globals0;
        }

        @JavascriptInterface
        public Object callLuaFunction(String s) {
            return this.mMain.get(s).invoke();
        }

        @JavascriptInterface
        public Object callLuaFunction(String s, String s1) {
            return this.mMain.get(s).invoke(LuaValue.valueOf(s1));
        }

        @JavascriptInterface
        public Object doLuaString(String s) {
            return this.mMain.get("load").invoke(this.mMain.get(s));
        }
    }

    class LuaWebChromeClient extends WebChromeClient {
        EditText prompt_input_field;

        LuaWebChromeClient() {
            this.prompt_input_field = new EditText(luaWebView0.mContext);
        }

        @Override  // android.webkit.WebChromeClient
        public Bitmap getDefaultVideoPoster() {
            return BitmapFactory.decodeResource(LuaWebView.this.mContext.getResources(), 0x7F02002B);  // drawable:ic_fuzzy_white_24dp
        }

        @Override  // android.webkit.WebChromeClient
        public boolean onJsAlert(WebView webView0, String s, String s1, JsResult jsResult0) {
            new AlertDialog.Builder(LuaWebView.this.mContext).setTitle(s).setMessage(s1).setPositiveButton(0x104000A, new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialogInterface0, int v) {
                    jsResult0.confirm();
                }
            }).setCancelable(false).create().show();
            return true;
        }

        @Override  // android.webkit.WebChromeClient
        public boolean onJsConfirm(WebView webView0, String s, String s1, JsResult jsResult0) {
            AlertDialog.Builder alertDialog$Builder0 = new AlertDialog.Builder(LuaWebView.this.mContext);
            alertDialog$Builder0.setTitle(s);
            alertDialog$Builder0.setMessage(s1);
            alertDialog$Builder0.setPositiveButton(0x104000A, new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialogInterface0, int v) {
                    jsResult0.confirm();
                }
            });
            alertDialog$Builder0.setNegativeButton(0x1040000, new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialogInterface0, int v) {
                    jsResult0.cancel();
                }
            });
            alertDialog$Builder0.setCancelable(false);
            alertDialog$Builder0.create();
            alertDialog$Builder0.show();
            return true;
        }

        @Override  // android.webkit.WebChromeClient
        public boolean onJsPrompt(WebView webView0, String s, String s1, String s2, JsPromptResult jsPromptResult0) {
            this.prompt_input_field.setText(s2);
            AlertDialog.Builder alertDialog$Builder0 = new AlertDialog.Builder(LuaWebView.this.mContext);
            alertDialog$Builder0.setTitle(s);
            alertDialog$Builder0.setMessage(s1);
            alertDialog$Builder0.setView(this.prompt_input_field);
            alertDialog$Builder0.setPositiveButton(0x104000A, new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialogInterface0, int v) {
                    String s = LuaWebChromeClient.this.prompt_input_field.getText().toString();
                    jsPromptResult0.confirm(s);
                }
            });
            alertDialog$Builder0.setNegativeButton(0x1040000, new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialogInterface0, int v) {
                    jsPromptResult0.cancel();
                }
            });
            alertDialog$Builder0.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override  // android.content.DialogInterface$OnCancelListener
                public void onCancel(DialogInterface dialogInterface0) {
                    jsPromptResult0.cancel();
                }
            });
            alertDialog$Builder0.show();
            return true;
        }

        @Override  // android.webkit.WebChromeClient
        public void onProgressChanged(WebView webView0, int v) {
            if(v == 100) {
                LuaWebView.this.mProgressbar.setVisibility(8);
            }
            else {
                LuaWebView.this.mProgressbar.setVisibility(0);
                LuaWebView.this.mProgressbar.setProgress(v);
            }
            super.onProgressChanged(webView0, v);
        }

        @Override  // android.webkit.WebChromeClient
        public void onReceivedIcon(WebView webView0, Bitmap bitmap0) {
            super.onReceivedIcon(webView0, bitmap0);
            if(LuaWebView.this.mOnReceivedIconListener != null) {
                LuaWebView.this.mOnReceivedIconListener.onReceivedIcon(bitmap0);
            }
        }

        @Override  // android.webkit.WebChromeClient
        public void onReceivedTitle(WebView webView0, String s) {
            super.onReceivedTitle(webView0, s);
            if(LuaWebView.this.mOnReceivedTitleListener != null) {
                LuaWebView.this.mOnReceivedTitleListener.onReceivedTitle(s);
            }
        }

        public void openFileChooser(ValueCallback valueCallback0) {
            this.openFileChooser(valueCallback0, "");
        }

        public void openFileChooser(ValueCallback valueCallback0, String s) {
            if(LuaWebView.this.mUploadMessage != null) {
                return;
            }
            LuaWebView.this.mUploadMessage = valueCallback0;
            LuaWebView.this.openFile(LuaWebView.this.mDir);
        }

        public void openFileChooser(ValueCallback valueCallback0, String s, String s1) {
            this.openFileChooser(valueCallback0, s);
        }
    }

    public interface LuaWebViewClient {
        public static final int ERROR_AUTHENTICATION = -4;
        public static final int ERROR_BAD_URL = -12;
        public static final int ERROR_CONNECT = -6;
        public static final int ERROR_FAILED_SSL_HANDSHAKE = -11;
        public static final int ERROR_FILE = -13;
        public static final int ERROR_FILE_NOT_FOUND = -14;
        public static final int ERROR_HOST_LOOKUP = -2;
        public static final int ERROR_IO = -7;
        public static final int ERROR_PROXY_AUTHENTICATION = -5;
        public static final int ERROR_REDIRECT_LOOP = -9;
        public static final int ERROR_TIMEOUT = -8;
        public static final int ERROR_TOO_MANY_REQUESTS = -15;
        public static final int ERROR_UNKNOWN = -1;
        public static final int ERROR_UNSUPPORTED_AUTH_SCHEME = -3;
        public static final int ERROR_UNSUPPORTED_SCHEME = -10;

        void doUpdateVisitedHistory(WebView arg1, String arg2, boolean arg3);

        void onFormResubmission(WebView arg1, Message arg2, Message arg3);

        void onLoadResource(WebView arg1, String arg2);

        void onPageFinished(WebView arg1, String arg2);

        void onPageStarted(WebView arg1, String arg2, Bitmap arg3);

        void onProceededAfterSslError(WebView arg1, SslError arg2);

        void onReceivedClientCertRequest(WebView arg1, ClientCertRequest arg2, String arg3);

        void onReceivedError(WebView arg1, int arg2, String arg3, String arg4);

        void onReceivedHttpAuthRequest(WebView arg1, HttpAuthHandler arg2, String arg3, String arg4);

        void onReceivedLoginRequest(WebView arg1, String arg2, String arg3, String arg4);

        void onReceivedSslError(WebView arg1, SslErrorHandler arg2, SslError arg3);

        void onScaleChanged(WebView arg1, float arg2, float arg3);

        @Deprecated
        void onTooManyRedirects(WebView arg1, Message arg2, Message arg3);

        void onUnhandledKeyEvent(WebView arg1, KeyEvent arg2);

        WebResourceResponse shouldInterceptRequest(WebView arg1, String arg2);

        boolean shouldOverrideKeyEvent(WebView arg1, KeyEvent arg2);

        boolean shouldOverrideUrlLoading(WebView arg1, String arg2);
    }

    public interface OnDownloadCompleteListener {
        void onDownloadComplete(String arg1, String arg2);
    }

    public interface OnDownloadStartListener {
        void onDownloadStart(String arg1, String arg2, String arg3, String arg4, long arg5);
    }

    public interface OnReceivedIconListener {
        void onReceivedIcon(Bitmap arg1);
    }

    public interface OnReceivedTitleListener {
        void onReceivedTitle(String arg1);
    }

    class SimpleLuaWebViewClient extends WebViewClient {
        private LuaWebViewClient mLuaWebViewClient;

        public SimpleLuaWebViewClient(LuaWebViewClient luaWebView$LuaWebViewClient0) {
            this.mLuaWebViewClient = luaWebView$LuaWebViewClient0;
        }

        @Override  // android.webkit.WebViewClient
        public void doUpdateVisitedHistory(WebView webView0, String s, boolean z) {
            this.mLuaWebViewClient.doUpdateVisitedHistory(webView0, s, z);
        }

        @Override  // android.webkit.WebViewClient
        public void onFormResubmission(WebView webView0, Message message0, Message message1) {
            message0.sendToTarget();
        }

        @Override  // android.webkit.WebViewClient
        public void onLoadResource(WebView webView0, String s) {
            this.mLuaWebViewClient.onLoadResource(webView0, s);
        }

        @Override  // android.webkit.WebViewClient
        public void onPageFinished(WebView webView0, String s) {
            this.mLuaWebViewClient.onPageFinished(webView0, s);
        }

        @Override  // android.webkit.WebViewClient
        public void onPageStarted(WebView webView0, String s, Bitmap bitmap0) {
            this.mLuaWebViewClient.onPageStarted(webView0, s, bitmap0);
        }

        public void onProceededAfterSslError(WebView webView0, SslError sslError0) {
            this.mLuaWebViewClient.onProceededAfterSslError(webView0, sslError0);
        }

        public void onReceivedClientCertRequest(WebView webView0, ClientCertRequest clientCertRequest0, String s) {
            this.mLuaWebViewClient.onReceivedClientCertRequest(webView0, clientCertRequest0, s);
        }

        @Override  // android.webkit.WebViewClient
        public void onReceivedError(WebView webView0, int v, String s, String s1) {
            this.mLuaWebViewClient.onReceivedError(webView0, v, s, s1);
        }

        @Override  // android.webkit.WebViewClient
        public void onReceivedHttpAuthRequest(WebView webView0, HttpAuthHandler httpAuthHandler0, String s, String s1) {
            this.mLuaWebViewClient.onReceivedHttpAuthRequest(webView0, httpAuthHandler0, s, s1);
        }

        @Override  // android.webkit.WebViewClient
        public void onReceivedLoginRequest(WebView webView0, String s, String s1, String s2) {
            this.mLuaWebViewClient.onReceivedLoginRequest(webView0, s, s1, s2);
        }

        @Override  // android.webkit.WebViewClient
        public void onReceivedSslError(WebView webView0, SslErrorHandler sslErrorHandler0, SslError sslError0) {
            this.mLuaWebViewClient.onReceivedSslError(webView0, sslErrorHandler0, sslError0);
        }

        @Override  // android.webkit.WebViewClient
        public void onScaleChanged(WebView webView0, float f, float f1) {
            this.mLuaWebViewClient.onScaleChanged(webView0, f, f1);
        }

        @Override  // android.webkit.WebViewClient
        @Deprecated
        public void onTooManyRedirects(WebView webView0, Message message0, Message message1) {
            message0.sendToTarget();
        }

        @Override  // android.webkit.WebViewClient
        public void onUnhandledKeyEvent(WebView webView0, KeyEvent keyEvent0) {
            this.mLuaWebViewClient.onUnhandledKeyEvent(webView0, keyEvent0);
        }

        @Override  // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView webView0, WebResourceRequest webResourceRequest0) {
            return super.shouldInterceptRequest(webView0, webResourceRequest0);
        }

        @Override  // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView webView0, String s) {
            if(LuaWebView.this.mAdsFilter != null) {
                try {
                    LuaString luaString0 = LuaValue.valueOf(s);
                    if(LuaWebView.this.mAdsFilter.call(luaString0).checkboolean()) {
                        return new WebResourceResponse(null, null, null);
                    }
                }
                catch(LuaError luaError0) {
                    luaError0.printStackTrace();
                }
            }
            return this.mLuaWebViewClient.shouldInterceptRequest(webView0, s);
        }

        @Override  // android.webkit.WebViewClient
        public boolean shouldOverrideKeyEvent(WebView webView0, KeyEvent keyEvent0) {
            return this.mLuaWebViewClient.shouldOverrideKeyEvent(webView0, keyEvent0);
        }

        @Override  // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView0, String s) {
            return this.mLuaWebViewClient.shouldOverrideUrlLoading(webView0, s);
        }
    }

    private static final String DOWNLOAD = "Download";
    private DisplayMetrics dm;
    private Globals globals;
    private LuaFunction mAdsFilter;
    private Context mContext;
    private String mDir;
    private HashMap mDownload;
    private DownloadBroadcastReceiver mDownloadBroadcastReceiver;
    private OnDownloadCompleteListener mOnDownloadCompleteListener;
    private OnReceivedIconListener mOnReceivedIconListener;
    private OnReceivedTitleListener mOnReceivedTitleListener;
    private ProgressBar mProgressbar;
    private ValueCallback mUploadMessage;
    private LuaFunction mfinished;
    private Dialog open_dlg;
    private ListView open_list;

    public LuaWebView(Context context0) {
        super(context0);
        this.mDownload = new HashMap();
        this.mDir = "/";
        this.mContext = context0;
        this.globals = ModLib._G;
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setDisplayZoomControls(true);
        this.getSettings().setSupportZoom(true);
        this.getSettings().setDomStorageEnabled(true);
        if(Build.VERSION.SDK_INT >= 21) {
            this.getSettings().setMixedContentMode(0);
        }
        this.addJavascriptInterface(new LuaJavaScriptInterface(this, this.globals), "androlua");
        this.setWebViewClient(new WebViewClient() {
            @Override  // android.webkit.WebViewClient
            public void onPageFinished(WebView webView0, String s) {
                if(LuaWebView.this.mfinished != null) {
                    try {
                        LuaWebView.this.mfinished.call(s);
                    }
                    catch(LuaError luaError0) {
                        luaError0.printStackTrace();
                    }
                }
                super.onPageFinished(webView0, s);
            }

            @Override  // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView0, SslErrorHandler sslErrorHandler0, SslError sslError0) {
                AlertDialog.Builder alertDialog$Builder0 = new AlertDialog.Builder(LuaWebView.this.mContext);
                alertDialog$Builder0.setTitle("SslError");
                alertDialog$Builder0.setMessage(sslError0.toString());
                alertDialog$Builder0.setPositiveButton(0x104000A, new DialogInterface.OnClickListener() {
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialogInterface0, int v) {
                        sslErrorHandler0.proceed();
                    }
                });
                alertDialog$Builder0.setNegativeButton(0x1040000, new DialogInterface.OnClickListener() {
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialogInterface0, int v) {
                        sslErrorHandler0.cancel();
                    }
                });
                alertDialog$Builder0.setCancelable(false);
                alertDialog$Builder0.create();
                alertDialog$Builder0.show();
            }

            @Override  // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView0, String s) {
                if(LuaWebView.this.mAdsFilter != null) {
                    try {
                        Boolean boolean0 = Boolean.valueOf(LuaWebView.this.mAdsFilter.call(LuaValue.valueOf(s)).checkboolean());
                        if(boolean0 != null && boolean0.booleanValue()) {
                            return new WebResourceResponse(null, null, null);
                        }
                    }
                    catch(LuaError luaError0) {
                        luaError0.printStackTrace();
                    }
                }
                return null;
            }

            @Override  // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView0, String s) {
                if(LuaWebView.this.mAdsFilter != null) {
                    try {
                        Boolean boolean0 = Boolean.valueOf(LuaWebView.this.mAdsFilter.call(LuaValue.valueOf(s)).checkboolean());
                        if(boolean0 != null && boolean0.booleanValue()) {
                            return true;
                        }
                    }
                    catch(LuaError luaError0) {
                        luaError0.printStackTrace();
                    }
                }
                if(!s.startsWith("http") && !s.startsWith("file")) {
                    try {
                        BaseActivity.instance.startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(s)), 0);
                    }
                    catch(Exception unused_ex) {
                    }
                    return true;
                }
                webView0.loadUrl(s);
                return true;
            }
        });
        DisplayMetrics displayMetrics0 = context0.getResources().getDisplayMetrics();
        this.dm = displayMetrics0;
        int v = (int)TypedValue.applyDimension(1, 2.0f, displayMetrics0);
        ProgressBar progressBar0 = new ProgressBar(context0, null, 0x1010078);
        this.mProgressbar = progressBar0;
        progressBar0.setLayoutParams(new AbsoluteLayout.LayoutParams(-1, v, 0, 0));
        this.addView(this.mProgressbar);
        this.setWebChromeClient(new LuaWebChromeClient(this));
        this.setDownloadListener(new Download(this, null));
    }

    public void addJSInterface(JsInterface luaWebView$JsInterface0, String s) {
        super.addJavascriptInterface(new JsObject(this, luaWebView$JsInterface0), s);
    }

    public void addJsInterface(JsInterface luaWebView$JsInterface0, String s) {
        super.addJavascriptInterface(new JsObject(this, luaWebView$JsInterface0), s);
    }

    @Override  // android.webkit.WebView
    public void destroy() {
        DownloadBroadcastReceiver luaWebView$DownloadBroadcastReceiver0 = this.mDownloadBroadcastReceiver;
        if(luaWebView$DownloadBroadcastReceiver0 != null) {
            this.mContext.unregisterReceiver(luaWebView$DownloadBroadcastReceiver0);
        }
        super.destroy();
    }

    @Override  // android.webkit.WebView
    public boolean onKeyDown(int v, KeyEvent keyEvent0) {
        if(v == 4 && this.canGoBack()) {
            this.goBack();
            return true;
        }
        return super.onKeyDown(v, keyEvent0);
    }

    @Override  // android.webkit.WebView
    protected void onScrollChanged(int v, int v1, int v2, int v3) {
        AbsoluteLayout.LayoutParams absoluteLayout$LayoutParams0 = (AbsoluteLayout.LayoutParams)this.mProgressbar.getLayoutParams();
        absoluteLayout$LayoutParams0.x = v;
        absoluteLayout$LayoutParams0.y = v1;
        this.mProgressbar.setLayoutParams(absoluteLayout$LayoutParams0);
        super.onScrollChanged(v, v1, v2, v3);
    }

    public void openFile(String s) {
        if(this.open_dlg == null) {
            this.open_dlg = new Dialog(this.getContext());
            ListView listView0 = new ListView(this.getContext());
            this.open_list = listView0;
            listView0.setFastScrollEnabled(true);
            this.open_list.setFastScrollAlwaysVisible(true);
            this.open_dlg.setContentView(this.open_list);
            this.open_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override  // android.widget.AdapterView$OnItemClickListener
                public void onItemClick(AdapterView adapterView0, View view0, int v, long v1) {
                    String s = ((TextView)view0).getText().toString();
                    if(s.equals("../")) {
                        String s1 = new File(LuaWebView.this.mDir).getParent() + "/";
                        LuaWebView.this.mDir = s1;
                        LuaWebView.this.openFile(LuaWebView.this.mDir);
                        return;
                    }
                    String s2 = LuaWebView.this.mDir + s;
                    if(new File(s2).isDirectory()) {
                        LuaWebView.this.mDir = s2;
                        LuaWebView.this.openFile(LuaWebView.this.mDir);
                        return;
                    }
                    LuaWebView.this.mUploadMessage.onReceiveValue(Uri.parse(s2));
                }
            });
        }
        File file0 = new File(s);
        ArrayList arrayList0 = new ArrayList();
        arrayList0.add("../");
        String[] arr_s = file0.list();
        if(arr_s != null) {
            Arrays.sort(arr_s);
            for(int v1 = 0; v1 < arr_s.length; ++v1) {
                String s1 = arr_s[v1];
                if(new File(this.mDir + s1).isDirectory()) {
                    arrayList0.add(s1 + "/");
                }
            }
            for(int v = 0; v < arr_s.length; ++v) {
                String s2 = arr_s[v];
                if(new File(this.mDir + s2).isFile()) {
                    arrayList0.add(s2);
                }
            }
        }
        ArrayAdapter arrayAdapter0 = new ArrayAdapter(this.getContext(), 0x1090003, arrayList0);
        this.open_list.setAdapter(arrayAdapter0);
        this.open_dlg.setTitle(this.mDir);
        this.open_dlg.show();
    }

    public void setAdsFilter(LuaFunction luaFunction0) {
        this.mAdsFilter = luaFunction0;
    }

    @Override  // android.webkit.WebView
    public void setDownloadListener(DownloadListener downloadListener0) {
        super.setDownloadListener(downloadListener0);
    }

    public void setFinished(LuaFunction luaFunction0) {
        this.mfinished = luaFunction0;
    }

    public void setOnDownloadCompleteListener(OnDownloadCompleteListener luaWebView$OnDownloadCompleteListener0) {
        this.mOnDownloadCompleteListener = luaWebView$OnDownloadCompleteListener0;
    }

    public void setOnDownloadStartListener(OnDownloadStartListener luaWebView$OnDownloadStartListener0) {
        this.setDownloadListener(new DownloadListener() {
            @Override  // android.webkit.DownloadListener
            public void onDownloadStart(String s, String s1, String s2, String s3, long v) {
                luaWebView$OnDownloadStartListener0.onDownloadStart(s, s1, s2, s3, v);
            }
        });
    }

    @Override  // android.view.View
    public void setOnKeyListener(View.OnKeyListener view$OnKeyListener0) {
        super.setOnKeyListener(view$OnKeyListener0);
    }

    public void setOnReceivedIconListener(OnReceivedIconListener luaWebView$OnReceivedIconListener0) {
        this.mOnReceivedIconListener = luaWebView$OnReceivedIconListener0;
    }

    public void setOnReceivedTitleListener(OnReceivedTitleListener luaWebView$OnReceivedTitleListener0) {
        this.mOnReceivedTitleListener = luaWebView$OnReceivedTitleListener0;
    }

    public void setProgressBar(ProgressBar progressBar0) {
        this.mProgressbar = progressBar0;
    }

    public void setProgressBarEnabled(boolean z) {
        if(z) {
            this.mProgressbar.setVisibility(0);
            return;
        }
        this.mProgressbar.setVisibility(8);
    }

    public void setWebViewClient(LuaWebViewClient luaWebView$LuaWebViewClient0) {
        super.setWebViewClient(new SimpleLuaWebViewClient(this, luaWebView$LuaWebViewClient0));
    }
}

