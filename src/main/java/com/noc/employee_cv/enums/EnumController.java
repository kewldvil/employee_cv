package com.noc.employee_cv.enums;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enum")
public class EnumController {

    @GetMapping("/police-rank")
    public <E extends Enum<E>>List<Map<Enum<PoliceRank>, String>> getPoliceRankings() {
        return getEnumKeyValuePairs(PoliceRank.class);
    }
    @GetMapping("/position")
    public <E extends Enum<E>>List<Map<Enum<Position>, String>> getPosition() {
        return getEnumKeyValuePairs(Position.class);
    }
    @GetMapping("/blood-type")
    public <E extends Enum<E>>List<Map<Enum<BloodType>, String>> getBloodType() {
        return getEnumKeyValuePairs(BloodType.class);
    }
    @GetMapping("/skill-level")
    public <E extends Enum<E>>List<Map<Enum<SkillLevel>, String>> getSkillLevel() {
        return getEnumKeyValuePairs(SkillLevel.class);
    }
    @GetMapping("/foreign-language")
    public <E extends Enum<E>>List<Map<Enum<ForeignLang>, String>> getForeignLang() {
        return getEnumKeyValuePairs(ForeignLang.class);
    }
    @GetMapping("/general-department")
    public <E extends Enum<E>>List<Map<Enum<GeneralDepartment>, String>> getGeneralDepartment() {
        return getEnumKeyValuePairs(GeneralDepartment.class);
    }
    @GetMapping("/university-major")
    public <E extends Enum<E>>List<Map<Enum<UniversityMajor>, String>> getUniversityMajor() {
        return getEnumKeyValuePairs(UniversityMajor.class);
    }
    public static <E extends Enum<E>> List<Map<Enum<E>, String>> getEnumKeyValuePairs(Class<E> enumClass) {
        List<Map<Enum<E>, String>> resultList = new ArrayList<>();
        // Iterate over enum constants
        for (E enumConstant : enumClass.getEnumConstants()) {
            // Create a map for each enum constant
            Map<Enum<E>, String> map = new HashMap<>();
            // Put the key and value into the map
            CommonEnum keyValueEnum = (CommonEnum) enumConstant;
            map.put(enumConstant, keyValueEnum.getValue());
            resultList.add(map);
        }

        return resultList;
    }

}
