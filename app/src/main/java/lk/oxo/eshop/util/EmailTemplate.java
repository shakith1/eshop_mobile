package lk.oxo.eshop.util;

public class EmailTemplate {

    public static String getEmailTemplate(String fname,String base64Image){
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Welcome to eShop</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .message {\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.6;\n" +
                "        }\n" +
                "        .data {\n" +
                "            font-size: 14px;\n" +
                "            line-height: 1;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            text-align: center;\n" +
                "            color: #888;\n" +
                "        }\n" +
                "    </style>\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <img src=\"data:image/png;base64,"+base64Image + "\" alt=\"Logo\" style=\"max-width: 200px; height: auto;\">\n" + // Adjust the size here (200px width in this case)
                "            <h1>Welcome to eShop!</h1>\n" +
                "        </div>\n" +
                "        <div class=\"message\">\n" +
                "            <p>Hello "+fname+",</p>\n" +
                "        </div>\n" +
                "        <div class=\"data\">\n" +
                "            <p>We are thrilled to welcome you to our community. Thank you for joining!</p>\n" +
                "            <p>Feel free to explore our platform and reach out if you have any questions.</p>\n" +
                "            <p>Best regards,</p>\n" +
                "            <p>eShop Team</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>© 2023 eShop</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        return template;
    }
}
