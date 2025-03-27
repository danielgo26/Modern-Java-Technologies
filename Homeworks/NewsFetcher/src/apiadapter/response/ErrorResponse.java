package apiadapter.response;

import com.google.gson.Gson;
import validator.object.ObjectValidator;

public record ErrorResponse(String status, String code, String message) {

    public static ErrorResponse of(String dataJson) {
        ObjectValidator.validateNotNull(dataJson,
            "The given data expected to be a in a valid json format, but is null!");

        return new Gson().fromJson(dataJson, ErrorResponse.class);
    }

}