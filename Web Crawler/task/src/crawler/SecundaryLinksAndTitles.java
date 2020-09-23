package crawler;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecundaryLinksAndTitles extends SwingWorker<Void, Integer> {

    String url;
    static Set<String> allLinks = new HashSet<>();
    static Set<String> validLinks = new HashSet<>();

    public SecundaryLinksAndTitles(String url) {
        this.url = url;
    }

    @Override
    public Void doInBackground() throws Exception {

        String title;

        try {
            URLConnection connection = new URL(url).openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String siteText;

            while ((siteText = reader.readLine()) != null) {

                Matcher matcherLink = Pattern.compile("(?<=href=[\"']).*(?=[\"']>)").matcher(siteText);

                if (matcherLink.find()) {

                    allLinks.add(matcherLink.group());

                }
            }

            int count = 0;

            for (String link : allLinks) {

                count++;

                MakeAbsoluteLink.makeAbsoluteLink(link, url);
                title = MainTitle.getMainTitle(link);
                if (!title.equals("")) {
                    validLinks.add(link);
                    WebCrawler.model.addRow(new Object[]{link, title});
                }
                WebCrawler.progressBar.setValue(100 * count / (allLinks.size() - 1));
            }

            for (String link : validLinks) {
                SecundaryLinksAndTitles secundaryLinksAndTitles = new SecundaryLinksAndTitles(link);
                secundaryLinksAndTitles.execute();
            }


        } catch (IOException ignored) {
        }
        WebCrawler.textPages.setText(String.valueOf(validLinks));
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        WebCrawler.progressBar.setValue(chunks.get(chunks.size()-1));
        WebCrawler.progressBar.setString("Links parsed: " + chunks.get(chunks.size() - 1) + "/" + (allLinks.size()-1));
    }

    @Override
    protected void done() {
        Toolkit.getDefaultToolkit().beep();
        WebCrawler.progressBar.setValue(0);

    }

}
