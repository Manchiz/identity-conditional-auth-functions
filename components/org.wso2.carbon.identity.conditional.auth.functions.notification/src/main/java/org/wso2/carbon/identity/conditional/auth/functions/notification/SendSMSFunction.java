package org.wso2.carbon.identity.conditional.auth.functions.notification;

import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticatedUser;

import java.util.Map;

public interface SendSMSFunction {

    public boolean sendSMS(JsAuthenticatedUser user, String templateId, Map<String, String> paramMap);
}
