package org.motechproject.mobileforms.api.web;

import static ch.lambdaj.Lambda.collect;
import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.on;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fcitmuk.epihandy.EpihandyXformSerializer;
import org.motechproject.mobileforms.api.callbacks.FormGroupPublisher;
import org.motechproject.mobileforms.api.callbacks.FormParser;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.domain.FormBeanGroup;
import org.motechproject.mobileforms.api.domain.FormGroupValidator;
import org.motechproject.mobileforms.api.domain.FormOutput;
import org.motechproject.mobileforms.api.parser.FormDataParser;
import org.motechproject.mobileforms.api.repository.AllMobileForms;
import org.motechproject.mobileforms.api.service.MobileFormsService;
import org.motechproject.mobileforms.api.utils.MapToBeanConvertor;
import org.motechproject.mobileforms.api.vo.Study;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;

@Controller
public class FormUploadServlet extends BaseFormServlet {

    private static final Logger log = LoggerFactory.getLogger(FormUploadServlet.class);

    private final FormGroupValidator formGroupValidator;
    private final FormGroupPublisher formGroupPublisher;
    private final AllMobileForms allMobileForms;
    private final String marker = "formname";
    private MobileFormsService mobileFormsService;

    @Autowired
    public FormUploadServlet(FormGroupValidator formGroupValidator, FormGroupPublisher formGroupPublisher,
            AllMobileForms allMobileForms, MobileFormsService mobileFormsService) {
        this.formGroupValidator = formGroupValidator;
        this.formGroupPublisher = formGroupPublisher;
        this.allMobileForms = allMobileForms;
        this.mobileFormsService = mobileFormsService;
    }

    @RequestMapping(value = "/formupload", method = RequestMethod.POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        ZOutputStream zOutput = new ZOutputStream(response.getOutputStream(), JZlib.Z_BEST_COMPRESSION);
        DataInputStream dataInput = new DataInputStream(request.getInputStream());
        DataOutputStream dataOutput = new DataOutputStream(zOutput);
        FormOutput formOutput = getFormOutput();
        try {
            readParameters(dataInput);
            readActionByte(dataInput);
            List<Study> studies = extractBeans(dataInput);
            List<FormBean> allForms = flatten(collect(studies, on(Study.class).forms()));
            for (Study study : studies) {
                for (FormBeanGroup group : study.groupedForms()) {
                    formGroupValidator.validate(group, allForms);
                    formGroupPublisher.publish(new FormBeanGroup(new FormBeanGroup(group.validForms())
                            .sortByDependency()));
                }
                formOutput.addStudy(study);
            }
            response.setContentType(APPLICATION_OCTET_STREAM);
            formOutput.writeFormErrors(dataOutput);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            log.error("Error in uploading form:", e);
            dataOutput.writeByte(RESPONSE_ERROR);
            throw new ServletException(FAILED_TO_SERIALIZE_DATA, e);
        } finally {
            dataOutput.flush();
            zOutput.finish();
            response.flushBuffer();
        }
    }

    private List<Study> extractBeans(DataInputStream dataInput) throws Exception {
        EpihandyXformSerializer serializer = serializer();
        FormParser formParser = createFormProcessor();
        serializer.addDeserializationListener(formParser);
        serializer.deserializeStudiesWithEvents(dataInput, mobileFormsService.getFormIdMap());
        return formParser.getStudies();
    }

    protected FormOutput getFormOutput() {
        return new FormOutput();
    }

    protected FormParser createFormProcessor() {
        return new FormParser(new FormDataParser(), new MapToBeanConvertor(), allMobileForms, marker);
    }
}
