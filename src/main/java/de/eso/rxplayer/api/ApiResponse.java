package de.eso.rxplayer.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ApiResponse<E> {
  /**
   * Response identifier number: This ID is identical to the ID of the ApiRequest that caused this
   * ApiResponse. Since a single ApiRequest can cause multiple ApiResponses to be sent, it is <b>not
   * necessarily unique</b>.
   *
   * <p>If the ApiResponse object is not caused by a client ApiRequest (i.e., represents a server
   * request or notification), it is guaranteed to not correspond to any previous client request.
   */
  public final int id;
  /** The response <b>type</b>. */
  public final String response;

  public final List<E> body;

  /**
   * Unsafe constructor accepting a given ID (request reference).
   *
   * @param id
   * @param responseType
   * @param body
   * @throws IllegalArgumentException
   */
  @JsonIgnore
  ApiResponse(int id, Type responseType, List<E> body) {
    if (responseType == null)
      throw new IllegalArgumentException(
          "ApiResponse(): responseType required, given: " + responseType);

    this.id = id;
    this.response = responseType.toString();
    this.body = body;
  }

  @JsonIgnore
  public ApiResponse(ApiRequest reference, Type responseType, List<E> body) {
    this(reference.id, responseType, body);
  }

  @JsonCreator
  public ApiResponse(
      @JsonProperty("id") int id,
      @JsonProperty("response") String responseType,
      @JsonProperty("body") List<E> params) {
    this(id, Type.valueOf(responseType), params);
  }

  public enum Type {
    SUCCESS,
    ERROR,
    COMPLETION
  }
}
