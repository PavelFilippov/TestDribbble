package ru.com.testdribbble.mvp;

public interface BaseView {

    void showLoading();

    void hideLoading();

    void showServerError(Throwable throwable);

}
