package com.veeson.easydict.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.veeson.easydict.R;
import com.veeson.easydict.adapter.CollectionWordAdapter;
import com.veeson.easydict.adapter.OnItemClickListener;
import com.veeson.easydict.adapter.expandRecyclerviewadapter.StickyRecyclerHeadersDecoration;
import com.veeson.easydict.db.DatabaseManager;
import com.veeson.easydict.model.YoudaoWord;
import com.veeson.easydict.pinyin.CharacterParser;
import com.veeson.easydict.pinyin.PinyinComparator;
import com.veeson.easydict.view.DividerDecoration;
import com.veeson.easydict.view.SideBar;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionWordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_collection_word)
    RecyclerView rvCollectionWord;
    @BindView(R.id.tv_letter_dialog)
    TextView tvLetterDialog;
    @BindView(R.id.sidebar)
    SideBar sideBar;

    private static final String TAG = "CollectionWordActivity";
    private List<YoudaoWord> list;
    private DatabaseManager databaseManager;
    private CollectionWordAdapter adapter;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_word);
        ButterKnife.bind(this);
        initView();
        databaseManager = DatabaseManager.getInstance(this);
        queryCollectionWords();
        setUI();

    }

    private void initView() {
        setToolbar();

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(tvLetterDialog);
    }

    private void setUI() {

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    rvCollectionWord.scrollToPosition(position);
                }

            }
        });
        separateLists(list);

        adapter = new CollectionWordAdapter(this, list);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CollectionWordActivity.this, SearchWordActivity.class);
                intent.putExtra("collection_word_data", list.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showDeleteOption(position);
            }
        });
        int orientation = LinearLayoutManager.VERTICAL;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, orientation, false);
        rvCollectionWord.setLayoutManager(layoutManager);

        rvCollectionWord.setAdapter(adapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        rvCollectionWord.addItemDecoration(headersDecor);
        rvCollectionWord.addItemDecoration(new DividerDecoration(this));

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
    }

    private void showDeleteOption(final int position) {
        new MaterialDialog.Builder(this)
                .items(R.array.delete_option)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                databaseManager.cancelCollectionWord(list.get(position).query);
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                                break;
                            case 1:
                                databaseManager.cancelAllCollectionWords();
                                list.clear();
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                })
                .show();
    }

    private void separateLists(List<YoudaoWord> list) {
        for (int i = 0; i < list.size(); i++) {
            String pinyin = characterParser.getSelling(list.get(i).query);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                list.get(i).sortLetters = sortString.toUpperCase();

            } else {
                list.get(i).sortLetters = "#";
            }
        }
        Collections.sort(list, pinyinComparator);
    }

    private void queryCollectionWords() {
        list = databaseManager.loadCollectionWords();
        Collections.reverse(list);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }
}
