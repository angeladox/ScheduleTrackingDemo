package org.motechproject.scheduletrackingdemo.validator;

import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
//import org.motechproject.scheduletrackingdemo.OpenMrsClient;
//import org.motechproject.scheduletrackingdemo.OpenMrsConceptConverter;
import org.motechproject.scheduletrackingdemo.beans.PatientEncounterBean;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.domain.FormBeanGroup;
import org.motechproject.mobileforms.api.domain.FormError;
import org.springframework.beans.factory.annotation.Autowired;

public class PatientEncounterValidator extends
        AbstractPatientValidator<PatientEncounterBean> {

//    @Autowired
//    public PatientEncounterValidator(OpenMrsClient openmrsClient) {
//        super(openmrsClient);
//    }

    // final V formBean, FormBeanGroup formGroup, List<FormBean> allForms

    @Override
    public List<FormError> validate(PatientEncounterBean formBean,
            FormBeanGroup formGroup, List<FormBean> allForms) {
        List<FormError> errors = super.validate(formBean, formGroup, allForms);
        validateOpenMrsPatientExists(formBean.getMotechId(), errors);
        if (errors.size() > 0) {
            // no point in other validation checks if patient doesn't exist in
            // system
            return errors;
        }

//        String conceptName = OpenMrsConceptConverter
//                .convertToNameFromIndex(formBean.getObservedConcept());
//        validateValidNextConcept(formBean.getMotechId(), conceptName,
//                formBean.getObservedDate(), errors);

        return errors;
    }

    private void validateValidNextConcept(String motechId, String conceptName,
            Date fulfilledDate, List<FormError> errors) {
//        String previousConcept = OpenMrsConceptConverter
//                .getConceptBefore(conceptName);
//        if (!previousConcept.equals(conceptName)) {
//            if (!openmrsClient.hasConcept(motechId, previousConcept)) {
//                errors.add(new FormError("observedConcept",
//                        "Patient has not fulfilled previous concept: "
//                                + previousConcept));
//                return;
//            }
//
//            if (openmrsClient.hasConcept(motechId, conceptName)) {
//                errors.add(new FormError("observedConcept",
//                        "Patient already has concept: " + conceptName));
//                return;
//            }
//
//            DateTime lastFulfilledDate = openmrsClient
//                    .lastTimeFulfilledDateTimeObs(motechId, previousConcept);
//            DateTime currentFufilledDate = new DateTime(fulfilledDate);
//            if (currentFufilledDate.isBefore(lastFulfilledDate)) {
//                errors.add(new FormError("observedDate",
//                        "Current fufill date is before last fulfill date"));
//            }
//        }
    }
}
