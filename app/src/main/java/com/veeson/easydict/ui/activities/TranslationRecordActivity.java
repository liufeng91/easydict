package com.veeson.easydict.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.veeson.easydict.R;
import com.veeson.easydict.adapter.OnItemClickListener;
import com.veeson.easydict.adapter.TranslationRecordAdapter;
import com.veeson.easydict.db.DatabaseManager;
import com.veeson.easydict.model.TranslationRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TranslationRecordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_trans_record)
    RecyclerView rvTransRecord;

    private TranslationRecordAdapter adapter;
    private List<TranslationRecord> list;
    private DatabaseManager databaseManager;
    private List<TranslationRecord> recordToDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_record);
        ButterKnife.bind(this);

        setToolbar();

        databaseManager = DatabaseManager.getInstance(this);
        queryTranslationRecord();
        initRecyclerView();
    }

    private void initRecyclerView() {
        rvTransRecord.setHasFixedSize(true);
        rvTransRecord.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TranslationRecordAdapter(list);
        rvTransRecord.setAdapter(adapter);
//        rvTransRecord.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 点击事件回调函数
                toDetailsActivity(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // 长按事件回调函数
                showDeleteOption(position);
            }
        });

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 返回false表示不执行拖动
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                // 滑动
                final int position = viewHolder.getAdapterPosition();
                final TranslationRecord translationRecord = list.get(position);
                Snackbar.make(viewHolder.itemView, "删除当前项", Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                list.add(position, translationRecord);
                                adapter.notifyItemInserted(position);
                                rvTransRecord.scrollToPosition(position);
                                recordToDelete.remove(translationRecord);

                            }
                        })
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event != DISMISS_EVENT_ACTION) {
                                    databaseManager.deleteTranslationRecord(translationRecord);
                                }
                            }
                        })
                        .show();
                list.remove(position);
                adapter.notifyItemRemoved(position);
                recordToDelete.add(translationRecord);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvTransRecord);
    }

    private void toDetailsActivity(int position) {
        Intent intent = new Intent(TranslationRecordActivity.this, TranslationRecordDetailsActivity.class);
        intent.putExtra("trans_record_data", list.get(position));
        startActivity(intent);
    }

    private void showDeleteOption(final int position) {
        new MaterialDialog.Builder(this)
                .items(R.array.delete_option)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                databaseManager.deleteTranslationRecord(list.get(position));
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                                break;
                            case 1:
                                databaseManager.deleteAllTranslationRecords();
                                list.clear();
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                })
                .show();
    }

    private void queryTranslationRecord() {
        list = databaseManager.loadTranslationRecord();
        Collections.reverse(list);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }
}
