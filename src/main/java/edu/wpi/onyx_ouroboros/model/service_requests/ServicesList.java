package edu.wpi.onyx_ouroboros.model.service_requests;

import lombok.Getter;
import lombok.Setter;

public class ServicesList {

    public static class Service {

        private static final String fxmlPrefix = "views/service_requests/";

        @Getter
        @Setter
        private String descriptionLangModelField;
        @Getter
        private final String fxmlFile;

        public Service(String descriptionLangModelField, String fxmlFile) {
            this.descriptionLangModelField = descriptionLangModelField;
            this.fxmlFile = fxmlFile;
        }
    }

    public static Service[] services = {};
}
