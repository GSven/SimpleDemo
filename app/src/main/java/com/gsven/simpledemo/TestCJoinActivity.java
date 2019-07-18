package com.gsven.simpledemo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.base.BaseActivity;
import com.gsven.library.utils.KLog;
import com.gsven.simpledemo.network.Network;

import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

/**
 * @author Gsven
 * @Date 2019/5/20 14:53
 * @Desc 测试C接入调用
 */
@Route(path = ARouterConstant.ACTIVITY_APP_ACTIVITY_TESTCJOIN)
public class TestCJoinActivity extends BaseActivity {
    @BindView(R.id.tvTestC)
    TextView tvTestC;
    @BindView(R.id.ivTestImage)
    ImageView ivTestImage;

    @Override
    public int getContentView() {
        return R.layout.activity_test_cjoin;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.tvTestC})
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.tvTestC:
                test();
                break;
        }
    }

    public static final String COMPANY_ID = "THINKIVE";
    public static final String SYSTEM_ID_LOGIN = "BASECIF";//统一账户
    public static final String SYSTEM_ID_ZHYW = "YDSZHYW";//综合业务
    public static final String SYSTEM_ID_MALL = "LCSMALL";//理财商城
    public static final String SYSTEM_ID_YGT = "FXCISMP";//云柜台
    public static final String SYSTEM_ID_RECONNECT = "SITESELF";//
    public static final String APP_ID = "app_id";
    public static final String CORP_ID = "corp_id";
    private void test() {
        RequestBody body = new FormBody.Builder()
                .add(APP_ID,"102000000001")
                .add(CORP_ID,"101000000001")
                .build();

        Request request = new Request.Builder()
                .url("https://app.lczq.com:7093")
                .addHeader("companyId",COMPANY_ID)
                .addHeader("systemId",SYSTEM_ID_MALL)
                .build();


         WebSocket socketClient= okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                KLog.e("onOpen");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                KLog.e("onMessage");
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                KLog.e("onMessage");
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                KLog.e("onClosing");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                KLog.e("onClosed");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                KLog.e("onFailure");
            }
        });
        socketClient.send("Hello");
    }

    private HttpLoggingInterceptor httpLoggingInterceptor;
    private OkHttpClient okHttpClient;

    @Override
    protected void initData() {
        httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    String text = URLDecoder.decode(message, "utf-8");
                    KLog.e(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

    }

    @Override
    protected void setListeners() {

    }
}
