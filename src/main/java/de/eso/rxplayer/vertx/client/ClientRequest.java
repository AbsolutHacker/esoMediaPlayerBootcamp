package de.eso.rxplayer.vertx.client;

import de.eso.rxplayer.api.ApiRequest;

import java.util.HashMap;
import java.util.Map;

public class ClientRequest extends ApiRequest {

  public static final ApiRequest.Type DEFAULT_TYPE = ApiRequest.Type.QUERY;

  @SuppressWarnings("unchecked")
  public ClientRequest(final int id, final Type type, final Map<String,?> params) {
    this.id = id;
    this.request = type.toString();

    // This cast should never fail because *everything* extends Object
    this.params = (Map<String,Object>) params;
  }

  public ClientRequest(final int id, final Type type, String singleMethodToInvoke) {
    this(id, type, new HashMap<String,String>(){{
      put("target", singleMethodToInvoke);
    }});
  }

  public ClientRequest(final int id, String singleMethodToInvoke) {
    this(id, DEFAULT_TYPE, singleMethodToInvoke);
  }

}
