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
  private final int id;

  /** The response <b>type</b>. */
  private final Type responseType;

  private final Class<E> typeToken;

  private final List<E> body;

  /**
   * Unsafe constructor accepting any given request reference ID.
   *
   * @param id
   * @param responseType
   * @param body
   * @param contentType
   * @throws IllegalArgumentException
   */
  @JsonIgnore
  ApiResponse(int id, Type responseType, List<E> body, Class<E> contentType) {
    if (responseType == null)
      throw new IllegalArgumentException(
          "ApiResponse(): responseType required, given: " + responseType);

    this.id = id;
    this.responseType = responseType;
    this.body = body;

    this.typeToken = contentType;
  }

  /**
   * Safer constructor
   *
   * @param reference
   * @param responseType
   * @param body
   */
  @JsonIgnore
  public ApiResponse(ApiRequest reference, Type responseType, List<E> body) {
    this(reference.id, responseType, body, reference.expectedReturnType);
  }

  @JsonCreator
  public ApiResponse(
      @JsonProperty("id") int id,
      @JsonProperty("response") String responseType,
      @JsonProperty("body") List<E> body,
      @JsonProperty("content-type") Class<E> contentType) {
    this(id, Type.valueOf(responseType), body, contentType);
  }

  @JsonProperty("id")
  public int getId() {
    return id;
  }

  @JsonProperty("response")
  public Type getResponseType() {
    return responseType;
  }

  @JsonProperty("body")
  public List<E> getBody() {
    return body;
  }

  @JsonProperty("content-type")
  public Class<E> getTypeToken() {
    return typeToken;
  }

  public enum Type {
    SUCCESS,
    ERROR,
    COMPLETION
  }
}
