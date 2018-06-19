package android.sgz.com.fragment;

import android.os.Bundle;
import android.sgz.com.R;
import android.sgz.com.base.BaseFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WD on 2018/6/19.
 */

public class ApproveExtroWorkFragment extends BaseFragment {
    @Override
    public View onCustomCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_approve_extra_work, null);
        }
        //缓存的rootView需要判断是否已经被加过parent,如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }
}
