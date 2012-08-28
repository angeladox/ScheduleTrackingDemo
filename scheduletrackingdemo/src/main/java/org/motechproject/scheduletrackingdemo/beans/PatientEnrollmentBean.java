package org.motechproject.scheduletrackingdemo.beans;

import org.motechproject.mobileforms.api.domain.FormBean;

public class PatientEnrollmentBean extends FormBean {

    private static final long serialVersionUID = 1L;

    private String motechId;
    private String phoneNumber;
    private String groupId;

    public String getMotechId() {
        return motechId;
    }

    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String groupId() {
        return groupId;
    }

}
