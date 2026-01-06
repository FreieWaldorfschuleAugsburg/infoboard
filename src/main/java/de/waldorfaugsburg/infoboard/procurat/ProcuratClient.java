package de.waldorfaugsburg.infoboard.procurat;

import com.google.gson.Gson;
import de.waldorfaugsburg.infoboard.InfoboardApplication;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public final class ProcuratClient {

    public static final Gson GSON = new Gson();
    private static final Duration TIMEOUT_DURATION = Duration.ofMinutes(5);

    private final String url;
    private final String apiKey;

    @Getter
    private ProcuratPersonService personService;
    @Getter
    private ProcuratGroupService groupService;

    ProcuratClient(final InfoboardApplication application) {
        this.url = application.getConfiguration().getProcurat().getUrl();
        this.apiKey = application.getConfiguration().getProcurat().getApiKey();
    }

    public static ProcuratClient createInstance(final InfoboardApplication application) throws HttpClientException {
        final ProcuratClient client = new ProcuratClient(application);
        client.setup();
        return client;
    }

    private void setup() {
        final OkHttpClient client = createClient(new OkHttpClient.Builder());
        final Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(client);
        builder.addConverterFactory(GsonConverterFactory.create(GSON));

        final Retrofit retrofit = createRetrofit(builder);

        this.personService = retrofit.create(ProcuratPersonService.class);
        this.groupService = retrofit.create(ProcuratGroupService.class);
    }

    OkHttpClient createClient(final OkHttpClient.Builder clientBuilder) {
        clientBuilder.addInterceptor(chain -> chain.proceed(chain.request().newBuilder().addHeader("X-API-KEY", apiKey).build()));
        clientBuilder.callTimeout(TIMEOUT_DURATION).connectTimeout(TIMEOUT_DURATION).readTimeout(TIMEOUT_DURATION).writeTimeout(TIMEOUT_DURATION);
        return clientBuilder.build();
    }

    Retrofit createRetrofit(final Retrofit.Builder retrofitBuilder) {
        retrofitBuilder.baseUrl(this.url);
        return retrofitBuilder.build();
    }

    public ProcuratPerson getProcuratPersonById(final int id) throws HttpClientException {
        return execute(personService.findById(id));
    }

    public List<ProcuratGroupMembership> getGroupMemberships(final int groupId) throws HttpClientException {
        return execute(groupService.findMembers(groupId));
    }

    public <T> T execute(final Call<T> call) throws HttpClientException {
        try {
            final Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                throw new HttpClientException(response.code(), parseError(response));
            }

            return response.body();
        } catch (final IOException e) {
            throw new HttpClientException(e);
        }
    }

    private ClientError parseError(final Response<?> response) throws IOException {
        final String contentType = response.headers().get("Content-Type");
        if (contentType == null || !contentType.equals("application/json")) {
            throw new RuntimeException("invalid content type: " + contentType);
        }

        final ResponseBody errorBody = response.errorBody();
        if (errorBody == null) {
            throw new RuntimeException("unrecognized error");
        }

        final String rawBody = errorBody.string();
        errorBody.close();
        return GSON.fromJson(rawBody, ClientError.class);
    }

}
