import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card extends ImageView {

    private CardType type;
    private boolean showFront;
    private boolean showing;

    private Image frontImage;
    private Image backImage;

    public Card(CardType type) {
        this(type.getFrontPath(), type.getBackPath());
        this.type = type;
    }

    public Card(String frontPath, String backPath) {
        super();
        if (frontPath != null) this.frontImage = new Image(frontPath);
        if (backPath != null) this.backImage = new Image(backPath);
    }

    public CardType getType() {
        return type;
    }

    public void setCardType(CardType cardType) {
        this.type = cardType;
        this.frontImage = new Image(cardType.getFrontPath());
        this.backImage = new Image(cardType.getBackPath());
    }

    public void setCustomCardFront(String frontPath) {
        this.type = null;
        this.frontImage = new Image(frontPath);
    }

    public void setCustomCardBack(String backPath) {
        this.type = null;
        this.backImage = new Image(backPath);
    }

    public void show() {
        this.showing = true;
        if (this.showFront) setImage(frontImage);
        else setImage(backImage);
    }

    public void hide() {
        this.showing = false;
        setImage(null);
    }

    public boolean isShowFront() {
        return showFront;
    }

    public void setShowFront(boolean showFront) {
        this.showFront = showFront;
        if (this.showing) {
            if (showFront) setImage(frontImage);
            else setImage(backImage);
        }
    }
}
