package com.util;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

public class Utilities {

	public static void desaturate(ImageView imageView){
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setSaturation(0);
		imageView.setEffect(colorAdjust);
	}

}
