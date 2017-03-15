package com.arlen.frame.common.base;

/**
 * Created by Arlen on 2016/12/21 16:30.
 */
public interface IBaseView {

     void showLoadingView(boolean isContent);

     void showErrorView();

     void showEmptyView();

     void showDataView();

}
