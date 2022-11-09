package org.wso2.carbon.identity.conditional.auth.functions.notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticatedUser;
import org.wso2.carbon.identity.conditional.auth.functions.notification.internal.NotificationFunctionServiceHolder;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.governance.service.notification.NotificationChannels;

import java.util.HashMap;
import java.util.Map;

public class SendSMSFunctionImpl implements SendSMSFunction {

    private static final String TEMPLATE_TYPE = "TEMPLATE_TYPE";

    private static final Log LOG = LogFactory.getLog(SendEmailFunctionImpl.class);


    @Override
    public boolean sendSMS(JsAuthenticatedUser user, String templateId, Map<String, String> paramMap) {

        String eventName = IdentityEventConstants.Event.TRIGGER_SMS_NOTIFICATION;

        HashMap<String, Object> properties = new HashMap<>(paramMap);
        properties.put(IdentityEventConstants.EventProperty.USER_NAME, user.getWrapped().getUserName());
        properties.put(IdentityEventConstants.EventProperty.USER_STORE_DOMAIN, user.getWrapped().getUserStoreDomain());
        properties.put(IdentityEventConstants.EventProperty.TENANT_DOMAIN, user.getWrapped().getTenantDomain());
        properties.put(IdentityEventConstants.EventProperty.NOTIFICATION_CHANNEL, NotificationChannels.SMS_CHANNEL.getChannelType());
        properties.put(TEMPLATE_TYPE, templateId);

        Event identityMgtEvent = new Event(eventName, properties);

        try {
            NotificationFunctionServiceHolder.getInstance().getIdentityEventService().handleEvent(identityMgtEvent);
        } catch (IdentityEventException e) {
            LOG.error(String.format("Error when sending notification of template %s to user %s", user
                    .getWrapped().toFullQualifiedUsername()), e);
            return false;
        }
        return true;
    }
}

