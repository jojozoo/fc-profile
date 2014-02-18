package com.renren.profile.util.sys.status;
/***
 * 
 * 项目名称：renren-profile  
 * 类名称：ReportStatus  
 * 类描述： 我的下属汇报状态
 * 创建人：wen.he1  
 * 创建时间：2012-3-16 上午10:22:36  
 * 
 * @version
 */
public enum ReportStatus {

    ON_START(1), // 还未写自评
    START(2), //   自评已经提交，主管可以评价
    EDIT(3),  //   主管对下属评价，但没有提交所以还可以编辑
    VIEW(4),  //   主管提交下属评价，可以查看对下属的评价信息
    ;
    private final int name;

    ReportStatus(int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }

}
