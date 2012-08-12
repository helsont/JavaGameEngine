package test.core;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import test.sprite.Sprite;

public class ResourceManager extends GameComponent implements Runnable {
	public SpriteBank sprites;
	public Sprite spriteLoad;
	
	//public AudioBank audios;
	//public AudioStream audioLoad;

	public List<String> loadQueue = new ArrayList<String>();
	
	public int totalNum;

	public Thread resourceThread;

	public ResourceManager(Game game) {
		super(game);

		sprites = new SpriteBank();
		spriteLoad = new Sprite();
		
		//audios = new AudioBank();
		
		resourceThread = new Thread(this, "Resource Manager");
		resourceThread.start();
		totalNum = 0;
	}

	/**
	 * @return The size of the sprite queue.
	 */
	public int getUnprocessed() {
		return loadQueue.size();
	}

	/**
	 * @return The size of the sprite hashmap.
	 */
	public int getProcessed() {
		return sprites.size(); //+ audios.size();
	}

	/**
	 * @return The number of sprites in th divided by the total number of
	 *         sprites that need to be processed
	 */
	public double getPercentComplete() {
		if (!loadQueue.isEmpty())
			return (totalNum - getUnprocessed()) / (double) totalNum;
		return 0;
	}

	public Image getSprite(String name) {
		return sprites.getSprite(name);
	}

	/*public AudioStream getAudio(String name) {
		return audios.getAudio(name);
	}*/
	public void addResource(String name, Sprite spr) {
		if (!loadQueue.contains(name) && !sprites.sprExists(name)){//&& !audios.audioExists(name)) {
			loadQueue.add(name);
			spriteLoad = spr;
		} else if (sprites.sprExists(name))
			spr.img = sprites.hm.get(name);
	}

	public void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	@Override
	public void run() {
		totalNum = loadQueue.size();
		for (int i = 0; i < loadQueue.size(); i++) {
			if (loadQueue.get(i) != null) {
				String key = loadQueue.get(i);
				spriteLoad.img = sprites.getSprite(key);
				spriteLoad.imgName = key;
				loadQueue.remove(i);
				System.out.println("Percent:" + getPercentComplete());
				sleep(1);
			}
		}

		if (loadQueue.isEmpty()) {
			totalNum = 0;
		}
		sleep(5);
	}
}