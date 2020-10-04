package com.wings2d.editor.objects.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ProjectTest {
	private File directory = new File(System.getProperty("user.dir") + "/src/test/resources");
	private static File newDirectory = new File(System.getProperty("user.dir") + "/src/test/resources/test");
	
	@BeforeAll
	static void setUpAll() throws Exception {
		newDirectory.mkdir();
	}

	@AfterAll
	static void tearDown() throws Exception {
		String[]entries = newDirectory.list();
		for(String s: entries){
		    File currentFile = new File(newDirectory.getPath(),s);
		    currentFile.delete();
		}
		newDirectory.delete();
	}

	@Test
	void testReadTestFile() { // Read pre-existing file in test directory
		try {
			Project testProject = new Project(directory);
			assertEquals("TestProject", testProject.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();}
	}

	@Test
	void testCreateNewProjectFile() { 
		try {
			Project testProject = new Project(newDirectory, true, "NewProject");
			assertEquals("NewProject", testProject.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();}
	}
	@Test
	void testReadNewProjectFile() { 
		try {
			new Project(newDirectory, true, "NewProject");
			Project testProject = new Project(newDirectory);
			assertEquals("NewProject", testProject.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();}
	}
}
