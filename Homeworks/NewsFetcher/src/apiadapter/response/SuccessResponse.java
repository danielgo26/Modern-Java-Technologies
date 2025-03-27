package apiadapter.response;

import com.google.gson.Gson;
import news.NewsHeadline;
import validator.object.ObjectValidator;

import java.util.List;

public record SuccessResponse(String status, int totalResults, List<NewsHeadline> articles) {

    public static SuccessResponse of(String dataJson) {
        ObjectValidator.validateNotNull(dataJson,
            "The given data expected to be a in a valid json format, but is null!");

        return new Gson().fromJson(dataJson, SuccessResponse.class);
    }

}