package me.tysheng.xishi.utils.fastcache;

public interface GetCallback<T> {
    void onSuccess(T object);

    void onFailure(Exception e);
}
