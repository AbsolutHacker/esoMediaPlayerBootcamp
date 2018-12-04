package de.eso.rxplayer.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * The "POJO" (<i>Plain Old Java Object</i>) class corresponding to a single client request. Client
 * requests are usually encoded as JSON strings before conversion to an ApiRequest.
 */
public class ApiRequest<E> {

  public static final Type DEFAULT_TYPE = Type.QUERY;

  /**
   * The ID number of the request, as chosen by the client. All responses to this request need to
   * refer to this ID. <b>Note:</b> it is the client's responsibility to ensure uniqueness of the
   * ID! Server implementations may silently discard requests with duplicate IDs, replace previous
   * requests or respond with an error.
   */
  private final int id;
  /**
   * Abstract type of the request, can be one of:
   *
   * <ul>
   *   <li>"subscribe"</li>
   *   <li>"unsubscribe"</li>
   *   <li>"query"</li>
   * </ul>
   */
  private final Type type;

  private final String target;

  private final Class<E> expectedReturnType;
  /** */
  private final Map<String, Object> params;


  @JsonCreator
  public ApiRequest(
      @JsonProperty("id") final int id,
      @JsonProperty("request") final Type type,
      @JsonProperty("target") final String target,
      @JsonProperty("params") final Map<String, ?> params,
      @JsonProperty("expected-return-type") final Class<E> expectedReturnType) {
    this.id = id;
    this.type = type;
    this.target = target;
    this.expectedReturnType = expectedReturnType;
    // This cast should never fail because *everything* extends Object
    this.params = (Map<String, Object>) params;
  }

  @JsonIgnore
  public ApiRequest(
      final int id, final Type type, String singleMethodToInvoke, Class<E> returnType) {
    this(id, type, singleMethodToInvoke, new HashMap<>(), returnType);
  }

  @JsonIgnore
  public ApiRequest(final int id, String singleMethodToInvoke, Class<E> returnType) {
    this(id, DEFAULT_TYPE, singleMethodToInvoke, returnType);
  }

//  @JsonIgnore
//  public boolean isValid() {
//    if (type == null) checkType();
//    return (type != Type.INVALID);
//  }

  @JsonProperty("id")
  public int getId() {
    return id;
  }

  @JsonProperty("request")
  public Type getType() {
    return type;
  }

  @JsonProperty("target")
  public String getTarget() {
    return target;
  }

  @JsonProperty("params")
  public Map<String,Object> getParameters() {
    return params;
  }

  @JsonProperty("target")
  public Class<E> getExpectedReturnType() {
    return expectedReturnType;
  }
//
//  private void checkType() {
//    if (request == null || request.isEmpty() || params == null) {
//      type = Type.INVALID;
//    } else {
//      try {
//        type = Type.valueOf(request.trim().toUpperCase());
//      } catch (IllegalArgumentException e) {
//        type = Type.INVALID;
//      }
//    }
//  }

  public enum Type {
    SUBSCRIBE,
    UNSUBSCRIBE,
    QUERY,
    INVALID
  }
}
