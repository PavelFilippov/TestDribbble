package ru.com.testdribbble.ui.adapters.base;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lombok.Getter;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    @Getter
    public View itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }


    protected <T extends View> T getView(@IdRes int viewId) {
        return (T) itemView.findViewById(viewId);
    }

}
