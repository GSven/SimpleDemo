package com.gsven.simpledemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ARouterConstant.ACTIVITY_APP_ACTIVITY_PDF)
public class PDFActivity extends BaseTitleActivity {

    @BindView(R.id.tvPrintHelper)
    TextView tvPrintHelper;
    @BindView(R.id.tvGetPdf)
    TextView tvGetPdf;
    @BindView(R.id.webView)
    WebView webView;

    private final String urlTest = "https://www.jianshu.com/p/2e32db0e63e9";

    @Override
    public int getContentView() {
        return R.layout.activity_pdf;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(urlTest);
    }

    @OnClick({
            R.id.tvGetPdf,
            R.id.tvPrintHelper
    })
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.tvGetPdf://生成pdf
                genPdfFile();
                break;
            case R.id.tvPrintHelper://连接打印机打印
                printPdf();
                break;
        }
    }

    private void genPdfFile() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            printAdapter = webView.createPrintDocumentAdapter("my.pdf");
        } else {
            printAdapter = webView.createPrintDocumentAdapter();
        }

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE,
                        200, 200))
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        printManager.print(jobName, printAdapter, attributes);
    }

    private void printPdf() {
        PrintHelper mPrintHelper = new PrintHelper(this);
        mPrintHelper.setColorMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        mPrintHelper.printBitmap("print-bitmap", mBitmap);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListeners() {

    }
}
