package me.tysheng.xishi.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Sty
 * Date: 16/9/5 15:26.
 */
public class Value extends JSONObject {

    /**
     * action : hongbao_end
     * value : 抱歉，红包活动已经结束！
     */

    public String action;
    public String value;

    public Value(String action, String value) {
        this.action = action;
        this.value = value;
    }
}
