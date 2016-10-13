package gui.settings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

import gui.BaseActivity;
import in.softc.aladindm.R;

public class LanguageSelector implements ListView.OnItemClickListener {

    private static HashMap<Integer, LanguageKey> languageKeyHashMap = new HashMap<>();
    private BaseActivity activity;
    private MaterialDialog dialog;


    public void show(BaseActivity activity) {
        this.addAllLanguages();
        this.activity = activity;
        dialog = new MaterialDialog.Builder(activity)
                .title(activity.getString(R.string.choose_default_language))
                .customView(R.layout.dialog_language_selector, false)
                .build();

        ListView languageList = (ListView) dialog.findViewById(R.id.language_list);
        LanguageListAdapter adapter = new LanguageListAdapter();
        adapter.context = activity;
        adapter.languageKeyHashMap = languageKeyHashMap;
        languageList.setAdapter(adapter);
        languageList.setOnItemClickListener(this);

        dialog.show();
    }


    private void addAllLanguages() {
        languageKeyHashMap.put(0, new LanguageKey("English", "en_US"));
        languageKeyHashMap.put(1, new LanguageKey("Bengali", "bn"));
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int listPosition, long id) {
        LanguageKey languageKey = languageKeyHashMap.get(listPosition);
        activity.setLanguage(languageKey.languageCode);
        dialog.dismiss();
    }


    static class LanguageKey {
        public String languageName;
        public String languageCode;


        public LanguageKey(String languageName, String languageCode) {
            this.languageName = languageName;
            this.languageCode = languageCode;
        }
    }

    static class LanguageListAdapter extends BaseAdapter {

        public Context context;
        public HashMap<Integer, LanguageKey> languageKeyHashMap;


        @Override
        public int getCount() {
            return languageKeyHashMap.size();
        }


        @Override
        public Object getItem(int itemIndex) {
            return languageKeyHashMap.get(itemIndex);
        }


        @Deprecated
        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int itemPosition, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(context, R.layout.dialog_language_item_row, null);
            }

            LanguageKey languageKey = languageKeyHashMap.get(itemPosition);
            ((TextView) view.findViewById(R.id.language)).setText(languageKey.languageName);

            return view;
        }

    }
}
