package com.veeson.easydict.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeson.easydict.R;
import com.veeson.easydict.adapter.expandRecyclerviewadapter.StickyRecyclerHeadersAdapter;
import com.veeson.easydict.common.capsulation.PlayAudioManager;
import com.veeson.easydict.common.utils.StringUtils;
import com.veeson.easydict.model.YoudaoWord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wilson on 2016/6/23.
 */
public class CollectionWordAdapter extends RecyclerView.Adapter<CollectionWordAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private List<YoudaoWord> list;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public CollectionWordAdapter(Context context, List<YoudaoWord> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = list.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_word_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvCollectionWord.setText(list.get(position).query + "  [" + list.get(position).basic.phonetic + "]");
        StringBuilder sb = new StringBuilder();
        for (String s :
                list.get(position).basic.explains) {
            sb.append(s).append(" ");
        }
        holder.tvCollectionWordParaphrase.setText(sb);
        holder.ivCollectionHorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = list.get(holder.getLayoutPosition()).query;
                if (StringUtils.isEnglish(word)) {
                    PlayAudioManager.playENPronVoice(context, word, "1");
                } else {
                    PlayAudioManager.playCNPronVoice(context, word);
                }
            }
        });
        holder.lyCollectionItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, pos);
            }
        });
        holder.lyCollectionItems.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                return false;
            }
        });
    }

    @Override
    public long getHeaderId(int position) {
        return list.get(position).sortLetters.charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.letter_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        String showValue = String.valueOf(list.get(position).sortLetters.charAt(0));
        textView.setText(showValue);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_collection_word)
        TextView tvCollectionWord;
        @BindView(R.id.tv_collection_word_paraphrase)
        TextView tvCollectionWordParaphrase;
        @BindView(R.id.iv_collection_horn)
        ImageView ivCollectionHorn;
        @BindView(R.id.ly_collection_items)
        LinearLayout lyCollectionItems;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
