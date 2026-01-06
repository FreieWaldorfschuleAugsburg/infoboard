package de.waldorfaugsburg.infoboard.procurat;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ProcuratGroupService {

    @GET("groups/{groupId}/members")
    Call<List<ProcuratGroupMembership>> findMembers(@Path("groupId") int groupId);

}
