package gui.transaction;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import accounts.AccountManager;
import accounts.Transaction;
import in.mobi_space.money_manager.R;
import utils.Font;

class TransactionCategoryManager {
    public TransactionActivity activity;
    public Transaction transaction;

    TransactionCategoryManager(TransactionActivity activity, Transaction transaction) {
        this.activity = activity;
        this.transaction = transaction;

        LinearLayout categoryContainer = (LinearLayout) activity.findViewById(R.id.transaction_category_container);
        addChildCategory(categoryContainer, activity.getApp().getAccountManager());

    }

    private void addChildCategory(final LinearLayout categoryContainer, AccountManager accountManager) {
        ArrayList<String> categoryList = transaction.isExpense ?
                accountManager.expenseCates : accountManager.totalIncomeCates;

        if (categoryList != null) {
            if (categoryList.size() > 0) {
                for (final String name : categoryList) {
                    final TextView categoryItemChild = (TextView) View.inflate(activity, R.layout
                            .activity_transaction_category_card_category_item, null);
                    if (categoryItemChild != null) {
                        categoryItemChild.setTypeface(Font.RobotoLight);
                        categoryItemChild.setText(name);
                        categoryContainer.addView(categoryItemChild);
                        categoryItemChild.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (int i = 0; i < categoryContainer.getChildCount(); i++) {
                                    View v = categoryContainer.getChildAt(i);
                                    if (v instanceof TextView) {
                                        ((TextView) v).setCompoundDrawablesRelativeWithIntrinsicBounds(
                                                0, 0, R.drawable.ic_white_box, 0);
                                    }
                                }
                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (name.equals("Other")) {
                                    activity.findViewById(R.id.edit_category_other).setVisibility(View.VISIBLE);
                                    EditText categoryOtherEditText = (EditText) activity.findViewById(R.id.edit_category_other);
                                    categoryOtherEditText.requestFocus();
                                    imm.showSoftInput(categoryOtherEditText, InputMethodManager.SHOW_IMPLICIT);

                                } else {
                                    EditText categoryOtherEditText = (EditText) activity.findViewById(R.id.edit_category_other);
                                    imm.hideSoftInputFromInputMethod(categoryOtherEditText.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
                                    activity.findViewById(R.id.edit_category_other).setVisibility(View.GONE);
                                }

                                categoryItemChild.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                                        R.drawable.ic_action_done, 0);
                                transaction.transactionCategory = name;

                            }
                        });
                    }
                }
            }
        }
    }

}
