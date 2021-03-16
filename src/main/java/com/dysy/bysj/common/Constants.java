package com.dysy.bysj.common;

/**
 * @author: Dai Junfeng
 * @create: 2020-12-29
 **/
public class Constants {

    /**
     * access_token 名字
     */
    public static final String ACCESS_TOKEN = "accessToken";

    /**
     * 请求头携带的 access_token
     */
    public static final String HEADER_TOKEN = "access-token";

    /**
     * refresh_token 名字
     */
    public static final String REFRESH_TOKEN = "refreshToken";

    /**
     * access_token 过期时间
     */
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 5 * 60;

    /**
     * refresh_token 过期时间
     */
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 60 * 60;

    /**
     * access_token 缓存前缀
     */
    public static final String ACCESS_TOKEN_CACHE_PREFIX = "accessToken:";

    /**
     * refresh_token 缓存前缀
     */
    public static final String REFRESH_TOKEN_CACHE_PREFIX = "refreshToken:";

    /**
     * token 黑名单
     */
    public static final String TOKEN_BLACK_LIST_PREFIX = "token:blacklist:";

    public static final long TOKEN_EXPIRE_TIME = 5 * 60 *1000;
    public static final String APP_SECRET = "djfapp";

    /**
     * 加密方法
     */
    public static final String APP_ALGORITHM_NAME = "MD5";

    /**
     * 加密散列次数
     */
    public static final int APP_SECRET_HASH_ITERATIONS = 10;

    /**
     * 随机盐的长度
     */
    public static final int RANDOM_SALT_LENGTH = 10;

    /**
     * 验证码长度
     */
    public static final int APP_VERIFY_CODE_LENGTH = 6;

    /**
     * 邮箱验证码主题
     */
    public static final String MAIL_SUBJECT = "[HRM] 验证电子邮箱";

    /**
     * 邮箱验证码正文
     */
    public static final String MAIL_HTML = "<html>\n" +
                                            "<body>\n" +
                                            "你正在使用电子邮箱进行验证, 你的验证码为:\n " +
                                            "    <h1 align=\"center\">{0}</h1> \n" +
                                            "1 分钟内有效, <b>请勿将此验证码转发给或提供给任何人</b>\n" +
                                            "</body>\n" +
                                            "</html>";

    /**
     * 验证码缓存前缀
     */
    public static final String VERYFY_CODE_CACHE_PREFIX = "hrm:verfycode:";

    /**
     * 验证码过期时间(单位: 秒)
     */
    public static final long VERYFY_CODE_EXPIRE_TIME = 1 * 60;
}
