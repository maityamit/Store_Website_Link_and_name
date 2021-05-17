package admin.example.satyadummy;

public class Website {

    private String Text,Link;

    public Website()
    {

    }

    public Website(String TEXT, String LINK) {
        this.Text = TEXT;
        this.Link = LINK;



    }


    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
