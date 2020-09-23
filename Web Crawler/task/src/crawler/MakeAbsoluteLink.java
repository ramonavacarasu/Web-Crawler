package crawler;

public class MakeAbsoluteLink {

    public static void makeAbsoluteLink(String relativeLink, String url) {

        LinkType linkType = GetLinkType.getLinkType(relativeLink);

        switch (linkType) {
            case Absolute:
                // https://www.wikipedia.org/index.html
                break;

            case WithoutProtocol:
                // //en.wikipedia.org/ or en.wikipedia.org/
                if (relativeLink.startsWith("//")) {
                    if (url.startsWith("https")) {
                        relativeLink = "https:" + relativeLink;
                    } else {
                        relativeLink = "http:" + relativeLink;
                    }
                } else {
                    if (url.startsWith("https")) {
                        relativeLink = "https://" + relativeLink;
                    } else {
                        relativeLink = "http://" + relativeLink;
                    }
                }
                break;

            case Relative:
                // page.html
                if (!url.endsWith("/")) {
                    relativeLink = url.replaceAll("(?<=[^/])/[^ /]+", "") + "/" + relativeLink;
                } else {
                    relativeLink = url.replaceAll("(?<=[^/])/[^ /]+", "") + relativeLink;
                }
                break;

            default:
                relativeLink = "";
                break;
        }

    }
}
