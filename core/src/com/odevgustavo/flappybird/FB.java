package com.odevgustavo.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FB extends ApplicationAdapter {

	float birdX = 60, birdY =  0;
	private float birdAnimation = 0;
	private SpriteBatch spriteBatch;
	private Texture background, bird[];
	private Texture upPipes, downPipes;
	private Texture gameOver;

	private float pipeMovement = 0;
	private float pipePositionVertical = 0;
	private float spaceBetweenPipes  = 300;
	private Random random;
	float gravity = 0;

	//Game Status
	private int gameState = 0;


	private Circle circleBird;
	private Rectangle rectanglePipeUp, rectanglePipeDown;
	private ShapeRenderer shapeRenderer;

	//Text
	BitmapFont score;
	int scoreValue = 0;

	BitmapFont textRestart;
	BitmapFont bestScore;

	boolean pipeHasPass = false;

	//Settings
	private float getWidth;
	private float getHeight;
	
	@Override
	public void create () {
		Gdx.app.log("Create", "Initiated");
		initiateObjects();
		initiateTexture();

	}

	@Override
	public void render () {
		drawTexture();
		verifyGameStatus();
		validateScore();
		collisionDetection();
	}
	private void verifyGameStatus(){

		//Game Status
		if( gameState == 0 ){
			//Applying gravity on bird
			if(Gdx.input.justTouched()){
				gravity = -15;
				gameState = 1;
			}
		}else if( gameState == 1 ){
			//Touching the screen to move the bird
			if(Gdx.input.justTouched()){
				gravity = -15;
			}
			//Movement and restart of pipe on screen
			pipeMovement -= Gdx.graphics.getDeltaTime()*500;
			if (pipeMovement<-upPipes.getWidth()){
				pipeMovement = getWidth;
				pipePositionVertical = random.nextInt(1900)-1000;
				pipeHasPass = false;
			}

			birdY = birdY - gravity;

			if(birdY<=0){
				birdY+=0;
			}

			Gdx.app.log("Height", String.valueOf(getHeight));


			gravity++;

		}else if( gameState ==2 ){

		}











	}
	private void initiateObjects(){
		//Essencial
		spriteBatch = new SpriteBatch();
		random = new Random();
		//Width and Screen Height
		getWidth = Gdx.graphics.getWidth();
		getHeight = Gdx.graphics.getHeight();
		birdY = getHeight/2;
		//Moviment of pipes
		pipeMovement = getWidth;
		//Score layout on screen
		score = new BitmapFont();
		score.setColor(Color.BLUE);
		score.getData().setScale(8);
		//Text to Restart
		textRestart = new BitmapFont();
		textRestart.setColor(Color.BLUE);
		textRestart.getData().setScale(4);
		//Best Score Layout on Screen
		bestScore = new BitmapFont();
		bestScore.setColor(Color.BLACK);
		bestScore.getData().setScale(4);
		//Geometric and collision
		shapeRenderer = new ShapeRenderer();
		circleBird = new Circle();
		rectanglePipeUp = new Rectangle();
		rectanglePipeDown = new Rectangle();




	}
	private void initiateTexture(){
		background = new Texture("fundo.png");
		bird = new Texture[3];
		bird[0] = new Texture("passaro1.png");
		bird[1] = new Texture("passaro2.png");
		bird[2] = new Texture("passaro3.png");


		gameOver = new Texture("game_over.png");

		upPipes = new Texture("cano_topo_maior.png");
		downPipes = new Texture("cano_baixo_maior.png");

	}

	private void drawTexture(){
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0, getWidth , getHeight);
		spriteBatch.draw(bird[ (int) birdAnimation], birdX, birdY);
		spriteBatch.draw(upPipes, pipeMovement, (float) (getHeight/2 + pipePositionVertical + spaceBetweenPipes/2));
		spriteBatch.draw(downPipes, pipeMovement, (float) (getHeight/2 - downPipes.getHeight()  + pipePositionVertical -spaceBetweenPipes/2));
		score.draw(spriteBatch, String.valueOf(scoreValue), getWidth/2-50, getHeight/2+800);

		if(gameState==2){
			spriteBatch.draw(gameOver, getWidth/2 - gameOver.getWidth()/2,  getHeight/2);
			textRestart.draw(spriteBatch, "Touch to Restart.", getWidth/2  - 140, getHeight/2 - gameOver.getHeight()/2);
			bestScore.draw(spriteBatch, "Your Record is: "+ String.valueOf(bestScore), getWidth/2 - gameOver.getWidth()/2, getHeight/2 - gameOver.getHeight());
		}
		spriteBatch.end();

	}

	private void validateScore(){
		if(pipeMovement<60 - bird[0].getWidth()) {
			if(!pipeHasPass){
				scoreValue++;
				pipeHasPass = true;
			}
		}

		birdAnimation+= Gdx.graphics.getDeltaTime()*10;

		//Bird Animation Exec
		if(birdAnimation>3) birdAnimation = 0;
	}

	private void collisionDetection(){
		circleBird.set(birdX+bird[0].getWidth()/2,birdY+bird[0].getHeight()/2,bird[0].getWidth()/2);
		rectanglePipeDown.set(pipeMovement, getHeight/2 - downPipes.getHeight() + pipePositionVertical -spaceBetweenPipes/2, downPipes.getWidth()+10, downPipes.getHeight());
		rectanglePipeUp.set(pipeMovement, (float) (getHeight/2 + pipePositionVertical + spaceBetweenPipes/2),upPipes.getWidth(), upPipes.getHeight());
		if(Intersector.overlaps(circleBird,rectanglePipeDown) || Intersector.overlaps(circleBird, rectanglePipeUp)){
			Gdx.app.log("Colision", "The bird has collided on the rectangle");
			gameState=2;
		}

		/*
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.circle(birdX+bird[0].getWidth()/2,birdY+bird[0].getHeight()/2,bird[0].getWidth()/2);
		shapeRenderer.rect(pipeMovement, (float) (getHeight/2 + pipePositionVertical + spaceBetweenPipes/2),upPipes.getWidth(), upPipes.getHeight());
		shapeRenderer.rect(pipeMovement, getHeight/2 - downPipes.getHeight() + pipePositionVertical -spaceBetweenPipes/2, downPipes.getWidth()+10, downPipes.getHeight());
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.end();
		 */
	}
	@Override
	public void dispose () {
		Gdx.app.log("Dispose", "Disposed");
	}
}
