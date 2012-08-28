package org.motechproject.mobileforms.api.service;

import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.validator.FormValidator;

public interface FormProvider<T extends FormBean, V extends FormValidator<? extends FormBean>> {

    T makeInstance();

    V getValidator();

    boolean isFormProviderFor(String formBeanName);
    
    boolean isValidatorFor(String validatorName);
}
