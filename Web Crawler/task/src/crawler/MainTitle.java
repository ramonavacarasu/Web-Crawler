package crawler;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTitle {

    public static String getMainTitle(String url) {

        Pattern javaPattern = Pattern.compile("(<title>)(.*)(</title>)");

        try (InputStream inputStream = new BufferedInputStream(new URL(url).openStream())) {
            String siteText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Matcher matcher = javaPattern.matcher(siteText);

            if (matcher.find()) {
                return matcher.group(2);
            }

        } catch (Exception ignored) { }

        return "";

    }

}
