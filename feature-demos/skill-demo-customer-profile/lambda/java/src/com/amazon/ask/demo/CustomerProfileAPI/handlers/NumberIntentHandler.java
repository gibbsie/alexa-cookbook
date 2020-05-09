package com.amazon.ask.demo.CustomerProfileAPI.handlers;

import com.amazon.ask.demo.CustomerProfileAPI.Constants;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ups.PhoneNumber;
import com.amazon.ask.model.services.ups.UpsServiceClient;

import java.util.Arrays;
import java.util.Optional;

public class NumberIntentHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("NumberIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        String consentToken = handlerInput.getRequestEnvelope().getContext().getSystem().getApiAccessToken();
        if (consentToken != null) {
            UpsServiceClient customerProfileServiceClient = handlerInput.getServiceClientFactory().getUpsService();
            PhoneNumber number = customerProfileServiceClient.getProfileMobileNumber();
            String phoneNumber = number.getCountryCode() + " " + number.getPhoneNumber();
            if (number == null) {
                return handlerInput.getResponseBuilder()
                        .withSpeech(Constants.NUMBER_MISSING)
                        .build();
            } else {
                return handlerInput.getResponseBuilder()
                        .withSpeech(Constants.NUMBER_AVAILABLE + phoneNumber)
                        .build();
            }
        } else {
            return handlerInput.getResponseBuilder()
                    .withSpeech(Constants.NOTIFY_MISSING_PERMISSIONS)
                    .withAskForPermissionsConsentCard(Arrays.asList(Constants.NAME_PERMISSION, Constants.EMAIL_PERMISSION, Constants.MOBILE_NUMBER_PERMISSION))
                    .build();
        }
    }
}