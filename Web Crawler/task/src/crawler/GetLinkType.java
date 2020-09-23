package crawler;

enum LinkType {
    Absolute,
    Relative,
    WithoutProtocol
}

public class GetLinkType {

    public static LinkType getLinkType(String initialLink) {

        if (initialLink.startsWith("http")) {
            return LinkType.Absolute;

        } else if (!initialLink.contains("/")) {
            return LinkType.Relative;

        } else {
            return LinkType.WithoutProtocol;
        }
    }
}
