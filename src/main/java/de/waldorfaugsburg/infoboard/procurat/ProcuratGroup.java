package de.waldorfaugsburg.infoboard.procurat;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@ToString
public final class ProcuratGroup {

    private int id;
    private int parentGroupId;
    private String name;
    private String shortName;
    private String type;
    private List<String> grades;
    private String schoolYear;
    private String additionalType;
    private int sortKey;

}
