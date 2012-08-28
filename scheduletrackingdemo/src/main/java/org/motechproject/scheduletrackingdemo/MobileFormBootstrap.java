package org.motechproject.scheduletrackingdemo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.mobileforms.api.domain.Form;
import org.motechproject.mobileforms.api.domain.FormGroup;
import org.motechproject.mobileforms.api.service.MobileFormsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.google.gson.reflect.TypeToken;

/**
 * Bootstraps mobile form functionality by using the {@link MobileFormsService}
 * to register new form definitions for download
 */
public class MobileFormBootstrap {

    private final MobileFormsService mobileFormService;
    private MotechJsonReader motechJsonReader;
    private static final String XFORMS_FOLDER = "xforms";

    @Autowired
    public MobileFormBootstrap(MobileFormsService mobileFormsService) {
        this.mobileFormService = mobileFormsService;
        this.motechJsonReader = new MotechJsonReader();
    }

    @SuppressWarnings("unchecked")
    public void bootstrapForms() throws IOException {
        // first load the form group configuration file
        // this defines the xforms that make up the form group, as well
        // as the files where the xforms reside
        ClassPathResource xformsResource = new ClassPathResource("forms-config.json");
        TypeToken<List<FormGroup>> type = new TypeToken<List<FormGroup>>() {
        };

        List<FormGroup> configuredFormGroups = (List<FormGroup>) motechJsonReader.readFromStream(
                xformsResource.getInputStream(), type.getType());
        
        for(FormGroup group : configuredFormGroups) {
            for(Form form : group.getForms()) {
                ClassPathResource xformFile = new ClassPathResource(XFORMS_FOLDER + File.separator + form.fileName());
                String xform = IOUtils.toString(xformFile.getInputStream());
                form.setContent(xform);
            }
            
            //mobileFormService.addFormGroup(group);
        }
    }

}
