package de.waldorfaugsburg.infoboard.procurat;

import com.google.gson.JsonObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@ToString
public final class ProcuratGroupMembership {

    private int id;
    private int groupId;
    private int personId;
    private String entryDate;
    private String exitDate;
    private JsonObject jsonData;
    private String grade;

}
