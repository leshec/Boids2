package com.badlogic.gdx;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;


public class Vehicle extends Vector2 {
    //duplicate WIDTH/HEIGHT
    //can I pull in from the LGW context?
    private final float WIDTH = 640;
    private final float HEIGHT = 480;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;
    float maxspeed;
    float maxforce;
    float r;
    float lineWidth;
    //private ArrayList<Vehicle> vehicles;
    int debugger =0;


    public Vehicle(float x, float y) {
        position = new Vector2(MathUtils.random(1,640), MathUtils.random(1,480));
        velocity = new Vector2(MathUtils.random(-1,1),MathUtils.random(-1,1));
        acceleration = new Vector2(0,0);

        r = 5.0f;
        lineWidth = 2.0f;
        maxforce = 0.08f;
        maxspeed = 1f;
    }

    void update() {
        velocity.add(acceleration);
        velocity.limit(maxspeed);
        position.add(velocity);
        acceleration.scl(1);

    }


    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getVelocityX(){
        return velocity.x;
    }

    public float getVelocityY(){
        return velocity.y;
    }

    public float getAccelerationX(){ return acceleration.x;}
    public float getAccelerationY(){ return acceleration.y;}

    void applyForce(Vector2 force) {
        acceleration.add(force);
    }

    public void borders() {

        if (position.x < -r) position.x = WIDTH + r;
        if (position.y < -r) position.y = HEIGHT + r;
        if (position.x > WIDTH + r) position.x = -r;
        if (position.y > HEIGHT + r) position.y = -r;
    }

    public void align(ArrayList<Vehicle> vehicles) {

        float neighbordist = r * 5;
        Vector2 sum = new Vector2(0,0);
        int count = 0;
        for (Vehicle vehicle : vehicles) {
            float d = Vector2.dst(position.x, position.y,vehicle.position.x, vehicle.position.y);
            if ((d > 0f) && (d < neighbordist)) {
                sum.add(vehicle.velocity);
                count++;
            }
        }

        if (count > 0) {
            sum.scl(1 /((float) count));
            sum.nor();
            sum.scl(maxspeed);
            Vector2 steer = sum.sub(velocity);
            steer.limit((maxforce));
            this.applyForce(steer);
        }

        else {
            this.applyForce(new Vector2());
        }


    }


    public void separate(ArrayList<Vehicle> vehicles) {
        //Make this a global variable, maybe?
        float desiredSeperation = r * 3;
        Vector2 sum = new Vector2();
        int count = 0;
        for (Vehicle vehicle : vehicles) {
            //bug here
            float d = Vector2.dst(position.x, position.y,vehicle.position.x, vehicle.position.y);
            if ((d > 0f) && (d < desiredSeperation)) {
                //must be a better way
                Vector2 diff = new Vector2(position.x -vehicle.position.x,position.y-vehicle.position.y);
                diff.sub(vehicle);
                diff.nor();
                diff.scl(1/(float) d); //a hack to replace division method
                sum.add(diff);
                count +=1;
            }
        }

        if (count > 0) {
            sum.setLength2(maxspeed);
            Vector2 steer = sum.sub(velocity);
            steer.limit((maxspeed));
            this.applyForce(steer);
        }
}

    public void cohesion(ArrayList<Vehicle> vehicles) {
        float neighbordist = 60;
        Vector2 sum = new Vector2(0,0);
        int count = 0;
        for (Vehicle vehicle : vehicles) {
            //have dst2 fixed it???
            float d = Vector2.dst2(position.x, position.y,vehicle.position.x, vehicle.position.y);
            if ((d > 0f) && (d < neighbordist)) {
                //must be a better way
                sum.add(vehicle.velocity);
                count++;
            }
        }

        if (count > 0) {
            //System.out.println(sum);
            sum.scl(1 /((float) count));
            //System.out.println(sum.scl(1 /((float) count)));
            //bug?
            seek(sum);
        }

        else {
            this.applyForce(new Vector2());
        }


    }

    private void seek(Vector2 target) {
        Vector2 desired = new Vector2(target.x-position.x,target.y-position.y);
        desired.nor();
        desired.scl(maxspeed);
        Vector2 steer = desired.sub(velocity);
        steer.limit(maxforce);
        this.applyForce(steer);
    }


//end of class
}

    








