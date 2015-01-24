package com.nedzhang.skunktool3.control;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

/**
 * <p>
 * TextField with regex-based real-time input validation. JavaFX 2 and FXML
 * compatible.
 * </p>
 * <p>
 * FXML code example:<div>
 * {@code <ValidatedTextField fx:id="validatedTextField" minLength="1" maxLength="1" mask="^[0-9]*$" />}
 * </div>
 * </p>
 * 
 * @author 82300009
 */
public final class ValidatedTextField extends TextField {

	private final BooleanProperty invalid = new SimpleBooleanProperty(false);
	private final StringProperty mask;
	private final IntegerProperty minLength;
	private final IntegerProperty maxLength;

	private Effect invalidEffect = new DropShadow(BlurType.GAUSSIAN, Color.RED,
			4, 0.0, 0, 0);

	public ValidatedTextField() {
		super();
		mask = new SimpleStringProperty(".");
		minLength = new SimpleIntegerProperty(-1);
		maxLength = new SimpleIntegerProperty(-1);

		bind();
	}

	public ValidatedTextField(final String mask, final int minLength,
			final int maxLength, final boolean nullable) {
		this(mask, minLength, maxLength, nullable, null);
	}

	public ValidatedTextField(final String mask, final int minLength,
			final int maxLength, final boolean nullable, final String string) {
		super(string);
		this.mask = new SimpleStringProperty(mask);
		this.minLength = new SimpleIntegerProperty(minLength);
		this.maxLength = new SimpleIntegerProperty(maxLength);

		bind();
	}

	public ReadOnlyBooleanProperty invalidProperty() {
		return invalid;
	}

	public ReadOnlyStringProperty maskProperty() {
		return mask;
	}

	public ReadOnlyIntegerProperty minLengthProperty() {
		return minLength;
	}

	public ReadOnlyIntegerProperty maxLengthProperty() {
		return maxLength;
	}

	public boolean getInvalid() {
		return invalid.get();
	}

	public String getMask() {
		return mask.get();
	}

	public void setMask(final String mask) {
		this.mask.set(mask);
	}

	public int getMinLength() {
		return minLength.get();
	}

	public void setMinLength(final int minLength) {
		this.minLength.set(minLength);
	}

	public int getMaxLength() {
		return maxLength.get();
	}

	public void setMaxLength(final int maxLength) {
		this.maxLength.set(maxLength);
	}

	public Effect getInvalidEffect() {
		return invalidEffect;
	}

	public void setInvalidEffect(final Effect effect) {
		invalidEffect = effect;
	}

	private void bind() {
		invalid.bind(maskCheck().or(minLengthCheck()));

		textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov,
					final String t, final String t1) {
				if (textProperty().get().length() > maxLength.get()) {
					setText(t);
				}
			}
		});

		invalid.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(final ObservableValue<? extends Boolean> ov,
					final Boolean t, final Boolean t1) {
				if (t ^ t1) {
					if (t1) {
						// setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
						setEffect(invalidEffect);
					} else {
						// setStyle("-fx-font-weight: normal; -fx-text-fill: inherit;");
						setEffect(null);
					}
				}

			}
		});
	}

	private BooleanBinding maskCheck() {
		return new BooleanBinding() {
			{
				super.bind(textProperty(), mask);
			}

			@Override
			protected boolean computeValue() {

				return !checkMatch(textProperty().get(), getMask());

			}
		};
	}

	private BooleanBinding minLengthCheck() {
		return new BooleanBinding() {
			{
				super.bind(textProperty(), minLength);
			}

			@Override
			protected boolean computeValue() {
				return getStringLength(textProperty().get()) < minLength.get();
			}
		};
	}

//	private BooleanBinding maxLengthCheck() {
//		return new BooleanBinding() {
//			{
//				super.bind(textProperty(), maxLength);
//			}
//
//			@Override
//			protected boolean computeValue() {
//				return getStringLength(textProperty().get()) > maxLength.get();
//			}
//		};
//	}

	private boolean checkMatch(final String stringToMatch,
			final String regularExpression) {

		return stringToMatch == null ? false : stringToMatch
				.matches(regularExpression);
	}

	private int getStringLength(final String stringToMeasure) {
		return stringToMeasure == null ? 0 : stringToMeasure.length();
	}
}