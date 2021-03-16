package com.dysy.bysj.common.enums;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-07
 */
public final class UnitEnum {

    public enum UnitTypeEnum {
        UNIT(0),
        DEPARTMENT(1)
        ;
        private int key;

        UnitTypeEnum(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }
    }

    public enum UnitInnerEnum {
        PRESET(1, "内置"),
        ADDITIONAL(0, "非内置")
        ;
        private int key;
        private String value;

        UnitInnerEnum(int key, String value) {
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

}
