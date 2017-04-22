package com.hss01248.hiddencamera;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface PhotoCallback {
    void onFail();
    void onSuccess(String path);
}
