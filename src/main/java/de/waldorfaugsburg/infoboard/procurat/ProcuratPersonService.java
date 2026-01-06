package de.waldorfaugsburg.infoboard.procurat;

import retrofit2.Call;
import retrofit2.http.*;

public interface ProcuratPersonService {

    @GET("persons/{personId}")
    Call<ProcuratPerson> findById(@Path("personId") int personId);

}
