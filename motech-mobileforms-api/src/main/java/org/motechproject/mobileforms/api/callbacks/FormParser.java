package org.motechproject.mobileforms.api.callbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fcitmuk.epihandy.DeserializationListenerAdapter;
import org.fcitmuk.epihandy.FormData;
import org.fcitmuk.epihandy.StudyData;
import org.motechproject.MotechException;
import org.motechproject.mobileforms.api.domain.Form;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.parser.FormDataParser;
import org.motechproject.mobileforms.api.repository.AllMobileForms;
import org.motechproject.mobileforms.api.service.FormProvider;
import org.motechproject.mobileforms.api.utils.MapToBeanConvertor;
import org.motechproject.mobileforms.api.vo.Study;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormParser extends DeserializationListenerAdapter {
    private final Logger log = LoggerFactory.getLogger(FormParser.class);

    private List<Study> studies;

    private FormDataParser parser;

    private MapToBeanConvertor mapToBeanConvertor;

    private AllMobileForms allMobileForms;

    private String marker;
        
    List<FormProvider> formProviders;

    public FormParser() {
    }

    public FormParser(FormDataParser parser, MapToBeanConvertor mapToBeanConvertor, AllMobileForms allMobileForms, String marker) {
        this.parser = parser;
        this.mapToBeanConvertor = mapToBeanConvertor;
        this.allMobileForms = allMobileForms;
        this.marker = marker;
    }

    @Override
    public void processingStudy(StudyData studyData) {
        studies.add(new Study());
    }

    @Override
    public void formProcessed(StudyData studyData, FormData formData, String formXml) {
        try {
            Map<String, String> data = parser.parse(formXml);
            Form form = allMobileForms.getFormByName(data.get(marker));
            FormBean formBean = null;
            
            for(FormProvider<?, ?> provider : formProviders) {
                if (provider.isFormProviderFor(form.bean())) {
                    formBean = provider.makeInstance();
                    formBean.setValidator(form.validator());
                    formBean.setFormname(form.name());
                    formBean.setStudyName(form.studyName());
                    formBean.setXmlContent(formXml);
                    formBean.setDepends(form.getDepends());
                    break;
                }
            }
            
            if (formBean != null) {
                mapToBeanConvertor.convert(formBean, handleEmptyStrings(data));
                studies.get(studies.size() - 1).addForm(formBean);
            } else {
                log.warn("Did not find a provider that supports form bean: " + form.bean());
            }

        } catch (Exception e) {
            throw new MotechException("Exception occurred while parsing form xml", e);
        }
    }

    @Override
    public void start() {
        studies = new ArrayList<Study>();
    }

    private Map<String, String> handleEmptyStrings(Map<String, String> attributes) {
        Map<String, String> attributeWithOutEmptyStringValue = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue())) {
                attributeWithOutEmptyStringValue.put(entry.getKey(), entry.getValue());
            }
        }
        return attributeWithOutEmptyStringValue;
    }

    public List<Study> getStudies() {
        return studies;
    }
}


