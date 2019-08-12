package kaptainwutax.traders.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Config {
	
	private static Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	protected String root = "config/QFM-Traders/";

    public Config readConfig() throws FileNotFoundException {
        return GSON.fromJson(new FileReader(this.root + this.getLocation()), this.getClass());
    }
	
    public void writeConfig() throws IOException {
        FileWriter writer = new FileWriter(this.root + this.getLocation());      
        GSON.toJson(this, writer);       
        writer.flush();
        writer.close();
    }
    
	public void generateConfig() {
		try {
			File dir = new File(this.root);
			dir.mkdirs();
			
			File config = new File(this.root + this.getLocation());
			config.createNewFile();
			
			this.writeConfig();
		} catch(IOException e) {e.printStackTrace();}
	}

	public abstract String getLocation();
   
}
