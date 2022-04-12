package com.badlogic.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Boids2 extends ApplicationAdapter {
	//note  from Lwjgl3Launcher in build: configuration.setWindowedMode(640, 480);
	//shall I remove these duplicated: TO DO check use
	private final float WIDTH = 640;
	private final float HEIGHT = 480;
	private SpriteBatch batch;
	private Texture image;
	private ShapeDrawer drawer;
	private OrthographicCamera camera;
	private Vector2 motion1;
	private Vector2 motion2;
	private Vector2 motion3;
	private Vector2 motion4;
	private Vehicle vehicle;
	private ArrayList<Vehicle> vehicles;
	private int numberOfBoids =30;

//TO DO contribute to lib gdx vector methods are lacking

	@Override
	public void create() {

		//initialise ArrayList
		vehicles = new ArrayList<Vehicle>();

		for(int i = 0; i<numberOfBoids; i++){
			Vehicle vehicle = new Vehicle(MathUtils.random(0, WIDTH), MathUtils.random(0,HEIGHT));
			vehicles.add(vehicle);
		}

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.drawPixel(0, 0);
		Texture texture = new Texture(pixmap); //remember to dispose of later
		pixmap.dispose(); //
		TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		//note  from Lwjgl3Launcher in build: configuration.setWindowedMode(640, 480);
		camera.setToOrtho(false, 640, 480);
		batch = new SpriteBatch();
		//image = new Texture("libgdx.png");
		drawer = new ShapeDrawer(batch, region);

		//stock vectors for testing
		motion1 = new Vector2(1, 0);
		motion2 = new Vector2(0,1);
		motion3 = new Vector2(-1, 0);
		motion4 = new Vector2(0,-1);




	}

	@Override
	public void render() {
		//note  from Lwjgl3Launcher in build: configuration.setWindowedMode(640, 480);
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		draw();
		update();

	}

	public void draw(){

		//drawer.circle(5,5,5,2);

		for(Vehicle vehicle: vehicles){
			batch.begin();
			drawer.circle(vehicle.getX(), vehicle.getY(), vehicle.r, vehicle.lineWidth);
			batch.end();
		}
	}


	public void update(){

		for(Vehicle vehicle: vehicles){
			vehicle.separate(vehicles);
			vehicle.align(vehicles);
			vehicle.cohesion(vehicles);
			vehicle.update();
			vehicle.borders();
		}
	}


	@Override
	public void dispose() {
		batch.dispose();
		image.dispose();
	}
}
