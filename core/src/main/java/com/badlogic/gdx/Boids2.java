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
	private int numberOfBoids = 70;

/* TO DO:

Firstly, on the colour issue. Check Bookmarks for the pixmap problem.
The issue seems to be based on creating another texture region. See stackoverflow discussion.
Need to create a helper function and
work out how to generate and use multiple pixmaps formed of a region with multiple colours.


*First: Bug is in cohesion and/or seek.
1. colour one boid
2. investigate updating the delta
3. make into triangles with velocity heading
4. introduce sliders
5. Bug: circles seem to want to track to bottom left corner: why?
6. reboot with a Android phone version
7. Refactor code as per NOC git repo: Flocking package
8. Remove possible bug of duplicate WIDTH and HEIGHT variables in two classes.

 */

	@Override
	public void create() {

		//initialise ArrayList
		vehicles = new ArrayList<>();

		for(int i = 0; i<numberOfBoids; i++){
			Vehicle vehicle = new Vehicle(320, 240);
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

		batch.begin();
		for(Vehicle vehicle: vehicles){

			drawer.circle(vehicle.getX(), vehicle.getY(), vehicle.r, vehicle.lineWidth);
			//drawer.line(vehicle.getX(), vehicle.getY(), vehicle.getVelocityX(), vehicle.getVelocityY());
		}
		batch.end();
	}


	public void update(){

		for(Vehicle vehicle: vehicles){
			vehicle.separate(vehicles);
			vehicle.align(vehicles);
			vehicle.cohesion(vehicles);
			vehicle.borders();
			vehicle.update();
		}
	}


	@Override
	public void dispose() {
		batch.dispose();
		image.dispose();
	}
}
