package de.eso.rxplayer.vertx.client;

import de.eso.rxplayer.api.ApiRequest;
import java.util.HashMap;
import java.util.Map;

public class ClientRequest extends ApiRequest {

  public static final ApiRequest.Type DEFAULT_TYPE = ApiRequest.Type.QUERY;

  @SuppressWarnings("unchecked")
  public ClientRequest(
      final int id, final Type type, final String target, final Map<String, ?> params) {
    this.id = id;
    this.request = type.toString();
    this.target = target;

    // This cast should never fail because *everything* extends Object
    this.params = (Map<String, Object>) params;
  }

  public ClientRequest(final int id, final Type type, String singleMethodToInvoke) {
    this(id, type, singleMethodToInvoke, new HashMap<String, Object>());
  }

  public ClientRequest(final int id, String singleMethodToInvoke) {
    this(id, DEFAULT_TYPE, singleMethodToInvoke);
  }
}
