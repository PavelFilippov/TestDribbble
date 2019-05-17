package ru.com.testdribbble.ui;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.widget.EditText;

import ru.com.testdribbble.core.utils.KeyboardUtils;
import ru.com.testdribbble.ui.main.MainActivity;

public class BaseMainFragment extends Fragment {

    public MainActivity getMainActivity() {
        if (getActivity() != null) return ((MainActivity) getActivity());
        return null;
    }

    @Override
    public void onPause() {
        KeyboardUtils.hideKeyboard(getMainActivity());
        super.onPause();
    }

    public void showKeyboardOnFocus(EditText editText) {
        editText.setOnFocusChangeListener((view, focus) -> {
            if(focus) {
                KeyboardUtils.showKeyboard(getMainActivity(), editText);
            }
        });
    }

    public boolean isDoubleClick(long mLastClickTime) {
        return SystemClock.elapsedRealtime() - mLastClickTime < 1000;
    }

}
