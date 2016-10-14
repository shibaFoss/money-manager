package gui.home;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import in.mobi_space.money_manager.R;
import utils.Font;

class AccountSwitcherPopupMenu implements View.OnClickListener {

    private PopupWindow popupWindow;
    private HomeActivity activity;
    private View anchorView;
    private View popupView;
    OnAccountSelection accountSelectionListener;

    interface OnAccountSelection {
        void onSelect(String accountName);
    }

    AccountSwitcherPopupMenu(HomeActivity activity) {
        this.activity = activity;
        initialize();
    }

    void setAccountSelectionListener(OnAccountSelection listener) {
        this.accountSelectionListener = listener;
    }

    void setAnchorView(View anchorView) {
        this.anchorView = anchorView;
    }

    public void show() {
        popupWindow.showAtLocation(anchorView, Gravity.TOP, 0, anchorView.getHeight() / 2);
    }

    public void close() {
        popupWindow.dismiss();
    }

    private void initialize() {
        initializePopupView();
        popupWindow = new PopupWindow(activity);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.transparent_bg));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(popupView);
    }

    private void initializePopupView() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.activity_home_account_switcher_popup_menu, null, false);
    }


    @Override
    public void onClick(View view) {
        popupWindow.dismiss();
        if (view instanceof TextView) {
            String menuName = ((TextView) view).getText().toString();
            if (accountSelectionListener != null)
                accountSelectionListener.onSelect(menuName);
        }
    }

    void addItems(String[] options) {
        LinearLayout itemContainer = (LinearLayout) popupView.findViewById(R.id.item_layout_container);
        if (itemContainer != null) {
            itemContainer.removeAllViews();
            for (String option : options) {
                TextView itemMenu = (TextView) View.inflate(activity,
                        R.layout.activity_home_account_switcher_item_popmenu, null);
                itemMenu.setTypeface(Font.RobotoRegular);
                itemMenu.setText(option);
                itemMenu.setOnClickListener(this);
                itemContainer.addView(itemMenu);
            }
        }

    }
}
