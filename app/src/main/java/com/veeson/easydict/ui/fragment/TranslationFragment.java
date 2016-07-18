package com.veeson.easydict.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.veeson.easydict.R;
import com.veeson.easydict.common.capsulation.PlayAudioManager;
import com.veeson.easydict.common.utils.FileUtils;
import com.veeson.easydict.common.utils.InputMethodUtils;
import com.veeson.easydict.common.utils.NetWorkUtils;
import com.veeson.easydict.common.utils.StringUtils;
import com.veeson.easydict.common.utils.TimeUtils;
import com.veeson.easydict.db.DatabaseManager;
import com.veeson.easydict.model.BaiduTranslation;
import com.veeson.easydict.model.GoogleTranslation;
import com.veeson.easydict.model.TranslationRecord;
import com.veeson.easydict.model.YoudaoTranslation;
import com.veeson.easydict.network.Network;
import com.veeson.easydict.ui.activities.BaseActivity;
import com.veeson.easydict.ui.activities.BlankActivity;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URISyntaxException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranslationFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tv_trans_source)
    TextView tvTransSource;
    @BindView(android.R.id.input)
    EditText input;
    @BindView(R.id.ly_to_translate)
    LinearLayout lyToTranslate;
    @BindView(R.id.iv_arrows)
    ImageView ivArrows;
    @BindView(R.id.tv_trans_results)
    TextView tvTransResults;
    @BindView(R.id.ib_cancel_input)
    ImageButton ibCancelInput;
    @BindView(R.id.ib_load_file)
    ImageButton ibLoadFile;
    @BindView(R.id.iv_read)
    ImageView ivRead;
    @BindView(R.id.iv_copy)
    ImageView ivCopy;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.iv_expand)
    ImageView ivExpand;

    private static final String TAG = "TranslationFragment";
    private static final Random random = new Random();
    private static final int FILE_SELECT_CODE = 0;
    Observer<BaiduTranslation> baiduTranslationObserver = new Observer<BaiduTranslation>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            handleTranslateError(e);
        }

        @Override
        public void onNext(BaiduTranslation baiduTranslation) {
            TranslationRecord translationRecord = new TranslationRecord();
            translationRecord.original = baiduTranslation.trans_result.get(0).src;
            translationRecord.result = baiduTranslation.trans_result.get(0).dst;
            translationRecord.date = TimeUtils.getCurrentTimeInString();
            translationRecord.fromTo = "来自百度翻译";
            saveTranslationRecord(translationRecord);
            handleBaiduTranslationContent(baiduTranslation);
        }
    };

    Observer<YoudaoTranslation> youdaoTranslationObserver = new Observer<YoudaoTranslation>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            handleTranslateError(e);
        }

        @Override
        public void onNext(YoudaoTranslation youdaoTranslation) {
            TranslationRecord translationRecord = new TranslationRecord();
            translationRecord.original = youdaoTranslation.query;
            translationRecord.result = youdaoTranslation.translation.get(0);
            translationRecord.date = TimeUtils.getCurrentTimeInString();
            translationRecord.fromTo = "来自有道翻译";
            saveTranslationRecord(translationRecord);
            handleYoudaoTranslationContent(youdaoTranslation);
        }
    };

    Observer<GoogleTranslation> googleTranslationObserver = new Observer<GoogleTranslation>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            handleTranslateError(e);
        }

        @Override
        public void onNext(GoogleTranslation googleTranslation) {
            TranslationRecord translationRecord = new TranslationRecord();
            translationRecord.original = googleTranslation.sentences.get(0).orig;
            translationRecord.result = googleTranslation.sentences.get(0).trans;
            translationRecord.date = TimeUtils.getCurrentTimeInString();
            translationRecord.fromTo = "来自谷歌翻译";
            saveTranslationRecord(translationRecord);
            handleGoogleTranslationContent(googleTranslation);
        }
    };

    private void saveTranslationRecord(TranslationRecord translationRecord) {
        DatabaseManager.getInstance(getActivity()).saveTranslationRecord(translationRecord);
    }

    private void handleTranslateError(Throwable e) {
        if (NetWorkUtils.isOnline(getActivity())) {
            Toast.makeText(getActivity(), "服务器忙，切换到其它翻译引擎试试", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        } else {
            Toast.makeText(getActivity(), "网络异常，请检查网络设置", Toast.LENGTH_SHORT).show();
        }
    }

//    public TranslationFragment() {
//        // Required empty public constructor
//    }

    public static TranslationFragment newInstance(String s) {
        TranslationFragment myFragment = new TranslationFragment();

        Bundle args = new Bundle();
        args.putString("Trans_text", s);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, container, false);
        ButterKnife.bind(this, view);
        setListener();
        String originalText = getArguments().getString("Trans_text", "");
        if (!TextUtils.isEmpty(originalText)) {
            input.setText(originalText);
            getYoudaoTranslationContent(originalText);
        }
        return view;
    }

    private void setListener() {
        ivArrows.setPressed(false); // 禁止点击箭头按钮
        lyToTranslate.setClickable(false); // 禁止点击翻译按钮
        tvTransSource.setOnClickListener(this);
        lyToTranslate.setOnClickListener(this);
        ivRead.setOnClickListener(this);
        ivExpand.setOnClickListener(this);
        tvTransResults.setMovementMethod(new ScrollingMovementMethod()); // 设置TextView可滚动
        ibCancelInput.setOnClickListener(this);
        ibLoadFile.setOnClickListener(this);
        ivCopy.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvTransSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!input.getText().toString().trim().isEmpty()) {
                    if (s.toString().equals("有道翻译")) {
                        getYoudaoTranslationContent(input.getText().toString());
                    } else if (s.toString().equals("百度翻译")) {
                        getBaiduTranslationContent(input.getText().toString());
                    } else if (s.toString().equals("谷歌翻译")) {
                        getGoogleTranslationContent(input.getText().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    ivArrows.setPressed(true);
                    lyToTranslate.setClickable(true);
                } else {
                    ivArrows.setPressed(false);
                    lyToTranslate.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }

    private void getBaiduTranslationContent(String input) {
        // 只要输入文本里面有中文就应该指定文本翻译成英文
        if (StringUtils.ifHaveChinese(input)) {
            baiduTranslationObserver(input, "zh", "en");
        } else {
            baiduTranslationObserver(input, "auto", "zh");
        }
    }

    private void baiduTranslationObserver(String input, String from, String to) {
        int salt = random.nextInt(10000);
        // 百度翻译接口要求获取md5签名：appid+q+salt+密钥
        String md5 = new String(Hex.encodeHex(DigestUtils.md5("20160607000022961" + input + salt + "s52637hqrVrVsZks53Dc")));
        subscription = Network.getBaiduTranslationApi()
                .getBaiduTranslation(input, from, to, "20160607000022961", String.valueOf(salt), md5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baiduTranslationObserver);
    }

    private void handleBaiduTranslationContent(BaiduTranslation baiduTranslation) {
        tvTransResults.setText(baiduTranslation.trans_result.get(0).dst);
    }

    private void getYoudaoTranslationContent(String text) {
        subscription = Network.getYoudaoTranslationApi()
                .getYoudaoTranslation("abc10086", "1210941837", "data", "json", "1.1", "translate", text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(youdaoTranslationObserver);
    }

    private void handleYoudaoTranslationContent(YoudaoTranslation youdaoTranslation) {
        String errorCode = youdaoTranslation.errorCode;
        switch (errorCode) {
            case "0":
                tvTransResults.setText(youdaoTranslation.translation.get(0));
                break;
            case "20":
                Toast.makeText(getActivity(), "要翻译的文本过长", Toast.LENGTH_SHORT).show();
                break;
            case "30":
                Toast.makeText(getActivity(), "无法进行有效的翻译", Toast.LENGTH_SHORT).show();
                break;
            case "40":
                Toast.makeText(getActivity(), "不支持的语言类型", Toast.LENGTH_SHORT).show();
                break;
            case "50":
                Toast.makeText(getActivity(), "无效的key", Toast.LENGTH_SHORT).show();
                break;
            case "60":
                Toast.makeText(getActivity(), "无词典结果，仅在获取词典结果生效", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getGoogleTranslationContent(String input) {
        if (!StringUtils.isEnglish(StringUtils.arraysToString(input))) {
//            googleTranslationObserver("zh-CN", "en", input);
            googleTranslationBug();
        } else {
            googleTranslationObserver("auto", "zh-CN", input);
        }
    }

    private void googleTranslationBug() {
        new MaterialDialog.Builder(getActivity())
                .title("谷歌翻译只支持英译中")
                .content("由于谷歌翻译接口的问题，目前谷歌翻译只支持英译中，暂不支持其它翻译，如有需要请切换为有道或百度翻译，作者后期将努力修复这个bug。")
                .positiveText("好的")
                .icon(getResources().getDrawable(R.drawable.ic_translation_green))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        tvTransResults.setText(input.getText());
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void handleGoogleTranslationContent(GoogleTranslation googleTranslation) {
        tvTransResults.setText(googleTranslation.sentences.get(0).trans);
    }

    private void googleTranslationObserver(String sl, String tl, String text) {
        subscription = Network.getGoogleTranslationApi()
                .getGoogleTranslation("gtx", sl, tl, "", new String[]{"t", "tl"}, "1", "icon", text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(googleTranslationObserver);
    }

    /**
     * 选择翻译引擎
     */
    private void selectTransSource() {
        new MaterialDialog.Builder(getActivity())
                .items(R.array.trans_source_array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tvTransSource.setText(text.toString());
                    }
                })
                .show();
    }

    /**
     * 选择文件
     */
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "选择要翻译的txt文本"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == BaseActivity.RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    // Get the path
                    String path = null;
                    try {
                        path = FileUtils.getPath(getActivity(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                    loadFileToTrans(path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 将.txt文本的文字转换为String并翻译
     *
     * @param path
     */
    private void loadFileToTrans(String path) {
        if (path.endsWith(".txt")) {
            String fileText = null;
            try {
                fileText = FileUtils.readFile(path, FileUtils.resolveCode(path)).replace("\n", "").replace("\r", " ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            input.setText(fileText);
            getYoudaoTranslationContent(fileText);
        } else {
            new MaterialDialog.Builder(getActivity())
                    .title("不支持的文本格式")
                    .content("文本翻译目前只支持.txt文本文件的翻译")
                    .positiveText("知道了")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void showAboutTxtTransDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("关于文本翻译")
                .content("文本翻译目前只支持.txt文本文件，比如filename.txt，暂不支持.doc等其它任何格式的文件。")
                .positiveText("选择文本")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        showFileChooser();
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_trans_source:
                selectTransSource();
                break;
            case R.id.ly_to_translate:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodUtils.closeSoftKeyboard(view);
                }
                String text = input.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    String transSource = tvTransSource.getText().toString();
                    switch (transSource) {
                        case "有道翻译":
                            getYoudaoTranslationContent(text);
                            break;
                        case "百度翻译":
                            getBaiduTranslationContent(text);
                            break;
                        case "谷歌翻译":
                            getGoogleTranslationContent(text);
                            break;
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入翻译内容", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_cancel_input:
                input.setText("");
                lyToTranslate.setClickable(true);
                break;
            case R.id.iv_read:
                String readResult = tvTransResults.getText().toString();
                if (!TextUtils.isEmpty(readResult) && !StringUtils.ifHaveChinese(readResult)) {
                    PlayAudioManager.playENPronVoice(getActivity(), tvTransResults.getText().toString(), "1");
                } else if (!TextUtils.isEmpty(readResult)) {
                    PlayAudioManager.playCNPronVoice(getActivity(), readResult);
                }
                break;
            case R.id.iv_copy:
                String copyResult = tvTransResults.getText().toString();
                if (!TextUtils.isEmpty(copyResult)) {
                    StringUtils.copyToClipboard(getActivity(), tvTransResults.getText().toString());
                }
                break;
            case R.id.iv_share:
                String shareText = tvTransResults.getText().toString();
                if (!TextUtils.isEmpty(shareText)) {
                    StringUtils.shareToApps(getActivity(), shareText);
                }
                break;
            case R.id.iv_expand:
                Intent intent = new Intent(getActivity(), BlankActivity.class);
                intent.putExtra("trans_result", tvTransResults.getText().toString());
                startActivity(intent);
                break;
            case R.id.ib_load_file:
                showAboutTxtTransDialog();
                break;
        }
    }
}
