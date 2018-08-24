package com.amkj.dmsh.constant;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/11/11
 * class description:请输入类描述
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
}
