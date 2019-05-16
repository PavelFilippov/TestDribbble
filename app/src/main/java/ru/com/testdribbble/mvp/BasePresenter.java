package ru.com.testdribbble.mvp;

import ru.terrakok.cicerone.Router;

public interface BasePresenter {

    void setRouter(Router router);

    void onBackPressed();

}
