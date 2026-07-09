package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestionResponse {
    private List<Suggestion> suggestions;

    public List<Suggestion> getSuggestions() { return suggestions; }
    public void setSuggestions(List<Suggestion> suggestions) { this.suggestions = suggestions; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Suggestion {
        private String value;
        private String unrestrictedValue;
        private Object data;  // Используем Object, чтобы игнорировать внутреннюю структуру

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getUnrestrictedValue() { return unrestrictedValue; }
        public void setUnrestrictedValue(String unrestrictedValue) { this.unrestrictedValue = unrestrictedValue; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}