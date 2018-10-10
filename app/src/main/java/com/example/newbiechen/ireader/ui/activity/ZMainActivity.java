package com.example.newbiechen.ireader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.presenter.MainPresenter;
import com.example.newbiechen.ireader.presenter.contract.MainContract;
import com.example.newbiechen.ireader.ui.base.BaseMVPActivity;
import com.example.newbiechen.ireader.ui.fragment.BookListFragment;
import com.example.newbiechen.ireader.utils.PermissionsChecker;
import com.example.newbiechen.ireader.utils.ToastUtils;
import com.flyco.roundview.RoundTextView;

import butterknife.BindView;

public class ZMainActivity extends BaseMVPActivity<MainContract.Presenter>
        implements MainContract.View {

    @BindView(R.id.m_fragment)
    FrameLayout m_fragment;

    @BindView(R.id.import_button)
    RoundTextView import_button;

    private boolean isPrepareFinish = false;
    private PermissionsChecker mPermissionsChecker;

    private static final int PERMISSIONS_REQUEST_STORAGE = 1;

    static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected int getContentId() {
        return R.layout.activity_zmain;
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected MainContract.Presenter bindPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initFragment();

        import_button.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){

                if (mPermissionsChecker == null){
                    mPermissionsChecker = new PermissionsChecker(this);
                }

                //获取读取和写入SD卡的权限
                if (mPermissionsChecker.lacksPermissions(PERMISSIONS)){
                    //请求权限
                    ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSIONS_REQUEST_STORAGE);
                }else{
                    Intent intent = new Intent(this, FileSystemActivity.class);
                    startActivity(intent);
                }
            }


        });
    }

    void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (null == fragmentManager.findFragmentByTag("BookListFragment")) {
            BookListFragment questionCardFragment = new BookListFragment();
            fragmentTransaction.add(R.id.m_fragment, questionCardFragment, "BookListFragment");
            fragmentTransaction.commit();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_REQUEST_STORAGE: {
                // 如果取消权限，则返回的值为0
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //跳转到 FileSystemActivity
                    Intent intent = new Intent(this, FileSystemActivity.class);
                    startActivity(intent);

                } else {
                    ToastUtils.show("用户拒绝开启读写权限");
                }
                return;
            }
        }
    }

    @Override
    protected void initClick() {
        super.initClick();
    }

    @Override
    protected void processLogic() {
        super.processLogic();
    }


    @Override
    public void onBackPressed() {
        if (!isPrepareFinish) {
            isPrepareFinish = true;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}
