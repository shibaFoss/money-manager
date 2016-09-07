package gui.transaction;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import gui.BaseActivity;
import in.softc.aladindm.R;
import utils.DialogUtility;

public class CalculatorDialog implements View.OnClickListener {

    private Dialog dialog;
    private BaseActivity activity;
    private TextView preview, bnt1, bnt2, bnt3, bnt4, bnt5, bnt6, bnt7, bnt8, bnt9,
            bnt0, bntDot, bntMinus, bntPlus, bntMultiply, bntDivision, bntEqual, bntCancel, bntDone;
    private ImageButton bntClear;
    private OnSubmitResult resultListener;

    public interface OnSubmitResult {
        void onSubmitResult(double result, Dialog dialog);
    }

    public CalculatorDialog(BaseActivity activity, String startingNumber) {
        this.activity = activity;
        initDialog(startingNumber);
    }

    public void setOnSubmitResultListener(OnSubmitResult listener) {
        this.resultListener = listener;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void initDialog(String startingNumber) {
        dialog = DialogUtility.generateNewDialog(activity, R.layout.dialog_calculator);
        preview = (TextView) dialog.findViewById(R.id.txt_preview);
        preview.setText(String.valueOf(startingNumber));

        bntClear = (ImageButton) dialog.findViewById(R.id.bntClear);

        bnt1 = (TextView) dialog.findViewById(R.id.bnt1);
        bnt2 = (TextView) dialog.findViewById(R.id.bnt2);
        bnt3 = (TextView) dialog.findViewById(R.id.bnt3);
        bntDivision = (TextView) dialog.findViewById(R.id.bntDivision);

        bnt4 = (TextView) dialog.findViewById(R.id.bnt4);
        bnt5 = (TextView) dialog.findViewById(R.id.bnt5);
        bnt6 = (TextView) dialog.findViewById(R.id.bnt6);
        bntMultiply = (TextView) dialog.findViewById(R.id.bntMultiply);

        bnt7 = (TextView) dialog.findViewById(R.id.bnt7);
        bnt8 = (TextView) dialog.findViewById(R.id.bnt8);
        bnt9 = (TextView) dialog.findViewById(R.id.bnt9);
        bntMinus = (TextView) dialog.findViewById(R.id.bntMinus);

        bntDot = (TextView) dialog.findViewById(R.id.bntDot);
        bnt0 = (TextView) dialog.findViewById(R.id.bnt0);
        bntEqual = (TextView) dialog.findViewById(R.id.bntEqual);
        bntPlus = (TextView) dialog.findViewById(R.id.bntPlus);

        bntCancel = (TextView) dialog.findViewById(R.id.bntCancel);
        bntDone = (TextView) dialog.findViewById(R.id.bntDone);

        View[] views = new View[]{bntClear, bnt1, bnt2, bnt3, bnt4, bnt5, bnt6, bnt7, bnt8, bnt9, bnt0,
                bntDot, bntMinus, bntPlus, bntMultiply, bntDivision, bntEqual, bntCancel, bntDone};
        for (View view : views)
            view.setOnClickListener(this);

        bntClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                preview.setText("");
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == bntCancel.getId()) dismiss();

        if (id == bntClear.getId()) backspacePreview();

        if (id == bnt0.getId()) preview.setText(String.valueOf(preview.getText().toString() + 0));

        if (id == bnt1.getId()) preview.setText(String.valueOf(preview.getText().toString() + 1));

        if (id == bnt2.getId()) preview.setText(String.valueOf(preview.getText().toString() + 2));

        if (id == bnt3.getId()) preview.setText(String.valueOf(preview.getText().toString() + 3));

        if (id == bnt4.getId()) preview.setText(String.valueOf(preview.getText().toString() + 4));

        if (id == bnt5.getId()) preview.setText(String.valueOf(preview.getText().toString() + 5));

        if (id == bnt6.getId()) preview.setText(String.valueOf(preview.getText().toString() + 6));

        if (id == bnt7.getId()) preview.setText(String.valueOf(preview.getText().toString() + 7));

        if (id == bnt8.getId()) preview.setText(String.valueOf(preview.getText().toString() + 8));

        if (id == bnt9.getId()) preview.setText(String.valueOf(preview.getText().toString() + 9));

        if (id == bntDot.getId()) preview.setText(String.valueOf(preview.getText().toString() + "."));

        if (id == bntDivision.getId()) preview.setText(String.valueOf(preview.getText().toString() + "/"));

        if (id == bntMultiply.getId()) preview.setText(String.valueOf(preview.getText().toString() + "*"));

        if (id == bntPlus.getId()) preview.setText(String.valueOf(preview.getText().toString() + "+"));

        if (id == bntMinus.getId()) preview.setText(String.valueOf(preview.getText().toString() + "-"));

        if (id == bntDone.getId() || id == bntEqual.getId()) {
            try {
                Expression expression = new ExpressionBuilder(preview.getText().toString()).build();
                double result = expression.evaluate();

                if (id == bntDone.getId()) {
                    dismiss();
                    if (resultListener != null) resultListener.onSubmitResult(result, dialog);
                } else {
                    preview.setText(String.valueOf(result));
                }

            } catch (Exception err) {
                err.printStackTrace();
                activity.vibrate(10);
                activity.toast(activity.getString(R.string.something_went_wrong));
            }
        }
    }

    private void backspacePreview() {
        activity.vibrate(5);
        try {
            String oldInput = preview.getText().toString();
            if (oldInput.length() <= 1) {
                preview.setText("");
            } else {
                String newInput = oldInput.substring(0, oldInput.length() - 1);
                preview.setText(newInput);
            }
        } catch (Exception err) {
            err.printStackTrace();
            preview.setText("");
        }
    }

}
