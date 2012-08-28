package org.motechproject.mobileforms.api.web;

import org.fcitmuk.epihandy.EpihandyXformSerializer;
import org.motechproject.mobileforms.api.callbacks.FormGroupPublisher;
import org.motechproject.mobileforms.api.callbacks.FormParser;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.motechproject.mobileforms.api.domain.FormGroupValidator;
import org.motechproject.mobileforms.api.domain.FormOutput;
import org.motechproject.mobileforms.api.parser.FormDataParser;
import org.motechproject.mobileforms.api.repository.AllMobileForms;
import org.motechproject.mobileforms.api.service.MobileFormsService;
import org.motechproject.mobileforms.api.service.UsersService;
import org.motechproject.mobileforms.api.utils.MapToBeanConvertor;
import org.motechproject.mobileforms.api.validator.FormValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseFormServlet {

    public static final byte RESPONSE_ERROR = 0;
    public static final byte RESPONSE_SUCCESS = 1;

    public static final String FAILED_TO_SERIALIZE_DATA = "failed to serialize data";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    protected EpihandyXformSerializer serializer() {
        return new EpihandyXformSerializer();
    }

    protected void readParameters(DataInputStream dataInput) throws IOException {
        String name = dataInput.readUTF();
        String password = dataInput.readUTF();
        String serializer = dataInput.readUTF();
        String locale = dataInput.readUTF();
    }

    protected byte readActionByte(DataInputStream dataInput) throws IOException {
        return dataInput.readByte();
    }
}
