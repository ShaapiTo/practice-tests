package dto;

public class SuggestionRequest {
    private String query;
    private Integer count;

    public SuggestionRequest(String query) {
        this.query = query;
    }

    public SuggestionRequest(String query, Integer count) {
        this.query = query;
        this.count = count;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}