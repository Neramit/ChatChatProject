package com.example.chatchatapplication.Object_json;

import java.util.List;

/**
 * Created by Neramit777 on 10/6/2017 at 8:18 AM.
 */

public class DetailDataRecieve {
    private Group groupDetail;
    private List<Member> memberList;

    public Group getGroupDetail() {
        return groupDetail;
    }

    public void setGroupDetail(Group groupDetail) {
        this.groupDetail = groupDetail;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
