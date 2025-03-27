package validator.statuscode;

import apiadapter.response.ErrorResponse;
import exception.status.APIStatusException;
import exception.status.BadRequestException;
import exception.status.ServerErrorException;
import exception.status.TooManyRequestException;
import exception.status.UnauthorizedException;
import exception.status.UnsupportedException;

import java.net.http.HttpResponse;

import static validator.object.ObjectValidator.validateNotNull;

public class ResponseStatusCodeValidator {

    private static final int OK_STATUS_CODE = 200;
    private static final int BAD_REQUEST_STATUS_CODE = 400;
    private static final int UNAUTHORIZED_STATUS_CODE = 401;
    private static final int TOO_MANY_REQUEST_STATUS_CODE = 429;
    private static final int SERVER_ERROR_STATUS_CODE = 500;

    public static void validateStatusCode(HttpResponse<String> response) throws APIStatusException {
        validateNotNull(response, "The given HTTP response is null!");

        int statusCode = response.statusCode();

        if (statusCode != OK_STATUS_CODE) {
            String errorMessage = ErrorResponse.of(response.body()).message();
            switch (statusCode) {
                case BAD_REQUEST_STATUS_CODE:
                    throw new BadRequestException(errorMessage);
                case UNAUTHORIZED_STATUS_CODE:
                    throw new UnauthorizedException(errorMessage);
                case TOO_MANY_REQUEST_STATUS_CODE:
                    throw new TooManyRequestException(errorMessage);
                case SERVER_ERROR_STATUS_CODE:
                    throw new ServerErrorException(errorMessage);
                default:
                    throw new UnsupportedException(errorMessage);
            }
        }
    }

}