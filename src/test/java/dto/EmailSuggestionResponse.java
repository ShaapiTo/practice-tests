package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailSuggestionResponse {
    private List<EmailSuggestion> suggestions;

    public List<EmailSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<EmailSuggestion> suggestions) {
        this.suggestions = suggestions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmailSuggestion {
        private String value;
        private String unrestrictedValue;
        private EmailData data;  // Теперь это объект

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUnrestrictedValue() {
            return unrestrictedValue;
        }

        public void setUnrestrictedValue(String unrestrictedValue) {
            this.unrestrictedValue = unrestrictedValue;
        }

        public EmailData getData() {
            return data;
        }

        public void setData(EmailData data) {
            this.data = data;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmailData {
        private String local;
        private String domain;
        private String type;
        private String source;
        private String qc;

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getQc() {
            return qc;
        }

        public void setQc(String qc) {
            this.qc = qc;
        }
    }
}