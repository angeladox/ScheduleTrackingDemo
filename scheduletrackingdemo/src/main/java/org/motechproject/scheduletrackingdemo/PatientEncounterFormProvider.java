package org.motechproject.scheduletrackingdemo;

import org.motechproject.mobileforms.api.service.FormProvider;
import org.motechproject.scheduletrackingdemo.beans.PatientEncounterBean;
import org.motechproject.scheduletrackingdemo.validator.PatientEncounterValidator;
import org.springframework.stereotype.Component;

@Component("patientEncounterFormProvider")
public class PatientEncounterFormProvider implements FormProvider<PatientEncounterBean, PatientEncounterValidator> {

    @Override
    public PatientEncounterBean makeInstance() {
        return new PatientEncounterBean();
    }

    @Override
    public PatientEncounterValidator getValidator() {
        return new PatientEncounterValidator();
    }

    @Override
    public boolean isFormProviderFor(String formBeanName) {
        return PatientEncounterBean.class.getName().equals(formBeanName);
    }

    @Override
    public boolean isValidatorFor(String validatorName) {
        return PatientEncounterValidator.class.getName().equals(validatorName);
    }
}
