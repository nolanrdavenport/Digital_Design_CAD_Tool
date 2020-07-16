/* 		
 * This class manages file IO, such as opening and reading to schematic files.
 *  
 *  	This program is a purely educational digital design CAD tool used for people to quickly sketch up digital systems designs.
 *      Copyright (C) 2020  Nolan Davenport
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import components.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileManager{	
	public ArrayList<File> files;
	public File currFile;
	public Stage primaryStage;
	public Main main;
	
	
	public FileManager(Main main, Stage stage) {
		// TODO: setup up file manager
		this.main = main;
		this.primaryStage = stage;
		files = new ArrayList<File>();
		
	}
	
	//TODO: Finish this method
	public void openSchematicFile() {
		try {
			// Fetch the file from the user's file system. 
			FileChooser fc = new FileChooser();
			fc.setTitle("Open a schematic file");
			fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\Documents\\Digital Design CAD Tool\\Schematics\\"));
			fc.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Schematic Files", "*.sch") ); // Only .sch files can be opened.
			File tempFile = fc.showOpenDialog(primaryStage);		
			files.add(tempFile);
			currFile = tempFile;
			
			// Pull the schematic information
			BufferedReader metadataReader = new BufferedReader(new FileReader(new File(System.getProperty("user.home") + "\\Documents\\Digital Design CAD Tool\\Schematics\\.metadata\\"+currFile.getName().substring(0, currFile.getName().length() - 4)+".metadata")));
			int width = Integer.parseInt(metadataReader.readLine());
			int height = Integer.parseInt(metadataReader.readLine());
			int numComponents = Integer.parseInt(metadataReader.readLine());
			int numIOPorts = Integer.parseInt(metadataReader.readLine());
			int numWires = Integer.parseInt(metadataReader.readLine());
			metadataReader.close();
			
			main.createSchematic(currFile.getName().substring(0, currFile.getName().length() - 4), width, height);
			
			FileInputStream fis = new FileInputStream(tempFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			// TODO: make inputs and outputs work with file system.
			for(int i = 0; i < numComponents; i++) {
				SerializableComponent temp = (SerializableComponent)ois.readObject();
				main.currSchematic.currState.components.add(temp.getDeserializedComponent());
			}
			
			for(int i = 0; i < numIOPorts; i++) {
				SerializableIOPort temp = (SerializableIOPort)ois.readObject();
				main.currSchematic.currState.components.add(temp.getDeserializedIOPort());
			}
			
			for(int i = 0; i < numWires; i++) {
				SerializableWire temp = (SerializableWire)ois.readObject();
				main.currSchematic.currState.wires.add(temp.getDeserializedWire(main.currSchematic.currState.components));
			}
			
			main.currSchematic.refresh(true);
			
			fis.close();
			ois.close();
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveSchematicFileAs() {
		// Let the user choose or create a new save file in the users file system.
		FileChooser fc = new FileChooser();
		fc.setTitle("Save your schematic");
		fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\Documents\\Digital Design CAD Tool\\Schematics\\"));
		fc.setInitialFileName(main.currSchematic.name+".sch");
		fc.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Schematic Files", "*.sch") ); // Only .sch files can be opened.
		currFile = fc.showSaveDialog(primaryStage);
		if(!files.contains(currFile)) {
			files.add(currFile);
		}
		saveSchematicFile();
	}

	public void saveSchematicFile() {
		try {
			// Write the meta data for the file into the metadata folder.
			new FileOutputStream(new File(System.getProperty("user.home") + "\\Documents\\Digital Design CAD Tool\\Schematics\\.metadata\\"+currFile.getName().substring(0, currFile.getName().length() - 4)+".metadata")).close(); // clears metadata file
			BufferedWriter metaDataWriter = new BufferedWriter(new FileWriter(System.getProperty("user.home") + "\\Documents\\Digital Design CAD Tool\\Schematics\\.metadata\\"+currFile.getName().substring(0, currFile.getName().length() - 4)+".metadata"));
			metaDataWriter.write(Integer.toString(main.currSchematic.width)); // width
			metaDataWriter.newLine();
			metaDataWriter.write(Integer.toString(main.currSchematic.height)); // height
			metaDataWriter.newLine();
			// Calculate num components and num IO ports separately.
			int compSize = 0;
			int ioSize = 0;
			for(Component comp : main.currSchematic.currState.components) {
				if(!comp.id.contains("IO")) {
					compSize++;
				}else {
					ioSize++;
				}
			}
			metaDataWriter.write(Integer.toString(compSize)); // number of components
			metaDataWriter.newLine();
			metaDataWriter.write(Integer.toString(ioSize)); // number of IOPorts
			metaDataWriter.newLine();
			metaDataWriter.write(Integer.toString(main.currSchematic.currState.wires.size())); // number of wires
			metaDataWriter.close();
			
			// Save the components and wires into the actual .sch file. Have to make serializable forms because JavaFX dependencies aren't serializable. 
			new FileOutputStream(currFile).close(); // this just clears the file so that we can re-copy all the objects over to it. 	
			FileOutputStream fos = new FileOutputStream(currFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			for(Component comp : main.currSchematic.currState.components) {
				if(!comp.id.contains("IO")) {
					oos.writeObject(comp.getSerializableComponent());
				}
			}
			for(Component comp : main.currSchematic.currState.components) {
				if(comp.id.contains("IO")) {
					oos.writeObject(((IOPort) comp).getSerializableIOPort());
				}
			}
			for(Wire wire : main.currSchematic.currState.wires) {
				oos.writeObject(wire.getSerializableWire());
			}
			
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
