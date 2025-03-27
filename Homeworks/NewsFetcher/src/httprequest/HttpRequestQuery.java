package httprequest;

import exception.syntax.APIRequestSyntaxException;

public interface HttpRequestQuery {

    String getRequestQuery() throws APIRequestSyntaxException;

}