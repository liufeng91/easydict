package com.veeson.easydict.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.veeson.easydict.AppConstants;
import com.veeson.easydict.R;
import com.veeson.easydict.common.capsulation.PlayAudioManager;
import com.veeson.easydict.common.utils.InputMethodUtils;
import com.veeson.easydict.common.utils.NetWorkUtils;
import com.veeson.easydict.common.utils.PreferencesUtils;
import com.veeson.easydict.common.utils.StringUtils;
import com.veeson.easydict.db.DatabaseManager;
import com.veeson.easydict.model.YoudaoWord;
import com.veeson.easydict.model.shanbay.AddWord;
import com.veeson.easydict.model.shanbay.Sentence;
import com.veeson.easydict.model.shanbay.Word;
import com.veeson.easydict.network.Network;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchWordActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ly_word_details)
    LinearLayout lyWordDetails;
    @BindView(R.id.iv_clear_content)
    ImageView ivClearContent;
    @BindView(R.id.iv_search_word)
    ImageView ivSearchWord;
    @BindView(R.id.et_input_word)
    EditText etInputWord;
    @BindView(R.id.tv_word)
    TextView tvWord;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.iv_uk_horn)
    ImageView ivUKHorn;
    @BindView(R.id.tv_uk_phonetic)
    TextView tvUKPhonetic;
    @BindView(R.id.iv_us_horn)
    ImageView ivUSHorn;
    @BindView(R.id.tv_us_phonetic)
    TextView tvUSPhonetic;
    @BindView(R.id.iv_slash)
    ImageView ivSlash;
    @BindView(R.id.tv_basic_paraphrase)
    TextView tvBasicParaphrase;
    @BindView(R.id.tv_web_paraphrase)
    TextView tvWebParaphrase;
    @BindView(R.id.tv_sentence)
    TextView tvSentence;
    @BindView(R.id.ly_horn_phonetic)
    LinearLayout lyHornPhonetic;
    @BindView(R.id.rl_error_tip)
    RelativeLayout rlErrorTip;
    @BindView(R.id.bt_to_translation)
    Button btToTranslation;

    private static final String TAG = "SearchWordActivity";
    private static YoudaoWord youdao;
    private static int flag = 0;

    Observer<YoudaoWord> youdaoWordObserver = new Observer<YoudaoWord>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
            if (NetWorkUtils.isOnline(SearchWordActivity.this)) {
                lyWordDetails.setVisibility(View.VISIBLE);
                tvBasicParaphrase.setText("没有基本释义");
                tvWebParaphrase.setText("没有网络释义");
                tvSentence.setText("没有例句");
                ivCollection.setVisibility(View.GONE);
                lyHornPhonetic.setVisibility(View.GONE);
                rlErrorTip.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(SearchWordActivity.this, R.string.bad_network, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNext(YoudaoWord youdaoWord) {
            if (PreferencesUtils.getBoolean(SearchWordActivity.this, AppConstants.LOGIN_SHANBAY) && StringUtils.isEnglish(youdaoWord.query)) {
                flag = 3;
                getShanbayWord(youdaoWord.query);
            }
            lyWordDetails.setVisibility(View.VISIBLE);
            ivCollection.setVisibility(View.VISIBLE);
            lyHornPhonetic.setVisibility(View.VISIBLE);
            rlErrorTip.setVisibility(View.GONE);
            youdao = youdaoWord;
            if (DatabaseManager.getInstance(SearchWordActivity.this).queryCollectionWord(youdao.query)) {
                ivCollection.setSelected(true);
            }
            handleYoudaoWordContent(youdao);
        }
    };

    Observer<Word> wordObserver = new Observer<Word>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
            flag = 0;
        }

        @Override
        public void onNext(Word word) {
            int ids = word.data.id;
            switch (flag) {
                case 1:
                    if (word.status_code == 0) {
                        addWordToShanbay(ids);
                    }
                    flag = 0;
                    break;
                case 3:
                    getShanbaySentence(ids);
                    flag = 0;
                    break;
            }
        }
    };

    Observer<AddWord> addWordObserver = new Observer<AddWord>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }

        @Override
        public void onNext(AddWord addWord) {
            Log.d(TAG, addWord.toString());
        }
    };

    Observer<Sentence> sentenceObserver = new Observer<Sentence>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
            tvSentence.setText("没有例句");
        }

        @Override
        public void onNext(Sentence sentence) {
            if (sentence.data.get(0).annotation != null) {
                setSentence(sentence);
            } else {
                tvSentence.setText("没有例句");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_word);
        ButterKnife.bind(this);
        setToolbar();
        if (!PreferencesUtils.getBoolean(this, AppConstants.LOGIN_SHANBAY)) {
            tvSentence.setText("登录扇贝网账号获取单词例句");
        }
        YoudaoWord youdaoWord = (YoudaoWord) getIntent().getSerializableExtra("collection_word_data");
        if (youdaoWord != null) {
            youdao = youdaoWord;
            if (DatabaseManager.getInstance(SearchWordActivity.this).queryCollectionWord(youdaoWord.query)) {
                ivCollection.setSelected(true);
            }
            setUI(youdaoWord);
            ivSearchWord.setSelected(false);
        } else {
            lyWordDetails.setVisibility(View.INVISIBLE);
            InputMethodUtils.openSoftKeyboard(this, etInputWord);
        }
        setListener();
    }

    private void getYoudaoWordContent(String inputWord) {
        subscription = Network.getYoudaoWordApi()
                .getYoudaoWord("abc10086", "1210941837", "data", "json", "1.1", inputWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(youdaoWordObserver);
    }

    /**
     * 获取扇贝单词
     *
     * @param word
     */
    private void getShanbayWord(String word) {
        subscription = Network.getWordApi()
                .getWord(PreferencesUtils.getString(this, AppConstants.ACCESS_TOKEN), word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordObserver);
    }

    /**
     * 添加生词到扇贝账号
     * @param id
     */
    private void addWordToShanbay(int id) {
        subscription = Network.getAddWordApi()
                .getAddWord(PreferencesUtils.getString(this, AppConstants.ACCESS_TOKEN), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addWordObserver);
    }

    private void getShanbaySentence(int id) {
        subscription = Network.getSentenceApi()
                .getSentence(PreferencesUtils.getString(this, AppConstants.ACCESS_TOKEN), id, "sys")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sentenceObserver);
    }

    /**
     * 处理获取到的有道单词翻译结果
     *
     * @param youdaoWord
     */
    private void handleYoudaoWordContent(YoudaoWord youdaoWord) {
        String errorCode = youdaoWord.errorCode;
        switch (errorCode) {
            case "0":
                setUI(youdaoWord);
                break;
            case "20":
                tvWord.setText("要翻译的文本过长");
                break;
            case "30":
                tvWord.setText("无法进行有效的翻译");
                break;
            case "40":
                tvWord.setText("不支持的语言类型");
                break;
            case "无效的key":
                tvWord.setText("无词典结果，仅在获取词典结果生效");
                break;
        }
    }

    /**
     * 适配UI
     *
     * @param youdaoWord
     */
    private void setUI(YoudaoWord youdaoWord) {
        tvWord.setText(youdaoWord.query);
        if (!StringUtils.ifAllChinese(youdaoWord.query)) {
            tvUKPhonetic.setText("英 [" + youdaoWord.basic.us_phonetic + "]");
            tvUSPhonetic.setText("美 [" + youdaoWord.basic.uk_phonetic + "]");
            tvUSPhonetic.setVisibility(View.VISIBLE);
            ivUSHorn.setVisibility(View.VISIBLE);
            ivSlash.setVisibility(View.VISIBLE);
        } else {
            tvUKPhonetic.setText("[" + youdaoWord.basic.phonetic + "]");
            tvUSPhonetic.setVisibility(View.INVISIBLE);
            ivUSHorn.setVisibility(View.INVISIBLE);
            ivSlash.setVisibility(View.INVISIBLE);
        }
        List<String> explainList = youdaoWord.basic.explains;
        StringBuilder explains = new StringBuilder("");
        for (int i = 0; i < explainList.size(); i++) {
            explains.append(explainList.get(i)).append("\n");
        }
        explains.setLength(explains.length() - 1);
        tvBasicParaphrase.setText(explains);
        List<YoudaoWord.WebEntity> webExplainList = youdaoWord.web;
        StringBuilder webExplains = new StringBuilder();
        List<String> valueList;
        StringBuilder values = new StringBuilder();
        for (int i = 0; i < webExplainList.size(); i++) {
            values.setLength(0);
            valueList = webExplainList.get(i).value;
            for (String value :
                    valueList) {
                values.append(value).append("；");
            }
            webExplains.append(webExplainList.get(i).key).append(":  ").append(values).append("\n");
        }
        if (webExplains.length() > 0) {
            webExplains.setLength(webExplains.length() - 1);
        }
        tvWebParaphrase.setText(webExplains);
    }

    private void setSentence(Sentence sentence) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentence.data.size(); i++) {
            sb.append(sentence.data.get(i).annotation).append("\n").append(sentence.data.get(i).translation).append("\n");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        tvSentence.setText(StringUtils.removeTags(sb.toString()));
    }

    /**
     * 设置控件点击事件监听
     */
    private void setListener() {
        ivClearContent.setOnClickListener(this);
        ivSearchWord.setOnClickListener(this);
        ivUKHorn.setOnClickListener(this);
        ivUSHorn.setOnClickListener(this);
        ivCollection.setOnClickListener(this);
        btToTranslation.setOnClickListener(this);
        etInputWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    ivSearchWord.setPressed(true);
                    ivClearContent.setVisibility(View.VISIBLE);
                } else {
                    ivSearchWord.setPressed(false);
                    ivClearContent.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etInputWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !v.getText().toString().trim().isEmpty()) {
                    ivSearchWord.performClick();
                    ivSearchWord.setPressed(false);
                    return true;
                }
                return false;
            }
        });
    }

    private void wordCollection(YoudaoWord youdaoWord) {
        DatabaseManager.getInstance(this).saveCollectionWord(youdaoWord);
        if (NetWorkUtils.isOnline(this) && StringUtils.isEnglish(youdaoWord.query)) {
            flag = 1;
            getShanbayWord(youdaoWord.query);
        }
    }

    private void cancelWordCollection(String word) {
        DatabaseManager.getInstance(this).cancelCollectionWord(word);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_word:
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodUtils.closeSoftKeyboard(view);
                }
                if (ivCollection.isSelected()) {
                    ivCollection.setSelected(false);
                }
                String inputWord = etInputWord.getText().toString().trim();
                if (inputWord.isEmpty()) {
                    Toast.makeText(this, "请输入要查询的单词", Toast.LENGTH_SHORT).show();
                    return;
                }
                getYoudaoWordContent(inputWord);
                break;
            case R.id.iv_clear_content:
                etInputWord.setText("");
                break;
            case R.id.iv_uk_horn:
                String word = tvWord.getText().toString();
                if (StringUtils.isEnglish(word)) {
                    PlayAudioManager.playENPronVoice(this, word, "1");
                } else {
                    PlayAudioManager.playCNPronVoice(this, word);
                }
                break;
            case R.id.iv_us_horn:
                PlayAudioManager.playENPronVoice(this, tvWord.getText().toString(), "2");
                break;
            case R.id.iv_collection:
                if (!ivCollection.isSelected()) {
                    ivCollection.setSelected(true);
                    wordCollection(youdao);
                } else {
                    ivCollection.setSelected(false);
                    cancelWordCollection(youdao.query);
                }
                break;
            case R.id.bt_to_translation:
                Intent intent = new Intent();
                intent.putExtra("to_trans", youdao.query);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodUtils.closeSoftKeyboard(view);
        }

    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }
}
