package com.dysy.bysj.common.enums;

/**
 * @author: Dai Junfeng
 * @create: 2021-03-12
 */
public class RoleEnum {
    public enum RoleTypeEnum {
        INNER(0, "预置"),
        CUSTOM(1, "自建")
        ;
        private int key;
        private String value;

        RoleTypeEnum(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public enum RoleStatusEnum {
        ENABLE(true, "启用"),
        DISABLE(false, "停用");

        private boolean key;
        private String value;

        RoleStatusEnum(boolean key, String value) {
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
