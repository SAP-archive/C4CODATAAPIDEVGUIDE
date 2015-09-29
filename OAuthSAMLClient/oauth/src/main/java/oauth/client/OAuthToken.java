package main.java.oauth.client;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"access_token",
	"expires_in",
	"token_type",
	"scope"
})
public class OAuthToken {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("scope")
    private String scope;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("access_token")
    public String getAccessToken() {
	return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
    }

    @JsonProperty("expires_in")
    public Integer getExpiresIn() {
	return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(Integer expiresIn) {
	this.expiresIn = expiresIn;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
	return tokenType;
    }

    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
	this.tokenType = tokenType;
    }

    @JsonProperty("scope")
    public String getScope() {
	return scope;
    }

    @JsonProperty("scope")
    public void setScope(String scope) {
	this.scope = scope;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
    }

}
