package com.noc.employee_cv.utils;

public class KhmerNumberUtil {
    private static final char[] KHMER_DIGITS = {'០', '១', '២', '៣', '៤', '៥', '៦', '៧', '៨', '៩'};

    public static String convertToKhmerNumber(int number) {
        if (number==0) return null;
        StringBuilder khmerNumber = new StringBuilder();
        String numberString = String.format("%02d", number); // Format with leading zero if necessary
        for (char digit : numberString.toCharArray()) {
            if (Character.isDigit(digit)) {
                khmerNumber.append(KHMER_DIGITS[Character.getNumericValue(digit)]);
            } else {
                khmerNumber.append(digit);
            }
        }

        return khmerNumber.toString();
    }

    public static String convertToKhmerDayMonthYear(String dateString) {
        if (dateString != null && dateString.length() >= 10) {
            String day = dateString.substring(0, 2);
            String month = dateString.substring(3, 5);
            String year = dateString.substring(6);
            return latinToKhmer(day) + "-" + convertDigitMonthToKhmer(month) + "-" + latinToKhmer(year);
        } else {
            return "";
        }
    }
    public static String convertToKhmerDayMonthYearAndOnlyYear(String dateString,Boolean isOnlyYear) {
        if (dateString != null && dateString.length() >= 10) {
            String day = dateString.substring(0, 2);
            String month = dateString.substring(3, 5);
            String year = dateString.substring(6);
            if(isOnlyYear) {
                return latinToKhmer(year);
            }else{
                return latinToKhmer(day) + "-" + convertDigitMonthToKhmer(month) + "-" + latinToKhmer(year);
            }
        } else {
            return "";
        }
    }


    private static String latinToKhmer(String latinString) {
        // Implement your logic to convert Latin digits to Khmer digits here
        // For example, a simple conversion can be done like this:
        // Replace each digit with its Khmer counterpart
        return latinString.replaceAll("0", "០")
                .replaceAll("1", "១")
                .replaceAll("2", "២")
                .replaceAll("3", "៣")
                .replaceAll("4", "៤")
                .replaceAll("5", "៥")
                .replaceAll("6", "៦")
                .replaceAll("7", "៧")
                .replaceAll("8", "៨")
                .replaceAll("9", "៩");
    }

    private static String convertDigitMonthToKhmer(String month) {
        // Assuming month is in Latin digits (e.g., "01" to "12")
        // Convert to Khmer representation if necessary
        // This method depends on your specific requirements for converting months
        // You can use a switch-case or a mapping based on your needs
        // For example:
        return switch (month) {
            case "01" -> "មករា";
            case "02" -> "កុម្ភៈ";
            case "03" -> "មីនា";
            case "04" -> "មេសា";
            case "05" -> "ឧសភា";
            case "06" -> "មិថុនា";
            case "07" -> "កក្តដា";
            case "08" -> "សីហា";
            case "09" -> "កញ្ញា";
            case "10" -> "តុលា";
            case "11" -> "វិច្ឆិកា";
            case "12" -> "ធ្នូ";
            default -> month; // Return the original month if no conversion is needed
        };
    }
    // Mapping of Khmer numerals to Latin numerals
    private static final char[] KHMER_NUMERALS = {'០', '១', '២', '៣', '៤', '៥', '៦', '៧', '៨', '៩'};
    private static final char[] LATIN_NUMERALS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static String convertKhmerToLatin(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        for (char ch : input.toCharArray()) {
            boolean isReplaced = false;
            for (int i = 0; i < KHMER_NUMERALS.length; i++) {
                if (ch == KHMER_NUMERALS[i]) {
                    result.append(LATIN_NUMERALS[i]);
                    isReplaced = true;
                    break;
                }
            }
            if (!isReplaced) {
                result.append(ch); // Append the character as is if it's not a Khmer numeral
            }
        }
        return result.toString();
    }

}
