package libs.localization;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import in.softc.aladindm.R;

public class BlankDummyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_dummy);

        dalayedFinish();
    }


    @Override
    public void finish() {
        super.finish();
    }


    private void dalayedFinish() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 200);
    }
}
