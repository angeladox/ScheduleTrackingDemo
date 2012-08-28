package org.motechproject.scheduletrackingdemo.validator;

import java.util.List;

//import org.motechproject.scheduletrackingdemo.OpenMrsClient;
import org.motechproject.scheduletrackingdemo.beans.PatientEnrollmentBean;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.domain.FormBeanGroup;
import org.motechproject.mobileforms.api.domain.FormError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientEnrollmentValidator extends
        AbstractPatientValidator<PatientEnrollmentBean> {

//    @Autowired
//    public PatientEnrollmentValidator(OpenMrsClient openmrsClient) {
//        super(openmrsClient);
//    }

    @Override
    public List<FormError> validate(PatientEnrollmentBean formBean,
            FormBeanGroup formGroup, List<FormBean> allForms) {
        List<FormError> errors = super.validate(formBean, formGroup, allForms);
        validatePhoneNumberFormat(formBean.getPhoneNumber(), errors);
        validateOpenMrsPatientExists(formBean.getMotechId(), errors);

        return errors;
    }
}
