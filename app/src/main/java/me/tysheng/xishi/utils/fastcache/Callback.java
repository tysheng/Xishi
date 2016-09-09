package me.tysheng.xishi.utils.fastcache;

public interface Callback {
    void onSuccess();

    void onFailure(Exception e);
}
