package test.huangjh.www.rxjava2learning.entity;

/**
 * Created by  Hjh on 2018/2/27.
 * desc：
 */

public class UserInfo {

    private UserBaseInfoResponse baseInfo;
    private UserExtraInfoResponse extraInfo;

    public UserInfo(UserBaseInfoResponse baseInfo, UserExtraInfoResponse extraInfo) {
        this.baseInfo = baseInfo;
        this.extraInfo = extraInfo;
    }
}
