package android.sgz.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.sgz.com.R;
import android.sgz.com.bean.AddOrderContactsBean;
import android.sgz.com.utils.Bimp;
import android.sgz.com.widget.IRecycleOnLongItemClickListener;
import android.sgz.com.widget.IRecycleViewOnItemClickListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

/**
 * Created by 92457 on 2018/5/25.
 */

public class ReleaseOrderAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AddOrderContactsBean> mList;
    private LayoutInflater inflater;

    public ReleaseOrderAdapter(Context context, List<AddOrderContactsBean> list) {
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_release_order, parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        String realName = mList.get(position).getRealName();
        viewHolder.tvName.setText( "" + realName);
        View itemView =((AutoLinearLayout) holder.itemView).getChildAt(0);
        if (mOnItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position);
                }

            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onLongItemClick(viewHolder.itemView, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private IRecycleViewOnItemClickListener mOnItemClickListener;//声明接口
    public void setOnItemClickListener(IRecycleViewOnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
