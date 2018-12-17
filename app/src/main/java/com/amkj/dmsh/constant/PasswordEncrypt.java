package com.amkj.dmsh.constant;

import java.util.regex.Pattern;

import static com.amkj.dmsh.constant.ConstantVariable.REGEX_PW_ALPHABET_NUMBER;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/11/11
 * class description:密码设置类
 */

public class PasswordEncrypt {
    public static String getEncryptedPassword(String password) {
        Sha1Md5 sha1Md5 = new Sha1Md5();
        final String t = "g{faJ&)H<34rz(q*\"C0S=Xy`TW~ZGD.wt6_1j@dU";
        return sha1Md5.getMD5(toLowerCase(sha1Md5.getDigestOfString(password.getBytes()).getBytes()) + t);
    }

    //大小写切换
    private static String toLowerCase(byte[] data) {
        int dist = 'a' - 'A';
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= 'A' && data[i] <= 'Z') {
                data[i] += dist;
            }
        }
        return new String(data);
    }

    /**
     * 密码6-20 字母数字组合
     * @param password
     * @return
     */
    public static boolean isPwEligibility(String password){
        return Pattern.compile(REGEX_PW_ALPHABET_NUMBER).matcher(password).matches();
    }
}
