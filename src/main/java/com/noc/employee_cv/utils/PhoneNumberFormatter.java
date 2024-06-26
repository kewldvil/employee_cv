package com.noc.employee_cv.utils;


public class PhoneNumberFormatter {

    public static String updatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            String[] phoneList = phoneNumber.split("/");
            StringBuilder newPhoneString = new StringBuilder();

            for (String phone : phoneList) {
                phone = phone.trim();

                // Validate phone number format (9 or 10 digits)
                if (phone.matches("\\d{9,10}")) {
                    if (!newPhoneString.isEmpty()) {
                        newPhoneString.append(" / ");
                    }
                    String formattedPhone = formatPhoneNumber(phone);
                    if (formattedPhone != null) {
                        newPhoneString.append(formattedPhone);
                    } else {
                        return ""; // Return empty string if any segment is not a valid phone number
                    }
                } else {
                    return ""; // Return empty string if any segment is not a valid phone number
                }
            }

            return newPhoneString.toString(); // Return the formatted phone numbers
        } else {
            return ""; // Return empty string if phoneNumber is undefined, null, or empty
        }
    }

    private static String formatPhoneNumber(String phoneNumber) {
        // Remove all non-numeric characters
        String cleaned = phoneNumber.replaceAll("\\D", "");

        // Check for different lengths and formats
        String formatted = String.format("(%s) %s-%s", cleaned.substring(0, 3), cleaned.substring(3, 6), cleaned.substring(6));
        if (cleaned.matches("\\d{9}")) {
            return formatted;
        } else if (cleaned.matches("\\d{10}")) {
            return formatted;
        }

        return null; // Return null if the phone number doesn't match any expected formats
    }
}


