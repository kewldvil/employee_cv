package com.noc.employee_cv.enums;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/enum")
public class EnumController {

    @GetMapping("/get-rank-position")
    public List<List<String>> getPoliceRankings() {

            List<List<String>> outerList = new ArrayList<>();
//            list of police ran index 0
            List<String> policeRankList = new ArrayList<>();
            for (PoliceRank p:PoliceRank.values()) {
                policeRankList.add(p.getValue());
            }
            outerList.add(policeRankList);
//=========
        //list of position index1
            List<String> positionList = new ArrayList<>();
            for (Position p:Position.values()) {
                positionList.add(p.value);
            }
            outerList.add(positionList);

//            bloodtype index2
        List<String> bloodTypeList = new ArrayList<>();
        for (BloodType p:BloodType.values()) {
            bloodTypeList.add(p.bloodType);
        }
        outerList.add(bloodTypeList);

//        skill level index3
        List<String> skillLevelList = new ArrayList<>();
        for (SkillLevel p:SkillLevel.values()) {
            skillLevelList.add(p.levelName);
        }
        outerList.add(skillLevelList);
//        foreign language index4
        List<String> foreignLanguageList = new ArrayList<>();
        for (ForeignLang p:ForeignLang.values()) {
            foreignLanguageList.add(p.language);
        }
        outerList.add(foreignLanguageList);
//        general department index5
        List<String> departmentList = new ArrayList<>();
        for (GeneralDepartment p:GeneralDepartment.values()) {
            departmentList.add(p.departmentName);
        }
        outerList.add(departmentList);
// get university major index 6
        List<String> majorList = new ArrayList<>();
        for (UniversityMajor p:UniversityMajor.values()) {
            majorList.add(p.majorSkill);
        }
        outerList.add(majorList);

            return outerList;
    }


}
