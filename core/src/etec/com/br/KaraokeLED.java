package etec.com.br;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class KaraokeLED extends Table {

    private boolean colorMode;

    private Label _label;
    private Label.LabelStyle _labelStyle;

    private TextField _textField;
    private TextField.TextFieldStyle _textFieldStyle;

    private static final Color unmarked = Color.WHITE;
    private static final Color marked = Color.RED;
    private static final String unmarkedName = "WHITE";
    private static final String markedName = "RED";

    public KaraokeLED(String messageText, KaraokeLEDStyle style) {
        super();
        //setDebug(true);

        _labelStyle = new Label.LabelStyle();
        _labelStyle.font = style.font;
        _labelStyle.fontColor = unmarked;
        _label = new Label("", _labelStyle);
        _label.setAlignment(Align.center);

        _textFieldStyle = style;
        _textField = new TextField("", _textFieldStyle);
        _textField.setMessageText(messageText);
        _textField.setWidth(80f);
        _textField.setAlignment(Align.center);

        setHeight(320f);
        setWidth(1040f);

        invalidateHierarchy();
        setColorMode(false);
    }

    public String getText() {
        return _textField.getText();
    }

    public void setPosition (float x, float y) {
        super.setPosition(x,y);
    }

    public void focus() {
        Stage stage = getStage();
        if(stage != null) {
            getStage().setKeyboardFocus(_textField);
            _textField.getOnscreenKeyboard().show(true);
        }
    }

    public void setColorMode(boolean color) {
        colorMode = color;

        clearChildren();

        if(color) {
            row().width(800f).height(320f).center();
            _label.setColor(unmarked);
            _label.setText(_textField.getText());
            add(_label);
        }
        else {
            row().width(800f).height(320f).center();
            add(_textField);
        }
    }

    public void color(int numOfChar) {

        String text = _textField.getText();

        String pre = text.substring(0, numOfChar);
        String post = text.substring(numOfChar);

        _label.setText("[" + markedName + "]" + pre + "[" + unmarkedName + "]" + post);
    }

    public void act (float delta) {
        super.act(delta);
    }

    public void draw(Batch batch, float parentAlpha) {
        validate();

        super.draw(batch, parentAlpha);
    }

    public void setKaraokeLEDListener (TextField.TextFieldListener listener) {
        _textField.setTextFieldListener(listener);
    }

    public void setKaraokeLEDFilter (TextField.TextFieldFilter filter) {
        _textField.setTextFieldFilter(filter);
    }

    static public class KaraokeLEDStyle extends TextField.TextFieldStyle {

        public KaraokeLEDStyle(){
            super();
        }

        public KaraokeLEDStyle (BitmapFont font, Color fontColor, Drawable cursor, Drawable selection, Drawable background) {
            super(font, fontColor, cursor, selection, background);
        }

        public KaraokeLEDStyle (TextField.TextFieldStyle style) {
            super(style);
        }
    }
}
