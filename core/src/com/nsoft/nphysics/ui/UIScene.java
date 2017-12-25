package com.nsoft.nphysics.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kotcrab.vis.ui.Sizes;

public class UIScene extends Stage{

	Skin skin;
	TextButton createMode;
	TextButton moveMode;
	public UIScene() {
		
		System.out.println(Sizes.class.getName());
		System.out.println(Gdx.files.getLocalStoragePath());
		
		skin = new Skin(Gdx.files.internal("skin-files/skin/neutralizer-ui.json"));
		createMode = new TextButton("Crear Objeto",skin);
		createMode.setPosition(20, 20);
		addActor(createMode);
		
	}
}
