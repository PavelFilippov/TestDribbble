package ru.com.testdribbble.mvp;

public interface BaseView {

    public abstract void fillStartPositions();

    public abstract void forbidAllClicks();

    public abstract void allowAllClicks();

}
