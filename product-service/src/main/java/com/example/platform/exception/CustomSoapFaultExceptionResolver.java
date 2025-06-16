package com.example.platform.exception;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

public class CustomSoapFaultExceptionResolver extends SoapFaultMappingExceptionResolver {
    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        SoapFaultDetail detail = fault.addFaultDetail();

        String errorCode;
        if (ex instanceof ProductNotFoundException) {
            errorCode = "PRODUCT_NOT_FOUND";
        } else if (ex instanceof ProductValidationException) {
            errorCode = "PRODUCT_VALIDATION_FAILED";
        } else {
            errorCode = "INTERNAL_ERROR";
        }

        QName name = new QName("http://example.com/fault", "FaultDetails");

        detail.addFaultDetailElement(name).addText(errorCode);
    }
}
