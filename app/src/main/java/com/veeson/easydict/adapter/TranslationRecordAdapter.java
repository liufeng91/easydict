package com.veeson.easydict.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.veeson.easydict.R;
import com.veeson.easydict.model.TranslationRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wilson on 2016/6/22.
 */
public class TranslationRecordAdapter extends RecyclerView.Adapter<TranslationRecordAdapter.ViewHolder> {
    private Context context;
    private List<TranslationRecord> list;
    private OnItemClickListener mOnItemClickListener;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public TranslationRecordAdapter(Context context, List<TranslationRecord> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trans_record_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvOriginal.setText(list.get(position).original);
        holder.tvDate.setText(list.get(position).date + "   " + list.get(position).fromTo);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, pos);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                return false;
            }
        });
        setAnimation(holder.cardView, position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.tv_original)
        TextView tvOriginal;
        @BindView(R.id.tv_date)
        TextView tvDate;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvOriginal.setEllipsize(TextUtils.TruncateAt.END);
            tvOriginal.setMaxLines(2);
        }
    }
}
