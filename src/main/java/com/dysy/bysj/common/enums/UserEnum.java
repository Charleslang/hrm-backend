package com.dysy.bysj.common.enums;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-07
 */
public final class UserEnum {

    public enum UserSexEnum {
        MALE(0, "男"),
        FEMALE(1, "女")
        ;
        private int key;
        private String value;

        UserSexEnum(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int getType() {
            return key;
        }

        public String getSex() {
            return value;
        }
    }

    public enum UserStatusEnum {
        ONJOB(0, "在职"),
        QUIT(1, "离职");

        private int key;
        private String value;

        UserStatusEnum(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public enum UserTypeEnum {
        FORMAL(0, "正式"),
        INGORMAL(1, "非正式")
        ;

        private int key;
        private String value;

        UserTypeEnum(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
