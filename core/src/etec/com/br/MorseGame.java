package etec.com.br;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MorseGame extends ApplicationAdapter {
	private static final int SCENE_WIDTH = 1080;
	private static final int SCENE_HEIGHT = 1920;

	private SpriteBatch batch;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background, buttonTexture, buttonPressTexture, cursorTexture;
	private Button button;
	private int coloredCharacters;
	private BitmapFont font;

	private CameraResolver camResolver;

	private KaraokeLED karaokeLED;
	private Stage stage;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		viewport.update(SCENE_WIDTH, SCENE_HEIGHT, true);

		stage = new Stage(viewport, batch);
		Gdx.input.setInputProcessor(stage);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Gidole-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 100;
		font = generator.generateFont(parameter);
		generator.dispose();

		//font.setMarkupEnabled(true);

		background = new Texture("fondo.png");
		buttonTexture = new Texture("boton.png");
		buttonPressTexture = new Texture("boton_pulsado.png");
		cursorTexture = new Texture("cursor.png");

		Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));
		buttonStyle.over = new TextureRegionDrawable(new TextureRegion(buttonPressTexture));
		button = new Button(buttonStyle);
		button.setTransform(true);
		button.setSize(800f, 800f);
		button.setOrigin(400f, 400f);
		button.setPosition((SCENE_WIDTH >> 1) - (button.getWidth() * .5f), 300f);

		stage.addActor(button);

		KaraokeLED.KaraokeLEDStyle kls = new KaraokeLED.KaraokeLEDStyle();
		kls.font = font;
		kls.fontColor = Color.BLACK;
		kls.cursor = new TextureRegionDrawable(new TextureRegion(cursorTexture));
		karaokeLED = new KaraokeLED("", kls);
		karaokeLED.setPosition(20.5f, 1470);
		stage.addActor(karaokeLED);
		karaokeLED.focus();

		karaokeLED.setKaraokeLEDFilter(new TextField.TextFieldFilter() {
			// Accepts a-z characters
			@Override
			public  boolean acceptChar(TextField textField, char c) {
				c = Character.toLowerCase(c);
				if (c >= 'a' && c <= 'z')
					return true;
				return false;
			}
		});

		button.addListener( new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
				if(!camResolver.isProccessing()) {
					coloredCharacters = 0;
					flashText(karaokeLED.getText());
				}

				return true;
			};
		});

	}

	@Override
	public void dispose () {
		camResolver.dispose();
		background.dispose();
		buttonTexture.dispose();
		buttonPressTexture.dispose();
		cursorTexture.dispose();
		font.dispose();
		stage.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float delta = Gdx.graphics.getDeltaTime();

		camResolver.update(delta);
		stage.act(delta);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0, SCENE_WIDTH, SCENE_HEIGHT);
		batch.end();

		stage.draw();
	}

	private void flashText(String text) {

		if(!text.isEmpty()) {

			karaokeLED.setColorMode(true);

			text = text.toLowerCase();
			for(int i=0; i<text.length(); i++) {
				String letter;
				if(text.charAt(i) == 'c' && i+1 < text.length() && text.charAt(i+1) == 'h') {
					letter = "ch";
					i++;
				}
				else
					letter = "" + text.charAt(i);

				camResolver.queueLetter(letter);
			}
		}
	}

	public void colorLetter() {

		String text = karaokeLED.getText();

		if(coloredCharacters < text.length()) {
			// Color 2 if 'ch' character
			if(text.charAt(coloredCharacters) == 'c' && coloredCharacters < text.length()-1 &&
					text.charAt(coloredCharacters+1) == 'h')
				coloredCharacters +=2;
			else
				coloredCharacters++;

			karaokeLED.color(coloredCharacters);
		}

		if(coloredCharacters == text.length())
			karaokeLED.setColorMode(false);
	}

	public void setCameraResolver(CameraResolver cameraResolver) {
		camResolver = cameraResolver;
	}

	@Override
	public void pause () {
		camResolver.releaseCamera();
	}

	@Override
	public void resume () {
		camResolver.connectToCamera();
	}
}
