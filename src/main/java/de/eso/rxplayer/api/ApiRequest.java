package de.eso.rxplayer.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * The "POJO" (<i>Plain Old Java Object</i>) class corresponding
 * to a single client request. Client requests are usually encoded
 * as JSON strings before conversion to an ApiRequest.
 */
public class ApiRequest {
  /**
   * The ID number of the request, as chosen by the client.
   * All responses to this request need to refer to this ID.
   * <b>Note:</b> it is the client's responsibility to ensure
   * uniqueness of the ID!
   * Server implementations may silently discard requests
   * with duplicate IDs, replace previous requests or respond
   * with an error.
   */
  public int id;
  /**
   * Abstract type of the request, can be one of:
   * <ul>
   *   <li>
   *     "subscribe"<br />
   *   </li>
   *   <li>"unsubscribe"</li>
   *   <li>"query"</li>
   * </ul>
   */
  public String request;
  /**
   *
   */
  public Map<String,Object> params;

  private Type type;

  @JsonIgnore
  public boolean isValid() {
    if (type == null)
      checkType();
    return (type != Type.INVALID);
  }

  @JsonIgnore
  public Type getType() {
    if (type == null)
      checkType();
    return type;
  }

  private void checkType() {
    /*
    * All requests require at least one parameter, i.e. the target to query/subscribe to;
    * hence, the #params map cannot be empty
    */
    if (request == null || request.isEmpty() || params == null || params.size() == 0) {
      type = Type.INVALID;
    } else {
      try {
        type = Type.valueOf(request.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        type = Type.INVALID;
      }
    }
  }

  public enum Type {
    SUBSCRIBE,
    UNSUBSCRIBE,
    QUERY,
    INVALID
  }

}
