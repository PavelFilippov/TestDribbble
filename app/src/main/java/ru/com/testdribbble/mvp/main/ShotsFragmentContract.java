package ru.com.testdribbble.mvp.main;

import java.util.List;

import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.mvp.BasePresenter;

public interface ShotsFragmentContract {

    interface ShotsFragmentView {

        void showLoading();

        void hideLoading();

        void setShots(List<Shot> shots);

        void showError(Throwable throwable);

    }

    interface ShotsFragmentPresenter extends BasePresenter {

        void setView(ShotsFragmentView view);

        void loadShots(int page);

    }
}
