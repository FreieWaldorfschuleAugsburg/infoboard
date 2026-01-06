package de.waldorfaugsburg.infoboard.procurat;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@ToString
public final class ProcuratPerson {

    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private int addressId;
    private int familyId;
    private String familyRole;
    private String birthDate;
    private String birthPlace;
    private int birthCountryId;
    private int languageId;
    private int religionId;
    private String allFirstNames;
    private String email;
    private String birthName;
    private String academicTitle;
    private String namePrefix;
    private String nobilityTitle;
    private String salutationA;
    private String salutationB;
    private String jobTitle;
    private String comment;
    private int nationalityId;
    private String maritalStatus;
    private String deathDate;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
