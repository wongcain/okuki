package okuki.sample.common.api.icndb;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IcndbResult {

    @SerializedName("value")
    public List<Joke> jokes = new ArrayList<>();

    public static class Joke {

        @SerializedName("joke")
        public String text;

    }

}
