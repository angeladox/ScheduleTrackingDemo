package org.motechproject.scheduletrackingdemo.validator;

import java.util.List;

//import org.motechproject.scheduletrackingdemo.OpenMrsClient;
import org.motechproject.scheduletrackingdemo.beans.PatientRegistrationBean;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.domain.FormBeanGroup;
import org.motechproject.mobileforms.api.domain.FormError;
//import org.motechproject.mrs.model.MRSPatient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientRegistrationValidator extends
        AbstractMobileValidator<PatientRegistrationBean> {

//    private OpenMrsClient openmrsClient;
//
//    @Autowired
//    public PatientRegistrationValidator(OpenMrsClient openmrsClient) {
//        this.openmrsClient = openmrsClient;
//    }

    @Override
    public List<FormError> validate(PatientRegistrationBean formBean,
            FormBeanGroup formGroup, List<FormBean> allForms) {
        List<FormError> errors = super.validate(formBean, formGroup, allForms);
        validatePhoneNumberFormat(formBean.getPhoneNumber(), errors);
        validateUniqueMotechId(formBean.getMotechId(), errors);

        return errors;
    }

    protected void validateUniqueMotechId(String motechId,
            List<FormError> errors) {
//        MRSPatient existingPatient = openmrsClient
//                .getPatientByMotechId(motechId);
//        if (existingPatient != null) {
//            errors.add(new FormError("motechId",
//                    "Already a patient with this MoTeCH Id"));
//        }
    }
}
