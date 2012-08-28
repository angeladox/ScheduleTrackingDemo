package org.motechproject.mobileforms.api.domain;

import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.join;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.motechproject.mobileforms.api.service.FormProvider;
import org.motechproject.mobileforms.api.validator.FormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

public class FormGroupValidator {
    private final Logger log = LoggerFactory.getLogger(FormGroupValidator.class);
    private final List<FormProvider> formProviders;

    public FormGroupValidator(List<FormProvider> formProviders) {
        this.formProviders = formProviders;
    }
    
    public void validate(FormBeanGroup formGroup, List<FormBean> allForms) {
        try {
            final List<FormBean> formBeansOrderedByPriority = formGroup.sortByDependency();
            final Map<String, FormBean> formBeansIndexedByName = index(formBeansOrderedByPriority, on(FormBean.class).getFormname());
            for (FormBean formBean : formBeansOrderedByPriority) {
                final List<String> invalidDependentForms = getInvalidDependentForms(formBean, formBeansIndexedByName);
                if (CollectionUtils.isEmpty(invalidDependentForms)) {
                    try {
                        boolean foundProvider = false;
                        for(FormProvider provider : formProviders) {
                            if (provider.isValidatorFor(formBean.getValidator())) {
                                FormValidator<FormBean> validator = provider.getValidator();
                                formBean.addFormErrors(validator.validate(formBean, formGroup, allForms));
                                foundProvider = true;
                            }
                        }
                        
                        if (!foundProvider) {
                            log.warn("Did not find a validator with name: " + formBean.getValidator());
                        }
                    } catch (Exception e) {
                        formBean.addFormError(new FormError("Form Error:" + formBean.getFormname(), "Server exception, contact your administrator"));
                        log.error("Encountered exception while validating form group, " + formGroup.toString(), e);
                    }
                } else {
                    formBean.addFormError(new FormError("Form Error:" + join(invalidDependentForms, ","), "Dependent form failed"));
                }
            }
        } catch (Exception e) {
            formGroup.markAllFormAsFailed("Server exception, contact your administrator");
            log.error("Encountered exception while validating form group, " + formGroup.toString(), e);
        }
    }

    private List<String> getInvalidDependentForms(FormBean formBean, Map<String, FormBean> formBeansIndexedByName) {
        List<String> failedForms = new ArrayList<String>();
        if (formBean.getDepends() != null && !formBean.getDepends().isEmpty()) {
            for (String name : formBean.getDepends()) {
                if (formBeansIndexedByName.get(name) != null && formBeansIndexedByName.get(name).hasErrors()) {
                    failedForms.add(name);
                }
            }
        }
        return failedForms;
    }

}
