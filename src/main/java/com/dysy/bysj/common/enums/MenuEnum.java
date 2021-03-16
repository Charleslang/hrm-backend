package com.dysy.bysj.common.enums;

/**
 * @author: Dai Junfeng
 * @create: 2021-03-14
 */
public class MenuEnum {

    public enum MenuStatusEnum {
        ENABLE(true, "启用"),
        DISABLE(false, "停用");

        private boolean key;
        private String value;

        MenuStatusEnum(boolean key, String value) {
            this.key = key;
            this.value = value;
        }

        public boolean getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
