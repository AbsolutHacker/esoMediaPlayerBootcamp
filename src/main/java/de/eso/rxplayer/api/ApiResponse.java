package de.eso.rxplayer.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ApiResponse {
  /**
   * Response identifier number:
   * This ID is identical to the ID of the ApiRequest that caused this ApiResponse.
   * Since a single ApiRequest can cause multiple ApiResponses to be sent, it is
   * <b>not necessarily unique</b>.
   *
   * If the ApiResponse object is not caused by a client ApiRequest (i.e., represents
   * a server request or notification), it is guaranteed to not correspond to any previous
   * client request.
   */
  public int id;
  /**
   * The response <b>type</b>.
   */
  public String response;
  public Map<String,Object> params;

  /**
   * Unsafe constructor accepting a given ID (request reference).
   * @param id
   * @param responseType
   * @param body
   * @throws IllegalArgumentException
   */
  @JsonIgnore
  ApiResponse(int id, Type responseType, Map<String,Object> body) {
    if (responseType == null)
      throw new IllegalArgumentException("ApiResponse(): responseType required, given: " + responseType);

    this.id = id;
    this.response = responseType.toString();
    this.params = body;
  }

  @JsonIgnore
  public ApiResponse(ApiRequest reference, Type responseType, Map<String, Object> body) {
    this(reference.id, responseType, body);
  }

  @JsonCreator
  public ApiResponse(
      @JsonProperty("id") int id,
      @JsonProperty("response") String responseType,
      @JsonProperty("params") Map<String, Object> params
  ) {
    this(id, Type.valueOf(responseType), params);
  }

  public enum Type {
    SUCCESS,
    ERROR,
    COMPLETION
  }

}
