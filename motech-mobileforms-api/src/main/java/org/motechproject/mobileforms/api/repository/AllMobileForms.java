package org.motechproject.mobileforms.api.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.mobileforms.api.domain.Form;
import org.motechproject.mobileforms.api.domain.FormGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllMobileForms extends MotechBaseRepository<FormGroup> {
    private static final Logger LOGGER = Logger.getLogger(AllMobileForms.class);

    @Autowired
    protected AllMobileForms(@Qualifier("motech-mobileforms-connector") CouchDbConnector db) {
        super(FormGroup.class, db);
    }

    public List<FormGroup> getAllFormGroups() {
        return getAll();
    }

    @View(name = "by_index", map = "function(doc) { if(doc.type === 'FormGroup') emit(doc.groupIndex); }")
    public FormGroup getFormGroup(Integer index) {
        List<FormGroup> groups = queryView("by_index", index);
        if (groups.size() == 0) {
            return null;
        } else if (groups.size() > 1) {
            LOGGER.warn("There are multiple Form Groups with the same group index. The group index should be unique for each Form Group");
            LOGGER.warn("Selecting first Form Group");
        }
        
        return groups.get(0);
    }

    public Form getFormByName(String formName) {
        for (FormGroup formGroup : getAllFormGroups())
            for (Form form : formGroup.getForms())
                if (form.name().equalsIgnoreCase(formName)) return form;
        return null;
    }
}
